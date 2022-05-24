
package es.bsc.dataclay.logic.sessionmgr;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.Set;
import java.util.UUID;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.GetResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.dbhandler.Utils;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectAlreadyExistException;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.DataContractID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.management.sessionmgr.SessionContract;
import es.bsc.dataclay.util.management.sessionmgr.SessionDataContract;
import es.bsc.dataclay.util.management.sessionmgr.SessionImplementation;
import es.bsc.dataclay.util.management.sessionmgr.SessionInterface;
import es.bsc.dataclay.util.management.sessionmgr.SessionOperation;
import es.bsc.dataclay.util.management.sessionmgr.SessionProperty;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;




/**
 * Data base connection.
 */
public final class ETCDSessionManagerDB {

	/** Logger. */
	private Logger logger;

	/** Indicates if debug is enabled. */
	private static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** DataSource. */
	private final Client dataSource;


	/**
	 * Constructor.
	 *
	 */
	public ETCDSessionManagerDB(final Client dataSource) {
		this.dataSource = dataSource;
		if (DEBUG_ENABLED) {
			logger = LogManager.getLogger("LMDB");
		}
	}

	/**
	 * Store into database
	 * @param infoSession
	 *            Object to store
	 */
	public void store(final Session infoSession) {

		final UUID uuid = infoSession.getDataClayID().getId();

		KV kvClient = dataSource.getKVClient();

		String key = "/session/" + uuid;
		ByteSequence keyBytes = ByteSequence.from(key.getBytes());

		String value = new JSONObject()
			.put("id", uuid)
			.put("account", infoSession.getAccountID().getId())
			.put("namespaces", JSONObject.NULL)
			.put("datasets", JSONObject.NULL)
			.put("dataset_for_store", JSONObject.NULL).toString();

		ByteSequence valueBytes = ByteSequence.from(value.getBytes());

		try {
			kvClient.put(keyBytes, valueBytes).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

	
