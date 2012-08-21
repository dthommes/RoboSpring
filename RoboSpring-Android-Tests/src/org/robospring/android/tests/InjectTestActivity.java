/**
 * Created on 26.02.2012
 *
 * © 2012 Daniel Thommes
 */
package org.robospring.android.tests;

import org.robospring.RoboSpring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

/**
 *
 *
 * @author Daniel Thommes
 */
public class InjectTestActivity extends Activity {

	@InjectView
	TextView textView;

	@InjectView(R.id.myButton)
	Button button;

	TextView textView2;

	@InjectView
	void setTextView(TextView view) {
		textView2 = view;
	}

	Button button2;

	@InjectView(R.id.myButton)
	void setButton2(Button view) {
		button2 = view;
	}

	@Autowired
	InjectedBean injectedBean;

	InjectedBean injectedBean2;

	@Autowired
	public void setInjectedBean2(InjectedBean injectedBean2) {
		this.injectedBean2 = injectedBean2;
	}

	@InjectExtra("myTown")
	String town;

	@InjectExtra
	String name;

	@InjectExtra("foo")
	String foovalue;

	@InjectExtra
	String webversion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.injecttest);

		String contextPath = ClassUtils.addResourcePathToPackagePath(
				getClass(), "InjectTest-context.xml");
		RoboSpring.autowire(this, contextPath);

		// textView.setText(injectedBean.getName());
	}

}
