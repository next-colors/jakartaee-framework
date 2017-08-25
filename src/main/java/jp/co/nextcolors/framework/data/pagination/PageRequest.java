package jp.co.nextcolors.framework.data.pagination;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * {@link IPageRequest} の実装クラスです。
 *
 * @author hamana
 */
@Getter
@ToString
@EqualsAndHashCode
public class PageRequest implements IPageRequest
{
	//-------------------------------------------------------------------------
	//    Private Properties
	//-------------------------------------------------------------------------
	/**
	 * ページ番号です。
	 *
	 */
	private final int pageNumber;

	/**
	 * ページに含める要素の最大数です。
	 *
	 */
	private final int pageSize;

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * @param pageNumber
	 * 			ページ番号
	 * @param pageSize
	 * 			ページに含める要素の最大数
	 */
	public PageRequest( final int pageNumber, final int pageSize )
	{
		if ( pageNumber < 1 ) {
			throw new IllegalArgumentException( "ページ番号が 1 未満です。" );
		}

		if ( pageSize < 1 ) {
			throw new IllegalArgumentException( "ページに含める要素の最大数が 1 未満です。" );
		}

		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public int getOffset()
	{
		return (pageNumber - 1) * pageSize;
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public IPageRequest first()
	{
		return new PageRequest( 1, pageSize );
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public IPageRequest previous()
	{
		if ( hasPrevious() ) {
			return new PageRequest( pageNumber - 1, pageSize );
		}

		return this;
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public IPageRequest next()
	{
		return new PageRequest( pageNumber + 1, pageSize );
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public boolean hasPrevious()
	{
		if ( pageNumber > 1 ) {
			return true;
		}

		return false;
	}
}
