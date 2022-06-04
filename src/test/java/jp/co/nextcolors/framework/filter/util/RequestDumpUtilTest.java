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
package jp.co.nextcolors.framework.filter.util;

import static net.andreinc.mockneat.unit.text.Strings.strings;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;

import java.util.Collections;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * {@link RequestDumpUtil} のテストです。
 *
 * @author hamana
 */
@ExtendWith(MockitoExtension.class)
class RequestDumpUtilTest {
    private static final String INDENT = StringUtils.SPACE.repeat(2);

    private static final String LF = System.lineSeparator();

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private Cookie cookie;

    /**
     * {@link RequestDumpUtil#dumpRequestProperties(StringBuffer, HttpServletRequest, String, String)} のテストです。
     */
    @Test
    void testDumpRequestProperties() {
        final StringBuffer buffer = new StringBuffer();

        doReturn(Collections.emptyEnumeration()).when(request).getLocales();
        RequestDumpUtil.dumpRequestProperties(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isNotEmpty();
        reset(request);

        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestProperties(null, request, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestProperties(buffer, null, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestProperties(buffer, request, null, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestProperties(buffer, request, LF, null));
    }

    /**
     * {@link RequestDumpUtil#dumpSessionProperties(StringBuffer, HttpServletRequest, String, String)} のテストです。
     */
    @Test
    void testDumpSessionProperties() {
        final StringBuffer buffer = new StringBuffer();

        RequestDumpUtil.dumpSessionProperties(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isEmpty();

        doReturn(session).when(request).getSession(false);
        RequestDumpUtil.dumpSessionProperties(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isNotEmpty();
        reset(request);

        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpSessionProperties(null, request, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpSessionProperties(buffer, null, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpSessionProperties(buffer, request, null, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpSessionProperties(buffer, request, LF, null));
    }

    /**
     * {@link RequestDumpUtil#dumpRequestHeaders(StringBuffer, HttpServletRequest, String, String)} のテストです。
     */
    @Test
    void testDumpRequestHeaders() {
        final StringBuffer buffer = new StringBuffer();

        doReturn(Collections.emptyEnumeration()).when(request).getHeaderNames();
        RequestDumpUtil.dumpRequestHeaders(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isEmpty();
        reset(request);

        doReturn(Collections.enumeration(strings().collection(2).get())).when(request).getHeaderNames();
        RequestDumpUtil.dumpRequestHeaders(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isNotEmpty();
        reset(request);

        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestHeaders(null, request, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestHeaders(buffer, null, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestHeaders(buffer, request, null, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestHeaders(buffer, request, LF, null));
    }

    /**
     * {@link RequestDumpUtil#dumpRequestParameters(StringBuffer, HttpServletRequest, String, String)} のテストです。
     */
    @Test
    void testDumpRequestParameters() {
        final StringBuffer buffer = new StringBuffer();

        doReturn(Collections.emptyEnumeration()).when(request).getParameterNames();
        RequestDumpUtil.dumpRequestParameters(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isEmpty();
        reset(request);

        doReturn(Collections.enumeration(strings().collection(2).get())).when(request).getParameterNames();
        doReturn(Arrays.<String>array()).when(request).getParameterValues(anyString());
        RequestDumpUtil.dumpRequestParameters(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isNotEmpty();
        reset(request);

        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestParameters(null, request, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestParameters(buffer, null, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestParameters(buffer, request, null, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestParameters(buffer, request, LF, null));
    }

    /**
     * {@link RequestDumpUtil#dumpCookies(StringBuffer, HttpServletRequest, String, String)} のテストです。
     */
    @Test
    void testDumpCookies() {
        final StringBuffer buffer = new StringBuffer();

        RequestDumpUtil.dumpCookies(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isEmpty();

        doReturn(Arrays.array(cookie)).when(request).getCookies();
        RequestDumpUtil.dumpCookies(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isNotEmpty();
        reset(request);

        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpCookies(null, request, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpCookies(buffer, null, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpCookies(buffer, request, null, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpCookies(buffer, request, LF, null));
    }
}
