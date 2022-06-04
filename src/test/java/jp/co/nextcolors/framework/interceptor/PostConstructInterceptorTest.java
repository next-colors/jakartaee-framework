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
package jp.co.nextcolors.framework.interceptor;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;

import javax.interceptor.InvocationContext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.SneakyThrows;

/**
 * {@link PostConstructInterceptor} のテストです。
 *
 * @author hamana
 */
@ExtendWith(MockitoExtension.class)
class PostConstructInterceptorTest {
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private PostConstructInterceptor interceptor;

    @Mock
    private InvocationContext context;

    /**
     * {@link PostConstructInterceptor#invoke(InvocationContext)} のテストです。
     */
    @SneakyThrows(Exception.class)
    @Test
    void testInvoke() {
        interceptor.invoke(context);
        verify(interceptor).invokeInternal(context);
        clearInvocations(interceptor);

        assertThatNullPointerException().isThrownBy(() -> interceptor.invoke(null));
    }
}
