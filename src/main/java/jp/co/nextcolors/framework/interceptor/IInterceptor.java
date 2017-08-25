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
 package jp.co.nextcolors.framework.interceptor;

import javax.interceptor.InvocationContext;

/**
 * インターセプタです。
 *
 * @author hamana
 */
public interface IInterceptor
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * インターセプトした呼び出しに対して割り込み処理を行います。
	 *
	 * @param context
	 * 			インターセプトした呼び出しに関するコンテキスト
	 * @return 割り込み処理を実行した結果
	 * @throws Exception
	 * 			割り込み処理の実行中にエラーが発生した場合
	 */
	Object invoke( InvocationContext context ) throws Exception;
}
