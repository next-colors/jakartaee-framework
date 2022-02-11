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

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.AbstractConverter;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import jp.co.nextcolors.framework.enumeration.type.ICodeEnum;
import jp.co.nextcolors.framework.util.GenericUtil;

/**
 * プロパティにコードを持つ列挙型の列挙型定数に変換するための抽象クラスです。
 *
 * @param <E> 列挙型の型です。
 * @param <C> 列挙型のコードの型です。
 * @author hamana
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class CodeEnumConverter<E extends Enum<E> & ICodeEnum<E, C>, C> extends AbstractConverter {
    /**
     * 列挙型の型を表すクラスです。
     */
    private final Class<E> enumClass;

    /**
     * 列挙型のコードの型を表すクラスです。
     */
    private final Class<C> enumCodeClass;

    @SuppressWarnings("unchecked")
    protected CodeEnumConverter() {
        super(null);

        Map<TypeVariable<?>, Type> typeVariableMap = GenericUtil.getTypeVariableMap(getClass());

        for (Class<?> clazz = getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            if (clazz.getSuperclass() == CodeEnumConverter.class) {
                Type[] paramTypes = GenericUtil.getGenericParameters(clazz.getGenericSuperclass());
                enumClass = (Class<E>) GenericUtil.getActualClass(paramTypes[0], typeVariableMap);
                enumCodeClass = (Class<C>) GenericUtil.getActualClass(paramTypes[1], typeVariableMap);

                return;
            }
        }

        throw new RuntimeException("列挙型の型/列挙型のコードの型を表すクラスを設定できませんでした。");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected <T> T convertToType(@NonNull final Class<T> type, @NonNull final Object value) throws Throwable {
        C code = enumCodeClass.cast(ConvertUtils.convert(value, enumCodeClass));

        return type.cast(ICodeEnum.codeOf(enumClass, code));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<E> getDefaultType() {
        return enumClass;
    }
}
