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

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Table;

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
	 * レコードを検索します。<br />
	 * 検索結果のレコードは指定したテーブルのレコードにマッピングします。
	 *
	 * @param <R>
	 *         レコードの型を表すクラス
	 * @param table
	 *         テーブル
	 * @return 検索結果
	 */
	<R extends Record> R fetchOneInto( Table<R> table );

	/**
	 * レコードを検索します。
	 *
	 * @return 検索結果
	 */
	Result<Record> fetch();

	/**
	 * レコードを検索します。<br />
	 * 検索結果のレコードは指定したテーブルのレコードにマッピングします。
	 *
	 * @param <R>
	 *         レコードの型を表すクラス
	 * @param table
	 *         テーブル
	 * @return 検索結果
	 */
	<R extends Record> Result<R> fetchInto( Table<R> table );
}
