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
package jp.co.nextcolors.framework.enumeration.type;

import static net.andreinc.mockneat.unit.objects.From.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * {@link ICodeEnum} のテストです。
 *
 * @author hamana
 */
class ICodeEnumTest {
    /**
     * {@link ICodeEnum#codeOf(Class, Object)} のテストです。
     */
    @Test
    void testCodeOf() {
        assertThat(ICodeEnum.codeOf(Foo.class, null)).isNull();

        Stream.of(Foo.values()).forEach(value ->
                assertThat(ICodeEnum.codeOf(Foo.class, value.getCode())).isEqualTo(value)
        );

        // 含まれていないコード
        assertThatIllegalArgumentException().isThrownBy(() -> ICodeEnum.codeOf(Foo.class, 2));

        assertThatNullPointerException().isThrownBy(() -> ICodeEnum.codeOf(null, from(Foo.class).get().getCode()));
    }

    /**
     * {@link ICodeEnum#isValidCode(Class, Object)} のテストです。
     */
    @Test
    void testIsValidCode() {
        Stream.of(Foo.values()).forEach(value ->
                assertThat(ICodeEnum.isValidCode(Foo.class, value.getCode())).isTrue()
        );

        assertThat(ICodeEnum.isValidCode(Foo.class, 2)).isFalse();

        assertThatNullPointerException().isThrownBy(() -> ICodeEnum.isValidCode(null, from(Foo.class).get().getCode()));
    }

    /**
     * {@link ICodeEnum#codes(Class)} のテストです。
     */
    @Test
    void testCodes() {
        assertThat(ICodeEnum.codes(Foo.class)).containsExactlyInAnyOrder(Stream.of(Foo.values()).map(Foo::getCode).toArray(Integer[]::new));

        assertThatNullPointerException().isThrownBy(() -> ICodeEnum.codes(null));
    }

    /**
     * {@link ICodeEnum#getCodeClass(Class)} のテストです。
     */
    @Test
    void testGetCodeClass() {
        assertThat(ICodeEnum.getCodeClass(Foo.class)).isEqualTo(Integer.class);

        assertThatNullPointerException().isThrownBy(() -> ICodeEnum.getCodeClass(null));
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private enum Foo implements ICodeEnum<Foo, Integer> {
        BAR(0),
        BAZ(1);

        @Getter
        @NonNull
        private final Integer code;
    }
}
