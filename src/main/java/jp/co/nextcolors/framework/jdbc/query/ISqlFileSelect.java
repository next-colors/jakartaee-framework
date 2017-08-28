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
 package jp.co.nextcolors.framework.jdbc.query;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;

/**
 * SQL ファイルを使用した検索です。
 *
 * @author hamana
 */
public interface ISqlFileSelect extends ISqlFileQuery<ISqlFileSelect>
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	ResultQuery<Record> getQuery();

	/**
	 * レコードを検索します。
	 *
	 * @return 検索結果
	 */
	Record fetchOne();

	/**
	 * レコードを検索します。
	 *
	 * @return 検索結果
	 */
	Result<Record> fetch();
}
