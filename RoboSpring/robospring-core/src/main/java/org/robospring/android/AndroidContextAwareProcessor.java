/**
 * Created on 29.09.2014
 * 
 * © 2014 Daniel Thommes
 */
package org.robospring.android;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import android.content.Context;

/**
 * 
 * 
 * @author Daniel Thommes
 */
public class AndroidContextAwareProcessor implements BeanPostProcessor {

	private Context context;

	public AndroidContextAwareProcessor(Context context) {
		this.context = context;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		if (bean instanceof AndroidContextAware) {
			AndroidContextAware aware = (AndroidContextAware) bean;
			aware.setAndroidContext(context);
		}
		return bean; // you can return any other object as well
	}

	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

}