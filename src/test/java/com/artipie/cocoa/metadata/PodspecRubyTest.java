/*
 * The MIT License (MIT) Copyright (c) 2020-2022 artipie.com
 * https://github.com/cocoa-adapter/artipie/LICENSE.txt
 */
package com.artipie.cocoa.metadata;

import com.artipie.asto.test.TestResource;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.cactoos.map.MapEntry;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link Podspec.Ruby}.
 * @since 0.1
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class PodspecRubyTest {

    /**
     * Test Ruby spec.
     */
    private static final String EXAMPLE =
        new String(new TestResource("example.spec").asBytes(), StandardCharsets.UTF_8);

    @Test
    void readsName() {
        MatcherAssert.assertThat(
            new Podspec.Ruby(PodspecRubyTest.EXAMPLE).name(),
            new IsEqual<>("Reachability")
        );
    }

    @Test
    void readsSummary() {
        MatcherAssert.assertThat(
            new Podspec.Ruby(PodspecRubyTest.EXAMPLE).summary(),
            new IsEqual<>("ARC and GCD Compatible Reachability Class for iOS and macOS.")
        );
    }

    @Test
    void readsLicence() {
        MatcherAssert.assertThat(
            new Podspec.Ruby(PodspecRubyTest.EXAMPLE).license(),
            new IsEqual<>("BSD")
        );
    }

    @Test
    void readsVersion() {
        MatcherAssert.assertThat(
            new Podspec.Ruby(PodspecRubyTest.EXAMPLE).version(),
            new IsEqual<>("3.1.0")
        );
    }

    @Test
    void readsAuthors() {
        MatcherAssert.assertThat(
            new Podspec.Ruby(PodspecRubyTest.EXAMPLE).authors().entrySet(),
            Matchers.containsInAnyOrder(
                new MapEntry<>("Tony Million", Optional.of("tonymillion@gmail.com"))
            )
        );
    }

    @Test
    void readsSeveralAuthorsAndEmails() {
        MatcherAssert.assertThat(
            new Podspec.Ruby(
                String.join(
                    "\n",
                    "spec.authors = { 'Darth Vader' => 'darthvader@darkside.com',",
                    "                 'Wookiee'     => 'wookiee@aggrrttaaggrrt.com' }"
                )
            ).authors().entrySet(),
            Matchers.containsInAnyOrder(
                new MapEntry<>("Darth Vader", Optional.of("darthvader@darkside.com")),
                new MapEntry<>("Wookiee", Optional.of("wookiee@aggrrttaaggrrt.com"))
            )
        );
    }

    @Test
    void readsSingleAuthor() {
        MatcherAssert.assertThat(
            new Podspec.Ruby(
                String.join(
                    "\n",
                    "spec.name = 'Reachability'",
                    "spec.authors = 'Darth Vader'",
                    "spec.version = '0.0.1'"
                )
            ).authors().entrySet(),
            Matchers.containsInAnyOrder(
                new MapEntry<>("Darth Vader", Optional.empty())
            )
        );
    }

    @Test
    void readsAuthorsList() {
        MatcherAssert.assertThat(
            new Podspec.Ruby(
                String.join(
                    "\n",
                    "spec.name = 'Reachability'",
                    "spec.authors = 'Darth Vader', 'Wookiee'",
                    "spec.version = '0.0.1'"
                )
            ).authors().entrySet(),
            Matchers.containsInAnyOrder(
                new MapEntry<>("Darth Vader", Optional.empty()),
                new MapEntry<>("Wookiee", Optional.empty())
            )
        );
    }

    @Test
    void readsSource() {
        MatcherAssert.assertThat(
            new Podspec.Ruby(PodspecRubyTest.EXAMPLE).source().entrySet(),
            Matchers.containsInAnyOrder(
                new MapEntry<>("git", "https://github.com/tonymillion/Reachability.git"),
                new MapEntry<>("tag", "v3.1.0")
            )
        );
    }

    @Test
    void readsSourceWithParam() {
        MatcherAssert.assertThat(
            new Podspec.Ruby(
                String.join(
                    "\n",
                    "spec.source = { :git => 'https://github.com/AFNetworking/AFNetworking.git',",
                    "                :tag => spec.version.to_s }"
                )
            ).source().entrySet(),
            Matchers.containsInAnyOrder(
                new MapEntry<>("git", "https://github.com/AFNetworking/AFNetworking.git"),
                new MapEntry<>("tag", "spec.version.to_s")
            )
        );
    }
}
