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

import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.math.DoubleMath;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * {@link IPage} の実装クラスです。
 *
 * @param <T> ページに含まれる要素の型です。
 * @author hamana
 */
@Getter
@ToString
@EqualsAndHashCode
public class Page<T> implements IPage<T> {
    /**
     * ページ付けの情報です。
     */
    private final IPageRequest pageRequest;

    /**
     * ページに含まれる要素です。
     */
    private final List<T> elements;

    /**
     * 要素の総数です。
     */
    private final int totalElements;

    /**
     * @param pageRequest   ページ付けの情報
     * @param elements      ページに含まれる要素
     * @param totalElements 要素の総数
     */
    public Page(@NonNull final IPageRequest pageRequest, @NonNull final List<T> elements, final int totalElements) {
        this.pageRequest = pageRequest;
        this.elements = List.copyOf(elements);
        this.totalElements = totalElements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumber() {
        return pageRequest.getPageNumber();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return pageRequest.getPageSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfElements() {
        return CollectionUtils.size(elements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTotalPages() {
        return DoubleMath.roundToInt((double) totalElements / getSize(), RoundingMode.CEILING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasElements() {
        return CollectionUtils.isNotEmpty(elements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFirst() {
        return !hasPrevious();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLast() {
        return !hasNext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPrevious() {
        return pageRequest.hasPrevious();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return getNumber() < getTotalPages();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPageRequest getPreviousPageRequest() {
        if (hasPrevious()) {
            return pageRequest.previous();
        }

        return pageRequest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPageRequest getNextPageRequest() {
        if (hasNext()) {
            return pageRequest.next();
        }

        return pageRequest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R> IPage<R> map(@NonNull final Function<? super T, ? extends R> mapper) {
        List<R> elements = this.elements.stream().map(mapper::apply).collect(Collectors.toList());

        return new Page<>(pageRequest, elements, totalElements);
    }
}
