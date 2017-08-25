package jp.co.nextcolors.framework.bean.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.beanutils.ConvertUtilsBean;

/**
 * {@link ConvertUtilsBean} に登録するコンバータであることを表すアノテーションです。
 *
 * @author hamana
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanConverter
{
	//-------------------------------------------------------------------------
	//    Public Methods
	//-------------------------------------------------------------------------
	/**
	 * 変換の対象となるクラスです。
	 *
	 * @return 変換の対象となるクラス
	 */
	Class<?> forClass();
}
