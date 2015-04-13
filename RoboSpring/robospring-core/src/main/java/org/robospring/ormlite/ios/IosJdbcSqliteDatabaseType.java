package org.robospring.ormlite.ios;

import com.j256.ormlite.db.BaseSqliteDatabaseType;

/**
 * Sqlite database type information used to create the tables, etc..
 * 
 * @author graywatson
 */
public class IosJdbcSqliteDatabaseType extends BaseSqliteDatabaseType {

	private final static String DATABASE_URL_PORTION = "sqlite";
	private final static String DRIVER_CLASS_NAME = "SQLite.JDBCDriver";
	private final static String DATABASE_NAME = "SQLite";

	public IosJdbcSqliteDatabaseType() {
	}

	public boolean isDatabaseUrlThisType(String url, String dbTypePart) {
		return DATABASE_URL_PORTION.equals(dbTypePart);
	}

	@Override
	protected String getDriverClassName() {
		return DRIVER_CLASS_NAME;
	}

	public String getDatabaseName() {
		return DATABASE_NAME;
	}

	@Override
	public void appendLimitValue(StringBuilder sb, long limit, Long offset) {
		sb.append("LIMIT ");
		if (offset != null) {
			sb.append(offset).append(',');
		}
		sb.append(limit).append(' ');
	}

	@Override
	public boolean isOffsetLimitArgument() {
		return true;
	}

	@Override
	public boolean isNestedSavePointsSupported() {
		return false;
	}

	@Override
	public void appendOffsetValue(StringBuilder sb, long offset) {
		throw new IllegalStateException(
				"Offset is part of the LIMIT in database type " + getClass());
	}
}
