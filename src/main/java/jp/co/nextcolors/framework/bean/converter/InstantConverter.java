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

import java.time.Instant;
import java.time.OffsetDateTime;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import jp.co.nextcolors.framework.bean.annotation.BeanConverter;

/**
 * {@link Instant} 型の日時に変換するためのコンバータです。
 *
 * @author hamana
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@BeanConverter(forClass = Instant.class)
public class InstantConverter extends JavaTimeConverter<Instant> {
    //-------------------------------------------------------------------------
    //    Protected Methods
    //-------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    protected Instant getDateTime(@NonNull final OffsetDateTime offsetDateTime) {
        return offsetDateTime.toInstant();
    }
}
