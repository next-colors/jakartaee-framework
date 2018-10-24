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
package jp.co.nextcolors.framework.jdbc.record.mapper;

import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DefaultDataType;
import org.jooq.lambda.Unchecked;
import org.jooq.tools.Convert;

import com.miragesql.miragesql.naming.DefaultNameConverter;
import com.miragesql.miragesql.naming.NameConverter;

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
 *          レコードの型です。
 * @param <B>
 *          変換する Bean（JavaBeans）の型です。
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

	/**
	 * {@link RecordMapper} が動作する構成です。
	 *
	 */
	@NonNull
	private final Configuration configuration;

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

		Arrays.stream( record.fields() ).forEach( Unchecked.consumer( field -> {
			String propertyName = nameConverter.columnToProperty( field.getName() );

			if ( !PropertyUtils.isWriteable( bean, propertyName ) ) {
				return;
			}

			Object value = field.getValue( record );

			if ( Objects.nonNull( value ) ) {
				Class<?> propertyType = PropertyUtils.getPropertyType( bean, propertyName );

				DataType<?> dataType = DefaultDataType.getDataType( configuration.dialect(), propertyType );

				value = Convert.convert( value, dataType.getConverter() );
			}

			PropertyUtils.setProperty( bean, propertyName, value );
		} ) );

		return bean;
	}
}
