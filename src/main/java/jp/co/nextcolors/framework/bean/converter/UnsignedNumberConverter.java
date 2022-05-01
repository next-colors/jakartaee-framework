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

import java.util.Objects;

import org.apache.commons.beanutils.converters.NumberConverter;
import org.jooq.types.UNumber;

import ru.vyarus.java.generics.resolver.GenericsResolver;
import ru.vyarus.java.generics.resolver.context.GenericsContext;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * 符号なし整数に変換するための抽象クラスです。
 *
 * @param <U> 符号なし整数の型です。
 * @param <S> 符号あり整数の型です。
 * @author hamana
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class UnsignedNumberConverter<U extends UNumber, S extends Number> extends NumberConverter {
    /**
     * 符号なし整数の型を表すクラスです。
     */
    private final Class<U> unsignedNumberClass;

    /**
     * 符号あり整数の型を表すクラスです。
     */
    private final Class<S> signedNumberClass;

    @SuppressWarnings("unchecked")
    protected UnsignedNumberConverter() {
        super(false);

        GenericsContext context = GenericsResolver.resolve(getClass()).type(UnsignedNumberConverter.class);

        unsignedNumberClass = (Class<U>) context.generic(0);
        signedNumberClass = (Class<S>) context.generic(1);
    }

    /**
     * @param defaultValue デフォルト値
     */
    protected UnsignedNumberConverter(final U defaultValue) {
        this();
        setDefaultValue(defaultValue);
    }

    /**
     * 符号なし整数を取得します。
     *
     * @param signedValue 符号あり整数
     * @return 符号なし整数
     * @throws NumberFormatException 符号あり整数が符号なし整数に変換できない場合
     */
    protected abstract U getUnsignedValue(S signedValue) throws NumberFormatException;

    /**
     * {@inheritDoc}
     */
    @Override
    protected <T> T convertToType(@NonNull final Class<T> type, final Object value) throws Throwable {
        S signedValue = super.convertToType(signedNumberClass, value);

        if (Objects.isNull(signedValue)) {
            return null;
        }

        return type.cast(getUnsignedValue(signedValue));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<U> getDefaultType() {
        return unsignedNumberClass;
    }
}
