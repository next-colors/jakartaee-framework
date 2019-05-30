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
package jp.co.nextcolors.framework.enumeration.type;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.IterableUtils;

import lombok.NonNull;

import jp.co.nextcolors.framework.util.GenericUtil;

/**
 * プロパティにコードを持つ列挙型を扱うためのインターフェースです。
 *
 * @author hamana
 * @param <T>
 *         列挙型の型です。
 * @param <V>
 *         列挙型のコードの型です。
 */
public interface ICodeEnum<T extends Enum<T> & ICodeEnum<T, V>, V>
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * 指定したコードを持つ指定した列挙型の列挙型定数を返します。<br>
	 * 指定したコードが {@code null} の場合は、{@code null} を返します。
	 *
	 * @param <T>
	 *         列挙型の型
	 * @param <V>
	 *         列挙型のコードの型
	 * @param enumClass
	 *         列挙型の型を表すクラス
	 * @param code
	 *         コード
	 * @return 列挙型定数
	 * @throws IllegalArgumentException
	 *         指定した列挙型に指定したコードの列挙型定数がない場合
	 */
	static <T extends Enum<T> & ICodeEnum<T, V>, V> T codeOf( @NonNull final Class<T> enumClass, final V code )
														throws IllegalArgumentException
	{
		if ( Objects.isNull( code ) ) {
			return null;
		}

		return Arrays.stream( enumClass.getEnumConstants() )
						.filter( constant -> Objects.equals( constant.getCode(), code ) )
						.findFirst()
						.orElseThrow( () ->
							new IllegalArgumentException( String.format( "%s のコード %s に %s は含まれていません。",
																			enumClass.getName(), codes( enumClass ), code ) )
						);
	}

	/**
	 * 指定したコードを持つ指定した列挙型の列挙型定数が存在するかどうかを判定します。
	 *
	 * @param <T>
	 *         列挙型の型
	 * @param <V>
	 *         列挙型のコードの型
	 * @param enumClass
	 *         列挙型の型を表すクラス
	 * @param code
	 *         コード
	 * @return 指定したコードを持つ指定した列挙型の列挙型定数が存在する場合は {@code true}、そうではない場合は {@code false}
	 */
	static <T extends Enum<T> & ICodeEnum<T, V>, V> boolean isValidCode( @NonNull final Class<T> enumClass, final V code )
	{
		return IterableUtils.contains( codes( enumClass ), code );
	}

	/**
	 * すべてのコードを返します。
	 *
	 * @param <T>
	 *         列挙型の型
	 * @param <V>
	 *         列挙型のコードの型
	 * @param enumClass
	 *         列挙型の型を表すクラス
	 * @return すべてのコード
	 */
	static <T extends Enum<T> & ICodeEnum<T, V>, V> Set<V> codes( @NonNull final Class<T> enumClass )
	{
		return Arrays.stream( enumClass.getEnumConstants() ).map( constant -> constant.getCode() ).collect( Collectors.toUnmodifiableSet() );
	}

	/**
	 * 列挙型のコードの型を表すクラスを返します。
	 *
	 * @param <T>
	 *         列挙型の型
	 * @param <V>
	 *         列挙型のコードの型
	 * @param enumClass
	 *         列挙型の型を表すクラス
	 * @return 列挙型のコードの型を表すクラス
	 */
	@SuppressWarnings("unchecked")
	static <T extends Enum<T> & ICodeEnum<T, V>, V> Class<V> getCodeClass( @NonNull final Class<T> enumClass )
	{
		Map<TypeVariable<?>, Type> typeVariableMap = GenericUtil.getTypeVariableMap( enumClass );

		for ( Type interfaceType : enumClass.getGenericInterfaces() ) {
			if ( GenericUtil.getRawClass( interfaceType ) == ICodeEnum.class ) {
				Type[] paramTypes = GenericUtil.getGenericParameters( interfaceType );

				return (Class<V>) GenericUtil.getActualClass( paramTypes[ 1 ], typeVariableMap );
			}
		}

		throw new RuntimeException( "列挙型のコードの型を表すクラスを取得できませんでした。" );
	}

	/**
	 * コードを取得します。
	 *
	 * @return コード
	 */
	V getCode();
}
