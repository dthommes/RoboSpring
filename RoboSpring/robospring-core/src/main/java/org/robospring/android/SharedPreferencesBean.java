/**
 * Created on 18.06.2013
 *
 * © 2013 Daniel Thommes
 */
package org.robospring.android;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Makes the shared preferences accessible without an Android context (the
 * latter is injected by RoboSpring before)
 * 
 * @author Daniel Thommes
 * @todo move into Android specific RoboSpring project
 */
public class SharedPreferencesBean implements InitializingBean,
		AndroidContextAware {

	private Context context;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.robospring.android.AndroidContextAware#setAndroidContext(android.content.Context)
	 */
	public void setAndroidContext(Context context) {
		this.context = context;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(context,
				"This bean requires an Android application context as 'context'");
	}

	public SharedPreferences getDefaultSharedPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public void setDefaultValues(int resId, boolean readAgain) {
		PreferenceManager.setDefaultValues(context, resId, readAgain);
	}

	public void setDefaultValues(String sharedPreferencesName,
			int sharedPreferencesMode, int resId, boolean readAgain) {
		PreferenceManager.setDefaultValues(context, sharedPreferencesName,
				sharedPreferencesMode, resId, readAgain);
	}
}
