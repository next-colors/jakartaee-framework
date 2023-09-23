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

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import ru.vyarus.java.generics.resolver.GenericsResolver;
import ru.vyarus.java.generics.resolver.context.GenericsContext;

import lombok.NonNull;

/**
 * プロパティにコードを持つ列挙型を扱うためのインターフェースです。
 *
 * @param <E> 列挙型の型です。
 * @param <C> 列挙型のコードの型です。
 * @author hamana
 */
public interface ICodeEnum<E extends Enum<E> & ICodeEnum<E, C>, C> {
    /**
     * 指定したコードを持つ指定した列挙型の列挙型定数を返します。<br />
     * 指定したコードが {@code null} の場合は、{@code null} を返します。
     *
     * @param <E>       列挙型の型
     * @param <C>       列挙型のコードの型
     * @param enumClass 列挙型の型を表すクラス
     * @param code      コード
     * @return 列挙型定数
     * @throws IllegalArgumentException 指定した列挙型に指定したコードの列挙型定数がない場合
     */
    static <E extends Enum<E> & ICodeEnum<E, C>, C> E codeOf(@NonNull final Class<E> enumClass, final C code)
            throws IllegalArgumentException {
        if (Objects.isNull(code)) {
            return null;
        }

        return EnumSet.allOf(enumClass).stream()
                .filter(constant -> Objects.equals(constant.getCode(), code))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("%s のコード %s に %s は含まれていません。"
                                .formatted(enumClass.getName(), codes(enumClass), code))
                );
    }

    /**
     * 指定したコードを持つ指定した列挙型の列挙型定数が存在するかどうかを判定します。
     *
     * @param <E>       列挙型の型
     * @param <C>       列挙型のコードの型
     * @param enumClass 列挙型の型を表すクラス
     * @param code      コード
     * @return 指定したコードを持つ指定した列挙型の列挙型定数が存在する場合は {@code true}、そうではない場合は {@code false}
     */
    static <E extends Enum<E> & ICodeEnum<E, C>, C> boolean isValidCode(@NonNull final Class<E> enumClass, final C code) {
        final Set<C> codes = codes(enumClass);

        return codes.contains(code);
    }

    /**
     * すべてのコードを返します。
     *
     * @param <E>       列挙型の型
     * @param <C>       列挙型のコードの型
     * @param enumClass 列挙型の型を表すクラス
     * @return すべてのコード
     */
    static <E extends Enum<E> & ICodeEnum<E, C>, C> Set<C> codes(@NonNull final Class<E> enumClass) {
        return EnumSet.allOf(enumClass).stream().map(ICodeEnum::getCode).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * 列挙型のコードの型を表すクラスを返します。
     *
     * @param <E>       列挙型の型
     * @param <C>       列挙型のコードの型
     * @param enumClass 列挙型の型を表すクラス
     * @return 列挙型のコードの型を表すクラス
     */
    @SuppressWarnings("unchecked")
    static <E extends Enum<E> & ICodeEnum<E, C>, C> Class<C> getCodeClass(@NonNull final Class<E> enumClass) {
        final GenericsContext context = GenericsResolver.resolve(enumClass).type(ICodeEnum.class);

        return (Class<C>) context.generic(1);
    }

    /**
     * コードを取得します。
     *
     * @return コード
     */
    C getCode();
}
