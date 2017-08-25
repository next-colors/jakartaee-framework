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
