package jp.co.nextcolors.framework.bean.converter;

import org.jooq.types.UInteger;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import jp.co.nextcolors.framework.bean.annotation.BeanConverter;

/**
 * {@link UInteger} 型の数値に変換するためのコンバータです。
 *
 * @author hamana
 */
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@BeanConverter(forClass = UInteger.class)
public class UIntegerConverter extends UnsignedNumberConverter<UInteger, Integer>
{
	//-------------------------------------------------------------------------
	//    Protected Methods
	//-------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	protected UInteger getUnsignedValue( @NonNull final Integer signedValue ) throws NumberFormatException
	{
		return UInteger.valueOf( signedValue );
	}

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * @param defaultValue
	 * 			デフォルト値
	 */
	public UIntegerConverter( final UInteger defaultValue )
	{
		super( defaultValue );
	}
}
