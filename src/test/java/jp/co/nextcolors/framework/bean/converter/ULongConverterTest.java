package jp.co.nextcolors.framework.bean.converter;

import static net.andreinc.mockneat.unit.types.Longs.longs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.stream.Stream;

import org.jooq.types.ULong;
import org.junit.jupiter.api.Test;

/**
 * {@link ULongConverter} のテストです。
 *
 * @author hamana
 */
class ULongConverterTest {
    private final ULongConverter converter = new ULongConverter();

    /**
     * {@link ULongConverter#getUnsignedValue(Long)} のテストです。
     */
    @Test
    void testGetDateTime() {
        final long unsignedNumber = longs().bound(Long.MAX_VALUE).get();

        assertThat(converter.getUnsignedValue(unsignedNumber)).isEqualTo(ULong.valueOf(unsignedNumber));

        Stream.of(-1L, Long.MIN_VALUE).forEach(number ->
                assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> converter.getUnsignedValue(number))
        );

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> converter.getUnsignedValue(null));
    }
}
