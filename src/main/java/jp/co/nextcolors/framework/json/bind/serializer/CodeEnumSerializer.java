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

import java.io.Serializable;
import java.util.Optional;

import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import jp.co.nextcolors.framework.enumeration.type.ICodeEnum;

/**
 * プロパティにコードを持つ列挙型の列挙型定数を JSON のプロパティに変換するための抽象クラスです。
 *
 * @param <T> 列挙型の型です。
 * @param <C> 列挙型のコードの型です。
 * @author hamana
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
@SuppressWarnings("serial")
public abstract class CodeEnumSerializer<T extends Enum<T> & ICodeEnum<T, C>, C> implements JsonbSerializer<T>, Serializable {
    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final T obj, @NonNull final JsonGenerator generator, @NonNull final SerializationContext ctx) {
        final C code = Optional.ofNullable(obj).map(value -> value.getCode()).orElse(null);

        ctx.serialize(code, generator);
    }
}
