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
