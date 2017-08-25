package jp.co.nextcolors.framework.data.pagination;

import java.util.List;
import java.util.function.Function;

/**
 * ページです。
 *
 * @author hamana
 * @param <T>
 * 			ページに含まれる要素の型です。
 */
public interface IPage<T>
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * ページ番号を取得します。
	 *
	 * @return ページ番号
	 */
	int getNumber();

	/**
	 * ページに含まれる要素の最大数を取得します。
	 *
	 * @return ページに含める要素の最大数
	 */
	int getSize();

	/**
	 * ページに含まれる要素の数を取得します。
	 *
	 * @return ページに含まれる要素の数を取得します。
	 */
	int getNumberOfElements();

	/**
	 * ページの総数を取得します。
	 *
	 * @return ページの総数
	 */
	int getTotalPages();

	/**
	 * 要素の総数を取得します。
	 *
	 * @return 要素の総数
	 */
	int getTotalElements();

	/**
	 * ページに含まれる要素を取得します。
	 *
	 * @return ページに含まれる要素
	 */
	List<T> getElements();

	/**
	 * ページに要素が含まれているかどうかを判定します。
	 *
	 * @return ページに要素が含まれている場合は {@code true}、そうでない場合は {@code false}
	 */
	boolean hasElements();

	/**
	 * 最初のページであるかどうかを判定します。
	 *
	 * @return 最初のページである場合は {@code true}、そうでない場合は {@code false}
	 */
	boolean isFirst();

	/**
	 * 最後のページであるかどうかを判定します。
	 *
	 * @return 最後のページである場合は {@code true}、そうでない場合は {@code false}
	 */
	boolean isLast();

	/**
	 * 前のページが存在するかどうかを判定します。
	 *
	 * @return 前のページが存在する場合は {@code true}、そうでない場合は {@code false}
	 */
	boolean hasPrevious();

	/**
	 * 次のページが存在するかどうかを判定します。
	 *
	 * @return 次のページが存在する場合は {@code true}、そうでない場合は {@code false}
	 */
	boolean hasNext();

	/**
	 * ページ付けの情報を取得します。
	 *
	 * @return ページ付けの情報
	 */
	IPageRequest getPageRequest();

	/**
	 * 前のページのページ付けの情報を取得します。
	 *
	 * @return 前のページのページ付けの情報
	 */
	IPageRequest getPreviousPageRequest();

	/**
	 * 次のページのページ付けの情報を取得します。
	 *
	 * @return 次のページのページ付けの情報
	 */
	IPageRequest getNextPageRequest();

	/**
	 * ページに含まれる要素に指定された関数を適用した結果から構成されるページを取得します。
	 *
	 * @param <R>
	 * 			ページに含まれる要素に指定された関数を適用した結果の型
	 * @param mapper
	 * 			ページに含まれる要素に適用する関数
	 * @return ページに含まれる要素に指定された関数を適用した結果から構成されるページ
	 */
	<R> IPage<R> map( Function<? super T, ? extends R> mapper );
}
