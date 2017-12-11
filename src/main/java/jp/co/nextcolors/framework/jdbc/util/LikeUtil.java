package jp.co.nextcolors.framework.jdbc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * LIKE 述語用のユーティリティです。
 *
 * @author hamana
 */
@UtilityClass
public class LikeUtil
{
	//-------------------------------------------------------------------------
	//    Private Constants
	//-------------------------------------------------------------------------
	/**
	 * LIKE 述語で指定される検索条件中のワイルドカードの正規表現です。
	 *
	 */
	private static final Pattern WILDCARD_PATTERN = Pattern.compile( "[%_％＿]" );

	/**
	 * LIKE 述語で指定される検索条件中のワイルドカードを置換するための正規表現です。
	 *
	 */
	private static final Pattern WILDCARD_REPLACEMENT_PATTERN = Pattern.compile( "[$%_％＿]" );

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * LIKE 述語で使用される検索条件にワイルドカードが含まれているかどうかを判定します。
	 *
	 * @param likeCondition
	 *			LIKE 述語で使用される検索条件
	 * @return LIKE 述語で使用される検索条件にワイルドカードが含まれている場合は {@code true}、そうでない場合は {@code false}
	 */
	public static boolean containsWildcard( @NonNull final String likeCondition )
	{
		Matcher matcher = WILDCARD_PATTERN.matcher( likeCondition );

		return matcher.find();
	}

	/**
	 * LIKE 述語で使用される検索条件のワイルドカードを {@code '$'} でエスケープします。
	 *
	 * @param likeCondition
	 *			LIKE 述語で使用される検索条件
	 * @return ワイルドカードを {@code '$'} でエスケープした検索条件
	 */
	public static String escapeWildcard( @NonNull final String likeCondition )
	{
		Matcher matcher = WILDCARD_REPLACEMENT_PATTERN.matcher( likeCondition );

		return matcher.replaceAll( "\\$$0" );
	}
}
