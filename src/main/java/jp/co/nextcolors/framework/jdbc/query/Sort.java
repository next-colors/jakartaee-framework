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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.jooq.SortOrder;

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
    /**
     * ソート順です。
     */
    @NonNull
    private final Collection<Order> orders;

    /**
     * ソートを生成します。
     *
     * @param orders ソート順
     * @return ソート
     */
    public static Sort by(@NonNull final Order... orders) {
        return by(List.of(orders));
    }

    /**
     * ソートを結合します。
     *
     * @param sort 結合するソート
     * @return 結合したソート
     */
    public Sort and(@NonNull final Sort sort) {
        final List<Order> orders = new ArrayList<>(this.orders);
        orders.addAll(sort.getOrders());

        return by(List.copyOf(orders));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return orders.stream().map(Order::toString).collect(Collectors.joining(", "));
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

        /**
         * ソート順を生成します。
         *
         * @param name プロパティ名/カラム名
         * @return ソート順
         */
        public static Order by(final String name) {
            return by(name, SortOrder.DEFAULT);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            final String columnName = CaseFormat.LOWER_SNAKE_CASE.convert(name);
            final String direction = sortOrder.toSQL().toUpperCase();

            return Stream.of(columnName, direction)
                    .filter(Predicate.not(String::isEmpty))
                    .collect(Collectors.joining(StringUtils.SPACE));
        }
    }
}
