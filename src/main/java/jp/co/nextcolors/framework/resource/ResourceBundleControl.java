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
