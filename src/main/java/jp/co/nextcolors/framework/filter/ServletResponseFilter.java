package jp.co.nextcolors.framework.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import jp.co.nextcolors.framework.servlet.ServletResponseHolder;

/**
 * レスポンス（{@link ServletResponse}）に関するフィルタです。
 *
 * @author hamana
 */
@ToString
@EqualsAndHashCode
public class ServletResponseFilter implements Filter
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void init( final FilterConfig filterConfig ) throws ServletException
	{
		// Do nothing.
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void doFilter( @NonNull final ServletRequest request, @NonNull final ServletResponse response, @NonNull final FilterChain chain )
				throws IOException, ServletException
	{
		ServletResponseHolder.RESPONSE.bind( response );

		try {
			chain.doFilter( request, response );
		}
		finally {
			ServletResponseHolder.RESPONSE.release();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void destroy()
	{
		// Do nothing.
	}
}
