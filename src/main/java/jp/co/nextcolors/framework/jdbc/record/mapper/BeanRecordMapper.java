package jp.co.nextcolors.framework.jdbc.record.mapper;

import java.util.Objects;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;

import jp.sf.amateras.mirage.naming.DefaultNameConverter;
import jp.sf.amateras.mirage.naming.NameConverter;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;

/**
 * レコードを Bean（JavaBeans）に変換するための {@link RecordMapper} の実装クラスです。
 *
 * @author hamana
 * @param <R>
 * 			レコードの型です。
 * @param <B>
 * 			変換する Bean（JavaBeans）の型です。
 */
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BeanRecordMapper<R extends Record, B> implements RecordMapper<R, B>
{
	//-------------------------------------------------------------------------
	//    Private Properties
	//-------------------------------------------------------------------------
	/**
	 * 変換する Bean（JavaBeans）の型を表すクラスです。
	 *
	 */
	@NonNull
	private final Class<B> beanClass;

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 *
	 */
	@SneakyThrows(ReflectiveOperationException.class)
	@Override
	public B map( final R record )
	{
		if ( Objects.isNull( record ) ) {
			return null;
		}

		NameConverter nameConverter = new DefaultNameConverter();

		B bean = ConstructorUtils.invokeConstructor( beanClass );

		for ( Field<?> field : record.fields() ) {
			String propertyName = nameConverter.columnToProperty( field.getName() );

			if ( !PropertyUtils.isWriteable( bean, propertyName ) ) {
				continue;
			}

			Object value = field.getValue( record );

			if ( Objects.isNull( value ) ) {
				PropertyUtils.setProperty( bean, propertyName, value );
			}
			else {
				BeanUtils.setProperty( bean, propertyName, value );
			}
		}

		return bean;
	}
}
