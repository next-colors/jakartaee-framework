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
package jp.co.nextcolors.framework.bean.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import jp.co.nextcolors.framework.bean.annotation.BeanConverter;

/**
 * {@link BeanConverterUtil} のテストです。
 *
 * @author hamana
 */
@ExtendWith(MockitoExtension.class)
class BeanConverterUtilTest {
    /**
     * {@link BeanConverterUtil#registerConverters()} のテストです。
     */
    @Test
    void testRegisterConverters() {
        try (final MockedStatic<ConvertUtils> convertUtils = mockStatic(ConvertUtils.class)) {
            BeanConverterUtil.registerConverters();

            try (final ScanResult scanResult = new ClassGraph().enableAllInfo().scan()) {
                final List<Class<Converter>> converterClasses = scanResult.getClassesImplementing(Converter.class)
                        .filter(converterClassInfo -> converterClassInfo.hasAnnotation(BeanConverter.class))
                        .loadClasses(Converter.class);

                convertUtils.verify(() -> ConvertUtils.register(any(), any()), times(converterClasses.size()));
            }
        }
    }
}
