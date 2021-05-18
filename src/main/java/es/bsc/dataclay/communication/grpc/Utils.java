
/**
 * 
 */
package es.bsc.dataclay.communication.grpc;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import es.bsc.dataclay.util.ids.*;
import es.bsc.dataclay.util.management.metadataservice.*;
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
	 * Get string representation of ID
	 * @param id ID to get representation from
	 * @return String represation or null if id is null
	 */
	public static String getMsgID(final ID id) {
		if (id == null) {
			return "";
		} else {
			return id.toString();
		}
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
	public static AccountID getAccountID(final String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new AccountID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get ContractID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return ContractID
	 */
	public static ContractID getContractID(final String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new ContractID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get CredentialID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return CredentialID
	 */
	public static CredentialID getCredentialID(String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new CredentialID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get DataContractID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return DataContractID
	 */
	public static DataContractID getDataContractID(String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new DataContractID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get DataSetID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return DataSetID
	 */
	public static DataSetID getDataSetID(final String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new DataSetID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get NamespaceID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return NamespaceID
	 */
	public static NamespaceID getNamespaceID(final String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new NamespaceID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get ECAID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return ECAID
	 */
	public static ECAID getECAID(final String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new ECAID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get EventMessageID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return EventMessageID
	 */
	public static EventMessageID getEventMessageID(final String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new EventMessageID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get EventObjsMeetConditionID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return EventObjsMeetConditionID
	 */
	public static EventObjsMeetConditionID getEventObjsMeetConditionID(String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new EventObjsMeetConditionID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get ExecutionEnvironmentID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return ExecutionEnvironmentID
	 */
	public static ExecutionEnvironmentID getExecutionEnvironmentID(String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new ExecutionEnvironmentID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get ImplementationID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return ImplementationID
	 */
	public static ImplementationID getImplementationID(String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new ImplementationID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get InterfaceID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return InterfaceID
	 */
	public static InterfaceID getInterfaceID(String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new InterfaceID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get MetaClassID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return MetaClassID
	 */
	public static MetaClassID getMetaClassID(String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new MetaClassID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get ObjectID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return ObjectID
	 */
	public static ObjectID getObjectID(final String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new ObjectID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get OperationID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return OperationID
	 */
	public static OperationID getOperationID(String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new OperationID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get PropertyID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return PropertyID
	 */
	public static PropertyID getPropertyID(final String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new PropertyID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get QualitativeRegistryID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return QualitativeRegistryID
	 */
	public static QualitativeRegistryID getQualitativeRegistryID(String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new QualitativeRegistryID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get ResourceID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return ResourceID
	 */
	public static ResourceID getResourceID(final String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new ResourceID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get SessionID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return SessionID
	 */
	public static SessionID getSessionID(final String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new SessionID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get StorageLocationID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return StorageLocationID
	 */
	public static StorageLocationID getStorageLocationID(String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new StorageLocationID(UUID.fromString(idMsg));
		}
	}

	/**
	 * Get DataClayID from Protobuf ID
	 * 
	 * @param idMsg
	 *            Message ID
	 * @return DataClayID
	 */
	public static DataClayInstanceID getDataClayInstanceID(String idMsg) {
		if (idMsg == null || idMsg.isEmpty()) {
			return null;
		} else {
			return new DataClayInstanceID(UUID.fromString(idMsg));
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
					final String oidIDmsg = Utils.getMsgID(oidEntry.getValue());
					metadataBuilder.putOids(oidEntry.getKey(), oidIDmsg);
					// System.err.println("OID entry bytes = " + Reflector.bytesToHex(
					// oidIDmsg.toByteArray()));
				}
			}
		}

		if (metadata.getClassIDs() != null) {
			for (final Entry<Integer, MetaClassID> classIDEntry : metadata.getClassIDs().entrySet()) {
				if (classIDEntry.getValue() != null) {
					final String classIDmsg = Utils.getMsgID(classIDEntry.getValue());
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
		if (metadata.getReplicaLocations() != null) {
			for (final ExecutionEnvironmentID loc : metadata.getReplicaLocations()) {
				if (loc != null) {
					metadataBuilder.addReplicaLocations(Utils.getMsgID(loc));
				}
			}
		}
		if (metadata.getAlias() != null) {
			metadataBuilder.setAlias(metadata.getAlias());
		}
		metadataBuilder.setRootLocation(Utils.getMsgID(metadata.getRootLocation()));
		metadataBuilder.setOriginLocation(Utils.getMsgID(metadata.getOriginLocation()));
		metadataBuilder.setIsReadOnly(metadata.getIsReadOnly());
		metadataBuilder.setOrigObjectID(Utils.getMsgID(metadata.getOriginalObjectID()));
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
			for (final Entry<Integer, String> classIDEntry : msg.getClassidsMap().entrySet()) {
				if (classIDEntry.getKey() != null && classIDEntry.getValue() != null) {
					final MetaClassID classID = Utils.getMetaClassID(classIDEntry.getValue());
					if (classID != null) {
						classIDs.put(classIDEntry.getKey(), classID);
					}
				}
			}
		}

		if (msg.getOidsMap() != null) {
			for (final Entry<Integer, String> entry : msg.getOidsMap().entrySet()) {
				if (entry.getKey() != null && entry.getValue() != null) {
					oids.put(entry.getKey(), Utils.getObjectID(entry.getValue()));
				}
			}
		}

		if (msg.getHintsMap() != null) {
			for (final Entry<Integer, String> entry : msg.getHintsMap().entrySet()) {
				if (entry.getKey() != null && entry.getValue() != null) {
					hints.put(entry.getKey(), Utils.getExecutionEnvironmentID(entry.getValue()));
				}
			}
		}

		final int numRefs = msg.getNumRefs();
		final ObjectID origObjectID = Utils.getObjectID(msg.getOrigObjectID());
		final ExecutionEnvironmentID rootLocation = Utils.getExecutionEnvironmentID(msg.getRootLocation());
		final ExecutionEnvironmentID origLocation = Utils.getExecutionEnvironmentID(msg.getOriginLocation());
		final Set<ExecutionEnvironmentID> replicaLocations = ConcurrentHashMap.newKeySet();
		for (String eeID : msg.getReplicaLocationsList()) {
			replicaLocations.add(Utils.getExecutionEnvironmentID(eeID));
		}


		return new DataClayObjectMetaData(msg.getAlias(), msg.getIsReadOnly(), oids, classIDs, hints, numRefs,
				origObjectID, rootLocation, origLocation, replicaLocations);

	}

	/**
	 * Return a message for GRPC call
	 *
	 * @param object object to convert
	 * @return the resulting message
	 */
	public static CommonMessages.MetaDataInfo getMetaDataInfo(
			final MetaDataInfo object) {
		if (object == null) {
			return CommonMessages.MetaDataInfo.getDefaultInstance();
		}
		CommonMessages.MetaDataInfo.Builder builder = CommonMessages.MetaDataInfo.newBuilder();
		builder.setObjectID(Utils.getMsgID(object.getDataClayID()));
		builder.setIsReadOnly(object.getIsReadOnly());
		builder.setDatasetID(Utils.getMsgID(object.getDatasetID()));
		builder.setMetaclassID(Utils.getMsgID(object.getMetaclassID()));
		builder.setOwnerID(Utils.getMsgID(object.getOwnerID()));
		if (object.getAlias() != null) {
			builder.setAlias(object.getAlias());
		}
		for (ExecutionEnvironmentID loc : object.getLocations()) {
			builder.addLocations(Utils.getMsgID(loc));
		}
		return builder.build();
	}

	/**
	 * Return an object FROM grpc message
	 *
	 * @param msg object to convert
	 * @return the resulting object
	 */
	public static MetaDataInfo getMetaDataInfo(
			final CommonMessages.MetaDataInfo msg) {

		if (msg.equals(CommonMessages.MetaDataInfo.getDefaultInstance())) {
			return null;
		}

		Set<ExecutionEnvironmentID> locs = new HashSet<>();
		for (String eeID : msg.getLocationsList()) {
			locs.add(Utils.getExecutionEnvironmentID(eeID));
		}
		MetaDataInfo mdInfo = new MetaDataInfo(
				Utils.getObjectID(msg.getObjectID()),
				Utils.getDataSetID(msg.getDatasetID()),
				Utils.getMetaClassID(msg.getMetaclassID()),
				msg.getIsReadOnly(),
				locs, msg.getAlias(), Utils.getAccountID(msg.getOwnerID()));
		return mdInfo;
	}

	/**
	 * Return a message for GRPC call
	 *
	 * @param object object to convert
	 * @return the resulting message
	 */
	public static CommonMessages.ExecutionEnvironmentInfo getExecutionEnvironment(
			final ExecutionEnvironment object) {
		CommonMessages.ExecutionEnvironmentInfo.Builder builder = CommonMessages.ExecutionEnvironmentInfo.newBuilder();
		builder.setId(Utils.getMsgID(object.getDataClayID()));
		builder.setHostname(object.getHostname());
		builder.setName(object.getName());
		builder.setPort(object.getPort());
		builder.setDataClayInstanceID(Utils.getMsgID(object.getDataClayInstanceID()));
		builder.setLanguage(object.getLang());
		return builder.build();
	}

	/**
	 * Return an object FROM grpc message
	 *
	 * @param msg object to convert
	 * @return the resulting object
	 */
	public static ExecutionEnvironment getExecutionEnvironment(
			final CommonMessages.ExecutionEnvironmentInfo msg) {
		ExecutionEnvironment eeLoc = new ExecutionEnvironment(
				msg.getHostname(),
				msg.getName(),
				msg.getPort(),
				msg.getLanguage(),
				Utils.getDataClayInstanceID(msg.getDataClayInstanceID()));
		eeLoc.setDataClayID(Utils.getExecutionEnvironmentID(msg.getId()));
		return eeLoc;
	}

	/**
	 * Return a message for GRPC call
	 *
	 * @param object object to convert
	 * @return the resulting message
	 */
	public static CommonMessages.StorageLocationInfo getStorageLocation(
			final StorageLocation object) {
		CommonMessages.StorageLocationInfo.Builder builder = CommonMessages.StorageLocationInfo.newBuilder();
		builder.setId(Utils.getMsgID(object.getDataClayID()));
		builder.setHostname(object.getHostname());
		builder.setName(object.getName());
		builder.setPort(object.getStorageTCPPort());
		return builder.build();
	}

	/**
	 * Return an object FROM grpc message
	 *
	 * @param msg object to convert
	 * @return the resulting object
	 */
	public static StorageLocation getStorageLocation(
			final CommonMessages.StorageLocationInfo msg) {
		StorageLocation stLoc = new StorageLocation(
				msg.getHostname(),
				msg.getName(),
				msg.getPort());
		stLoc.setDataClayID(Utils.getStorageLocationID(msg.getId()));
		return stLoc;
	}

	/**
	 * Return a message for GRPC call
	 *
	 * @param dataClayInstance object to convert
	 * @return the resulting message
	 */
	public static CommonMessages.DataClayInstance getDataClayInstance(
			final DataClayInstance dataClayInstance) {
		CommonMessages.DataClayInstance.Builder builder = CommonMessages.DataClayInstance.newBuilder();
		builder.setId(Utils.getMsgID(dataClayInstance.getDcID()));
		builder.addAllHosts(dataClayInstance.getHosts());
		builder.addAllPorts(dataClayInstance.getPorts());
		return builder.build();
	}

	/**
	 * Return an object FROM grpc message
	 *
	 * @param dataClayInstanceMsg object to convert
	 * @return the resulting message
	 */
	public static DataClayInstance getDataClayInstance(
			final CommonMessages.DataClayInstance dataClayInstanceMsg) {
		String[] hosts = new String[dataClayInstanceMsg.getHostsCount()];
		for (int i = 0; i < hosts.length; ++i) {
			hosts[i] = dataClayInstanceMsg.getHosts(i);
		}
		Integer[] ports = new Integer[dataClayInstanceMsg.getPortsCount()];
		for (int i = 0; i < hosts.length; ++i) {
			ports[i] = dataClayInstanceMsg.getPorts(i);
		}
		return new DataClayInstance(Utils.getDataClayInstanceID(dataClayInstanceMsg.getId()),
				hosts, ports);
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
		final String classIDmsg = Utils.getMsgID(volParamOrRet.getClassID());
		volBuilder.setClassid(classIDmsg);
		// System.err.println("ObjwData class ID size : " +
		// classIDmsg.getSerializedSize());

		final String objectIDmsg = Utils.getMsgID(volParamOrRet.getObjectID());
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
		final ObjectID oid = Utils.getObjectID(volParamOrRet.getOid());
		final MetaClassID classid = Utils.getMetaClassID(volParamOrRet.getClassid());
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
		hint = Utils.getExecutionEnvironmentID(paramOrRet.getHint());
		classID = Utils.getMetaClassID(paramOrRet.getClassID());
		extDataClayID = Utils.getDataClayInstanceID(paramOrRet.getExtDataClayID());


		return new PersistentParamOrReturn(Utils.getObjectID(paramOrRet.getOid()), hint, classID, extDataClayID);
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
	 * @param ex
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
			LOGGER.debug("[==EXCEPTION==] Sending exception {}. Activate trace to see stack trace", ex.getClass().getName());
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
					LOGGER.debug("[==EXCEPTION==] Sending exception {}. Activate trace to see stack trace", cause.getMessage());
					LOGGER.trace("[==EXCEPTION==] Received DataClayException GRPC response: ", cause);
				}
				throw cause;

			}
		}
	}

}
