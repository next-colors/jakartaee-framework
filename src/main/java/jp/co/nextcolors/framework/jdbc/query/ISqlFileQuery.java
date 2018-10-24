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
package jp.co.nextcolors.framework.jdbc.query;

import java.util.Map;

import org.jooq.Query;

/**
 * SQL ファイルを使用して問い合わせ行うためのベースとなるインターフェースです。
 *
 * @author hamana
 * @param <S>
 * 			{@link ISqlFileQuery} のサブタイプです。
 */
public interface ISqlFileQuery<S extends ISqlFileQuery<S>>
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * パラメータを追加します。
	 *
	 * @param name
	 * 			パラメータ名
	 * @param value
	 * 			パラメータ値
	 * @return このインスタンス自身
	 */
	S addParameter( String name, Object value );

	/**
	 * パラメータを追加します。
	 *
	 * @param params
	 * 			パラメータ
	 * @return このインスタンス自身
	 */
	S addParameters( Map<String, Object> params );

	/**
	 * パラメータを設定します。
	 *
	 * @param params
	 * 			パラメータ
	 * @return このインスタンス自身
	 */
	S setParameters( Map<String, Object> params );

	/**
	 * クエリを取得します。
	 *
	 * @return クエリ
	 */
	Query getQuery();
}
