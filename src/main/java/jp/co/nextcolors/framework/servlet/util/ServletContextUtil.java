package jp.co.nextcolors.framework.servlet.util;

import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.tools.JavaFileObject;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * Servlet コンテキストに関するユーティリティです。
 *
 * @author hamana
 */
@UtilityClass
public class ServletContextUtil
{
	//-------------------------------------------------------------------------
	//    Private Constants
	//-------------------------------------------------------------------------
	/**
	 * クラスファイルの正規表現です。
	 *
	 */
	private static final Pattern CLASS_FILE_PATTERN = Pattern.compile( ".+\\" + JavaFileObject.Kind.CLASS.extension );

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * クラスパス配下のクラスをリフレクトするためのスキャナ（{@link Reflections} インスタンス）を返します。
	 *
	 * @param context
	 * 			Servlet コンテキスト
	 * @return クラスパス配下のクラスをリフレクトするためのスキャナ（{@link Reflections} インスタンス）
	 */
	public static Reflections getClassReflectionScanner( @NonNull final ServletContext context )
	{
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.addUrls( ClasspathHelper.forWebInfClasses( context ) );
		builder.addUrls( ClasspathHelper.forWebInfLib( context ) );
		builder.setInputsFilter( new FilterBuilder().include( CLASS_FILE_PATTERN.pattern() ) );

		return new Reflections( builder );
	}
}
