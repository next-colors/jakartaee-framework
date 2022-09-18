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

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import jp.co.nextcolors.framework.filter.util.RequestDumpUtil;

/**
 * リクエスト（{@link HttpServletRequest}）の内容をダンプするフィルタです。
 *
 * @author hamana
 */
@Slf4j
@ToString
@EqualsAndHashCode
public class RequestDumpFilter implements Filter {
    /**
     * リクエスト（{@link HttpServletRequest}）の内容をダンプする際のインデントです。
     */
    private static final String INDENT = StringUtils.SPACE.repeat(2);

    /**
     * リクエスト（{@link HttpServletRequest}）の内容をダンプする際の改行文字です。
     */
    private static final String LF = System.lineSeparator();

    /**
     * リクエスト（{@link HttpServletRequest}）の内容をダンプします。
     *
     * @param request リクエスト
     */
    private void dump(final HttpServletRequest request) {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(LF);
        buffer.append(LF);
        buffer.append("** Request Dump ***************************************************************");
        buffer.append(LF);

        // リクエストのプロパティを文字列バッファにダンプ
        RequestDumpUtil.dumpRequestProperties(buffer, request, LF, INDENT);
        // セッションのプロパティを文字列バッファにダンプ
        RequestDumpUtil.dumpSessionProperties(buffer, request, LF, INDENT);
        // リクエストヘッダの内容を文字列バッファにダンプ
        RequestDumpUtil.dumpRequestHeaders(buffer, request, LF, INDENT);
        // リクエストパラメータの内容を文字列バッファにダンプ
        RequestDumpUtil.dumpRequestParameters(buffer, request, LF, INDENT);
        // クッキーの内容を文字列バッファにダンプ
        RequestDumpUtil.dumpCookies(buffer, request, LF, INDENT);

        buffer.append("*******************************************************************************");
        buffer.append(LF);

        log.atDebug().log(buffer.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        // Do nothing.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(@NonNull final ServletRequest request, @NonNull final ServletResponse response, @NonNull final FilterChain chain)
            throws IOException, ServletException {
        if (log.isDebugEnabled() && request instanceof HttpServletRequest httpServletRequest) {
            dump(httpServletRequest);
        }

        chain.doFilter(request, response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        // Do nothing.
    }
}
