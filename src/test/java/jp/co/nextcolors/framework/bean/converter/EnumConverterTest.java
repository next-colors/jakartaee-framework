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

import static net.andreinc.mockneat.unit.objects.From.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.stream.Stream;

import org.apache.commons.beanutils.ConversionException;
import org.jooq.lambda.Unchecked;
import org.junit.jupiter.api.Test;

import lombok.SneakyThrows;

/**
 * {@link EnumConverter} のテストです。
 *
 * @author hamana
 */
class EnumConverterTest {
    private final FooConverter converter = new FooConverter();

    /**
     * {@link EnumConverter#convertToType(Class, Object)} のテストです。
     */
    @SneakyThrows(Throwable.class)
    @Test
    void testConvertToType() {
        Stream.of(Foo.values()).forEach(Unchecked.consumer(value -> {
            assertThat(converter.convertToType(Foo.class, value.ordinal())).isEqualTo(value);
            assertThat(converter.convertToType(Foo.class, value.name())).isEqualTo(value);
        }));

        // 含まれていない序数
        assertThatExceptionOfType(ConversionException.class).isThrownBy(() -> converter.convertToType(Foo.class, 2));

        // 含まれていない名前
        Stream.of(Foo.values()).forEach(value ->
                assertThatExceptionOfType(ConversionException.class).isThrownBy(() -> converter.convertToType(Foo.class, value.name().toLowerCase()))
        );

        assertThatNullPointerException().isThrownBy(() -> converter.convertToType(null, from(Foo.class).get().name()));
        assertThatNullPointerException().isThrownBy(() -> converter.convertToType(Foo.class, null));
    }

    /**
     * {@link EnumConverter#getDefaultType()} のテストです。
     */
    @Test
    void testGetDefaultType() {
        assertThat(converter.getDefaultType()).isEqualTo(Foo.class);
    }

    private enum Foo {
        BAR,
        BAZ
    }

    private static class FooConverter extends EnumConverter<Foo> {
    }
}
