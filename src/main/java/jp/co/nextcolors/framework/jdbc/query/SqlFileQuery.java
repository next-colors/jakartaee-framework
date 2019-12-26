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

import com.miragesql.miragesql.bean.BeanDescFactory;
import com.miragesql.miragesql.parser.Node;
import com.miragesql.miragesql.parser.SqlContext;
import com.miragesql.miragesql.parser.SqlParser;
import com.miragesql.miragesql.parser.SqlParserImpl;
import com.miragesql.miragesql.util.MirageUtil;

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
	 * SQL のパーサを生成します。
	 *
	 * @return SQL のパーサ
	 */
	@SneakyThrows(IOException.class)
	protected SqlParser createSqlParser()
	{
		String sql = Files.readString( sqlFilePath );

		return new SqlParserImpl( sql, new BeanDescFactory() );
	}

	/**
	 * SQL の構成要素を生成します。
	 *
	 * @return SQL の構成要素
	 */
	protected Node createSqlNode()
	{
		SqlParser sqlParser = createSqlParser();

		return sqlParser.parse();
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
	protected SqlContext createSqlContext()
	{
		Node sqlNode = createSqlNode();

		Map<String, Object> params = createBindParameters();

		SqlContext sqlContext = MirageUtil.getSqlContext( new BeanDescFactory(), params );

		sqlNode.accept( sqlContext );

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
