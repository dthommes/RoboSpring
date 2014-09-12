/**
 * Created on 28.02.2012
 *
 * © 2012 Daniel Thommes
 */
package org.robospring.inject.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.robospring.inject.InjectUtils;
import org.robospring.inject.InjectedFieldElement;
import org.robospring.inject.InjectedMethodElement;
import org.robospring.inject.Injector;
import org.springframework.beans.factory.BeanCreationException;

import roboguice.inject.InjectView;
import android.app.Activity;
import android.app.Fragment;
import android.view.View;

/**
 *
 *
 * @author Daniel Thommes
 */
public class ViewInjector implements Injector {

	/**
	 * {@inheritDoc}
	 * @see org.robospring.inject.Injector#injectField(java.lang.Object,
	 * org.robospring.inject.InjectedFieldElement)
	 */
	public void injectField(Object target, InjectedFieldElement fieldElement) {
		Field field = fieldElement.getField();
		try {
			View view = getViewFromTarget(target, field.getName(),
					fieldElement.getAnnotation());
			InjectUtils.setFieldWithValue(field, target, view);
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
			String propertyName = InjectUtils.getPropertyNameFromSetter(method);
			View view = getViewFromTarget(target, propertyName,
					methodElement.getAnnotation());
			InjectUtils.invokeSetterWithValue(method, target, view);
		}
		catch (Throwable ex) {
			throw new BeanCreationException("Could not inject on method: "
					+ method.getName(), ex);
		}

	}

	/**
	 * @param target
	 * @param propertyName
	 * @param annotation
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	private View getViewFromTarget(Object target, String propertyName,
			Annotation annotation) throws ClassNotFoundException,
			NoSuchFieldException, IllegalAccessException {
		InjectView injectAnnotation = (InjectView) annotation;
		Activity activity = null;
		if (target instanceof Activity) {
			activity = (Activity) target;
		}
		else {
			throw new BeanCreationException(
					"Could not inject value - target object is not an Activity.");
		}
		int viewId = injectAnnotation.value() != -1 ? injectAnnotation.value()
				: getViewIdByPropertyName(propertyName, activity);
		View view = activity.findViewById(viewId);
		if (view == null) {
			throw new BeanCreationException("Could not inject view '"
					+ propertyName + "' - view with ID '" + viewId
					+ "' could not be found.");
		}
		return view;
	}

	/**
	 * @param propertyName
	 * @param activity
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	private static int getViewIdByPropertyName(String propertyName,
			Activity activity) throws ClassNotFoundException,
			NoSuchFieldException, IllegalAccessException {
		String packageName = activity.getApplicationContext().getPackageName();
		Class<?> idClass = Class.forName(packageName + ".R$id");
		Field idField = idClass.getField(propertyName);
		return idField.getInt(idClass);
	}

}
