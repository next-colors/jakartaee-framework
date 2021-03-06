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

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.math.NumberUtils;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import jp.co.nextcolors.framework.util.GenericUtil;

/**
 * 列挙型の列挙型定数に変換するための抽象クラスです。
 *
 * @author hamana
 * @param <E>
 *         列挙型の型です。
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class EnumConverter<E extends Enum<E>> extends AbstractConverter
{
	//-------------------------------------------------------------------------
	//    Private Properties
	//-------------------------------------------------------------------------
	/**
	 * 列挙型の型を表すクラスです。
	 *
	 */
	private final Class<E> enumClass;

	//-------------------------------------------------------------------------
	//    Private Methods
	//-------------------------------------------------------------------------
	/**
	 * 指定した序数の列挙型定数に変換します。
	 *
	 * @param ordinal
	 *         序数
	 * @return 列挙型定数
	 * @throws ConversionException
	 *         指定した序数の列挙型定数がない場合
	 */
	private E convert( final int ordinal )
	{
		Map<Integer, E> constants = Arrays.stream( enumClass.getEnumConstants() ).collect( Collectors.toMap( Enum::ordinal, Function.identity() ) );

		if ( !constants.containsKey( ordinal ) ) {
			throw new ConversionException( String.format( "序数 %s に %s は含まれていません。", constants.keySet(), ordinal ) );
		}

		return constants.get( ordinal );
	}

	/**
	 * 指定した名前の列挙型定数に変換します。
	 *
	 * @param name
	 *         名前
	 * @return 列挙型定数
	 * @throws ConversionException
	 *         指定した名前の列挙型定数がない場合
	 */
	private E convert( @NonNull final String name )
	{
		Map<String, E> constants = EnumUtils.getEnumMap( enumClass );

		if ( !constants.containsKey( name ) ) {
			throw new ConversionException( String.format( "名前 %s に %s は含まれていません。", constants.keySet(), name ) );
		}

		return constants.get( name );
	}

	//-------------------------------------------------------------------------
	//    Protected Methods
	//-------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	protected EnumConverter()
	{
		super( null );

		Map<TypeVariable<?>, Type> typeVariableMap = GenericUtil.getTypeVariableMap( getClass() );

		for ( Class<?> clazz = getClass(); clazz != Object.class; clazz = clazz.getSuperclass() ) {
			if ( clazz.getSuperclass() == EnumConverter.class ) {
				Type[] paramTypes = GenericUtil.getGenericParameters( clazz.getGenericSuperclass() );
				enumClass = (Class<E>) GenericUtil.getActualClass( paramTypes[ 0 ], typeVariableMap );

				return;
			}
		}

		throw new RuntimeException( "列挙型の型を表すクラスを設定できませんでした。" );
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	protected <T> T convertToType( @NonNull final Class<T> type, @NonNull final Object value ) throws Throwable
	{
		String key = Objects.toString( value );

		if ( NumberUtils.isDigits( key ) ) {
			return type.cast( convert( NumberUtils.toInt( key ) ) );
		}

		return type.cast( convert( key ) );
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	protected Class<E> getDefaultType()
	{
		return enumClass;
	}
}
