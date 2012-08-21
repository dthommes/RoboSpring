/*
 * Copyright 2002-2011 the original author or authors.
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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;

/**
 * Internal class for managing injection metadata.
 * Not intended for direct use in applications.
 *
 * <p>Used by {@link AutowiredAnnotationBeanPostProcessor},
 * {@link org.springframework.context.annotation.CommonAnnotationBeanPostProcessor} and
 * {@link org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor}.
 *
 * @author Juergen Hoeller
 * @since 2.5
 */
public class InjectionMetadata {

	private final Log logger = LogFactory.getLog(InjectionMetadata.class);

	private final Set<AbstractInjectedElement> injectedElements;


	public InjectionMetadata(Class targetClass, Collection<AbstractInjectedElement> elements) {
		this.injectedElements = Collections.synchronizedSet(new LinkedHashSet<AbstractInjectedElement>());
		for (AbstractInjectedElement element : elements) {
			if (logger.isDebugEnabled()) {
				logger.debug("Found injected element on class [" + targetClass.getName() + "]: " + element);
			}
			this.injectedElements.add(element);
		}
	}

	/**
	 * @return the injectedElements
	 */
	public Set<AbstractInjectedElement> getInjectedElements() {
		return injectedElements;
	}

}
