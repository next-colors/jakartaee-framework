package jp.co.nextcolors.framework.jdbc.pagination;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import org.jooq.Condition;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableRecord;

import jp.co.nextcolors.framework.data.pagination.IPage;
import jp.co.nextcolors.framework.data.pagination.IPageRequest;

/**
 * レコードをページング検索するためのページャです。
 *
 * @author hamana
 * @param <T>
 * 			ページに含まれるレコードの型です。
 */
public interface IPager<T>
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * レコードをページング検索します。
	 *
	 * @param <R>
	 * 			テーブルのレコードの型
	 * @param pageRequest
	 * 			ページ付けの情報
	 * @param table
	 * 			検索するテーブル
	 * @param condition
	 * 			検索条件
	 * @param order
	 * 			ソート順
	 * @return 検索結果
	 */
	<R extends TableRecord<R>> IPage<T> fetchPage( IPageRequest pageRequest, Table<R> table,
													Condition condition, SortField<?>... order );

	/**
	 * レコードをページング検索します。
	 *
	 * @param <R>
	 * 			テーブルのレコードの型
	 * @param pageRequest
	 * 			ページ付けの情報
	 * @param table
	 * 			検索するテーブル
	 * @param condition
	 * 			検索条件
	 * @param order
	 * 			ソート順
	 * @return 検索結果
	 */
	<R extends TableRecord<R>> IPage<T> fetchPage( IPageRequest pageRequest, Table<R> table,
													Condition condition, Collection<SortField<?>> order );

	/**
	 * SQL ファイルを使用してレコードをページング検索します。
	 *
	 * @param pageRequest
	 * 			ページ付けの情報
	 * @param sqlFile
	 * 			SQL ファイル
	 * @return 検索結果
	 */
	IPage<T> fetchPageBySqlFile( IPageRequest pageRequest, File sqlFile );

	/**
	 * SQL ファイルを使用してレコードをページング検索します。
	 *
	 * @param pageRequest
	 * 			ページ付けの情報
	 * @param sqlFile
	 * 			SQL ファイル
	 * @param params
	 * 			パラメータ
	 * @return 検索結果
	 */
	IPage<T> fetchPageBySqlFile( IPageRequest pageRequest, File sqlFile, Map<String, Object> params );
}
