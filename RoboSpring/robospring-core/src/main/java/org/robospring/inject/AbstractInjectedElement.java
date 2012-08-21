/**
* Created on 28.02.2012
*
* © 2012 Daniel Thommes
*/
package org.robospring.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;

import org.springframework.beans.PropertyValues;

public abstract class AbstractInjectedElement {

	protected final Member member;

	protected final boolean isField;

	protected volatile Boolean skip;

	private final Annotation annotation;

	protected volatile boolean cached = false;

	/**
	 * @return the cached
	 */
	public boolean isCached() {
		return cached;
	}

	/**
	 * @param cached the cached to set
	 */
	public void setCached(boolean cached) {
		this.cached = cached;
	}

	protected volatile Object cachedValue;

	private final boolean required;

	/**
	 * @return the cachedValue
	 */
	public Object getCachedValue() {
		return cachedValue;
	}

	/**
	 * @param cachedValue the cachedValue to set
	 */
	public void setCachedValue(Object cachedValue) {
		this.cachedValue = cachedValue;
	}

	/**
	 * @return the annotation
	 */
	public Annotation getAnnotation() {
		return annotation;
	}

	protected AbstractInjectedElement(Member member, Annotation annotation, boolean required) {
		this.member = member;
		this.annotation = annotation;
		this.required = required;
		this.isField = (member instanceof Field);
	}

	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	public final Member getMember() {
		return this.member;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AbstractInjectedElement)) {
			return false;
		}
		AbstractInjectedElement otherElement = (AbstractInjectedElement) other;
		return this.member.equals(otherElement.member);
	}

	@Override
	public int hashCode() {
		return this.member.getClass().hashCode() * 29 + this.member.getName().hashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " for " + this.member;
	}

	/**
	 * @param bean
	 * @param beanName
	 * @param pvs
	 * @throws Throwable
	 */
	abstract protected void inject(Object bean, String beanName, PropertyValues pvs)
			throws Throwable;
}