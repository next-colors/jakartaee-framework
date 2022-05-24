package jp.co.nextcolors.framework.bean.converter;

import static net.andreinc.mockneat.unit.types.Ints.ints;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.jooq.types.UNumber;
import org.junit.jupiter.api.Test;

import lombok.SneakyThrows;

/**
 * {@link UnsignedNumberConverter} のテストです。
 *
 * @author hamana
 */
class UnsignedNumberConverterTest {
    private final UNumberConverter converter = spy(new UNumberConverter());

    /**
     * {@link UnsignedNumberConverter#convertToType(Class, Object)} のテストです。
     */
    @SneakyThrows(Throwable.class)
    @Test
    void testConvertToType() {
        final Integer number = ints().get();

        converter.convertToType(Number.class, number);
        verify(converter, times(1)).getUnsignedValue(number);
        clearInvocations(converter);

        converter.convertToType(Number.class, number.toString());
        verify(converter, times(1)).getUnsignedValue(number);
        clearInvocations(converter);
    }

    /**
     * {@link UnsignedNumberConverter#getDefaultType()} のテストです。
     */
    @Test
    void testGetDefaultType() {
        assertThat(converter.getDefaultType()).isEqualTo(UNumber.class);
    }

    private static class UNumberConverter extends UnsignedNumberConverter<UNumber, Integer> {

        @Override
        protected UNumber getUnsignedValue(Integer signedValue) throws NumberFormatException {
            return null;
        }
    }
}
