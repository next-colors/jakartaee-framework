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

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

/**
 * {@link Sha256Converter} のテストです。
 *
 * @author hamana
 */
class Sha256ConverterTest {
    private final Sha256Converter converter = new Sha256Converter();

    /**
     * {@link Sha256Converter#to(String)} のテストです。
     */
    @Test
    void testTo() {
        String text = strings().get();
        String expected = DigestUtils.sha256Hex(text);

        assertThat(converter.to(text)).isEqualTo(expected);

        // null
        assertThat(converter.to(null)).isNull();
    }
}
