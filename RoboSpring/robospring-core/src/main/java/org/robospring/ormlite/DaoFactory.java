/** 
 * Created on 01.08.2014 
 * 
 * © 2014 Daniel Thommes
 */
package org.robospring.ormlite;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;

/**
 * 
 * 
 * @author Daniel Thommes
 */
public interface DaoFactory {

	<T, ID> Dao<T, ID> getDao(Class<T> entityClass) throws SQLException;

}
