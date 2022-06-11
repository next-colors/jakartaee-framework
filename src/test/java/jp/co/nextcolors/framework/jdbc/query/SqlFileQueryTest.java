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

import static net.andreinc.mockneat.unit.seq.IntSeq.intSeq;
import static net.andreinc.mockneat.unit.text.SQLInserts.sqlInserts;
import static net.andreinc.mockneat.unit.text.Strings.strings;
import static net.andreinc.mockneat.unit.types.Ints.ints;
import static net.andreinc.mockneat.unit.types.Longs.longs;
import static net.andreinc.mockneat.unit.user.Emails.emails;
import static net.andreinc.mockneat.unit.user.Names.names;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIOException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.co.future.uroborosql.UroboroSQL;
import jp.co.future.uroborosql.UroboroSQL.UroboroSQLBuilder;
import jp.co.future.uroborosql.context.SqlContext;
import jp.co.future.uroborosql.dialect.DefaultDialect;
import jp.co.future.uroborosql.expr.ognl.OgnlExpressionParser;

import lombok.SneakyThrows;

/**
 * {@link SqlFileQuery} のテストです。
 *
 * @author hamana
 */
@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(MockitoExtension.class)
class SqlFileQueryTest {
    @InjectMocks
    private FooSqlFileQuery sqlFileQuery;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private DSLContext dslContext;

    @Mock
    private Path sqlFilePath;

    @Mock
    private Map<String, Object> params;

    @Mock
    private Connection connection;

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private DefaultDialect dialect;

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private OgnlExpressionParser expressionParser;

    /**
     * {@link SqlFileQuery#SqlFileQuery(DSLContext, Path, Map)} のテストです。
     */
    @Test
    void testConstructor() {
        assertThatNullPointerException().isThrownBy(() -> new FooSqlFileQuery(null, sqlFilePath, params));
        assertThatNullPointerException().isThrownBy(() -> new FooSqlFileQuery(dslContext, null, params));
        assertThatNullPointerException().isThrownBy(() -> new FooSqlFileQuery(dslContext, sqlFilePath, null));
    }

    /**
     * {@link SqlFileQuery#createBindParameters()} のテストです。
     */
    @Test
    void testCreateBindParameters() {
        try (final MockedStatic<DSL> dsl = mockStatic(DSL.class)) {
            assertThat(sqlFileQuery.createBindParameters()).isEmpty();

            final Map<String, Object> params = Map.of(
                    strings().get(), strings().collection(2).get(),
                    strings().get(), ints().get(),
                    strings().get(), longs().get()
            );

            sqlFileQuery.setParameters(params);
            assertThat(sqlFileQuery.createBindParameters()).hasSameSizeAs(params);
            dsl.verify(() -> DSL.list(anyCollection()));
            dsl.clearInvocations();
        }
    }

    /**
     * {@link SqlFileQuery#createSqlContext()} のテストです。
     */
    @SneakyThrows(SQLException.class)
    @Test
    void testCreateSqlContext() {
        try (final MockedStatic<Files> files = mockStatic(Files.class);
             final MockedStatic<UroboroSQL> uroboroSQL = mockStatic(UroboroSQL.class, Answers.CALLS_REAL_METHODS)) {
            final String sql = sqlInserts()
                    .tableName("foo")
                    .column("id", intSeq())
                    .column("first_name", "/*firstName*/")
                    .column("last_name", "/*lastName*/")
                    .column("email", "/*email*/")
                    .get().toString();

            final Map<String, Object> params = Map.of(
                    "firstName", names().first().get(),
                    "lastName", names().last().get(),
                    "email", emails().get()
            );

            final UroboroSQLBuilder builder = UroboroSQL.builder(connection)
                    .setDialect(dialect)
                    .setExpressionParser(expressionParser);

            files.when(() -> Files.readString(sqlFilePath)).thenReturn(sql);
            uroboroSQL.when(() -> UroboroSQL.builder(any(Connection.class))).thenReturn(builder);
            sqlFileQuery.setParameters(params);
            final SqlContext sqlContext = sqlFileQuery.createSqlContext();
            assertThat(sqlContext.getExecutableSql()).isNotEmpty();
            assertThat(sqlContext.getBindVariables()).isNotEmpty();
            files.reset();
            uroboroSQL.reset();

            files.when(() -> Files.readString(sqlFilePath)).thenThrow(IOException.class);
            assertThatIOException().isThrownBy(() -> sqlFileQuery.createSqlContext());
            files.reset();

            files.when(() -> Files.readString(sqlFilePath)).thenReturn(sql);
            uroboroSQL.when(() -> UroboroSQL.builder(connection)).thenReturn(builder);
            when(dslContext.configuration().connectionProvider().acquire()).thenReturn(connection);
            doThrow(SQLException.class).when(connection).close();
            assertThatExceptionOfType(SQLException.class).isThrownBy(() -> sqlFileQuery.createSqlContext());
            files.reset();
            uroboroSQL.reset();
            reset(dslContext.configuration().connectionProvider().acquire());
            reset(connection);
        }
    }

    /**
     * {@link SqlFileQuery#addParameter(String, Object)} のテストです。
     */
    @Test
    void testAddParameter() {
        final String name = strings().get();
        final Object value = strings().get();

        final int size = sqlFileQuery.params.size();

        sqlFileQuery.addParameter(name, value);
        assertThat(sqlFileQuery.params.size()).isGreaterThanOrEqualTo(size);

        assertThatNullPointerException().isThrownBy(() -> sqlFileQuery.addParameter(null, value));
    }

    /**
     * {@link SqlFileQuery#addParameters(Map)} のテストです。
     */
    @Test
    void testAddParameters() {
        final Map<String, Object> params = Map.of(
                strings().get(), strings().get(),
                strings().get(), ints().get(),
                strings().get(), longs().get()
        );

        final int size = sqlFileQuery.params.size();

        sqlFileQuery.addParameters(params);
        assertThat(sqlFileQuery.params.size()).isGreaterThanOrEqualTo(size);

        assertThatNullPointerException().isThrownBy(() -> sqlFileQuery.addParameters(null));
    }

    /**
     * {@link SqlFileQuery#setParameters(Map)} のテストです。
     */
    @Test
    void testSetParameters() {
        final Map<String, Object> params = Map.of(
                strings().get(), strings().get(),
                strings().get(), ints().get(),
                strings().get(), longs().get()
        );

        sqlFileQuery.setParameters(params);
        assertThat(sqlFileQuery.params).hasSameSizeAs(params);

        assertThatNullPointerException().isThrownBy(() -> sqlFileQuery.setParameters(null));
    }

    private static class FooSqlFileQuery extends SqlFileQuery<FooSqlFileQuery> {
        private FooSqlFileQuery(final DSLContext dslContext,
                                final Path sqlFilePath,
                                final Map<String, Object> params) {
            super(dslContext, sqlFilePath, params);
        }

        @Override
        public Query getQuery() {
            return null;
        }
    }
}
