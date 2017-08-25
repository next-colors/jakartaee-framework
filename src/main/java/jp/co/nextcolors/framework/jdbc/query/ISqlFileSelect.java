package jp.co.nextcolors.framework.jdbc.query;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;

/**
 * SQL ファイルを使用した検索です。
 *
 * @author hamana
 */
public interface ISqlFileSelect extends ISqlFileQuery<ISqlFileSelect>
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 *
	 */
    @Override
	ResultQuery<Record> getQuery();

	/**
	 * レコードを検索します。
	 *
	 * @return 検索結果
	 */
	Record fetchOne();

	/**
	 * レコードを検索します。
	 *
	 * @return 検索結果
	 */
	Result<Record> fetch();
}
