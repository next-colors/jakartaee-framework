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
package jp.co.nextcolors.framework.jdbc.record.mapper;

import static net.andreinc.mockneat.unit.text.Strings.strings;
import static net.andreinc.mockneat.unit.types.Ints.ints;
import static net.andreinc.mockneat.unit.types.Longs.longs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.jooq.Configuration;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.jooq.impl.TableRecordImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * {@link BeanRecordMapper} のテストです。
 *
 * @author hamana
 */
@ExtendWith(MockitoExtension.class)
class BeanRecordMapperTest {
    @Mock
    private Configuration configuration;

    /**
     * {@link BeanRecordMapper#map(Record)} のテストです。
     */
    @Test
    void testMap() {
        final FooRecord record = new FooRecord();
        record.setBaz(strings().get());
        record.setQux(ints().get());
        record.setQuux(longs().get());

        final BeanRecordMapper<FooRecord, Foo> fooMapper = new BeanRecordMapper<>(Foo.class, configuration);

        final Foo bean = fooMapper.map(record);
        assertThat(bean.getBaz()).isEqualTo(record.getBaz());
        assertThat(bean.getQux()).isEqualTo(record.getQux());
        assertThat(bean.getQuux()).isNotEqualTo(record.getQuux());

        // null
        assertThat(fooMapper.map(null)).isNull();

        final BeanRecordMapper<FooRecord, Bar> barMapper = new BeanRecordMapper<>(Bar.class, configuration);

        assertThatExceptionOfType(ReflectiveOperationException.class).isThrownBy(() -> barMapper.map(record));
    }

    @SuppressWarnings("serial")
    private static class FooRecord extends TableRecordImpl<FooRecord> {
        private FooRecord() {
            super(new FooTable());
        }

        public String getBaz() {
            return (String) get(0);
        }

        public void setBaz(final String value) {
            set(0, value);
        }

        public Integer getQux() {
            return (Integer) get(1);
        }

        public void setQux(final Integer value) {
            set(1, value);
        }

        public Long getQuux() {
            return (Long) get(2);
        }

        public void setQuux(final Long value) {
            set(2, value);
        }
    }

    @SuppressWarnings({"serial", "unused"})
    private static class FooTable extends TableImpl<FooRecord> {
        private final TableField<FooRecord, String> BAZ = createField(DSL.name("baz"), SQLDataType.CHAR, this);

        private final TableField<FooRecord, Integer> QUX = createField(DSL.name("qux"), SQLDataType.INTEGER, this);

        private final TableField<FooRecord, Long> QUUX = createField(DSL.name("quux"), SQLDataType.BIGINT, this);

        private FooTable() {
            super(DSL.name(FooTable.class.getSimpleName()));
        }
    }

    @Getter
    @Setter
    public static class Foo {
        private String baz;

        private Integer qux;

        @Setter(AccessLevel.NONE)
        private Long quux;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Bar {
        private String baz;

        private Integer qux;

        @Setter(AccessLevel.NONE)
        private Long quux;
    }
}
