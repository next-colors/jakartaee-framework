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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.nio.file.Path;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.co.future.uroborosql.context.SqlContext;

/**
 * {@link SqlFileWrite} のテストです。
 *
 * @author hamana
 */
@ExtendWith(MockitoExtension.class)
class SqlFileWriteTest {
    @Spy
    @InjectMocks
    private SqlFileWrite sqlFileWrite;

    @Mock
    private DSLContext dslContext;

    @Mock
    private Path sqlFilePath;

    @Mock
    private Map<String, Object> params;

    @Mock(answer = Answers.RETURNS_MOCKS)
    private SqlContext sqlContext;

    @Mock
    private Query query;

    /**
     * {@link SqlFileWrite#SqlFileWrite(DSLContext, Path)} のテストです。
     */
    @Test
    void testConstructor() {
        assertThatNoException().isThrownBy(() -> new SqlFileWrite(dslContext, sqlFilePath));
    }

    /**
     * {@link SqlFileWrite#getQuery()} のテストです。
     */
    @Test
    void testGetQuery() {
        doReturn(sqlContext).when(sqlFileWrite).createSqlContext();
        sqlFileWrite.getQuery();
        verify(dslContext).query(anyString(), any(Object[].class));
        reset(sqlFileWrite);
    }

    /**
     * {@link SqlFileWrite#execute()} のテストです。
     */
    @Test
    void testExecute() {
        doReturn(query).when(sqlFileWrite).getQuery();
        sqlFileWrite.execute();
        verify(query).execute();
        reset(sqlFileWrite);
    }
}
