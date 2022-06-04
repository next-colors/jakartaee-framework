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

import static net.andreinc.mockneat.unit.text.Strings.strings;
import static net.andreinc.mockneat.unit.time.LocalDates.localDates;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.mock;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;

import lombok.SneakyThrows;

/**
 * {@link DateConverter} のテストです。
 *
 * @author hamana
 */
class DateConverterTest {
    private final DateConverter converter = new DateConverter();

    /**
     * {@link DateConverter#convertToType(Class, Object)} のテストです。
     */
    @SneakyThrows(Throwable.class)
    @Test
    void testConvertToType() {
        final Date date = localDates().mapToDate().get();
        final ZoneId zone = ZoneId.systemDefault();
        final LocalDate epochDate = LocalDateTime.ofInstant(Instant.EPOCH, zone).toLocalDate();

        final OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(date.toInstant(), zone);
        final Instant instant = offsetDateTime.toInstant();
        final LocalDate localDate = offsetDateTime.toLocalDate();
        final LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();
        final LocalTime localTime = offsetDateTime.toLocalTime();
        final OffsetTime offsetTime = offsetDateTime.toOffsetTime();
        final ZonedDateTime zonedDateTime = offsetDateTime.toZonedDateTime();

        assertThat(converter.convertToType(Date.class, instant)).isEqualTo(Date.from(instant));
        assertThat(converter.convertToType(Date.class, localDate)).isEqualTo(Date.from(localDate.atStartOfDay(zone).toInstant()));
        assertThat(converter.convertToType(Date.class, localDateTime)).isEqualTo(Date.from(localDateTime.atZone(zone).toInstant()));
        assertThat(converter.convertToType(Date.class, localTime)).isEqualTo(Date.from(epochDate.atTime(localTime).atZone(zone).toInstant()));
        assertThat(converter.convertToType(Date.class, offsetDateTime)).isEqualTo(Date.from(offsetDateTime.toInstant()));
        assertThat(converter.convertToType(Date.class, offsetTime)).isEqualTo(Date.from(epochDate.atTime(offsetTime).toInstant()));
        assertThat(converter.convertToType(Date.class, zonedDateTime)).isEqualTo(Date.from(zonedDateTime.toInstant()));
        assertThatIllegalArgumentException().isThrownBy(() -> converter.convertToType(Date.class, mock(Temporal.class)));

        assertThatNoException().isThrownBy(() -> converter.convertToType(Date.class, date.getTime()));

        assertThatNoException().isThrownBy(() -> converter.convertToType(Date.class, localDates().display(DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.getPattern()).get()));
        assertThatExceptionOfType(ConversionException.class).isThrownBy(() -> converter.convertToType(Date.class, strings().get()));

        assertThatNullPointerException().isThrownBy(() -> converter.convertToType(null, date));
        assertThatNullPointerException().isThrownBy(() -> converter.convertToType(Date.class, null));
    }

    /**
     * {@link DateConverter#getDefaultType()} のテストです。
     */
    @Test
    void testGetDefaultType() {
        assertThat(converter.getDefaultType()).isEqualTo(Date.class);
    }
}
