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

import static net.andreinc.mockneat.unit.objects.Filler.filler;
import static net.andreinc.mockneat.unit.text.Strings.strings;
import static net.andreinc.mockneat.unit.types.Ints.ints;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.jooq.SortOrder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jp.co.nextcolors.framework.jdbc.query.Sort.Order;

/**
 * {@link Sort} のテストです。
 *
 * @author hamana
 */
class SortTest {
    /**
     * {@link Sort#by(Order...)} のテストです。
     */
    @Test
    void testBy() {
        assertThatNoException().isThrownBy(() -> Sort.by(Order.by(strings().get())));

        assertThatNullPointerException().isThrownBy(() -> Sort.by((Order[]) null));
    }

    /**
     * {@link Sort#and(Sort)} のテストです。
     */
    @Test
    void testAnd() {
        final Sort sort = Sort.by(filler(() -> Order.by(strings().get())).collection(ints().range(1, 10)).get());

        final Sort other = Sort.by(filler(() -> Order.by(strings().get())).collection(ints().range(1, 10)).get());

        assertThat(sort.and(other).getOrders()).containsAll(sort.getOrders()).containsAll(other.getOrders());

        assertThatNullPointerException().isThrownBy(() -> sort.and(null));
    }

    /**
     * {@link Sort#toString()} のテストです。
     */
    @Test
    void testToString() {
        final Sort singleSort = Sort.by(Order.by(strings().get()));

        assertThat(singleSort.toString()).isEqualTo(singleSort.getOrders().stream().map(Order::toString).findFirst().get());

        final Sort multiSort = Sort.by(filler(() -> Order.by(strings().get())).collection(ints().range(2, 10)).get());

        assertThat(multiSort.toString()).contains(multiSort.getOrders().stream().map(Order::toString).toList()).contains(", ");
    }

    /**
     * {@link Order} のテストです。
     *
     * @author hamana
     */
    @Nested
    class OrderTest {
        /**
         * {@link Order#by(String)} のテストです。
         */
        @Test
        void testBy() {
            assertThatNoException().isThrownBy(() -> Order.by(strings().get()));
        }

        /**
         * {@link Order#toString()} のテストです。
         */
        @Test
        void testToString() {
            Stream.of(SortOrder.values()).forEach(sortOrder -> {
                final Order order = Order.by("FooBar", sortOrder);

                if (sortOrder.toSQL().isEmpty()) {
                    assertThat(order.toString()).isEqualTo("foo_bar");
                } else {
                    assertThat(order.toString()).isEqualTo(String.join(StringUtils.SPACE, "foo_bar", sortOrder.toSQL().toUpperCase()));
                }
            });
        }
    }
}
