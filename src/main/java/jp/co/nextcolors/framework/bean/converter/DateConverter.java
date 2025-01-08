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

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.beanutils2.ConversionException;
import org.apache.commons.beanutils2.converters.DateTimeConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import jp.co.nextcolors.framework.bean.annotation.BeanConverter;

/**
 * {@link Date} 型の日付に変換するためのコンバータです。
 *
 * @author hamana
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@BeanConverter(forClass = Date.class)
public class DateConverter extends DateTimeConverter<Date> {
    /**
     * 日付のコンポーネントです。
     */
    private static final String[] DATE_COMPONENTS =
            StringUtils.split(DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.getPattern(), '-');

    /**
     * 時間のコンポーネントです。
     */
    private static final String[] TIME_COMPONENTS =
            StringUtils.split(DateFormatUtils.ISO_8601_EXTENDED_TIME_FORMAT.getPattern(), ':');

    /**
     * タイムゾーンのコンポーネントです。
     */
    private static final String[] TIME_ZONE_COMPONENTS = {StringUtils.EMPTY, "Z", "ZZ", "ZZZ"};

    /**
     * 日付のセパレータです。
     */
    private static final String[] DATE_SEPARATORS = {StringUtils.EMPTY, "-", "/"};

    /**
     * 時間のセパレータです。
     */
    private static final String[] TIME_SEPARATORS = {StringUtils.EMPTY, ":"};

    /**
     * 日付と時間のセパレータです。
     */
    private static final String[] DATE_TIME_SEPARATORS = {StringUtils.EMPTY, StringUtils.SPACE, "'T'"};

    public DateConverter() {
        super(null);
        setPatterns(getDateTimeFormats());
    }

    /**
     * {@code java.time} の日付/時間を {@link Date} 型への日付に変換します。
     *
     * @param value {@code java.time} の日付/時間
     * @return {@link Date} 型の日付
     */
    private Date toDate(final Temporal value) {
        final ZoneId zone = Optional.ofNullable(getTimeZone()).map(TimeZone::toZoneId).orElseGet(ZoneId::systemDefault);
        final LocalDate epochDate = LocalDateTime.ofInstant(Instant.EPOCH, zone).toLocalDate();

        return switch (value) {
            case Instant instant -> Date.from(instant);
            case LocalDate localDate -> Date.from(localDate.atStartOfDay(zone).toInstant());
            case LocalDateTime localDateTime -> Date.from(localDateTime.atZone(zone).toInstant());
            case LocalTime localTime -> Date.from(epochDate.atTime(localTime).atZone(zone).toInstant());
            case OffsetDateTime offsetDateTime -> Date.from(offsetDateTime.toInstant());
            case OffsetTime offsetTime -> Date.from(epochDate.atTime(offsetTime).toInstant());
            case ZonedDateTime zonedDateTime -> Date.from(zonedDateTime.toInstant());
            default -> throw new IllegalArgumentException("%s はサポートされていない日付/時間の型です。"
                    .formatted(value.getClass().getName()));
        };
    }

    /**
     * 日付のフォーマットを取得します。
     *
     * @return 日付のフォーマット
     */
    private Set<String> getDateFormats() {
        return Stream.of(DATE_SEPARATORS)
                .map(separator -> String.join(separator, DATE_COMPONENTS))
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * 時間のフォーマットを取得します。
     *
     * @return 時間のフォーマット
     */
    private Set<String> getTimeFormats() {
        final Set<String> timeFormats = new HashSet<>();

        Stream.of(TIME_SEPARATORS).forEach(separator ->
                IntStream.rangeClosed(1, TIME_COMPONENTS.length).forEach(length -> {
                    final String[] timeComponents = Arrays.copyOf(TIME_COMPONENTS, length);

                    final String timeFormat = String.join(separator, timeComponents);

                    Stream.of(TIME_ZONE_COMPONENTS).forEach(timeZoneComponent ->
                            timeFormats.add(timeFormat + timeZoneComponent)
                    );
                })
        );

        return Set.copyOf(timeFormats);
    }

    /**
     * 日時のフォーマットを取得します。
     *
     * @return 日時のフォーマット
     */
    private String[] getDateTimeFormats() {
        final Set<String> dateFormats = getDateFormats();
        final Set<String> timeFormats = getTimeFormats();

        final Set<String> dateTimeFormats = new HashSet<>();

        dateFormats.forEach(dateFormat ->
                timeFormats.forEach(timeFormat ->
                        Stream.of(DATE_TIME_SEPARATORS).forEach(separator ->
                                dateTimeFormats.add(String.join(separator, dateFormat, timeFormat))
                        )
                )
        );

        dateTimeFormats.addAll(dateFormats);
        dateTimeFormats.addAll(timeFormats);

        return dateTimeFormats.toArray(String[]::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected <T> T convertToType(@NonNull final Class<T> type, @NonNull final Object value) throws Exception {
        if (value instanceof Temporal temporal) {
            final Date date = toDate(temporal);

            return type.cast(date);
        }

        if (!(value instanceof String)) {
            return super.convertToType(type, value);
        }

        try {
            final Date date = DateUtils.parseDateStrictly(Objects.toString(value), getPatterns());

            return type.cast(date);
        } catch (final ParseException e) {
            throw new ConversionException("%s を %s に変換できませんでした。使用した日時フォーマットは %s です。"
                    .formatted(value, type.getName(), Arrays.toString(getPatterns())),
                    e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<Date> getDefaultType() {
        return Date.class;
    }
}
