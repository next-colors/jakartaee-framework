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
package jp.co.nextcolors.framework.jdbc.converter;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.jooq.impl.AbstractConverter;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * レコードのフィールドを Base64 変換するためのコンバータです。
 *
 * @author hamana
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
public class Base64Converter extends AbstractConverter<String, String>
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	public Base64Converter()
	{
		super( String.class, String.class );
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public String from( final String databaseObject )
	{
		if ( Objects.isNull( databaseObject ) ) {
			return null;
		}

		return StringUtils.toEncodedString( Base64.decodeBase64( databaseObject ), StandardCharsets.UTF_8 );
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public String to( final String userObject )
	{
		if ( Objects.isNull( userObject ) ) {
			return null;
		}

		return Base64.encodeBase64String( userObject.getBytes( StandardCharsets.UTF_8 ) );
	}
}
