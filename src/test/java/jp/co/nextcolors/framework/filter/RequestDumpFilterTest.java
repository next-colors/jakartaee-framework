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
package jp.co.nextcolors.framework.filter;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggingEventBuilder;

import lombok.SneakyThrows;

import jp.co.nextcolors.framework.filter.util.RequestDumpUtil;

/**
 * {@link RequestDumpFilter} のテストです。
 *
 * @author hamana
 */
@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(MockitoExtension.class)
class RequestDumpFilterTest {
    private static final MockedStatic<LoggerFactory> LOGGER_FACTORY = mockStatic(LoggerFactory.class);

    private static final Logger LOGGER = mock(Logger.class);

    private final RequestDumpFilter filter = new RequestDumpFilter();

    @BeforeAll
    static void setup() {
        LOGGER_FACTORY.when(() -> LoggerFactory.getLogger(RequestDumpFilter.class)).thenReturn(LOGGER);
    }

    @AfterAll
    static void tearDown() {
        LOGGER_FACTORY.close();
    }

    /**
     * {@link RequestDumpFilter#init(FilterConfig)} のテストです。
     */
    @Test
    void testInit() {
        assertThatNoException().isThrownBy(() -> filter.init(mock(FilterConfig.class)));
    }

    /**
     * {@link RequestDumpFilter#doFilter(ServletRequest, ServletResponse, FilterChain)} のテストです。
     */
    @SneakyThrows({IOException.class, ServletException.class})
    @Test
    void testDoFilter() {
        try (final MockedStatic<RequestDumpUtil> requestDumpUtil = mockStatic(RequestDumpUtil.class)) {
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final ServletResponse response = mock(ServletResponse.class);
            final FilterChain chain = mock(FilterChain.class);

            filter.doFilter(request, response, chain);
            requestDumpUtil.verify(() -> RequestDumpUtil.dumpRequestProperties(any(), any(), any(), any()), never());
            requestDumpUtil.verify(() -> RequestDumpUtil.dumpSessionProperties(any(), any(), any(), any()), never());
            requestDumpUtil.verify(() -> RequestDumpUtil.dumpRequestHeaders(any(), any(), any(), any()), never());
            requestDumpUtil.verify(() -> RequestDumpUtil.dumpRequestParameters(any(), any(), any(), any()), never());
            requestDumpUtil.verify(() -> RequestDumpUtil.dumpCookies(any(), any(), any(), any()), never());

            when(LOGGER.isDebugEnabled()).thenReturn(true);
            filter.doFilter(mock(ServletRequest.class), response, chain);
            requestDumpUtil.verify(() -> RequestDumpUtil.dumpRequestProperties(any(), any(), any(), any()), never());
            requestDumpUtil.verify(() -> RequestDumpUtil.dumpSessionProperties(any(), any(), any(), any()), never());
            requestDumpUtil.verify(() -> RequestDumpUtil.dumpRequestHeaders(any(), any(), any(), any()), never());
            requestDumpUtil.verify(() -> RequestDumpUtil.dumpRequestParameters(any(), any(), any(), any()), never());
            requestDumpUtil.verify(() -> RequestDumpUtil.dumpCookies(any(), any(), any(), any()), never());
            reset(LOGGER);

            when(LOGGER.isDebugEnabled()).thenReturn(true);
            when(LOGGER.atDebug()).thenReturn(mock(LoggingEventBuilder.class));
            filter.doFilter(request, response, chain);
            requestDumpUtil.verify(() -> RequestDumpUtil.dumpRequestProperties(any(), same(request), anyString(), anyString()));
            requestDumpUtil.verify(() -> RequestDumpUtil.dumpSessionProperties(any(), same(request), anyString(), anyString()));
            requestDumpUtil.verify(() -> RequestDumpUtil.dumpRequestHeaders(any(), same(request), anyString(), anyString()));
            requestDumpUtil.verify(() -> RequestDumpUtil.dumpRequestParameters(any(), same(request), anyString(), anyString()));
            requestDumpUtil.verify(() -> RequestDumpUtil.dumpCookies(any(), same(request), anyString(), anyString()));
            reset(LOGGER);

            assertThatNullPointerException().isThrownBy(() -> filter.doFilter(null, response, chain));
            assertThatNullPointerException().isThrownBy(() -> filter.doFilter(request, null, chain));
            assertThatNullPointerException().isThrownBy(() -> filter.doFilter(request, response, null));
        }
    }

    /**
     * {@link RequestDumpFilter#destroy()} のテストです。
     */
    @Test
    void testDestroy() {
        assertThatNoException().isThrownBy(() -> filter.destroy());
    }
}
