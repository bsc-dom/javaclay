package es.bsc.dataclay.dbhandler.sql.common;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLResultSetWrapper implements ResultSet {
	private final ResultSet rs;

	public SQLResultSetWrapper(ResultSet rs) {
		this.rs = rs;
	}

	@Override
	public Object getObject(int col) throws SQLException {
		UUID uuid = null;
		String s = rs.getString(col);
		if (s == null) {
			return null;
		}
		try {
			if (s.length() == 36) {
				uuid = UUID.fromString(s);
			}
		} catch (Exception e) {
			return (Object) s;
		}
		return uuid;
	}

	public java.lang.Object getObject(java.lang.String arg0) throws SQLException {
		return this.getObject(findColumn(arg0));
	}

	public Array getArray(int i) throws SQLException {
		final String s = rs.getString(i);
		if (s == null) {
			return null;
		}
		return new SQLArray(s);
	}

	public Array getArray(String col) throws SQLException {
		return getArray(findColumn(col));
	}

	/* Wrapped calls */
	public java.lang.String getString(java.lang.String arg0) throws SQLException {
		return rs.getString(arg0);
	}

	public java.lang.String getString(int arg0) throws SQLException {
		return rs.getString(arg0);
	}

	public java.sql.Time getTime(int arg0, java.util.Calendar arg1) throws SQLException {
		return rs.getTime(arg0, arg1);
	}

	public java.sql.Time getTime(java.lang.String arg0, java.util.Calendar arg1) throws SQLException {
		return rs.getTime(arg0, arg1);
	}

	public java.sql.Time getTime(java.lang.String arg0) throws SQLException {
		return rs.getTime(arg0);
	}

	public java.sql.Time getTime(int arg0) throws SQLException {
		return rs.getTime(arg0);
	}

	public java.sql.Date getDate(int arg0) throws SQLException {
		return rs.getDate(arg0);
	}

	public java.sql.Date getDate(int arg0, java.util.Calendar arg1) throws SQLException {
		return rs.getDate(arg0, arg1);
	}

	public java.sql.Date getDate(java.lang.String arg0, java.util.Calendar arg1) throws SQLException {
		return rs.getDate(arg0, arg1);
	}

	public java.sql.Date getDate(java.lang.String arg0) throws SQLException {
		return rs.getDate(arg0);
	}

	public boolean wasNull() throws SQLException {
		return rs.wasNull();
	}

	public java.math.BigDecimal getBigDecimal(int arg0) throws SQLException {
		return rs.getBigDecimal(arg0);
	}

	public java.math.BigDecimal getBigDecimal(java.lang.String arg0) throws SQLException {
		return rs.getBigDecimal(arg0);
	}

	public java.math.BigDecimal getBigDecimal(int arg0, int arg1) throws SQLException {
		return rs.getBigDecimal(arg0, arg1);
	}

	public java.math.BigDecimal getBigDecimal(java.lang.String arg0, int arg1) throws SQLException {
		return rs.getBigDecimal(arg0, arg1);
	}

	public java.sql.Timestamp getTimestamp(int arg0, java.util.Calendar arg1) throws SQLException {
		return rs.getTimestamp(arg0, arg1);
	}

	public java.sql.Timestamp getTimestamp(java.lang.String arg0, java.util.Calendar arg1) throws SQLException {
		return rs.getTimestamp(arg0, arg1);
	}

	public java.sql.Timestamp getTimestamp(int arg0) throws SQLException {
		return rs.getTimestamp(arg0);
	}

	public java.sql.Timestamp getTimestamp(java.lang.String arg0) throws SQLException {
		return rs.getTimestamp(arg0);
	}

	public java.io.InputStream getAsciiStream(java.lang.String arg0) throws SQLException {
		return rs.getAsciiStream(arg0);
	}

	public java.io.InputStream getAsciiStream(int arg0) throws SQLException {
		return rs.getAsciiStream(arg0);
	}

	public java.io.InputStream getUnicodeStream(int arg0) throws SQLException {
		return rs.getUnicodeStream(arg0);
	}

	public java.io.InputStream getUnicodeStream(java.lang.String arg0) throws SQLException {
		return rs.getUnicodeStream(arg0);
	}

	public java.io.InputStream getBinaryStream(java.lang.String arg0) throws SQLException {
		return rs.getBinaryStream(arg0);
	}

	public java.io.InputStream getBinaryStream(int arg0) throws SQLException {
		return rs.getBinaryStream(arg0);
	}

	public java.sql.SQLWarning getWarnings() throws SQLException {
		return rs.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		rs.clearWarnings();
	}

	public java.lang.String getCursorName() throws SQLException {
		return rs.getCursorName();
	}

	public java.sql.ResultSetMetaData getMetaData() throws SQLException {
		return rs.getMetaData();
	}

	public int findColumn(java.lang.String arg0) throws SQLException {
		return rs.findColumn(arg0);
	}

	public java.io.Reader getCharacterStream(java.lang.String arg0) throws SQLException {
		return rs.getCharacterStream(arg0);
	}

	public java.io.Reader getCharacterStream(int arg0) throws SQLException {
		return rs.getCharacterStream(arg0);
	}

	public boolean isBeforeFirst() throws SQLException {
		return rs.isBeforeFirst();
	}

	public boolean isAfterLast() throws SQLException {
		return rs.isAfterLast();
	}

	public boolean isFirst() throws SQLException {
		return rs.isFirst();
	}

	public boolean isLast() throws SQLException {
		return rs.isLast();
	}

	public void beforeFirst() throws SQLException {
		rs.beforeFirst();
	}

	public void afterLast() throws SQLException {
		rs.afterLast();
	}

	public boolean last() throws SQLException {
		return rs.last();
	}

	public int getRow() throws SQLException {
		return rs.getRow();
	}

	public boolean absolute(int arg0) throws SQLException {
		return rs.absolute(arg0);
	}

	public boolean relative(int arg0) throws SQLException {
		return rs.relative(arg0);
	}

	public void setFetchDirection(int arg0) throws SQLException {
		rs.setFetchDirection(arg0);
	}

	public int getFetchDirection() throws SQLException {
		return rs.getFetchDirection();
	}

	public void setFetchSize(int arg0) throws SQLException {
		rs.setFetchSize(arg0);
	}

	public int getFetchSize() throws SQLException {
		return rs.getFetchSize();
	}

	public int getConcurrency() throws SQLException {
		return rs.getConcurrency();
	}

	public boolean rowUpdated() throws SQLException {
		return rs.rowUpdated();
	}

	public boolean rowInserted() throws SQLException {
		return rs.rowInserted();
	}

	public boolean rowDeleted() throws SQLException {
		return rs.rowDeleted();
	}

	public void updateNull(java.lang.String arg0) throws SQLException {
		rs.updateNull(arg0);
	}

	public void updateNull(int arg0) throws SQLException {
		rs.updateNull(arg0);
	}

	public void updateBoolean(int arg0, boolean arg1) throws SQLException {
		rs.updateBoolean(arg0, arg1);
	}

	public void updateBoolean(java.lang.String arg0, boolean arg1) throws SQLException {
		rs.updateBoolean(arg0, arg1);
	}

	public void updateByte(java.lang.String arg0, byte arg1) throws SQLException {
		rs.updateByte(arg0, arg1);
	}

	public void updateByte(int arg0, byte arg1) throws SQLException {
		rs.updateByte(arg0, arg1);
	}

	public void updateShort(java.lang.String arg0, short arg1) throws SQLException {
		rs.updateShort(arg0, arg1);
	}

	public void updateShort(int arg0, short arg1) throws SQLException {
		rs.updateShort(arg0, arg1);
	}

	public void updateInt(int arg0, int arg1) throws SQLException {
		rs.updateInt(arg0, arg1);
	}

	public void updateInt(java.lang.String arg0, int arg1) throws SQLException {
		rs.updateInt(arg0, arg1);
	}

	public void updateLong(java.lang.String arg0, long arg1) throws SQLException {
		rs.updateLong(arg0, arg1);
	}

	public void updateLong(int arg0, long arg1) throws SQLException {
		rs.updateLong(arg0, arg1);
	}

	public void updateFloat(int arg0, float arg1) throws SQLException {
		rs.updateFloat(arg0, arg1);
	}

	public void updateFloat(java.lang.String arg0, float arg1) throws SQLException {
		rs.updateFloat(arg0, arg1);
	}

	public void updateDouble(java.lang.String arg0, double arg1) throws SQLException {
		rs.updateDouble(arg0, arg1);
	}

	public void updateDouble(int arg0, double arg1) throws SQLException {
		rs.updateDouble(arg0, arg1);
	}

	public void updateBigDecimal(java.lang.String arg0, java.math.BigDecimal arg1) throws SQLException {
		rs.updateBigDecimal(arg0, arg1);
	}

	public void updateBigDecimal(int arg0, java.math.BigDecimal arg1) throws SQLException {
		rs.updateBigDecimal(arg0, arg1);
	}

	public void updateString(int arg0, java.lang.String arg1) throws SQLException {
		rs.updateString(arg0, arg1);
	}

	public void updateString(java.lang.String arg0, java.lang.String arg1) throws SQLException {
		rs.updateString(arg0, arg1);
	}

	public void updateDate(int arg0, java.sql.Date arg1) throws SQLException {
		rs.updateDate(arg0, arg1);
	}

	public void updateDate(java.lang.String arg0, java.sql.Date arg1) throws SQLException {
		rs.updateDate(arg0, arg1);
	}

	public void updateTime(int arg0, java.sql.Time arg1) throws SQLException {
		rs.updateTime(arg0, arg1);
	}

	public void updateTime(java.lang.String arg0, java.sql.Time arg1) throws SQLException {
		rs.updateTime(arg0, arg1);
	}

	public void updateTimestamp(java.lang.String arg0, java.sql.Timestamp arg1) throws SQLException {
		rs.updateTimestamp(arg0, arg1);
	}

	public void updateTimestamp(int arg0, java.sql.Timestamp arg1) throws SQLException {
		rs.updateTimestamp(arg0, arg1);
	}

	public void updateAsciiStream(java.lang.String arg0, java.io.InputStream arg1, int arg2) throws SQLException {
		rs.updateAsciiStream(arg0, arg1, arg2);
	}

	public void updateAsciiStream(int arg0, java.io.InputStream arg1, long arg2) throws SQLException {
		rs.updateAsciiStream(arg0, arg1, arg2);
	}

	public void updateAsciiStream(java.lang.String arg0, java.io.InputStream arg1) throws SQLException {
		rs.updateAsciiStream(arg0, arg1);
	}

	public void updateAsciiStream(int arg0, java.io.InputStream arg1) throws SQLException {
		rs.updateAsciiStream(arg0, arg1);
	}

	public void updateAsciiStream(int arg0, java.io.InputStream arg1, int arg2) throws SQLException {
		rs.updateAsciiStream(arg0, arg1, arg2);
	}

	public void updateAsciiStream(java.lang.String arg0, java.io.InputStream arg1, long arg2) throws SQLException {
		rs.updateAsciiStream(arg0, arg1, arg2);
	}

	public void updateBinaryStream(java.lang.String arg0, java.io.InputStream arg1) throws SQLException {
		rs.updateBinaryStream(arg0, arg1);
	}

	public void updateBinaryStream(int arg0, java.io.InputStream arg1, int arg2) throws SQLException {
		rs.updateBinaryStream(arg0, arg1, arg2);
	}

	public void updateBinaryStream(int arg0, java.io.InputStream arg1) throws SQLException {
		rs.updateBinaryStream(arg0, arg1);
	}

	public void updateBinaryStream(int arg0, java.io.InputStream arg1, long arg2) throws SQLException {
		rs.updateBinaryStream(arg0, arg1, arg2);
	}

	public void updateBinaryStream(java.lang.String arg0, java.io.InputStream arg1, int arg2) throws SQLException {
		rs.updateBinaryStream(arg0, arg1, arg2);
	}

	public void updateBinaryStream(java.lang.String arg0, java.io.InputStream arg1, long arg2) throws SQLException {
		rs.updateBinaryStream(arg0, arg1, arg2);
	}

	public void updateCharacterStream(int arg0, java.io.Reader arg1) throws SQLException {
		rs.updateCharacterStream(arg0, arg1);
	}

	public void updateCharacterStream(int arg0, java.io.Reader arg1, long arg2) throws SQLException {
		rs.updateCharacterStream(arg0, arg1, arg2);
	}

	public void updateCharacterStream(int arg0, java.io.Reader arg1, int arg2) throws SQLException {
		rs.updateCharacterStream(arg0, arg1, arg2);
	}

	public void updateCharacterStream(java.lang.String arg0, java.io.Reader arg1) throws SQLException {
		rs.updateCharacterStream(arg0, arg1);
	}

	public void updateCharacterStream(java.lang.String arg0, java.io.Reader arg1, int arg2) throws SQLException {
		rs.updateCharacterStream(arg0, arg1, arg2);
	}

	public void updateCharacterStream(java.lang.String arg0, java.io.Reader arg1, long arg2) throws SQLException {
		rs.updateCharacterStream(arg0, arg1, arg2);
	}

	public void updateObject(java.lang.String arg0, java.lang.Object arg1, int arg2) throws SQLException {
		rs.updateObject(arg0, arg1, arg2);
	}

	public void updateObject(java.lang.String arg0, java.lang.Object arg1) throws SQLException {
		rs.updateObject(arg0, arg1);
	}

	public void updateObject(int arg0, java.lang.Object arg1, java.sql.SQLType arg2) throws SQLException {
		rs.updateObject(arg0, arg1, arg2);
	}

	public void updateObject(java.lang.String arg0, java.lang.Object arg1, java.sql.SQLType arg2, int arg3)
			throws SQLException {
		rs.updateObject(arg0, arg1, arg2, arg3);
	}

	public void updateObject(int arg0, java.lang.Object arg1, java.sql.SQLType arg2, int arg3) throws SQLException {
		rs.updateObject(arg0, arg1, arg2, arg3);
	}

	public void updateObject(int arg0, java.lang.Object arg1, int arg2) throws SQLException {
		rs.updateObject(arg0, arg1, arg2);
	}

	public void updateObject(java.lang.String arg0, java.lang.Object arg1, java.sql.SQLType arg2) throws SQLException {
		rs.updateObject(arg0, arg1, arg2);
	}

	public void updateObject(int arg0, java.lang.Object arg1) throws SQLException {
		rs.updateObject(arg0, arg1);
	}

	public void insertRow() throws SQLException {
		rs.insertRow();
	}

	public void updateRow() throws SQLException {
		rs.updateRow();
	}

	public void deleteRow() throws SQLException {
		rs.deleteRow();
	}

	public void refreshRow() throws SQLException {
		rs.refreshRow();
	}

	public void cancelRowUpdates() throws SQLException {
		rs.cancelRowUpdates();
	}

	public void moveToInsertRow() throws SQLException {
		rs.moveToInsertRow();
	}

	public void moveToCurrentRow() throws SQLException {
		rs.moveToCurrentRow();
	}

	public java.sql.Statement getStatement() throws SQLException {
		return rs.getStatement();
	}

	public java.sql.Blob getBlob(int arg0) throws SQLException {
		return rs.getBlob(arg0);
	}

	public java.sql.Blob getBlob(java.lang.String arg0) throws SQLException {
		return rs.getBlob(arg0);
	}

	public java.sql.Clob getClob(int arg0) throws SQLException {
		return rs.getClob(arg0);
	}

	public java.sql.Clob getClob(java.lang.String arg0) throws SQLException {
		return rs.getClob(arg0);
	}

	public void updateRef(java.lang.String arg0, java.sql.Ref arg1) throws SQLException {
		rs.updateRef(arg0, arg1);
	}

	public void updateRef(int arg0, java.sql.Ref arg1) throws SQLException {
		rs.updateRef(arg0, arg1);
	}

	public void updateBlob(int arg0, java.io.InputStream arg1, long arg2) throws SQLException {
		rs.updateBlob(arg0, arg1, arg2);
	}

	public void updateBlob(java.lang.String arg0, java.sql.Blob arg1) throws SQLException {
		rs.updateBlob(arg0, arg1);
	}

	public void updateBlob(int arg0, java.sql.Blob arg1) throws SQLException {
		rs.updateBlob(arg0, arg1);
	}

	public void updateBlob(int arg0, java.io.InputStream arg1) throws SQLException {
		rs.updateBlob(arg0, arg1);
	}

	public void updateBlob(java.lang.String arg0, java.io.InputStream arg1) throws SQLException {
		rs.updateBlob(arg0, arg1);
	}

	public void updateBlob(java.lang.String arg0, java.io.InputStream arg1, long arg2) throws SQLException {
		rs.updateBlob(arg0, arg1, arg2);
	}

	public void updateClob(java.lang.String arg0, java.sql.Clob arg1) throws SQLException {
		rs.updateClob(arg0, arg1);
	}

	public void updateClob(int arg0, java.sql.Clob arg1) throws SQLException {
		rs.updateClob(arg0, arg1);
	}

	public void updateClob(int arg0, java.io.Reader arg1, long arg2) throws SQLException {
		rs.updateClob(arg0, arg1, arg2);
	}

	public void updateClob(int arg0, java.io.Reader arg1) throws SQLException {
		rs.updateClob(arg0, arg1);
	}

	public void updateClob(java.lang.String arg0, java.io.Reader arg1) throws SQLException {
		rs.updateClob(arg0, arg1);
	}

	public void updateClob(java.lang.String arg0, java.io.Reader arg1, long arg2) throws SQLException {
		rs.updateClob(arg0, arg1, arg2);
	}

	public void updateArray(int arg0, java.sql.Array arg1) throws SQLException {
		rs.updateArray(arg0, arg1);
	}

	public void updateArray(java.lang.String arg0, java.sql.Array arg1) throws SQLException {
		rs.updateArray(arg0, arg1);
	}

	public java.sql.RowId getRowId(int arg0) throws SQLException {
		return rs.getRowId(arg0);
	}

	public java.sql.RowId getRowId(java.lang.String arg0) throws SQLException {
		return rs.getRowId(arg0);
	}

	public void updateRowId(int arg0, java.sql.RowId arg1) throws SQLException {
		rs.updateRowId(arg0, arg1);
	}

	public void updateRowId(java.lang.String arg0, java.sql.RowId arg1) throws SQLException {
		rs.updateRowId(arg0, arg1);
	}

	public int getHoldability() throws SQLException {
		return rs.getHoldability();
	}

	public boolean isClosed() throws SQLException {
		return rs.isClosed();
	}

	public void updateNString(int arg0, java.lang.String arg1) throws SQLException {
		rs.updateNString(arg0, arg1);
	}

	public void updateNString(java.lang.String arg0, java.lang.String arg1) throws SQLException {
		rs.updateNString(arg0, arg1);
	}

	public void updateNClob(java.lang.String arg0, java.sql.NClob arg1) throws SQLException {
		rs.updateNClob(arg0, arg1);
	}

	public void updateNClob(int arg0, java.io.Reader arg1, long arg2) throws SQLException {
		rs.updateNClob(arg0, arg1, arg2);
	}

	public void updateNClob(int arg0, java.sql.NClob arg1) throws SQLException {
		rs.updateNClob(arg0, arg1);
	}

	public void updateNClob(java.lang.String arg0, java.io.Reader arg1) throws SQLException {
		rs.updateNClob(arg0, arg1);
	}

	public void updateNClob(int arg0, java.io.Reader arg1) throws SQLException {
		rs.updateNClob(arg0, arg1);
	}

	public void updateNClob(java.lang.String arg0, java.io.Reader arg1, long arg2) throws SQLException {
		rs.updateNClob(arg0, arg1, arg2);
	}

	public java.sql.NClob getNClob(java.lang.String arg0) throws SQLException {
		return rs.getNClob(arg0);
	}

	public java.sql.NClob getNClob(int arg0) throws SQLException {
		return rs.getNClob(arg0);
	}

	public java.sql.SQLXML getSQLXML(int arg0) throws SQLException {
		return rs.getSQLXML(arg0);
	}

	public java.sql.SQLXML getSQLXML(java.lang.String arg0) throws SQLException {
		return rs.getSQLXML(arg0);
	}

	public void updateSQLXML(java.lang.String arg0, java.sql.SQLXML arg1) throws SQLException {
		rs.updateSQLXML(arg0, arg1);
	}

	public void updateSQLXML(int arg0, java.sql.SQLXML arg1) throws SQLException {
		rs.updateSQLXML(arg0, arg1);
	}

	public java.lang.String getNString(java.lang.String arg0) throws SQLException {
		return rs.getNString(arg0);
	}

	public java.lang.String getNString(int arg0) throws SQLException {
		return rs.getNString(arg0);
	}

	public java.io.Reader getNCharacterStream(int arg0) throws SQLException {
		return rs.getNCharacterStream(arg0);
	}

	public java.io.Reader getNCharacterStream(java.lang.String arg0) throws SQLException {
		return rs.getNCharacterStream(arg0);
	}

	public void updateNCharacterStream(int arg0, java.io.Reader arg1, long arg2) throws SQLException {
		rs.updateNCharacterStream(arg0, arg1, arg2);
	}

	public void updateNCharacterStream(int arg0, java.io.Reader arg1) throws SQLException {
		rs.updateNCharacterStream(arg0, arg1);
	}

	public void updateNCharacterStream(java.lang.String arg0, java.io.Reader arg1) throws SQLException {
		rs.updateNCharacterStream(arg0, arg1);
	}

	public void updateNCharacterStream(java.lang.String arg0, java.io.Reader arg1, long arg2) throws SQLException {
		rs.updateNCharacterStream(arg0, arg1, arg2);
	}

	public void updateBytes(java.lang.String arg0, byte[] arg1) throws SQLException {
		rs.updateBytes(arg0, arg1);
	}

	public void updateBytes(int arg0, byte[] arg1) throws SQLException {
		rs.updateBytes(arg0, arg1);
	}

	public java.lang.Object getObject(java.lang.String arg0, java.util.Map arg1) throws SQLException {
		return rs.getObject(arg0, arg1);
	}

	public java.lang.Object getObject(int arg0, java.lang.Class arg1) throws SQLException {
		return rs.getObject(arg0, arg1);
	}

	public java.lang.Object getObject(int arg0, java.util.Map arg1) throws SQLException {
		return rs.getObject(arg0, arg1);
	}

	public java.lang.Object getObject(java.lang.String arg0, java.lang.Class arg1) throws SQLException {
		return rs.getObject(arg0, arg1);
	}

	public boolean getBoolean(java.lang.String arg0) throws SQLException {
		return rs.getBoolean(arg0);
	}

	public boolean getBoolean(int arg0) throws SQLException {
		return rs.getBoolean(arg0);
	}

	public byte getByte(java.lang.String arg0) throws SQLException {
		return rs.getByte(arg0);
	}

	public byte getByte(int arg0) throws SQLException {
		return rs.getByte(arg0);
	}

	public short getShort(java.lang.String arg0) throws SQLException {
		return rs.getShort(arg0);
	}

	public short getShort(int arg0) throws SQLException {
		return rs.getShort(arg0);
	}

	public int getInt(java.lang.String arg0) throws SQLException {
		return rs.getInt(arg0);
	}

	public int getInt(int arg0) throws SQLException {
		return rs.getInt(arg0);
	}

	public long getLong(java.lang.String arg0) throws SQLException {
		return rs.getLong(arg0);
	}

	public long getLong(int arg0) throws SQLException {
		return rs.getLong(arg0);
	}

	public float getFloat(int arg0) throws SQLException {
		return rs.getFloat(arg0);
	}

	public float getFloat(java.lang.String arg0) throws SQLException {
		return rs.getFloat(arg0);
	}

	public double getDouble(int arg0) throws SQLException {
		return rs.getDouble(arg0);
	}

	public double getDouble(java.lang.String arg0) throws SQLException {
		return rs.getDouble(arg0);
	}

	public byte[] getBytes(java.lang.String arg0) throws SQLException {
		return rs.getBytes(arg0);
	}

	public byte[] getBytes(int arg0) throws SQLException {
		return rs.getBytes(arg0);
	}

	public boolean next() throws SQLException {
		return rs.next();
	}

	public java.net.URL getURL(java.lang.String arg0) throws SQLException {
		return rs.getURL(arg0);
	}

	public java.net.URL getURL(int arg0) throws SQLException {
		return rs.getURL(arg0);
	}

	public boolean first() throws SQLException {
		return rs.first();
	}

	public void close() throws SQLException {
		rs.close();
	}

	public int getType() throws SQLException {
		return rs.getType();
	}

	public java.sql.Ref getRef(java.lang.String arg0) throws SQLException {
		return rs.getRef(arg0);
	}

	public java.sql.Ref getRef(int arg0) throws SQLException {
		return rs.getRef(arg0);
	}

	public boolean previous() throws SQLException {
		return rs.previous();
	}

	public boolean isWrapperFor(java.lang.Class arg0) throws SQLException {
		return rs.isWrapperFor(arg0);
	}

	public java.lang.Object unwrap(java.lang.Class arg0) throws SQLException {
		return rs.unwrap(arg0);
	}

	/* Automatically generate wrapper */
	// import java.lang.reflect.Method;
	// import java.lang.reflect.Parameter;
	// public static void main(String[] args) {
	// Method[] methods = ResultSet.class.getMethods();
	// for(Method method: methods) {
	// String types = "";
	// String arguments = "";
	// for(Parameter parameter: method.getParameters()) {
	// types += parameter.getType().getCanonicalName() + " " + parameter.getName() +
	// ",";
	// arguments += parameter.getName() + ",";
	// }
	//
	// if(arguments.length() > 0) {
	// types = types.replaceAll(",$", "");
	// arguments = arguments.replaceAll(",$", "");
	// }
	//
	// String returnType = method.getReturnType().getCanonicalName();
	//
	// String m = "public " + returnType + " " + method.getName() + "(" + types + ")
	// throws SQLException{\n\t";
	// if(!returnType.equals("void")) {
	// m += "return ";
	// }
	// m += "rs." + method.getName() + "(" + arguments + ");\n}";
	// System.out.println(m);
	// }
	// }
}
