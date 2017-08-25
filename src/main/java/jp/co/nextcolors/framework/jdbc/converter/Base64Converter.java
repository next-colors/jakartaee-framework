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
