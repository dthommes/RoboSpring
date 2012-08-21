/**
 * Created on 28.02.2012
 *
 * © 2012 Daniel Thommes
 */
package org.robospring.inject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.util.ReflectionUtils;

import android.content.Context;

/**
 *
 *
 * @author Daniel Thommes
 */
public class InjectUtils {

	private static final Log log = LogFactory.getLog(InjectUtils.class);

	/**
	 * @param method
	 * @return
	 */
	public static String getPropertyNameFromSetter(Method method) {
		return method.getName().substring(3, 4).toLowerCase()
				+ method.getName().substring(4);
	}

	/**
	 * @param setter
	 * @return
	 */
	public static Class<?> getPropertyTypeFromSetter(Method setter) {
		Class<?>[] paramTypes = setter.getParameterTypes();
		if (paramTypes.length > 1 || paramTypes.length < 1) {
			throw new BeanCreationException(
					"Could not find setter type on method '"
							+ setter.getName()
							+ "' - only setters with one parameter are supported.");
		}
		return paramTypes[0];
	}

	/**
	 * @param setter
	 * @param target
	 * @param value
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void invokeSetterWithValue(Method setter, Object target,
			Object value) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		ReflectionUtils.makeAccessible(setter);
		if (value instanceof Object[]) {
			// The varargs stuff will only work with explicit casting to an
			// Object array
			setter.invoke(target, (Object[]) value);
		}
		else {
			setter.invoke(target, value);
		}
	}

	/**
	 * @param field
	 * @param target
	 * @param value
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setFieldWithValue(Field field, Object target,
			Object value) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		ReflectionUtils.makeAccessible(field);
		field.set(target, value);
	}

	/**
	 * @param propertyName
	 * @param context
	 * @param type
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static int getResourceRefFromName(String propertyName,
			Context context, AndroidResourceType type) {
		// TODO
		// context.getResources().getIdentifier(name, defType, defPackage)
		int ref;
		String packageName = context.getPackageName();
		Class<?> idClass;
		try {
			idClass = Class.forName(packageName + ".R$" + type.toString());
			Field idField = idClass.getField(propertyName);
			ref = idField.getInt(idClass);
		}
		catch (Exception e) {
			throw new RuntimeException(
					"Error when trying to resolve resource reference "
							+ type.toString() + "." + propertyName + ": ", e);
		}
		return ref;
	}

}
