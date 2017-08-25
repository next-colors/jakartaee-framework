package jp.co.nextcolors.framework.jdbc.query;

import java.io.File;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Query;

import jp.sf.amateras.mirage.parser.SqlContext;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * {@link ISqlFileWrite} の実装クラスです。
 *
 * @author hamana
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SqlFileWrite extends SqlFileQuery<ISqlFileWrite> implements ISqlFileWrite
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * @param dslContext
	 * 			DSL コンテキスト
	 * @param sqlFile
	 * 			SQL ファイル
	 */
	public SqlFileWrite( @NonNull final DSLContext dslContext, @NonNull final File sqlFile )
	{
		super( dslContext, sqlFile );
	}

	/**
	 * @param dslContext
	 * 			DSL コンテキスト
	 * @param sqlFile
	 * 			SQL ファイル
	 * @param params
	 * 			パラメータ
	 */
	public SqlFileWrite( @NonNull final DSLContext dslContext,
							@NonNull final File sqlFile,
							@NonNull final Map<String, Object> params )
	{
		super( dslContext, sqlFile, params );
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public Query getQuery()
	{
		SqlContext sqlContext = createSqlContext();

		return dslContext.query( sqlContext.getSql(), sqlContext.getBindVariables() );
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public int execute()
	{
		return getQuery().execute();
	}
}
