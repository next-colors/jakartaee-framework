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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * {@link HashConverter} のテストです。
 *
 * @author hamana
 */
@ExtendWith(MockitoExtension.class)
class HashConverterTest {
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private HashConverter converter;

    /**
     * {@link HashConverter#from(String)} のテストです。
     */
    @Test
    void testFrom() {
        final String text = strings().get();

        assertThat(converter.from(text)).isEqualTo(text);

        // null
        assertThat(converter.from(null)).isNull();
    }
}
