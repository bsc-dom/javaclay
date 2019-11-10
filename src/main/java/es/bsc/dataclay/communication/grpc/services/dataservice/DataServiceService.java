
/**
 * 
 */
package es.bsc.dataclay.communication.grpc.services.dataservice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.protobuf.ByteString;

import es.bsc.dataclay.communication.grpc.Utils;
import es.bsc.dataclay.communication.grpc.generated.dataservice.DataServiceGrpc;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ActivateTracingRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.CloseSessionInDSRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ConsolidateVersionRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployClassesRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployMetaClassesRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.EnrichClassRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.FederateRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.FilterObjectRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.FilterObjectResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFederatedObjectsRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFederatedObjectsResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetReferencedObjectIDsRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetReferencedObjectIDsResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetRetainedReferencesResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.InitBackendIDRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MakePersistentRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigratedObjects;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewMetaDataRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreObjectsRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UnfederateRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateObjectRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpsertObjectsRequest;
import es.bsc.dataclay.dataservice.DataService;
import es.bsc.dataclay.serialization.lib.ObjectWithDataParamOrReturn;
import es.bsc.dataclay.serialization.lib.SerializedParametersOrReturn;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.ids.StorageLocationID;
import es.bsc.dataclay.util.info.VersionInfo;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.metadataservice.MetaDataInfo;
import es.bsc.dataclay.util.management.metadataservice.StorageLocation;
import es.bsc.dataclay.util.structs.Tuple;
import es.bsc.dataclay.util.yaml.CommonYAML;
import io.grpc.stub.StreamObserver;

/**
 * Implements all DataService GRPC methods.
 */
public final class DataServiceService extends DataServiceGrpc.DataServiceImplBase {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger("communication.LogicModule.service");

	/** Actual DataService implementation. */
	private final DataService dataService;
	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/**
	 * Constructor
	 * 
	 * @param thedataService
	 *            Dataservice to process calls
	 */
	public DataServiceService(final DataService thedataService) {
		this.dataService = thedataService;
		LOGGER.info("Initialized DataServiceService");
	}

	@Override
	public void initBackendID(final InitBackendIDRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {
		try {
			dataService.initBackendID(Utils.getID(request.getBackendID()));
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void deployMetaClasses(final DeployMetaClassesRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {
		try {
			final Map<String, MetaClass> deploymentPack = new ConcurrentHashMap<>();
			for (final Entry<String, String> entry : request.getDeploymentPackMap().entrySet()) {
				final MetaClass mClass = (MetaClass) CommonYAML.getYamlObject().load(entry.getValue());
				deploymentPack.put(entry.getKey(), mClass);
			}
			dataService.deployMetaClasses(request.getNamespace(), deploymentPack);
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void deployClasses(final DeployClassesRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {
		try {
			final Map<Tuple<String, MetaClassID>, byte[]> newClasses = new ConcurrentHashMap<>();
			for (final Entry<String, ByteString> entry : request.getClassesToDeployMap().entrySet()) {
				final String className = entry.getKey();
				final MetaClassID classID = Utils.getID(request.getClassIdsMap().get(className));
				newClasses.put(new Tuple<>(className, classID), entry.getValue().toByteArray());
			}

			final Map<String, byte[]> aspects = new ConcurrentHashMap<>();
			for (final Entry<String, ByteString> entry : request.getAspectsMap().entrySet()) {
				aspects.put(entry.getKey(), entry.getValue().toByteArray());
			}

			final Map<String, byte[]> yamls = new ConcurrentHashMap<>();
			for (final Entry<String, ByteString> entry : request.getYamlsMap().entrySet()) {
				yamls.put(entry.getKey(), entry.getValue().toByteArray());
			}
			dataService.deployClasses(request.getNamespace(), newClasses, aspects, yamls);
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void enrichClass(final EnrichClassRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {

		try {
			dataService.enrichClass(request.getNamespace(), request.getClassname(),
					request.getClassToDeploy().toByteArray(), request.getAspect().toByteArray(),
					request.getYaml().toByteArray());
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	/**
	 * <pre>
	 * Storage Location.
	 * </pre>
	 */
	@Override
	public void newPersistentInstance(final NewPersistentInstanceRequest request,
			final io.grpc.stub.StreamObserver<NewPersistentInstanceResponse> responseObserver) {

		try {

			final Map<MetaClassID, byte[]> ifaceBitMaps = new ConcurrentHashMap<>();
			for (final Entry<String, ByteString> entry : request.getIfaceBitMapsMap().entrySet()) {
				ifaceBitMaps.put(Utils.getMetaClassIDFromUUID(entry.getKey()), entry.getValue().toByteArray());
			}

			SerializedParametersOrReturn params = null;
			if (request.hasParams()) {
				params = Utils.getParamsOrReturn(request.getParams());
			}
			final ObjectID oid = dataService.newPersistentInstance(Utils.getID(request.getSessionID()),
					Utils.getID(request.getClassID()), Utils.getID(request.getImplementationID()), ifaceBitMaps,
					params);

			final NewPersistentInstanceResponse resp = NewPersistentInstanceResponse.newBuilder()
					.setObjectID(Utils.getMsgID(oid)).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final NewPersistentInstanceResponse.Builder builder = NewPersistentInstanceResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final NewPersistentInstanceResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void storeObjects(final StoreObjectsRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {

		try {
			final List<ObjectWithDataParamOrReturn> objects = new ArrayList<>();
			for (final CommonMessages.ObjectWithDataParamOrReturn volParam : request.getObjectsList()) {
				objects.add(Utils.getObjectWithDataParamOrReturn(volParam));
			}
			final Set<ObjectID> idsWithAlias = new HashSet<>();
			if (request.getIdsWithAliasCount() > 0) {
				for (final CommonMessages.ObjectID idWithAlias : request.getIdsWithAliasList()) {
					idsWithAlias.add(Utils.getID(idWithAlias));
				}
			}

			dataService.storeObjects(Utils.getID(request.getSessionID()), objects, request.getMoving(), idsWithAlias);
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();
		} catch (final Exception ex) {
			LOGGER.debug("storeObjects DataClay native error", ex);
			final ExceptionInfo resp = Utils.serializeException(ex);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	@Override
	public void getCopyOfObject(final GetCopyOfObjectRequest request,
			final io.grpc.stub.StreamObserver<GetCopyOfObjectResponse> responseObserver) {

		try {

			final SerializedParametersOrReturn result = dataService.getCopyOfObject(Utils.getID(request.getSessionID()),
					Utils.getID(request.getObjectID()), request.getRecursive());

			final GetCopyOfObjectResponse.Builder builder = GetCopyOfObjectResponse.newBuilder();
			builder.setRet(Utils.getParamsOrReturn(result));

			final GetCopyOfObjectResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetCopyOfObjectResponse.Builder builder = GetCopyOfObjectResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetCopyOfObjectResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void updateObject(final UpdateObjectRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {

		try {

			dataService.updateObject(Utils.getID(request.getSessionID()), Utils.getID(request.getIntoObjectID()),
					Utils.getParamsOrReturn(request.getFromObject()));

			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception ex) {
			LOGGER.debug("putObject DataClay native error", ex);
			final ExceptionInfo resp = Utils.serializeException(ex);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getObjects(final GetObjectsRequest request,
			final io.grpc.stub.StreamObserver<GetObjectsResponse> responseObserver) {

		try {

			final Set<ObjectID> objectIDs = new HashSet<>();
			for (final CommonMessages.ObjectID oid : request.getObjectIDSList()) {
				objectIDs.add(Utils.getID(oid));
			}
			final List<ObjectWithDataParamOrReturn> result = dataService.getObjects(Utils.getID(request.getSessionID()),
					objectIDs, request.getRecursive(), request.getMoving());

			final GetObjectsResponse.Builder builder = GetObjectsResponse.newBuilder();
			for (final ObjectWithDataParamOrReturn entry : result) {
				builder.addObjects(Utils.getObjectWithDataParamOrReturn(entry));
			}

			final GetObjectsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetObjectsResponse.Builder builder = GetObjectsResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetObjectsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getReferencedObjectsIDs(final GetReferencedObjectIDsRequest request,
			final io.grpc.stub.StreamObserver<GetReferencedObjectIDsResponse> responseObserver) {
		try {
			final Set<ObjectID> objectIDs = new HashSet<>();
			for (final CommonMessages.ObjectID oid : request.getObjectIDSList()) {
				objectIDs.add(Utils.getID(oid));
			}
			final Set<ObjectID> result = dataService.getReferencedObjectsIDs(Utils.getID(request.getSessionID()),
					objectIDs);

			final GetReferencedObjectIDsResponse.Builder builder = GetReferencedObjectIDsResponse.newBuilder();
			for (final ObjectID entry : result) {
				builder.addObjectIDs(Utils.getMsgID(entry));
			}

			final GetReferencedObjectIDsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetReferencedObjectIDsResponse.Builder builder = GetReferencedObjectIDsResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetReferencedObjectIDsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getFederatedObjects(final GetFederatedObjectsRequest request,
			final io.grpc.stub.StreamObserver<GetFederatedObjectsResponse> responseObserver) {
		try {
			final Set<ObjectID> objectIDs = new HashSet<>();
			for (final CommonMessages.ObjectID oid : request.getObjectIDSList()) {
				objectIDs.add(Utils.getID(oid));
			}
			final List<ObjectWithDataParamOrReturn> result = dataService
					.getFederatedObjects(Utils.getID(request.getExtDataClayID()), objectIDs);

			final GetFederatedObjectsResponse.Builder builder = GetFederatedObjectsResponse.newBuilder();
			for (final ObjectWithDataParamOrReturn entry : result) {
				builder.addObjects(Utils.getObjectWithDataParamOrReturn(entry));
			}

			final GetFederatedObjectsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetFederatedObjectsResponse.Builder builder = GetFederatedObjectsResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetFederatedObjectsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void filterObject(final FilterObjectRequest request,
			final io.grpc.stub.StreamObserver<FilterObjectResponse> responseObserver) {
		try {
			final SerializedParametersOrReturn result = dataService.filterObject(Utils.getID(request.getSessionID()),
					Utils.getID(request.getObjectID()), request.getConditions());

			final FilterObjectResponse.Builder builder = FilterObjectResponse.newBuilder();
			if (result != null) {
				builder.setRet(Utils.getParamsOrReturn(result));
			}

			final FilterObjectResponse resp = builder.build();

			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final FilterObjectResponse.Builder builder = FilterObjectResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final FilterObjectResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void newMetaData(final NewMetaDataRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {

		try {
			final Map<ObjectID, MetaDataInfo> mdInfos = new ConcurrentHashMap<>();
			for (final Entry<String, String> entry : request.getMdInfosMap().entrySet()) {
				mdInfos.put(Utils.getObjectIDFromUUID(entry.getKey()),
						(MetaDataInfo) CommonYAML.getYamlObject().load(entry.getValue()));
			}
			dataService.newMetaData(mdInfos);
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	@Override
	public void newVersion(final NewVersionRequest request,
			final io.grpc.stub.StreamObserver<NewVersionResponse> responseObserver) {

		try {

			final Tuple<ObjectID, Map<ObjectID, ObjectID>> result = dataService.newVersion(
					Utils.getID(request.getSessionID()), Utils.getID(request.getObjectID()),
					(MetaDataInfo) CommonYAML.getYamlObject().load(request.getMetadataInfo()));

			final NewVersionResponse.Builder builder = NewVersionResponse.newBuilder();
			for (final Entry<ObjectID, ObjectID> entry : result.getSecond().entrySet()) {
				builder.putVersionedIDs(entry.getKey().getId().toString(), entry.getValue().getId().toString());
			}
			builder.setObjectID(Utils.getMsgID(result.getFirst()));

			final NewVersionResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final NewVersionResponse.Builder builder = NewVersionResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final NewVersionResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void consolidateVersion(final ConsolidateVersionRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {

		try {
			dataService.consolidateVersion(Utils.getID(request.getSessionID()),
					(VersionInfo) CommonYAML.getYamlObject().load(request.getVersionInfo()));
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void upsertObjects(final UpsertObjectsRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {

		try {
			final SessionID sessionID = Utils.getID(request.getSessionID());
			final List<ObjectWithDataParamOrReturn> objects = new ArrayList<>();
			for (final CommonMessages.ObjectWithDataParamOrReturn entry : request.getBytesUpdateList()) {
				objects.add(Utils.getObjectWithDataParamOrReturn(entry));
			}
			dataService.upsertObjects(sessionID, objects);
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void newReplica(final NewReplicaRequest request,
			final io.grpc.stub.StreamObserver<NewReplicaResponse> responseObserver) {
		try {

			final Set<ObjectID> result = dataService.newReplica(Utils.getID(request.getSessionID()),
					Utils.getID(request.getObjectID()), request.getRecursive());

			final NewReplicaResponse.Builder builder = NewReplicaResponse.newBuilder();
			for (final ObjectID oid : result) {
				builder.addReplicatedIDs(Utils.getMsgID(oid));
			}

			final NewReplicaResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final NewReplicaResponse.Builder builder = NewReplicaResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final NewReplicaResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void moveObjects(final MoveObjectsRequest request,
			final io.grpc.stub.StreamObserver<MoveObjectsResponse> responseObserver) {
		try {

			final Set<ObjectID> result = dataService.moveObjects(Utils.getID(request.getSessionID()),
					Utils.getID(request.getObjectID()), Utils.getID(request.getDestLocID()), request.getRecursive());
			final MoveObjectsResponse.Builder builder = MoveObjectsResponse.newBuilder();
			for (final ObjectID oid : result) {
				builder.addMovedObjects(Utils.getMsgID(oid));
			}

			final MoveObjectsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final MoveObjectsResponse.Builder builder = MoveObjectsResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final MoveObjectsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void removeObjects(final RemoveObjectsRequest request,
			final io.grpc.stub.StreamObserver<RemoveObjectsResponse> responseObserver) {
		try {

			final Set<ObjectID> objectIDs = new HashSet<>();
			for (final CommonMessages.ObjectID oid : request.getObjectIDsList()) {
				objectIDs.add(Utils.getID(oid));
			}
			final Map<ObjectID, ExecutionEnvironmentID> result = dataService.removeObjects(
					Utils.getID(request.getSessionID()), objectIDs, request.getRecursive(), request.getMoving(),
					Utils.getID(request.getNewHint()));
			final RemoveObjectsResponse.Builder builder = RemoveObjectsResponse.newBuilder();
			for (final Entry<ObjectID, ExecutionEnvironmentID> entry : result.entrySet()) {
				builder.putRemovedObjects(entry.getKey().getId().toString(), entry.getValue().getId().toString());
			}

			final RemoveObjectsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final RemoveObjectsResponse.Builder builder = RemoveObjectsResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final RemoveObjectsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void migrateObjectsToBackends(final MigrateObjectsRequest request,
			final io.grpc.stub.StreamObserver<MigrateObjectsResponse> responseObserver) {
		try {

			final Map<StorageLocationID, StorageLocation> backends = new ConcurrentHashMap<>();
			for (final Entry<String, String> entry : request.getDestStorageLocsMap().entrySet()) {
				backends.put(Utils.getStorageLocationIDFromUUID(entry.getKey()),
						(StorageLocation) CommonYAML.getYamlObject().load(entry.getValue()));
			}
			final Tuple<Map<StorageLocationID, Set<ObjectID>>, Set<ObjectID>> result = dataService
					.migrateObjectsToBackends(backends);

			final MigrateObjectsResponse.Builder builder = MigrateObjectsResponse.newBuilder();
			for (final Entry<StorageLocationID, Set<ObjectID>> entry : result.getFirst().entrySet()) {
				final MigratedObjects.Builder migratedObjsBuilder = MigratedObjects.newBuilder();
				for (final ObjectID oid : entry.getValue()) {
					migratedObjsBuilder.addObjs(Utils.getMsgID(oid));
				}
				final MigratedObjects mObjs = migratedObjsBuilder.build();
				builder.putMigratedObjs(entry.getKey().getId().toString(), mObjs);
			}

			final MigratedObjects.Builder nonmigratedObjsBuilder = MigratedObjects.newBuilder();
			for (final ObjectID oid : result.getSecond()) {
				nonmigratedObjsBuilder.addObjs(Utils.getMsgID(oid));
			}
			final MigratedObjects nonmObjs = nonmigratedObjsBuilder.build();
			builder.setNonMigratedObjs(nonmObjs);

			final MigrateObjectsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final MigrateObjectsResponse.Builder builder = MigrateObjectsResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final MigrateObjectsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getClassIDFromObjectInMemory(final GetClassIDFromObjectInMemoryRequest request,
			final io.grpc.stub.StreamObserver<GetClassIDFromObjectInMemoryResponse> responseObserver) {
		try {

			final ObjectID oid = Utils.getID(request.getObjectID());

			final MetaClassID result = dataService.getClassIDFromObjectInMemory(oid);

			final GetClassIDFromObjectInMemoryResponse.Builder builder = GetClassIDFromObjectInMemoryResponse
					.newBuilder();
			if (result != null) {
				builder.setClassID(Utils.getMsgID(result));
			}
			final GetClassIDFromObjectInMemoryResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetClassIDFromObjectInMemoryResponse.Builder builder = GetClassIDFromObjectInMemoryResponse
					.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetClassIDFromObjectInMemoryResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void makePersistent(final MakePersistentRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {
		LOGGER.debug("[==Serialization==] Received request for make persistent ");
		try {
			SerializedParametersOrReturn params = null;
			if (request.hasParams()) {
				params = Utils.getParamsOrReturn(request.getParams());
			}
			if (DEBUG_ENABLED) {
				LOGGER.debug("[==Serialization==] Received request for make persistent with params:" + params);
			}
			dataService.makePersistent(Utils.getID(request.getSessionID()), params);

			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			e.printStackTrace();
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void federate(final FederateRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {
		try {

			SerializedParametersOrReturn params = null;
			if (request.hasParams()) {
				params = Utils.getParamsOrReturn(request.getParams());
			}

			dataService.federate(Utils.getID(request.getSessionID()), params);

			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void unfederate(final UnfederateRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {
		try {
			final Set<ObjectID> objectIDs = new HashSet<>();
			for (final CommonMessages.ObjectID oid : request.getObjectIDsList()) {
				objectIDs.add(Utils.getID(oid));
			}
			dataService.unfederate(Utils.getID(request.getSessionID()), objectIDs);
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void executeImplementation(final ExecuteImplementationRequest request,
			final io.grpc.stub.StreamObserver<ExecuteImplementationResponse> responseObserver) {
		try {

			SerializedParametersOrReturn params = null;
			if (request.hasParams()) {
				params = Utils.getParamsOrReturn(request.getParams());
			}

			final ObjectID objectID = Utils.getID(request.getObjectID());
			final ImplementationID implID = Utils.getID(request.getImplementationID());

			final SerializedParametersOrReturn result = dataService.executeImplementation(objectID, implID, params,
					Utils.getID(request.getSessionID()));

			final ExecuteImplementationResponse.Builder builder = ExecuteImplementationResponse.newBuilder();
			if (result != null) {
				builder.setRet(Utils.getParamsOrReturn(result));
			}

			final ExecuteImplementationResponse resp = builder.build();

			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			LOGGER.debug("executeImplementation DataClay native error", e);
			final ExecuteImplementationResponse.Builder builder = ExecuteImplementationResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final ExecuteImplementationResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void closeSessionInDS(final CloseSessionInDSRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {

		try {
			dataService.closeSessionInDS(Utils.getID(request.getSessionID()));
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void updateRefs(
			final es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateRefsRequest request,
			final io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
		try {
			final Map<ObjectID, Integer> updateRefs = new ConcurrentHashMap<>();
			if (request.getRefsToUpdateCount() > 0) {
				for (final Entry<String, Integer> curEntry : request.getRefsToUpdateMap().entrySet()) {
					updateRefs.put(Utils.getObjectIDFromUUID(curEntry.getKey()), curEntry.getValue());
				}
			}

			dataService.updateRefs(updateRefs);
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getRetainedReferences(
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
			final io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetRetainedReferencesResponse> responseObserver) {
		try {
			final Set<ObjectID> result = dataService.getRetainedReferences();

			final GetRetainedReferencesResponse.Builder builder = GetRetainedReferencesResponse.newBuilder();
			for (final ObjectID oid : result) {
				builder.addRetainedReferences(Utils.getMsgID(oid));
			}

			final GetRetainedReferencesResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception ex) {
			LOGGER.debug("getRetainedReferences error", ex);

			final GetRetainedReferencesResponse.Builder builder = GetRetainedReferencesResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(ex));
			final GetRetainedReferencesResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	/**
	 * <pre>
	 * Others.
	 * </pre>
	 */
	@Override
	public void cleanExecutionClassDirectory(final CommonMessages.EmptyMessage request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {
		try {
			dataService.cleanExecutionClassDirectory();
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void closeDbHandler(final CommonMessages.EmptyMessage request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {
		try {
			dataService.closeDbHandler();
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void shutDown(final CommonMessages.EmptyMessage request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {
		try {
			dataService.shutDown();
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void disconnectFromOthers(final CommonMessages.EmptyMessage request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {
		try {
			dataService.disconnectFromOthers();
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	/**
	 * <pre>
	 * Paraver.
	 * </pre>
	 */
	@Override
	public void cleanCaches(final CommonMessages.EmptyMessage request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {
		try {
			dataService.cleanCaches();
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void activateTracing(final ActivateTracingRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {

		final int currentAvailableTaskID = request.getTaskid();
		try {
			dataService.activateTracing(currentAvailableTaskID);
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void deactivateTracing(final CommonMessages.EmptyMessage request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {
		try {
			dataService.deactivateTracing();
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}
	
	@Override
	public void getTraces(final EmptyMessage request, final StreamObserver<GetTracesResponse> responseObserver) {
		try {
			final Map<String, byte[]> traces = dataService.getTraces();
			final GetTracesResponse.Builder builder = GetTracesResponse.newBuilder();
			for (final Entry<String, byte[]> entry : traces.entrySet()) {
				builder.putTraces(entry.getKey(), ByteString.copyFrom(entry.getValue()));
			}

			final GetTracesResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetTracesResponse.Builder builder = GetTracesResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetTracesResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void registerPendingObjects(final CommonMessages.EmptyMessage request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {

		try {
			dataService.registerPendingObjects();
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}
	
	@Override
	public void exists(final DataserviceMessages.ExistsRequest request,
			final io.grpc.stub.StreamObserver<DataserviceMessages.ExistsResponse> responseObserver) {
		try {
			final ObjectID objectID = Utils.getID(request.getObjectID());
			final boolean result = dataService.existsInEE(objectID);
			final DataserviceMessages.ExistsResponse.Builder builder = DataserviceMessages.ExistsResponse.newBuilder();
			builder.setExists(result);
			final DataserviceMessages.ExistsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final DataserviceMessages.ExistsResponse.Builder builder = DataserviceMessages.ExistsResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final DataserviceMessages.ExistsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}
	
	@Override
	public void existsInDB(final DataserviceMessages.ExistsInDBRequest request,
			final io.grpc.stub.StreamObserver<DataserviceMessages.ExistsInDBResponse> responseObserver) {
		try {
			final ObjectID objectID = Utils.getID(request.getObjectID());

			final boolean result = dataService.exists(objectID);
			
			final DataserviceMessages.ExistsInDBResponse.Builder builder = DataserviceMessages.ExistsInDBResponse.newBuilder();
			builder.setExists(result);
			final DataserviceMessages.ExistsInDBResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final DataserviceMessages.ExistsInDBResponse.Builder builder = DataserviceMessages.ExistsInDBResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final DataserviceMessages.ExistsInDBResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void storeToDB(final DataserviceMessages.StoreToDBRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {
		try {
			final ExecutionEnvironmentID eeID = Utils.getID(request.getExecutionEnvironmentID());
			final ObjectID objectID = Utils.getID(request.getObjectID());
			final byte[] bytes = request.getObjBytes().toByteArray();

			dataService.store(eeID, objectID, bytes);
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getFromDB(final DataserviceMessages.GetFromDBRequest request,
			final io.grpc.stub.StreamObserver<DataserviceMessages.GetFromDBResponse> responseObserver) {
		try {
			final ObjectID objectID = Utils.getID(request.getObjectID());
			final ExecutionEnvironmentID eeID = Utils.getID(request.getExecutionEnvironmentID());

			final byte[] getResult = dataService.get(eeID, objectID);
			final ByteString result = ByteString.copyFrom(getResult);

			final GetFromDBResponse.Builder builder = GetFromDBResponse.newBuilder();
			if (result != null) {
				builder.setObjBytes((result));
			}

			final GetFromDBResponse resp = builder.build();

			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetFromDBResponse.Builder builder = GetFromDBResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetFromDBResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void updateToDB(final DataserviceMessages.UpdateToDBRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {
		try {
			final ExecutionEnvironmentID eeID = Utils.getID(request.getExecutionEnvironmentID());
			final ObjectID objectID = Utils.getID(request.getObjectID());
			final byte[] bytes = request.getObjBytes().toByteArray();
			final boolean dirty = request.getDirty();
			dataService.update(eeID, objectID, bytes, dirty);
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void deleteToDB(final DataserviceMessages.DeleteToDBRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {
		try {
			final ExecutionEnvironmentID eeID = Utils.getID(request.getExecutionEnvironmentID());
			final ObjectID objectID = Utils.getID(request.getObjectID());

			dataService.delete(eeID, objectID);
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void associateExecutionEnvironment(final DataserviceMessages.AssociateExecutionEnvironmentRequest request,
			final io.grpc.stub.StreamObserver<CommonMessages.ExceptionInfo> responseObserver) {
		try {
			dataService.associateExecutionEnvironment(Utils.getID(request.getExecutionEnvironmentID()));
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}
}
