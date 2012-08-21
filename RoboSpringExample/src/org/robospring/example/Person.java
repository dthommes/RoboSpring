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
public class Person {

	private Person father;
	private Person mother;
	private String name;

	/**
	 * @return the father
	 */
	public Person getFather() {
		return father;
	}

	/**
	 * @param father
	 *            the father to set
	 */
	public void setFather(Person father) {
		this.father = father;
	}

	/**
	 * @return the mother
	 */
	public Person getMother() {
		return mother;
	}

	/**
	 * @param mother
	 *            the mother to set
	 */
	public void setMother(Person mother) {
		this.mother = mother;
	}

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

}
