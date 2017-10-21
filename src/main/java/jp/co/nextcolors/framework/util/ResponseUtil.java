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
package jp.co.nextcolors.framework.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * レスポンスに関するユーティリティです。
 *
 * @author hamana
 */
@UtilityClass
public class ResponseUtil
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * ダウンロードレスポンスを出力します。
	 *
	 * @param response
	 * 			レスポンス
	 * @param fileName
	 *			レスポンスとして返されるファイル名
	 * @param data
	 *			ダウンロードさせたいデータ
	 * @throws IOException
	 * 			ユーザが途中でダウンロードを中断した場合
	 */
	public static void download( @NonNull final HttpServletResponse response,
									@NonNull final String fileName,
									@NonNull final byte[] data )
						throws IOException
	{
		download( response, fileName, new ByteArrayInputStream( data ), data.length );
	}

	/**
	 * 指定された入力ストリームから読み込んで、ダウンロードレスポンスを出力します。
	 *
	 * @param response
	 * 			レスポンス
	 * @param fileName
	 * 			レスポンスとして返されるファイル名
	 * @param input
	 * 			ダウンロードさせたいデータ
	 * @throws IOException
	 * 			入力ストリームから読み込めないか、ユーザが途中でダウンロードを中断した場合
	 */
	public static void download( @NonNull final HttpServletResponse response,
									@NonNull final String fileName,
									@NonNull final InputStream input )
						throws IOException
	{
		response.setContentType( MediaType.OCTET_STREAM.toString() );
		response.setHeader( HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"" );

		try ( OutputStream output = response.getOutputStream() ) {
			IOUtils.copy( input, output );
		}
	}

	/**
	 * 指定されたストリームから読み込んで、指定した Content-Length と共にダウンロードレスポンスを出力します。
	 *
	 * @param response
	 * 			レスポンス
	 * @param fileName
	 * 			レスポンスとして返されるファイル名
	 * @param input
	 * 			ダウンロードさせたいデータ
	 * @param length
	 * 			"Content-Length" フィールドの値
	 * @throws IOException
	 * 			入力ストリームから読み込めないか、ユーザが途中でダウンロードを中断した場合
	 */
	public static void download( @NonNull final HttpServletResponse response,
									@NonNull final String fileName,
									@NonNull final InputStream input,
									final long length )
						throws IOException
	{
		response.setContentLengthLong( length );
		download( response, fileName, input );
	}

	/**
	 * レスポンスにテキストを書き込みます。
	 *
	 * @param response
	 * 			レスポンス
	 * @param text
	 *			テキスト
	 * @throws IOException
	 * 			テキストの書き込みに失敗した場合
	 */
	public static void write( @NonNull final HttpServletResponse response, final String text )
						throws IOException
	{
		write( response, text, null, null );
	}

	/**
	 * レスポンスにテキストを書き込みます。
	 *
	 * @param response
	 * 			レスポンス
	 * @param text
	 *			テキスト
	 * @param contentType
	 *			コンテントタイプ（指定しなかった場合は、text/plain が設定されます。）
	 * @throws IOException
	 * 			テキストの書き込みに失敗した場合
	 */
	public static void write( @NonNull final HttpServletResponse response,
								final String text, final String contentType )
						throws IOException
	{
		write( response, text, contentType, null );
	}

	/**
	 * レスポンスにテキストを書き込みます。
	 *
	 * @param response
	 * 			レスポンス
	 * @param text
	 *			テキスト
	 * @param contentType
	 *			コンテントタイプ（指定しなかった場合は、text/plain が設定されます。）
	 * @param encoding
	 *			エンコーディング（指定しなかった場合は、UTF-8 が設定されます。）
	 * @throws IOException
	 * 			テキストの書き込みに失敗した場合
	 */
	public static void write( @NonNull final HttpServletResponse response,
								final String text, String contentType, Charset encoding )
						throws IOException
	{
		contentType = StringUtils.defaultIfEmpty( contentType, MediaType.PLAIN_TEXT_UTF_8.toString() );
		encoding = ObjectUtils.defaultIfNull( encoding, StandardCharsets.UTF_8 );

		MediaType mediaType = MediaType.parse( contentType ).withCharset( encoding );

		response.setContentType( mediaType.toString() );

		try ( OutputStream output = response.getOutputStream() ) {
			IOUtils.write( text, output, encoding );
		}
	}
}
