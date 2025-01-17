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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import jp.co.future.uroborosql.UroboroSQL;
import jp.co.future.uroborosql.config.SqlConfig;
import jp.co.future.uroborosql.context.ExecutionContext;
import jp.co.future.uroborosql.dialect.Dialect;
import jp.co.future.uroborosql.parser.ContextTransformer;
import jp.co.future.uroborosql.parser.SqlParser;
import jp.co.future.uroborosql.parser.SqlParserImpl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;

/**
 * SQL ファイルを使用して問い合わせ行うための抽象クラスです。
 *
 * @param <S> {@link ISqlFileQuery} のサブタイプです。
 * @author hamana
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
public abstract class SqlFileQuery<S extends ISqlFileQuery<S>> implements ISqlFileQuery<S> {
    /**
     * DSL コンテキストです。
     */
    @NonNull
    protected final DSLContext dslContext;

    /**
     * SQL ファイルのパスです。
     */
    @NonNull
    protected final Path sqlFilePath;

    /**
     * パラメータです。
     */
    protected final Map<String, Object> params = new HashMap<>();

    /**
     * @param dslContext  DSL コンテキスト
     * @param sqlFilePath SQL ファイルのパス
     * @param params      パラメータ
     */
    protected SqlFileQuery(final DSLContext dslContext,
                           final Path sqlFilePath,
                           @NonNull final Map<String, Object> params) {
        this(dslContext, sqlFilePath);
        this.params.putAll(params);
    }

    /**
     * SQL にバインドするパラメータを生成します。
     *
     * @return SQL にバインドするパラメータ
     */
    protected Map<String, Object> createBindParameters() {
        final Map<String, Object> params = new HashMap<>();

        this.params.forEach((key, value) -> {
            if (value instanceof Collection<?> collection) {
                value = collection.toArray();
            }

            if (value instanceof Object[] array) {
                value = DSL.list(Stream.of(array).map(DSL::val).toList());
            }

            params.put(key, value);
        });

        return Collections.unmodifiableMap(params);
    }

    /**
     * SQL の実行コンテキストを生成します。
     *
     * @return SQL の実行コンテキスト
     */
    @SneakyThrows({IOException.class, SQLException.class})
    protected ExecutionContext createExecutionContext() {
        final String sql = Files.readString(sqlFilePath);
        final Map<String, Object> params = createBindParameters();

        try (final Connection connection = dslContext.configuration().connectionProvider().acquire()) {
            final SqlConfig sqlConfig = UroboroSQL.builder(connection).build();

            final Dialect dialect = sqlConfig.getDialect();

            final SqlParser sqlParser = new SqlParserImpl(sql, sqlConfig.getExpressionParser(), dialect.isRemoveTerminator(), false);

            final ExecutionContext executionContext = sqlConfig.context()
                    .paramMap(params)
                    .param(Dialect.PARAM_KEY_ESCAPE_CHAR, dialect.getEscapeChar());

            final ContextTransformer contextTransformer = sqlParser.parse();
            contextTransformer.transform(executionContext);

            return executionContext;
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public S addParameter(@NonNull final String name, final Object value) {
        params.put(name, value);

        return (S) this;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public S addParameters(@NonNull final Map<String, Object> params) {
        this.params.putAll(params);

        return (S) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public S setParameters(@NonNull final Map<String, Object> params) {
        this.params.clear();

        return addParameters(params);
    }
}
