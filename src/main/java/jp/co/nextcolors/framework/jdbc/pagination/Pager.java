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
package jp.co.nextcolors.framework.jdbc.pagination;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.OrderField;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.SelectSeekStepN;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.impl.DSL;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import jp.co.nextcolors.framework.data.pagination.IPage;
import jp.co.nextcolors.framework.data.pagination.IPageRequest;
import jp.co.nextcolors.framework.data.pagination.Page;
import jp.co.nextcolors.framework.jdbc.query.ISqlFileSelect;
import jp.co.nextcolors.framework.jdbc.query.SqlFileSelect;
import jp.co.nextcolors.framework.jdbc.record.mapper.BeanRecordMapper;

/**
 * {@link IPager} の実装クラスです。
 *
 * @author hamana
 * @param <T>
 *         ページに含まれるレコードの型です。
 */
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Pager<T> implements IPager<T>
{
	//-------------------------------------------------------------------------
	//    Private Properties
	//-------------------------------------------------------------------------
	/**
	 * DSL コンテキストです。
	 *
	 */
	@NonNull
	private final DSLContext dslContext;

	/**
	 * ページに含まれるレコードの型を表すクラスです。
	 *
	 */
	@NonNull
	private final Class<T> resultClass;

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public <R extends TableRecord<R>> IPage<T> fetchPage( @NonNull final IPageRequest pageRequest,
															@NonNull final Table<R> table,
															final Condition condition,
															@NonNull final OrderField<?>... order )
	{
		return fetchPage( pageRequest, table, condition, Arrays.asList( order ) );
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public <R extends TableRecord<R>> IPage<T> fetchPage( @NonNull final IPageRequest pageRequest,
															@NonNull final Table<R> table,
															final Condition condition,
															@NonNull final Collection<? extends OrderField<?>> order )
	{
		SelectSeekStepN<R> query = dslContext.selectFrom( table ).where( condition ).orderBy( order );

		int totalElements = dslContext.fetchCount( query );

		List<T> elements = query.offset( pageRequest.getOffset() )
								.limit( pageRequest.getPageSize() )
							.fetchInto( resultClass );

		return new Page<>( pageRequest, elements, totalElements );
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public IPage<T> fetchPageBySqlFile( @NonNull final IPageRequest pageRequest, @NonNull final File sqlFile )
	{
		return fetchPageBySqlFile( pageRequest, sqlFile, Collections.emptyMap() );
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public IPage<T> fetchPageBySqlFile( @NonNull final IPageRequest pageRequest,
										@NonNull final File sqlFile,
										@NonNull final Map<String, Object> params )
	{
		ISqlFileSelect select = new SqlFileSelect( dslContext, sqlFile, params );

		Query query = select.getQuery();

		Table<Record> table = DSL.table( '(' + query.getSQL() + ')', query.getBindValues().toArray() );

		int totalElements = dslContext.fetchCount( table.as( "q" ) );

		select.addParameter( "offset", pageRequest.getOffset() );
		select.addParameter( "limit", pageRequest.getPageSize() );

		List<T> elements = select.fetch().map( new BeanRecordMapper<>( resultClass, dslContext.configuration() ) );

		return new Page<>( pageRequest, elements, totalElements );
	}
}
