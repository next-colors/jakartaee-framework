/*
 * Copyright (c) 2017 NEXT COLORS Co., Ltd. All Rights Reserved.
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
