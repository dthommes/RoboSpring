/** 
 * Created on 18.03.2014 
 * 
 * © 2014 Daniel Thommes
 */
package org.robospring;

import org.springframework.context.support.AbstractXmlApplicationContext;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Loader for asynchronous loading of the ApplicationContext
 * 
 * @author Daniel Thommes
 */
public class RoboSpringLoader extends
		AsyncTaskLoader<AbstractXmlApplicationContext> {

	private String configLocation;

	private Object autowireTarget;

	/**
	 * @param context
	 */
	public RoboSpringLoader(Context context) {
		super(context);
	}

	/**
	 * @param context
	 */
	public RoboSpringLoader(Context context, Object autowireTarget) {
		super(context);
		this.autowireTarget = autowireTarget;
	}

	/**
	 * @param context
	 */
	public RoboSpringLoader(Context context, String configLocation) {
		super(context);
		this.configLocation = configLocation;
	}

	/**
	 * @param context
	 */
	public RoboSpringLoader(Context context, String configLocation,
			Object autowireTarget) {
		super(context);
		this.configLocation = configLocation;
		this.autowireTarget = autowireTarget;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.support.v4.content.AsyncTaskLoader#loadInBackground()
	 */
	public AbstractXmlApplicationContext loadInBackground() {
		Thread.currentThread().setContextClassLoader(
				RoboSpringLoader.class.getClassLoader());
		AbstractXmlApplicationContext context;
		if (configLocation != null) {
			context = RoboSpring.getContext(getContext(), configLocation);
			if (autowireTarget != null) {
				RoboSpring.autowire(autowireTarget, configLocation);
			}
		}
		else {
			context = RoboSpring.getContext(getContext());
			if (autowireTarget != null) {
				RoboSpring.autowire(autowireTarget);
			}
		}
		return context;
	}

}
