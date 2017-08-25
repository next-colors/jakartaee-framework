package jp.co.nextcolors.framework.bean.converter;

import org.jooq.types.UByte;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import jp.co.nextcolors.framework.bean.annotation.BeanConverter;

/**
 * {@link UByte} 型の数値に変換するためのコンバータです。
 *
 * @author hamana
 */
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@BeanConverter(forClass = UByte.class)
public class UByteConverter extends UnsignedNumberConverter<UByte, Byte>
{
	//-------------------------------------------------------------------------
	//    Protected Methods
	//-------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	protected UByte getUnsignedValue( @NonNull final Byte signedValue ) throws NumberFormatException
	{
		return UByte.valueOf( signedValue );
	}

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * @param defaultValue
	 * 			デフォルト値
	 */
	public UByteConverter( final UByte defaultValue )
	{
		super( defaultValue );
	}
}
