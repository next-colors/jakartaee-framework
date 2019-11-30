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
package jp.co.nextcolors.framework.util;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

import org.apache.commons.lang3.reflect.TypeUtils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * ジェネリクスを扱うためのユーティリティです。
 *
 * @author hamana
 */
@UtilityClass
public class GenericUtil
{
	//-------------------------------------------------------------------------
	//    Private Methods
	//-------------------------------------------------------------------------
	/**
	 * パラメータ化された型（クラスまたはインタフェース）が持つ型変数および型引数を集めて {@link Map} に追加します。
	 *
	 * @param type
	 *         型
	 * @param map
	 *         パラメータ化された型が持つ型変数をキー、型引数を値とする {@link Map}
	 */
	private static void gatherTypeVariables( @NonNull final Type type, @NonNull final Map<TypeVariable<?>, Type> map )
	{
		if ( ParameterizedType.class.isInstance( type ) ) {
			map.putAll( TypeUtils.getTypeArguments( ParameterizedType.class.cast( type ) ) );
		}
	}

	/**
	 * パラメータ化された型（クラスまたはインタフェース）が持つ型変数および型引数を集めて {@link Map} に追加します。
	 *
	 * @param clazz
	 *         クラス
	 * @param type
	 *         型
	 * @param map
	 *         パラメータ化された型が持つ型変数をキー、型引数を値とする {@link Map}
	 */
	private static void gatherTypeVariables( @NonNull final Class<?> clazz, @NonNull final Type type,
												@NonNull final Map<TypeVariable<?>, Type> map )
	{
		gatherTypeVariables( type, map );

		Class<?> superClass = clazz.getSuperclass();
		Type superClassType = clazz.getGenericSuperclass();

		if ( Objects.nonNull( superClass ) ) {
			gatherTypeVariables( superClass, superClassType, map );
		}

		Class<?>[] interfaces = clazz.getInterfaces();
		Type[] interfaceTypes = clazz.getGenericInterfaces();

		IntStream.range( 0, interfaces.length ).forEach( i ->
			gatherTypeVariables( interfaces[ i ], interfaceTypes[ i ], map )
		);
	}

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * 型の原型を返します。
	 *
	 * @param type
	 *         型
	 * @return 型の原型
	 */
	public static Class<?> getRawClass( @NonNull final Type type )
	{
		return TypeUtils.getRawType( type, null );
	}

	/**
	 * 型の型引数の配列を返します。<br>
	 * 型がパラメータ化された型ではない場合は {@code null} を返します。
	 *
	 * @param type
	 *         型
	 * @return 型の型引数の配列
	 */
	public static Type[] getGenericParameters( @NonNull final Type type )
	{
		if ( ParameterizedType.class.isInstance( type ) ) {
			ParameterizedType parameterizedType = ParameterizedType.class.cast( type );

			return parameterizedType.getActualTypeArguments();
		}

		if ( GenericArrayType.class.isInstance( type ) ) {
			GenericArrayType genericArrayType = GenericArrayType.class.cast( type );

			return getGenericParameters( genericArrayType.getGenericComponentType() );
		}

		return null;
	}

	/**
	 * 指定された位置の型の型引数を返します。<br>
	 * 型がパラメータ化された型ではない場合は {@code null} を返します。
	 *
	 * @param type
	 *         型
	 * @param index
	 *         位置
	 * @return 指定された位置の型の型引数
	 */
	public static Type getGenericParameter( @NonNull final Type type, final int index )
	{
		if ( !ParameterizedType.class.isInstance( type ) ) {
			return null;
		}

		Type[] genericParameter = getGenericParameters( type );

		if ( Objects.isNull( genericParameter ) ) {
			return null;
		}

		return genericParameter[ index ];
	}

	/**
	 * パラメータ化された型を要素とする配列の要素型を返します。<br>
	 * 型がパラメータ化された型の配列ではない場合は {@code null} を返します。
	 *
	 * @param type
	 *         パラメータ化された型を要素とする配列
	 * @return パラメータ化された型を要素とする配列の要素型
	 */
	public static Type getElementTypeOfArray( @NonNull final Type type )
	{
		return TypeUtils.getArrayComponentType( type );
	}

	/**
	 * パラメータ化された {@link Collection} の要素型を返します。<br>
	 * 型がパラメータ化された {@link Collection} ではない場合は {@code null} を返します。
	 *
	 * @param type
	 *         パラメータ化された {@link Collection}
	 * @return パラメータ化された {@link Collection} の要素型
	 */
	public static Type getElementTypeOfCollection( @NonNull final Type type )
	{
		if ( !TypeUtils.isAssignable( type, Collection.class ) ) {
			return null;
		}

		return getGenericParameter( type, 0 );
	}

	/**
	 * パラメータ化された {@link List} の要素型を返します。<br>
	 * 型がパラメータ化された {@link List} ではない場合は {@code null} を返します。
	 *
	 * @param type
	 *         パラメータ化された {@link List}
	 * @return パラメータ化された {@link List} の要素型
	 */
	public static Type getElementTypeOfList( @NonNull final Type type )
	{
		if ( !TypeUtils.isAssignable( type, List.class ) ) {
			return null;
		}

		return getGenericParameter( type, 0 );
	}

	/**
	 * パラメータ化された {@link Set} の要素型を返します。<br>
	 * 型がパラメータ化された {@link Set} ではない場合は {@code null} を返します。
	 *
	 * @param type
	 *         パラメータ化された {@link Set}
	 * @return パラメータ化された {@link Set} の要素型
	 */
	public static Type getElementTypeOfSet( @NonNull final Type type )
	{
		if ( !TypeUtils.isAssignable( type, Set.class ) ) {
			return null;
		}

		return getGenericParameter( type, 0 );
	}

	/**
	 * パラメータ化された {@link Map} のキーの型を返します。<br>
	 * 型がパラメータ化された {@link Map} ではない場合は {@code null} を返します。
	 *
	 * @param type
	 *         パラメータ化された {@link Map}
	 * @return パラメータ化された {@link Map} のキーの型
	 */
	public static Type getKeyTypeOfMap( @NonNull final Type type )
	{
		if ( !TypeUtils.isAssignable( type, Map.class ) ) {
			return null;
		}

		return getGenericParameter( type, 0 );
	}

	/**
	 * パラメータ化された {@link Map} の値の型を返します。<br>
	 * 型がパラメータ化された {@link Map} ではない場合は {@code null} を返します。
	 *
	 * @param type
	 *         パラメータ化された {@link Map}
	 * @return パラメータ化された {@link Map} の値の型
	 */
	public static Type getValueTypeOfMap( @NonNull final Type type )
	{
		if ( !TypeUtils.isAssignable( type, Map.class ) ) {
			return null;
		}

		return getGenericParameter( type, 1 );
	}

	/**
	 * パラメータ化された型（クラスまたはインタフェース）が持つ型変数をキー、型引数を値とする {@link Map} を返します。
	 *
	 * @param clazz
	 *         パラメータ化された型（クラスまたはインタフェース）
	 * @return パラメータ化された型が持つ型変数をキー、型引数を値とする {@link Map}
	 */
	public static Map<TypeVariable<?>, Type> getTypeVariableMap( @NonNull final Class<?> clazz )
	{
		Map<TypeVariable<?>, Type> map = new LinkedHashMap<>();

		Arrays.stream( clazz.getTypeParameters() ).forEach( typeParameter ->
			map.put( typeParameter, getActualClass( typeParameter.getBounds()[ 0 ], map ) )
		);

		gatherTypeVariables( clazz, clazz, map );

		return map;
	}

	/**
	 * 型の実際の型を返します。
	 * <ul>
	 *     <li>型が {@link Class} の場合はそのまま返します。</li>
	 *     <li>型がワイルドカード型の場合は（最初の）上限境界を返します。</li>
	 *     <li>型が型変数で引数 {@code map} のキーとして含まれている場合はその変数の実際の型引数を返します。</li>
	 *     <li>型が型変数で引数 {@code map} のキーとして含まれていない場合は（最初の）上限境界を返します。</li>
	 *     <li>型が配列の場合はその要素の実際の型の配列を返します。</li>
	 *     <li>その他の場合は {@code null} を返します。</li>
	 * </ul>
	 *
	 * @param type
	 *         型
	 * @param map
	 *         パラメータ化された型が持つ型変数をキー、型引数を値とする {@link Map}
	 * @return 型の実際の型
	 */
	public static Class<?> getActualClass( @NonNull final Type type, @NonNull final Map<TypeVariable<?>, Type> map )
	{
		if ( Class.class.isInstance( type ) ) {
			return Class.class.cast( type );
		}

		if ( ParameterizedType.class.isInstance( type ) ) {
			ParameterizedType parameterizedType = ParameterizedType.class.cast( type );

			return getActualClass( parameterizedType.getRawType(), map );
		}

		if ( WildcardType.class.isInstance( type ) ) {
			WildcardType wildcardType = WildcardType.class.cast( type );

			return getActualClass( wildcardType.getUpperBounds()[ 0 ], map );
		}

		if ( TypeVariable.class.isInstance( type ) ) {
			TypeVariable<?> typeVariable = TypeVariable.class.cast( type );

			if ( map.containsKey( typeVariable ) ) {
				return getActualClass( map.get( typeVariable ), map );
			}

			return getActualClass( typeVariable.getBounds()[ 0 ], map );
		}

		if ( GenericArrayType.class.isInstance( type ) ) {
			GenericArrayType genericArrayType = GenericArrayType.class.cast( type );

			Class<?> componentClass = getActualClass( genericArrayType.getGenericComponentType(), map );

			return Array.newInstance( componentClass, 0 ).getClass();
		}

		return null;
	}

	/**
	 * パラメータ化された型を要素とする配列の実際の要素型を返します。
	 * <ul>
	 *     <li>型がパラメータ化された型の配列ではない場合は {@code null} を返します。</li>
	 *     <li>型が {@link Class} の場合はそのまま返します。</li>
	 *     <li>型がパラメータ化された型の場合はその原型を返します。</li>
	 *     <li>型がワイルドカード型の場合は（最初の）上限境界を返します。</li>
	 *     <li>型が型変数の場合はその変数の実際の型引数を返します。</li>
	 *     <li>型が配列の場合はその要素の実際の型の配列を返します。</li>
	 *     <li>その他の場合は {@code null} を返します。</li>
	 * </ul>
	 *
	 * @param type
	 *         パラメータ化された型を要素とする配列
	 * @param map
	 *         パラメータ化された型が持つ型変数をキー、型引数を値とする {@link Map}
	 * @return パラメータ化された型を要素とする配列の実際の要素型
	 */
	public static Class<?> getActualElementClassOfArray( @NonNull final Type type, @NonNull final Map<TypeVariable<?>, Type> map )
	{
		return getActualClass( TypeUtils.getArrayComponentType( type ), map );
	}

	/**
	 * パラメータ化された {@link Collection} の実際の要素型を返します。
	 * <ul>
	 *     <li>型がパラメータ化された {@link Collection} ではない場合は {@code null} を返します。</li>
	 *     <li>型が {@link Class} の場合はそのまま返します。</li>
	 *     <li>型がパラメータ化された型の場合はその原型を返します。</li>
	 *     <li>型がワイルドカード型の場合は（最初の）上限境界を返します。</li>
	 *     <li>型が型変数の場合はその変数の実際の型引数を返します。</li>
	 *     <li>型が配列の場合はその要素の実際の型の配列を返します。</li>
	 *     <li>その他の場合は {@code null} を返します。</li>
	 * </ul>
	 *
	 * @param type
	 *         パラメータ化された {@link Collection}
	 * @param map
	 *         パラメータ化された型が持つ型変数をキー、型引数を値とする {@link Map}
	 * @return パラメータ化された {@link Collection} の実際の要素型
	 */
	public static Class<?> getActualElementClassOfCollection( @NonNull final Type type, @NonNull final Map<TypeVariable<?>, Type> map )
	{
		if ( !TypeUtils.isAssignable( type, Collection.class ) ) {
			return null;
		}

		return getActualClass( getGenericParameter( type, 0 ), map );
	}

	/**
	 * パラメータ化された {@link List} の実際の要素型を返します。
	 * <ul>
	 *     <li>型がパラメータ化された {@link List} ではない場合は {@code null} を返します。</li>
	 *     <li>型が {@link Class} の場合はそのまま返します。</li>
	 *     <li>型がパラメータ化された型の場合はその原型を返します。</li>
	 *     <li>型がワイルドカード型の場合は（最初の）上限境界を返します。</li>
	 *     <li>型が型変数の場合はその変数の実際の型引数を返します。</li>
	 *     <li>型が配列の場合はその要素の実際の型の配列を返します。</li>
	 *     <li>その他の場合は {@code null} を返します。</li>
	 * </ul>
	 *
	 * @param type
	 *         パラメータ化された {@link List}
	 * @param map
	 *         パラメータ化された型が持つ型変数をキー、型引数を値とする {@link Map}
	 * @return パラメータ化された {@link List} の実際の要素型
	 */
	public static Class<?> getActualElementClassOfList( @NonNull final Type type, @NonNull final Map<TypeVariable<?>, Type> map )
	{
		if ( !TypeUtils.isAssignable( type, List.class ) ) {
			return null;
		}

		return getActualClass( getGenericParameter( type, 0 ), map );
	}

	/**
	 * パラメータ化された {@link Set} の実際の要素型を返します。
	 * <ul>
	 *     <li>型がパラメータ化された {@link Set} ではない場合は {@code null} を返します。</li>
	 *     <li>型が {@link Class} の場合はそのまま返します。</li>
	 *     <li>型がパラメータ化された型の場合はその原型を返します。</li>
	 *     <li>型がワイルドカード型の場合は（最初の）上限境界を返します。</li>
	 *     <li>型が型変数の場合はその変数の実際の型引数を返します。</li>
	 *     <li>型が配列の場合はその要素の実際の型の配列を返します。</li>
	 *     <li>その他の場合は {@code null} を返します。</li>
	 * </ul>
	 *
	 * @param type
	 *         パラメータ化された {@link Set}
	 * @param map
	 *         パラメータ化された型が持つ型変数をキー、型引数を値とする {@link Map}
	 * @return パラメータ化された {@link Set} の実際の要素型
	 */
	public static Class<?> getActualElementClassOfSet( @NonNull final Type type, @NonNull final Map<TypeVariable<?>, Type> map )
	{
		if ( !TypeUtils.isAssignable( type, Set.class ) ) {
			return null;
		}

		return getActualClass( getGenericParameter( type, 0 ), map );
	}

	/**
	 * パラメータ化された {@link Map} のキーの実際の型を返します。
	 * <ul>
	 *     <li>型がパラメータ化された {@link Map} ではない場合は {@code null} を返します。</li>
	 *     <li>型が {@link Class} の場合はそのまま返します。</li>
	 *     <li>型がパラメータ化された型の場合はその原型を返します。</li>
	 *     <li>型がワイルドカード型の場合は（最初の）上限境界を返します。</li>
	 *     <li>型が型変数の場合はその変数の実際の型引数を返します。</li>
	 *     <li>型が配列の場合はその要素の実際の型の配列を返します。</li>
	 *     <li>その他の場合は {@code null} を返します。</li>
	 * </ul>
	 *
	 * @param type
	 *         パラメータ化された {@link Map}
	 * @param map
	 *         パラメータ化された型が持つ型変数をキー、型引数を値とする {@link Map}
	 * @return パラメータ化された {@link Map} のキーの実際の型
	 */
	public static Class<?> getActualKeyClassOfMap( @NonNull final Type type, @NonNull final Map<TypeVariable<?>, Type> map )
	{
		if ( !TypeUtils.isAssignable( type, Map.class ) ) {
			return null;
		}

		return getActualClass( getGenericParameter( type, 0 ), map );
	}

	/**
	 * パラメータ化された {@link Map} の値の実際の型を返します。
	 * <ul>
	 *     <li>型がパラメータ化された {@link Map} ではない場合は {@code null} を返します。</li>
	 *     <li>型が {@link Class} の場合はそのまま返します。</li>
	 *     <li>型がパラメータ化された型の場合はその原型を返します。</li>
	 *     <li>型がワイルドカード型の場合は（最初の）上限境界を返します。</li>
	 *     <li>型が型変数の場合はその変数の実際の型引数を返します。</li>
	 *     <li>型が配列の場合はその要素の実際の型の配列を返します。</li>
	 *     <li>その他の場合は {@code null} を返します。</li>
	 * </ul>
	 *
	 * @param type
	 *         パラメータ化された {@link Map}
	 * @param map
	 *         パラメータ化された型が持つ型変数をキー、型引数を値とする {@link Map}
	 * @return パラメータ化された {@link Map} の値の実際の型
	 */
	public static Class<?> getActualValueClassOfMap( @NonNull final Type type, @NonNull final Map<TypeVariable<?>, Type> map )
	{
		if ( !TypeUtils.isAssignable( type, Map.class ) ) {
			return null;
		}

		return getActualClass( getGenericParameter( type, 1 ), map );
	}
}
