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
 package jp.co.nextcolors.framework.jdbc.converter;

import java.net.URL;
import java.util.Objects;

import org.apache.commons.beanutils.ConvertUtils;
import org.jooq.impl.AbstractConverter;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * レコードの {@link URL} 型のフィールドを文字列に変換するためのコンバータです。
 *
 * @author hamana
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
public class UrlConverter extends AbstractConverter<String, URL>
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	public UrlConverter()
	{
		super( String.class, URL.class );
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public URL from( final String databaseObject )
	{
		if ( Objects.isNull( databaseObject ) ) {
			return null;
		}

		return URL.class.cast( ConvertUtils.convert( databaseObject, URL.class ) );
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public String to( final URL userObject )
	{
		if ( Objects.isNull( userObject ) ) {
			return null;
		}

		return userObject.toString();
	}
}
