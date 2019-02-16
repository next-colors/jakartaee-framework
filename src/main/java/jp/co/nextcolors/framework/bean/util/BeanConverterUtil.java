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

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.jooq.lambda.Unchecked;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import lombok.experimental.UtilityClass;

import jp.co.nextcolors.framework.bean.annotation.BeanConverter;

/**
 * Bean（JavaBeans）のコンバータに関するユーティリティです。
 *
 * @author hamana
 */
@UtilityClass
public class BeanConverterUtil
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * {@link ConvertUtilsBean} にコンバータを登録します。
	 *
	 * @see ConvertUtils#register(Converter, Class)
	 */
	public static void registerConverters()
	{
		try ( ScanResult scanResult = new ClassGraph().enableAllInfo().scan() ) {
			ClassInfoList converters = scanResult.getClassesImplementing( Converter.class.getName() );

			converters.filter( converter -> converter.hasAnnotation( BeanConverter.class.getName() ) )
						.loadClasses( Converter.class ).forEach( Unchecked.consumer( converterClass -> {
				Converter converter = ConstructorUtils.invokeConstructor( converterClass );
				Class<?> targetClass = converterClass.getAnnotation( BeanConverter.class ).forClass();

				ConvertUtils.register( converter, targetClass );
			} ) );
		}
	}
}
