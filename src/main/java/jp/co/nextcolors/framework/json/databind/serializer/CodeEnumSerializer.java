package jp.co.nextcolors.framework.json.databind.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import jp.co.nextcolors.framework.enumeration.type.ICodeEnum;

/**
 * プロパティにコードを持つ列挙型の列挙型定数を JSON のプロパティに変換するための抽象クラスです。
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
public abstract class CodeEnumSerializer<T extends Enum<T> & ICodeEnum<T, C>, C> extends StdSerializer<T>
{
	//-------------------------------------------------------------------------
	//    Protected Methods
	//-------------------------------------------------------------------------
	/**
	 * @param enumClass
	 * 			列挙型の型を表すクラス
	 */
	protected CodeEnumSerializer( @NonNull final Class<T> enumClass )
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
	@Override
	public JsonNode getSchema( @NonNull final SerializerProvider provider, @NonNull final Type typeHint )
					throws JsonMappingException
	{
		Class<C> enumCodeClass = ICodeEnum.getCodeClass( handledType() );

		String type;

		if ( ClassUtils.isAssignable( enumCodeClass, Number.class ) ) {
			type = Number.class.getSimpleName();
		}
		else if ( enumCodeClass == Boolean.class ) {
			type = Boolean.class.getSimpleName();
		}
		else {
			type = String.class.getSimpleName();
		}

		return createSchemaNode( StringUtils.lowerCase( type ) );
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void serialize( @NonNull final T value, @NonNull final JsonGenerator gen, @NonNull final SerializerProvider provider )
				throws IOException
	{
		gen.writeObject( value.getCode() );
	}
}
