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

import static org.assertj.core.api.Assertions.assertThat;

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
    private FooConverter converter = new FooConverter();

    /**
     * {@link CodeEnumConverter#convert(Class, Object)} のテストです。
     */
    @Test
    void testConvert() {
        Stream.of(Foo.values()).forEach(value ->
                assertThat(converter.convert(Foo.class, value.getCode())).isEqualTo(value)
        );

        // 含まれていないコード
        assertThat(converter.convert(Foo.class, 2)).isNull();

        // null
        assertThat(converter.convert(Foo.class, null)).isNull();
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Foo implements ICodeEnum<Foo, Integer> {
        BAR(0),
        BAZ(1);

        @Getter
        @NonNull
        private final Integer code;
    }

    public static class FooConverter extends CodeEnumConverter<Foo, Integer> {
    }
}
