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
import org.jooq.Query;

import jp.co.future.uroborosql.context.ExecutionContext;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * {@link ISqlFileWrite} の実装クラスです。
 *
 * @author hamana
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SqlFileWrite extends SqlFileQuery<ISqlFileWrite> implements ISqlFileWrite {
    /**
     * @param dslContext  DSL コンテキスト
     * @param sqlFilePath SQL ファイルのパス
     */
    public SqlFileWrite(final DSLContext dslContext, final Path sqlFilePath) {
        super(dslContext, sqlFilePath);
    }

    /**
     * @param dslContext  DSL コンテキスト
     * @param sqlFilePath SQL ファイルのパス
     * @param params      パラメータ
     */
    public SqlFileWrite(final DSLContext dslContext,
                        final Path sqlFilePath,
                        final Map<String, Object> params) {
        super(dslContext, sqlFilePath, params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Query getQuery() {
        final ExecutionContext executionContext = createExecutionContext();

        return dslContext.query(executionContext.getExecutableSql(), executionContext.getBindVariables());
    }
}
