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
package jp.co.nextcolors.framework.bean.converter;

import static net.andreinc.mockneat.unit.types.Ints.ints;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.stream.Stream;

import org.jooq.types.UShort;
import org.junit.jupiter.api.Test;

/**
 * {@link UShortConverter} のテストです。
 *
 * @author hamana
 */
class UShortConverterTest {
    private final UShortConverter converter = new UShortConverter();

    /**
     * {@link UShortConverter#getUnsignedValue(Short)} のテストです。
     */
    @Test
    void testGetUnsignedValue() {
        final short unsignedNumber = ints().bound(Short.valueOf(Short.MAX_VALUE).intValue()).get().shortValue();

        assertThat(converter.getUnsignedValue(unsignedNumber)).isEqualTo(UShort.valueOf(unsignedNumber));

        Stream.of(-1, Short.MIN_VALUE).forEach(number ->
                assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> converter.getUnsignedValue(number.shortValue()))
        );

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> converter.getUnsignedValue(null));
    }
}
