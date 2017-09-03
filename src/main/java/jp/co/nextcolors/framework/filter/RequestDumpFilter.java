/*
 * Copyright (c) 2017 NEXT COLORS Co., Ltd. All Rights Reserved.
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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
public class RequestDumpFilter implements Filter
{
	//-------------------------------------------------------------------------
	//    Private Classes
	//-------------------------------------------------------------------------
	/**
	 * リクエスト（{@link HttpServletRequest}）の内容のダンプに関する設定です。
	 *
	 * @author hamana
	 */
	@ToString
	@EqualsAndHashCode
	private static final class RequestDumpConfig
	{
		//---------------------------------------------------------------------
		//    Private Enumerations
		//---------------------------------------------------------------------
		/**
		 * ダンプに関する設定の項目を表す列挙型です。
		 *
		 * @author hamana
		 */
		@AllArgsConstructor(access = AccessLevel.PRIVATE)
		@ToString
		private enum ConfigParameter
		{
			/**
			 * リクエストヘッダの内容をダンプするかどうかの設定です。
			 *
			 */
			REQUEST_HEADER( "requestHeader" ),

			/**
			 * リクエストパラメータの内容をダンプするかどうかの設定です。
			 *
			 */
			REQUEST_PARAMETER( "requestParameter" ),

			/**
			 * クッキーの内容をダンプするかどうかの設定です。
			 *
			 */
			COOKIE( "cookie" );

			//-----------------------------------------------------------------
			//    Private Properties
			//-----------------------------------------------------------------
			/**
			 * 項目名です。
			 *
			 */
			@NonNull
			private final String name;
		}

		//---------------------------------------------------------------------
		//    Private Properties
		//---------------------------------------------------------------------
		/**
		 * リクエストヘッダの内容をダンプするかどうかです。
		 *
		 */
		private final boolean isRequestHeaderDumpMode;

		/**
		 * リクエストパラメータの内容をダンプするかどうかです。
		 *
		 */
		private final boolean isRequestParameterDumpMode;

		/**
		 * クッキーの内容をダンプするかどうかです。
		 *
		 */
		private final boolean isCookieDumpMode;

		//---------------------------------------------------------------------
		//    Private Methods
		//---------------------------------------------------------------------
		/**
		 * ダンプに関する設定の項目の値を指定された型で取得します。
		 *
		 * @param <T>
		 * 			ダンプに関する設定の項目の値の型
		 * @param filterConfig
		 * 			フィルタの設定
		 * @param configParam
		 * 			ダンプに関する設定の項目
		 * @param type
		 * 			ダンプに関する設定の項目の値の型を表すクラス
		 * @param defaultValue
		 * 			ダンプに関する設定の項目の値がない場合の値
		 * @return ダンプに関する設定の項目の値
		 */
		private <T> T getConfigParameterValue( @NonNull final FilterConfig filterConfig,
												@NonNull final ConfigParameter configParam,
												@NonNull final Class<T> type, final T defaultValue )
		{
			String paramValue = filterConfig.getInitParameter( configParam.name );

			if ( StringUtils.isEmpty( paramValue ) ) {
				return defaultValue;
			}

			return type.cast( ConvertUtils.convert( paramValue, type ) );
		}

		/**
		 * @param filterConfig
		 * 			フィルタの設定
		 */
		private RequestDumpConfig( @NonNull final FilterConfig filterConfig )
		{
			isRequestHeaderDumpMode = getConfigParameterValue( filterConfig, ConfigParameter.REQUEST_HEADER, Boolean.class, true );
			isRequestParameterDumpMode = getConfigParameterValue( filterConfig, ConfigParameter.REQUEST_PARAMETER, Boolean.class, true );
			isCookieDumpMode = getConfigParameterValue( filterConfig, ConfigParameter.COOKIE, Boolean.class, true );
		}
	}

	//-------------------------------------------------------------------------
	//    Private Constants
	//-------------------------------------------------------------------------
	/**
	 * リクエスト（{@link HttpServletRequest}）の内容をダンプする際のインデントです。
	 *
	 */
	private static final String INDENT = StringUtils.repeat( StringUtils.SPACE, 2 );

	/**
	 * リクエスト（{@link HttpServletRequest}）の内容をダンプする際の改行文字です。
	 *
	 */
	private static final String LF = System.lineSeparator();

	//-------------------------------------------------------------------------
	//    Private Properties
	//-------------------------------------------------------------------------
	/**
	 * リクエスト（{@link HttpServletRequest}）の内容のダンプに関する設定です。
	 *
	 */
	private RequestDumpConfig requestDumpConfig;

	//-------------------------------------------------------------------------
	//    Private Methods
	//-------------------------------------------------------------------------
	/**
	 * リクエスト（{@link HttpServletRequest}）の内容をダンプします。
	 *
	 * @param request
	 * 			リクエスト
	 */
	private void dump( @NonNull final HttpServletRequest request )
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append( LF );
		buffer.append( LF );
		buffer.append( "** Request Dump ***************************************************************" );
		buffer.append( LF );

		// リクエストのプロパティを文字列バッファにダンプ
		RequestDumpUtil.dumpRequestProperties( buffer, request, LF, INDENT );
		// セッションのプロパティを文字列バッファにダンプ
		RequestDumpUtil.dumpSessionProperties( buffer, request, LF, INDENT );

		if ( requestDumpConfig.isRequestHeaderDumpMode ) {
			// リクエストヘッダの内容を文字列バッファにダンプ
			RequestDumpUtil.dumpRequestHeaders( buffer, request, LF, INDENT );
		}

		if ( requestDumpConfig.isRequestParameterDumpMode ) {
			// リクエストパラメータの内容を文字列バッファにダンプ
			RequestDumpUtil.dumpRequestParameters( buffer, request, LF, INDENT );
		}

		if ( requestDumpConfig.isCookieDumpMode ) {
			// クッキーの内容を文字列バッファにダンプ
			RequestDumpUtil.dumpCookies( buffer, request, LF, INDENT );
		}

		buffer.append( "*******************************************************************************" );
		buffer.append( LF );

		log.debug( buffer.toString() );
	}

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void init( @NonNull final FilterConfig filterConfig ) throws ServletException
	{
		requestDumpConfig = new RequestDumpConfig( filterConfig );
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void doFilter( @NonNull final ServletRequest request, @NonNull final ServletResponse response, @NonNull final FilterChain chain )
				throws IOException, ServletException
	{
		if ( !log.isDebugEnabled() ) {
			chain.doFilter( request, response );

			return;
		}

		if ( !HttpServletRequest.class.isInstance( request ) ) {
			chain.doFilter( request, response );

			return;
		}

		dump( HttpServletRequest.class.cast( request ) );

		chain.doFilter( request, response );
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void destroy()
	{
		// Do nothing.
	}
}
