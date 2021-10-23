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
package jp.co.nextcolors.framework.jdbc.query;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import jp.co.future.uroborosql.UroboroSQL;
import jp.co.future.uroborosql.config.SqlConfig;
import jp.co.future.uroborosql.context.SqlContext;
import jp.co.future.uroborosql.parser.ContextTransformer;
import jp.co.future.uroborosql.parser.SqlParser;
import jp.co.future.uroborosql.parser.SqlParserImpl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;

/**
 * SQL ファイルを使用して問い合わせ行うための抽象クラスです。
 *
 * @author hamana
 * @param <S>
 *         {@link ISqlFileQuery} のサブタイプです。
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
public abstract class SqlFileQuery<S extends ISqlFileQuery<S>> implements ISqlFileQuery<S>
{
	//-------------------------------------------------------------------------
	//    Protected Properties
	//-------------------------------------------------------------------------
	/**
	 * DSL コンテキストです。
	 *
	 */
	@NonNull
	protected final DSLContext dslContext;

	/**
	 * SQL ファイルのパスです。
	 *
	 */
	@NonNull
	protected final Path sqlFilePath;

	/**
	 * パラメータです。
	 *
	 */
	protected final Map<String, Object> params = new HashMap<>();

	//-------------------------------------------------------------------------
	//    Protected Methods
	//-------------------------------------------------------------------------
	/**
	 * @param dslContext
	 *         DSL コンテキスト
	 * @param sqlFilePath
	 *         SQL ファイルのパス
	 * @param params
	 *         パラメータ
	 */
	protected SqlFileQuery( @NonNull final DSLContext dslContext,
							@NonNull final Path sqlFilePath,
							@NonNull final Map<String, Object> params )
	{
		this( dslContext, sqlFilePath );
		this.params.putAll( params );
	}

	/**
	 * SQL にバインドするパラメータを生成します。
	 *
	 * @return SQL にバインドするパラメータ
	 */
	protected Map<String, Object> createBindParameters()
	{
		Map<String, Object> params = new HashMap<>();

		this.params.forEach( ( key, value ) -> {
			if ( Objects.nonNull( value ) ) {
				if ( Collection.class.isInstance( value ) ) {
					value = Collection.class.cast( value ).toArray();
				}

				if ( value.getClass().isArray() ) {
					value = DSL.list( Arrays.stream( (Object[]) value ).map( DSL::val ).collect( Collectors.toList() ) );
				}
			}

			params.put( key, value );
		} );

		return Collections.unmodifiableMap( params );
	}

	/**
	 * SQL のコンテキストを生成します。
	 *
	 * @return SQL のコンテキスト
	 */
	@SneakyThrows(IOException.class)
	protected SqlContext createSqlContext()
	{
		String sql = Files.readString( sqlFilePath );

		SqlConfig sqlConfig = UroboroSQL.builder( dslContext.configuration().connectionProvider().acquire() ).build();

		SqlParser sqlParser = new SqlParserImpl( sql, sqlConfig.getExpressionParser(), sqlConfig.getDialect().isRemoveTerminator(), false );

		Map<String, Object> params = createBindParameters();

		SqlContext sqlContext = sqlConfig.context().paramMap( params );

		ContextTransformer contextTransformer = sqlParser.parse();

		contextTransformer.transform( sqlContext );

		return sqlContext;
	}

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public S addParameter( @NonNull final String name, final Object value )
	{
		params.put( name, value );

		return (S) this;
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public S addParameters( @NonNull final Map<String, Object> params )
	{
		this.params.putAll( params );

		return (S) this;
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public S setParameters( @NonNull final Map<String, Object> params )
	{
		this.params.clear();

		return addParameters( params );
	}
}
