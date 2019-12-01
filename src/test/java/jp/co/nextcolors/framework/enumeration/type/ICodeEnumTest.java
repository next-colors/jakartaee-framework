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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * {@link ICodeEnum} のテストです。
 *
 * @author hamana
 */
class ICodeEnumTest
{
	//-------------------------------------------------------------------------
	//    Test Preparation
	//-------------------------------------------------------------------------
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public enum Foo implements ICodeEnum<Foo, Integer>
	{
		BAR( 0 ),
		BAZ( 1 );

		@Getter
		@NonNull
		private final Integer code;
	}

	//-------------------------------------------------------------------------
	//    Test
	//-------------------------------------------------------------------------
	/**
	 * {@link ICodeEnum#codeOf(Class, Object)} のテストです。
	 *
	 */
	@Test
	void testCodeOf()
	{
		assertThat( ICodeEnum.codeOf( Foo.class, null ) ).isNull();

		assertThat( ICodeEnum.codeOf( Foo.class, 0 ) ).isEqualTo( Foo.BAR );

		assertThat( ICodeEnum.codeOf( Foo.class, 1 ) ).isEqualTo( Foo.BAZ );

		assertThatExceptionOfType( IllegalArgumentException.class ).isThrownBy( () -> ICodeEnum.codeOf( Foo.class, 2 ) );
	}

	/**
	 * {@link ICodeEnum#isValidCode(Class, Object)} のテストです。
	 *
	 */
	@Test
	void testIsValidCode()
	{
		assertThat( ICodeEnum.isValidCode( Foo.class, 0 ) ).isTrue();

		assertThat( ICodeEnum.isValidCode( Foo.class, 1 ) ).isTrue();

		assertThat( ICodeEnum.isValidCode( Foo.class, 2 ) ).isFalse();
	}

	/**
	 * {@link ICodeEnum#codes(Class)} のテストです。
	 *
	 */
	@Test
	void testCodes()
	{
		assertThat( ICodeEnum.codes( Foo.class ) ).containsExactlyInAnyOrder( 0, 1 );
	}

	/**
	 * {@link ICodeEnum#getCodeClass(Class)} のテストです。
	 *
	 */
	@Test
	void testGetCodeClass()
	{
		assertThat( ICodeEnum.getCodeClass( Foo.class ) ).isEqualTo( Integer.class );
	}
}
