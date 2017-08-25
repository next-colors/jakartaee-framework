package jp.co.nextcolors.framework.bean.converter;

import org.jooq.types.ULong;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import jp.co.nextcolors.framework.bean.annotation.BeanConverter;

/**
 * {@link ULong} 型の数値に変換するためのコンバータです。
 *
 * @author hamana
 */
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@BeanConverter(forClass = ULong.class)
public class ULongConverter extends UnsignedNumberConverter<ULong, Long>
{
	//-------------------------------------------------------------------------
	//    Protected Methods
	//-------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	protected ULong getUnsignedValue( @NonNull final Long signedValue ) throws NumberFormatException
	{
		return ULong.valueOf( signedValue );
	}

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * @param defaultValue
	 * 			デフォルト値
	 */
	public ULongConverter( final ULong defaultValue )
	{
		super( defaultValue );
	}
}
