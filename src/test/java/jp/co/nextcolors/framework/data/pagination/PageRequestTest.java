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
package jp.co.nextcolors.framework.data.pagination;

import static net.andreinc.mockneat.unit.types.Ints.ints;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * {@link PageRequest} のテストです。
 *
 * @author hamana
 */
class PageRequestTest {
    /**
     * {@link PageRequest#PageRequest(int, int)} のテストです。
     */
    @Test
    void testConstructor() {
        Stream.of(0, Integer.MIN_VALUE).forEach(pageNumber -> {
            final int pageSize = ints().lowerBound(1).get();

            assertThatIllegalArgumentException().isThrownBy(() -> new PageRequest(pageNumber, pageSize));
        });

        Stream.of(0, Integer.MIN_VALUE).forEach(pageSize -> {
            final int pageNumber = ints().lowerBound(1).get();

            assertThatIllegalArgumentException().isThrownBy(() -> new PageRequest(pageNumber, pageSize));
        });
    }

    /**
     * {@link PageRequest#getOffset()} のテストです。
     */
    @Test
    void testGetOffset() {
        final int pageNumber = ints().lowerBound(1).get();
        final int pageSize = ints().lowerBound(1).get();

        final PageRequest pageRequest = new PageRequest(pageNumber, pageSize);

        assertThat(pageRequest.getOffset()).isEqualTo((pageNumber - 1) * pageSize);
    }

    /**
     * {@link PageRequest#first()} のテストです。
     */
    @Test
    void testFirst() {
        final int pageNumber = ints().lowerBound(1).get();
        final int pageSize = ints().lowerBound(1).get();

        final PageRequest pageRequest = new PageRequest(pageNumber, pageSize);

        assertThat(pageRequest.first()).isEqualTo(new PageRequest(1, pageSize));
    }

    /**
     * {@link PageRequest#previous()} のテストです。
     */
    @Test
    void testPrevious() {
        final int pageSize = ints().lowerBound(1).get();

        Stream.of(2, Integer.MAX_VALUE).forEach(pageNumber -> {
            final PageRequest pageRequest = new PageRequest(pageNumber, pageSize);

            assertThat(pageRequest.previous()).isEqualTo(new PageRequest(pageNumber - 1, pageSize));
        });

        final PageRequest pageRequest = new PageRequest(1, pageSize);

        assertThat(pageRequest.previous()).isEqualTo(pageRequest);
    }

    /**
     * {@link PageRequest#next()} のテストです。
     */
    @Test
    void testNext() {
        final int pageNumber = ints().lowerBound(1).get();
        final int pageSize = ints().lowerBound(1).get();

        final PageRequest pageRequest = new PageRequest(pageNumber, pageSize);

        assertThat(pageRequest.next()).isEqualTo(new PageRequest(pageNumber + 1, pageSize));
    }

    /**
     * {@link PageRequest#hasPrevious()} のテストです。
     */
    @Test
    void testHasPrevious() {
        final int pageSize = ints().lowerBound(1).get();

        Stream.of(2, Integer.MAX_VALUE).forEach(pageNumber -> {
            final PageRequest pageRequest = new PageRequest(pageNumber, pageSize);

            assertThat(pageRequest.hasPrevious()).isTrue();
        });

        final PageRequest pageRequest = new PageRequest(1, pageSize);

        assertThat(pageRequest.hasPrevious()).isFalse();
    }
}
