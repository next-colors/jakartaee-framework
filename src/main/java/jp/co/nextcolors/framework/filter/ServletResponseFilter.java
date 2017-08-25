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

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import jp.co.nextcolors.framework.servlet.ServletResponseHolder;

/**
 * レスポンス（{@link ServletResponse}）に関するフィルタです。
 *
 * @author hamana
 */
@ToString
@EqualsAndHashCode
public class ServletResponseFilter implements Filter
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void init( final FilterConfig filterConfig ) throws ServletException
	{
		// Do nothing.
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void doFilter( @NonNull final ServletRequest request, @NonNull final ServletResponse response, @NonNull final FilterChain chain )
				throws IOException, ServletException
	{
		ServletResponseHolder.RESPONSE.bind( response );

		try {
			chain.doFilter( request, response );
		}
		finally {
			ServletResponseHolder.RESPONSE.release();
		}
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
