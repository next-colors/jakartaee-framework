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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;

import java.util.Collections;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
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

    @Mock(answer = Answers.RETURNS_MOCKS)
    private HttpServletRequest request;

    @Mock
    private Cookie cookie;

    /**
     * {@link RequestDumpUtil#dumpRequestProperties(StringBuilder, HttpServletRequest, String, String)} のテストです。
     */
    @Test
    void testDumpRequestProperties() {
        final StringBuilder builder = new StringBuilder();

        RequestDumpUtil.dumpRequestProperties(builder, request, LF, INDENT);
        assertThat(builder.toString()).isNotEmpty();

        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestProperties(null, request, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestProperties(builder, null, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestProperties(builder, request, null, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestProperties(builder, request, LF, null));
    }

    /**
     * {@link RequestDumpUtil#dumpSessionProperties(StringBuilder, HttpServletRequest, String, String)} のテストです。
     */
    @Test
    void testDumpSessionProperties() {
        final StringBuilder builder = new StringBuilder();

        doReturn(null).when(request).getSession(false);
        RequestDumpUtil.dumpSessionProperties(builder, request, LF, INDENT);
        assertThat(builder.toString()).isEmpty();
        reset(request);

        RequestDumpUtil.dumpSessionProperties(builder, request, LF, INDENT);
        assertThat(builder.toString()).isNotEmpty();

        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpSessionProperties(null, request, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpSessionProperties(builder, null, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpSessionProperties(builder, request, null, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpSessionProperties(builder, request, LF, null));
    }

    /**
     * {@link RequestDumpUtil#dumpRequestHeaders(StringBuilder, HttpServletRequest, String, String)} のテストです。
     */
    @Test
    void testDumpRequestHeaders() {
        final StringBuilder builder = new StringBuilder();

        RequestDumpUtil.dumpRequestHeaders(builder, request, LF, INDENT);
        assertThat(builder.toString()).isEmpty();

        doReturn(Collections.enumeration(strings().collection(2).get())).when(request).getHeaderNames();
        RequestDumpUtil.dumpRequestHeaders(builder, request, LF, INDENT);
        assertThat(builder.toString()).isNotEmpty();
        reset(request);

        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestHeaders(null, request, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestHeaders(builder, null, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestHeaders(builder, request, null, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestHeaders(builder, request, LF, null));
    }

    /**
     * {@link RequestDumpUtil#dumpRequestParameters(StringBuilder, HttpServletRequest, String, String)} のテストです。
     */
    @Test
    void testDumpRequestParameters() {
        final StringBuilder builder = new StringBuilder();

        RequestDumpUtil.dumpRequestParameters(builder, request, LF, INDENT);
        assertThat(builder.toString()).isEmpty();

        doReturn(Collections.enumeration(strings().collection(2).get())).when(request).getParameterNames();
        RequestDumpUtil.dumpRequestParameters(builder, request, LF, INDENT);
        assertThat(builder.toString()).isNotEmpty();
        reset(request);

        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestParameters(null, request, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestParameters(builder, null, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestParameters(builder, request, null, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpRequestParameters(builder, request, LF, null));
    }

    /**
     * {@link RequestDumpUtil#dumpCookies(StringBuilder, HttpServletRequest, String, String)} のテストです。
     */
    @Test
    void testDumpCookies() {
        final StringBuilder builder = new StringBuilder();

        RequestDumpUtil.dumpCookies(builder, request, LF, INDENT);
        assertThat(builder.toString()).isEmpty();

        doReturn(Arrays.array(cookie)).when(request).getCookies();
        RequestDumpUtil.dumpCookies(builder, request, LF, INDENT);
        assertThat(builder.toString()).isNotEmpty();
        reset(request);

        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpCookies(null, request, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpCookies(builder, null, LF, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpCookies(builder, request, null, INDENT));
        assertThatNullPointerException().isThrownBy(() -> RequestDumpUtil.dumpCookies(builder, request, LF, null));
    }
}
