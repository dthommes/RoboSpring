package org.robospring.example;

import java.net.URL;

import org.robospring.RoboSpring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractXmlApplicationContext;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

public class RoboSpringExampleActivity extends FragmentActivity {

	@InjectView
	private TextView textView;

	@InjectView
	private TextView timeView;

	private TextView locationView;

	@Autowired
	private URL serviceUrl;

	/**
	 * @param locationView
	 *            the locationView to set
	 */
	@InjectView
	public void setLocationView(TextView locationView) {
		this.locationView = locationView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		long startTime = System.currentTimeMillis();

		RoboSpring.autowire(this);

		AbstractXmlApplicationContext ctx = RoboSpring.getContext();
		long duration = System.currentTimeMillis() - startTime;

		Person kelly = ctx.getBean("kelly", Person.class);
		textView.setText(kelly.getName() + " -> " + kelly.getFather().getName());
		timeView.setText("Time: " + duration + " ms");

		// Should have been autowired by annotaiton-config
		Location location = kelly.getLocation();
		locationView.setText(location.getName());
	}

	public void onLoadClicked(View view) {

	}

	public void onAutowireClicked(View view) {
		long startTime = System.currentTimeMillis();
		RoboSpring.autowire(this);

		// setLocation(RoboSpring.getDefaultContext().getBean(Location.class));

		long duration = System.currentTimeMillis() - startTime;
		timeView.setText("Time: " + duration + " ms");
	}
}