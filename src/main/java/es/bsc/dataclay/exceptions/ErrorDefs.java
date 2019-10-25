
/**
 * @file ErrorDefs.java
 * 
 * @date Oct 22, 2012
 */

package es.bsc.dataclay.exceptions;

import java.util.ResourceBundle;

/**
 * This class contains all errors that Logic Module must return in case of an Exception in any manager.
 * 
 * 
 */
public final class ErrorDefs {

	/** Error code properties file. */
	private static ResourceBundle errorProps = null;

	/**
	 * Enum for the error codes.
	 */
	public enum ERRORCODE {
		/** DB backend failure. */
		DB_EXCEPTION,

		/** Some parameter is null or empty. */
		NULL_OR_EMPTY_PARAMETER_EXCEPTION,
		
		/** Some parameter has non-valid value. */
		INVALID_PARAMETER_EXCEPTION,

		// DBHANDLER ERRORS
		/** DbHandlerConf is bad defined. */
		BAD_DEFINED_DBHANDLER_CONF,

		// ACCOUNT ERRORS
		/** Account already exists. */
		ACCOUNT_EXISTS,
		/** Account does not exist. */
		ACCOUNT_NOT_EXIST,
		/** Account invalid credentials. */
		INVALID_CREDENTIALS,

		// CLASS ERRORS
		/** Class does not exist. */
		CLASS_NOT_EXIST,
		/** Class language dependant info already registered. */
		CLASS_LANG_DEP_INFO_ALREADY_REGISTERED,
		/** Class found an Unsupported Language. */
		CLASS_UNSUPPORTED_LANGUAGE,
		/** Property language dependant info already registered. */
		PROP_LANG_DEP_INFO_ALREADY_REGISTERED,
		/** Type language dependant info already registered. */
		TYPE_LANG_DEP_INFO_ALREADY_REGISTERED,
		/** Operation language dependant info already registered. */
		OP_LANG_DEP_INFO_ALREADY_REGISTERED,
		/** Incompatible language of class with namespace where it is being registered. */
		INCOMPATIBLE_LANGUAGE_FOR_CLASS_WITH_NAMESPACE,
		/** Property does not exist. */
		PROPERTY_NOT_EXIST,
		/** Property not in class. */
		PROPERTY_NOT_IN_CLASS,
		/** Property already in class. */
		PROPERTY_ALREADY_IN_CLASS,
		/** Default getter and setter operations cannot be removed. */
		DEFAULT_GETTER_SETTER_OPERATION_CANNOT_BE_REMOVED,
		/** Operation does not exist. */
		OPERATION_NOT_EXIST,
		/** Operation not in class. */
		OPERATION_NOT_IN_CLASS,
		/** Operation already exists in class. */
		OPERATION_ALREADY_IN_CLASS,
		/** Implementation does not exist. */
		IMPLEMENTATION_NOT_EXIST,
		/** Implementation not compatible with its operation. */
		INCOMPATIBLE_IMPLEMENTATION,
		/** Operation has no other implementation. */
		NO_OTHER_IMPL_FOR_OPERATION,
		/** Some implementation accesses the operation. */
		SOME_IMPLEMENTATIONS_ACCESS_OPERATION,

		// NAMESPACE ERRORS
		/** Namespace already exists. */
		NAMESPACE_EXISTS,
		/** Namespace does not exist. */
		NAMESPACE_NOT_EXIST,
		/** Wrong namespace responsible provided. */
		BAD_NAMESPACE_RESPONSIBLE,
		/** Namespace has classes. */
		NAMESPACE_WITH_CLASSES,
		/** Namespace has contracts. */
		NAMESPACE_WITH_CONTRACTS,

		// DATASET ERRORS
		/** DataSet already exists. */
		DATASET_EXISTS,
		/** DataSet does not exist. */
		DATASET_NOT_EXIST,
		/** DataSet with objects. */
		DATASET_NOT_EMPTY,
		/** Wrong dataset responsible provided. */
		BAD_DATASET_RESPONSIBLE,

		// INTERFACE ERRORS
		/** Interface does not exist. */
		INTERFACE_NOT_EXIST,
		/** Operation already in interface. */
		OPERATION_ALREADY_IN_INTERFACE,
		/** Operation not in interface. */
		OPERATION_NOT_IN_INTERFACE,
		/** Interface has contracts. */
		INTERFACE_WITH_CONTRACTS,
		/** Interface is "bad", or inconsistent with neighbors. */
		BAD_INTERFACE,

		// CONTRACT ERRORS
		/** Account already registered in contract. */
		ACCOUNT_ALREADY_REGISTERED_IN_CONTRACT,
		/** Account not registered in contract. */
		ACCOUNT_NOT_REGISTERED_IN_CONTRACT,
		/** Contract does not exist. */
		CONTRACT_NOT_EXIST,
		/** Contract is not active. */
		CONTRACT_NOT_ACTIVE,
		/** Contract is not public. */
		CONTRACT_NOT_PUBLIC,
		/** Interface is not in contract. */
		INTERFACE_NOT_IN_CONTRACT,
		/** Interface is not in contract. */
		INTERFACE_NOT_ACCESSIBLE,
		/** Contract is duplicated. */
		CONTRACT_DUPLICATION,
		/** Bad contract defined. */
		BAD_CONTRACT,
		/** Wrong dates in contract. */
		BAD_DATES,
		/** Some interfaces belongs to the same metaclass. */
		SOME_INTERFACES_WITH_SAME_METACLASS,
		/** Some operation has no accessible implementation. */
		SOME_OPERATION_WITH_NO_ACCESSIBLE_IMPL,
		/** Implementation is accessible from contracts. */
		IMPL_ACCESSIBLE_FROM_CONTRACTS,

		// DATA CONTRACT ERRORS
		/** Account already has a contract with a certain dataset as provider. */
		ACCOUNT_ALREADY_HAS_A_DATACONTRACT_WITH_PROVIDER,
		/** Account has no contract with a certain dataset as provider. */
		ACCOUNT_HAS_NO_DATACONTRACT_WITH_PROVIDER,
		/** Contract does not exist. */
		DATACONTRACT_NOT_EXIST,
		/** Contract is not active. */
		DATACONTRACT_NOT_ACTIVE,
		/** Contract is not public. */
		DATACONTRACT_NOT_PUBLIC,
		/** Dataset with contracts. */
		DATASET_WITH_DATACONTRACTS,

		// DATA SERVICE
		/** Execute method error. */
		EXECUTE_METHOD_ERROR,
		/** Object does not exist. */
		OBJECT_NOT_EXIST,
		/** Data Service initialization error. */
		DATASERVICE_INIT_ERROR,
		/** Error when deploying a class. */
		CLASS_DEPLOYMENT_ERROR,

		/** Error produced during an execution of a user registered class. */
		LANGUAGE_EXECUTION_EXCEPTION,

		/** Cannot clean execution classes directory. */
		CLEAN_EXEC_CLASSES_DIR_ERROR, 
		/** Error while deploying library. */
		ERROR_DEPLOYING_LIB,

		// METADATASERVICE
		/** Backend already exists. */
		STORAGE_LOCATION_ALREADY_EXIST,
		/** Backend does not exist. */
		STORAGE_LOCATION_NOT_EXIST,
		/** Backend unreachable. */
		STORAGE_LOCATION_UNREACHABLE,
		/** Backend already exists. */
		EXECUTION_ENVIRONMENT_ALREADY_EXIST,
		/** Backend does not exist. */
		EXECUTION_ENVIRONMENT_NOT_EXIST,
		/** Object already registered in MDS. */
		OBJECT_ALREADY_REGISTERED,
		/** Alias already exists. */
		ALIAS_ALREADY_EXISTS,
		/** Object has replicas. */
		OBJECT_HAS_REPLICAS, 
		/** Object is not readonly. */
		OBJECT_IS_NOT_READONLY, 
		/** Object is readonly. */
		OBJECT_IS_READONLY, 
		/** Object is not in backend. */
		OBJECT_NOT_IN_BACKEND,
		/** External dataClay instance is not registered. */
		EXTERNAL_DATACLAY_NOT_REGISTERED,

		// IMPORTS
		/** Interface already imported. */
		INTERFACE_ALREADY_IMPORTED,
		/** Interface not imported. */
		INTERFACE_NOT_IMPORTED,
		/** Interface to import was created in target namespace. */
		IMPORTED_INTERFACE_WAS_CREATED_IN_TARGET_NAMESPACE,
		/** Class of imported interface was created in target namespace. */
		CLASS_OF_IMPORTED_INTERFACE_WAS_CREATED_IN_TARGET_NAMESPACE,
		/** Class with same name in namespace. */
		CLASS_WITH_SAME_NAME_IN_NAMESPACE,
		/** Imported interface in use. */
		IMPORTED_INTERFACE_IN_USE,
		/** Class is not imported. */
		CLASS_NOT_IMPORTED,
		/** Class not created in namespace or not imported. */
		CLASS_NOT_CREATED_IN_NAMESPACE_NOR_IMPORTED,
		/** Bad imports. */
		BAD_IMPORTS,

		// SESSION MANAGER
		/** Appliable end deta for session is before current date. */
		APPLIABLE_END_DATE_FOR_SESSION_IS_BEFORE_CURRENT_DATE, 
		/** Session does not match requirements. */
		SESSION_DOES_NOT_MATCH_REQ,
		/** Session is not active. */
		SESSION_IS_NOT_ACTIVE,
		/** Session not of account. */
		SESSION_NOT_EXISTS,
		/** The data set specified for storing new objects is not present among the given data contracts. */
		DATASET_FOR_STORE_NOT_AMONG_DATACONTRACTS,
		/** The data set of an object is not among the available data contracts. */
		DATASET_NOT_AMONG_DATACONTRACTS,

		/** Class is included. */
		CLASS_INCLUDED,
		/** DataService Backend not answering. */
		DATASERVICE_BACKEND_NOT_RESPONDS,

		// DB HANDLER
		/** Object not exists in database. */
		OBJECT_NOT_EXISTS_IN_DB,
		/** Object already exists in database. */
		OBJECT_EXISTS_IN_DB,

		// TCP COMMUNICATION PROTOCOL
		/** Request was interrupted. */
		REQUEST_INTERRUPTED,
		/** Unexpected exception. */
		UNEXPECTED_EXCEPTION,

		// NOTIFICATION MGR
		/** Event listener already registered. */
		EVENT_LISTENER_ALREADY_REGISTERED,
		/** Event objs meet condition already registered. */
		EVENT_OBJS_MEET_CONDITION_ALREADY_REGISTERED,
		/** Event listener not registered. */
		EVENT_LISTENER_NOT_REGISTERED,
		/** Event message not registered. */
		EVENT_MESSAGE_NOT_REGISTERED,

		// REGISTER LISTENER
		/**  */
		/** Metaclass does not exist. */
		METACLASSID_NOT_EXIST,
		/** MetadataInfo was not found. */
		METADATAINFO_WAS_NOT_FOUND,
		/** FilterMethod must not have parameters. */
		FILTERMETHOD_MUST_NOT_HAVE_PARAMETERS,
		/** FilterMethod must return a boolean. */
		FILTERMETHOD_MUST_RETURN_A_BOOLEAN,
		/** FilterMethod was not found. */
		FILTERMETHOD_WAS_NOT_FOUND,

		// Others
		/** Generic java exception. */
		UNKNOWN_EXCEPTION;

		/**
		 * Init properties
		 */
		private void init() {
			if (errorProps == null) {
				errorProps = ResourceBundle.getBundle("properties.errorcodes");
			}
			final Integer errvalue = Integer.valueOf(errorProps.getString(this.name()));
			setErrorID(errvalue);
		}

		/** Error code ID. */
		private Integer errorID = null;

		/**
		 * Get the ERRORCODE::errorID
		 * @return the errorID
		 */
		public int getErrorID() {
			if (errorID == null) {
				init();
			}
			return errorID;
		}

		/**
		 * Set the ERRORCODE::errorID
		 * @param newerrorID
		 *            the errorID to set
		 */
		public void setErrorID(final int newerrorID) {
			this.errorID = newerrorID;
		}
	}
}
