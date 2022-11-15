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

import java.io.Serializable;
import java.lang.reflect.Type;

import javax.json.bind.JsonbException;
import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.stream.JsonParser;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import jp.co.nextcolors.framework.enumeration.type.ICodeEnum;

/**
 * JSON のプロパティをプロパティにコードを持つ列挙型の列挙型定数に変換するための抽象クラスです。
 *
 * @param <T> 列挙型の型です。
 * @param <C> 列挙型のコードの型です。
 * @author hamana
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
@SuppressWarnings("serial")
public abstract class CodeEnumDeserializer<T extends Enum<T> & ICodeEnum<T, C>, C> implements JsonbDeserializer<T>, Serializable {
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public T deserialize(@NonNull final JsonParser parser, @NonNull final DeserializationContext ctx, @NonNull final Type rtType) {
        final Class<T> enumClass = (Class<T>) rtType;
        final Class<C> enumCodeClass = ICodeEnum.getCodeClass(enumClass);

        final String value = parser.getString();

        if (StringUtils.isBlank(value)) {
            return null;
        }

        try {
            final C code = enumCodeClass.cast(ConvertUtils.convert(value, enumCodeClass));

            return ICodeEnum.codeOf(enumClass, code);
        } catch (final Exception e) {
            throw new JsonbException(ExceptionUtils.getMessage(e), e);
        }
    }
}
