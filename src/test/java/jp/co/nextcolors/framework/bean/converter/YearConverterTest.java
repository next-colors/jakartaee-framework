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

import static net.andreinc.mockneat.unit.time.LocalDates.localDates;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.time.OffsetDateTime;
import java.time.Year;

import org.junit.jupiter.api.Test;

/**
 * {@link YearConverter} のテストです。
 *
 * @author hamana
 */
class YearConverterTest {
    private final YearConverter converter = new YearConverter();

    /**
     * {@link YearConverter#getDateTime(OffsetDateTime)} のテストです。
     */
    @Test
    void testGetDateTime() {
        final OffsetDateTime offsetDateTime = localDates().get().atStartOfDay(converter.zone).toOffsetDateTime();

        assertThat(converter.getDateTime(offsetDateTime).getValue()).isEqualTo(Year.from(offsetDateTime).getValue());

        assertThatNullPointerException().isThrownBy(() -> converter.getDateTime(null));
    }
}
