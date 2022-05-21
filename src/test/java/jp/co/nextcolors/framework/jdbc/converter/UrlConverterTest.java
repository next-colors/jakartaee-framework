/*
 * (C) 2017 NEXT COLORS Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.co.nextcolors.framework.jdbc.converter;

import static net.andreinc.mockneat.unit.networking.URLs.urls;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;

import org.jooq.lambda.Unchecked;
import org.junit.jupiter.api.Test;

/**
 * {@link UrlConverter} のテストです。
 *
 * @author hamana
 */
class UrlConverterTest {
    private final UrlConverter converter = new UrlConverter();

    /**
     * {@link UrlConverter#from(String)} のテストです。
     */
    @Test
    void testFrom() {
        final URL url = urls().get(Unchecked.function(URL::new));

        assertThat(converter.from(url.toString())).isEqualTo(url);

        // null
        assertThat(converter.from(null)).isNull();
    }

    /**
     * {@link UrlConverter#to(URL)} のテストです。
     */
    @Test
    void testTo() {
        final URL url = urls().get(Unchecked.function(URL::new));

        assertThat(converter.to(url)).isEqualTo(url.toString());

        // null
        assertThat(converter.to(null)).isNull();
    }
}
