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
