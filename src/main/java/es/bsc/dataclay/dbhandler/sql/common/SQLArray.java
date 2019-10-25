package es.bsc.dataclay.dbhandler.sql.common;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class SQLArray implements Array {
	private static final char DELIMITER = (char)1;
	private final String serializedArray;

	public SQLArray(String baseTypeName, Object[] array) {
		if(!baseTypeName.equals("varchar") && !baseTypeName.equals("uuid")) {
			throw new IllegalArgumentException("Unsupported");
		}

		StringBuffer sb = new StringBuffer(baseTypeName + DELIMITER);
		if(array != null) {
			for(Object o: array) {
				sb.append(o.toString() + DELIMITER);
			}
		}
		serializedArray = sb.toString();
	}

	public SQLArray(String array) {
		if(array == null || array.equals("null")) {
			this.serializedArray = null;
			return;
		}
		this.serializedArray = array;
	}

	@Override
	public String getBaseTypeName() throws SQLException {
		return serializedArray.substring(0, serializedArray.indexOf(DELIMITER));
	}

	private int getSize() {
		return serializedArray.split("" + DELIMITER).length - 1;
	}

	@Override
	public Object getArray() throws SQLException {
		if(serializedArray == null || serializedArray.equals("null")) {
			return null;
		}

		final String type = getBaseTypeName();
		final int size = getSize();

		if(size == 0) {
			return type.equals("varchar") ? new String[0] : new UUID[0];
		}
		if(size == -1) {
			return null;
		}

		int start = serializedArray.indexOf(DELIMITER) + 1;
		int end = serializedArray.lastIndexOf(DELIMITER);

		String[] elements = serializedArray.substring(start, end).split("" + DELIMITER);

		if(type.equals("varchar")) {
			return elements;
		}

		if(type.equals("uuid")) {
			UUID[] array = new UUID[size];
			for(int i = 0; i < elements.length; i++) {
				array[i] = UUID.fromString(elements[i]);
			}
			return array;
		}

		System.out.println("unsupported");;throw new SQLException("Unsupported");
	}

	public String toString() {
		return serializedArray;
	}

	@Override
	public int getBaseType() throws SQLException {
		System.out.println("unsupported");;throw new SQLException("Unsupported");
	}

	@Override
	public Object getArray(Map<String, Class<?>> map) throws SQLException {
		System.out.println("unsupported");;throw new SQLException("Unsupported");
	}

	@Override
	public Object getArray(long index, int count) throws SQLException {
		System.out.println("unsupported");;throw new SQLException("Unsupported");
	}

	@Override
	public Object getArray(long index, int count, Map<String, Class<?>> map) throws SQLException {
		System.out.println("unsupported");;throw new SQLException("Unsupported");
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		System.out.println("unsupported");;throw new SQLException("Unsupported");
	}

	@Override
	public ResultSet getResultSet(Map<String, Class<?>> map) throws SQLException {
		System.out.println("unsupported");;throw new SQLException("Unsupported");
	}

	@Override
	public ResultSet getResultSet(long index, int count) throws SQLException {
		System.out.println("unsupported");;throw new SQLException("Unsupported");
	}

	@Override
	public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) throws SQLException {
		System.out.println("unsupported");;throw new SQLException("Unsupported");
	}

	@Override
	public void free() throws SQLException {
		System.out.println("unsupported");;throw new SQLException("Unsupported");
	}

}
