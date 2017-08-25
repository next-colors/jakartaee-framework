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
