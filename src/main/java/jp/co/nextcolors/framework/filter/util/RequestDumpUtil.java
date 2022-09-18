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

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * リクエスト（{@link HttpServletRequest}）の内容をダンプするユーティリティです。
 *
 * @author hamana
 */
@UtilityClass
public class RequestDumpUtil {
    /**
     * リクエストのプロパティを文字列ビルダーにダンプします。
     *
     * @param builder 文字列ビルダー
     * @param request リクエスト
     * @param lf      改行文字
     * @param indent  インデント
     */
    public static void dumpRequestProperties(@NonNull final StringBuilder builder, @NonNull final HttpServletRequest request,
                                             @NonNull final String lf, @NonNull final String indent) {
        builder.append(indent);
        builder.append("Request Class = ").append(request.getClass().getName());
        builder.append(", Instance = ").append(request);
        builder.append(lf);

        builder.append(indent);
        builder.append("RequestedSessionId = ").append(request.getRequestedSessionId());
        builder.append(lf);

        builder.append(indent);
        builder.append("REQUEST_URI = ").append(request.getRequestURI());
        builder.append(", SERVLET_PATH = ").append(request.getServletPath());
        builder.append(lf);

        builder.append(indent);
        builder.append("CharacterEncoding = ").append(request.getCharacterEncoding());
        builder.append(", ContentLength = ").append(request.getContentLength());
        builder.append(", ContentType = ").append(request.getContentType());
        builder.append(", Locale = ").append(request.getLocale());
        builder.append(", Locales = ").append(Collections.list(request.getLocales()));
        builder.append(", Scheme = ").append(request.getScheme());
        builder.append(", isSecure = ").append(request.isSecure());
        builder.append(lf);

        builder.append(indent);
        builder.append("SERVER_PROTOCOL = ").append(request.getProtocol());
        builder.append(", REMOTE_ADDR = ").append(request.getRemoteAddr());
        builder.append(", REMOTE_HOST = ").append(request.getRemoteHost());
        builder.append(", SERVER_NAME = ").append(request.getServerName());
        builder.append(", SERVER_PORT = ").append(request.getServerPort());
        builder.append(lf);

        builder.append(indent);
        builder.append("ContextPath = ").append(request.getContextPath());
        builder.append(", REQUEST_METHOD = ").append(request.getMethod());
        builder.append(", QUERY_STRING = ").append(Arrays.toString(StringUtils.split(request.getQueryString(), '&')));
        builder.append(", PathInfo = ").append(request.getPathInfo());
        builder.append(", RemoteUser = ").append(request.getRemoteUser());
        builder.append(lf);
    }

    /**
     * セッションのプロパティを文字列ビルダーにダンプします。
     *
     * @param builder 文字列ビルダー
     * @param request リクエスト
     * @param lf      改行文字
     * @param indent  インデント
     */
    public static void dumpSessionProperties(@NonNull final StringBuilder builder, @NonNull final HttpServletRequest request,
                                             @NonNull final String lf, @NonNull final String indent) {
        final HttpSession session = request.getSession(false);

        if (Objects.isNull(session)) {
            return;
        }

        builder.append(indent);
        builder.append("Session :: SessionId = ").append(session.getId());
        builder.append(lf);

        builder.append(indent);
        builder.append("Session :: CreationTime = ").append(ZonedDateTime.ofInstant(Instant.ofEpochMilli(session.getCreationTime()), ZoneId.systemDefault()));
        builder.append(", LastAccessedTime = ").append(ZonedDateTime.ofInstant(Instant.ofEpochMilli(session.getLastAccessedTime()), ZoneId.systemDefault()));
        builder.append(", MaxInactiveInterval = ").append(session.getMaxInactiveInterval());
        builder.append(lf);
    }

    /**
     * リクエストヘッダの内容を文字列ビルダーにダンプします。
     *
     * @param builder 文字列ビルダー
     * @param request リクエスト
     * @param lf      改行文字
     * @param indent  インデント
     */
    public static void dumpRequestHeaders(@NonNull final StringBuilder builder, @NonNull final HttpServletRequest request,
                                          @NonNull final String lf, @NonNull final String indent) {
        Collections.list(request.getHeaderNames()).stream().sorted().forEach(headerName -> {
            builder.append(indent);
            builder.append("[Header] ").append(headerName).append(" = ").append(request.getHeader(headerName));
            builder.append(lf);
        });
    }

    /**
     * リクエストパラメータの内容を文字列ビルダーにダンプします。
     *
     * @param builder 文字列ビルダー
     * @param request リクエスト
     * @param lf      改行文字
     * @param indent  インデント
     */
    public static void dumpRequestParameters(@NonNull final StringBuilder builder, @NonNull final HttpServletRequest request,
                                             @NonNull final String lf, @NonNull final String indent) {
        Collections.list(request.getParameterNames()).stream().sorted().forEach(paramName -> {
            builder.append(indent);
            builder.append("[Parameter] ").append(paramName).append(" = ").append(String.join(", ", request.getParameterValues(paramName)));
            builder.append(lf);
        });
    }

    /**
     * クッキーの内容を文字列ビルダーにダンプします。
     *
     * @param builder 文字列ビルダー
     * @param request リクエスト
     * @param lf      改行文字
     * @param indent  インデント
     */
    public static void dumpCookies(@NonNull final StringBuilder builder, @NonNull final HttpServletRequest request,
                                   @NonNull final String lf, @NonNull final String indent) {
        Stream.ofNullable(request.getCookies()).flatMap(Stream::of).sorted(Comparator.comparing(Cookie::getName)).forEach(cookie -> {
            builder.append(indent);
            builder.append("[Cookie] ").append(cookie.getName()).append(" = ").append(cookie.getValue());
            builder.append(lf);
        });
    }
}
