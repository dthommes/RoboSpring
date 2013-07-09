package org.robospring.example;

import java.net.URL;
import java.util.Date;

import org.robospring.RoboSpring;
import org.robospring.example.aop.BusinessLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractXmlApplicationContext;

import roboguice.inject.InjectView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RoboSpringExampleActivity extends Activity {

	@InjectView
	private TextView textView;

	@InjectView
	private TextView timeView;

	private TextView locationView;

	@InjectView
	private TextView businessLogicOutputView;

	@Autowired
	private Location location;

	@Autowired
	private BusinessLogic businessLogic;

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
	}

	public void onLoadClicked(View view) {

	}

	public void onAutowireClicked(View view) {
		long startTime = System.currentTimeMillis();
		RoboSpring.autowire(this);

		// setLocation(RoboSpring.getDefaultContext().getBean(Location.class));

		long duration = System.currentTimeMillis() - startTime;
		locationView.setText("" + location);
		timeView.setText("Time: " + duration + " ms");
	}

	public void onStartAop(View view) {
		Date time = businessLogic.doBusiness(this);
		businessLogicOutputView.setText(time.toLocaleString());
	}
}