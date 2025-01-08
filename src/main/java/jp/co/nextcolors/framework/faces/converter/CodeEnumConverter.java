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
package jp.co.nextcolors.framework.faces.converter;

import java.util.Optional;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.EnumConverter;

import org.apache.commons.beanutils2.ConvertUtils;
import org.apache.commons.lang3.StringUtils;

import com.sun.faces.util.MessageFactory;

import ru.vyarus.java.generics.resolver.GenericsResolver;
import ru.vyarus.java.generics.resolver.context.GenericsContext;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import jp.co.nextcolors.framework.enumeration.type.ICodeEnum;

/**
 * Faces でプロパティにコードを持つ列挙型の列挙型定数に変換するための抽象クラスです。
 *
 * @param <E> 列挙型の型です。
 * @param <C> 列挙型のコードの型です。
 * @author hamana
 */
@ToString
@EqualsAndHashCode
public abstract class CodeEnumConverter<E extends Enum<E> & ICodeEnum<E, C>, C> implements Converter<E> {
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
        final GenericsContext context = GenericsResolver.resolve(getClass()).type(CodeEnumConverter.class);

        enumClass = (Class<E>) context.generic(0);
        enumCodeClass = (Class<C>) context.generic(1);
    }

    /**
     * 変換に失敗した場合のメッセージを取得します。
     *
     * @param context   Faces コンテキスト
     * @param component UI コンポーネント
     * @param value     変換対象の値
     * @return 変換に失敗した場合のメッセージ
     */
    private FacesMessage getConversionErrorMessage(final FacesContext context,
                                                   final UIComponent component,
                                                   final Object value) {
        final Object label = MessageFactory.getLabel(context, component);

        return MessageFactory.getMessage(context, EnumConverter.ENUM_ID, value, null, label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E getAsObject(@NonNull final FacesContext context, @NonNull final UIComponent component, final String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        final C code = enumCodeClass.cast(ConvertUtils.convert(value, enumCodeClass));

        try {
            return ICodeEnum.codeOf(enumClass, code);
        } catch (final IllegalArgumentException e) {
            throw new ConverterException(getConversionErrorMessage(context, component, code), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsString(@NonNull final FacesContext context, @NonNull final UIComponent component, final E value) {
        return Optional.ofNullable(value).map(val -> val.getCode().toString()).orElse(StringUtils.EMPTY);
    }
}
