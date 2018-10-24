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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * リクエスト（{@link HttpServletRequest}）の内容をダンプするユーティリティです。
 *
 * @author hamana
 */
@UtilityClass
public class RequestDumpUtil
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * リクエストのプロパティを文字列バッファにダンプします。
	 *
	 * @param buffer
	 *         文字列バッファ
	 * @param request
	 *         リクエスト
	 * @param lf
	 *         改行文字
	 * @param indent
	 *         インデント
	 */
	public static void dumpRequestProperties( @NonNull final StringBuffer buffer, @NonNull final HttpServletRequest request,
												@NonNull final String lf, @NonNull final String indent )
	{
		buffer.append( indent );
		buffer.append( "Request Class = " ).append( request.getClass().getName() );
		buffer.append( ", Instance = " ).append( request );
		buffer.append( lf );

		buffer.append( indent );
		buffer.append( "RequestedSessionId = " ).append( request.getRequestedSessionId() );
		buffer.append( lf );

		buffer.append( indent );
		buffer.append( "REQUEST_URI = " ).append( request.getRequestURI() );
		buffer.append( ", SERVLET_PATH = " ).append( request.getServletPath() );
		buffer.append( lf );

		buffer.append( indent );
		buffer.append( "CharacterEncoding = " ).append( request.getCharacterEncoding() );
		buffer.append( ", ContentLength = " ).append( request.getContentLength() );
		buffer.append( ", ContentType = " ).append( request.getContentType() );
		buffer.append( ", Locale = " ).append( request.getLocale() );
		buffer.append( ", Locales = " ).append( Collections.list( request.getLocales() ) );
		buffer.append( ", Scheme = " ).append( request.getScheme() );
		buffer.append( ", isSecure = " ).append( request.isSecure() );
		buffer.append( lf );

		buffer.append( indent );
		buffer.append( "SERVER_PROTOCOL = " ).append( request.getProtocol() );
		buffer.append( ", REMOTE_ADDR = " ).append( request.getRemoteAddr() );
		buffer.append( ", REMOTE_HOST = " ).append( request.getRemoteHost() );
		buffer.append( ", SERVER_NAME = " ).append( request.getServerName() );
		buffer.append( ", SERVER_PORT = " ).append( request.getServerPort() );
		buffer.append( lf );

		buffer.append( indent );
		buffer.append( "ContextPath = " ).append( request.getContextPath() );
		buffer.append( ", REQUEST_METHOD = " ).append( request.getMethod() );
		buffer.append( ", QUERY_STRING = " ).append( Arrays.toString( StringUtils.split( request.getQueryString(), '&' ) ) );
		buffer.append( ", PathInfo = " ).append( request.getPathInfo() );
		buffer.append( ", RemoteUser = " ).append( request.getRemoteUser() );
		buffer.append( lf );
	}

	/**
	 * セッションのプロパティを文字列バッファにダンプします。
	 *
	 * @param buffer
	 *         文字列バッファ
	 * @param request
	 *         リクエスト
	 * @param lf
	 *         改行文字
	 * @param indent
	 *         インデント
	 */
	public static void dumpSessionProperties( @NonNull final StringBuffer buffer, @NonNull final HttpServletRequest request,
												@NonNull final String lf, @NonNull final String indent )
	{
		HttpSession session = request.getSession( false );

		if ( Objects.isNull( session ) ) {
			return;
		}

		buffer.append( indent );
		buffer.append( "Session :: SessionId = " ).append( session.getId() );
		buffer.append( lf );

		buffer.append( indent );
		buffer.append( "Session :: CreationTime = " ).append( ZonedDateTime.ofInstant( Instant.ofEpochMilli( session.getCreationTime() ), ZoneId.systemDefault() ) );
		buffer.append( ", LastAccessedTime = " ).append( ZonedDateTime.ofInstant( Instant.ofEpochMilli( session.getLastAccessedTime() ), ZoneId.systemDefault() ) );
		buffer.append( ", MaxInactiveInterval = " ).append( session.getMaxInactiveInterval() );
		buffer.append( lf );
	}

	/**
	 * リクエストヘッダの内容を文字列バッファにダンプします。
	 *
	 * @param buffer
	 *         文字列バッファ
	 * @param request
	 *         リクエスト
	 * @param lf
	 *         改行文字
	 * @param indent
	 *         インデント
	 */
	public static void dumpRequestHeaders( @NonNull final StringBuffer buffer, @NonNull final HttpServletRequest request,
											@NonNull final String lf, @NonNull final String indent )
	{
		Collections.list( request.getHeaderNames() ).stream().sorted().forEach( headerName -> {
			buffer.append( indent );
			buffer.append( "[Header] " ).append( headerName ).append( " = " ).append( request.getHeader( headerName ) );
			buffer.append( lf );
		} );
	}

	/**
	 * リクエストパラメータの内容を文字列バッファにダンプします。
	 *
	 * @param buffer
	 *         文字列バッファ
	 * @param request
	 *         リクエスト
	 * @param lf
	 *         改行文字
	 * @param indent
	 *         インデント
	 */
	public static void dumpRequestParameters( @NonNull final StringBuffer buffer, @NonNull final HttpServletRequest request,
												@NonNull final String lf, @NonNull final String indent )
	{
		Collections.list( request.getParameterNames() ).stream().sorted().forEach( paramName -> {
			buffer.append( indent );
			buffer.append( "[Parameter] " ).append( paramName ).append( " = " ).append( StringUtils.join( request.getParameterValues( paramName ), ", " ) );
			buffer.append( lf );
		} );
	}

	/**
	 * クッキーの内容を文字列バッファにダンプします。
	 *
	 * @param buffer
	 *         文字列バッファ
	 * @param request
	 *         リクエスト
	 * @param lf
	 *         改行文字
	 * @param indent
	 *         インデント
	 */
	public static void dumpCookies( @NonNull final StringBuffer buffer, @NonNull final HttpServletRequest request,
									@NonNull final String lf, @NonNull final String indent )
	{
		Cookie[] cookies = request.getCookies();

		if ( ArrayUtils.isEmpty( cookies ) ) {
			return;
		}

		Arrays.stream( cookies ).sorted( Comparator.comparing( Cookie::getName ) ).forEach( cookie -> {
			buffer.append( indent );
			buffer.append( "[Cookie] " ).append( cookie.getName() ).append( " = " ).append( cookie.getValue() );
			buffer.append( lf );
		} );
	}
}
