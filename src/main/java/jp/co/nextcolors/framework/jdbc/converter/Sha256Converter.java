package jp.co.nextcolors.framework.jdbc.converter;

import java.util.Objects;

import org.apache.commons.codec.digest.DigestUtils;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * レコードのフィールドを SHA-256 ハッシュ値に変換するためのコンバータです。
 *
 * @author hamana
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
public class Sha256Converter extends HashConverter
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

		return DigestUtils.sha256Hex( userObject );
	}
}
