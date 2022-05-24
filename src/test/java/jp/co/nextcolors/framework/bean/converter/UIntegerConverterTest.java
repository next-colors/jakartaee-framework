package jp.co.nextcolors.framework.bean.converter;

import static net.andreinc.mockneat.unit.types.Ints.ints;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.stream.Stream;

import org.jooq.types.UInteger;
import org.junit.jupiter.api.Test;

/**
 * {@link UIntegerConverter} のテストです。
 *
 * @author hamana
 */
class UIntegerConverterTest {
    private final UIntegerConverter converter = new UIntegerConverter();

    /**
     * {@link UIntegerConverter#getUnsignedValue(Integer)} のテストです。
     */
    @Test
    void testGetUnsignedValue() {
        final int unsignedNumber = ints().bound(Integer.MAX_VALUE).get();

        assertThat(converter.getUnsignedValue(unsignedNumber)).isEqualTo(UInteger.valueOf(unsignedNumber));

        Stream.of(-1, Integer.MIN_VALUE).forEach(number ->
                assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> converter.getUnsignedValue(number))
        );

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> converter.getUnsignedValue(null));
    }
}
