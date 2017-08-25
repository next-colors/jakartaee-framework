package jp.co.nextcolors.framework.interceptor;

import javax.interceptor.AroundConstruct;
import javax.interceptor.InvocationContext;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * コンストラクタに対して割り込み処理を行うインターセプタの抽象クラスです。
 *
 * @author hamana
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
public abstract class ConstructorInterceptor implements IInterceptor
{
	//-------------------------------------------------------------------------
	//    Protected Methods
	//-------------------------------------------------------------------------
	/**
	 * インターセプトしたコンストラクタに対して割り込み処理を行います。
	 *
	 * @param context
	 * 			インターセプトしたコンストラクタに関するコンテキスト
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
	@AroundConstruct
	@Override
	public Object invoke( @NonNull final InvocationContext context ) throws Exception
	{
		return invokeInternal( context );
	}
}
