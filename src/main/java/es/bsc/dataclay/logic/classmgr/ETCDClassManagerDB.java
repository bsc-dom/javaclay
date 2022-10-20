
package es.bsc.dataclay.logic.classmgr;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.GetResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.management.classmgr.AccessedImplementation;
import es.bsc.dataclay.util.management.classmgr.AccessedProperty;
import es.bsc.dataclay.util.management.classmgr.Annotation;
import es.bsc.dataclay.util.management.classmgr.Implementation;
import es.bsc.dataclay.util.management.classmgr.LanguageDependantClassInfo;
import es.bsc.dataclay.util.management.classmgr.LanguageDependantOperationInfo;
import es.bsc.dataclay.util.management.classmgr.LanguageDependantPropertyInfo;
import es.bsc.dataclay.util.management.classmgr.LanguageDependantTypeInfo;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.PrefetchingInformation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.management.classmgr.Type;
import es.bsc.dataclay.util.management.classmgr.UserType;
import es.bsc.dataclay.util.management.classmgr.features.ArchitectureFeature;
import es.bsc.dataclay.util.management.classmgr.features.CPUFeature;
import es.bsc.dataclay.util.management.classmgr.features.Feature;
import es.bsc.dataclay.util.management.classmgr.features.LanguageFeature;
import es.bsc.dataclay.util.management.classmgr.features.MemoryFeature;
import es.bsc.dataclay.util.management.classmgr.features.QualitativeFeature;
import es.bsc.dataclay.util.management.classmgr.features.QuantitativeFeature;
import es.bsc.dataclay.util.management.classmgr.features.Feature.FeatureType;
import es.bsc.dataclay.util.management.classmgr.java.JavaClassInfo;
import es.bsc.dataclay.util.management.classmgr.java.JavaImplementation;
import es.bsc.dataclay.util.management.classmgr.java.JavaOperationInfo;
import es.bsc.dataclay.util.management.classmgr.java.JavaPropertyInfo;
import es.bsc.dataclay.util.management.classmgr.java.JavaTypeInfo;
import es.bsc.dataclay.util.management.classmgr.python.PythonClassInfo;
import es.bsc.dataclay.util.management.classmgr.python.PythonImplementation;
import es.bsc.dataclay.util.management.classmgr.python.PythonOperationInfo;
import es.bsc.dataclay.util.management.classmgr.python.PythonPropertyInfo;
import es.bsc.dataclay.util.management.classmgr.python.PythonTypeInfo;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;

/**
 * Class manager data base.
 */
public final class ETCDClassManagerDB {

	/** Logger. */
	private static final Logger logger = LogManager.getLogger("managers.ETCDClassManager.DB");

	/** Indicates if debug is enabled. */
	private static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** DataSource. */
	private final Client dataSource;

	/**
	 * ETCDClassManagerDB constructor.
	 * 
	 * @param dataSource
	 *                   Name of the LM service managing.
	 */
	public ETCDClassManagerDB(final Client dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Store MetaClass into database
	 * 
	 * @param metaclass
	 *                  metaclass
	 * @return UUID of stored object
	 */
	public UUID storeMetaClass(final MetaClass metaclass) {

		final UUID uuid = metaclass.getDataClayID().getId();

		// TODO Store properties, operations and Parent type

		// Properties
		// final UUID[] properties = new UUID[metaclass.getProperties().size()];
		// int i = 0;
		// for (final Property prop : metaclass.getProperties()) {
		// properties[i] = this.storeProperty(prop);
		// i++;
		// }

		// Operations
		// final UUID[] operations = new UUID[metaclass.getOperations().size()];
		// i = 0;
		// for (final Operation op : metaclass.getOperations()) {
		// operations[i] = this.storeOperation(op);
		// i++;
		// }

		// ParentType
		// UUID parentTypeID = null;
		// if (metaclass.getParentType() != null) {
		// parentTypeID = this.storeType(metaclass.getParentType());
		// }

		KV kvClient = dataSource.getKVClient();

		String key = "/metaclass/" + uuid;
		ByteSequence keyBytes = ByteSequence.from(key.getBytes());

		String value = new JSONObject()
				.put("id", uuid)
				.put("namespace", metaclass.getNamespace())
				.put("class_name", metaclass.getName())
				// .put("parentType", JSONObject.NULL)
				// .put("properties", JSONObject.NULL)
				// .put("operations", JSONObject.NULL)
				// .put("isAbstract", metaclass.getIsAbstract())
				// .put("namespaceID", metaclass.getNamespaceID().getId())
				// .put("extendedtypes", JSONObject.NULL)
				// .put("extensions", JSONObject.NULL)
				.toString();

		ByteSequence valueBytes = ByteSequence.from(value.getBytes());

		try {
			kvClient.put(keyBytes, valueBytes).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
