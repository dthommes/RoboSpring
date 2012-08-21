/**
 * Created on 23.02.2012
 *
 * © 2012 Daniel Thommes
 */
package org.robospring.example;

/**
 *
 *
 * @author Daniel Thommes
 */
public class Location {

	private String name;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "" + name;
	}

}
