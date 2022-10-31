/*
 * The MIT License (MIT) Copyright (c) 2020-2022 artipie.com
 * https://github.com/cocoa-adapter/artipie/LICENSE.txt
 */
package com.artipie.cocoa.metadata;

import com.artipie.asto.test.TestResource;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;
import org.cactoos.map.MapEntry;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link  Podspec.Json}.
 * @since 0.1
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class PodspecJsonTest {

    /**
     * Test json spec.
     */
    private static final JsonObject EXAMPLE =
        Json.createReader(new TestResource("example.spec.json").asInputStream()).readObject();

    @Test
    void readsName() {
        MatcherAssert.assertThat(
            new Podspec.Json(PodspecJsonTest.EXAMPLE).name(),
            new IsEqual<>("BatteryHandlerSpec")
        );
    }

    @Test
    void readsSummary() {
        MatcherAssert.assertThat(
            new Podspec.Json(PodspecJsonTest.EXAMPLE).summary(),
            new IsEqual<>("A suummary to help")
        );
    }

    @Test
    void readsHomepage() {
        MatcherAssert.assertThat(
            new Podspec.Json(PodspecJsonTest.EXAMPLE).homepage().toString(),
            new IsEqual<>("https://github.com/MohamedJaffer-24/BatteryHandler")
        );
    }

    @Test
    void readsLicense() {
        MatcherAssert.assertThat(
            new Podspec.Json(PodspecJsonTest.EXAMPLE).license(),
            new IsEqual<>("MIT")
        );
    }

    @Test
    void readsVersion() {
        MatcherAssert.assertThat(
            new Podspec.Json(PodspecJsonTest.EXAMPLE).version(),
            new IsEqual<>("0.0.2")
        );
    }

    @Test
    void readsSource() {
        MatcherAssert.assertThat(
            new Podspec.Json(PodspecJsonTest.EXAMPLE).source().entrySet(),
            Matchers.containsInAnyOrder(
                new MapEntry<>("git", "https://github.com/MohamedJaffer-24/BatteryHandler.git"),
                new MapEntry<>("tag", "0.0.2")
            )
        );
    }

    @Test
    void readsAuthors() {
        MatcherAssert.assertThat(
            new Podspec.Json(PodspecJsonTest.EXAMPLE).authors().entrySet(),
            Matchers.containsInAnyOrder(
                new MapEntry<>("Jaffer", Optional.of("jaffer.s@zohocorp.com"))
            )
        );
    }

    @Test
    void readsAuthorsList() {
        MatcherAssert.assertThat(
            new Podspec.Json(
                Json.createObjectBuilder().add(
                    "authors", Json.createArrayBuilder().add("Ann").add("Jacob").build()
                ).build()
            ).authors().entrySet(),
            Matchers.containsInAnyOrder(
                new MapEntry<>("Ann", Optional.empty()),
                new MapEntry<>("Jacob", Optional.empty())
            )
        );
    }

    @Test
    void readsSingleAuthor() {
        MatcherAssert.assertThat(
            new Podspec.Json(
                Json.createObjectBuilder().add("authors", "Mark").build()
            ).authors().entrySet(),
            Matchers.containsInAnyOrder(
                new MapEntry<>("Mark", Optional.empty())
            )
        );
    }
}
