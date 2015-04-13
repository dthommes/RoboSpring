/** 
 * Created on 01.08.2014 
 * 
 * © 2014 Daniel Thommes
 */
package org.robospring.ormlite.android;

import java.sql.SQLException;

import org.robospring.ormlite.AbstractDaoFactory;
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
public class DaoFactoryAndroidImpl extends AbstractDaoFactory implements
		ApplicationContextAware {

	/**
	 * OrmLiteSqliteOpenHelper for later use in DAOs
	 */
	private DatabaseHelper helper;

	private Context context;

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
		super.afterPropertiesSet();
		Assert.notNull(context,
				"An Android context is not contained in your Spring ApplicationContext"
						+ " - aren't you running RoboSpring on Android?");
		helper = new DatabaseHelper(context, databaseName, databaseVersion);

	}

	protected ConnectionSource getConnectionSource() {
		return helper.getConnectionSource();
	}
}
