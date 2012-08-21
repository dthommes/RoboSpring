/*
 * Copyright 2002-2012 the original author or authors.
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

package org.robospring.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.app.Activity;

import com.google.inject.Inject;

/**
 * This injector is able to inject Spring beans, Views from the layout and
 * extras into a Bean or {@link Activity}.
 *
 * @author Daniel Thommes
 */
public class RoboSpringInjector extends
		InstantiationAwareBeanPostProcessorAdapter {

	protected final Log logger = LogFactory.getLog(getClass());

	private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<Class<? extends Annotation>>();

	private String requiredParameterName = "required";

	private boolean requiredParameterValue = true;

	private int order = Ordered.LOWEST_PRECEDENCE - 2;

	ConfigurableListableBeanFactory beanFactory;

	private final Map<Class<?>, Constructor<?>[]> candidateConstructorsCache = new ConcurrentHashMap<Class<?>, Constructor<?>[]>();

	private final Map<Class<?>, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<Class<?>, InjectionMetadata>();

	/**
	 * Create a new AutowiredAnnotationBeanPostProcessor for Spring's standard
	 * {@link Autowired} annotation.
	 * @param roboSpringApplicationContext
	 */
	public RoboSpringInjector(ConfigurableListableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
		this.autowiredAnnotationTypes.add(Autowired.class);
		this.autowiredAnnotationTypes.add(Value.class);
		// TODO modularize Google Guice stuff
		this.autowiredAnnotationTypes.add(InjectView.class);
		this.autowiredAnnotationTypes.add(InjectExtra.class);
		this.autowiredAnnotationTypes.add(Inject.class);
	}

	/**
	 * Set the 'autowired' annotation type, to be used on constructors, fields,
	 * setter methods and arbitrary config methods.
	 * <p>
	 * The default autowired annotation type is the Spring-provided
	 * {@link Autowired} annotation, as well as {@link Value}.
	 * <p>
	 * This setter property exists so that developers can provide their own
	 * (non-Spring-specific) annotation type to indicate that a member is
	 * supposed to be autowired.
	 */
	public void setAutowiredAnnotationType(
			Class<? extends Annotation> autowiredAnnotationType) {
		Assert.notNull(autowiredAnnotationType,
				"'autowiredAnnotationType' must not be null");
		this.autowiredAnnotationTypes.clear();
		this.autowiredAnnotationTypes.add(autowiredAnnotationType);
	}

	/**
	 * Set the 'autowired' annotation types, to be used on constructors, fields,
	 * setter methods and arbitrary config methods.
	 * <p>
	 * The default autowired annotation type is the Spring-provided
	 * {@link Autowired} annotation, as well as {@link Value}.
	 * <p>
	 * This setter property exists so that developers can provide their own
	 * (non-Spring-specific) annotation types to indicate that a member is
	 * supposed to be autowired.
	 */
	public void setAutowiredAnnotationTypes(
			Set<Class<? extends Annotation>> autowiredAnnotationTypes) {
		Assert.notEmpty(autowiredAnnotationTypes,
				"'autowiredAnnotationTypes' must not be empty");
		this.autowiredAnnotationTypes.clear();
		this.autowiredAnnotationTypes.addAll(autowiredAnnotationTypes);
	}

	/**
	 * Set the name of a parameter of the annotation that specifies whether it
	 * is required.
	 * @see #setRequiredParameterValue(boolean)
	 */
	public void setRequiredParameterName(String requiredParameterName) {
		this.requiredParameterName = requiredParameterName;
	}

	/**
	 * Set the boolean value that marks a dependency as required
	 * <p>
	 * For example if using 'required=true' (the default), this value should be
	 * <code>true</code>; but if using 'optional=false', this value should be
	 * <code>false</code>.
	 * @see #setRequiredParameterName(String)
	 */
	public void setRequiredParameterValue(boolean requiredParameterValue) {
		this.requiredParameterValue = requiredParameterValue;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getOrder() {
		return this.order;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
			throw new IllegalArgumentException(
					"AutowiredAnnotationBeanPostProcessor requires a ConfigurableListableBeanFactory");
		}
		this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
	}

	@Override
	public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass,
			String beanName) throws BeansException {
		// Quick check on the concurrent map first, with minimal locking.
		Constructor<?>[] candidateConstructors = this.candidateConstructorsCache
				.get(beanClass);
		if (candidateConstructors == null) {
			synchronized (this.candidateConstructorsCache) {
				candidateConstructors = this.candidateConstructorsCache
						.get(beanClass);
				if (candidateConstructors == null) {
					Constructor<?>[] rawCandidates = beanClass
							.getDeclaredConstructors();
					List<Constructor<?>> candidates = new ArrayList<Constructor<?>>(
							rawCandidates.length);
					Constructor<?> requiredConstructor = null;
					Constructor<?> defaultConstructor = null;
					for (Constructor<?> candidate : rawCandidates) {
						Annotation annotation = findAutowiredAnnotation(candidate);
						if (annotation != null) {
							if (requiredConstructor != null) {
								throw new BeanCreationException(
										"Invalid autowire-marked constructor: "
												+ candidate
												+ ". Found another constructor with 'required' Autowired annotation: "
												+ requiredConstructor);
							}
							if (candidate.getParameterTypes().length == 0) {
								throw new IllegalStateException(
										"Autowired annotation requires at least one argument: "
												+ candidate);
							}
							boolean required = determineRequiredStatus(annotation);
							if (required) {
								if (!candidates.isEmpty()) {
									throw new BeanCreationException(
											"Invalid autowire-marked constructors: "
													+ candidates
													+ ". Found another constructor with 'required' Autowired annotation: "
													+ requiredConstructor);
								}
								requiredConstructor = candidate;
							}
							candidates.add(candidate);
						}
						else if (candidate.getParameterTypes().length == 0) {
							defaultConstructor = candidate;
						}
					}
					if (!candidates.isEmpty()) {
						// Add default constructor to list of optional
						// constructors, as fallback.
						if (requiredConstructor == null
								&& defaultConstructor != null) {
							candidates.add(defaultConstructor);
						}
						candidateConstructors = candidates
								.toArray(new Constructor[candidates.size()]);
					}
					else {
						candidateConstructors = new Constructor[0];
					}
					this.candidateConstructorsCache.put(beanClass,
							candidateConstructors);
				}
			}
		}
		return (candidateConstructors.length > 0 ? candidateConstructors : null);
	}

	/**
	 * 'Native' processing method for direct calls with an arbitrary target
	 * instance, resolving all of its fields and methods which are annotated
	 * with <code>@Autowired</code>.
	 * @param bean the target instance to process
	 * @throws BeansException if autowiring failed
	 */
	public void processInjection(Object bean) throws BeansException {
		Class<?> clazz = bean.getClass();
		InjectionMetadata metadata = findAutowiringMetadata(clazz);
		try {
			Set<AbstractInjectedElement> injectedElements = metadata
					.getInjectedElements();
			if (!injectedElements.isEmpty()) {
				boolean debug = logger.isDebugEnabled();
				for (AbstractInjectedElement element : injectedElements) {
					if (debug) {
						logger.debug("Processing injection: " + element);
					}
					element.inject(bean, null, null);
				}
			}
		}
		catch (Throwable ex) {
			throw new BeanCreationException(
					"Injection of autowired dependencies failed for class ["
							+ clazz + "]", ex);
		}
	}

	private InjectionMetadata findAutowiringMetadata(Class<?> clazz) {
		// Quick check on the concurrent map first, with minimal locking.
		InjectionMetadata metadata = this.injectionMetadataCache.get(clazz);
		if (metadata == null) {
			synchronized (this.injectionMetadataCache) {
				metadata = this.injectionMetadataCache.get(clazz);
				if (metadata == null) {
					metadata = buildAutowiringMetadata(clazz);
					this.injectionMetadataCache.put(clazz, metadata);
				}
			}
		}
		return metadata;
	}

	// TODO remove the class InjectionMetadata and use Set directly instead
	private InjectionMetadata buildAutowiringMetadata(Class<?> clazz) {
		LinkedList<AbstractInjectedElement> elements = new LinkedList<AbstractInjectedElement>();
		Class<?> targetClass = clazz;

		do {
			LinkedList<AbstractInjectedElement> currElements = new LinkedList<AbstractInjectedElement>();
			/*************************************************************
			 * Fields
			 *************************************************************/
			for (Field field : targetClass.getDeclaredFields()) {
				Annotation annotation = findAutowiredAnnotation(field);
				if (annotation != null) {
					if (Modifier.isStatic(field.getModifiers())) {
						if (logger.isWarnEnabled()) {
							logger.warn("Autowired annotation is not supported on static fields: "
									+ field);
						}
						continue;
					}
					boolean required = determineRequiredStatus(annotation);
					currElements.add(new InjectedFieldElement(this, field,
							required, annotation));
				}
			}
			/*************************************************************
			 * Methods
			 *************************************************************/
			for (Method method : targetClass.getDeclaredMethods()) {
				Method bridgedMethod = BridgeMethodResolver
						.findBridgedMethod(method);
				Annotation annotation = BridgeMethodResolver
						.isVisibilityBridgeMethodPair(method, bridgedMethod) ? findAutowiredAnnotation(bridgedMethod)
						: findAutowiredAnnotation(method);
				if (annotation != null
						&& method.equals(ClassUtils.getMostSpecificMethod(
								method, clazz))) {
					if (Modifier.isStatic(method.getModifiers())) {
						if (logger.isWarnEnabled()) {
							logger.warn("Autowired annotation is not supported on static methods: "
									+ method);
						}
						continue;
					}
					if (method.getParameterTypes().length == 0) {
						if (logger.isWarnEnabled()) {
							logger.warn("Autowired annotation should be used on methods with actual parameters: "
									+ method);
						}
					}
					boolean required = determineRequiredStatus(annotation);
					currElements.add(new InjectedMethodElement(this, method,
							required, annotation));
				}
			}
			elements.addAll(0, currElements);
			targetClass = targetClass.getSuperclass();
			// skip android super classes - they won't be annotated
		} while (targetClass != null
				&& !targetClass.getName().startsWith("android.")
				&& targetClass != Object.class);

		return new InjectionMetadata(clazz, elements);
	}

	private Annotation findAutowiredAnnotation(AccessibleObject ao) {
		for (Class<? extends Annotation> type : this.autowiredAnnotationTypes) {
			Annotation annotation = ao.getAnnotation(type);
			if (annotation != null) {
				return annotation;
			}
		}
		return null;
	}

	/**
	 * Determine if the annotated field or method requires its dependency.
	 * <p>
	 * A 'required' dependency means that autowiring should fail when no beans
	 * are found. Otherwise, the autowiring process will simply bypass the field
	 * or method when no beans are found.
	 * @param annotation the Autowired annotation
	 * @return whether the annotation indicates that a dependency is required
	 */
	protected boolean determineRequiredStatus(Annotation annotation) {
		try {
			Method method = ReflectionUtils.findMethod(
					annotation.annotationType(), this.requiredParameterName);
			return (this.requiredParameterValue == (Boolean) ReflectionUtils
					.invokeMethod(method, annotation));
		}
		catch (Exception ex) {
			// required by default
			return true;
		}
	}

	/**
	 * Register the specified bean as dependent on the autowired beans.
	 */
	void registerDependentBeans(String beanName, Set<String> autowiredBeanNames) {
		if (beanName != null) {
			for (String autowiredBeanName : autowiredBeanNames) {
				beanFactory.registerDependentBean(autowiredBeanName, beanName);
				if (logger.isDebugEnabled()) {
					logger.debug("Autowiring by type from bean name '"
							+ beanName + "' to bean named '"
							+ autowiredBeanName + "'");
				}
			}
		}
	}

	/**
	 * Resolve the specified cached method argument or field value.
	 */
	Object resolvedCachedArgument(String beanName, Object cachedArgument) {
		if (cachedArgument instanceof DependencyDescriptor) {
			DependencyDescriptor descriptor = (DependencyDescriptor) cachedArgument;
			TypeConverter typeConverter = beanFactory.getTypeConverter();
			return beanFactory.resolveDependency(descriptor, beanName, null,
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

}
