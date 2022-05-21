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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import jp.co.nextcolors.framework.enumeration.type.ICodeEnum;

/**
 * {@link CodeEnumConverter} のテストです。
 *
 * @author hamana
 */
@ExtendWith(MockitoExtension.class)
class CodeEnumConverterTest {
    private final FooConverter converter = new FooConverter();

    /**
     * {@link CodeEnumConverter#from(Object)} のテストです。
     */
    @Test
    void testFrom() {
        Stream.of(Foo.values()).forEach(value ->
                assertThat(converter.from(value.getCode().longValue())).isEqualTo(value)
        );

        // null
        assertThat(converter.from(null)).isNull();

        // 含まれていないコード
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> converter.from(2L));
    }

    /**
     * {@link CodeEnumConverter#to(Enum)} のテストです。
     */
    @Test
    void testTo() {
        Stream.of(Foo.values()).forEach(value ->
                assertThat(converter.to(value)).isEqualTo(value.getCode().longValue())
        );

        // null
        assertThat(converter.to(null)).isNull();
    }

    /**
     * {@link CodeEnumConverter#fromType()} のテストです。
     */
    @Test
    void testFromType() {
        assertThat(converter.fromType()).isEqualTo(Long.class);
    }

    /**
     * {@link CodeEnumConverter#toType()} のテストです。
     */
    @Test
    void testToType() {
        assertThat(converter.toType()).isEqualTo(Foo.class);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private enum Foo implements ICodeEnum<Foo, Integer> {
        BAR(0),
        BAZ(1);

        @Getter
        @NonNull
        private final Integer code;
    }

    @SuppressWarnings("serial")
    private static class FooConverter extends CodeEnumConverter<Foo, Integer, Long> {
    }
}
