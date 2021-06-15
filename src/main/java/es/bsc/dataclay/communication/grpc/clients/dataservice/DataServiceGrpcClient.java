
/**
 * 
 */
package es.bsc.dataclay.communication.grpc.clients.dataservice;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.communication.grpc.generated.logicmodule.LogicModuleGrpc;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages;
import es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.protobuf.ByteString;

import es.bsc.dataclay.communication.grpc.Utils;
import es.bsc.dataclay.communication.grpc.clients.CommonGrpcClient;
import es.bsc.dataclay.communication.grpc.generated.dataservice.DataServiceGrpc;
import es.bsc.dataclay.communication.grpc.generated.dataservice.DataServiceGrpc.DataServiceBlockingStub;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ActivateTracingRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.AssociateExecutionEnvironmentRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.CloseSessionInDSRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ConsolidateVersionRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeleteToDBRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployClassesRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployMetaClassesRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.EnrichClassRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.FederateRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetRetainedReferencesResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.InitBackendIDRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MakePersistentRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigratedObjects;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsResponse;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreObjectsRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreToDBRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UnfederateRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateObjectRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateToDBRequest;
import es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpsertObjectsRequest;
import es.bsc.dataclay.dataservice.api.DataServiceAPI;
import es.bsc.dataclay.serialization.lib.ObjectWithDataParamOrReturn;
import es.bsc.dataclay.serialization.lib.SerializedParametersOrReturn;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
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
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.MetadataUtils;

/**
 * Client code that makes gRPC calls to the server.
 */
@SuppressWarnings("all")
public final class DataServiceGrpcClient implements DataServiceAPI {

	/** Number of max seconds to shutdown. */
	private static final int SECONDS_SHUTDOWN = 5;

	/** Sleeping time while there is asynchronous request to wait for. */
	private static final int ASYNC_WAIT_MILLIS = 1000;

	/** Logger. */
	private final Logger logger;

	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Channel. */
	private final ManagedChannel channel;

	/** Blocking stub. */
	private DataServiceBlockingStub blockingStub;

	/** Number of asynchronous requests send. */
	private final AtomicInteger asyncReqSend = new AtomicInteger(0);

	/** Number of asynchronous requests received. */
	private final AtomicInteger asyncReqReceived = new AtomicInteger(0);

	/**
	 * Construct client for accessing server at {@code host:port}.
	 * 
	 * @param originHostName
	 *            DS host name
	 * @param host
	 *            Host
	 * @param port
	 *            Port
	 */
	public DataServiceGrpcClient(final String originHostName, final String host, final int port) {
		// Logger.getLogger("io.grpc").setLevel(Level.OFF);
		logger = LogManager.getLogger("grpc.client.dataservice");


		logger.info("Creating new connection to " + host + ":" + port);


		NettyChannelBuilder chBuilder = null;
		String serviceAlias = null;
		// Check if paths to certificates are defined
		if (Configuration.Flags.SSL_CLIENT_TRUSTED_CERTIFICATES.getStringValue() != null
				|| Configuration.Flags.SSL_CLIENT_CERTIFICATE.getStringValue() != null
				|| Configuration.Flags.SSL_CLIENT_KEY.getStringValue() != null) {
			try {

				if (port != 443) {
					serviceAlias = String.valueOf(port);
					logger.info("SSL configured: Changing " + host + ":" + port + " to " + host + ":443");
					chBuilder = NettyChannelBuilder.forAddress(host,443);
				} else {
					serviceAlias = Configuration.Flags.SSL_TARGET_DS_ALIAS.getStringValue();
					chBuilder = NettyChannelBuilder.forAddress(host, port);
				}


				chBuilder.useTransportSecurity();
				final SslContextBuilder sslContextBuilder = GrpcSslContexts.forClient();
				if (Configuration.Flags.SSL_CLIENT_TRUSTED_CERTIFICATES.getStringValue() != null) {
					sslContextBuilder.trustManager(new File(Configuration.Flags.SSL_CLIENT_TRUSTED_CERTIFICATES.getStringValue()));
				}
				if (Configuration.Flags.SSL_CLIENT_CERTIFICATE.getStringValue() != null
						&& Configuration.Flags.SSL_CLIENT_KEY.getStringValue() != null) {
					sslContextBuilder.keyManager(new File(Configuration.Flags.SSL_CLIENT_CERTIFICATE.getStringValue()),
							new File(Configuration.Flags.SSL_CLIENT_KEY.getStringValue()));
				}
				final SslContext sslContext = sslContextBuilder.build();
				chBuilder.overrideAuthority(Configuration.Flags.SSL_TARGET_AUTHORITY.getStringValue());
				chBuilder.sslContext(sslContext);
			} catch (final Exception e) {
				e.printStackTrace();
			}
			logger.info("SSL configured: using SSL_CLIENT_TRUSTED_CERTIFICATES located at " + Configuration.Flags.SSL_CLIENT_TRUSTED_CERTIFICATES.getStringValue());
			logger.info("SSL configured: using SSL_CLIENT_CERTIFICATE located at " + Configuration.Flags.SSL_CLIENT_CERTIFICATE.getStringValue());
			logger.info("SSL configured: using SSL_CLIENT_KEY located at " + Configuration.Flags.SSL_CLIENT_KEY.getStringValue());

		} else {
			chBuilder = NettyChannelBuilder.forAddress(host, port);
			chBuilder.usePlaintext();
			//chBuilder.negotiationType(NegotiationType.PLAINTEXT);
			logger.info("Not using SSL");

		}

		if (Configuration.Flags.GRPC_USE_FORK_JOIN_POOL.getBooleanValue()) {
			chBuilder.executor(new ForkJoinPool());
		}
		chBuilder.maxInboundMetadataSize(Integer.MAX_VALUE);
		chBuilder.maxHeaderListSize(Integer.MAX_VALUE);
		chBuilder.maxInboundMessageSize(Integer.MAX_VALUE);

		channel = chBuilder.build();

		// Capture the metadata exchange
		if (Configuration.Flags.SSL_CLIENT_TRUSTED_CERTIFICATES.getStringValue() != null
				|| Configuration.Flags.SSL_CLIENT_CERTIFICATE.getStringValue() != null
				|| Configuration.Flags.SSL_CLIENT_KEY.getStringValue() != null) {
			final Metadata fixedHeaders = new Metadata();
			logger.info("SSL configured: added rule: " + CommonGrpcClient.SERVICE_ALIAS_HEADER_KEY + " = " + port);
			fixedHeaders.put(CommonGrpcClient.SERVICE_ALIAS_HEADER_KEY, serviceAlias);
			blockingStub = MetadataUtils.attachHeaders(DataServiceGrpc.newBlockingStub(channel), fixedHeaders);
		} else {
			blockingStub = DataServiceGrpc.newBlockingStub(channel).withMaxOutboundMessageSize(Integer.MAX_VALUE)
					.withMaxInboundMessageSize(Integer.MAX_VALUE);
		}



	}

	/**
	 * Shutdown client
	 * 
	 * @throws InterruptedException
	 *             if some exception occurs
	 */
	public void shutdown() throws InterruptedException {
		channel.shutdownNow();
		while (!channel.awaitTermination(SECONDS_SHUTDOWN, TimeUnit.SECONDS)) { 
			System.out.println("[grpc] Waiting for channel to close...");
		}
	}

	@Override
	public void cleanCaches() {
		ExceptionInfo response;
		try {

			response = blockingStub.cleanCaches(EmptyMessage.newBuilder().build());

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public void registerPendingObjects() {
		ExceptionInfo response;
		try {

			response = blockingStub.registerPendingObjects(EmptyMessage.newBuilder().build());
		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public void activateTracing(final int currentAvailableTaskID) {
		final ActivateTracingRequest request = ActivateTracingRequest.newBuilder().setTaskid(currentAvailableTaskID)
				.build();
		ExceptionInfo response;
		try {

			response = blockingStub.activateTracing(request);

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public void deactivateTracing() {
		ExceptionInfo response = null;
		try {

			response = blockingStub.deactivateTracing(EmptyMessage.newBuilder().build());

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}
	
	@Override
	public Map<String, byte[]> getTraces() { 
		final GetTracesResponse response;
		try {
			response = blockingStub.getTraces(EmptyMessage.newBuilder().build());
		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response.getExcInfo());
		final Map<String, byte[]> result = new HashMap<>();
		for (final Entry<String, ByteString> entry : response.getTracesMap().entrySet()) {
			final byte[] objBytes = entry.getValue().toByteArray();
			result.put(entry.getKey(), objBytes);
		}
		return result;
	}

	@Override
	public void initBackendID(final StorageLocationID backendID) {
		final InitBackendIDRequest request = InitBackendIDRequest.newBuilder().setBackendID(Utils.getMsgID(backendID))
				.build();
		ExceptionInfo response;
		try {

			response = blockingStub.initBackendID(request);

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public void deployMetaClasses(final String namespaceName, final Map<String, MetaClass> deploymentPack) {
		try {
			final DeployMetaClassesRequest.Builder builder = DeployMetaClassesRequest.newBuilder();
			builder.setNamespace(namespaceName);
			for (final Entry<String, MetaClass> entry : deploymentPack.entrySet()) {
				logger.debug("Dumping metaclass" + entry.getKey());
				builder.putDeploymentPack(entry.getKey(), CommonYAML.getYamlObject().dump(entry.getValue()));
			}
			final DeployMetaClassesRequest request = builder.build();
			ExceptionInfo response;
			try {
				logger.debug("Calling deploy metaclass");
				response = blockingStub.deployMetaClasses(request);

			} catch (final StatusRuntimeException ex) {
				logger.debug("deployMetaClasses error", ex);
				throw new RuntimeException(ex.getMessage());
			}
			// Utils.checkIsExc(response);
		} catch (final Exception exr) {
			exr.printStackTrace();
		}
	}

	@Override
	public void deployClasses(final String namespaceName, final Map<Tuple<String, MetaClassID>, byte[]> classesToDeploy,
			final Map<String, byte[]> classesAspects, final Map<String, byte[]> stubYamls) {
		final DeployClassesRequest.Builder builder = DeployClassesRequest.newBuilder();
		builder.setNamespace(namespaceName);
		for (final Entry<Tuple<String, MetaClassID>, byte[]> entry : classesToDeploy.entrySet()) {
			final String className = entry.getKey().getFirst();
			final MetaClassID classID = entry.getKey().getSecond();
			final byte[] bytes = entry.getValue();
			builder.putClassesToDeploy(className, ByteString.copyFrom(bytes));
			builder.putClassIds(className, Utils.getMsgID(classID));
		}
		for (final Entry<String, byte[]> aspect : classesAspects.entrySet()) {
			builder.putAspects(aspect.getKey(), ByteString.copyFrom(aspect.getValue()));
		}
		for (final Entry<String, byte[]> entry : stubYamls.entrySet()) {
			builder.putYamls(entry.getKey(), ByteString.copyFrom(entry.getValue()));
		}
		final DeployClassesRequest request = builder.build();
		ExceptionInfo response;
		try {

			response = blockingStub.deployClasses(request);

		} catch (final StatusRuntimeException ex) {
			logger.debug("deployClasses error", ex);
			throw new RuntimeException(ex.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public void enrichClass(final String namespaceName, final String className, final byte[] classToDeploy,
			final byte[] classAspects, final byte[] stubYaml) {

		final EnrichClassRequest.Builder requestBuilder = EnrichClassRequest.newBuilder().setNamespace(namespaceName)
				.setClassname(className).setClassToDeploy(ByteString.copyFrom(classToDeploy))
				.setAspect(ByteString.copyFrom(classAspects)).setYaml(ByteString.copyFrom(stubYaml));

		final EnrichClassRequest request = requestBuilder.build();
		ExceptionInfo response;
		try {

			response = blockingStub.enrichClass(request);

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public ObjectID newPersistentInstance(final SessionID sessionID, final MetaClassID classID,
			final ImplementationID implementationID, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final SerializedParametersOrReturn params) {
		final NewPersistentInstanceRequest.Builder builder = NewPersistentInstanceRequest.newBuilder();
		builder.setSessionID(Utils.getMsgID(sessionID));
		builder.setClassID(Utils.getMsgID(classID));
		builder.setImplementationID(Utils.getMsgID(implementationID));
		if (ifaceBitMaps != null) {
			for (final Entry<MetaClassID, byte[]> entry : ifaceBitMaps.entrySet()) {
				builder.putIfaceBitMaps(entry.getKey().getId().toString(), ByteString.copyFrom(entry.getValue()));
			}
		}
		if (params != null) {
			builder.setParams(Utils.getParamsOrReturn(params));
		}

		final NewPersistentInstanceRequest request = builder.build();
		NewPersistentInstanceResponse response;
		try {

			response = blockingStub.newPersistentInstance(request);

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response.getExcInfo());

		return Utils.getObjectID(response.getObjectID());
	}

	@Override
	public void storeObjects(final SessionID sessionID, final List<ObjectWithDataParamOrReturn> objects,
			final boolean moving, final Set<ObjectID> idsWithAlias) {
		final StoreObjectsRequest.Builder builder = StoreObjectsRequest.newBuilder();
		builder.setSessionID(Utils.getMsgID(sessionID));
		for (final ObjectWithDataParamOrReturn obj : objects) {
			builder.addObjects(Utils.getObjectWithDataParamOrReturn(obj));
		}

		if (idsWithAlias != null) {
			for (final ObjectID idWithAlias : idsWithAlias) {
				if (idWithAlias != null) {
					builder.addIdsWithAlias(Utils.getMsgID(idWithAlias));
				}
			}
		}
		builder.setMoving(moving);
		final StoreObjectsRequest request = builder.build();

		if (Configuration.Flags.PRETTY_PRINT_MESSAGES.getBooleanValue()) {
			Utils.printMsg(request);
		}
		ExceptionInfo response;
		try {
			response = blockingStub.storeObjects(request);
		} catch (final StatusRuntimeException ex) {
			logger.debug("storeObjects error", ex);
			throw new RuntimeException(ex.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public SerializedParametersOrReturn getCopyOfObject(final SessionID sessionID, final ObjectID objectID,
			final boolean recursive) {
		final GetCopyOfObjectRequest.Builder builder = GetCopyOfObjectRequest.newBuilder();
		builder.setSessionID(Utils.getMsgID(sessionID));
		builder.setObjectID(Utils.getMsgID(objectID));
		builder.setRecursive(recursive);
		final GetCopyOfObjectRequest request = builder.build();
		GetCopyOfObjectResponse response;
		try {
			response = blockingStub.getCopyOfObject(request);
		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response.getExcInfo());

		final SerializedParametersOrReturn result = Utils.getParamsOrReturn(response.getRet());
		return result;
	}

	@Override
	public void updateObject(final SessionID sessionID, final ObjectID intoObjectID,
			final SerializedParametersOrReturn fromObject) {
		final UpdateObjectRequest.Builder builder = UpdateObjectRequest.newBuilder();
		builder.setSessionID(Utils.getMsgID(sessionID));
		builder.setIntoObjectID(Utils.getMsgID(intoObjectID));
		builder.setFromObject(Utils.getParamsOrReturn(fromObject));
		final UpdateObjectRequest request = builder.build();
		ExceptionInfo response;
		try {
			response = blockingStub.updateObject(request);
		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public List<ObjectWithDataParamOrReturn> getObjects(final SessionID sessionID, final Set<ObjectID> objectIDs,
														final Set<ObjectID> alreadyObtainedObjects,
			final boolean recursive, final ExecutionEnvironmentID destBackendID, final int updateReplicaLocs) {
		final GetObjectsRequest.Builder builder = GetObjectsRequest.newBuilder();
		for (final ObjectID oid : objectIDs) {
			builder.addObjectIDS(Utils.getMsgID(oid));
		}
		for (final ObjectID oid : alreadyObtainedObjects) {
			builder.addAlreadyObtainedObjects(Utils.getMsgID(oid));
		}
		builder.setRecursive(recursive);
		builder.setSessionID(Utils.getMsgID(sessionID));
		builder.setDestBackendID(Utils.getMsgID(destBackendID));
		builder.setUpdateReplicaLocs(updateReplicaLocs);
		final GetObjectsRequest request = builder.build();
		GetObjectsResponse response;
		try {

			response = blockingStub.getObjects(request);

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response.getExcInfo());

		final List<ObjectWithDataParamOrReturn> result = new ArrayList<>();
		for (final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ObjectWithDataParamOrReturn entry : response.getObjectsList()) {
			ObjectWithDataParamOrReturn objectWithDataParamOrReturn = Utils.getObjectWithDataParamOrReturn(entry);
			result.add(objectWithDataParamOrReturn);
		}
		return result;
	}

	@Override
	public void makePersistent(final SessionID sessionID, final List<ObjectWithDataParamOrReturn> params) {

		final MakePersistentRequest.Builder builder = MakePersistentRequest.newBuilder();

		builder.setSessionID(Utils.getMsgID(sessionID));
		if (params != null) {
			for (final ObjectWithDataParamOrReturn obj : params) {
				builder.addObjects(Utils.getObjectWithDataParamOrReturn(obj));
			}
		}
		final MakePersistentRequest request = builder.build();
		ExceptionInfo response;
		try {
			response = blockingStub.makePersistent(request);
			if (Configuration.Flags.PRETTY_PRINT_MESSAGES.getBooleanValue()) {
				Utils.printMsg(response);
			}
		} catch (final StatusRuntimeException ex) {
			logger.debug("** CAUGHT EXCEPTION **", ex.getStatus());
			throw ex;
		} catch (final Exception ex) {
			logger.debug("executeImplementation error", ex);
			throw ex;
		}

		Utils.checkIsExc(response);
	}


	@Override
	public void federate(final SessionID sessionID, final ObjectID objectID,
						 final ExecutionEnvironmentID externalExecutionEnvironmentID,
						 final boolean recursive) {
		final DataserviceMessages.FederateRequest.Builder builder = DataserviceMessages.FederateRequest.newBuilder();

		builder.setSessionID(Utils.getMsgID(sessionID));
		builder.setObjectID(Utils.getMsgID(objectID));
		builder.setExternalExecutionEnvironmentID(Utils.getMsgID(externalExecutionEnvironmentID));
		builder.setRecursive(recursive);
		final DataserviceMessages.FederateRequest request = builder.build();
		ExceptionInfo response;
		try {
			response = blockingStub.federate(request);
		} catch (final StatusRuntimeException ex) {
			logger.debug("** CAUGHT EXCEPTION **", ex);
			throw ex;
		} catch (final Exception ex) {
			logger.debug("federate error", ex);
			throw ex;
		}
		Utils.checkIsExc(response);
	}

	@Override
	public void unfederate(final SessionID sessionID, final ObjectID objectID,
						 final ExecutionEnvironmentID externalExecutionEnvironmentID,
						 final boolean recursive) {
		final DataserviceMessages.UnfederateRequest.Builder builder = DataserviceMessages.UnfederateRequest.newBuilder();

		builder.setSessionID(Utils.getMsgID(sessionID));
		builder.setObjectID(Utils.getMsgID(objectID));
		builder.setExternalExecutionEnvironmentID(Utils.getMsgID(externalExecutionEnvironmentID));
		builder.setRecursive(recursive);
		final DataserviceMessages.UnfederateRequest request = builder.build();
		ExceptionInfo response;
		try {
			response = blockingStub.unfederate(request);
		} catch (final StatusRuntimeException ex) {
			logger.debug("** CAUGHT EXCEPTION **", ex);
			throw ex;
		} catch (final Exception ex) {
			logger.debug("federate error", ex);
			throw ex;
		}
		Utils.checkIsExc(response);
	}

	@Override
	public void notifyFederation(final SessionID sessionID, final List<ObjectWithDataParamOrReturn> params) {

		final DataserviceMessages.NotifyFederationRequest.Builder builder = DataserviceMessages.NotifyFederationRequest.newBuilder();

		builder.setSessionID(Utils.getMsgID(sessionID));
		if (params != null) {
			for (final ObjectWithDataParamOrReturn obj : params) {
				builder.addObjects(Utils.getObjectWithDataParamOrReturn(obj));
			}
		}
		final DataserviceMessages.NotifyFederationRequest request = builder.build();
		ExceptionInfo response;
		try {
			response = blockingStub.withMaxInboundMessageSize(Integer.MAX_VALUE)
					.withMaxOutboundMessageSize(Integer.MAX_VALUE).notifyFederation(request);
		} catch (final StatusRuntimeException ex) {
			logger.debug("** CAUGHT EXCEPTION **", ex);
			throw ex;
		} catch (final Exception ex) {
			logger.debug("federate error", ex);
			throw ex;
		}

		Utils.checkIsExc(response);
	}

	@Override
	public void notifyUnfederation(final SessionID sessionID, final Set<ObjectID> objectIDs) {

		final DataserviceMessages.NotifyUnfederationRequest.Builder builder = DataserviceMessages.NotifyUnfederationRequest.newBuilder();

		builder.setSessionID(Utils.getMsgID(sessionID));
		for (final ObjectID oid : objectIDs) {
			builder.addObjectIDs(Utils.getMsgID(oid));
		}
		final DataserviceMessages.NotifyUnfederationRequest request = builder.build();
		ExceptionInfo response;
		try {
			response = blockingStub.notifyUnfederation(request);
		} catch (final Exception ex) {
			logger.debug("unfederate exception", ex);
			throw ex;
		}

		Utils.checkIsExc(response);
	}

	@Override
	public SerializedParametersOrReturn executeImplementation(final ObjectID objectID, final ImplementationID implID,
			final SerializedParametersOrReturn params, final SessionID sessionID) {

		final ExecuteImplementationRequest.Builder builder = ExecuteImplementationRequest.newBuilder();

		builder.setSessionID(Utils.getMsgID(sessionID));
		builder.setObjectID(Utils.getMsgID(objectID));
		builder.setImplementationID(Utils.getMsgID(implID));

		if (params != null) {
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.SerializedParametersOrReturn paramsMgs = Utils
					.getParamsOrReturn(params);
			builder.setParams(paramsMgs);
		}

		final ExecuteImplementationRequest request = builder.build();
		if (Configuration.Flags.PRETTY_PRINT_MESSAGES.getBooleanValue()) {
			Utils.printMsg(request);
		}

		ExecuteImplementationResponse response;
		try {

			if (DEBUG_ENABLED) {
				logger.debug("** EXECUTE request to " + blockingStub.getChannel());
			}

			response = blockingStub.withMaxInboundMessageSize(Integer.MAX_VALUE)
					.withMaxOutboundMessageSize(Integer.MAX_VALUE).executeImplementation(request);

			if (DEBUG_ENABLED) {
				logger.debug("** FINISHED EXECUTE request to " + blockingStub.getChannel());
			}

			if (Configuration.Flags.PRETTY_PRINT_MESSAGES.getBooleanValue()) {
				Utils.printMsg(response);
			}
		} catch (final StatusRuntimeException ex) {
			logger.debug("** CAUGHT EXCEPTION possibly due to volatile controlled race condition **", ex);
			throw ex;
		} catch (final Exception ex) {
			logger.debug("executeImplementation error", ex);
			throw ex;
		}

		Utils.checkIsExc(response.getExcInfo());

		if (response.hasRet()) {
			final SerializedParametersOrReturn result = Utils.getParamsOrReturn(response.getRet());
			return result;
		} else {
			return null;
		}

	}


	@Override
	public void synchronize(final SessionID sessionID, final ObjectID objectID, final ImplementationID implID,
							final SerializedParametersOrReturn params, final ExecutionEnvironmentID callingBackendID) {

		final DataserviceMessages.SynchronizeRequest.Builder builder = DataserviceMessages.SynchronizeRequest.newBuilder();

		builder.setSessionID(Utils.getMsgID(sessionID));
		builder.setObjectID(Utils.getMsgID(objectID));
		builder.setImplementationID(Utils.getMsgID(implID));
		builder.setCallingBackendID(Utils.getMsgID(callingBackendID));
		if (params != null) {
			final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.SerializedParametersOrReturn paramsMgs = Utils
					.getParamsOrReturn(params);
			builder.setParams(paramsMgs);
		}

		final DataserviceMessages.SynchronizeRequest request = builder.build();
		ExceptionInfo response;
		try {
			response = blockingStub.withMaxInboundMessageSize(Integer.MAX_VALUE)
					.withMaxOutboundMessageSize(Integer.MAX_VALUE).synchronize(request);
		} catch (final StatusRuntimeException ex) {
			logger.debug("** CAUGHT EXCEPTION possibly due to volatile controlled race condition **", ex);
			throw ex;
		} catch (final Exception ex) {
			logger.debug("executeImplementation error", ex);
			throw ex;
		}
		Utils.checkIsExc(response);
	}

	@Override
	public ObjectID newVersion(final SessionID sessionID, final ObjectID objectID,
			final ExecutionEnvironmentID destBackendID) {
		final NewVersionRequest request = NewVersionRequest.newBuilder().setObjectID(Utils.getMsgID(objectID))
				.setSessionID(Utils.getMsgID(sessionID))
				.setDestBackendID(Utils.getMsgID(destBackendID))
				.build();
		NewVersionResponse response;
		try {

			response = blockingStub.newVersion(request);

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getObjectID(response.getObjectID());

	}

	@Override
	public void consolidateVersion(final SessionID sessionID, final ObjectID versionObjectID) {
		final ConsolidateVersionRequest.Builder builder = ConsolidateVersionRequest.newBuilder();
		builder.setSessionID(Utils.getMsgID(sessionID));
		builder.setVersionObjectID(Utils.getMsgID(versionObjectID));
		final ConsolidateVersionRequest request = builder.build();
		ExceptionInfo response;
		try {
			response = blockingStub.consolidateVersion(request);

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public void upsertObjects(final SessionID sessionID, final List<ObjectWithDataParamOrReturn> objectBytes) {
		final UpsertObjectsRequest.Builder builder = UpsertObjectsRequest.newBuilder();
		builder.setSessionID(Utils.getMsgID(sessionID));
		for (final ObjectWithDataParamOrReturn entry : objectBytes) {
			builder.addBytesUpdate(Utils.getObjectWithDataParamOrReturn(entry));
		}
		final UpsertObjectsRequest request = builder.build();
		ExceptionInfo response;
		try {

			response = blockingStub.upsertObjects(request);

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public Set<ObjectID> newReplica(final SessionID sessionID, final ObjectID objectID,
									final ExecutionEnvironmentID destBackendID,
									final boolean recursive) {
		final NewReplicaRequest request = NewReplicaRequest.newBuilder()
				.setSessionID(Utils.getMsgID(sessionID))
				.setObjectID(Utils.getMsgID(objectID))
				.setDestBackendID(Utils.getMsgID(destBackendID))
				.setRecursive(recursive).build();
		NewReplicaResponse response;
		try {

			response = blockingStub.newReplica(request);

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response.getExcInfo());

		final Set<ObjectID> result = new HashSet<>();
		for (final String oid : response.getReplicatedObjectsList()) {
			result.add(Utils.getObjectID(oid));
		}
		return result;
	}

	@Override
	public Set<ObjectID> moveObjects(final SessionID sessionID, final ObjectID objectID,
			final ExecutionEnvironmentID destStLocation, final boolean recursive) {
		final MoveObjectsRequest request = MoveObjectsRequest.newBuilder().setSessionID(Utils.getMsgID(sessionID))
				.setDestLocID(Utils.getMsgID(destStLocation)).setRecursive(recursive)
				.setObjectID(Utils.getMsgID(objectID)).build();
		MoveObjectsResponse response;
		try {

			response = blockingStub.moveObjects(request);

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response.getExcInfo());

		final Set<ObjectID> result = new HashSet<>();
		for (final String oid : response
				.getMovedObjectsList()) {
			result.add(Utils.getObjectID(oid));
		}
		return result;
	}

	@Override
	public Map<ObjectID, ExecutionEnvironmentID> removeObjects(final SessionID sessionID, final Set<ObjectID> objectIDs,
			final boolean recursive, final boolean moving, final ExecutionEnvironmentID newhint) {
		final RemoveObjectsRequest.Builder builder = RemoveObjectsRequest.newBuilder();
		builder.setSessionID(Utils.getMsgID(sessionID));
		builder.setRecursive(recursive);
		builder.setMoving(moving);
		builder.setNewHint(Utils.getMsgID(newhint));
		for (final ObjectID oid : objectIDs) {
			builder.addObjectIDs(Utils.getMsgID(oid));
		}
		final RemoveObjectsRequest request = builder.build();
		RemoveObjectsResponse response;
		try {

			response = blockingStub.removeObjects(request);

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response.getExcInfo());

		final Map<ObjectID, ExecutionEnvironmentID> result = new ConcurrentHashMap<>();
		for (final Entry<String, String> entry : response.getRemovedObjectsMap().entrySet()) {
			result.put(Utils.getObjectID(entry.getKey()),
					Utils.getExecutionEnvironmentID(entry.getValue()));
		}
		return result;
	}

	@Override
	public Tuple<Map<StorageLocationID, Set<ObjectID>>, Set<ObjectID>> migrateObjectsToBackends(
			final Map<StorageLocationID, StorageLocation> backends) {

		final MigrateObjectsRequest.Builder builder = MigrateObjectsRequest.newBuilder();
		for (final Entry<StorageLocationID, StorageLocation> entry : backends.entrySet()) {
			builder.putDestStorageLocs(entry.getKey().getId().toString(),
					Utils.getStorageLocation(entry.getValue()));
		}
		final MigrateObjectsRequest request = builder.build();
		MigrateObjectsResponse response;
		try {

			response = blockingStub.migrateObjectsToBackends(request);

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response.getExcInfo());

		final Map<StorageLocationID, Set<ObjectID>> result = new ConcurrentHashMap<>();
		for (final Entry<String, MigratedObjects> entry : response.getMigratedObjsMap().entrySet()) {
			final MigratedObjects mObj = entry.getValue();
			final Set<ObjectID> oids = new HashSet<>();
			for (final String oid : mObj.getObjsList()) {
				oids.add(Utils.getObjectID(oid));
			}
			result.put(Utils.getStorageLocationID(entry.getKey()), oids);

		}

		final Set<ObjectID> nonmigrated = new HashSet<>();
		for (final String oid : response
				.getNonMigratedObjs().getObjsList()) {
			nonmigrated.add(Utils.getObjectID(oid));
		}
		return new Tuple<>(result, nonmigrated);
	}

	@Override
	public MetaClassID getClassIDFromObjectInMemory(final ObjectID objectID) {
		final GetClassIDFromObjectInMemoryRequest request = GetClassIDFromObjectInMemoryRequest.newBuilder()
				.setObjectID(Utils.getMsgID(objectID)).build();
		GetClassIDFromObjectInMemoryResponse response;
		try {

			response = blockingStub.getClassIDFromObjectInMemory(request);

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response.getExcInfo());

		return Utils.getMetaClassID(response.getClassID());
	}

	@Override
	public void cleanExecutionClassDirectory() {
		ExceptionInfo response;
		try {

			response = blockingStub.cleanExecutionClassDirectory(EmptyMessage.newBuilder().build());

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public void closeDbHandler() {
		ExceptionInfo response;
		try {

			response = blockingStub.closeDbHandler(EmptyMessage.newBuilder().build());

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public void shutDown() {
		ExceptionInfo response;
		try {

			response = blockingStub.shutDown(EmptyMessage.newBuilder().build());

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public void disconnectFromOthers() {
		ExceptionInfo response;
		try {

			response = blockingStub.disconnectFromOthers(EmptyMessage.newBuilder().build());

		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getStatus().getDescription());
		}
		Utils.checkIsExc(response);
	}

	/**
	 * Wait all async. requests
	 */
	public void waitAndProcessAllAsyncRequests() {
		while (asyncReqSend.get() != asyncReqReceived.get()) {
			try {
				Thread.sleep(ASYNC_WAIT_MILLIS);
			} catch (final InterruptedException ex) {
				logger.debug("waitAndProcessAllAsyncRequests interrupted while sleeping", ex);
			}
		}
	}

	@Override
	public void closeSessionInDS(final SessionID sessionID) {
		final CloseSessionInDSRequest.Builder builder = CloseSessionInDSRequest.newBuilder();
		builder.setSessionID(Utils.getMsgID(sessionID));
		final CloseSessionInDSRequest request = builder.build();
		ExceptionInfo response;
		try {
			response = blockingStub.closeSessionInDS(request);
		} catch (final Exception e) {
			throw e;
		}
		Utils.checkIsExc(response);
	}

	@Override
	public void updateRefs(final Map<ObjectID, Integer> updateCounterRefs) {
		final es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateRefsRequest.Builder builder = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateRefsRequest
				.newBuilder();
		for (final Entry<ObjectID, Integer> entry : updateCounterRefs.entrySet()) {
			final ObjectID oid = entry.getKey();
			final Integer counter = entry.getValue();
			builder.putRefsToUpdate(oid.getId().toString(), counter);
		}
		final es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateRefsRequest request = builder
				.build();
		ExceptionInfo response;
		try {
			response = blockingStub.updateRefs(request);
		} catch (final Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public Set<ObjectID> getRetainedReferences() {
		GetRetainedReferencesResponse response;
		try {
			response = blockingStub.getRetainedReferences(EmptyMessage.newBuilder().build());
		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response.getExcInfo());

		final Set<ObjectID> result = new HashSet<>();
		for (final String oid : response
				.getRetainedReferencesList()) {
			result.add(Utils.getObjectID(oid));
		}
		return result;
	}

	@Override
	public void deleteAlias(final SessionID sessionID, final ObjectID objectID) {
		final DataserviceMessages.DeleteAliasRequest.Builder builder = DataserviceMessages.DeleteAliasRequest
				.newBuilder();
		builder.setSessionID(Utils.getMsgID(sessionID));
		builder.setObjectID(Utils.getMsgID(objectID));
		final es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeleteAliasRequest request = builder
				.build();
		ExceptionInfo response;
		try {
			response = blockingStub.deleteAlias(request);
		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public void detachObjectFromSession(final ObjectID objectID, final SessionID sessionID) {
		final DataserviceMessages.DetachObjectFromSessionRequest.Builder builder = DataserviceMessages.DetachObjectFromSessionRequest
				.newBuilder();
		builder.setSessionID(Utils.getMsgID(sessionID));
		builder.setObjectID(Utils.getMsgID(objectID));
		final es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DetachObjectFromSessionRequest request = builder
				.build();
		ExceptionInfo response;
		try {
			response = blockingStub.detachObjectFromSession(request);
		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public boolean exists(final ObjectID objectID) {
		final ExistsRequest.Builder builder = ExistsRequest.newBuilder();
		builder.setObjectID(Utils.getMsgID(objectID));
		final ExistsRequest request = builder.build();
		ExistsResponse response;
		try {
			response = blockingStub.exists(request);
		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response.getExcInfo());
		return response.getExists();
	}

	@Override
	public boolean existsInDB(final ObjectID objectID) {
		final ExistsInDBRequest.Builder builder = ExistsInDBRequest.newBuilder();
		builder.setObjectID(Utils.getMsgID(objectID));
		final ExistsInDBRequest request = builder.build();
		ExistsInDBResponse response;
		try {
			response = blockingStub.existsInDB(request);
		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response.getExcInfo());
		return response.getExists();
	}


	@Override
	public int getNumObjects() {
		CommonMessages.GetNumObjectsResponse response;
		try {
			response = blockingStub.getNumObjects(EmptyMessage.newBuilder().build());
		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response.getExcInfo());
		return response.getNumObjs();
	}

	@Override
	public int getNumObjectsInEE() {
		CommonMessages.GetNumObjectsResponse response;
		try {
			response = blockingStub.getNumObjectsInEE(EmptyMessage.newBuilder().build());
		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response.getExcInfo());
		return response.getNumObjs();
	}

	@Override
	public void store(final ExecutionEnvironmentID eeID, final ObjectID objectID, final byte[] bytes) {
		final StoreToDBRequest.Builder builder = StoreToDBRequest.newBuilder();
		builder.setExecutionEnvironmentID(Utils.getMsgID(eeID));
		builder.setObjectID(Utils.getMsgID(objectID));
		builder.setObjBytes(ByteString.copyFrom(bytes));

		final StoreToDBRequest request = builder.build();
		ExceptionInfo response;
		try {
			response = blockingStub.storeToDB(request);
		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public byte[] get(final ExecutionEnvironmentID eeID, final ObjectID objectID) {
		final GetFromDBRequest request = GetFromDBRequest.newBuilder().setExecutionEnvironmentID(Utils.getMsgID(eeID))
				.setObjectID(Utils.getMsgID(objectID)).build();
		GetFromDBResponse response;
		try {
			response = blockingStub.getFromDB(request);
		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response.getExcInfo());

		if (response.getObjBytes() != null) {
			return response.getObjBytes().toByteArray();
		} else {
			return null;
		}
	}

	@Override
	public void update(final ExecutionEnvironmentID eeID, final ObjectID objectID, final byte[] newbytes,
			final boolean dirty) {
		final UpdateToDBRequest.Builder builder = UpdateToDBRequest.newBuilder();
		builder.setExecutionEnvironmentID(Utils.getMsgID(eeID));
		builder.setObjectID(Utils.getMsgID(objectID));
		builder.setObjBytes(ByteString.copyFrom(newbytes));
		builder.setDirty(dirty);
		final UpdateToDBRequest request = builder.build();
		ExceptionInfo response;
		try {
			response = blockingStub.updateToDB(request);
		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public void delete(final ExecutionEnvironmentID eeID, final ObjectID objectID) {
		final DeleteToDBRequest.Builder builder = DeleteToDBRequest.newBuilder();
		builder.setExecutionEnvironmentID(Utils.getMsgID(eeID));
		builder.setObjectID(Utils.getMsgID(objectID));

		final DeleteToDBRequest request = builder.build();
		ExceptionInfo response;
		try {
			response = blockingStub.deleteToDB(request);
		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

	@Override
	public void associateExecutionEnvironment(final ExecutionEnvironmentID executionEnvironmentID) {
		final AssociateExecutionEnvironmentRequest.Builder builder = AssociateExecutionEnvironmentRequest.newBuilder();
		builder.setExecutionEnvironmentID(Utils.getMsgID(executionEnvironmentID));

		final AssociateExecutionEnvironmentRequest request = builder.build();
		ExceptionInfo response;
		try {
			response = blockingStub.associateExecutionEnvironment(request);
		} catch (final StatusRuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		Utils.checkIsExc(response);
	}

}
