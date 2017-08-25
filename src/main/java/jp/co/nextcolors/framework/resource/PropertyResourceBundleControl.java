package jp.co.nextcolors.framework.resource;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * {@link ResourceBundleControl} の拡張クラスです。<br>
 * *.properties ファイルからリソースを取得するための設定を行います。
 *
 * @author hamana
 */
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PropertyResourceBundleControl extends ResourceBundleControl
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * @param timeToLive
	 * 			キャッシュ期間
	 */
	public PropertyResourceBundleControl( final long timeToLive )
	{
		super( timeToLive, FORMAT_PROPERTIES );
	}
}
