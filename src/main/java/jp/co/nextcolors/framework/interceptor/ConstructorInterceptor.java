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
public abstract class ConstructorInterceptor implements IInterceptor {
    //-------------------------------------------------------------------------
    //    Protected Methods
    //-------------------------------------------------------------------------

    /**
     * インターセプトしたコンストラクタに対して割り込み処理を行います。
     *
     * @param context インターセプトしたコンストラクタに関するコンテキスト
     * @return 割り込み処理を実行した結果
     * @throws Exception 割り込み処理の実行中にエラーが発生した場合
     */
    protected abstract Object invokeInternal(InvocationContext context) throws Exception;

    //-------------------------------------------------------------------------
    //    Public Methods
    //-------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @AroundConstruct
    @Override
    public Object invoke(@NonNull final InvocationContext context) throws Exception {
        return invokeInternal(context);
    }
}
