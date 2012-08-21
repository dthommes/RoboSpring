/**
 * Created on 28.02.2012
 *
 * © 2012 Daniel Thommes
 */
package org.robospring.inject;



/**
 *
 *
 * @author Daniel Thommes
 */
public interface Injector {

	void injectField(Object target, InjectedFieldElement fieldElement);

	void injectMethod(Object target, InjectedMethodElement methodElement);

}
