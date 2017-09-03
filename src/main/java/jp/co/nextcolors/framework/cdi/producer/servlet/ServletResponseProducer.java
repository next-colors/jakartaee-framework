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
package jp.co.nextcolors.framework.cdi.producer.servlet;

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Typed;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import jp.co.nextcolors.framework.servlet.ServletResponseHolder;

/**
 * レスポンス（{@link ServletResponse}/{@link HttpServletResponse} インスタンス）をインジェクションするためのプロデューサーです。
 *
 * @author hamana
 */
@ToString
@EqualsAndHashCode
@ApplicationScoped
public class ServletResponseProducer
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * レスポンス（{@link ServletResponse} インスタンス）を取得します。
	 *
	 * @return レスポンス（{@link ServletResponse} インスタンス）
	 * @throws IllegalStateException
	 * 			有効なレスポンス（{@link ServletResponse} インスタンス）が存在しない場合
	 */
	@RequestScoped
	@Produces
	public ServletResponse getServletResponse() throws IllegalStateException
	{
		ServletResponse response = ServletResponseHolder.RESPONSE.get();

		if ( Objects.nonNull( response ) ) {
			return response;
		}

		throw new IllegalStateException( "有効なレスポンス（" + ServletResponse.class.getName() + " インスタンス）が存在しません。" );
	}

	/**
	 * レスポンス（{@link HttpServletResponse} インスタンス）を取得します。
	 *
	 * @return レスポンス（{@link HttpServletResponse} インスタンス）
	 * @throws IllegalStateException
	 * 			有効なレスポンス（{@link HttpServletResponse} インスタンス）が存在しない場合、
	 * 			レスポンスが {@link HttpServletResponse} インスタンスではない場合
	 */
	@RequestScoped
	@Produces
	@Typed(HttpServletResponse.class)
	public HttpServletResponse getHttpServletResponse() throws IllegalStateException
	{
		ServletResponse response = getServletResponse();

		if ( HttpServletResponse.class.isInstance( response ) ) {
			return HttpServletResponse.class.cast( response );
		}

		throw new IllegalStateException( "レスポンスは、" + HttpServletResponse.class.getName() + " インスタンスではありません。" );
	}
}
