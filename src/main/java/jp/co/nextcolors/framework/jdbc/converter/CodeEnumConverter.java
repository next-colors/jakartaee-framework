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

import java.util.Objects;

import org.apache.commons.beanutils.ConvertUtils;
import org.jooq.Converter;

import ru.vyarus.java.generics.resolver.GenericsResolver;
import ru.vyarus.java.generics.resolver.context.GenericsContext;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import jp.co.nextcolors.framework.enumeration.type.ICodeEnum;

/**
 * プロパティにコードを持つ列挙型の値を DB のデータ型に準拠した値に変換するための抽象クラスです。
 *
 * @param <E> 列挙型の型です。
 * @param <C> 列挙型のコードの型です。
 * @param <D> DB のデータ型に準拠した型です。
 * @author hamana
 */
@ToString
@EqualsAndHashCode
@SuppressWarnings("serial")
public abstract class CodeEnumConverter<E extends Enum<E> & ICodeEnum<E, C>, C, D> implements Converter<D, E> {
    /**
     * 列挙型の型を表すクラスです。
     */
    private final Class<E> enumClass;

    /**
     * 列挙型のコードの型を表すクラスです。
     */
    private final Class<C> enumCodeClass;

    /**
     * DB のデータ型に準拠した型を表すクラスです。
     */
    private final Class<D> databaseObjectClass;

    @SuppressWarnings("unchecked")
    protected CodeEnumConverter() {
        GenericsContext context = GenericsResolver.resolve(getClass()).type(CodeEnumConverter.class);

        enumClass = (Class<E>) context.generic(0);
        enumCodeClass = (Class<C>) context.generic(1);
        databaseObjectClass = (Class<D>) context.generic(2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E from(final D databaseObject) {
        if (Objects.isNull(databaseObject)) {
            return null;
        }

        C code = enumCodeClass.cast(ConvertUtils.convert(databaseObject, enumCodeClass));

        return ICodeEnum.codeOf(enumClass, code);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public D to(final E userObject) {
        if (Objects.isNull(userObject)) {
            return null;
        }

        C code = userObject.getCode();

        return databaseObjectClass.cast(ConvertUtils.convert(code, databaseObjectClass));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<D> fromType() {
        return databaseObjectClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<E> toType() {
        return enumClass;
    }
}
