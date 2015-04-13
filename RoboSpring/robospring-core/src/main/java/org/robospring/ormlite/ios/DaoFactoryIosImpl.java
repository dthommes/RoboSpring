/** 
 * Created on 23.10.2014 
 * 
 * © 2014 Daniel Thommes
 */
package org.robospring.ormlite.ios;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.robospring.ormlite.AbstractDaoFactory;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Versioning
 * https://stackoverflow.com/questions/8030779/change-sqlite-database-version-number
 * @author Daniel Thommes
 */
public class DaoFactoryIosImpl extends AbstractDaoFactory {

	private static final Log logger = LogFactory
			.getLog(DaoFactoryIosImpl.class);

	private File dbFile;

	private ConnectionSource connectionSource;

	/**
	 * {@inheritDoc}
	 * @see org.robospring.ormlite.AbstractDaoFactory#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		dbFile = new File(System.getenv("HOME"), "Documents/databases/"
				+ databaseName);
		dbFile.getParentFile().mkdirs();

		String databaseUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
		logger.info("Connecting to " + databaseUrl);
		connectionSource = new JdbcConnectionSource(databaseUrl,
				new IosJdbcSqliteDatabaseType());
	}

	/**
	 * {@inheritDoc}
	 * @see org.robospring.ormlite.AbstractDaoFactory#getConnectionSource()
	 */
	@Override
	protected ConnectionSource getConnectionSource() {
		return connectionSource;
	}

}
