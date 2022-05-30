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
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.jooq.types.UNumber;
import org.junit.jupiter.api.Test;

import lombok.SneakyThrows;

/**
 * {@link UnsignedNumberConverter} のテストです。
 *
 * @author hamana
 */
class UnsignedNumberConverterTest {
    private final UNumberConverter converter = spy(new UNumberConverter());

    /**
     * {@link UnsignedNumberConverter#convertToType(Class, Object)} のテストです。
     */
    @SneakyThrows(Throwable.class)
    @Test
    void testConvertToType() {
        final Integer number = ints().get();

        converter.convertToType(Number.class, number);
        verify(converter).getUnsignedValue(number);
        clearInvocations(converter);

        converter.convertToType(Number.class, number.toString());
        verify(converter).getUnsignedValue(number);
        clearInvocations(converter);

        assertThatNullPointerException().isThrownBy(() -> converter.convertToType(null, number));
    }

    /**
     * {@link UnsignedNumberConverter#getDefaultType()} のテストです。
     */
    @Test
    void testGetDefaultType() {
        assertThat(converter.getDefaultType()).isEqualTo(UNumber.class);
    }

    private static class UNumberConverter extends UnsignedNumberConverter<UNumber, Integer> {

        @Override
        protected UNumber getUnsignedValue(Integer signedValue) throws NumberFormatException {
            return null;
        }
    }
}
