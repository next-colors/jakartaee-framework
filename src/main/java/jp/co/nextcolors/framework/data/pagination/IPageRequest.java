package jp.co.nextcolors.framework.data.pagination;

/**
 * ページ付けの情報です。
 *
 * @author hamana
 */
public interface IPageRequest
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * ページ番号を取得します。
	 *
	 * @return ページ番号
	 */
	int getPageNumber();

	/**
	 * ページに含める要素の最大数を取得します。
	 *
	 * @return ページに含める要素の最大数
	 */
	int getPageSize();

	/**
	 * ページに含める最初の要素の番号を取得します。
	 *
	 * @return ページに含める最初の要素の番号
	 */
	int getOffset();

	/**
	 * 最初のページのページ付けの情報を取得します。
	 *
	 * @return 最初のページのページ付けの情報
	 */
	IPageRequest first();

	/**
	 * 前のページのページ付けの情報を取得します。
	 *
	 * @return 前のページのページ付けの情報
	 */
	IPageRequest previous();

	/**
	 * 次のページのページ付けの情報を取得します。
	 *
	 * @return 次のページのページ付けの情報
	 */
	IPageRequest next();

	/**
	 * 前のページのページ付けができるかどうかを判定します。
	 *
	 * @return 前のページのページ付けができる場合は {@code true}、そうでない場合は {@code false}
	 */
	boolean hasPrevious();
}
