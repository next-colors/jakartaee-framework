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

import java.nio.file.Path;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Table;

import jp.co.future.uroborosql.context.SqlContext;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * {@link ISqlFileSelect} の実装クラスです。
 *
 * @author hamana
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SqlFileSelect extends SqlFileQuery<ISqlFileSelect> implements ISqlFileSelect {
    /**
     * @param dslContext  DSL コンテキスト
     * @param sqlFilePath SQL ファイルのパス
     */
    public SqlFileSelect(final DSLContext dslContext, final Path sqlFilePath) {
        super(dslContext, sqlFilePath);
    }

    /**
     * @param dslContext  DSL コンテキスト
     * @param sqlFilePath SQL ファイルのパス
     * @param params      パラメータ
     */
    public SqlFileSelect(final DSLContext dslContext,
                         final Path sqlFilePath,
                         final Map<String, Object> params) {
        super(dslContext, sqlFilePath, params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultQuery<Record> getQuery() {
        SqlContext sqlContext = createSqlContext();

        return dslContext.resultQuery(sqlContext.getExecutableSql(), sqlContext.getBindVariables());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Record fetchOne() {
        return getQuery().fetchOne();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R extends Record> R fetchOneInto(@NonNull final Table<R> table) {
        return getQuery().fetchOneInto(table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result<Record> fetch() {
        return getQuery().fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R extends Record> Result<R> fetchInto(@NonNull final Table<R> table) {
        return getQuery().fetchInto(table);
    }
}
