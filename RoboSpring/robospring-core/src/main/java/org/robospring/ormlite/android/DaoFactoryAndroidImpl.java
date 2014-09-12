/** 
 * Created on 01.08.2014 
 * 
 * © 2014 Daniel Thommes
 */
package org.robospring.ormlite.android;

import java.sql.SQLException;
import java.util.List;

import org.robospring.ormlite.DaoFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * RoboSpring configurable factory to create OrmLite DAOs for given Entity
 * classes
 * 
 * @author Daniel Thommes
 */
public class DaoFactoryAndroidImpl implements DaoFactory, InitializingBean,
		ApplicationContextAware {

	/**
	 * Name of the database file
	 */
	private String databaseName;

	/**
	 * Version of the database
	 */
	private int databaseVersion = 1;

	/**
	 * List of entity classes to generate tables for
	 */
	private List<Class<?>> entityClasses;

	private List<Object> initialEntities;

	/**
	 * OrmLiteSqliteOpenHelper for later use in DAOs
	 */
	private DatabaseHelper helper;

	private Context context;

	/**
	 * @param entityClasses the entityClasses to set
	 */
	public void setEntityClasses(List<Class<?>> entityClasses) {
		this.entityClasses = entityClasses;
	}

	/**
	 * 
	 * 
	 * @author Daniel Thommes
	 */
	public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

		DatabaseHelper(Context context, String databaseName, int databaseVersion) {
			super(context, databaseName, null, databaseVersion);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void onCreate(SQLiteDatabase database,
				ConnectionSource connectionSource) {
			try {
				/*************************************************************
				 * Creating Tables
				 *************************************************************/
				for (Class<?> entityClass : entityClasses) {
					TableUtils.createTable(connectionSource, entityClass);

				}
				/*************************************************************
				 * Creating the initial Entities in the database
				 *************************************************************/
				if (initialEntities != null) {
					for (Object entity : initialEntities) {
						Dao dao = getDao(entity.getClass());
						if (dao != null) {
							dao.create(entity);
						}
					}
				}
			}
			catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

		public void onUpgrade(SQLiteDatabase database,
				ConnectionSource connectionSource, int oldVersion,
				int newVersion) {
			for (Class<?> entityClass : entityClasses) {
				try {
					TableUtils.dropTable(connectionSource, entityClass, true);
				}
				catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
			onCreate(database, connectionSource);
		}
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
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext.getBean(Context.class);
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
		Assert.notNull(context,
				"An Android context is not contained in your Spring ApplicationContext"
						+ " - aren't you running RoboSpring on Android?");

		helper = new DatabaseHelper(context, databaseName, databaseVersion);

	}

	public <T, ID> Dao<T, ID> getDao(Class<T> entityClass) throws SQLException {
		return DaoManager.createDao(helper.getConnectionSource(), entityClass);
	}
}
