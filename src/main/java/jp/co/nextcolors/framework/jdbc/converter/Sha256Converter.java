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

import org.apache.commons.codec.digest.DigestUtils;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 文字列を SHA-256 ハッシュ値に変換するためのコンバータです。
 *
 * @author hamana
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
public class Sha256Converter extends HashConverter {
    /**
     * {@inheritDoc}
     */
    @Override
    public String to(final String userObject) {
        if (Objects.isNull(userObject)) {
            return null;
        }

        return DigestUtils.sha256Hex(userObject);
    }
}
