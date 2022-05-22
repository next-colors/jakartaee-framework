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

import java.util.stream.Stream;

import org.jooq.lambda.Unchecked;
import org.junit.jupiter.api.Test;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

import jp.co.nextcolors.framework.enumeration.type.ICodeEnum;

/**
 * {@link CodeEnumConverter} のテストです。
 *
 * @author hamana
 */
class CodeEnumConverterTest {
    private final FooConverter converter = new FooConverter();

    /**
     * {@link CodeEnumConverter#convertToType(Class, Object)} のテストです。
     */
    @SneakyThrows(Throwable.class)
    @Test
    void testConvertToType() {
        Stream.of(Foo.values()).forEach(Unchecked.consumer(value ->
                assertThat(converter.convertToType(Foo.class, value.getCode())).isEqualTo(value)
        ));

        // 含まれていないコード
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> converter.convertToType(Foo.class, 2));

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> converter.convertToType(null, from(Foo.class).get().getCode()));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> converter.convertToType(Foo.class, null));
    }

    /**
     * {@link CodeEnumConverter#getDefaultType()} のテストです。
     */
    @Test
    void testGetDefaultType() {
        assertThat(converter.getDefaultType()).isEqualTo(Foo.class);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private enum Foo implements ICodeEnum<Foo, Integer> {
        BAR(0),
        BAZ(1);

        @Getter
        @NonNull
        private final Integer code;
    }

    private static class FooConverter extends CodeEnumConverter<Foo, Integer> {
    }
}
