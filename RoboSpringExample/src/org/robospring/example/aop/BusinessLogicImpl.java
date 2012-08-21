/** 
* Created on 14.08.2012 
* 
* © 2012 Daniel Thommes
*/
package org.robospring.example.aop;

/** 
 * 
 *
 * @author Daniel Thommes
 */
public class BusinessLogicImpl implements BusinessLogic {

	/** 
	 * {@inheritDoc} 
	 * @see org.robospring.example.aop.BusinessLogic#doBusiness(int)
	 */
	@Override
	public int doBusiness(int data) {
		return data * 42;
	}

}
