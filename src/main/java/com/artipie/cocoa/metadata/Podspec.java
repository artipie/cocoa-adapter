/*
 * The MIT License (MIT) Copyright (c) 2020-2022 artipie.com
 * https://github.com/cocoa-adapter/artipie/LICENSE.txt
 */
package com.artipie.cocoa.metadata;

import com.artipie.asto.misc.UncheckedIOScalar;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonValue;
import org.apache.commons.io.IOUtils;

/**
 * Cocoa PODSPEC metadata.
 * <a href="https://guides.cocoapods.org/syntax/podspec.html#specification">Docs</a>.
 * @since 0.1
 */
public interface Podspec {

    /**
     * Package name.
     * @return The name
     */
    String name();

    /**
     * Package version.
     * @return String version
     */
    String version();

    /**
     * Authors list with optional emails. Check the
     * <a href="https://guides.cocoapods.org/syntax/podspec.html#authors">docs</a>
     * for more details about format.
     * @return Authors and emails if present
     */
    Map<String, Optional<String>> authors();

    /**
     * Package version.
     * @return String version
     */
    String license();

    /**
     * Homepage URL.
     * @return The URI
     */
    URI homepage();

    /**
     * Packages source, type and value map. Check the
     * <a href="https://guides.cocoapods.org/syntax/podspec.html#source">docs</a>
     * for more details about the format.
     * @return Map of the sources
     */
    Map<String, String> source();

    /**
     * Package summary.
     * @return String summary
     */
    String summary();

    /**
     * Create {@link Podspec} instance from podspec file input stream.
     * @param input Podspec file input stream
     * @return Instance of {@link  Podspec}
     * @throws IOException On error
     */
    @SuppressWarnings("PMD.ProhibitPublicStaticMethods")
    static Podspec initiate(final InputStream input) throws IOException {
        final String data = IOUtils.toString(input, StandardCharsets.UTF_8);
        Podspec res;
        try {
            res = new Json(javax.json.Json.createReader(new StringReader(data)).readObject());
        } catch (final JsonException err) {
            res = new Ruby(data);
        }
        return res;
    }

    /**
     * Podspec in ruby format.
     * @since 0.1
     */
    class Ruby implements Podspec {

        /**
         * Ruby source.
         */
        private final String data;

        /**
         * Ctor.
         * @param data Ruby source
         */
        public Ruby(final String data) {
            this.data = data;
        }

        /**
         * Ctor.
         * @param input Input stream with ruby podspec
         */
        public Ruby(final InputStream input) {
            this(
                new UncheckedIOScalar<>(() -> IOUtils.toString(input, StandardCharsets.UTF_8))
                    .value()
            );
        }

        @Override
        public String name() {
            return this.getStringValue(".name");
        }

        @Override
        public String version() {
            return this.getStringValue(".version");
        }

        @Override
        public Map<String, Optional<String>> authors() {
            final String param = ".authors";
            final int start = this.data.indexOf(param) + param.length();
            final String val = this.data.substring(start, this.data.indexOf("\n", start));
            final Map<String, Optional<String>> res = new HashMap<>();
            if (val.contains("{")) {
                final String[] items = this.data.substring(start, this.data.indexOf("}", start))
                    .split(",");
                for (final String item : items) {
                    final String[] author = item.replaceAll("'\"", "").split("=>");
                    res.put(Ruby.clean(author[0]), Optional.of(Ruby.clean(author[1])));
                }
            } else if (val.contains(",")) {
                Arrays.stream(Ruby.clean(val).split(",")).forEach(
                    item -> res.put(item, Optional.empty())
                );
            } else {
                res.put(this.getStringValue(param), Optional.empty());
            }
            return res;
        }

        @Override
        public String license() {
            return this.getStringValue(".license");
        }

        @Override
        public URI homepage() {
            return URI.create(this.getStringValue(".homepage"));
        }

        @Override
        public Map<String, String> source() {
            final String param = ".source";
            final int start = this.data.indexOf(param) + param.length();
            final String val = this.data.substring(start, this.data.indexOf("}", start))
                .replaceAll("[=\\s*{}'\"]", "");
            final String[] items = val.split(",");
            final Map<String, String> res = new HashMap<>();
            for (final String item : items) {
                final String[] src = item.split(">");
                res.put(src[0].replaceAll(":", ""), src[1]);
            }
            return res;
        }

        @Override
        public String summary() {
            return this.getStringValue(".summary");
        }

        /**
         * Get string type value by its key.
         * @param key The key
         * @return String value
         */
        private String getStringValue(final String key) {
            return this.data.substring(
                this.data.indexOf(key) + key.length(),
                this.data.indexOf("\n", this.data.indexOf(key))
            ).replaceAll("\\s*=\\s*['\"]", "").replaceAll("['\"]\\s*", "");
        }

        /**
         * Clean the value.
         * @param val The value to clean
         * @return Value without extra spaces, =, {, } and quotes
         */
        private static String clean(final String val) {
            return val.replaceAll("\\s*=?\\s*\\{?\\s*['\"]\\s*", "");
        }
    }

    /**
     * Podspec in json format.
     * @since 0.1
     */
    final class Json implements Podspec {

        /**
         * Podspec data as json object.
         */
        private final JsonObject data;

        /**
         * Ctor.
         * @param data Json podspec data
         */
        public Json(final JsonObject data) {
            this.data = data;
        }

        /**
         * Ctor from spec string.
         * @param data Json podspec string data
         */
        public Json(final String data) {
            this(javax.json.Json.createReader(new StringReader(data)).readObject());
        }

        /**
         * Ctor from spec input stream.
         * @param data Json podspec input stream
         */
        public Json(final InputStream data) {
            this(javax.json.Json.createReader(data).readObject());
        }

        @Override
        public String name() {
            return this.data.getString("name");
        }

        @Override
        public String version() {
            return this.data.getString("version");
        }

        @Override
        public Map<String, Optional<String>> authors() {
            final JsonValue val = this.data.get("authors");
            final Map<String, Optional<String>> res = new HashMap<>(1);
            if (val.getValueType() == JsonValue.ValueType.STRING) {
                res.put(Json.removeQuotes(val), Optional.empty());
            } else if (val.getValueType() == JsonValue.ValueType.ARRAY) {
                val.asJsonArray()
                    .forEach(item -> res.put(Json.removeQuotes(item), Optional.empty()));
            } else {
                val.asJsonObject().forEach(
                    (key, value) -> res.put(key, Optional.of(Json.removeQuotes(value)))
                );
            }
            return res;
        }

        @Override
        public String license() {
            return this.data.getString("license");
        }

        @Override
        public URI homepage() {
            return URI.create(this.data.getString("homepage"));
        }

        @Override
        public Map<String, String> source() {
            final Map<String, String> res = new HashMap<>();
            this.data.getJsonObject("source").forEach(
                (key, val) -> res.put(key, Json.removeQuotes(val))
            );
            return res;
        }

        @Override
        public String summary() {
            return this.data.getString("summary");
        }

        /**
         * Method {@link JsonValue#toString()} do not removes value quotes, so
         * we do it manually.
         * @param val String json value
         * @return The value without quotes
         */
        private static String removeQuotes(final JsonValue val) {
            return val.toString().replaceAll("\"", "");
        }
    }
}
