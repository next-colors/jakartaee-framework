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

import static net.andreinc.mockneat.unit.objects.Filler.filler;
import static net.andreinc.mockneat.unit.types.Ints.ints;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.NonNull;

/**
 * {@link Page} のテストです。
 *
 * @author hamana
 */
@ExtendWith(MockitoExtension.class)
class PageTest {
    @Mock
    private IPageRequest pageRequest;

    /**
     * {@link Page#Page(IPageRequest, List, int)} のテストです。
     */
    @Test
    void testConstructor() {
        final List<Foo> elements = filler(Foo::new).list(ints().range(1, 10)).get();
        final int totalElements = ints().lowerBound(elements.size()).get();

        assertThatNullPointerException().isThrownBy(() -> new Page<Foo>(null, elements, totalElements));
        assertThatNullPointerException().isThrownBy(() -> new Page<Foo>(pageRequest, null, totalElements));
    }

    /**
     * {@link Page#getNumber()} のテストです。
     */
    @Test
    void testGetNumber() {
        final List<Foo> elements = filler(Foo::new).list(ints().range(1, 10)).get();
        final int totalElements = ints().lowerBound(elements.size()).get();

        final Page<Foo> page = new Page<>(pageRequest, elements, totalElements);

        assertThat(page.getNumber()).isEqualTo(pageRequest.getPageNumber());
    }

    /**
     * {@link Page#getSize()} のテストです。
     */
    @Test
    void testGetSize() {
        final List<Foo> elements = filler(Foo::new).list(ints().range(1, 10)).get();
        final int totalElements = ints().lowerBound(elements.size()).get();

        final Page<Foo> page = new Page<>(pageRequest, elements, totalElements);

        assertThat(page.getSize()).isEqualTo(pageRequest.getPageSize());
    }

    /**
     * {@link Page#getNumberOfElements()} のテストです。
     */
    @Test
    void testGetNumberOfElements() {
        final List<Foo> elements = filler(Foo::new).list(ints().range(1, 10)).get();
        final int totalElements = ints().lowerBound(elements.size()).get();

        final Page<Foo> page = new Page<>(pageRequest, elements, totalElements);

        assertThat(page.getNumberOfElements()).isEqualTo(elements.size());
    }

    /**
     * {@link Page#getTotalPages()} のテストです。
     */
    @Test
    void testGetTotalPages() {
        final List<Foo> elements = filler(Foo::new).list(ints().range(1, 10)).get();
        final int totalElements = ints().lowerBound(elements.size()).get();

        final Page<Foo> page = new Page<>(pageRequest, elements, totalElements);

        assertThat(page.getTotalPages()).isEqualTo((int) Math.ceil((double) totalElements / page.getSize()));
    }

    /**
     * {@link Page#hasElements()} のテストです。
     */
    @Test
    void testHasElements() {
        final List<Foo> elements = filler(Foo::new).list(ints().range(1, 10)).get();
        final int totalElements = ints().lowerBound(elements.size()).get();

        Page<Foo> page = new Page<>(pageRequest, elements, totalElements);

        assertThat(page.hasElements()).isTrue();

        page = new Page<>(pageRequest, Collections.emptyList(), totalElements);

        assertThat(page.hasElements()).isFalse();
    }

    /**
     * {@link Page#isFirst()} のテストです。
     */
    @SuppressWarnings("unchecked")
    @Test
    void testIsFirst() {
        final List<Foo> elements = filler(Foo::new).list(ints().range(1, 10)).get();
        final int totalElements = ints().lowerBound(elements.size()).get();

        final Page<Foo> page = spy(new Page<>(pageRequest, elements, totalElements));

        doReturn(false).when(page).hasPrevious();
        assertThat(page.isFirst()).isTrue();
        reset(page);

        doReturn(true).when(page).hasPrevious();
        assertThat(page.isFirst()).isFalse();
        reset(page);
    }

    /**
     * {@link Page#isLast()} のテストです。
     */
    @SuppressWarnings("unchecked")
    @Test
    void testIsLast() {
        final List<Foo> elements = filler(Foo::new).list(ints().range(1, 10)).get();
        final int totalElements = ints().lowerBound(elements.size()).get();

        final Page<Foo> page = spy(new Page<>(pageRequest, elements, totalElements));

        doReturn(false).when(page).hasNext();
        assertThat(page.isLast()).isTrue();
        reset(page);

        doReturn(true).when(page).hasNext();
        assertThat(page.isLast()).isFalse();
        reset(page);
    }

    /**
     * {@link Page#hasPrevious()} のテストです。
     */
    @Test
    void testHasPrevious() {
        final List<Foo> elements = filler(Foo::new).list(ints().range(1, 10)).get();
        final int totalElements = ints().lowerBound(elements.size()).get();

        final Page<Foo> page = new Page<>(pageRequest, elements, totalElements);

        doReturn(true).when(pageRequest).hasPrevious();
        assertThat(page.hasPrevious()).isTrue();
        reset(pageRequest);

        doReturn(false).when(pageRequest).hasPrevious();
        assertThat(page.hasPrevious()).isFalse();
        reset(pageRequest);
    }

    /**
     * {@link Page#hasNext()} のテストです。
     */
    @SuppressWarnings("unchecked")
    @Test
    void testHasNext() {
        final List<Foo> elements = filler(Foo::new).list(ints().range(1, 10)).get();
        final int totalElements = ints().lowerBound(elements.size()).get();

        final Page<Foo> page = spy(new Page<>(pageRequest, elements, totalElements));

        final int pageNmber = ints().rangeClosed(1, Integer.MAX_VALUE - 1).get();
        final int totalPages = ints().rangeClosed(pageNmber, Integer.MAX_VALUE).get();

        doReturn(pageNmber).when(page).getNumber();
        doReturn(totalPages).when(page).getTotalPages();
        assertThat(page.hasNext()).isTrue();
        reset(page);

        doReturn(pageNmber).when(page).getNumber();
        doReturn(pageNmber).when(page).getTotalPages();
        assertThat(page.hasNext()).isFalse();
        reset(page);
    }

    /**
     * {@link Page#getPreviousPageRequest()} のテストです。
     */
    @SuppressWarnings("unchecked")
    @Test
    void testGetPreviousPageRequest() {
        final List<Foo> elements = filler(Foo::new).list(ints().range(1, 10)).get();
        final int totalElements = ints().lowerBound(elements.size()).get();

        final Page<Foo> page = spy(new Page<>(pageRequest, elements, totalElements));

        doReturn(true).when(page).hasPrevious();
        assertThat(page.getPreviousPageRequest()).isNotSameAs(pageRequest);
        reset(page);

        doReturn(false).when(page).hasPrevious();
        assertThat(page.getPreviousPageRequest()).isSameAs(pageRequest);
        reset(page);
    }

    /**
     * {@link Page#getNextPageRequest()} のテストです。
     */
    @SuppressWarnings("unchecked")
    @Test
    void testGetNextPageRequest() {
        final List<Foo> elements = filler(Foo::new).list(ints().range(1, 10)).get();
        final int totalElements = ints().lowerBound(elements.size()).get();

        final Page<Foo> page = spy(new Page<>(pageRequest, elements, totalElements));

        doReturn(true).when(page).hasNext();
        assertThat(page.getNextPageRequest()).isNotSameAs(pageRequest);
        reset(page);

        doReturn(false).when(page).hasNext();
        assertThat(page.getNextPageRequest()).isSameAs(pageRequest);
        reset(page);
    }

    /**
     * {@link Page#map(Function)} のテストです。
     */
    @Test
    void testMap() {
        final List<Foo> elements = filler(Foo::new).list(ints().range(1, 10)).get();
        final int totalElements = ints().lowerBound(elements.size()).get();

        final Page<Foo> fooPage = new Page<>(pageRequest, elements, totalElements);

        final IPage<Bar> barPage = fooPage.map(new FooBarMapper());

        assertThat(fooPage.getPageRequest()).isSameAs(barPage.getPageRequest());
        assertThat(fooPage.getNumberOfElements()).isEqualTo(fooPage.getNumberOfElements());
        assertThat(fooPage.getTotalPages()).isEqualTo(fooPage.getTotalPages());

        assertThatNullPointerException().isThrownBy(() -> fooPage.map(null));
    }

    private static class Foo {
    }

    private static class Bar {
    }

    private static class FooBarMapper implements Function<Foo, Bar> {
        @Override
        public Bar apply(@NonNull final Foo t) {
            return new Bar();
        }
    }
}
