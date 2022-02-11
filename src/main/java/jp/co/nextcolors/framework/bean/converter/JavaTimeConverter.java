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
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.AbstractConverter;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import jp.co.nextcolors.framework.util.GenericUtil;

/**
 * {@code java.time} の日付/時間に変換するための抽象クラスです。
 *
 * @param <JT> 日付/時間の型です。
 * @author hamana
 */
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class JavaTimeConverter<JT extends Temporal> extends AbstractConverter {
    //-------------------------------------------------------------------------
    //    Private Properties
    //-------------------------------------------------------------------------
    /**
     * 日付/時間の型を表すクラスです。
     */
    private final Class<JT> javaTimeClass;

    //-------------------------------------------------------------------------
    //    Protected Properties
    //-------------------------------------------------------------------------
    /**
     * タイムゾーン ID です。
     */
    @NonNull
    protected ZoneId zone = ZoneId.systemDefault();

    //-------------------------------------------------------------------------
    //    Protected Methods
    //-------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    protected JavaTimeConverter() {
        super(null);

        Map<TypeVariable<?>, Type> typeVariableMap = GenericUtil.getTypeVariableMap(getClass());

        for (Class<?> clazz = getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            if (clazz.getSuperclass() == JavaTimeConverter.class) {
                Type[] paramTypes = GenericUtil.getGenericParameters(clazz.getGenericSuperclass());
                javaTimeClass = (Class<JT>) GenericUtil.getActualClass(paramTypes[0], typeVariableMap);

                return;
            }
        }

        throw new RuntimeException("日付/時間の型を表すクラスを設定できませんでした。");
    }

    /**
     * 日付/時間を取得します。
     *
     * @param offsetDateTime UTC/グリニッジからのオフセット付きの日時
     * @return 日付/時間
     */
    protected abstract JT getDateTime(OffsetDateTime offsetDateTime);

    /**
     * {@inheritDoc}
     */
    @Override
    protected <T> T convertToType(@NonNull final Class<T> type, @NonNull final Object value) throws Throwable {
        Date date = (Date) ConvertUtils.convert(value, Date.class);

        OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(date.toInstant(), zone);

        return type.cast(getDateTime(offsetDateTime));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<JT> getDefaultType() {
        return javaTimeClass;
    }
}
