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
import java.util.Collection;
import java.util.Map;

import org.jooq.Condition;
import org.jooq.OrderField;
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
													Condition condition, OrderField<?>... order );

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
													Condition condition, Collection<? extends OrderField<?>> order );

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
