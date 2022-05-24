package jp.co.nextcolors.framework.bean.converter;

import static net.andreinc.mockneat.unit.types.Ints.ints;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.stream.Stream;

import org.jooq.types.UShort;
import org.junit.jupiter.api.Test;

/**
 * {@link UShortConverter} のテストです。
 *
 * @author hamana
 */
class UShortConverterTest {
    private final UShortConverter converter = new UShortConverter();

    /**
     * {@link UShortConverter#getUnsignedValue(Short)} のテストです。
     */
    @Test
    void testGetUnsignedValue() {
        final short unsignedNumber = ints().bound(Short.valueOf(Short.MAX_VALUE).intValue()).get().shortValue();

        assertThat(converter.getUnsignedValue(unsignedNumber)).isEqualTo(UShort.valueOf(unsignedNumber));

        Stream.of(-1, Short.MIN_VALUE).forEach(number ->
                assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> converter.getUnsignedValue(number.shortValue()))
        );

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> converter.getUnsignedValue(null));
    }
}
