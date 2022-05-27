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
package jp.co.nextcolors.framework.resource;

import static net.andreinc.mockneat.unit.text.Strings.strings;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Locale;
import java.util.ResourceBundle.Control;

import org.junit.jupiter.api.Test;

import net.andreinc.mockneat.unit.types.Longs;

/**
 * {@link PropertyResourceBundleControl} のテストです。
 *
 * @author hamana
 */
class PropertyResourceBundleControlTest {
    /**
     * {@link PropertyResourceBundleControl#PropertyResourceBundleControl(long)} のテストです。
     */
    @Test
    void testConstructor() {
        final long timeToLive = Longs.longs().get();

        final PropertyResourceBundleControl control = new PropertyResourceBundleControl(timeToLive);

        assertThat(control.getTimeToLive(strings().get(), mock(Locale.class))).isEqualTo(timeToLive);
        assertThat(control.getFormats(strings().get())).isEqualTo(Control.FORMAT_PROPERTIES);
    }
}
