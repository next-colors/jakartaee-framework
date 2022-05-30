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
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.stream.Stream;

import org.jooq.types.UInteger;
import org.junit.jupiter.api.Test;

/**
 * {@link UIntegerConverter} のテストです。
 *
 * @author hamana
 */
class UIntegerConverterTest {
    private final UIntegerConverter converter = new UIntegerConverter();

    /**
     * {@link UIntegerConverter#getUnsignedValue(Integer)} のテストです。
     */
    @Test
    void testGetUnsignedValue() {
        final int unsignedNumber = ints().bound(Integer.MAX_VALUE).get();

        assertThat(converter.getUnsignedValue(unsignedNumber)).isEqualTo(UInteger.valueOf(unsignedNumber));

        Stream.of(-1, Integer.MIN_VALUE).forEach(number ->
                assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> converter.getUnsignedValue(number))
        );

        assertThatNullPointerException().isThrownBy(() -> converter.getUnsignedValue(null));
    }
}
