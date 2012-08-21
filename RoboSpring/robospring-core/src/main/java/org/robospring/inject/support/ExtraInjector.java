/**
 * Created on 28.02.2012
 *
 * © 2012 Daniel Thommes
 */
package org.robospring.inject.support;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.robospring.inject.InjectUtils;
import org.robospring.inject.InjectedFieldElement;
import org.robospring.inject.InjectedMethodElement;
import org.robospring.inject.Injector;
import org.springframework.beans.factory.BeanCreationException;

import roboguice.inject.InjectExtra;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

/**
 *
 *
 * @author Daniel Thommes
 */
public class ExtraInjector implements Injector {

	/**
	 * {@inheritDoc}
	 * @see org.robospring.inject.Injector#injectField(java.lang.Object,
	 * org.robospring.inject.InjectedFieldElement)
	 */
	public void injectField(Object target, InjectedFieldElement fieldElement) {
		Field field = fieldElement.getField();
		try {
			Class<?> fieldType = field.getType();
			InjectExtra annotation = (InjectExtra) fieldElement.getAnnotation();
			String extraName = !annotation.value().equals("") ? annotation.value()
					: field.getName();
			Object extraValue = getExtraValue(target, extraName, fieldType,
					annotation.optional());
			InjectUtils.setFieldWithValue(field, target, extraValue);
		}
		catch (Throwable ex) {
			throw new BeanCreationException("Could not inject field: " + field,
					ex);
		}

	}

	/**
	 * {@inheritDoc}
	 * @see org.robospring.inject.Injector#injectMethod(java.lang.Object,
	 * org.robospring.inject.InjectedMethodElement)
	 */
	public void injectMethod(Object target, InjectedMethodElement methodElement) {
		Method method = methodElement.getMethod();
		try {
			InjectExtra annotation = (InjectExtra) methodElement.getAnnotation();
			String extraName = !annotation.value().equals("") ? annotation.value()
					: InjectUtils.getPropertyNameFromSetter(method);
			Class<?> propertyType = InjectUtils
					.getPropertyTypeFromSetter(method);
			Object extraValue = getExtraValue(target, extraName, propertyType,
					annotation.optional());
			InjectUtils.invokeSetterWithValue(method, target, extraValue);
		}
		catch (Throwable ex) {
			throw new BeanCreationException("Could not inject extra on method "
					+ method.getName(), ex);
		}

	}

	/**
	 * @param target
	 * @param extraName
	 * @param fieldType
	 * @param optional
	 * @return
	 */
	private static Object getExtraValue(Object target, String extraName,
			Class<?> fieldType, boolean optional) {
		Intent intent = null;
		// Retrieve the intent from one of Android's targets
		if (target instanceof Activity) {
			Activity activity = (Activity) target;
			intent = activity.getIntent();
		}
		else {
			throw new BeanCreationException(
					"Could not inject value - target object is not an Activity.");
		}
		if (!intent.hasExtra(extraName)) {
			if (optional) {
				return null;
			}
			else {
				throw new BeanCreationException("Could not inject extra '"
						+ extraName + "' - the extra could not be found "
						+ "but is not optional.");
			}
		}
		if (String.class.isAssignableFrom(fieldType)) {
			return intent.getStringExtra(extraName);
		}
		else if (int.class.isAssignableFrom(fieldType)
				|| Integer.class.isAssignableFrom(fieldType)) {
			return intent.getIntExtra(extraName, 0);
		}
		else if (long.class.isAssignableFrom(fieldType)
				|| Long.class.isAssignableFrom(fieldType)) {
			return intent.getLongExtra(extraName, 0);
		}
		else if (boolean.class.isAssignableFrom(fieldType)
				|| Boolean.class.isAssignableFrom(fieldType)) {
			return intent.getBooleanExtra(extraName, false);
		}
		else if (Bundle.class.isAssignableFrom(fieldType)) {
			return intent.getBundleExtra(extraName);
		}
		else if (byte.class.isAssignableFrom(fieldType)
				|| Byte.class.isAssignableFrom(fieldType)) {
			return intent.getByteExtra(extraName, (byte) 0);
		}
		else if (char.class.isAssignableFrom(fieldType)
				|| Character.class.isAssignableFrom(fieldType)) {
			return intent.getCharExtra(extraName, ' ');
		}
		else if (double.class.isAssignableFrom(fieldType)
				|| Double.class.isAssignableFrom(fieldType)) {
			return intent.getDoubleExtra(extraName, 0.0);
		}
		// #####
		else if (float.class.isAssignableFrom(fieldType)
				|| Float.class.isAssignableFrom(fieldType)) {
			return intent.getFloatExtra(extraName, 0.0f);
		}
		else if (Parcelable.class.isAssignableFrom(fieldType)) {
			return intent.getParcelableExtra(extraName);
		}
		else if (short.class.isAssignableFrom(fieldType)
				|| Short.class.isAssignableFrom(fieldType)) {
			return intent.getShortExtra(extraName, (short) 0);
		}
		// After String
		else if (CharSequence.class.isAssignableFrom(fieldType)) {
			return intent.getCharSequenceExtra(extraName);
		}

		else if (String[].class.isAssignableFrom(fieldType)) {
			return intent.getStringArrayExtra(extraName);
		}
		else if (boolean[].class.isAssignableFrom(fieldType)
				|| Boolean[].class.isAssignableFrom(fieldType)) {
			return intent.getBooleanArrayExtra(extraName);
		}
		else if (long[].class.isAssignableFrom(fieldType)
				|| Long[].class.isAssignableFrom(fieldType)) {
			return intent.getLongArrayExtra(extraName);
		}
		else if (int[].class.isAssignableFrom(fieldType)
				|| Integer[].class.isAssignableFrom(fieldType)) {
			return intent.getIntArrayExtra(extraName);
		}
		else if (char[].class.isAssignableFrom(fieldType)
				|| Character[].class.isAssignableFrom(fieldType)) {
			return intent.getCharArrayExtra(extraName);
		}
		else if (byte[].class.isAssignableFrom(fieldType)
				|| Byte[].class.isAssignableFrom(fieldType)) {
			return intent.getByteArrayExtra(extraName);
		}
		else if (short[].class.isAssignableFrom(fieldType)
				|| Short[].class.isAssignableFrom(fieldType)) {
			return intent.getShortArrayExtra(extraName);
		}
		else if (double[].class.isAssignableFrom(fieldType)
				|| Double[].class.isAssignableFrom(fieldType)) {
			return intent.getDoubleArrayExtra(extraName);
		}
		else if (float[].class.isAssignableFrom(fieldType)
				|| Float[].class.isAssignableFrom(fieldType)) {
			return intent.getFloatArrayExtra(extraName);
		}
		else if (Parcelable[].class.isAssignableFrom(fieldType)) {
			return intent.getParcelableArrayExtra(extraName);
		}
		else if (List.class.isAssignableFrom(fieldType)) {
			// TODO - type safety - does this throw an
			// exception? I think not, only later when the list
			// is used there can be an exception if the items
			// are no strings
			return intent.getStringArrayListExtra(extraName);
		}
		// Last!!
		else if (Serializable.class.isAssignableFrom(fieldType)) {
			return intent.getSerializableExtra(extraName);
		}
		else {
			throw new BeanCreationException("Could not extract extra '"
					+ extraName + "' from intent. The target type '"
					+ fieldType.getName()
					+ "' cannot be contained in an extra bundle.");
		}

	}

}
