package jp.co.nextcolors.framework.bean.util;

import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.reflections.Reflections;

import com.google.common.collect.ImmutableSet;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import jp.co.nextcolors.framework.bean.annotation.BeanConverter;
import jp.co.nextcolors.framework.servlet.util.ServletContextUtil;

/**
 * Bean（JavaBeans）のコンバータに関するユーティリティです。
 *
 * @author hamana
 */
@UtilityClass
public class BeanConverterUtil
{
	//-------------------------------------------------------------------------
	//    Private Methods
	//-------------------------------------------------------------------------
	/**
	 * {@link ConvertUtilsBean} に登録するコンバータとコンバータでの変換の対象となるクラスの対応表を返します。
	 *
	 * @param context
	 * 			Servlet コンテキスト
	 * @return {@link ConvertUtilsBean} に登録するコンバータとコンバータでの変換の対象となるクラスの対応表
	 */
	private static Set<Pair<Class<? extends Converter>, Class<?>>> getConversionRelations( @NonNull final ServletContext context )
	{
		ImmutableSet.Builder<Pair<Class<? extends Converter>, Class<?>>> builder = ImmutableSet.builder();

		Reflections reflectionScanner = ServletContextUtil.getClassReflectionScanner( context );

		Set<Class<? extends Converter>> converters = reflectionScanner.getSubTypesOf( Converter.class );

		for ( Class<? extends Converter> converter : converters ) {
			if ( converter.isAnnotationPresent( BeanConverter.class ) ) {
				Class<?> targetClass = converter.getAnnotation( BeanConverter.class ).forClass();

				builder.add( Pair.of( converter, targetClass ) );
			}
		}

		return builder.build();
	}

	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * {@link ConvertUtilsBean} にコンバータを登録します。
	 *
	 * @param context
	 * 			Servlet コンテキスト
	 * @see org.apache.commons.beanutils.ConvertUtils#register(Converter, Class)
	 */
	@SneakyThrows(ReflectiveOperationException.class)
	public static void registerConverters( @NonNull final ServletContext context )
	{
		Set<Pair<Class<? extends Converter>, Class<?>>> relations = getConversionRelations( context );

		for ( Pair<Class<? extends Converter>, Class<?>> relation : relations ) {
			Converter converter = ConstructorUtils.invokeConstructor( relation.getLeft() );
			Class<?> targetClass = relation.getRight();

			ConvertUtils.register( converter, targetClass );
		}
	}
}
