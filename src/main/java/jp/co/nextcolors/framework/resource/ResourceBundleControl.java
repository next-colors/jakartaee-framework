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
package jp.co.nextcolors.framework.resource;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * {@link ResourceBundle.Control} の拡張クラスです。<br>
 * キャッシュ期間、リソースのフォーマットを設定します。
 *
 * @author hamana
 */
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResourceBundleControl extends Control
{
	//-------------------------------------------------------------------------
	//    Public Constants
	//-------------------------------------------------------------------------
	/**
	 * キャッシュ期間 1 分を表します。
	 *
	 */
	public static final long TTL_1MIN = Duration.ofMinutes( 1 ).toMillis();

	/**
	 * キャッシュ期間 5 分を表します。
	 *
	 */
	public static final long TTL_5MIN = Duration.ofMinutes( 5 ).toMillis();

	/**
	 * キャッシュ期間 10 分を表します。
	 *
	 */
	public static final long TTL_10MIN = Duration.ofMinutes( 10 ).toMillis();

	/**
	 * キャッシュ期間 30 分を表します。
	 *
	 */
	public static final long TTL_30MIN = Duration.ofMinutes( 30 ).toMillis();

	/**
	 * キャッシュ期間 1 時間を表します。
	 *
	 */
	public static final long TTL_1H = Duration.ofHours( 1 ).toMillis();

	//-------------------------------------------------------------------------
	//    Private Properties
	//-------------------------------------------------------------------------
	/**
	 * リソースのキャッシュ期間です。
	 *
	 */
	private final long timeToLive;

	/**
	 * リソースのフォーマットです。
	 *
	 */
	@NonNull
	private final List<String> format;

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	public ResourceBundleControl()
	{
		timeToLive = TTL_NO_EXPIRATION_CONTROL;
		format = FORMAT_DEFAULT;
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public long getTimeToLive( @NonNull final String baseName, @NonNull final Locale locale )
	{
		return timeToLive;
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public List<String> getFormats( @NonNull final String baseName )
	{
		return format;
	}
}
