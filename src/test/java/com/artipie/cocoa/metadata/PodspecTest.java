/*
 * The MIT License (MIT) Copyright (c) 2020-2022 artipie.com
 * https://github.com/cocoa-adapter/artipie/LICENSE.txt
 */
package com.artipie.cocoa.metadata;

import com.artipie.asto.test.TestResource;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link Podspec}.
 * @since 0.1
 */
class PodspecTest {

    @Test
    void initializesRuby() throws IOException {
        MatcherAssert.assertThat(
            Podspec.initiate(new TestResource("example.spec").asInputStream()),
            new IsInstanceOf(Podspec.Ruby.class)
        );
    }

    @Test
    void initializesJson() throws IOException {
        MatcherAssert.assertThat(
            Podspec.initiate(new TestResource("example.spec.json").asInputStream()),
            new IsInstanceOf(Podspec.Json.class)
        );
    }
}
