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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.OffsetDateTime;
import java.time.YearMonth;

import org.junit.jupiter.api.Test;

/**
 * {@link YearMonthConverter} のテストです。
 *
 * @author hamana
 */
class YearMonthConverterTest {
    private final YearMonthConverter converter = new YearMonthConverter();

    /**
     * {@link YearMonthConverter#getDateTime(OffsetDateTime)} のテストです。
     */
    @Test
    void testGetDateTime() {
        final OffsetDateTime offsetDateTime = localDates().get().atStartOfDay(converter.zone).toOffsetDateTime();

        assertThat(converter.getDateTime(offsetDateTime)).isEqualTo(YearMonth.from(offsetDateTime));

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> converter.getDateTime(null));
    }
}
