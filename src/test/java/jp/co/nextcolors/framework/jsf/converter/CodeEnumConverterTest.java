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
package jp.co.nextcolors.framework.jsf.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mockStatic;

import java.util.stream.Stream;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sun.faces.util.MessageFactory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import jp.co.nextcolors.framework.enumeration.type.ICodeEnum;

/**
 * {@link CodeEnumConverter} のテストです。
 *
 * @author hamana
 */
@ExtendWith(MockitoExtension.class)
class CodeEnumConverterTest {
    private FooConverter converter = new FooConverter();

    @Mock
    private FacesContext context;

    @Mock
    private UIComponent component;

    /**
     * {@link CodeEnumConverter#getAsObject(FacesContext, UIComponent, String)} のテストです。
     */
    @Test
    void testGetAsObject() {
        try (MockedStatic<MessageFactory> messageFactory = mockStatic(MessageFactory.class)) {
            messageFactory.when(() -> MessageFactory.getMessage(isA(FacesContext.class), anyString(), any())).thenReturn(new FacesMessage());

            Stream.of(null, StringUtils.EMPTY, StringUtils.SPACE).forEach(value ->
                    assertThat(converter.getAsObject(context, component, value)).isNull()
            );

            Stream.of(Foo.values()).forEach(value ->
                    assertThat(converter.getAsObject(context, component, value.getCode().toString())).isEqualTo(value)
            );

            assertThatExceptionOfType(ConverterException.class).isThrownBy(() -> converter.getAsObject(context, component, String.valueOf(2)));
        }
    }

    /**
     * {@link CodeEnumConverter#getAsString(FacesContext, UIComponent, Enum)} のテストです。
     */
    @Test
    void testGetAsString() {
        assertThat(converter.getAsString(context, component, null)).isEmpty();

        Stream.of(Foo.values()).forEach(value ->
                assertThat(converter.getAsString(context, component, value)).isEqualTo(value.getCode().toString())
        );
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Foo implements ICodeEnum<Foo, Integer> {
        BAR(0),
        BAZ(1);

        @Getter
        @NonNull
        private final Integer code;
    }

    public static class FooConverter extends CodeEnumConverter<Foo, Integer> {
    }
}
