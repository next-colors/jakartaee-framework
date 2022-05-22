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
package jp.co.nextcolors.framework.json.bind.serializer;

import static net.andreinc.mockneat.unit.objects.From.from;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import jp.co.nextcolors.framework.enumeration.type.ICodeEnum;

/**
 * {@link CodeEnumSerializer} のテストです。
 *
 * @author hamana
 */
@ExtendWith(MockitoExtension.class)
class CodeEnumSerializerTest {
    private final FooSerializer serializer = new FooSerializer();

    @Mock
    private JsonGenerator generator;

    @Mock
    private SerializationContext ctx;

    /**
     * {@link CodeEnumSerializer#serialize(Enum, JsonGenerator, SerializationContext)} のテストです。
     */
    @Test
    void testSerialize() {
        Stream.of(Foo.values()).forEach(value -> {
            serializer.serialize(value, generator, ctx);
            verify(ctx, times(1)).serialize(anyInt(), same(generator));
            clearInvocations(ctx);
        });

        // null
        serializer.serialize(null, generator, ctx);
        verify(ctx, times(1)).serialize(null, generator);
        clearInvocations(ctx);

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> serializer.serialize(from(Foo.class).get(), null, ctx));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> serializer.serialize(from(Foo.class).get(), generator, null));
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
    private static class FooSerializer extends CodeEnumSerializer<Foo, Integer> {
    }
}
