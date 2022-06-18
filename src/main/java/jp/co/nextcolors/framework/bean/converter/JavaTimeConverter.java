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

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.util.Date;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.AbstractConverter;

import ru.vyarus.java.generics.resolver.GenericsResolver;
import ru.vyarus.java.generics.resolver.context.GenericsContext;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

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
    /**
     * 日付/時間の型を表すクラスです。
     */
    private final Class<JT> javaTimeClass;

    /**
     * タイムゾーン ID です。
     */
    @NonNull
    protected ZoneId zone = ZoneId.systemDefault();

    @SuppressWarnings("unchecked")
    protected JavaTimeConverter() {
        super(null);

        final GenericsContext context = GenericsResolver.resolve(getClass()).type(JavaTimeConverter.class);

        javaTimeClass = (Class<JT>) context.generic(0);
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
        final Date date = (Date) ConvertUtils.convert(value, Date.class);

        final OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(date.toInstant(), zone);

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
