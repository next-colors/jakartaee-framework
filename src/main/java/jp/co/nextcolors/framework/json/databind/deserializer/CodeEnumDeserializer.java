/*
 * Copyright (c) 2017 NEXT COLORS Co., Ltd. All Rights Reserved.
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
 package jp.co.nextcolors.framework.json.databind.deserializer;

import java.io.IOException;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import jp.co.nextcolors.framework.enumeration.type.ICodeEnum;

/**
 * JSON のプロパティをプロパティにコードを持つ列挙型の列挙型定数に変換するための抽象クラスです。
 *
 * @author hamana
 * @param <T>
 * 			列挙型の型です。
 * @param <C>
 * 			列挙型のコードの型です。
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
public abstract class CodeEnumDeserializer<T extends Enum<T> & ICodeEnum<T, C>, C> extends StdDeserializer<T>
{
	//-------------------------------------------------------------------------
	//    Protected Methods
	//-------------------------------------------------------------------------
	/**
	 * @param enumClass
	 * 			列挙型の型を表すクラス
	 */
	protected CodeEnumDeserializer( @NonNull final Class<T> enumClass )
	{
		super( enumClass );
	}

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class<T> handledType()
	{
		return (Class<T>) super.handledType();
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public T deserialize( @NonNull final JsonParser p, @NonNull final DeserializationContext ctxt )
				throws IOException, JsonMappingException
	{
		Class<T> enumClass = handledType();
		Class<C> enumCodeClass = ICodeEnum.getCodeClass( enumClass );

		try {
			C code = enumCodeClass.cast( _parseString( p, ctxt ) );

			return ICodeEnum.codeOf( enumClass, code );
		}
		catch ( Exception e ) {
			throw JsonMappingException.from( p, ExceptionUtils.getMessage( e ), e );
		}
	}
}
