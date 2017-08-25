package jp.co.nextcolors.framework.jdbc.converter;

import org.jooq.impl.AbstractConverter;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * レコードのフィールドをハッシュ値に変換するための抽象クラスです。
 *
 * @author hamana
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
public abstract class HashConverter extends AbstractConverter<String, String>
{
	//-------------------------------------------------------------------------
	//    Protected Methods
	//-------------------------------------------------------------------------
	protected HashConverter()
	{
		super( String.class, String.class );
	}

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public String from( final String databaseObject )
	{
		return databaseObject;
	}
}
