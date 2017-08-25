package jp.co.nextcolors.framework.jdbc.converter;

import java.util.Objects;

import org.apache.commons.codec.digest.DigestUtils;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * レコードのフィールドを SHA-512 ハッシュ値に変換するためのコンバータです。
 *
 * @author hamana
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
public class Sha512Converter extends HashConverter
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
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

		return DigestUtils.sha512Hex( userObject );
	}
}
