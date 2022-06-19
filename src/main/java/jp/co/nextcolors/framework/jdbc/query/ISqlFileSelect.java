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

import lombok.NonNull;

/**
 * SQL ファイルを使用した検索です。
 *
 * @author hamana
 */
public interface ISqlFileSelect extends ISqlFileQuery<ISqlFileSelect> {
    /**
     * {@inheritDoc}
     */
    @Override
    ResultQuery<Record> getQuery();

    /**
     * レコードを検索します。
     *
     * @return 検索結果
     */
    default Record fetchOne() {
        return getQuery().fetchOne();
    }

    /**
     * レコードを検索します。<br />
     * 検索結果のレコードは指定したテーブルのレコードにマッピングします。
     *
     * @param <R>   レコードの型を表すクラス
     * @param table テーブル
     * @return 検索結果
     */
    default <R extends Record> R fetchOneInto(@NonNull final Table<R> table) {
        return getQuery().fetchOneInto(table);
    }

    /**
     * レコードを検索します。
     *
     * @return 検索結果
     */
    default Result<Record> fetch() {
        return getQuery().fetch();
    }

    /**
     * レコードを検索します。<br />
     * 検索結果のレコードは指定したテーブルのレコードにマッピングします。
     *
     * @param <R>   レコードの型を表すクラス
     * @param table テーブル
     * @return 検索結果
     */
    default <R extends Record> Result<R> fetchInto(@NonNull final Table<R> table) {
        return getQuery().fetchInto(table);
    }
}
