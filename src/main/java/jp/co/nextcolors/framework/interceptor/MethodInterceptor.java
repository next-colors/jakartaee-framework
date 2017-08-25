package jp.co.nextcolors.framework.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * メソッドに対して割り込み処理を行うインターセプタの抽象クラスです。
 *
 * @author hamana
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
public abstract class MethodInterceptor implements IInterceptor
{
	//-------------------------------------------------------------------------
	//    Protected Methods
	//-------------------------------------------------------------------------
	/**
	 * インターセプトしたメソッドに対して割り込み処理を行います。
	 *
	 * @param context
	 * 			インターセプトしたメソッドに関するコンテキスト
	 * @return 割り込み処理を実行した結果
	 * @throws Exception
	 * 			割り込み処理の実行中にエラーが発生した場合
	 */
	protected abstract Object invokeInternal( InvocationContext context ) throws Exception;

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 *
	 */
	@AroundInvoke
	@Override
	public Object invoke( @NonNull final InvocationContext context ) throws Exception
	{
		return invokeInternal( context );
	}
}
