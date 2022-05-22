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
package jp.co.nextcolors.framework.json.bind.deserializer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.Objects;
import java.util.stream.Stream;

import javax.json.bind.JsonbException;
import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import jp.co.nextcolors.framework.enumeration.type.ICodeEnum;
import jp.co.nextcolors.framework.json.bind.serializer.CodeEnumSerializer;

/**
 * {@link CodeEnumDeserializer} のテストです。
 *
 * @author hamana
 */
@ExtendWith(MockitoExtension.class)
class CodeEnumDeserializerTest {
    private final FooDeserializer deserializer = new FooDeserializer();

    @Mock
    private JsonParser parser;

    @Mock
    private DeserializationContext ctx;

    /**
     * {@link CodeEnumSerializer#serialize(Enum, JsonGenerator, SerializationContext)} のテストです。
     */
    @Test
    void testDeserialize() {
        Stream.of(Foo.values()).forEach(value -> {
            when(parser.getString()).thenReturn(value.getCode().toString());
            assertThat(deserializer.deserialize(parser, ctx, value.getClass())).isEqualTo(value);
            reset(parser);
        });

        Stream.of(null, StringUtils.EMPTY, StringUtils.SPACE).forEach(value -> {
            when(parser.getString()).thenReturn(value);
            assertThat(deserializer.deserialize(parser, ctx, Foo.class)).isNull();
            reset(parser);
        });

        // 含まれていないコード
        when(parser.getString()).thenReturn(Objects.toString(2));
        assertThatExceptionOfType(JsonbException.class).isThrownBy(() -> assertThat(deserializer.deserialize(parser, ctx, Foo.class)));
        reset(parser);

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> deserializer.deserialize(null, ctx, Foo.class));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> deserializer.deserialize(parser, null, Foo.class));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> deserializer.deserialize(parser, ctx, null));
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
    private static class FooDeserializer extends CodeEnumDeserializer<Foo, Integer> {
    }
}
