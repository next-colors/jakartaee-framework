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
package jp.co.nextcolors.framework.bean.converter;

import static net.andreinc.mockneat.unit.time.LocalDates.localDates;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;

import java.time.OffsetDateTime;
import java.time.temporal.Temporal;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.SneakyThrows;

/**
 * {@link JavaTimeConverter} のテストです。
 *
 * @author hamana
 */
@ExtendWith(MockitoExtension.class)
class JavaTimeConverterTest {
    @Spy
    private final TemporalConverter converter = new TemporalConverter();

    /**
     * {@link JavaTimeConverter#convertToType(Class, Object)} のテストです。
     */
    @SneakyThrows(Throwable.class)
    @Test
    void testConvertToType() {
        final Date date = localDates().mapToDate().get();

        converter.convertToType(Temporal.class, date);
        verify(converter).getDateTime(any());
        clearInvocations(converter);

        assertThatNullPointerException().isThrownBy(() -> converter.convertToType(null, date));
        assertThatNullPointerException().isThrownBy(() -> converter.convertToType(Temporal.class, null));
    }

    /**
     * {@link JavaTimeConverter#getDefaultType()} のテストです。
     */
    @Test
    void testGetDefaultType() {
        assertThat(converter.getDefaultType()).isEqualTo(Temporal.class);
    }

    private static class TemporalConverter extends JavaTimeConverter<Temporal> {
        @Override
        protected Temporal getDateTime(OffsetDateTime offsetDateTime) {
            return null;
        }
    }
}
