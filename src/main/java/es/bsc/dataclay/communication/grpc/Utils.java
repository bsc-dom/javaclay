
/**
 * 
 */
package es.bsc.dataclay.communication.grpc;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.FieldDescriptor;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo;
import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.serialization.buffer.DataClayByteArray;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.lib.DataClayDeserializationLib;
import es.bsc.dataclay.serialization.lib.ImmutableParamOrReturn;
import es.bsc.dataclay.serialization.lib.LanguageParamOrReturn;
import es.bsc.dataclay.serialization.lib.ObjectWithDataParamOrReturn;
import es.bsc.dataclay.serialization.lib.PersistentParamOrReturn;
import es.bsc.dataclay.serialization.lib.SerializationLibUtils;
import es.bsc.dataclay.serialization.lib.SerializedParametersOrReturn;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.CredentialID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.DataContractID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.ECAID;
import es.bsc.dataclay.util.ids.EventMessageID;
import es.bsc.dataclay.util.ids.EventObjsMeetConditionID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.ids.QualitativeRegistryID;
import es.bsc.dataclay.util.ids.ResourceID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.ids.StorageLocationID;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;

import com.google.protobuf.MapEntry;

/**
 * Utility class for communication.
 */
public final class Utils {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger("exceptions");

	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/**
	 * Utility classes should have private constructor.
	 */
	private Utils() {

	}

	/**
	 * Get password credential
	 * 
	 * @param cred
	 *            Credential
	 * @return Password credential
	 */
	public static PasswordCredential getCredential(
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Credential cred) {
		if (cred == null || cred
				.equals(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Credential.getDefaultInstance())) {
			return null;
		} else {
			return new PasswordCredential(cred.getPassword());
		}
	}

	/**
	 * Get AccountID ID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return AccountID
	 */
	public static AccountID getID(final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.AccountID idMsg) {
		if (idMsg == null || idMsg
				.equals(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.AccountID.getDefaultInstance())) {
			return null;
		} else {
			return new AccountID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get ContractID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return ContractID
	 */
	public static ContractID getID(final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ContractID idMsg) {
		if (idMsg == null || idMsg
				.equals(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ContractID.getDefaultInstance())) {
			return null;
		} else {
			return new ContractID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get CredentialID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return CredentialID
	 */
	public static CredentialID getID(
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.CredentialID idMsg) {
		if (idMsg == null || idMsg
				.equals(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.CredentialID.getDefaultInstance())) {
			return null;
		} else {
			return new CredentialID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get DataContractID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return DataContractID
	 */
	public static DataContractID getID(
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.DataContractID idMsg) {
		if (idMsg == null || idMsg.equals(
				es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.DataContractID.getDefaultInstance())) {
			return null;
		} else {
			return new DataContractID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get DataSetID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return DataSetID
	 */
	public static DataSetID getID(final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.DataSetID idMsg) {
		if (idMsg == null || idMsg
				.equals(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.DataSetID.getDefaultInstance())) {
			return null;
		} else {
			return new DataSetID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get NamespaceID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return NamespaceID
	 */
	public static NamespaceID getID(
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.NamespaceID idMsg) {
		if (idMsg == null || idMsg
				.equals(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.NamespaceID.getDefaultInstance())) {
			return null;
		} else {
			return new NamespaceID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get ECAID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return ECAID
	 */
	public static ECAID getID(final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ECAID idMsg) {
		if (idMsg == null || idMsg
				.equals(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ECAID.getDefaultInstance())) {
			return null;
		} else {
			return new ECAID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get EventMessageID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return EventMessageID
	 */
	public static EventMessageID getID(
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EventMessageID idMsg) {
		if (idMsg == null || idMsg.equals(
				es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EventMessageID.getDefaultInstance())) {
			return null;
		} else {
			return new EventMessageID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get EventObjsMeetConditionID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return EventObjsMeetConditionID
	 */
	public static EventObjsMeetConditionID getID(
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EventObjsMeetConditionID idMsg) {
		if (idMsg == null
				|| idMsg.equals(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EventObjsMeetConditionID
						.getDefaultInstance())) {
			return null;
		} else {
			return new EventObjsMeetConditionID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get ExecutionEnvironmentID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return ExecutionEnvironmentID
	 */
	public static ExecutionEnvironmentID getID(
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExecutionEnvironmentID idMsg) {
		if (idMsg == null
				|| idMsg.equals(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExecutionEnvironmentID
						.getDefaultInstance())) {
			return null;
		} else {
			return new ExecutionEnvironmentID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get ImplementationID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return ImplementationID
	 */
	public static ImplementationID getID(
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ImplementationID idMsg) {
		if (idMsg == null || idMsg.equals(
				es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ImplementationID.getDefaultInstance())) {
			return null;
		} else {
			return new ImplementationID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get InterfaceID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return InterfaceID
	 */
	public static InterfaceID getID(
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.InterfaceID idMsg) {
		if (idMsg == null || idMsg
				.equals(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.InterfaceID.getDefaultInstance())) {
			return null;
		} else {
			return new InterfaceID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get MetaClassID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return MetaClassID
	 */
	public static MetaClassID getID(
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.MetaClassID idMsg) {
		if (idMsg == null || idMsg
				.equals(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.MetaClassID.getDefaultInstance())) {
			return null;
		} else {
			return new MetaClassID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get ObjectID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return ObjectID
	 */
	public static ObjectID getID(final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ObjectID idMsg) {
		if (idMsg == null || idMsg
				.equals(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ObjectID.getDefaultInstance())) {
			return null;
		} else {
			return new ObjectID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get OperationID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return OperationID
	 */
	public static OperationID getID(
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.OperationID idMsg) {
		if (idMsg == null || idMsg
				.equals(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.OperationID.getDefaultInstance())) {
			return null;
		} else {
			return new OperationID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get PropertyID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return PropertyID
	 */
	public static PropertyID getID(final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.PropertyID idMsg) {
		if (idMsg == null || idMsg
				.equals(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.PropertyID.getDefaultInstance())) {
			return null;
		} else {
			return new PropertyID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get QualitativeRegistryID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return QualitativeRegistryID
	 */
	public static QualitativeRegistryID getID(
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.QualitativeRegistryID idMsg) {
		if (idMsg == null
				|| idMsg.equals(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.QualitativeRegistryID
						.getDefaultInstance())) {
			return null;
		} else {
			return new QualitativeRegistryID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get ResourceID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return ResourceID
	 */
	public static ResourceID getID(final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ResourceID idMsg) {
		if (idMsg == null || idMsg
				.equals(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ResourceID.getDefaultInstance())) {
			return null;
		} else {
			return new ResourceID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get SessionID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return SessionID
	 */
	public static SessionID getID(final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.SessionID idMsg) {
		if (idMsg == null || idMsg
				.equals(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.SessionID.getDefaultInstance())) {
			return null;
		} else {
			return new SessionID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get StorageLocationID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return StorageLocationID
	 */
	public static StorageLocationID getID(
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.StorageLocationID idMsg) {
		if (idMsg == null || idMsg.equals(
				es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.StorageLocationID.getDefaultInstance())) {
			return null;
		} else {
			return new StorageLocationID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get DataClayID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return DataClayID
	 */
	public static DataClayInstanceID getID(
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.DataClayInstanceID idMsg) {
		if (idMsg == null || idMsg.equals(
				es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.DataClayInstanceID.getDefaultInstance())) {
			return null;
		} else {
			return new DataClayInstanceID(UUID.fromString(idMsg.getUuid()));
		}
	}

	/**
	 * Get ObjectID from uuid string
	 * 
	 * @param msgStr
	 *            UUID string
	 * @return ObjectID
	 */
	public static ObjectID getObjectIDFromUUID(final String msgStr) {
		if (msgStr == null || msgStr.trim().isEmpty()) {
			return null;
		} else {
			return new ObjectID(msgStr);
		}
	}

	/**
	 * Get MetaClassID from uuid string
	 * 
	 * @param msgStr
	 *            UUID string
	 * @return MetaClassID
	 */
	public static MetaClassID getMetaClassIDFromUUID(final String msgStr) {
		if (msgStr == null || msgStr.trim().isEmpty()) {
			return null;
		} else {
			return new MetaClassID(msgStr);
		}
	}

	/**
	 * Get DataContractID from uuid string
	 * 
	 * @param msgStr
	 *            UUID string
	 * @return DataContractID
	 */
	public static DataContractID getDataContractIDFromUUID(final String msgStr) {
		if (msgStr == null || msgStr.trim().isEmpty()) {
			return null;
		} else {
			return new DataContractID(msgStr);
		}
	}

	/**
	 * Get ContractID from uuid string
	 * 
	 * @param msgStr
	 *            UUID string
	 * @return ContractID
	 */
	public static ContractID getContractIDFromUUID(final String msgStr) {
		if (msgStr == null || msgStr.trim().isEmpty()) {
			return null;
		} else {
			return new ContractID(msgStr);
		}
	}

	/**
	 * Get AccountID from uuid string
	 * 
	 * @param msgStr
	 *            UUID string
	 * @return AccountID
	 */
	public static AccountID getAccountIDFromUUID(final String msgStr) {
		if (msgStr == null || msgStr.trim().isEmpty()) {
			return null;
		} else {
			return new AccountID(msgStr);
		}
	}

	/**
	 * Get StorageLocationID from uuid string
	 * 
	 * @param msgStr
	 *            UUID string
	 * @return StorageLocationID
	 */
	public static StorageLocationID getStorageLocationIDFromUUID(final String msgStr) {
		if (msgStr == null || msgStr.trim().isEmpty()) {
			return null;
		} else {
			return new StorageLocationID(msgStr);
		}
	}

	/**
	 * Get ExecutionEnvironmentID from uuid string
	 * 
	 * @param msgStr
	 *            UUID string
	 * @return ExecutionEnvironmentID
	 */
	public static ExecutionEnvironmentID getExecutionEnvironmentIDFromUUID(final String msgStr) {
		if (msgStr == null || msgStr.trim().isEmpty()) {
			return null;
		} else {
			return new ExecutionEnvironmentID(msgStr);
		}
	}

	/**
	 * Get SessionID from uuid string
	 * 
	 * @param msgStr
	 *            UUID string
	 * @return SessionID
	 */
	public static SessionID getSessionIDFromUUID(final String msgStr) {
		if (msgStr == null || msgStr.trim().isEmpty()) {
			return null;
		} else {
			return new SessionID(msgStr);
		}
	}

	/**
	 * Get message ID from Account ID
	 * 
	 * @param id
	 *            Account ID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.AccountID getMsgID(final AccountID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.AccountID.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.AccountID.newBuilder()
					.setUuid(id.getId().toString()).build();
		}
	}

	/**
	 * Get message ID from ContractID
	 * 
	 * @param id
	 *            ContractID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ContractID getMsgID(final ContractID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ContractID.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ContractID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from CredentialID
	 * 
	 * @param id
	 *            CredentialID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.CredentialID getMsgID(
			final CredentialID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.CredentialID.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.CredentialID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from DataContractID
	 * 
	 * @param id
	 *            DataContractID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.DataContractID getMsgID(
			final DataContractID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.DataContractID.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.DataContractID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from DataSetID
	 * 
	 * @param id
	 *            DataSetID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.DataSetID getMsgID(final DataSetID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.DataSetID.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.DataSetID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from NamespaceID
	 * 
	 * @param id
	 *            NamespaceID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.NamespaceID getMsgID(
			final NamespaceID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.NamespaceID.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.NamespaceID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from ECAID
	 * 
	 * @param id
	 *            ECAID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ECAID getMsgID(final ECAID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ECAID.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ECAID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from EventMessageID
	 * 
	 * @param id
	 *            EventMessageID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EventMessageID getMsgID(
			final EventMessageID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EventMessageID.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EventMessageID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from EventObjsMeetConditionID
	 * 
	 * @param id
	 *            EventObjsMeetConditionID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EventObjsMeetConditionID getMsgID(
			final EventObjsMeetConditionID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EventObjsMeetConditionID
					.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EventObjsMeetConditionID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from ExecutionEnvironmentID
	 * 
	 * @param id
	 *            ExecutionEnvironmentID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExecutionEnvironmentID getMsgID(
			final ExecutionEnvironmentID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExecutionEnvironmentID
					.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExecutionEnvironmentID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from ImplementationID
	 * 
	 * @param id
	 *            ImplementationID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ImplementationID getMsgID(
			final ImplementationID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ImplementationID.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ImplementationID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from InterfaceID
	 * 
	 * @param id
	 *            InterfaceID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.InterfaceID getMsgID(
			final InterfaceID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.InterfaceID.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.InterfaceID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from MetaClassID
	 * 
	 * @param id
	 *            MetaClassID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.MetaClassID getMsgID(
			final MetaClassID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.MetaClassID.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.MetaClassID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from ObjectID
	 * 
	 * @param id
	 *            ObjectID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ObjectID getMsgID(final ObjectID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ObjectID.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ObjectID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from OperationID
	 * 
	 * @param id
	 *            OperationID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.OperationID getMsgID(
			final OperationID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.OperationID.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.OperationID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from PropertyID
	 * 
	 * @param id
	 *            PropertyID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.PropertyID getMsgID(final PropertyID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.PropertyID.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.PropertyID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from QualitativeRegistryID
	 * 
	 * @param id
	 *            QualitativeRegistryID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.QualitativeRegistryID getMsgID(
			final QualitativeRegistryID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.QualitativeRegistryID
					.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.QualitativeRegistryID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from ResourceID
	 * 
	 * @param id
	 *            ResourceID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ResourceID getMsgID(final ResourceID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ResourceID.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ResourceID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from SessionID
	 * 
	 * @param id
	 *            SessionID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.SessionID getMsgID(final SessionID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.SessionID.getDefaultInstance();
		} else {
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.SessionID sessIDMsg = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.SessionID
					.newBuilder().setUuid(id.getId().toString()).build();
			return sessIDMsg;
		}
	}

	/**
	 * Get message ID from StorageLocationID
	 * 
	 * @param id
	 *            StorageLocationID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.StorageLocationID getMsgID(
			final StorageLocationID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.StorageLocationID.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.StorageLocationID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get message ID from DataClayID
	 * 
	 * @param id
	 *            DataClayID
	 * @return Protocol buffers message ID
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.DataClayInstanceID getMsgID(
			final DataClayInstanceID id) {
		if (id == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.DataClayInstanceID.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.DataClayInstanceID.newBuilder()
					.setUuid(id.getId().toString()).build();

		}
	}

	/**
	 * Get password credential
	 * 
	 * @param cred
	 *            Credential
	 * @return Password credential
	 */
	public static es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Credential getCredential(
			final PasswordCredential cred) {
		if (cred == null) {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Credential.getDefaultInstance();
		} else {
			return es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Credential.newBuilder()
					.setPassword(cred.getPassword()).build();
		}
	}

	/**
	 * Return ExceptionInfo message
	 * 
	 * @param responseObserver
	 *            Observer
	 */
	public static void returnExceptionInfoMessage(final io.grpc.stub.StreamObserver<ExceptionInfo> responseObserver) {
		final ExceptionInfo resp = ExceptionInfo.newBuilder().build();
		responseObserver.onNext(resp);
	}

	/**
	 * Get MetaData GRPC message from DataClayMetaData
	 * 
	 * @param metadata
	 *            DataClayMetaData
	 * @return MetaData GRPC message
	 */
	public static CommonMessages.DataClayObjectMetaData getMetaData(final DataClayObjectMetaData metadata) {
		final CommonMessages.DataClayObjectMetaData.Builder metadataBuilder = CommonMessages.DataClayObjectMetaData
				.newBuilder();

		if (metadata.getOids() != null) {
			for (final Entry<Integer, ObjectID> oidEntry : metadata.getOids().entrySet()) {
				if (oidEntry.getValue() != null) {
					final CommonMessages.ObjectID oidIDmsg = Utils.getMsgID(oidEntry.getValue());
					metadataBuilder.putOids(oidEntry.getKey(), oidIDmsg);
					// System.err.println("OID entry bytes = " + Reflector.bytesToHex(
					// oidIDmsg.toByteArray()));
				}
			}
		}

		if (metadata.getClassIDs() != null) {
			for (final Entry<Integer, MetaClassID> classIDEntry : metadata.getClassIDs().entrySet()) {
				if (classIDEntry.getValue() != null) {
					final CommonMessages.MetaClassID classIDmsg = Utils.getMsgID(classIDEntry.getValue());
					metadataBuilder.putClassids(classIDEntry.getKey(), classIDmsg);
					// System.err.println("ClassID entry bytes = " + Reflector.bytesToHex(
					// classIDmsg.toByteArray()));
				}
			}
		}

		if (metadata.getHints() != null) {
			for (final Entry<Integer, ExecutionEnvironmentID> hintEntry : metadata.getHints().entrySet()) {
				if (hintEntry.getKey() != null && hintEntry.getValue() != null) {
					metadataBuilder.putHints(hintEntry.getKey(), Utils.getMsgID(hintEntry.getValue()));
				}
			}
		}

		metadataBuilder.setNumRefs(metadata.getNumRefs());

		return metadataBuilder.build();

	}

	/**
	 * Retrieve the metadata from the specified msg object
	 * 
	 * @param msg
	 *            a message with metadata of a certain object
	 * @return the metadata included within the message
	 */
	public static DataClayObjectMetaData getMetaData(final CommonMessages.DataClayObjectMetaData msg) {
		final Map<Integer, MetaClassID> classIDs = new ConcurrentHashMap<>();
		final Map<Integer, ObjectID> oids = new ConcurrentHashMap<>();
		final Map<Integer, ExecutionEnvironmentID> hints = new ConcurrentHashMap<>();
		final Map<Integer, DataClayInstanceID> extDataClayIDs = new ConcurrentHashMap<>();

		if (msg.getClassidsMap() != null) {
			for (final Entry<Integer, CommonMessages.MetaClassID> classIDEntry : msg.getClassidsMap().entrySet()) {
				if (classIDEntry.getKey() != null && classIDEntry.getValue() != null) {
					final MetaClassID classID = Utils.getID(classIDEntry.getValue());
					if (classID != null) {
						classIDs.put(classIDEntry.getKey(), classID);
					}
				}
			}
		}

		if (msg.getOidsMap() != null) {
			for (final Entry<Integer, CommonMessages.ObjectID> entry : msg.getOidsMap().entrySet()) {
				if (entry.getKey() != null && entry.getValue() != null) {
					oids.put(entry.getKey(), Utils.getID(entry.getValue()));
				}
			}
		}

		if (msg.getHintsMap() != null) {
			for (final Entry<Integer, CommonMessages.ExecutionEnvironmentID> entry : msg.getHintsMap().entrySet()) {
				if (entry.getKey() != null && entry.getValue() != null) {
					hints.put(entry.getKey(), Utils.getID(entry.getValue()));
				}
			}
		}

		if (msg.getExtDataClayIDsMap() != null) {
			for (final Entry<Integer, CommonMessages.DataClayInstanceID> entry : msg.getExtDataClayIDsMap()
					.entrySet()) {
				if (entry.getKey() != null && entry.getValue() != null) {
					extDataClayIDs.put(entry.getKey(), Utils.getID(entry.getValue()));
				}
			}
		}

		final int numRefs = msg.getNumRefs();
		return new DataClayObjectMetaData(oids, classIDs, hints, extDataClayIDs, numRefs);

	}

	/**
	 * Return a message containing an object with data (parameter or return)
	 * 
	 * @param volParamOrRet
	 *            an object with data (parameter or return)
	 * @return the resulting message
	 */
	public static CommonMessages.ObjectWithDataParamOrReturn getObjectWithDataParamOrReturn(
			final ObjectWithDataParamOrReturn volParamOrRet) {
		final CommonMessages.ObjectWithDataParamOrReturn.Builder volBuilder = CommonMessages.ObjectWithDataParamOrReturn
				.newBuilder();
		final CommonMessages.MetaClassID classIDmsg = Utils.getMsgID(volParamOrRet.getClassID());
		volBuilder.setClassid(classIDmsg);
		// System.err.println("ObjwData class ID size : " +
		// classIDmsg.getSerializedSize());

		final CommonMessages.ObjectID objectIDmsg = Utils.getMsgID(volParamOrRet.getObjectID());
		volBuilder.setOid(objectIDmsg);
		// System.err.println("ObjwData oid ID size : " +
		// objectIDmsg.getSerializedSize());

		if (volParamOrRet.getMetaData() != null) {
			final CommonMessages.DataClayObjectMetaData mdata = Utils.getMetaData(volParamOrRet.getMetaData());
			volBuilder.setMetadata(mdata);
		}
		// System.err.println("ObjwData metadata size : " + mdata.getSerializedSize());

		final ByteString objByteString = volParamOrRet.getSerializedBytes().getByteString();
		volBuilder.setObjbytes(objByteString);
		// System.err.println("ObjwData bytestring size : " + objByteString.size());
		return volBuilder.build();
	}

	/**
	 * Obtain the object with data (parameter or return) from the specified message
	 * 
	 * @param volParamOrRet
	 *            a message containing an object with data (parameter or return)
	 * @return the instance of an object with data (parameter or return)
	 */
	public static ObjectWithDataParamOrReturn getObjectWithDataParamOrReturn(
			final CommonMessages.ObjectWithDataParamOrReturn volParamOrRet) {
		final ObjectID oid = Utils.getID(volParamOrRet.getOid());
		final MetaClassID classid = Utils.getID(volParamOrRet.getClassid());
		final DataClayObjectMetaData mdata = Utils.getMetaData(volParamOrRet.getMetadata());
		final DataClayByteArray byteArray = new DataClayByteArray(volParamOrRet.getObjbytes());
		return new ObjectWithDataParamOrReturn(oid, classid, mdata, byteArray);
	}

	/**
	 * Return a message containing a language object (parameter or return)
	 * 
	 * @param paramOrRet
	 *            the language object
	 * @return the resulting message
	 */
	public static CommonMessages.LanguageParamOrReturn getLanguageParamOrReturn(
			final LanguageParamOrReturn paramOrRet) {
		final CommonMessages.LanguageParamOrReturn.Builder builder = CommonMessages.LanguageParamOrReturn.newBuilder();

		final CommonMessages.DataClayObjectMetaData mdata = Utils.getMetaData(paramOrRet.getMetaData());
		builder.setMetadata(mdata);
		// System.err.println("Lang param metadata lenght = " +
		// mdata.getSerializedSize());
		// System.err.println("Lang param metadata = " + Reflector.bytesToHex(
		// mdata.toByteArray()));

		final ByteString objBytes = paramOrRet.getSerializedBytes().getByteString();
		builder.setObjbytes(objBytes);
		// System.err.println("Lang param obj.bytes lenght = " + objBytes.size());

		return builder.build();
	}

	/**
	 * Return an instance of language object included in the specified message
	 * 
	 * @param paramOrRet
	 *            the message containing the language objet
	 * @return the language object
	 */
	public static LanguageParamOrReturn getLanguageParamOrReturn(
			final CommonMessages.LanguageParamOrReturn paramOrRet) {
		final DataClayObjectMetaData mdata = Utils.getMetaData(paramOrRet.getMetadata());
		final DataClayByteArray byteArray = new DataClayByteArray(paramOrRet.getObjbytes());
		return new LanguageParamOrReturn(mdata, byteArray);
	}

	/**
	 * Return the immutable instance included in the specified message
	 * 
	 * @param paramOrRet
	 *            the message
	 * @return the resulting immutable instance
	 */
	public static ImmutableParamOrReturn getImmutableParamOrReturn(
			final CommonMessages.ImmutableParamOrReturn paramOrRet) {
		final DataClayByteArray byteArray = new DataClayByteArray(paramOrRet.getObjbytes());
		return new ImmutableParamOrReturn(byteArray);
	}

	/**
	 * Return a message including the specified immutable object
	 * 
	 * @param paramOrRet
	 *            the immutable object
	 * @return the resulting message
	 */
	public static CommonMessages.ImmutableParamOrReturn getImmutableParamOrReturn(
			final ImmutableParamOrReturn paramOrRet) {
		final CommonMessages.ImmutableParamOrReturn.Builder builder = CommonMessages.ImmutableParamOrReturn
				.newBuilder();
		builder.setObjbytes(paramOrRet.getSerializedBytes().getByteString());
		return builder.build();
	}

	/**
	 * Return a message including the specified param or return object
	 * 
	 * @param paramOrRet
	 *            the object to be included within the message
	 * @return the resulting message
	 */
	public static CommonMessages.PersistentParamOrReturn getPersistentParamOrReturn(
			final PersistentParamOrReturn paramOrRet) {
		final CommonMessages.PersistentParamOrReturn.Builder builder = CommonMessages.PersistentParamOrReturn
				.newBuilder();
		builder.setOid(Utils.getMsgID(paramOrRet.getObjectID()));
		if (paramOrRet.getHint() != null) {
			builder.setHint(Utils.getMsgID(paramOrRet.getHint()));
		}
		if (paramOrRet.getClassID() != null) {
			builder.setClassID(Utils.getMsgID(paramOrRet.getClassID()));
		}
		if (paramOrRet.getExtDataClayID() != null) {
			builder.setExtDataClayID(Utils.getMsgID(paramOrRet.getExtDataClayID()));
		}

		return builder.build();
	}

	/**
	 * Return the param or return object included in the specified message
	 * 
	 * @param paramOrRet
	 *            the message
	 * @return the resulting param or return object
	 */
	public static PersistentParamOrReturn getPersistentParamOrReturn(
			final CommonMessages.PersistentParamOrReturn paramOrRet) {

		MetaClassID classID = null;
		ExecutionEnvironmentID hint = null;
		DataClayInstanceID extDataClayID = null;
		if (paramOrRet.hasHint()) {
			hint = Utils.getID(paramOrRet.getHint());
		}
		if (paramOrRet.hasClassID()) {
			classID = Utils.getID(paramOrRet.getClassID());
		}
		if (paramOrRet.hasExtDataClayID()) {
			extDataClayID = Utils.getID(paramOrRet.getExtDataClayID());
		}

		return new PersistentParamOrReturn(Utils.getID(paramOrRet.getOid()), hint, classID, extDataClayID);
	}

	/**
	 * Return the message including the specified serialized param or return object
	 * 
	 * @param serParamsOrRet
	 *            a serialized param or return object
	 * @return the resulting message
	 */
	public static CommonMessages.SerializedParametersOrReturn getParamsOrReturn(
			final SerializedParametersOrReturn serParamsOrRet) {

		final CommonMessages.SerializedParametersOrReturn.Builder builder = CommonMessages.SerializedParametersOrReturn
				.newBuilder();

		builder.setNumParams(serParamsOrRet.getNumParams());
		for (final Entry<Integer, ImmutableParamOrReturn> langEntry : serParamsOrRet.getImmObjs().entrySet()) {
			final CommonMessages.ImmutableParamOrReturn immParamMsg = Utils
					.getImmutableParamOrReturn(langEntry.getValue());
			builder.putImmParams(langEntry.getKey(), immParamMsg);
			// System.err.println("Imm param bytes lenght = " +
			// immParamMsg.getSerializedSize());

		}
		for (final Entry<Integer, LanguageParamOrReturn> langEntry : serParamsOrRet.getLangObjs().entrySet()) {
			final CommonMessages.LanguageParamOrReturn langParamMsg = Utils
					.getLanguageParamOrReturn(langEntry.getValue());
			builder.putLangParams(langEntry.getKey(), langParamMsg);
			// System.err.println("Lang param bytes lenght = " +
			// langParamMsg.getSerializedSize());

		}
		for (final Entry<Integer, PersistentParamOrReturn> entry : serParamsOrRet.getPersistentRefs().entrySet()) {
			final CommonMessages.PersistentParamOrReturn persMsg = Utils.getPersistentParamOrReturn(entry.getValue());
			builder.putPersParams(entry.getKey(), persMsg);
			// System.err.println("Pers param bytes lenght = " +
			// persMsg.getSerializedSize());
		}
		for (final Entry<Integer, ObjectWithDataParamOrReturn> entry : serParamsOrRet.getVolatileObjs().entrySet()) {
			final CommonMessages.ObjectWithDataParamOrReturn objDataParamMsg = Utils
					.getObjectWithDataParamOrReturn(entry.getValue());
			builder.putVolatileParams(entry.getKey(), objDataParamMsg);
			// System.err.println("Obj.Data param bytes lenght = " +
			// objDataParamMsg.getSerializedSize());

		}
		return builder.build();
	}

	/**
	 * Return a serialized param or return object from the specified message
	 * 
	 * @param serParamsOrRetMsg
	 *            the message
	 * @return the serialized param or return object from the message
	 */
	public static SerializedParametersOrReturn getParamsOrReturn(
			final CommonMessages.SerializedParametersOrReturn serParamsOrRetMsg) {
		final int numParams = serParamsOrRetMsg.getNumParams();
		final Map<Integer, ImmutableParamOrReturn> immObjs = new ConcurrentHashMap<>();
		final Map<Integer, LanguageParamOrReturn> langObjs = new ConcurrentHashMap<>();
		final Map<Integer, ObjectWithDataParamOrReturn> volObjs = new ConcurrentHashMap<>();
		final Map<Integer, PersistentParamOrReturn> persObjs = new ConcurrentHashMap<>();
		for (final Entry<Integer, CommonMessages.ImmutableParamOrReturn> entry : serParamsOrRetMsg.getImmParamsMap()
				.entrySet()) {
			immObjs.put(entry.getKey(), Utils.getImmutableParamOrReturn(entry.getValue()));
		}
		for (final Entry<Integer, CommonMessages.LanguageParamOrReturn> entry : serParamsOrRetMsg.getLangParamsMap()
				.entrySet()) {
			langObjs.put(entry.getKey(), Utils.getLanguageParamOrReturn(entry.getValue()));
		}
		for (final Entry<Integer, CommonMessages.ObjectWithDataParamOrReturn> entry : serParamsOrRetMsg
				.getVolatileParamsMap().entrySet()) {
			volObjs.put(entry.getKey(), Utils.getObjectWithDataParamOrReturn(entry.getValue()));
		}
		for (final Entry<Integer, CommonMessages.PersistentParamOrReturn> entry : serParamsOrRetMsg.getPersParamsMap()
				.entrySet()) {
			persObjs.put(entry.getKey(), Utils.getPersistentParamOrReturn(entry.getValue()));
		}
		return new SerializedParametersOrReturn(numParams, immObjs, langObjs, volObjs, persObjs);
	}

	private static final int MAX_BYTES_PER_LINE = 16;
	private static final int MAX_CHARS_PER_BYTE = 10;
	private static final String COLUMN_SEPARATOR = "|";
	private static final String LINE_SEPARATOR = "-";

	protected static final char[] hexArray = "0123456789ABCDEF".toCharArray();

	/**
	 * Get bytes to hexadecimal Strings
	 * 
	 * @param bytes
	 *            Bytes
	 * @return Hexadecimal strings
	 */
	public static synchronized Queue<String> bytesToHex(final byte[] bytes) {
		final char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			final int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		final Queue<String> listBytes = new LinkedList<>();
		for (final char hexChar : hexChars) {
			listBytes.add(String.valueOf(hexChar));
		}
		return listBytes;
	}

	/**
	 * Get number of bytes needed for a GRPC tag
	 * 
	 * @param numNextBytes
	 *            Number of bytes to represent
	 * @return Number of bytes needed for a GRPC tag.
	 */
	public static synchronized int getNumBytesNeededInVLQInt(final int numNextBytes) {
		int bytesNeeded = 0;
		int curMaxSize = 0;
		final int bitsAvailablePerByte = 7;
		while (curMaxSize < numNextBytes) {
			bytesNeeded++;
			curMaxSize = (int) Math.pow(2, bitsAvailablePerByte * bytesNeeded);
		}
		return bytesNeeded;
	}

	/**
	 * Print bytes header
	 */
	public static synchronized void printBytesHeader(final Queue<String> byteStr, final int byteIndex) {
		System.out.println();
		for (int j = 0; j < MAX_BYTES_PER_LINE; ++j) {
			for (int i = 0; i < MAX_CHARS_PER_BYTE; ++i) {
				System.out.print("=");
			}
		}
		System.out.println();

		System.out.print(COLUMN_SEPARATOR);
		for (int j = 0; j < MAX_BYTES_PER_LINE; ++j) {
			int maxLength = MAX_CHARS_PER_BYTE - 1;
			for (int i = 0; i < MAX_CHARS_PER_BYTE - 1; ++i) {
				if (i == Math.round(MAX_CHARS_PER_BYTE / 2.0) - 1) {
					System.out.print(j + byteIndex);
					maxLength = maxLength - String.valueOf(j + byteIndex).length();
				} else {
					System.out.print(" ");
					if (i >= maxLength) {
						break;
					}
				}
			}
			System.out.print(COLUMN_SEPARATOR);
		}
		System.out.println();

		for (int j = 0; j < MAX_BYTES_PER_LINE; ++j) {
			for (int i = 0; i < MAX_CHARS_PER_BYTE; ++i) {
				System.out.print(LINE_SEPARATOR);
			}
		}
		System.out.println();
	}

	/**
	 * Print grpc tag
	 * 
	 * @param typeName
	 *            name of type
	 * @param serializedSize
	 *            Size of message (next message identified by tag)
	 * @param byteStr
	 *            Hexadecimal bytes
	 * @param currentByteIndex
	 *            Current byte index
	 * @return Current byte index
	 */
	private static synchronized int printGrpcTagField(final String typeName, final int serializedSize,
			final Queue<String> byteStr, final int currentByteIndex) {
		final int numBytesNeededPerTag = getNumBytesNeededInVLQInt(serializedSize) + 1; // tag + numBytes
		return printBytes(typeName, numBytesNeededPerTag, byteStr, currentByteIndex);
	}

	/**
	 * Print N bytes
	 * 
	 * @param typeName
	 *            Tag (if needed)
	 * @param numBytes
	 *            Num bytes to print
	 * @param byteStr
	 *            Hexadecimal bytes
	 * @param byteIndex
	 *            Current byte index
	 * @return Current byte index
	 */
	private static synchronized int printBytes(final String typeName, final int numBytes, final Queue<String> byteStr,
			final int byteIndex) {
		int currentByteIndex = byteIndex;
		for (int i = 0; i < numBytes; ++i) {

			if (currentByteIndex % MAX_BYTES_PER_LINE == 0) {
				if (currentByteIndex > 0) {
					System.out.print(COLUMN_SEPARATOR);
				}
				printBytesHeader(byteStr, currentByteIndex);
			}

			String tagToWrite = COLUMN_SEPARATOR + "0x";

			if (byteStr == null || byteStr.isEmpty()) {
				tagToWrite += "??";
			} else {
				tagToWrite += byteStr.poll();
				tagToWrite += byteStr.poll();
			}
			tagToWrite += " ";
			if (typeName != null) {
				tagToWrite += typeName;
			}

			System.out.print(tagToWrite); // Embedded Message
			final int freeSpace = MAX_CHARS_PER_BYTE - tagToWrite.length(); // 23*Nbytes spaces
			for (int j = 0; j < freeSpace; ++j) {
				System.out.print(" ");
			}
			currentByteIndex++;
		}
		return currentByteIndex;
	}

	/**
	 * Print GRPC message (pretty)
	 * 
	 * @param msg
	 *            GRPC message
	 */
	public static synchronized void printMsg(final com.google.protobuf.GeneratedMessageV3 msg) {
		printMsg(msg, 0, new ConcurrentHashMap<String, String>());
	}

	/**
	 * Print grpc message Aux function
	 * 
	 * @param msg
	 *            GRPC message
	 * @param byteIndex
	 *            Current byte index
	 * @param tagsMap
	 *            Tags map
	 * @return Current byte index
	 */
	private static synchronized int printMsg(final com.google.protobuf.GeneratedMessageV3 msg, final int byteIndex,
			final Map<String, String> tagsMap) {

		boolean isFirst = false;
		int currentByteIndex = byteIndex;
		if (currentByteIndex == 0) {
			System.out.println("Message size : " + msg.getSerializedSize());
			isFirst = true;
		}

		final Queue<String> bytesAsStr = bytesToHex(msg.toByteArray());

		for (final Entry<FieldDescriptor, Object> fieldEntry : msg.getAllFields().entrySet()) {
			final FieldDescriptor fDescriptor = fieldEntry.getKey();

			// print value
			final Object fieldValue = fieldEntry.getValue();

			// Print value
			currentByteIndex = printValue(fieldValue, fDescriptor.getName(), bytesAsStr, currentByteIndex, tagsMap);

		}
		if (isFirst) {
			System.out.println();
			for (int j = 0; j < MAX_BYTES_PER_LINE; ++j) {
				for (int i = 0; i < MAX_CHARS_PER_BYTE; ++i) {
					System.out.print("=");
				}
			}
			System.out.println();
			final List<String> orderedList = new ArrayList<>(tagsMap.values());
			java.util.Collections.sort(orderedList);
			int curInColumn = 0;
			for (final String currentStr : orderedList) {
				for (final Entry<String, String> tagEntry : tagsMap.entrySet()) {
					if (tagEntry.getValue().equals(currentStr)) {
						final String toPrint = "|" + tagEntry.getValue() + ": " + tagEntry.getKey();
						System.out.print(toPrint);
						curInColumn++;
						if (curInColumn % 5 == 0) {
							System.out.println();
						} else {
							final int freeSpaces = MAX_CHARS_PER_BYTE * 3 - toPrint.length();
							for (int i = 0; i < freeSpaces; ++i) {
								System.out.print(" ");
							}
						}
						break;
					}
				}
			}
			System.out.println();
		}

		return currentByteIndex;
	}

	/**
	 * Print Field Tag
	 * 
	 * @param fieldDescriptorName
	 *            Field descriptor
	 * @param bytesAsStr
	 *            Hexadecimal bytes
	 * @param byteIndex
	 *            Current byte index
	 * @param tagsMap
	 *            tags map
	 * @return Current byte index
	 */
	private static synchronized int printFTTag(final String fieldDescriptorName, final Queue<String> bytesAsStr,
			final int byteIndex, final Map<String, String> tagsMap) {
		String tagName = tagsMap.get(fieldDescriptorName);
		if (tagName == null) {
			tagName = "FT" + tagsMap.size();
			tagsMap.put(fieldDescriptorName, tagName);
		}
		return printGrpcTagField(tagName, 0, bytesAsStr, byteIndex);
	}

	/**
	 * Print Embedded message Tag
	 * 
	 * @param fieldDescriptorName
	 *            Field descriptor
	 * @param bytesAsStr
	 *            Hexadecimal bytes
	 * @param byteIndex
	 *            Current byte index
	 * @param tagsMap
	 *            tags map
	 * @param serializedSize
	 *            size of the message
	 * @return Current byte index
	 */
	private static synchronized int printEMTag(final String fieldDescriptorName, final Queue<String> bytesAsStr,
			final int byteIndex, final Map<String, String> tagsMap, final int serializedSize) {
		String tagName = tagsMap.get(fieldDescriptorName);
		if (tagName == null) {
			tagName = "EM" + tagsMap.size();
			tagsMap.put(fieldDescriptorName, tagName);
		}
		return printGrpcTagField(tagName, serializedSize, bytesAsStr, byteIndex);
	}

	/**
	 * Get serialized size of field value provided
	 * 
	 * @param fieldValue
	 *            Field value
	 * @return serialized size
	 */
	@SuppressWarnings("rawtypes")
	private static synchronized int getSerializedSize(final Object fieldValue) {
		if (fieldValue instanceof Long) {
			return 8;
		} else if (fieldValue instanceof Integer) {
			return Math.max(1, getNumBytesNeededInVLQInt((Integer) fieldValue));
		} else if (fieldValue instanceof com.google.protobuf.GeneratedMessageV3) {
			final com.google.protobuf.GeneratedMessageV3 fieldMsg = (com.google.protobuf.GeneratedMessageV3) fieldValue;
			return Math.max(2, fieldMsg.getSerializedSize());
		} else if (fieldValue instanceof List) {
			int acumSize = 0;
			for (final Object fieldElement : (List) fieldValue) {
				acumSize += getSerializedSize(fieldElement);
			}
			return acumSize;
		} else if (fieldValue instanceof MapEntry) {
			final MapEntry entry = (MapEntry) fieldValue;
			return entry.getSerializedSize();
		} else if (fieldValue instanceof ByteString) {
			final ByteString bString = (ByteString) fieldValue;
			return bString.size();
		}
		return 0;
	}

	/**
	 * Print field value
	 * 
	 * @param fieldValue
	 *            Field value
	 * @param fieldDescriptorName
	 *            Field descriptor
	 * @param bytesAsStr
	 *            Hexadecimal bytes
	 * @param byteIndex
	 *            Current byte index
	 * @param tagsMap
	 *            tags map
	 * @return Current byte index
	 */
	@SuppressWarnings("rawtypes")
	private static synchronized int printValue(final Object fieldValue, final String fieldDescriptorName,
			final Queue<String> bytesAsStr, final int byteIndex, final Map<String, String> tagsMap) {

		int currentByteIndex = byteIndex;

		if (fieldValue instanceof Long) {
			final int size = getSerializedSize(fieldValue);
			currentByteIndex = printFTTag(fieldDescriptorName, bytesAsStr, currentByteIndex, tagsMap);
			currentByteIndex = printBytes(null, size, bytesAsStr, currentByteIndex);
		} else if (fieldValue instanceof Integer) {
			final int size = getSerializedSize(fieldValue);
			currentByteIndex = printFTTag(fieldDescriptorName, bytesAsStr, currentByteIndex, tagsMap);
			currentByteIndex = printBytes(null, size, bytesAsStr, currentByteIndex);
		} else if (fieldValue instanceof com.google.protobuf.GeneratedMessageV3) {
			final int size = getSerializedSize(fieldValue);
			final com.google.protobuf.GeneratedMessageV3 fieldMsg = (com.google.protobuf.GeneratedMessageV3) fieldValue;
			currentByteIndex = printEMTag(fieldDescriptorName, bytesAsStr, currentByteIndex, tagsMap, size);
			currentByteIndex = printMsg(fieldMsg, currentByteIndex, tagsMap);
		} else if (fieldValue instanceof List) {
			for (final Object fieldElement : (List) fieldValue) {
				currentByteIndex = printValue(fieldElement, "ListElement", bytesAsStr, currentByteIndex, tagsMap);
			}
		} else if (fieldValue instanceof MapEntry) {
			final int size = getSerializedSize(fieldValue);
			currentByteIndex = printEMTag("MapEntry", bytesAsStr, currentByteIndex, tagsMap, size);
			final MapEntry entry = (MapEntry) fieldValue;
			currentByteIndex = printValue(entry.getKey(), "MapKey", bytesAsStr, currentByteIndex, tagsMap);
			currentByteIndex = printValue(entry.getValue(), "MapValue", bytesAsStr, currentByteIndex, tagsMap);
		} else if (fieldValue instanceof ByteString) {
			final int size = getSerializedSize(fieldValue);
			currentByteIndex = printEMTag(fieldDescriptorName, bytesAsStr, currentByteIndex, tagsMap, size);
			currentByteIndex = printBytes(null, size, bytesAsStr, currentByteIndex);

		} else {
			System.err.println("Missing " + fieldValue.getClass().getName());
		}

		return currentByteIndex;

	}

	// ##**EXCEPTIONS UTILS**##

	/**
	 * Return Exception Infos for gRPC message
	 * 
	 * @param isExc
	 *            True if there is an exception
	 * @param serializedExc
	 *            The exception serialized in a DCBuffer
	 * @param excMessage
	 *            The formatted Message + Code + Server Stack of the Exception
	 * @return builder of proto ExceptionInfo message
	 */
	private static CommonMessages.ExceptionInfo getExcInfo(final boolean isExc, final ByteString serializedExc,
			final ByteString excMessage) {
		final CommonMessages.ExceptionInfo.Builder builder = CommonMessages.ExceptionInfo.newBuilder();

		builder.setIsException(isExc);
		if (serializedExc != null) {
			builder.setSerializedException(serializedExc);
		}
		if (excMessage != null) {
			builder.setExceptionMessage(excMessage);
		}

		return builder.build();
	}

	/**
	 * get Exception StackTrace
	 * 
	 * @param e
	 *            Exception
	 * @return string StackTrace
	 */
	private static String exceptionStacktraceToString(final Exception e) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);
		ps.close();
		return baos.toString();
	}

	/**
	 * get Serialized Exception Message to send
	 * 
	 * @param e
	 *            DataClayException
	 * @return message + StackTrace formatted
	 */
	private static ByteString getSerializedExceptionMessage(final DataClayException e) {

		final String stack = exceptionStacktraceToString(e);
		final String excMess = e.getLocalizedMessage();
		final String errorcode = e.getErrorcode().toString();

		final char[] chars = new char[146];
		Arrays.fill(chars, '-');
		final String s = new String(chars);

		final String joined = new StringJoiner("").add("\n").add(s).add("\n Error Message: ").add(excMess)
				.add("\n\n Error Code: ").add(errorcode).add("\n").add(s).add("\n Server StackTrace: ").add(stack)
				.add(s).toString();

		return ByteString.copyFromUtf8(joined);
	}

	/**
	 * Serialize Exception
	 * 
	 * @param dcEx
	 *            Exception to serialize
	 * @return Serialized exception
	 */
	public static ExceptionInfo serializeException(final Exception ex) {

		// wrap exception if needed
		DataClayException dcEx = null;
		if (ex instanceof DataClayException) {
			dcEx = (DataClayException) ex;
		} else {
			dcEx = new DataClayException(ex);
		}

		// Create response with exception
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==EXCEPTION==] Sending DataClayException GRPC response: ", ex);
		}
		final DataClayByteBuffer dcBuffer = SerializationLibUtils.newByteBuffer();
		byte[] serializedBytes = null;
		ExceptionInfo resp = null;
		try {
			// Serialize exception and set the ExcInfo message
			dcEx.serialize(dcBuffer, false, null, null, null, null);
			serializedBytes = dcBuffer.getArray();

			resp = (Utils.getExcInfo(true, ByteString.copyFrom(serializedBytes),
					Utils.getSerializedExceptionMessage(dcEx)));
		} finally {
			dcBuffer.release();

		}
		return resp;
	}

	/**
	 * Verifies if response message isException and if this is the case print and raise it properly
	 * 
	 * @param response
	 *            ExceptionInfo response message
	 */
	public static void checkIsExc(final ExceptionInfo response) {
		if (response.getIsException()) {

			final DataClayException dcEx = new DataClayException();
			dcEx.setExceptionMessage(response.getExceptionMessage().toStringUtf8());
			final ByteString serializedExceptionBytes = response.getSerializedException();
			if (serializedExceptionBytes.size() == 0) {
				// Python generic exception without bytes, just message
				throw dcEx;
			} else {
				final DataClayByteArray serializedException = new DataClayByteArray(serializedExceptionBytes);
				DataClayDeserializationLib.createBufferAndDeserialize(serializedException, dcEx, null, null, null);

				// WARNING: Currently only sending runtime exceptions
				final RuntimeException cause = (RuntimeException) dcEx.getCause();
				if (DEBUG_ENABLED) {
					LOGGER.debug("[==EXCEPTION==] Received DataClayException GRPC response: ", cause);
				}
				throw cause;

			}
		}
	}

}
