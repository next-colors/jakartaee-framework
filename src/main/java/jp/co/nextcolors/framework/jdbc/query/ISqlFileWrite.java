package jp.co.nextcolors.framework.jdbc.query;

/**
 * SQL ファイルを使用した書き込み（挿入/更新/削除）です。
 *
 * @author hamana
 */
public interface ISqlFileWrite extends ISqlFileQuery<ISqlFileWrite>
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * レコードを書き込みます。
	 *
	 * @return レコードの書き込み件数
	 */
	int execute();
}
