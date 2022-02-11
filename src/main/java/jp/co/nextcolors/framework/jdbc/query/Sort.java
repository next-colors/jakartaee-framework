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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jooq.SortOrder;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import jp.co.future.uroborosql.utils.CaseFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * ソートです。
 *
 * @author hamana
 */
@AllArgsConstructor(staticName = "by")
@Getter
@EqualsAndHashCode
@SuppressWarnings("serial")
public class Sort implements Serializable {
    //-------------------------------------------------------------------------
    //    Public Classes
    //-------------------------------------------------------------------------

    /**
     * ソート順です。
     */
    @NonNull
    private final Collection<Order> orders;

    //-------------------------------------------------------------------------
    //    Private Properties
    //-------------------------------------------------------------------------

    /**
     * ソートを生成します。
     *
     * @param orders ソート順
     * @return ソート
     */
    public static Sort by(@NonNull final Order... orders) {
        return by(List.of(orders));
    }

    //-------------------------------------------------------------------------
    //    Public Methods
    //-------------------------------------------------------------------------

    /**
     * ソートを結合します。
     *
     * @param sort 結合するソート
     * @return 結合したソート
     */
    public Sort and(@NonNull final Sort sort) {
        List<Order> orders = new ArrayList<>();
        orders.addAll(this.orders);
        orders.addAll(sort.getOrders());

        return by(List.copyOf(orders));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return StringUtils.join(orders, ", ");
    }

    /**
     * ソート順です。
     *
     * @author hamana
     */
    @AllArgsConstructor(staticName = "by")
    @Getter
    @EqualsAndHashCode
    public static class Order implements Serializable {
        //---------------------------------------------------------------------
        //    Private Properties
        //---------------------------------------------------------------------
        /**
         * プロパティ名/カラム名です。
         */
        @NonNull
        private final String name;

        /**
         * ソートの順序です。
         */
        @NonNull
        private final SortOrder sortOrder;

        //---------------------------------------------------------------------
        //    Public Methods
        //---------------------------------------------------------------------

        /**
         * ソート順を生成します。
         *
         * @param name プロパティ名/カラム名
         * @return ソート順
         */
        public static Order by(@NonNull final String name) {
            return by(name, SortOrder.DEFAULT);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            String columnName = CaseFormat.LOWER_SNAKE_CASE.convert(name);

            String direction = Strings.emptyToNull(sortOrder.toSQL().toUpperCase());

            return Joiner.on(StringUtils.SPACE).skipNulls().join(columnName, direction);
        }
    }
}
