/**
 * Created on 28.02.2012
 *
 * © 2012 Daniel Thommes
 */
package org.robospring.inject.support;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.robospring.inject.InjectUtils;
import org.robospring.inject.InjectedFieldElement;
import org.robospring.inject.InjectedMethodElement;
import org.robospring.inject.Injector;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;

/**
 *
 *
 * @author Daniel Thommes
 */
public class SpringContextInjector implements Injector {

	protected final Log logger = LogFactory.getLog(getClass());

	private ConfigurableListableBeanFactory beanFactory;

	/**
	 * @param beanFactory2
	 */
	public SpringContextInjector(ConfigurableListableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	/**
	 * {@inheritDoc}
	 * @see org.robospring.inject.Injector#injectField(java.lang.Object,
	 * org.robospring.inject.InjectedFieldElement)
	 */
	public void injectField(Object target, InjectedFieldElement fieldElement) {
		Field field = fieldElement.getField();
		try {
			Object value;
			if (fieldElement.isCached()) {
				value = resolvedCachedArgument(fieldElement.getCachedValue());
			}
			else {
				DependencyDescriptor descriptor = new DependencyDescriptor(
						field, fieldElement.isRequired());
				Set<String> autowiredBeanNames = new LinkedHashSet<String>(1);
				TypeConverter typeConverter = beanFactory.getTypeConverter();
				value = beanFactory.resolveDependency(descriptor, null,
						autowiredBeanNames, typeConverter);
				synchronized (fieldElement) {
					if (!fieldElement.isCached()) {
						if (value != null || fieldElement.isRequired()) {
							fieldElement.setCachedValue(descriptor);
							if (autowiredBeanNames.size() == 1) {
								String autowiredBeanName = autowiredBeanNames
										.iterator().next();
								if (beanFactory.containsBean(autowiredBeanName)) {
									if (beanFactory.isTypeMatch(
											autowiredBeanName, field.getType())) {
										fieldElement
												.setCachedValue(new RuntimeBeanReference(
														autowiredBeanName));
									}
								}
							}
						}
						else {
							fieldElement.setCachedValue(null);
						}
						fieldElement.setCached(true);
					}
				}
			}
			if (value != null) {
				InjectUtils.setFieldWithValue(field, target, value);
			}
		}
		catch (Throwable ex) {
			throw new BeanCreationException("Could not autowire field: "
					+ field, ex);
		}

	}

	/**
	 * {@inheritDoc}
	 * @see org.robospring.inject.Injector#injectMethod(java.lang.Object,
	 * org.robospring.inject.InjectedMethodElement)
	 */
	public void injectMethod(Object target, InjectedMethodElement methodElement) {
		Method method = (Method) methodElement.getMethod();
		try {
			Object[] arguments;
			if (methodElement.isCached()) {
				// Shortcut for avoiding synchronization...
				arguments = resolveCachedArguments(methodElement);
			}
			else {
				Class<?>[] paramTypes = method.getParameterTypes();
				arguments = new Object[paramTypes.length];
				DependencyDescriptor[] descriptors = new DependencyDescriptor[paramTypes.length];
				Set<String> autowiredBeanNames = new LinkedHashSet<String>(
						paramTypes.length);
				TypeConverter typeConverter = beanFactory.getTypeConverter();
				for (int i = 0; i < arguments.length; i++) {
					MethodParameter methodParam = new MethodParameter(method, i);
					GenericTypeResolver.resolveParameterType(methodParam,
							target.getClass());
					descriptors[i] = new DependencyDescriptor(methodParam,
							methodElement.isRequired());
					arguments[i] = beanFactory.resolveDependency(
							descriptors[i], null, autowiredBeanNames,
							typeConverter);
					if (arguments[i] == null && !methodElement.isRequired()) {
						arguments = null;
						break;
					}
				}
				synchronized (methodElement) {
					if (!methodElement.isCached()) {
						if (arguments != null) {
							Object[] cachedMethodArguments = new Object[arguments.length];
							for (int i = 0; i < arguments.length; i++) {
								cachedMethodArguments[i] = descriptors[i];
							}
							if (autowiredBeanNames.size() == paramTypes.length) {
								Iterator<String> it = autowiredBeanNames
										.iterator();
								for (int i = 0; i < paramTypes.length; i++) {
									String autowiredBeanName = it.next();
									if (beanFactory
											.containsBean(autowiredBeanName)) {
										if (beanFactory.isTypeMatch(
												autowiredBeanName,
												paramTypes[i])) {
											cachedMethodArguments[i] = new RuntimeBeanReference(
													autowiredBeanName);
										}
									}
								}
							}
							methodElement
									.setCachedMethodArguments(cachedMethodArguments);
						}
						else {
							methodElement.setCachedMethodArguments(null);
						}
						methodElement.setCached(true);
					}
				}
			}
			if (arguments != null) {
				InjectUtils.invokeSetterWithValue(method, target, arguments);
			}
		}
		catch (InvocationTargetException ex) {
			throw new BeanCreationException("Could not autowire method: "
					+ method, ex.getTargetException());
		}
		catch (Throwable ex) {
			throw new BeanCreationException("Could not autowire method: "
					+ method, ex);
		}

	}

	/**
	 * Resolve the specified cached method argument or field value.
	 */
	private Object resolvedCachedArgument(Object cachedArgument) {
		if (cachedArgument instanceof DependencyDescriptor) {
			DependencyDescriptor descriptor = (DependencyDescriptor) cachedArgument;
			TypeConverter typeConverter = beanFactory.getTypeConverter();
			// Bean name was always null from the original
			// AutowiredAnnotationBeanPostProcessor
			return beanFactory.resolveDependency(descriptor, null, null,
					typeConverter);
		}
		else if (cachedArgument instanceof RuntimeBeanReference) {
			return beanFactory.getBean(((RuntimeBeanReference) cachedArgument)
					.getBeanName());
		}
		else {
			return cachedArgument;
		}
	}

	private Object[] resolveCachedArguments(InjectedMethodElement methodElement) {
		Object[] cachedMethodArguments = methodElement
				.getCachedMethodArguments();
		if (cachedMethodArguments == null) {
			return null;
		}
		Object[] arguments = new Object[cachedMethodArguments.length];
		for (int i = 0; i < arguments.length; i++) {
			arguments[i] = resolvedCachedArgument(cachedMethodArguments[i]);
		}
		return arguments;
	}

}
