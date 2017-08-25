package jp.co.nextcolors.framework.jdbc.query;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import com.google.common.collect.Maps;

import jp.sf.amateras.mirage.bean.BeanDescFactory;
import jp.sf.amateras.mirage.parser.Node;
import jp.sf.amateras.mirage.parser.SqlContext;
import jp.sf.amateras.mirage.parser.SqlParser;
import jp.sf.amateras.mirage.parser.SqlParserImpl;
import jp.sf.amateras.mirage.util.MirageUtil;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;

/**
 * SQL ファイルを使用して問い合わせ行うための抽象クラスです。
 *
 * @author hamana
 * @param <S>
 * 			{@link ISqlFileQuery} のサブタイプです。
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
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
	 * SQL ファイルです。
	 *
	 */
	@NonNull
	protected final File sqlFile;

	/**
	 * パラメータです。
	 *
	 */
	protected final Map<String, Object> params = Maps.newHashMap();

	//-------------------------------------------------------------------------
	//    Protected Methods
	//-------------------------------------------------------------------------
	/**
	 * @param dslContext
	 * 			DSL コンテキスト
	 * @param sqlFile
	 * 			SQL ファイル
	 * @param params
	 * 			パラメータ
	 */
	protected SqlFileQuery( @NonNull final DSLContext dslContext,
							@NonNull final File sqlFile,
							@NonNull final Map<String, Object> params )
	{
		this( dslContext, sqlFile );
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
		String sql = IOUtils.toString( sqlFile.toURI(), StandardCharsets.UTF_8 );

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
		Map<String, Object> params = Maps.newHashMap();

		for ( String key : this.params.keySet() ) {
			Object value = this.params.get( key );

			if ( Objects.nonNull( value ) ) {
				if ( Collection.class.isInstance( value ) ) {
					value = Collection.class.cast( value ).toArray();
				}

				if ( value.getClass().isArray() ) {
					value = DSL.list( Arrays.stream( (Object[]) value ).map( DSL::val ).collect( Collectors.toList() ) );
				}
			}

			params.put( key, value );
		}

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
