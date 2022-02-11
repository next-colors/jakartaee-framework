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

import org.jooq.types.UByte;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import jp.co.nextcolors.framework.bean.annotation.BeanConverter;

/**
 * {@link UByte} 型の数値に変換するためのコンバータです。
 *
 * @author hamana
 */
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@BeanConverter(forClass = UByte.class)
public class UByteConverter extends UnsignedNumberConverter<UByte, Byte> {
    //-------------------------------------------------------------------------
    //    Protected Methods
    //-------------------------------------------------------------------------

    /**
     * @param defaultValue デフォルト値
     */
    public UByteConverter(final UByte defaultValue) {
        super(defaultValue);
    }

    //-------------------------------------------------------------------------
    //    Public Methods
    //-------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    protected UByte getUnsignedValue(@NonNull final Byte signedValue) throws NumberFormatException {
        return UByte.valueOf(signedValue.shortValue());
    }
}
