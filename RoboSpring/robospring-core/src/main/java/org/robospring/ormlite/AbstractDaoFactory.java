/** 
 * Created on 23.10.2014 
 * 
 * © 2014 Daniel Thommes
 */
package org.robospring.ormlite;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

/**
 * 
 * 
 * @author Daniel Thommes
 */
public abstract class AbstractDaoFactory implements DaoFactory,
		InitializingBean {

	/**
	 * Name of the database file
	 */
	protected String databaseName;

	/**
	 * Version of the database
	 */
	protected int databaseVersion = 1;

	/**
	 * List of entity classes to generate tables for
	 */
	protected List<Class<?>> entityClasses;

	protected List<Object> initialEntities;

	/**
	 * @param entityClasses the entityClasses to set
	 */
	public void setEntityClasses(List<Class<?>> entityClasses) {
		this.entityClasses = entityClasses;
	}

	/**
	 * @param databaseName the databaseName to set
	 */
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	/**
	 * @param initialEntities the initialEntities to set
	 */
	public void setInitialEntities(List<Object> initialEntities) {
		this.initialEntities = initialEntities;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(databaseName, "The databaseName is required.");
		Assert.notNull(entityClasses, "Please set one or more entityClasses");
		Assert.notEmpty(entityClasses, "Please set one or more entityClasses");

	}

	public <T, ID> Dao<T, ID> getDao(Class<T> entityClass) throws SQLException {
		return DaoManager.createDao(getConnectionSource(), entityClass);
	}

	/**
	 * @return
	 */
	protected abstract ConnectionSource getConnectionSource();

}
