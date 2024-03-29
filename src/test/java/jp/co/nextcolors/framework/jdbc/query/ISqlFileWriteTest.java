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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import org.jooq.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * {@link ISqlFileWrite} のテストです。
 *
 * @author hamana
 */
@ExtendWith(MockitoExtension.class)
class ISqlFileWriteTest {
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private ISqlFileWrite sqlFileWrite;

    @Mock
    private Query query;

    /**
     * {@link ISqlFileWrite#execute()} のテストです。
     */
    @Test
    void testExecute() {
        doReturn(query).when(sqlFileWrite).getQuery();
        sqlFileWrite.execute();
        verify(query).execute();
        reset(sqlFileWrite);
    }
}
