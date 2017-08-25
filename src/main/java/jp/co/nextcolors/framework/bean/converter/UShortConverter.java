package jp.co.nextcolors.framework.bean.converter;

import org.jooq.types.UShort;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import jp.co.nextcolors.framework.bean.annotation.BeanConverter;

/**
 * {@link UShort} 型の数値に変換するためのコンバータです。
 *
 * @author hamana
 */
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@BeanConverter(forClass = UShort.class)
public class UShortConverter extends UnsignedNumberConverter<UShort, Short>
{
	//-------------------------------------------------------------------------
	//    Protected Methods
	//-------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	protected UShort getUnsignedValue( @NonNull final Short signedValue ) throws NumberFormatException
	{
		return UShort.valueOf( signedValue );
	}

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * @param defaultValue
	 * 			デフォルト値
	 */
	public UShortConverter( final UShort defaultValue )
	{
		super( defaultValue );
	}
}
