
package es.bsc.dataclay.logic.classmgr;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements.
 */
public final class ClassManagerSQLStatements {

	/** Properties. */
	private static ResourceBundle props = null;

	/**
	 * Utility classes should have private constructor.
	 */
	private ClassManagerSQLStatements() {

	}

	/**
	 * Init properties of the properties file
	 */
	static {
		if (props == null) {
			props = ResourceBundle.getBundle("es.bsc.dataclay.properties.class_mgr_sql");
		}
	}

	/**
	 * This enumeration represent all possible SQL statements.
	 */
	public enum SqlStatements {

		/** Create table. */
		CREATE_TABLE_ACCESSED_IMPL,
		/** Create table. */
		CREATE_TABLE_ACCESSED_PROP,
		/** Create table. */
		CREATE_TABLE_TYPE,
		/** Create table. */
		CREATE_TABLE_JAVA_TYPE,
		/** Create table. */
		CREATE_TABLE_PYTHON_TYPE,
		/** Create table. */
		CREATE_TABLE_MEMORY_FEATURE,
		/** Create table. */
		CREATE_TABLE_CPU_FEATURE,
		/** Create table. */
		CREATE_TABLE_LANGUAGE_FEATURE,
		/** Create table. */
		CREATE_TABLE_ARCH_FEATURE,
		/** Create table. */
		CREATE_TABLE_PREFETCHING_INFO,
		/** Create table. */
		CREATE_TABLE_PYTHON_IMPLEMENTATION,
		/** Create table. */
		CREATE_TABLE_JAVA_IMPLEMENTATION,
		/** Create table. */
		CREATE_TABLE_ANNOTATION,
		/** Create table. */
		CREATE_TABLE_PROPERTY,
		/** Create table. */
		CREATE_TABLE_JAVA_PROPERTY,
		/** Create table. */
		CREATE_TABLE_PYTHON_PROPERTY,
		/** Create table. */
		CREATE_TABLE_OPERATION,
		/** Create table. */
		CREATE_TABLE_JAVA_OPERATION,
		/** Create table. */
		CREATE_TABLE_PYTHON_OPERATION,
		/** Create table. */
		CREATE_TABLE_METACLASS,
		/** Create table. */
		CREATE_TABLE_JAVA_METACLASS,
		/** Create table. */
		CREATE_TABLE_PYTHON_METACLASS,
		/** Drop table. */
		DROP_TABLE_ACCESSED_IMPL,
		/** Drop table. */
		DROP_TABLE_ACCESSED_PROP,
		/** Drop table. */
		DROP_TABLE_TYPE,
		/** Drop table. */
		DROP_TABLE_JAVA_TYPE,
		/** Drop table. */
		DROP_TABLE_PYTHON_TYPE,
		/** Drop table. */
		DROP_TABLE_MEMORY_FEATURE,
		/** Drop table. */
		DROP_TABLE_CPU_FEATURE,
		/** Drop table. */
		DROP_TABLE_LANGUAGE_FEATURE,
		/** Drop table. */
		DROP_TABLE_ARCH_FEATURE,
		/** Drop table. */
		DROP_TABLE_PREFETCHING_INFO,
		/** Drop table. */
		DROP_TABLE_JAVA_IMPLEMENTATION,
		/** Drop table. */
		DROP_TABLE_PYTHON_IMPLEMENTATION,
		/** Drop table. */
		DROP_TABLE_ANNOTATION,
		/** Drop table. */
		DROP_TABLE_PROPERTY,
		/** Drop table. */
		DROP_TABLE_JAVA_PROPERTY,
		/** Drop table. */
		DROP_TABLE_PYTHON_PROPERTY,
		/** Drop table. */
		DROP_TABLE_OPERATION,
		/** Drop table. */
		DROP_TABLE_JAVA_OPERATION,
		/** Drop table. */
		DROP_TABLE_PYTHON_OPERATION,
		/** Drop table. */
		DROP_TABLE_METACLASS,
		/** Drop table. */
		DROP_TABLE_JAVA_METACLASS,
		/** Drop table. */
		DROP_TABLE_PYTHON_METACLASS,
		/** Insert. */
		INSERT_ACCESSED_IMPL,
		/** Insert. */
		INSERT_ACCESSED_PROP,
		/** Insert. */
		INSERT_TYPE,
		/** Insert. */
		INSERT_JAVA_TYPE,
		/** Insert. */
		INSERT_PYTHON_TYPE,
		/** Insert. */
		INSERT_MEMORY_FEATURE,
		/** Insert. */
		INSERT_CPU_FEATURE,
		/** Insert. */
		INSERT_LANGUAGE_FEATURE,
		/** Insert. */
		INSERT_ARCH_FEATURE,
		/** Insert. */
		INSERT_PREFETCHING_INFO,
		/** Insert. */
		INSERT_JAVA_IMPLEMENTATION,
		/** Insert. */
		INSERT_PYTHON_IMPLEMENTATION,
		/** Insert. */
		INSERT_ANNOTATION,
		/** Insert. */
		INSERT_PROPERTY,
		/** Insert. */
		INSERT_JAVA_PROPERTY,
		/** Insert. */
		INSERT_PYTHON_PROPERTY,
		/** Insert. */
		INSERT_OPERATION,
		/** Insert. */
		INSERT_JAVA_OPERATION,
		/** Insert. */
		INSERT_PYTHON_OPERATION,
		/** Insert. */
		INSERT_METACLASS,
		/** Insert. */
		INSERT_JAVA_METACLASS,
		/** Insert. */
		INSERT_PYTHON_METACLASS,
		/** Select. */
		SELECT_METACLASS_BY_ID,
		/** Select. */
		SELECT_JAVA_METACLASS_BY_ID,
		/** Select. */
		SELECT_PYTHON_METACLASS_BY_ID,
		/** Select. */
		SELECT_TYPE_BY_ID,
		/** Select. */
		SELECT_JAVA_TYPE_BY_ID,
		/** Select. */
		SELECT_PYTHON_TYPE_BY_ID,
		/** Select. */
		SELECT_ACCESSED_PROP_BY_ID,
		/** Select. */
		SELECT_ACCESSED_IMPL_BY_ID,
		/** Select. */
		SELECT_MEMORY_FEATURE_BY_ID,
		/** Select. */
		SELECT_CPU_FEATURE_BY_ID,
		/** Select. */
		SELECT_LANGUAGE_FEATURE_BY_ID,
		/** Select. */
		SELECT_ARCH_FEATURE_BY_ID,
		/** Select. */
		SELECT_PREFETCHING_INFO_BY_ID,
		/** Select. */
		SELECT_JAVA_IMPLEMENTATION_BY_ID,
		/** Select. */
		SELECT_PYTHON_IMPLEMENTATION_BY_ID,
		/** Select. */
		SELECT_PYTHON_CODE_BY_IMPLEMENTATION_ID,
		/** Select. */
		SELECT_ANNOTATION_BY_ID,
		/** Select. */
		SELECT_PROPERTY_BY_ID,
		/** Select. */
		SELECT_JAVA_PROPERTY_BY_ID,
		/** Select. */
		SELECT_PYTHON_PROPERTY_BY_ID,
		/** Select. */
		SELECT_OPERATION_BY_ID,
		/** Select. */
		SELECT_JAVA_OPERATION_BY_ID,
		/** Select. */
		SELECT_PYTHON_OPERATION_BY_ID,
		/** Delete. */
		DELETE_TYPE_BY_ID,
		/** Delete. */
		DELETE_JAVA_TYPE_BY_ID,
		/** Delete. */
		DELETE_PYTHON_TYPE_BY_ID,
		/** Delete. */
		DELETE_ACCESSED_PROP_BY_ID,
		/** Delete. */
		DELETE_ACCESSED_IMPL_BY_ID,
		/** Delete. */
		DELETE_MEMORY_FEATURE_BY_ID,
		/** Delete. */
		DELETE_CPU_FEATURE_BY_ID,
		/** Delete. */
		DELETE_LANGUAGE_FEATURE_BY_ID,
		/** Delete. */
		DELETE_ARCH_FEATURE_BY_ID,
		/** Delete. */
		DELETE_PREFETCHING_INFO_BY_ID,
		/** Delete. */
		DELETE_JAVA_IMPLEMENTATION_BY_ID,
		/** Delete. */
		DELETE_PYTHON_IMPLEMENTATION_BY_ID,
		/** Delete. */
		DELETE_ANNOTATION_BY_ID,
		/** Delete. */
		DELETE_PROPERTY_BY_ID,
		/** Delete. */
		DELETE_JAVA_PROPERTY_BY_ID,
		/** Delete. */
		DELETE_PYTHON_PROPERTY_BY_ID,
		/** Delete. */
		DELETE_OPERATION_BY_ID,
		/** Delete. */
		DELETE_JAVA_OPERATION_BY_ID,
		/** Delete. */
		DELETE_PYTHON_OPERATION_BY_ID,
		/** Delete. */
		DELETE_METACLASS_BY_ID,
		/** Delete. */
		DELETE_JAVA_METACLASS_BY_ID,
		/** Delete. */
		DELETE_PYTHON_METACLASS_BY_ID,
		/** Update. */
		UPDATE_JAVA_CLASS_BYTECODE_BY_ID,
		/** Exists class in namespace provided. */
		EXISTS_CLASS_IN_NAMESPACE,
		/** Add property. */
		UPDATE_CLASS_ADD_PROPERTY_BY_ID,
		/** Add operation. */
		UPDATE_CLASS_ADD_OPERATION_BY_ID,
		/** Remove operation from class. */
		UPDATE_CLASS_REMOVE_OPERATION_BY_ID,
		/** Add implementation . */
		UPDATE_OPERATION_ADD_IMPLEMENTATION_BY_ID,
		/** Remove implementation from op. */
		UPDATE_OPERATION_REMOVE_IMPLEMENTATION_BY_ID,
		/** Select class by name and namespace ID. */
		SELECT_CLASS_BY_NAME_AND_NAMESPACEID,
		/** Select class by name and namespace. */
		SELECT_CLASS_BY_NAME_AND_NAMESPACE,
		/** Select property by names. */
		SELECT_PROPERTY_BY_NAMES,
		/** Select operation by names. */
		SELECT_OPERATION_BY_NAMES,
		/** Select implementation by names. */
		SELECT_IMPLEMENTATION_BY_NAMES,
		/** Select class by name. */
		SELECT_CLASS_BY_NAME,
		/** Exists type using class id. */
		EXISTS_TYPE_CLASS_ID,
		/** Exists accessed implementation with id. */
		EXISTS_ACCESSED_IMPLEMENTATION_ID,
		/** Get property of class id and namespace id. */
		SELECT_PROPERTY_OF_ENRICHMENT,
		/** Get operation of class id and namespace id. */
		SELECT_OPERATION_OF_ENRICHMENT,
		/** Get all classes in NAMESPACE. */
		SELECT_CLASSES_IN_NAMESPACE,
		/** Get classID by class name and its namespace id. */
		SELECT_CLASSID_BY_NAME_AND_NAMESPACEID;

		/** SQL statement. */
		private String sqlStatement;

		/**
		 * Init properties of the properties file
		 */
		private void init() {
			final String sqlSt = props.getString(this.name());
			setSqlStatement(sqlSt);
		}

		/**
		 * Get the sqlStatement
		 * @return the sqlStatement
		 */
		public String getSqlStatement() {
			if (sqlStatement == null) {
				init();
			}
			return sqlStatement;
		}

		/**
		 * Set the sqlStatement
		 * @param newsqlStatement
		 *            the sqlStatement
		 */
		public void setSqlStatement(final String newsqlStatement) {
			sqlStatement = newsqlStatement;
		}
	}
}
