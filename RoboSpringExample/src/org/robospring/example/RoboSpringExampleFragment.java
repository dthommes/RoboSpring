/** 
 * Created on 13.04.2015 
 * 
 * © 2015 Daniel Thommes
 */
package org.robospring.example;

import org.robospring.RoboSpring;

import roboguice.inject.InjectView;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 *
 * @author Daniel Thommes
 */
public class RoboSpringExampleFragment extends Fragment {

	@InjectView
	private TextView myTextView;

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_test, container, false);
		return view;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RoboSpring.autowire(this);

		myTextView.setText("Autowiring for Fragments is working!");
	}

}
