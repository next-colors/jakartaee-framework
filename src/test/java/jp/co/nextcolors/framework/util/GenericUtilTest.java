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

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import lombok.SneakyThrows;

/**
 * {@link GenericUtil} のテストです。
 *
 * @author hamana
 */
public class GenericUtilTest
{
	//-------------------------------------------------------------------------
	//    Test Preparation
	//-------------------------------------------------------------------------
	public interface ArrayType
	{
		Class<String>[] arrayOfStringClass();
	}

	public interface Foo<T1, T2>
	{
		T2 foo( T1 foo );
	}

	public interface Bar extends Foo<Integer, Long>
	{
	}

	public interface Baz<T1, T2>
	{
		T1[] array();

		List<T2> list();

		Set<T1> set();

		Map<T1, T2> map();
	}

	public static abstract class Qux implements Bar, Baz<String, Boolean>
	{
	}

	public interface Quux
	{
		<T> T getQuux();
	}

	//-------------------------------------------------------------------------
	//    Test
	//-------------------------------------------------------------------------
	/**
	 * クラスに関するテストです。
	 *
	 */
	@Test
	public void testClass()
	{
		Map<TypeVariable<?>, Type> map = GenericUtil.getTypeVariableMap( Foo.class );
		assertThat( map ).isNotEmpty();
		assertThat( map.get( Foo.class.getTypeParameters()[ 0 ] ) ).isEqualTo( Object.class );
	}

	/**
	 * ジェネリクスメソッドに関するテストです。
	 *
	 */
	@SneakyThrows(NoSuchMethodException.class)
	@Test
	public void testGenericMethod()
	{
		Map<TypeVariable<?>, Type> map = GenericUtil.getTypeVariableMap( Quux.class );
		assertThat( map ).isEmpty();

		Method method = Quux.class.getMethod( "getQuux" );
		Class<?> actualClass = GenericUtil.getActualClass( method.getGenericReturnType(), map );
		assertThat( actualClass ).isEqualTo( Object.class );
	}

	/**
	 * 配列に関するテストです。
	 *
	 */
	@SneakyThrows(NoSuchMethodException.class)
	@Test
	public void testArray()
	{
		Method method = ArrayType.class.getMethod( "arrayOfStringClass" );
		Type elementType = GenericUtil.getElementTypeOfArray( method.getGenericReturnType() );
		Class<?> rawClass = GenericUtil.getRawClass( elementType );
		Type genericParameter = GenericUtil.getGenericParameter( elementType, 0 );
		assertThat( rawClass ).isEqualTo( Class.class );
		assertThat( genericParameter ).isEqualTo( String.class );
	}

	/**
	 * {@link GenericUtil#getTypeVariableMap(Class)} のテストです。
	 *
	 */
	@Test
	public void testGetTypeVariableMap()
	{
		Map<TypeVariable<?>, Type> map = GenericUtil.getTypeVariableMap( Qux.class );
		assertThat( map ).hasSize( 4 );
		assertThat( map.keySet() ).extracting( TypeVariable::getName ).containsExactlyInAnyOrder( "T1", "T2", "T1", "T2" );
		assertThat( map ).containsValues( Integer.class, Long.class, String.class, Boolean.class );
	}

	/**
	 * {@link GenericUtil#getActualClass(Type, Map)} のテストです。
	 *
	 */
	@SneakyThrows(NoSuchMethodException.class)
	@Test
	public void testGetActualClass()
	{
		Map<TypeVariable<?>, Type> map = GenericUtil.getTypeVariableMap( Qux.class );

		Method method = Qux.class.getMethod( "foo", Object.class );
		Class<?> actualClass = GenericUtil.getActualClass( method.getGenericParameterTypes()[ 0 ], map );
		assertThat( actualClass ).isEqualTo( Integer.class );
		actualClass = GenericUtil.getActualClass( method.getGenericReturnType(), map );
		assertThat( actualClass ).isEqualTo( Long.class );

		method = Qux.class.getMethod( "array" );
		actualClass = GenericUtil.getActualClass( method.getGenericReturnType(), map );
		assertThat( actualClass ).isEqualTo( String[].class );

		method = Qux.class.getMethod( "list" );
		actualClass = GenericUtil.getActualClass( method.getGenericReturnType(), map );
		assertThat( actualClass ).isEqualTo( List.class );

		method = Qux.class.getMethod( "set" );
		actualClass = GenericUtil.getActualClass( method.getGenericReturnType(), map );
		assertThat( actualClass ).isEqualTo( Set.class );

		method = Qux.class.getMethod( "map" );
		actualClass = GenericUtil.getActualClass( method.getGenericReturnType(), map );
		assertThat( actualClass ).isEqualTo( Map.class );
	}

	/**
	 * {@link GenericUtil#getActualElementClassOfArray(Type, Map)} のテストです。
	 *
	 */
	@SneakyThrows(NoSuchMethodException.class)
	@Test
	public void testGetActualElementClassOfArray()
	{
		Map<TypeVariable<?>, Type> map = GenericUtil.getTypeVariableMap( Qux.class );
		Method method = Qux.class.getMethod( "array" );
		Class<?> elementClass = GenericUtil.getActualElementClassOfArray( method.getGenericReturnType(), map );
		assertThat( elementClass ).isEqualTo( String.class );
	}

	/**
	 * {@link GenericUtil#getActualElementClassOfList(Type, Map)} のテストです。
	 *
	 */
	@SneakyThrows(NoSuchMethodException.class)
	@Test
	public void testGetActualElementClassOfList()
	{
		Map<TypeVariable<?>, Type> map = GenericUtil.getTypeVariableMap( Qux.class );
		Method method = Qux.class.getMethod( "list" );
		Class<?> elementClass = GenericUtil.getActualElementClassOfList( method.getGenericReturnType(), map );
		assertThat( elementClass ).isEqualTo( Boolean.class );
	}

	/**
	 * {@link GenericUtil#getActualElementClassOfSet(Type, Map)} のテストです。
	 *
	 */
	@SneakyThrows(NoSuchMethodException.class)
	@Test
	public void testGetActualElementClassOfSet()
	{
		Map<TypeVariable<?>, Type> map = GenericUtil.getTypeVariableMap( Qux.class );
		Method method = Qux.class.getMethod( "set" );
		Class<?> elementClass = GenericUtil.getActualElementClassOfSet( method.getGenericReturnType(), map );
		assertThat( elementClass ).isEqualTo( String.class );
	}

	/**
	 * {@link GenericUtil#getActualKeyClassOfMap(Type, Map)} のテストです。
	 *
	 */
	@SneakyThrows(NoSuchMethodException.class)
	@Test
	public void testGetActualKeyClassOfMap()
	{
		Map<TypeVariable<?>, Type> map = GenericUtil.getTypeVariableMap( Qux.class );
		Method method = Qux.class.getMethod( "map" );
		Class<?> keyClass = GenericUtil.getActualKeyClassOfMap( method.getGenericReturnType(), map );
		assertThat( keyClass ).isEqualTo( String.class );
	}

	/**
	 * {@link GenericUtil#getActualValueClassOfMap(Type, Map)} のテストです。
	 *
	 */
	@SneakyThrows(NoSuchMethodException.class)
	@Test
	public void testGetActualValueClassOfMap()
	{
		Map<TypeVariable<?>, Type> map = GenericUtil.getTypeVariableMap( Qux.class );
		Method method = Qux.class.getMethod( "map" );
		Class<?> valueClass = GenericUtil.getActualValueClassOfMap( method.getGenericReturnType(), map );
		assertThat( valueClass ).isEqualTo( Boolean.class );
	}
}
