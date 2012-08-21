/**
 * Created on 28.02.2012
 *
 * © 2012 Daniel Thommes
 */
package org.robospring.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.robospring.inject.support.ExtraInjector;
import org.robospring.inject.support.SpringContextInjector;
import org.robospring.inject.support.ViewInjector;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

/**
 * Class representing injection information about an annotated field.
 */
public class InjectedFieldElement extends AbstractInjectedElement {

	/**
	 *
	 */
	private final RoboSpringInjector roboSpringInjector;

	public InjectedFieldElement(RoboSpringInjector roboSpringInjector,
			Field field, boolean required, Annotation annotation) {
		super(field, annotation, required);
		this.roboSpringInjector = roboSpringInjector;	}

	public Field getField() {
		return (Field) member;
	}

	@Override
	protected void inject(Object bean, String beanName, PropertyValues pvs)
			throws Throwable {
		Annotation annotation = getAnnotation();
		if (annotation instanceof Autowired || annotation instanceof Value) {
			injectAutowired(bean, beanName);
		}
		else if (annotation instanceof InjectView) {
			injectView(bean, (InjectView) annotation);
		}
		else if (annotation instanceof InjectExtra) {
			injectExtra(bean, (InjectExtra) annotation);
		}
	}

	/**
	 * @param bean
	 * @param annotation2
	 */
	private void injectExtra(Object bean, InjectExtra injectExtraAnnotation) {
		new ExtraInjector().injectField(bean, this);
	}

	/**
	 * @param bean
	 * @param injectViewAnnotation
	 */
	private void injectView(Object bean, InjectView injectViewAnnotation) {
		new ViewInjector().injectField(bean, this);
	}

	private void injectAutowired(Object bean, String beanName) {
		new SpringContextInjector(roboSpringInjector.beanFactory).injectField(
				bean, this);
	}
}