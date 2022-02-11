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
package jp.co.nextcolors.framework.jdbc.query;

/**
 * SQL ファイルを使用した書き込み（挿入/更新/削除）です。
 *
 * @author hamana
 */
public interface ISqlFileWrite extends ISqlFileQuery<ISqlFileWrite> {
    /**
     * レコードを書き込みます。
     *
     * @return レコードの書き込み件数
     */
    int execute();
}
