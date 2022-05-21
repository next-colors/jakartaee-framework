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

import static net.andreinc.mockneat.unit.text.Strings.strings;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * {@link Base64Converter} のテストです。
 *
 * @author hamana
 */
class Base64ConverterTest {
    private Base64Converter converter = new Base64Converter();

    /**
     * {@link Base64Converter#from(String)} のテストです。
     */
    @Test
    void testFrom() {
        String text = strings().get();
        String expected = StringUtils.toEncodedString(Base64.decodeBase64(text), StandardCharsets.UTF_8);

        assertThat(converter.from(text)).isEqualTo(expected);

        // null
        assertThat(converter.from(null)).isNull();
    }

    /**
     * {@link Base64Converter#to(String)} のテストです。
     */
    @Test
    void testTo() {
        String text = strings().get();
        String expected = Base64.encodeBase64String(text.getBytes(StandardCharsets.UTF_8));

        assertThat(converter.to(text)).isEqualTo(expected);

        // null
        assertThat(converter.to(null)).isNull();
    }
}
