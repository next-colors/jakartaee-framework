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
 package jp.co.nextcolors.framework.servlet;

import java.util.Objects;

import javax.servlet.ServletResponse;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * スレッド毎にレスポンス（{@link ServletResponse} インスタンス）を管理するためのクラスです。
 *
 * @author hamana
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class ServletResponseHolder
{
	//-------------------------------------------------------------------------
	//    Public Common Properties
	//-------------------------------------------------------------------------
	/**
	 * スレッド毎にレスポンス（{@link ServletResponse} インスタンス）を管理するための自身のインスタンスです。
	 *
	 */
	public static final ServletResponseHolder RESPONSE = new ServletResponseHolder();

	//-------------------------------------------------------------------------
	//    Private Properties
	//-------------------------------------------------------------------------
	/**
	 * レスポンス（{@link ServletResponse} インスタンス）を管理するためのスレッドローカル変数です。
	 *
	 */
	private final ThreadLocal<ServletResponse> threadLocal = new ThreadLocal<>();

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * すでにレスポンス（{@link ServletResponse} インスタンス）がバインドされているかどうかを判定します。
	 *
	 * @return すでにレスポンス（{@link ServletResponse} インスタンス）がバインドされている場合は {@code true}、そうでない場合は {@code false}
	 */
	public boolean isBound()
	{
		return Objects.nonNull( threadLocal.get() );
	}

	/**
	 * レスポンス（{@link ServletResponse} インスタンス）を現在のスレッドにバインドします。
	 *
	 * @param response
	 *			レスポンス
	 * @throws IllegalStateException
	 * 			すでにレスポンス（{@link ServletResponse} インスタンス）がバインドされている場合
	 */
	public void bind( final ServletResponse response )
	{
		if ( isBound() ) {
			throw new IllegalStateException( "すでにレスポンス（" + ServletResponse.class.getName() + " インスタンス）がバインドされています。" );
		}

		threadLocal.set( response );
	}

	/**
	 * 現在のスレッドからレスポンス（{@link ServletResponse} インスタンス）を解放します。
	 *
	 */
	public void release()
	{
		threadLocal.remove();
	}

	/**
	 * 現在のスレッドにバインドされているレスポンス（{@link ServletResponse} インスタンス）を取得します。
	 *
	 * @return 現在のスレッドにバインドされているレスポンス（{@link ServletResponse} インスタンス）
	 * @throws IllegalStateException
	 * 			現在のスレッドにバインドされているレスポンス（{@link ServletResponse} インスタンス）が存在しない場合
	 */
	public ServletResponse get()
	{
		ServletResponse response = threadLocal.get();

		if ( Objects.isNull( response ) ) {
			throw new IllegalStateException( "現在のスレッドにバインドされているレスポンス（" + ServletResponse.class.getName() + " インスタンス）が存在しません。" );
		}

		return response;
	}
}
