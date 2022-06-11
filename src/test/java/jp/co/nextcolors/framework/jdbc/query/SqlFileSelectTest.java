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

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.nio.file.Path;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.jooq.Table;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.co.future.uroborosql.context.SqlContext;

/**
 * {@link SqlFileSelect} のテストです。
 *
 * @author hamana
 */
@ExtendWith(MockitoExtension.class)
class SqlFileSelectTest {
    @Spy
    @InjectMocks
    private SqlFileSelect sqlFileSelect;

    @Mock
    private DSLContext dslContext;

    @Mock
    private Path sqlFilePath;

    @Mock
    private Map<String, Object> params;

    @Mock(answer = Answers.RETURNS_MOCKS)
    private SqlContext sqlContext;

    @Mock
    private ResultQuery<Record> query;

    @Mock
    private Table<? extends Record> table;

    /**
     * {@link SqlFileSelect#SqlFileSelect(DSLContext, Path)} のテストです。
     */
    @Test
    void testConstructor() {
        assertThatNoException().isThrownBy(() -> new SqlFileSelect(dslContext, sqlFilePath));
    }

    /**
     * {@link SqlFileSelect#getQuery()} のテストです。
     */
    @Test
    void testGetQuery() {
        doReturn(sqlContext).when(sqlFileSelect).createSqlContext();
        sqlFileSelect.getQuery();
        verify(dslContext).resultQuery(anyString(), any(Object[].class));
        reset(sqlFileSelect);
    }

    /**
     * {@link SqlFileSelect#fetchOne()} のテストです。
     */
    @Test
    void testFetchOne() {
        doReturn(query).when(sqlFileSelect).getQuery();
        sqlFileSelect.fetchOne();
        verify(query).fetchOne();
        reset(sqlFileSelect);
    }

    /**
     * {@link SqlFileSelect#fetchOneInto(Table)} のテストです。
     */
    @Test
    void testFetchOneInto() {
        doReturn(query).when(sqlFileSelect).getQuery();
        sqlFileSelect.fetchOneInto(table);
        verify(query).fetchOneInto(table);
        reset(sqlFileSelect);

        assertThatNullPointerException().isThrownBy(() -> sqlFileSelect.fetchOneInto(null));
    }

    /**
     * {@link SqlFileSelect#fetch()} のテストです。
     */
    @Test
    void testFetch() {
        doReturn(query).when(sqlFileSelect).getQuery();
        sqlFileSelect.fetch();
        verify(query).fetch();
        reset(sqlFileSelect);
    }

    /**
     * {@link SqlFileSelect#fetchInto(Table)} のテストです。
     */
    @Test
    void testFetchInto() {
        doReturn(query).when(sqlFileSelect).getQuery();
        sqlFileSelect.fetchInto(table);
        verify(query).fetchInto(table);
        reset(sqlFileSelect);

        assertThatNullPointerException().isThrownBy(() -> sqlFileSelect.fetchInto(null));
    }
}
