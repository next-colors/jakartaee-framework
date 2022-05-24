package jp.co.nextcolors.framework.bean.converter;

import static net.andreinc.mockneat.unit.types.Ints.ints;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.stream.Stream;

import org.jooq.types.UByte;
import org.junit.jupiter.api.Test;

/**
 * {@link UByteConverter} のテストです。
 *
 * @author hamana
 */
class UByteConverterTest {
    private final UByteConverter converter = new UByteConverter();

    /**
     * {@link UByteConverter#getUnsignedValue(Byte)} のテストです。
     */
    @Test
    void testGetUnsignedValue() {
        final byte unsignedNumber = ints().bound(Byte.valueOf(Byte.MAX_VALUE).intValue()).get().byteValue();

        assertThat(converter.getUnsignedValue(unsignedNumber)).isEqualTo(UByte.valueOf(unsignedNumber));

        Stream.of(-1, Byte.MIN_VALUE).forEach(number ->
                assertThatExceptionOfType(NumberFormatException.class).isThrownBy(() -> converter.getUnsignedValue(number.byteValue()))
        );

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> converter.getUnsignedValue(null));
    }
}
