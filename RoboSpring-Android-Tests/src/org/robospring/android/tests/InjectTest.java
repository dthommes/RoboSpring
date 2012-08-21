/**
 * Created on 26.02.2012
 *
 * © 2012 Daniel Thommes
 */
package org.robospring.android.tests;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

/**
 *
 *
 * @author Daniel Thommes
 */
public class InjectTest extends
		ActivityInstrumentationTestCase2<InjectTestActivity> {

	/**
	 * @param activityClass
	 */
	public InjectTest() {
		super(InjectTestActivity.class);
	}

	/**
	* {@inheritDoc}
	* @see android.test.ActivityInstrumentationTestCase2#setUp()
	*/
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent();
		intent.putExtra("myTown", "Stuttgart");
		intent.putExtra("name", "Daniel");
		intent.putExtra("foo", "bar");
		intent.putExtra("webversion", "2.0");
		setActivityIntent(intent);
	}

	public void testInjectStringExtraIntoFieldByKey() throws Exception {
		assertEquals("Stuttgart", getActivity().town);
	}

	public void testInjectStringExtraIntoFieldByPropertyName() throws Exception {
		assertEquals("Daniel", getActivity().name);
	}

	public void testInjectStringExtraIntoSetterByKey() throws Exception {
		assertEquals("bar", getActivity().foovalue);
	}

	public void testInjectStringExtraIntoSetterByPropertyName() throws Exception {
		assertEquals("2.0", getActivity().webversion);
	}

	public void testInjectViewToFieldWithId() throws Exception {
		assertNotNull("button has not been injected.", getActivity().button);
	}

	public void testInjectViewToFieldByFieldName() throws Exception {
		assertNotNull("textView has not been injected.", getActivity().textView);
	}

	public void testInjectViewToSetterByFieldName() throws Exception {
		assertNotNull("textView2 has not been injected.",
				getActivity().textView2);
	}

	public void testInjectViewToSetterById() throws Exception {
		assertNotNull("button2 has not been injected.", getActivity().button2);
	}

	public void testAutowireToField() throws Exception {
		assertNotNull("injectedBean has not been autowired.",
				getActivity().injectedBean);
		assertNotNull("injectedBean has no name.",
				getActivity().injectedBean.getName());
		assertEquals("Superduper", getActivity().injectedBean.getName());
	}
}
