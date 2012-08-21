/**
 * Created on 28.02.2012
 *
 * © 2012 Daniel Thommes
 */
package org.robospring.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.robospring.inject.support.ExtraInjector;
import org.robospring.inject.support.SpringContextInjector;
import org.robospring.inject.support.ViewInjector;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

/**
 * Class representing injection information about an annotated method.
 */
public class InjectedMethodElement extends AbstractInjectedElement {

	/**
	 *
	 */
	private final RoboSpringInjector roboSpringInjector;


	private volatile Object[] cachedMethodArguments;


	public InjectedMethodElement(RoboSpringInjector roboSpringInjector,
			Method method, boolean required, Annotation annotation) {
		super(method, annotation, required);
		this.roboSpringInjector = roboSpringInjector;
	}

	/**
	 * @return
	 */
	public Method getMethod() {
		return (Method) member;
	}

	/**
	 * @return the cachedMethodArguments
	 */
	public Object[] getCachedMethodArguments() {
		return cachedMethodArguments;
	}

	/**
	 * @param cachedMethodArguments the cachedMethodArguments to set
	 */
	public void setCachedMethodArguments(Object[] cachedMethodArguments) {
		this.cachedMethodArguments = cachedMethodArguments;
	}

	@Override
	protected void inject(Object bean, String beanName, PropertyValues pvs)
			throws Throwable {
		Annotation annotation = getAnnotation();
		if (annotation instanceof Autowired || annotation instanceof Value) {
			injectAutowired(bean, beanName, pvs);
		}
		else if (annotation instanceof InjectView) {
			injectView(bean, (InjectView) annotation);
		}
		else if (annotation instanceof InjectExtra) {
			injectExtra(bean, (InjectExtra) annotation);
		}
	}

	private void injectView(Object bean, InjectView injectViewAnnotation) {
		new ViewInjector().injectMethod(bean, this);
	}

	private void injectExtra(Object bean, InjectExtra injectExtraAnnotation) {
		new ExtraInjector().injectMethod(bean, this);
	}

	private void injectAutowired(Object bean, String beanName,
			PropertyValues pvs) throws Throwable {
		new SpringContextInjector(roboSpringInjector.beanFactory).injectMethod(
				bean, this);
	}
}