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

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.converters.DateTimeConverter;
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
public class DateConverter extends DateTimeConverter
{
	//-------------------------------------------------------------------------
	//    Private Constants
	//-------------------------------------------------------------------------
	/**
	 * 日付のコンポーネントです。
	 *
	 */
	private static final String[] DATE_COMPONENTS =
			StringUtils.split( DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.getPattern(), '-' );

	/**
	 * 時間のコンポーネントです。
	 *
	 */
	private static final String[] TIME_COMPONENTS =
			StringUtils.split( DateFormatUtils.ISO_8601_EXTENDED_TIME_FORMAT.getPattern(), ':' );

	/**
	 * タイムゾーンのコンポーネントです。
	 *
	 */
	private static final String[] TIME_ZONE_COMPONENTS = { StringUtils.EMPTY, "Z", "ZZ", "ZZZ" };

	/**
	 * 日付のセパレータです。
	 *
	 */
	private static final String[] DATE_SEPARATORS = { StringUtils.EMPTY, "-", "/" };

	/**
	 * 時間のセパレータです。
	 *
	 */
	private static final String[] TIME_SEPARATORS = { StringUtils.EMPTY, ":" };

	/**
	 * 日付と時間のセパレータです。
	 *
	 */
	private static final String[] DATE_TIME_SEPARATORS = { StringUtils.EMPTY, StringUtils.SPACE, "'T'" };

	//-------------------------------------------------------------------------
	//    Private Methods
	//-------------------------------------------------------------------------
	/**
	 * {@code java.time} の日付/時間を {@link Date} 型への日付に変換します。
	 *
	 * @param value
	 *         {@code java.time} の日付/時間
	 * @return {@link Date} 型の日付
	 */
	private Date toDate( @NonNull final Temporal value )
	{
		ZoneId zone = Optional.ofNullable( getTimeZone() ).map( TimeZone::toZoneId ).orElseGet( ZoneId::systemDefault );

		if ( Instant.class.isInstance( value ) ) {
			Instant instant = Instant.class.cast( value );

			return Date.from( instant );
		}

		if ( LocalDate.class.isInstance( value ) ) {
			LocalDate localDate = LocalDate.class.cast( value );

			return Date.from( localDate.atStartOfDay( zone ).toInstant() );
		}

		if ( LocalDateTime.class.isInstance( value ) ) {
			LocalDateTime localDateTime = LocalDateTime.class.cast( value );

			return Date.from( localDateTime.atZone( zone ).toInstant() );
		}

		if ( LocalTime.class.isInstance( value ) ) {
			LocalTime localTime = LocalTime.class.cast( value );
			LocalDate localDate = LocalDateTime.ofInstant( Instant.EPOCH, zone ).toLocalDate();

			return Date.from( localDate.atTime( localTime ).atZone( zone ).toInstant() );
		}

		if ( OffsetDateTime.class.isInstance( value ) ) {
			OffsetDateTime offsetDateTime = OffsetDateTime.class.cast( value );

			return Date.from( offsetDateTime.toInstant() );
		}

		if ( OffsetTime.class.isInstance( value ) ) {
			OffsetTime offsetTime = OffsetTime.class.cast( value );
			LocalDate localDate = LocalDateTime.ofInstant( Instant.EPOCH, zone ).toLocalDate();

			return Date.from( localDate.atTime( offsetTime ).toInstant() );
		}

		if ( ZonedDateTime.class.isInstance( value ) ) {
			ZonedDateTime zonedDateTime = ZonedDateTime.class.cast( value );

			return Date.from( zonedDateTime.toInstant() );
		}

		throw new IllegalArgumentException( String.format( "%s はサポートされていない日付/時間の型です。",
															value.getClass().getName() ) );
	}

	/**
	 * 日付のフォーマットを取得します。
	 *
	 * @return 日付のフォーマット
	 */
	private Set<String> getDateFormats()
	{
		return Arrays.stream( DATE_SEPARATORS )
						.map( separator -> String.join( separator, DATE_COMPONENTS ) )
						.collect( Collectors.toUnmodifiableSet() );
	}

	/**
	 * 時間のフォーマットを取得します。
	 *
	 * @return 時間のフォーマット
	 */
	private Set<String> getTimeFormats()
	{
		Set<String> timeFormats = new HashSet<>();

		Arrays.stream( TIME_SEPARATORS ).forEach( separator ->
			IntStream.rangeClosed( 1, TIME_COMPONENTS.length ).forEach( length -> {
				String[] timeComponents = Arrays.copyOf( TIME_COMPONENTS, length );

				String timeFormat = String.join( separator, timeComponents );

				Arrays.stream( TIME_ZONE_COMPONENTS ).forEach( timeZoneComponent ->
					timeFormats.add( timeFormat + timeZoneComponent )
				);
			} )
		);

		return Set.copyOf( timeFormats );
	}

	/**
	 * 日時のフォーマットを取得します。
	 *
	 * @return 日時のフォーマット
	 */
	private String[] getDateTimeFormats()
	{
		Set<String> dateFormats = getDateFormats();
		Set<String> timeFormats = getTimeFormats();

		Set<String> dateTimeFormats = new HashSet<>();

		dateFormats.forEach( dateFormat ->
			timeFormats.forEach( timeFormat ->
				Arrays.stream( DATE_TIME_SEPARATORS ).forEach( separator ->
					dateTimeFormats.add( String.join( separator, dateFormat, timeFormat ) )
				)
			)
		);

		dateTimeFormats.addAll( dateFormats );
		dateTimeFormats.addAll( timeFormats );

		return dateTimeFormats.toArray( String[]::new );
	}

	//-------------------------------------------------------------------------
	//    Protected Methods
	//-------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	protected <T> T convertToType( @NonNull final Class<T> type, @NonNull final Object value ) throws Exception
	{
		if ( Temporal.class.isInstance( value ) ) {
			Date date = toDate( Temporal.class.cast( value ) );

			return type.cast( date );
		}

		if ( !String.class.isInstance( value ) ) {
			return super.convertToType( type, value );
		}

		try {
			Date date = DateUtils.parseDateStrictly( Objects.toString( value ), getPatterns() );

			return type.cast( date );
		}
		catch ( ParseException e ) {
			throw new ConversionException( String.format( "%s を %s に変換できませんでした。使用した日時フォーマットは %s です。",
															value, type.getName(), Arrays.toString( getPatterns() ) ),
											e );
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	protected Class<Date> getDefaultType()
	{
		return Date.class;
	}

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	public DateConverter()
	{
		super( null );
		setPatterns( getDateTimeFormats() );
	}
}
