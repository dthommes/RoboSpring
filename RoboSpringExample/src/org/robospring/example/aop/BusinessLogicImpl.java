/**
 * Created on 14.08.2012
 *
 * © 2012 Daniel Thommes
 */
package org.robospring.example.aop;

import java.util.Date;

import android.content.Context;

/**
 *
 *
 * @author Daniel Thommes
 */
public class BusinessLogicImpl implements BusinessLogic {

	/**
	 * {@inheritDoc}
	 * @return
	 *
	 * @see org.robospring.example.aop.BusinessLogic#doBusiness(int)
	 */
	@Override
	public Date doBusiness(Context context) {
		return new Date();
	}

}
