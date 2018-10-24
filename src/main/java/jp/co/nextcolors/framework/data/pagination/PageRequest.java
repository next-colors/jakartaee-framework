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
	 *          ページ番号
	 * @param pageSize
	 *          ページに含める要素の最大数
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
