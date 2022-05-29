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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

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

    /**
     * {@link RequestDumpUtil#dumpRequestProperties(StringBuffer, HttpServletRequest, String, String)} のテストです。
     */
    @Test
    void testDumpRequestProperties() {
        final StringBuffer buffer = new StringBuffer();

        when(request.getLocales()).thenReturn(Collections.emptyEnumeration());
        RequestDumpUtil.dumpRequestProperties(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isNotEmpty();
        reset(request);

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpRequestProperties(null, request, LF, INDENT));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpRequestProperties(buffer, null, LF, INDENT));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpRequestProperties(buffer, request, null, INDENT));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpRequestProperties(buffer, request, LF, null));
    }

    /**
     * {@link RequestDumpUtil#dumpSessionProperties(StringBuffer, HttpServletRequest, String, String)} のテストです。
     */
    @Test
    void testDumpSessionProperties() {
        final StringBuffer buffer = new StringBuffer();

        RequestDumpUtil.dumpSessionProperties(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isEmpty();

        when(request.getSession(false)).thenReturn(mock(HttpSession.class));
        RequestDumpUtil.dumpSessionProperties(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isNotEmpty();
        reset(request);

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpSessionProperties(null, request, LF, INDENT));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpSessionProperties(buffer, null, LF, INDENT));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpSessionProperties(buffer, request, null, INDENT));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpSessionProperties(buffer, request, LF, null));
    }

    /**
     * {@link RequestDumpUtil#dumpRequestHeaders(StringBuffer, HttpServletRequest, String, String)} のテストです。
     */
    @Test
    void testDumpRequestHeaders() {
        final StringBuffer buffer = new StringBuffer();

        when(request.getHeaderNames()).thenReturn(Collections.emptyEnumeration());
        RequestDumpUtil.dumpRequestHeaders(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isEmpty();
        reset(request);

        when(request.getHeaderNames()).thenReturn(Collections.enumeration(strings().collection(2).get()));
        RequestDumpUtil.dumpRequestHeaders(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isNotEmpty();
        reset(request);

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpRequestHeaders(null, request, LF, INDENT));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpRequestHeaders(buffer, null, LF, INDENT));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpRequestHeaders(buffer, request, null, INDENT));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpRequestHeaders(buffer, request, LF, null));
    }

    /**
     * {@link RequestDumpUtil#dumpRequestParameters(StringBuffer, HttpServletRequest, String, String)} のテストです。
     */
    @Test
    void testDumpRequestParameters() {
        final StringBuffer buffer = new StringBuffer();

        when(request.getParameterNames()).thenReturn(Collections.emptyEnumeration());
        RequestDumpUtil.dumpRequestParameters(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isEmpty();
        reset(request);

        when(request.getParameterNames()).thenReturn(Collections.enumeration(strings().collection(2).get()));
        when(request.getParameterValues(anyString())).thenReturn(Collections.emptyList().toArray(String[]::new));
        RequestDumpUtil.dumpRequestParameters(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isNotEmpty();
        reset(request);

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpRequestParameters(null, request, LF, INDENT));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpRequestParameters(buffer, null, LF, INDENT));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpRequestParameters(buffer, request, null, INDENT));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpRequestParameters(buffer, request, LF, null));
    }

    /**
     * {@link RequestDumpUtil#dumpCookies(StringBuffer, HttpServletRequest, String, String)} のテストです。
     */
    @Test
    void testDumpCookies() {
        final StringBuffer buffer = new StringBuffer();

        RequestDumpUtil.dumpCookies(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isEmpty();

        when(request.getCookies()).thenReturn(Arrays.array(mock(Cookie.class)));
        RequestDumpUtil.dumpCookies(buffer, request, LF, INDENT);
        assertThat(buffer.toString()).isNotEmpty();
        reset(request);

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpCookies(null, request, LF, INDENT));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpCookies(buffer, null, LF, INDENT));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpCookies(buffer, request, null, INDENT));
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> RequestDumpUtil.dumpCookies(buffer, request, LF, null));
    }
}
