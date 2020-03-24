
/**
 * 
 */
package es.bsc.dataclay.communication.grpc.clients.logicmodule;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.protobuf.ByteString;

import es.bsc.dataclay.commonruntime.DataServiceRuntime;
import es.bsc.dataclay.communication.grpc.Utils;
import es.bsc.dataclay.communication.grpc.clients.CommonGrpcClient;
import es.bsc.dataclay.communication.grpc.generated.logicmodule.LogicModuleGrpc;
import es.bsc.dataclay.communication.grpc.generated.logicmodule.LogicModuleGrpc.LogicModuleBlockingStub;
import es.bsc.dataclay.communication.grpc.generated.logicmodule.LogicModuleGrpc.LogicModuleStub;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.*;
import es.bsc.dataclay.logic.api.LogicModuleAPI;
import es.bsc.dataclay.serialization.lib.SerializedParametersOrReturn;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.events.listeners.ECA;
import es.bsc.dataclay.util.events.message.EventMessage;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.DataContractID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.ids.StorageLocationID;
import es.bsc.dataclay.util.info.VersionInfo;
import es.bsc.dataclay.util.management.accountmgr.Account;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.contractmgr.Contract;
import es.bsc.dataclay.util.management.datacontractmgr.DataContract;
import es.bsc.dataclay.util.management.datasetmgr.DataSet;
import es.bsc.dataclay.util.management.interfacemgr.Interface;
import es.bsc.dataclay.util.management.metadataservice.DataClayInstance;
import es.bsc.dataclay.util.management.metadataservice.ExecutionEnvironment;
import es.bsc.dataclay.util.management.metadataservice.MetaDataInfo;
import es.bsc.dataclay.util.management.metadataservice.RegistrationInfo;
import es.bsc.dataclay.util.management.metadataservice.StorageLocation;
import es.bsc.dataclay.util.management.namespacemgr.Namespace;
import es.bsc.dataclay.util.management.sessionmgr.SessionInfo;
import es.bsc.dataclay.util.structs.Triple;
import es.bsc.dataclay.util.structs.Tuple;
import es.bsc.dataclay.util.yaml.CommonYAML;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.grpc.stub.MetadataUtils;
import io.grpc.stub.StreamObserver;

/**
 * Client code that makes gRPC calls to the server.
 */
public final class LogicModuleGrpcClient implements LogicModuleAPI {

	/** Logger. */
	private final Logger logger;

	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Number of max seconds to shutdown. */
	private static final int SECONDS_SHUTDOWN = 60;

	/** Sleeping time while there is asynchronous request to wait for. */
	private static final int ASYNC_WAIT_MILLIS = 1000;

	/** Channel. */
	private ManagedChannel channel;

	/** Logic Module blocking stub. */
	private LogicModuleBlockingStub blockingStub;

	/** Logic Module stub. */
	private LogicModuleStub asyncStub;

	/** Number of asynchronous requests send. */
	private final AtomicInteger asyncReqSend = new AtomicInteger(0);

	/** Number of asynchronous requests received. */
	private final AtomicInteger asyncReqReceived = new AtomicInteger(0);

	/**
	 * Construct client for accessing LogicModule server at {@code host:port}.
	 * 
	 * @param host
	 *            Host
	 * @param port
	 *            Port
	 */
	public LogicModuleGrpcClient(final String host, final int port) {
		// Logger.getLogger("io.grpc").setLevel(Level.OFF);
		logger = LogManager.getLogger("grpc.client.logicmodule");
		createStubs(host, port);
	}

	/**
	 * Create stubs to specified host and port. This function is also used in case
	 * we replace the connection.
	 * 
	 * @param host
	 *            Host name
	 * @param port
	 *            Port
	 */
	private void createStubs(final String host, final int port) {

		/*
		 * Tuple<String, Integer> actualAddress = translateDockerAddressIfNeeded(host,
		 * port); String actualHost = actualAddress.getFirst(); int actualPort =
		 * actualAddress.getSecond();
		 */
		final NettyChannelBuilder chBuilder = NettyChannelBuilder.forAddress(host, port)
				.maxInboundMessageSize(Integer.MAX_VALUE).maxInboundMetadataSize(Integer.MAX_VALUE);
		
		// Check if paths to certificates are defined 
		if (Configuration.Flags.SSL_CLIENT_TRUSTED_CERTIFICATES.getStringValue() != null
				|| Configuration.Flags.SSL_CLIENT_CERTIFICATE.getStringValue() != null
				|| Configuration.Flags.SSL_CLIENT_KEY.getStringValue() != null) {
			try {
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
			chBuilder.usePlaintext();
			//chBuilder.negotiationType(NegotiationType.PLAINTEXT); 
			logger.info("Not using SSL");

		}
		channel = chBuilder.build();
		// Capture the metadata exchange
		final Metadata fixedHeaders = new Metadata();
		fixedHeaders.put(CommonGrpcClient.SERVICE_ALIAS_HEADER_KEY, Configuration.Flags.LM_SERVICE_ALIAS_HEADERMSG.getStringValue());
		blockingStub = MetadataUtils.attachHeaders(LogicModuleGrpc.newBlockingStub(channel), fixedHeaders);
		asyncStub = MetadataUtils.attachHeaders(LogicModuleGrpc.newStub(channel), fixedHeaders);
		
		//blockingStub.getCallOptions().withMaxOutboundMessageSize(Integer.MAX_VALUE);
		//asyncStub.getCallOptions().withMaxOutboundMessageSize(Integer.MAX_VALUE);

		
	}

	/**
	 * Get blocking stub. Getter used in lambda functions to ensure it is executed
	 * and in case of replacing the stub, have control here.
	 * 
	 * @return Blocking stub.
	 */
	private LogicModuleBlockingStub getBlockingStub() {
		return blockingStub;
	}

	/**
	 * Get async stub. Getter used in lambda functions to ensure it is executed and
	 * in case of replacing the stub, have control here.
	 * 
	 * @return Blocking stub.
	 */
	private LogicModuleStub getAsyncStub() {
		return asyncStub;
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

	/**
	 * Call logic module using lambda function provided.
	 * 
	 * @param <T>
	 *            Request type depending on function
	 * @param <R>
	 *            Response type depending on function
	 * @param request
	 *            Request to send
	 * @param fn
	 *            Function to call
	 * @return Response
	 */
	private <T, R> R callLogicModule(final T request, final Function<T, R> fn) {
		R response = null;
		short retryCommunicationIteration = 0;
		while (retryCommunicationIteration < Configuration.Flags.MAX_RETRIES_LOGICMODULE.getShortValue()) {
			try {
				response = fn.apply(request);
				break;
			} catch (final StatusRuntimeException e) {
				logger.debug("Error calling LogicModule", e);
				retryCommunicationIteration++;
				if (retryCommunicationIteration >= Configuration.Flags.MAX_RETRIES_LOGICMODULE.getShortValue()) {
					// Use Backup LM
					throw new RuntimeException(e.getStatus().getDescription());
					
				} else { 
					try {
						Thread.sleep(Configuration.Flags.SLEEP_RETRIES_LOGICMODULE.getShortValue());
					} catch (final InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		return response;
	}

	/**
	 * Do an asynchronous call to LogicModule
	 * 
	 * @param <T>
	 *            Request type depending on function
	 * @param <R>
	 *            Response type depending on function
	 * @param request
	 *            Request to send
	 * @param fn
	 *            Function to call
	 * @param resultObserver
	 *            For asynchronous call, response observer in which response will be
	 *            added.
	 */
	private <T, R> void callLogicModuleAsync(final T request, final StreamObserver<R> resultObserver,
			final BiConsumer<T, StreamObserver<R>> fn) {
		short retryCommunicationIteration = 0;
		asyncReqSend.incrementAndGet();
		while (retryCommunicationIteration < Configuration.Flags.MAX_RETRIES_LOGICMODULE.getShortValue()) {
			try {
				fn.accept(request, resultObserver);
				break;
			} catch (final StatusRuntimeException e) {
				retryCommunicationIteration++;
				if (retryCommunicationIteration >= Configuration.Flags.MAX_RETRIES_LOGICMODULE.getShortValue()) {
					
					throw new RuntimeException(e.getStatus().getDescription());
					
				}
			}
		}
	}

	@Override
	public void checkAlive() {
		final EmptyMessage request = EmptyMessage.getDefaultInstance();
		final ExceptionInfo response;
		final Function<EmptyMessage, ExceptionInfo> f = req -> getBlockingStub().checkAlive(req);
		response = this.<EmptyMessage, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}

	@Override
	public void autoregisterSL(final StorageLocationID id, final String dsName,
			final String dsHostname, final Integer dsTCPPort) {

		final AutoRegisterSLRequest request = AutoRegisterSLRequest.newBuilder()
				.setDsName(dsName).setStorageLocationID(Utils.getMsgID(id))
				.setDsHostname(dsHostname).setDsPort(dsTCPPort).build();
		final ExceptionInfo response;
		final Function<AutoRegisterSLRequest, ExceptionInfo> f = req -> getBlockingStub()
				.autoregisterSL(req);
		response = this.<AutoRegisterSLRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}
	
	@Override
	public StorageLocationID autoregisterEE(final ExecutionEnvironmentID id, final String eeName,
			final String eeHostname, final Integer eePort, final Langs language) {

		final AutoRegisterEERequest request = AutoRegisterEERequest.newBuilder()
				.setEeName(eeName).setEeHostname(eeHostname).setEePort(eePort)
				.setLang(language).setExecutionEnvironmentID(Utils.getMsgID(id)).build();
		final AutoRegisterEEResponse response;
		final Function<AutoRegisterEERequest, AutoRegisterEEResponse> f = req -> getBlockingStub()
				.autoregisterEE(req);
		response = this.<AutoRegisterEERequest, AutoRegisterEEResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getStorageLocationID());
	}

	@Override
	public void unregisterStorageLocation(final StorageLocationID stLocID) {
		final UnregisterStorageLocationRequest request = UnregisterStorageLocationRequest.newBuilder()
				.setStorageLocationID(Utils.getMsgID(stLocID)).build();
		final ExceptionInfo response;
		final Function<UnregisterStorageLocationRequest, ExceptionInfo> f = req -> getBlockingStub()
				.unregisterStorageLocation(req);
		response = this.<UnregisterStorageLocationRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}

	@Override
	public void unregisterExecutionEnvironment(final ExecutionEnvironmentID execEnvID) {
		final UnregisterExecutionEnvironmentRequest request = UnregisterExecutionEnvironmentRequest.newBuilder()
				.setExecutionEnvironmentID(Utils.getMsgID(execEnvID)).build();
		final ExceptionInfo response;
		final Function<UnregisterExecutionEnvironmentRequest, ExceptionInfo> f = req -> getBlockingStub()
				.unregisterExecutionEnvironment(req);
		response = this.<UnregisterExecutionEnvironmentRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}

	@Override
	public byte[] performSetOfNewAccounts(final AccountID adminID, final PasswordCredential adminCredential,
			final byte[] yamlFile) {
		final PerformSetAccountsRequest request = PerformSetAccountsRequest.newBuilder()
				.setAccountID(Utils.getMsgID(adminID)).setCredential(Utils.getCredential(adminCredential))
				.setYamlBytes(ByteString.copyFrom(yamlFile)).build();
		final PerformSetAccountsResponse response;
		final Function<PerformSetAccountsRequest, PerformSetAccountsResponse> f = req -> getBlockingStub()
				.performSetOfNewAccounts(req);
		response = this.<PerformSetAccountsRequest, PerformSetAccountsResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		final byte[] result = response.getResultYamlBytes().toByteArray();
		return result;
	}

	@Override
	public byte[] performSetOfOperations(final AccountID performerID, final PasswordCredential performerCredential,
			final byte[] yamlFile) {
		final PerformSetOperationsRequest request = PerformSetOperationsRequest.newBuilder()
				.setAccountID(Utils.getMsgID(performerID)).setCredential(Utils.getCredential(performerCredential))
				.setYamlBytes(ByteString.copyFrom(yamlFile)).build();

		final PerformSetOperationsResponse response;
		final Function<PerformSetOperationsRequest, PerformSetOperationsResponse> f = req -> getBlockingStub()
				.performSetOfOperations(req);
		response = this.<PerformSetOperationsRequest, PerformSetOperationsResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		final byte[] result = response.getResultYamlBytes().toByteArray();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#newAccount(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential,
	 * util.management.accountmgr.Account)
	 */
	@Override
	public AccountID newAccountNoAdmin(final Account newAccount) {
		final String accYaml = CommonYAML.getYamlObject().dump(newAccount);
		final NewAccountNoAdminRequest request = NewAccountNoAdminRequest.newBuilder().setYamlNewAccount(accYaml)
				.build();

		final NewAccountResponse response;
		final Function<NewAccountNoAdminRequest, NewAccountResponse> f = req -> getBlockingStub()
				.newAccountNoAdmin(req);

		response = this.<NewAccountNoAdminRequest, NewAccountResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getNewAccountID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#newAccount(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential,
	 * util.management.accountmgr.Account)
	 */
	@Override
	public AccountID newAccount(final AccountID adminAccountID, final PasswordCredential adminCredential,
			final Account newAccount) {
		final String accYaml = CommonYAML.getYamlObject().dump(newAccount);
		final NewAccountRequest request = NewAccountRequest.newBuilder().setAdminID(Utils.getMsgID(adminAccountID))
				.setAdmincredential(Utils.getCredential(adminCredential)).setYamlNewAccount(accYaml).build();

		final NewAccountResponse response;
		final Function<NewAccountRequest, NewAccountResponse> f = req -> getBlockingStub().newAccount(req);

		response = this.<NewAccountRequest, NewAccountResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getNewAccountID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getAccountID(java.lang.String)
	 */
	@Override
	public AccountID getAccountID(final String accountName) {

		final GetAccountIDRequest request = GetAccountIDRequest.newBuilder().setAccountName(accountName).build();

		final GetAccountIDResponse response;
		final Function<GetAccountIDRequest, GetAccountIDResponse> f = req -> getBlockingStub().getAccountID(req);
		response = this.<GetAccountIDRequest, GetAccountIDResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getNewAccountID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getAccountList(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential)
	 */
	@Override
	public Set<AccountID> getAccountList(final AccountID adminAccountID, final PasswordCredential adminCredential) {
		final GetAccountListRequest request = GetAccountListRequest.newBuilder()
				.setAdminID(Utils.getMsgID(adminAccountID)).setAdmincredential(Utils.getCredential(adminCredential))
				.build();

		final GetAccountListResponse response;
		final Function<GetAccountListRequest, GetAccountListResponse> f = req -> getBlockingStub().getAccountList(req);
		response = this.<GetAccountListRequest, GetAccountListResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());

		final Set<AccountID> result = new HashSet<>();
		for (final String id : response.getAccountIDsList()) {
			result.add(Utils.getAccountIDFromUUID(id));
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#newSession(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, java.util.Set, java.util.Set,
	 * java.lang.String, communication.grpc.messages.common.CommonMessages.Langs)
	 */
	@Override
	public SessionInfo newSession(final AccountID accountID, final PasswordCredential credential,
			final Set<ContractID> contracts, final Set<DataSetID> dataSetIDs, final DataSetID dataSetForStore,
			final Langs newsessionLang) {

		final NewSessionRequest.Builder requestBuilder = NewSessionRequest.newBuilder();
		for (final ContractID conID : contracts) {
			requestBuilder.addContractIDs(Utils.getMsgID(conID));
		}
		for (final DataSetID dataSetID : dataSetIDs) {
			requestBuilder.addDataSetIDs(Utils.getMsgID(dataSetID));
		}
		final NewSessionRequest request = requestBuilder.setAccountID(Utils.getMsgID(accountID))
				.setCredential(Utils.getCredential(credential)).setStoreDataSet(Utils.getMsgID(dataSetForStore))
				.setSessionLang(newsessionLang).build();

		final NewSessionResponse response;
		final Function<NewSessionRequest, NewSessionResponse> f = req -> getBlockingStub().newSession(req);
		response = this.<NewSessionRequest, NewSessionResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());

		return (SessionInfo) CommonYAML.getYamlObject().load(response.getSessionInfo());
	}

	@Override
	public void closeSession(final SessionID sessionID) {
		final CloseSessionRequest.Builder requestBuilder = CloseSessionRequest.newBuilder();
		requestBuilder.setSessionID(Utils.getMsgID(sessionID));
		final CloseSessionRequest request = requestBuilder.build();
		final ExceptionInfo response;
		final Function<CloseSessionRequest, ExceptionInfo> f = req -> getBlockingStub().closeSession(req);
		response = this.<CloseSessionRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#getInfoOfSessionForDS(dataClay.util.ids.SessionID)
	 */
	@Override
	public Tuple<Tuple<DataSetID, Set<DataSetID>>, Calendar> getInfoOfSessionForDS(final SessionID sessionID) {
		final GetInfoOfSessionForDSRequest request = GetInfoOfSessionForDSRequest.newBuilder()
				.setSessionID(Utils.getMsgID(sessionID)).build();

		final GetInfoOfSessionForDSResponse response;
		final Function<GetInfoOfSessionForDSRequest, GetInfoOfSessionForDSResponse> f = req -> getBlockingStub()
				.getInfoOfSessionForDS(req);
		response = this.<GetInfoOfSessionForDSRequest, GetInfoOfSessionForDSResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());

		final DataSetID dsId = Utils.getID(response.getDataSetID());
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(response.getDate());
		final Set<DataSetID> datasets = new HashSet<>();
		for (final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.DataSetID id : response
				.getDataSetIDsList()) {
			datasets.add(Utils.getID(id));
		}

		return new Tuple<>(new Tuple<>(dsId, datasets), calendar);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#newNamespace(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential,
	 * util.management.namespacemgr.Namespace)
	 */
	@Override
	public NamespaceID newNamespace(final AccountID accountID, final PasswordCredential credential,
			final Namespace newNamespace) {
		final String yaml = CommonYAML.getYamlObject().dump(newNamespace);
		final NewNamespaceRequest request = NewNamespaceRequest.newBuilder().setAccountID(Utils.getMsgID(accountID))
				.setCredential(Utils.getCredential(credential)).setNewNamespaceYaml(yaml).build();

		final NewNamespaceResponse response;
		final Function<NewNamespaceRequest, NewNamespaceResponse> f = req -> getBlockingStub().newNamespace(req);
		response = this.<NewNamespaceRequest, NewNamespaceResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getNamespaceID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#removeNamespace(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, java.lang.String)
	 */
	@Override
	public void removeNamespace(final AccountID accountID, final PasswordCredential credential,
			final String namespaceName) {
		final RemoveNamespaceRequest request = RemoveNamespaceRequest.newBuilder()
				.setAccountID(Utils.getMsgID(accountID)).setCredential(Utils.getCredential(credential))
				.setNamespaceName(namespaceName).build();
		final ExceptionInfo response;
		final Function<RemoveNamespaceRequest, ExceptionInfo> f = req -> getBlockingStub().removeNamespace(req);
		response = this.<RemoveNamespaceRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getNamespaces(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential)
	 */
	@Override
	public Set<String> getNamespaces(final AccountID accountID, final PasswordCredential credential) {
		final GetNamespacesRequest request = GetNamespacesRequest.newBuilder().setAccountID(Utils.getMsgID(accountID))
				.setCredential(Utils.getCredential(credential)).build();
		final GetNamespacesResponse response;
		final Function<GetNamespacesRequest, GetNamespacesResponse> f = req -> getBlockingStub().getNamespaces(req);
		response = this.<GetNamespacesRequest, GetNamespacesResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		final Set<String> result = new HashSet<>();
		for (final String namespace : response.getNamespacesList()) {
			result.add(namespace);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getNamespaceID(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, java.lang.String)
	 */
	@Override
	public NamespaceID getNamespaceID(final AccountID accountID, final PasswordCredential credential,
			final String namespaceName) {
		final GetNamespaceIDRequest request = GetNamespaceIDRequest.newBuilder().setAccountID(Utils.getMsgID(accountID))
				.setCredential(Utils.getCredential(credential)).setNamespaceName(namespaceName).build();
		final GetNamespaceIDResponse response;
		final Function<GetNamespaceIDRequest, GetNamespaceIDResponse> f = req -> getBlockingStub().getNamespaceID(req);
		response = this.<GetNamespaceIDRequest, GetNamespaceIDResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());

		return Utils.getID(response.getNamespaceID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getNamespaceID(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, java.lang.String)
	 */
	@Override
	public Langs getNamespaceLang(final AccountID accountID, final PasswordCredential credential,
			final String namespaceName) {
		final GetNamespaceLangRequest request = GetNamespaceLangRequest.newBuilder()
				.setAccountID(Utils.getMsgID(accountID)).setCredential(Utils.getCredential(credential))
				.setNamespaceName(namespaceName).build();
		final GetNamespaceLangResponse response;
		final Function<GetNamespaceLangRequest, GetNamespaceLangResponse> f = req -> getBlockingStub()
				.getNamespaceLang(req);
		response = this.<GetNamespaceLangRequest, GetNamespaceLangResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return response.getLanguage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getObjectDataSetID(dataClay.util.ids.SessionID,
	 * util.ids.ObjectID)
	 */
	@Override
	public DataSetID getObjectDataSetID(final SessionID sessionID, final ObjectID oid) {
		final GetObjectDataSetIDRequest request = GetObjectDataSetIDRequest.newBuilder()
				.setSessionID(Utils.getMsgID(sessionID)).setObjectID(Utils.getMsgID(oid)).build();
		final GetObjectDataSetIDResponse response;
		final Function<GetObjectDataSetIDRequest, GetObjectDataSetIDResponse> f = req -> getBlockingStub()
				.getObjectDataSetID(req);
		response = this.<GetObjectDataSetIDRequest, GetObjectDataSetIDResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getDataSetID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#importInterface(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, util.ids.NamespaceID,
	 * util.ids.ContractID, util.ids.InterfaceID)
	 */
	@Override
	public void importInterface(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final ContractID contractID, final InterfaceID interfaceID) {
		final ImportInterfaceRequest request = ImportInterfaceRequest.newBuilder()
				.setAccountID(Utils.getMsgID(accountID)).setCredential(Utils.getCredential(credential))
				.setNamespaceID(Utils.getMsgID(namespaceID)).setContractID(Utils.getMsgID(contractID))
				.setInterfaceID(Utils.getMsgID(interfaceID)).build();
		final ExceptionInfo response;
		final Function<ImportInterfaceRequest, ExceptionInfo> f = req -> getBlockingStub().importInterface(req);
		response = this.<ImportInterfaceRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#importContract(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, util.ids.NamespaceID,
	 * util.ids.ContractID)
	 */
	@Override
	public void importContract(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final ContractID contractID) {
		final ImportContractRequest request = ImportContractRequest.newBuilder().setAccountID(Utils.getMsgID(accountID))
				.setCredential(Utils.getCredential(credential)).setNamespaceID(Utils.getMsgID(namespaceID))
				.setContractID(Utils.getMsgID(contractID)).build();
		final ExceptionInfo response;
		final Function<ImportContractRequest, ExceptionInfo> f = req -> getBlockingStub().importContract(req);
		response = this.<ImportContractRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#newDataSet(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential,
	 * util.management.datasetmgr.DataSet)
	 */
	@Override
	public DataSetID newDataSet(final AccountID accountID, final PasswordCredential credential, final DataSet dataset) {
		final String yaml = CommonYAML.getYamlObject().dump(dataset);
		final NewDataSetRequest request = NewDataSetRequest.newBuilder().setAccountID(Utils.getMsgID(accountID))
				.setCredential(Utils.getCredential(credential)).setDataSetYaml(yaml).build();

		final NewDataSetResponse response;
		final Function<NewDataSetRequest, NewDataSetResponse> f = req -> getBlockingStub().newDataSet(req);
		response = this.<NewDataSetRequest, NewDataSetResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getDataSetID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#removeDataSet(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, java.lang.String)
	 */
	@Override
	public void removeDataSet(final AccountID accountID, final PasswordCredential credential,
			final String datasetName) {
		final RemoveDataSetRequest request = RemoveDataSetRequest.newBuilder().setAccountID(Utils.getMsgID(accountID))
				.setCredential(Utils.getCredential(credential)).setDataSetName(datasetName).build();
		final ExceptionInfo response;
		final Function<RemoveDataSetRequest, ExceptionInfo> f = req -> getBlockingStub().removeDataSet(req);
		response = this.<RemoveDataSetRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getDataSetID(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, java.lang.String)
	 */
	@Override
	public DataSetID getDataSetID(final AccountID accountID, final PasswordCredential credential,
			final String datasetName) {
		final GetDataSetIDRequest request = GetDataSetIDRequest.newBuilder().setAccountID(Utils.getMsgID(accountID))
				.setCredential(Utils.getCredential(credential)).setDataSetName(datasetName).build();
		final GetDataSetIDResponse response;
		final Function<GetDataSetIDRequest, GetDataSetIDResponse> f = req -> getBlockingStub().getDataSetID(req);
		response = this.<GetDataSetIDRequest, GetDataSetIDResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getDataSetID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#checkDataSetIsPublic(dataClay.util.ids.DataSetID)
	 */
	@Override
	public boolean checkDataSetIsPublic(final es.bsc.dataclay.util.ids.DataSetID dataSetID) {
		final CheckDataSetIsPublicRequest request = CheckDataSetIsPublicRequest.newBuilder()
				.setDataSetID(Utils.getMsgID(dataSetID)).build();
		final CheckDataSetIsPublicResponse response;
		final Function<CheckDataSetIsPublicRequest, CheckDataSetIsPublicResponse> f = req -> getBlockingStub()
				.checkDataSetIsPublic(req);
		response = this.<CheckDataSetIsPublicRequest, CheckDataSetIsPublicResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return response.getIsPublic();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getPublicDataSets(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential)
	 */
	@Override
	public Set<String> getPublicDataSets(final AccountID accountID, final PasswordCredential credential) {
		final GetPublicDataSetsRequest request = GetPublicDataSetsRequest.newBuilder()
				.setAccountID(Utils.getMsgID(accountID)).setCredential(Utils.getCredential(credential)).build();
		final GetPublicDataSetsResponse response;
		final Function<GetPublicDataSetsRequest, GetPublicDataSetsResponse> f = req -> getBlockingStub()
				.getPublicDataSets(req);
		response = this.<GetPublicDataSetsRequest, GetPublicDataSetsResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		final Set<String> result = new HashSet<>();
		for (final String dataSetName : response.getDataSetsList()) {
			result.add(dataSetName);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getPublicDataSets(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential)
	 */
	@Override
	public Set<String> getAccountDataSets(final AccountID accountID, final PasswordCredential credential) {
		final GetAccountDataSetsRequest request = GetAccountDataSetsRequest.newBuilder()
				.setAccountID(Utils.getMsgID(accountID)).setCredential(Utils.getCredential(credential)).build();
		final GetAccountDataSetsResponse response;
		final Function<GetAccountDataSetsRequest, GetAccountDataSetsResponse> f = req -> getBlockingStub()
				.getAccountDataSets(req);
		response = this.<GetAccountDataSetsRequest, GetAccountDataSetsResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		final Set<String> result = new HashSet<>();
		for (final String dataSetName : response.getDataSetsList()) {
			result.add(dataSetName);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#newClass(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential,
	 * communication.grpc.messages.common.CommonMessages.Langs, java.util.Map)
	 */
	@Override
	public Map<String, MetaClass> newClass(final AccountID accountID, final PasswordCredential credential,
			final Langs language, final Map<String, MetaClass> newClasses) {

		final NewClassRequest.Builder builder = NewClassRequest.newBuilder();
		builder.setAccountID(Utils.getMsgID(accountID));
		builder.setCredential(Utils.getCredential(credential));
		builder.setLanguage(language);
		for (final Entry<String, MetaClass> entry : newClasses.entrySet()) {
			final String yaml = CommonYAML.getYamlObject().dump(entry.getValue());
			builder.putNewClasses(entry.getKey(), yaml);
		}
		final NewClassRequest request = builder.build();
		final NewClassResponse response;
		final Function<NewClassRequest, NewClassResponse> f = req -> getBlockingStub().newClass(req);
		response = this.<NewClassRequest, NewClassResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		final Map<String, MetaClass> result = new HashMap<>();
		for (final Entry<String, String> entry : response.getNewClassesMap().entrySet()) {
			final MetaClass clazz = (MetaClass) CommonYAML.getYamlObject().load(entry.getValue());
			result.put(entry.getKey(), clazz);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#newClassID(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, java.lang.String,
	 * communication.grpc.messages.common.CommonMessages.Langs, java.util.Map)
	 */
	@Override
	public MetaClassID newClassID(final AccountID accountID, final PasswordCredential credential,
			final String className, final Langs language, final Map<String, MetaClass> newClasses) {
		final NewClassIDRequest.Builder builder = NewClassIDRequest.newBuilder();
		builder.setAccountID(Utils.getMsgID(accountID));
		builder.setCredential(Utils.getCredential(credential));
		builder.setClassName(className);
		builder.setLanguage(language);
		for (final Entry<String, MetaClass> entry : newClasses.entrySet()) {
			final String yaml = CommonYAML.getYamlObject().dump(entry.getValue());
			builder.putNewClasses(entry.getKey(), yaml);
		}
		final NewClassIDRequest request = builder.build();
		final NewClassIDResponse response;
		final Function<NewClassIDRequest, NewClassIDResponse> f = req -> getBlockingStub().newClassID(req);
		response = this.<NewClassIDRequest, NewClassIDResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getClassID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#removeClass(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, util.ids.NamespaceID,
	 * java.lang.String)
	 */
	@Override
	public void removeClass(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final String className) {
		final RemoveClassRequest request = RemoveClassRequest.newBuilder().setAccountID(Utils.getMsgID(accountID))
				.setCredential(Utils.getCredential(credential)).setClassName(className).build();
		final ExceptionInfo response;
		final Function<RemoveClassRequest, ExceptionInfo> f = req -> getBlockingStub().removeClass(req);
		response = this.<RemoveClassRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#removeOperation(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, util.ids.NamespaceID,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void removeOperation(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final String className, final String operationSignature) {
		final RemoveOperationRequest request = RemoveOperationRequest.newBuilder()
				.setAccountID(Utils.getMsgID(accountID)).setCredential(Utils.getCredential(credential))
				.setNamespaceID(Utils.getMsgID(namespaceID)).setClassName(className)
				.setOperationNameAndSignature(operationSignature).build();
		final ExceptionInfo response;
		final Function<RemoveOperationRequest, ExceptionInfo> f = req -> getBlockingStub().removeOperation(req);
		response = this.<RemoveOperationRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#removeImplementation(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, util.ids.NamespaceID,
	 * util.ids.ImplementationID)
	 */
	@Override
	public void removeImplementation(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final ImplementationID implementationID) {
		final RemoveImplementationRequest request = RemoveImplementationRequest.newBuilder()
				.setAccountID(Utils.getMsgID(accountID)).setCredential(Utils.getCredential(credential))
				.setNamespaceID(Utils.getMsgID(namespaceID)).setImplementationID(Utils.getMsgID(implementationID))
				.build();
		final ExceptionInfo response;
		final Function<RemoveImplementationRequest, ExceptionInfo> f = req -> getBlockingStub()
				.removeImplementation(req);
		response = this.<RemoveImplementationRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getOperationID(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, util.ids.NamespaceID,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public OperationID getOperationID(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final String className, final String operationSignature) {
		final GetOperationIDRequest request = GetOperationIDRequest.newBuilder().setAccountID(Utils.getMsgID(accountID))
				.setCredential(Utils.getCredential(credential)).setClassName(className)
				.setOperationNameAndSignature(operationSignature).build();
		final GetOperationIDResponse response;
		final Function<GetOperationIDRequest, GetOperationIDResponse> f = req -> getBlockingStub().getOperationID(req);
		response = this.<GetOperationIDRequest, GetOperationIDResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getOperationID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getPropertyID(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, util.ids.NamespaceID,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public PropertyID getPropertyID(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final String className, final String propertyName) {
		final GetPropertyIDRequest request = GetPropertyIDRequest.newBuilder().setAccountID(Utils.getMsgID(accountID))
				.setCredential(Utils.getCredential(credential)).setNamespaceID(Utils.getMsgID(namespaceID))
				.setClassName(className).setPropertyName(propertyName).build();
		final GetPropertyIDResponse response;
		final Function<GetPropertyIDRequest, GetPropertyIDResponse> f = req -> getBlockingStub().getPropertyID(req);
		response = this.<GetPropertyIDRequest, GetPropertyIDResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getPropertyID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getClassID(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, util.ids.NamespaceID,
	 * java.lang.String)
	 */
	@Override
	public MetaClassID getClassID(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final String className) {
		final GetClassIDRequest request = GetClassIDRequest.newBuilder().setAccountID(Utils.getMsgID(accountID))
				.setCredential(Utils.getCredential(credential)).setNamespaceID(Utils.getMsgID(namespaceID))
				.setClassName(className).build();
		final GetClassIDResponse response;
		final Function<GetClassIDRequest, GetClassIDResponse> f = req -> getBlockingStub().getClassID(req);
		response = this.<GetClassIDRequest, GetClassIDResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getClassID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getClassInfo(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, util.ids.NamespaceID,
	 * java.lang.String)
	 */
	@Override
	public MetaClass getClassInfo(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final String className) {
		final GetClassInfoRequest request = GetClassInfoRequest.newBuilder().setAccountID(Utils.getMsgID(accountID))
				.setCredential(Utils.getCredential(credential)).setNamespaceID(Utils.getMsgID(namespaceID))
				.setClassName(className).build();
		final GetClassInfoResponse response;
		final Function<GetClassInfoRequest, GetClassInfoResponse> f = req -> getBlockingStub().getClassInfo(req);
		response = this.<GetClassInfoRequest, GetClassInfoResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		final MetaClass result = (MetaClass) CommonYAML.getYamlObject().load(response.getMetaClassYaml());
		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getInfoOfClassesInNamespace(dataClay.util.ids.
	 * AccountID, dataClay.util.management.accountmgr.Credential,
	 * util.ids.NamespaceID)
	 */
	@Override
	public Map<MetaClassID, MetaClass> getInfoOfClassesInNamespace(final AccountID accountID,
			final PasswordCredential credential, final NamespaceID namespaceID) {
		final GetInfoOfClassesInNamespaceRequest request = GetInfoOfClassesInNamespaceRequest.newBuilder()
				.setAccountID(Utils.getMsgID(accountID)).setCredential(Utils.getCredential(credential))
				.setNamespaceID(Utils.getMsgID(namespaceID)).build();
		GetInfoOfClassesInNamespaceResponse response = null;
		final Function<GetInfoOfClassesInNamespaceRequest, GetInfoOfClassesInNamespaceResponse> f = req -> getBlockingStub()
				.getInfoOfClassesInNamespace(req);
		response = this
				.<GetInfoOfClassesInNamespaceRequest, GetInfoOfClassesInNamespaceResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		final Map<MetaClassID, MetaClass> result = new HashMap<>();
		for (final Entry<String, String> entry : response.getClassesInfoMap().entrySet()) {
			final MetaClass clazz = (MetaClass) CommonYAML.getYamlObject().load(entry.getValue());
			result.put(Utils.getMetaClassIDFromUUID(entry.getKey()), clazz);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#newContract(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential,
	 * util.management.contractmgr.Contract)
	 */
	@Override
	public ContractID newContract(final AccountID accountID, final PasswordCredential credential,
			final Contract newContract) {
		final String yaml = CommonYAML.getYamlObject().dump(newContract);
		final NewContractRequest request = NewContractRequest.newBuilder().setAccountID(Utils.getMsgID(accountID))
				.setCredential(Utils.getCredential(credential)).setNewContractYaml(yaml).build();

		final NewContractResponse response;
		final Function<NewContractRequest, NewContractResponse> f = req -> getBlockingStub().newContract(req);
		response = this.<NewContractRequest, NewContractResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getContractID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#registerToPublicContract(dataClay.util.ids.
	 * AccountID, dataClay.util.management.accountmgr.Credential,
	 * util.ids.ContractID)
	 */
	@Override
	public void registerToPublicContract(final AccountID accountID, final PasswordCredential credential,
			final ContractID contractID) {
		final RegisterToPublicContractRequest request = RegisterToPublicContractRequest.newBuilder()
				.setAccountID(Utils.getMsgID(accountID)).setCredential(Utils.getCredential(credential))
				.setContractID(Utils.getMsgID(contractID)).build();
		final ExceptionInfo response;
		final Function<RegisterToPublicContractRequest, ExceptionInfo> f = req -> getBlockingStub()
				.registerToPublicContract(req);
		response = this.<RegisterToPublicContractRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#registerToPublicContractOfNamespace(dataClay.util.
	 * ids.AccountID, dataClay.util.management.accountmgr.Credential,
	 * util.ids.NamespaceID)
	 */
	@Override
	public ContractID registerToPublicContractOfNamespace(final AccountID accountID,
			final PasswordCredential credential, final NamespaceID namespaceID) {
		final RegisterToPublicContractOfNamespaceResponse response;
		final RegisterToPublicContractOfNamespaceRequest request = RegisterToPublicContractOfNamespaceRequest
				.newBuilder().setAccountID(Utils.getMsgID(accountID)).setCredential(Utils.getCredential(credential))
				.setNamespaceID(Utils.getMsgID(namespaceID)).build();
		final Function<RegisterToPublicContractOfNamespaceRequest, RegisterToPublicContractOfNamespaceResponse> f = req -> getBlockingStub()
				.registerToPublicContractOfNamespace(req);
		response = this
				.<RegisterToPublicContractOfNamespaceRequest, RegisterToPublicContractOfNamespaceResponse>callLogicModule(
						request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getContractID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getContractIDsOfApplicant(dataClay.util.ids.
	 * AccountID, dataClay.util.management.accountmgr.Credential)
	 */
	@Override
	public Map<ContractID, Contract> getContractIDsOfApplicant(final AccountID applicantAccountID,
			final PasswordCredential credential) {
		final GetContractIDsOfApplicantRequest request = GetContractIDsOfApplicantRequest.newBuilder()
				.setApplicantID(Utils.getMsgID(applicantAccountID)).setCredential(Utils.getCredential(credential))
				.build();
		final GetContractIDsOfApplicantResponse response;
		final Function<GetContractIDsOfApplicantRequest, GetContractIDsOfApplicantResponse> f = req -> getBlockingStub()
				.getContractIDsOfApplicant(req);
		response = this.<GetContractIDsOfApplicantRequest, GetContractIDsOfApplicantResponse>callLogicModule(request,
				f);
		Utils.checkIsExc(response.getExcInfo());

		final Map<ContractID, Contract> result = new HashMap<>();
		for (final Entry<String, String> entry : response.getContractsMap().entrySet()) {
			result.put(Utils.getContractIDFromUUID(entry.getKey()),
					(Contract) CommonYAML.getYamlObject().load(entry.getValue()));
		}
		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getContractIDsOfProvider(dataClay.util.ids.
	 * AccountID, dataClay.util.management.accountmgr.Credential,
	 * util.ids.NamespaceID)
	 */
	@Override
	public Map<ContractID, Contract> getContractIDsOfProvider(final AccountID accountID,
			final PasswordCredential credential, final NamespaceID namespaceIDofProvider) {
		final GetContractIDsOfProviderRequest request = GetContractIDsOfProviderRequest.newBuilder()
				.setProviderID(Utils.getMsgID(accountID)).setCredential(Utils.getCredential(credential))
				.setNamespaceIDOfProvider(Utils.getMsgID(namespaceIDofProvider)).build();
		final GetContractIDsOfProviderResponse response;
		final Function<GetContractIDsOfProviderRequest, GetContractIDsOfProviderResponse> f = req -> getBlockingStub()
				.getContractIDsOfProvider(req);
		response = this.<GetContractIDsOfProviderRequest, GetContractIDsOfProviderResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());

		final Map<ContractID, Contract> result = new HashMap<>();
		for (final Entry<String, String> entry : response.getContractsMap().entrySet()) {
			result.put(Utils.getContractIDFromUUID(entry.getKey()),
					(Contract) CommonYAML.getYamlObject().load(entry.getValue()));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#getContractIDsOfApplicantWithProvider(dataClay.util.
	 * ids.AccountID, util.management.accountmgr.Credential, util.ids.NamespaceID)
	 */
	@Override
	public Map<ContractID, Contract> getContractIDsOfApplicantWithProvider(final AccountID applicantAccountID,
			final PasswordCredential credential, final NamespaceID namespaceIDofProvider) {
		final GetContractsOfApplicantWithProvRequest request = GetContractsOfApplicantWithProvRequest.newBuilder()
				.setApplicantID(Utils.getMsgID(applicantAccountID)).setCredential(Utils.getCredential(credential))
				.setNamespaceIDOfProvider(Utils.getMsgID(namespaceIDofProvider)).build();
		final GetContractsOfApplicantWithProvResponse response;
		final Function<GetContractsOfApplicantWithProvRequest, GetContractsOfApplicantWithProvResponse> f = req -> getBlockingStub()
				.getContractIDsOfApplicantWithProvider(req);
		response = this
				.<GetContractsOfApplicantWithProvRequest, GetContractsOfApplicantWithProvResponse>callLogicModule(
						request, f);
		Utils.checkIsExc(response.getExcInfo());

		final Map<ContractID, Contract> result = new HashMap<>();
		for (final Entry<String, String> entry : response.getContractsMap().entrySet()) {
			result.put(Utils.getContractIDFromUUID(entry.getKey()),
					(Contract) CommonYAML.getYamlObject().load(entry.getValue()));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#newDataContract(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential,
	 * util.management.datacontractmgr.DataContract)
	 */
	@Override
	public DataContractID newDataContract(final AccountID accountID, final PasswordCredential credential,
			final DataContract newDataContract) {
		final String yaml = CommonYAML.getYamlObject().dump(newDataContract);
		final NewDataContractRequest request = NewDataContractRequest.newBuilder()
				.setAccountID(Utils.getMsgID(accountID)).setCredential(Utils.getCredential(credential))
				.setDataContractYaml(yaml).build();

		final NewDataContractResponse response;
		final Function<NewDataContractRequest, NewDataContractResponse> f = req -> getBlockingStub()
				.newDataContract(req);
		response = this.<NewDataContractRequest, NewDataContractResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getDataContractID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#registerToPublicDataContract(dataClay.util.ids.
	 * AccountID, dataClay.util.management.accountmgr.Credential,
	 * util.ids.DataContractID)
	 */
	@Override
	public void registerToPublicDataContract(final AccountID accountID, final PasswordCredential credential,
			final DataContractID datacontractID) {
		final RegisterToPublicDataContractRequest request = RegisterToPublicDataContractRequest.newBuilder()
				.setAccountID(Utils.getMsgID(accountID)).setCredential(Utils.getCredential(credential))
				.setDataContractID(Utils.getMsgID(datacontractID)).build();
		final ExceptionInfo response;
		final Function<RegisterToPublicDataContractRequest, ExceptionInfo> f = req -> getBlockingStub()
				.registerToPublicDataContract(req);
		response = this.<RegisterToPublicDataContractRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getDataContractIDsOfProvider(dataClay.util.ids.
	 * AccountID, dataClay.util.management.accountmgr.Credential,
	 * util.ids.DataSetID)
	 */
	@Override
	public Map<DataContractID, DataContract> getDataContractIDsOfProvider(final AccountID accountID,
			final PasswordCredential credential, final DataSetID datasetIDofProvider) {
		final GetDataContractIDsOfProviderRequest request = GetDataContractIDsOfProviderRequest.newBuilder()
				.setProviderID(Utils.getMsgID(accountID)).setCredential(Utils.getCredential(credential))
				.setDataSetIDOfProvider(Utils.getMsgID(datasetIDofProvider)).build();
		final GetDataContractIDsOfProviderResponse response;
		final Function<GetDataContractIDsOfProviderRequest, GetDataContractIDsOfProviderResponse> f = req -> getBlockingStub()
				.getDataContractIDsOfProvider(req);
		response = this
				.<GetDataContractIDsOfProviderRequest, GetDataContractIDsOfProviderResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());

		final Map<DataContractID, DataContract> result = new HashMap<>();
		for (final Entry<String, String> entry : response.getDatacontractsMap().entrySet()) {
			result.put(Utils.getDataContractIDFromUUID(entry.getKey()),
					(DataContract) CommonYAML.getYamlObject().load(entry.getValue()));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#getDataContractIDsOfApplicant(dataClay.util.ids.
	 * AccountID, dataClay.util.management.accountmgr.Credential)
	 */
	@Override
	public Map<DataContractID, DataContract> getDataContractIDsOfApplicant(final AccountID applicantAccountID,
			final PasswordCredential credential) {
		final GetDataContractIDsOfApplicantRequest request = GetDataContractIDsOfApplicantRequest.newBuilder()
				.setApplicantID(Utils.getMsgID(applicantAccountID)).setCredential(Utils.getCredential(credential))
				.build();
		final GetDataContractIDsOfApplicantResponse response;
		final Function<GetDataContractIDsOfApplicantRequest, GetDataContractIDsOfApplicantResponse> f = req -> getBlockingStub()
				.getDataContractIDsOfApplicant(req);
		response = this.<GetDataContractIDsOfApplicantRequest, GetDataContractIDsOfApplicantResponse>callLogicModule(
				request, f);
		Utils.checkIsExc(response.getExcInfo());

		final Map<DataContractID, DataContract> result = new HashMap<>();
		for (final Entry<String, String> entry : response.getDatacontractsMap().entrySet()) {
			result.put(Utils.getDataContractIDFromUUID(entry.getKey()),
					(DataContract) CommonYAML.getYamlObject().load(entry.getValue()));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#getDataContractInfoOfApplicantWithProvider(dataClay.
	 * util.ids.AccountID, util.management.accountmgr.Credential,
	 * util.ids.DataSetID)
	 */
	@Override
	public DataContract getDataContractInfoOfApplicantWithProvider(final AccountID applicantAccountID,
			final PasswordCredential credential, final DataSetID datasetIDofProvider) {
		final GetDataContractInfoOfApplicantWithProvRequest request = GetDataContractInfoOfApplicantWithProvRequest
				.newBuilder().setApplicantID(Utils.getMsgID(applicantAccountID))
				.setCredential(Utils.getCredential(credential))
				.setDataSetIDOfProvider(Utils.getMsgID(datasetIDofProvider)).build();
		final GetDataContractInfoOfApplicantWithProvResponse response;
		final Function<GetDataContractInfoOfApplicantWithProvRequest, GetDataContractInfoOfApplicantWithProvResponse> f = req -> getBlockingStub()
				.getDataContractInfoOfApplicantWithProvider(req);
		response = this
				.<GetDataContractInfoOfApplicantWithProvRequest, GetDataContractInfoOfApplicantWithProvResponse>callLogicModule(
						request, f);
		Utils.checkIsExc(response.getExcInfo());

		return (DataContract) CommonYAML.getYamlObject().load(response.getDataContractInfo());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#newInterface(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential,
	 * util.management.interfacemgr.Interface)
	 */
	@Override
	public InterfaceID newInterface(final AccountID accountID, final PasswordCredential credential,
			final Interface newInterface) {
		final String yaml = CommonYAML.getYamlObject().dump(newInterface);
		final NewInterfaceRequest request = NewInterfaceRequest.newBuilder().setApplicantID(Utils.getMsgID(accountID))
				.setCredential(Utils.getCredential(credential)).setInterfaceYaml(yaml).build();

		final NewInterfaceResponse response;
		final Function<NewInterfaceRequest, NewInterfaceResponse> f = req -> getBlockingStub().newInterface(req);
		response = this.<NewInterfaceRequest, NewInterfaceResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getInterfaceID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getInterfaceInfo(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, util.ids.InterfaceID)
	 */
	@Override
	public Interface getInterfaceInfo(final AccountID accountID, final PasswordCredential credential,
			final InterfaceID interfaceID) {
		final GetInterfaceInfoRequest request = GetInterfaceInfoRequest.newBuilder()
				.setApplicantID(Utils.getMsgID(accountID)).setCredential(Utils.getCredential(credential))
				.setInterfaceID(Utils.getMsgID(interfaceID)).build();

		final GetInterfaceInfoResponse response;
		final Function<GetInterfaceInfoRequest, GetInterfaceInfoResponse> f = req -> getBlockingStub()
				.getInterfaceInfo(req);
		response = this.<GetInterfaceInfoRequest, GetInterfaceInfoResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return (Interface) CommonYAML.getYamlObject().load(response.getInterfaceYaml());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#removeInterface(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, util.ids.NamespaceID,
	 * util.ids.InterfaceID)
	 */
	@Override
	public void removeInterface(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final InterfaceID interfaceID) {
		final RemoveInterfaceRequest request = RemoveInterfaceRequest.newBuilder()
				.setApplicantID(Utils.getMsgID(accountID)).setCredential(Utils.getCredential(credential))
				.setNamespaceID(Utils.getMsgID(namespaceID)).setInterfaceID(Utils.getMsgID(interfaceID)).build();
		final ExceptionInfo response;
		final Function<RemoveInterfaceRequest, ExceptionInfo> f = req -> getBlockingStub().removeInterface(req);
		response = this.<RemoveInterfaceRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);

	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getStorageLocationForDS(dataClay.util.ids.
	 * StorageLocationID)
	 */
	@Override
	public StorageLocation getStorageLocationForDS(final StorageLocationID backendID) {
		final GetStorageLocationForDSRequest request = GetStorageLocationForDSRequest.newBuilder()
				.setStorageLocationID(Utils.getMsgID(backendID)).build();
		final GetStorageLocationForDSResponse response;
		final Function<GetStorageLocationForDSRequest, GetStorageLocationForDSResponse> f = req -> getBlockingStub()
				.getStorageLocationForDS(req);
		response = this.<GetStorageLocationForDSRequest, GetStorageLocationForDSResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return (StorageLocation) CommonYAML.getYamlObject().load(response.getStorageLocationYaml());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getExecutionEnvironmentForDS(dataClay.util.ids.
	 * ExecutionEnvironmentID)
	 */
	@Override
	public ExecutionEnvironment getExecutionEnvironmentForDS(final ExecutionEnvironmentID backendID) {
		final GetExecutionEnvironmentForDSRequest request = GetExecutionEnvironmentForDSRequest.newBuilder()
				.setExecEnvID(Utils.getMsgID(backendID)).build();
		final GetExecutionEnvironmentForDSResponse response;
		final Function<GetExecutionEnvironmentForDSRequest, GetExecutionEnvironmentForDSResponse> f = req -> getBlockingStub()
				.getExecutionEnvironmentForDS(req);
		response = this
				.<GetExecutionEnvironmentForDSRequest, GetExecutionEnvironmentForDSResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return (ExecutionEnvironment) CommonYAML.getYamlObject().load(response.getExecEnvYaml());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getDataClayID()
	 */
	@Override
	public DataClayInstanceID getDataClayID() {
		final GetDataClayIDResponse response;
		final Function<EmptyMessage, GetDataClayIDResponse> f = req -> getBlockingStub().getDataClayID(req);
		response = this.<EmptyMessage, GetDataClayIDResponse>callLogicModule(EmptyMessage.newBuilder().build(), f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getDataClayID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getExternalDataClayInfo(dataClay.util.ids.
	 * DataClayInstanceID)
	 */
	@Override
	public DataClayInstance getExternalDataClayInfo(final DataClayInstanceID extDataClayID) {
		final GetExtDataClayInfoRequest request = GetExtDataClayInfoRequest.newBuilder()
				.setExtDataClayID(Utils.getMsgID(extDataClayID)).build();
		final GetExtDataClayInfoResponse response;
		final Function<GetExtDataClayInfoRequest, GetExtDataClayInfoResponse> f = req -> getBlockingStub()
				.getExternalDataClayInfo(req);
		response = this.<GetExtDataClayInfoRequest, GetExtDataClayInfoResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return (DataClayInstance) CommonYAML.getYamlObject().load(response.getExtDataClayYaml());
	}

	@Override
	public DataClayInstanceID getExternalDataClayID(final String dcHost, final int dcPort) {
		final GetExternalDataclayIDRequest request = GetExternalDataclayIDRequest.newBuilder().setHost(dcHost)
				.setPort(dcPort).build();
		final GetExternalDataclayIDResponse response;
		final Function<GetExternalDataclayIDRequest, GetExternalDataclayIDResponse> f = req -> getBlockingStub()
				.getExternalDataclayId(req);
		response = this.<GetExternalDataclayIDRequest, GetExternalDataclayIDResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getExtDataClayID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#registerExternalDataClay(dataClay.util.ids.
	 * DataClayInstanceID String, String, int)
	 */
	@Override
	public DataClayInstanceID registerExternalDataClay(final String hostname, final int port) {
		final RegisterExternalDataClayRequest request = RegisterExternalDataClayRequest.newBuilder()
				.setHostname(hostname).setPort(port).build();
		final RegisterExternalDataClayResponse response;
		final Function<RegisterExternalDataClayRequest, RegisterExternalDataClayResponse> f = req -> getBlockingStub()
				.registerExternalDataClay(req);
		response = this.<RegisterExternalDataClayRequest, RegisterExternalDataClayResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getExtDataClayID());
	}

	@Override
	public DataClayInstanceID registerExternalDataClayOverrideAuthority(final AccountID adminAccountID, final PasswordCredential adminCredential,
			final String hostname, final int port, final String authority) {
		final RegisterExternalDataClayOverrideAuthorityRequest request = RegisterExternalDataClayOverrideAuthorityRequest.newBuilder()
				.setAdminAccountID(Utils.getMsgID(adminAccountID)).setAdminCredential(Utils.getCredential(adminCredential))
				.setHostname(hostname).setPort(port).setAuthority(authority).build();
		final RegisterExternalDataClayResponse response;
		final Function<RegisterExternalDataClayOverrideAuthorityRequest, RegisterExternalDataClayResponse> f = req -> getBlockingStub()
				.registerExternalDataClayOverrideAuthority(req);
		response = this.<RegisterExternalDataClayOverrideAuthorityRequest, RegisterExternalDataClayResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getExtDataClayID());
	}
	
	@Override
	public DataClayInstanceID notifyRegistrationOfExternalDataClay(final DataClayInstanceID dataClayInstanceID,
			final String hostname, final int port) {
		final NotifyRegistrationOfExternalDataClayRequest request = NotifyRegistrationOfExternalDataClayRequest
				.newBuilder().setExtDataClayID(Utils.getMsgID(dataClayInstanceID)).setHostname(hostname).setPort(port)
				.build();
		final NotifyRegistrationOfExternalDataClayResponse response;
		final Function<NotifyRegistrationOfExternalDataClayRequest, NotifyRegistrationOfExternalDataClayResponse> f = req -> getBlockingStub()
				.notifyRegistrationOfExternalDataClay(req);
		response = this
				.<NotifyRegistrationOfExternalDataClayRequest, NotifyRegistrationOfExternalDataClayResponse>callLogicModule(
						request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getExtDataClayID());
	}

	@Override
	public void federateObject(final SessionID sessionID, final ObjectID objectID,
			final DataClayInstanceID extDataClayID, final boolean recursive) {
		final FederateObjectRequest request = FederateObjectRequest.newBuilder().setSessionID(Utils.getMsgID(sessionID))
				.setObjectID(Utils.getMsgID(objectID)).setExtDataClayID(Utils.getMsgID(extDataClayID))
				.setRecursive(recursive).build();
		final ExceptionInfo response;
		final Function<FederateObjectRequest, ExceptionInfo> f = req -> getBlockingStub().federateObject(req);
		response = this.<FederateObjectRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}

	@Override
	public void unfederateObject(final SessionID sessionID, final ObjectID objectID,
			final DataClayInstanceID extDataClayID, final boolean recursive) {
		final UnfederateObjectRequest request = UnfederateObjectRequest.newBuilder()
				.setSessionID(Utils.getMsgID(sessionID)).setObjectID(Utils.getMsgID(objectID))
				.setExtDataClayID(Utils.getMsgID(extDataClayID)).setRecursive(recursive).build();
		final ExceptionInfo response;
		final Function<UnfederateObjectRequest, ExceptionInfo> f = req -> getBlockingStub().unfederateObject(req);
		response = this.<UnfederateObjectRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}
	
	@Override
	public void unfederateObjectWithAllDCs(final SessionID sessionID, final ObjectID objectID,
			final boolean recursive) {
		final UnfederateObjectWithAllDCsRequest request = UnfederateObjectWithAllDCsRequest.newBuilder()
				.setSessionID(Utils.getMsgID(sessionID)).setObjectID(Utils.getMsgID(objectID))
				.setRecursive(recursive).build();
		final ExceptionInfo response;
		final Function<UnfederateObjectWithAllDCsRequest, ExceptionInfo> f = req -> getBlockingStub().unfederateObjectWithAllDCs(req);
		response = this.<UnfederateObjectWithAllDCsRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}
	
	
	@Override
	public void federateAllObjects(final SessionID sessionID,
			final DataClayInstanceID externalDestinationDataClayID) {
		final FederateAllObjectsRequest request = FederateAllObjectsRequest.newBuilder()
				.setSessionID(Utils.getMsgID(sessionID))
				.setExternalDestinationDataClayID(Utils.getMsgID(externalDestinationDataClayID)).build();
		final ExceptionInfo response;
		final Function<FederateAllObjectsRequest, ExceptionInfo> f = req -> getBlockingStub().federateAllObjects(req);
		response = this.<FederateAllObjectsRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}
	
	@Override
	public void migrateFederatedObjects(final SessionID sessionID,
			final DataClayInstanceID externalOriginDataClayID, 
			final DataClayInstanceID externalDestinationDataClayID) {
		final MigrateFederatedObjectsRequest request = MigrateFederatedObjectsRequest.newBuilder()
				.setSessionID(Utils.getMsgID(sessionID)).setExternalOriginDataClayID(Utils.getMsgID(externalOriginDataClayID))
				.setExternalDestinationDataClayID(Utils.getMsgID(externalDestinationDataClayID)).build();
		final ExceptionInfo response;
		final Function<MigrateFederatedObjectsRequest, ExceptionInfo> f = req -> getBlockingStub().migrateFederatedObjects(req);
		response = this.<MigrateFederatedObjectsRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}
	
	
	@Override
	public void unfederateAllObjects(final SessionID sessionID, 
			final DataClayInstanceID extDataClayID) {
		final UnfederateAllObjectsRequest request = UnfederateAllObjectsRequest.newBuilder()
				.setSessionID(Utils.getMsgID(sessionID))
				.setExtDataClayID(Utils.getMsgID(extDataClayID)).build();
		final ExceptionInfo response;
		final Function<UnfederateAllObjectsRequest, ExceptionInfo> f = req -> getBlockingStub().unfederateAllObjects(req);
		response = this.<UnfederateAllObjectsRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}

	@Override
	public void unfederateAllObjectsWithAllDCs(final SessionID sessionID) {
		final UnfederateAllObjectsWithAllDCsRequest request = UnfederateAllObjectsWithAllDCsRequest.newBuilder()
				.setSessionID(Utils.getMsgID(sessionID)).build();
		final ExceptionInfo response;
		final Function<UnfederateAllObjectsWithAllDCsRequest, ExceptionInfo> f = req -> getBlockingStub().unfederateAllObjectsWithAllDCs(req);
		response = this.<UnfederateAllObjectsWithAllDCsRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}
	
	@Override
	public void notifyFederatedObjects(final DataClayInstanceID srcDataClayID, final String srcDcHost,
			final int srcDcPort, final Map<ObjectID, MetaDataInfo> objectsInfo, 
			final Map<Langs, SerializedParametersOrReturn> federatedObjects) {
		final NotifyFederatedObjectsRequest.Builder requestBuilder = NotifyFederatedObjectsRequest.newBuilder();

		for (final Entry<ObjectID, MetaDataInfo> entry : objectsInfo.entrySet()) {
			final String objectInfo = CommonYAML.getYamlObject().dump(entry.getValue());
			requestBuilder.putObjectsInfo(entry.getKey().toString(), objectInfo);
		}
		for (final Entry<Langs, SerializedParametersOrReturn> entry : federatedObjects.entrySet()) {
			final String langStr = entry.getKey().name();
			requestBuilder.putFederatedObjects(langStr, Utils.getParamsOrReturn(entry.getValue()));
		}
		
		final NotifyFederatedObjectsRequest request = requestBuilder.setSrcDcID(Utils.getMsgID(srcDataClayID))
				.setSrcDcHost(srcDcHost).setSrcDcPort(srcDcPort).build();
		final ExceptionInfo response;
		final Function<NotifyFederatedObjectsRequest, ExceptionInfo> f = req -> getBlockingStub()
				.notifyFederatedObjects(req);
		response = this.<NotifyFederatedObjectsRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}

	@Override
	public void notifyUnfederatedObjects(final DataClayInstanceID srcDataClayID, final Set<ObjectID> objectIDs) {
		final NotifyUnfederatedObjectsRequest.Builder requestBuilder = NotifyUnfederatedObjectsRequest.newBuilder();

		for (final ObjectID oid : objectIDs) {
			requestBuilder.addObjectsIDs(Utils.getMsgID(oid));
		}

		final NotifyUnfederatedObjectsRequest request = requestBuilder.setSrcDcID(Utils.getMsgID(srcDataClayID))
				.build();
		final ExceptionInfo response;
		final Function<NotifyUnfederatedObjectsRequest, ExceptionInfo> f = req -> getBlockingStub()
				.notifyUnfederatedObjects(req);
		response = this.<NotifyUnfederatedObjectsRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}


	@Override
	public boolean checkObjectIsFederatedWithDataClayInstance(final ObjectID objectID,
			final DataClayInstanceID extDataClayID) {
		final CheckObjectFederatedWithDataClayInstanceRequest request = CheckObjectFederatedWithDataClayInstanceRequest
				.newBuilder().setObjectID(Utils.getMsgID(objectID)).setExtDataClayID(Utils.getMsgID(extDataClayID))
				.build();
		final CheckObjectFederatedWithDataClayInstanceResponse response;
		final Function<CheckObjectFederatedWithDataClayInstanceRequest, CheckObjectFederatedWithDataClayInstanceResponse> f = req -> getBlockingStub()
				.checkObjectIsFederatedWithDataClayInstance(req);
		response = this
				.<CheckObjectFederatedWithDataClayInstanceRequest, CheckObjectFederatedWithDataClayInstanceResponse>callLogicModule(
						request, f);
		Utils.checkIsExc(response.getExcInfo());
		return response.getIsFederated();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#getFederationOfObject(dataClay.util.ids.ObjectID)
	 */
	@Override
	public Set<DataClayInstanceID> getDataClaysObjectIsFederatedWith(final ObjectID objectID) {
		final GetDataClaysObjectIsFederatedWithRequest request = GetDataClaysObjectIsFederatedWithRequest.newBuilder()
				.setObjectID(Utils.getMsgID(objectID)).build();
		final GetDataClaysObjectIsFederatedWithResponse response;
		final Function<GetDataClaysObjectIsFederatedWithRequest, GetDataClaysObjectIsFederatedWithResponse> f = req -> getBlockingStub()
				.getDataClaysObjectIsFederatedWith(req);
		response = this.<GetDataClaysObjectIsFederatedWithRequest, GetDataClaysObjectIsFederatedWithResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		final Set<DataClayInstanceID> result = new HashSet<>();
		for (final  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.DataClayInstanceID curInstanceID : response.getExtDataClayIDsList()) {
			result.add(Utils.getID(curInstanceID));
		}
		return result;
	}
	
	@Override
	public DataClayInstanceID getExternalSourceDataClayOfObject(final ObjectID objectID) {
		final GetExternalSourceDataClayOfObjectRequest request = GetExternalSourceDataClayOfObjectRequest.newBuilder()
				.setObjectID(Utils.getMsgID(objectID)).build();
		final GetExternalSourceDataClayOfObjectResponse response;
		final Function<GetExternalSourceDataClayOfObjectRequest, GetExternalSourceDataClayOfObjectResponse> f = req -> getBlockingStub()
				.getExternalSourceDataClayOfObject(req);
		response = this.<GetExternalSourceDataClayOfObjectRequest, GetExternalSourceDataClayOfObjectResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getExtDataClayID());
	}

	@Override
	public void registerObjectFromGC(final RegistrationInfo regInfo, final ExecutionEnvironmentID backendID,
			final DataServiceRuntime clientLib) {

		final RegisterObjectForGCRequest.Builder builder = RegisterObjectForGCRequest.newBuilder();

		final CommonMessages.RegistrationInfo.Builder regInfoBuilder = CommonMessages.RegistrationInfo.newBuilder();
		regInfoBuilder.setObjectID(Utils.getMsgID(regInfo.getObjectID()));
		regInfoBuilder.setClassID(Utils.getMsgID(regInfo.getClassID()));
		regInfoBuilder.setSessionID(Utils.getMsgID(regInfo.getStoreSessionID()));
		regInfoBuilder.setDataSetID(Utils.getMsgID(regInfo.getDataSetID()));

		builder.setBackendID(Utils.getMsgID(backendID));
		builder.setRegInfo(regInfoBuilder.build());
		final RegisterObjectForGCRequest request = builder.build();
		final StreamObserver<ExceptionInfo> responseObserver = new StreamObserver<ExceptionInfo>() {
			@Override
			public void onNext(final ExceptionInfo summary) {
				// do nothing
			}

			@Override
			public void onError(final Throwable t) {
				logger.debug("responseObserver for registerObjectsFromDSGarbageCollector got an error", t);
			}

			@Override
			public void onCompleted() {
				if (DEBUG_ENABLED) {
					logger.debug("[==Communication==] Asynchronous register object finished for object: {}",
							regInfo.getObjectID());
				}
				asyncReqReceived.incrementAndGet();
			}
		};
		if (DEBUG_ENABLED) {
			logger.debug("[==Communication==] Asynchronous call to register object from Garbage Collector. "
					+ "Object: " + regInfo.getObjectID());
		}
		final BiConsumer<RegisterObjectForGCRequest, StreamObserver<ExceptionInfo>> f = (req, obs) -> getAsyncStub()
				.registerObjectFromGC(req, obs);
		this.<RegisterObjectForGCRequest, ExceptionInfo>callLogicModuleAsync(request, responseObserver, f);
	}

	@Override
	public ObjectID registerObject(final RegistrationInfo regInfo, final ExecutionEnvironmentID backendID,
			final String alias, final Langs lang) {

		final RegisterObjectRequest.Builder builder = RegisterObjectRequest.newBuilder();

		final CommonMessages.RegistrationInfo.Builder regInfoBuilder = CommonMessages.RegistrationInfo.newBuilder();
		regInfoBuilder.setObjectID(Utils.getMsgID(regInfo.getObjectID()));
		regInfoBuilder.setClassID(Utils.getMsgID(regInfo.getClassID()));
		regInfoBuilder.setSessionID(Utils.getMsgID(regInfo.getStoreSessionID()));
		regInfoBuilder.setDataSetID(Utils.getMsgID(regInfo.getDataSetID()));
		builder.setRegInfo(regInfoBuilder.build());

		if (alias != null) {
			builder.setAlias(alias);
		}

		builder.setBackendID(Utils.getMsgID(backendID));
		builder.setLang(lang);

		final RegisterObjectRequest request = builder.build();
		final RegisterObjectResponse response;
		final Function<RegisterObjectRequest, RegisterObjectResponse > f = req -> getBlockingStub().registerObject(req);
		response = this.<RegisterObjectRequest, RegisterObjectResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());

		return Utils.getID(response.getObjectID());
	}

	@Override
	public void setDataSetIDFromGarbageCollector(final ObjectID objectID, final DataSetID dataSetID) {
		final SetDataSetIDFromGarbageCollectorRequest request = SetDataSetIDFromGarbageCollectorRequest.newBuilder()
				.setObjectID(Utils.getMsgID(objectID)).setDatasetID(Utils.getMsgID(dataSetID)).build();
		final StreamObserver<ExceptionInfo> responseObserver = new StreamObserver<ExceptionInfo>() {
			@Override
			public void onNext(final ExceptionInfo summary) {
				// do nothing
			}

			@Override
			public void onError(final Throwable t) {
				logger.debug("responseObserver for setDataSetIDFromGarbageCollector got an error", t);
			}

			@Override
			public void onCompleted() {
				asyncReqReceived.incrementAndGet();
			}
		};

		final BiConsumer<SetDataSetIDFromGarbageCollectorRequest, StreamObserver<ExceptionInfo>> f = (req,
				obs) -> getAsyncStub().setDataSetIDFromGarbageCollector(req, obs);
		this.<SetDataSetIDFromGarbageCollectorRequest, ExceptionInfo>callLogicModuleAsync(request, responseObserver, f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getExecutionEnvironmentsInfo(dataClay.util.ids.
	 * AccountID, dataClay.util.management.accountmgr.Credential)
	 */
	@Override
	public Map<ExecutionEnvironmentID, ExecutionEnvironment> getExecutionEnvironmentsInfo(final SessionID sessionID,
			final Langs execEnvLang, final boolean fromClient) {
		final GetExecutionEnvironmentsInfoRequest request = GetExecutionEnvironmentsInfoRequest.newBuilder()
				.setSessionID(Utils.getMsgID(sessionID)).setExecEnvLang(execEnvLang)
				.setFromClient(fromClient).build();
		final GetExecutionEnvironmentsInfoResponse response;
		final Function<GetExecutionEnvironmentsInfoRequest, GetExecutionEnvironmentsInfoResponse> f = req -> getBlockingStub()
				.getExecutionEnvironmentsInfo(req);
		response = this
				.<GetExecutionEnvironmentsInfoRequest, GetExecutionEnvironmentsInfoResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());

		final Map<ExecutionEnvironmentID, ExecutionEnvironment> result = new HashMap<>();
		for (final Entry<String, String> entry : response.getExecEnvsMap().entrySet()) {
			result.put(Utils.getExecutionEnvironmentIDFromUUID(entry.getKey()),
					(ExecutionEnvironment) CommonYAML.getYamlObject().load(entry.getValue()));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#getExecutionEnvironmentsNames(dataClay.util.ids.
	 * AccountID, dataClay.util.management.accountmgr.Credential)
	 */
	@Override
	public Set<String> getExecutionEnvironmentsNames(final AccountID accountID, final PasswordCredential credential,
			final Langs execEnvLang) {
		final GetExecutionEnvironmentsNamesRequest request = GetExecutionEnvironmentsNamesRequest.newBuilder()
				.setAccountID(Utils.getMsgID(accountID)).setCredential(Utils.getCredential(credential))
				.setExecEnvLang(execEnvLang).build();
		final GetExecutionEnvironmentsNamesResponse response;
		final Function<GetExecutionEnvironmentsNamesRequest, GetExecutionEnvironmentsNamesResponse> f = req -> getBlockingStub()
				.getExecutionEnvironmentsNames(req);
		response = this.<GetExecutionEnvironmentsNamesRequest, GetExecutionEnvironmentsNamesResponse>callLogicModule(
				request, f);
		Utils.checkIsExc(response.getExcInfo());

		final Set<String> result = new HashSet<>();
		for (final String id : response.getExecEnvsList()) {
			result.add(id);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#getStorageLocationsPerExecutionEnvironments(dataClay
	 * .util.ids.sessionID, dataClay.util.ids.ObjectID, dataClay.util.ids.DataSetID)
	 */
	@Override
	public void setDataSetID(final SessionID sessionID, final ObjectID objectID, final DataSetID dataSetID) {
		final SetDataSetIDRequest request = SetDataSetIDRequest.newBuilder().setSessionID(Utils.getMsgID(sessionID))
				.setObjectID(Utils.getMsgID(objectID)).setDatasetID(Utils.getMsgID(dataSetID)).build();
		final ExceptionInfo response;
		final Function<SetDataSetIDRequest, ExceptionInfo> f = req -> getBlockingStub().setDataSetID(req);
		response = this.<SetDataSetIDRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getObjectInfo(dataClay.util.ids.SessionID,
	 * util.ids.ObjectID)
	 */
	@Override
	public Tuple<String, String> getObjectInfo(final SessionID sessionID, final ObjectID objectID) {
		final GetObjectInfoRequest request = GetObjectInfoRequest.newBuilder().setSessionID(Utils.getMsgID(sessionID))
				.setObjectID(Utils.getMsgID(objectID)).build();
		final GetObjectInfoResponse response;
		final Function<GetObjectInfoRequest, GetObjectInfoResponse> f = req -> getBlockingStub().getObjectInfo(req);
		response = this.<GetObjectInfoRequest, GetObjectInfoResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return new Tuple<>(response.getClassname(), response.getNamespace());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#getObjectIDfromAlias(dataClay.util.ids.SessionID,
	 * java.lang.String)
	 */
	@Override
	public Triple<ObjectID, MetaClassID, ExecutionEnvironmentID> getObjectFromAlias(final SessionID sessionID,
			final String alias) {
		final GetObjectFromAliasRequest request = GetObjectFromAliasRequest.newBuilder()
				.setSessionID(Utils.getMsgID(sessionID)).setAlias(alias).build();
		final GetObjectFromAliasResponse response;
		final Function<GetObjectFromAliasRequest, GetObjectFromAliasResponse> f = req -> getBlockingStub()
				.getObjectFromAlias(req);
		response = this.<GetObjectFromAliasRequest, GetObjectFromAliasResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return new Triple<>(Utils.getID(response.getObjectID()), Utils.getID(response.getClassID()),
				Utils.getID(response.getHint()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#deleteAlias(dataClay.util.ids.SessionID,
	 * util.ids.MetaClassID, java.lang.String)
	 */
	@Override
	public void deleteAlias(final SessionID sessionID, final String alias) {
		final DeleteAliasRequest request = DeleteAliasRequest.newBuilder().setSessionID(Utils.getMsgID(sessionID))
				.setAlias(alias).build();
		final ExceptionInfo response;
		final Function<DeleteAliasRequest, ExceptionInfo> f = req -> getBlockingStub().deleteAlias(req);
		response = this.<DeleteAliasRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#getObjectsMetaDataInfoOfClassForNM(dataClay.util.ids
	 * .MetaClassID)
	 */
	@Override
	public HashMap<ObjectID, MetaDataInfo> getObjectsMetaDataInfoOfClassForNM(final MetaClassID classID) {
		final GetObjectsMetaDataInfoOfClassForNMRequest request = GetObjectsMetaDataInfoOfClassForNMRequest.newBuilder()
				.setClassID(Utils.getMsgID(classID)).build();

		final GetObjectsMetaDataInfoOfClassForNMResponse response;
		final Function<GetObjectsMetaDataInfoOfClassForNMRequest, GetObjectsMetaDataInfoOfClassForNMResponse> f = req -> getBlockingStub()
				.getObjectsMetaDataInfoOfClassForNM(req);
		response = this
				.<GetObjectsMetaDataInfoOfClassForNMRequest, GetObjectsMetaDataInfoOfClassForNMResponse>callLogicModule(
						request, f);
		Utils.checkIsExc(response.getExcInfo());
		final HashMap<ObjectID, MetaDataInfo> result = new HashMap<>();
		for (final Entry<String, String> entry : response.getMdataInfoMap().entrySet()) {
			result.put(Utils.getObjectIDFromUUID(entry.getKey()),
					(MetaDataInfo) CommonYAML.getYamlObject().load(entry.getValue()));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#newVersion(dataClay.util.ids.SessionID,
	 * util.ids.ObjectID, util.ids.StorageLocationID)
	 */
	@Override
	public VersionInfo newVersion(final SessionID sessionID, final ObjectID objectID,
			final ExecutionEnvironmentID optionalDestBackendID) {
		final NewVersionRequest request = NewVersionRequest.newBuilder().setSessionID(Utils.getMsgID(sessionID))
				.setObjectID(Utils.getMsgID(objectID)).setOptDestBackendID(Utils.getMsgID(optionalDestBackendID))
				.build();
		final NewVersionResponse response;
		final Function<NewVersionRequest, NewVersionResponse> f = req -> getBlockingStub().newVersion(req);
		response = this.<NewVersionRequest, NewVersionResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());

		return (VersionInfo) CommonYAML.getYamlObject().load(response.getVersionInfoYaml());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#consolidateVersion(dataClay.util.ids.SessionID,
	 * util.info.VersionInfo)
	 */
	@Override
	public void consolidateVersion(final SessionID sessionID, final VersionInfo version) {
		final ConsolidateVersionRequest request = ConsolidateVersionRequest.newBuilder()
				.setSessionID(Utils.getMsgID(sessionID)).setVersionInfoYaml(CommonYAML.getYamlObject().dump(version))
				.build();
		final ExceptionInfo response;
		final Function<ConsolidateVersionRequest, ExceptionInfo> f = req -> getBlockingStub().consolidateVersion(req);
		response = this.<ConsolidateVersionRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#newReplica(dataClay.util.ids.SessionID,
	 * util.ids.ObjectID, util.ids.StorageLocationID)
	 */
	@Override
	public ExecutionEnvironmentID newReplica(final SessionID sessionID, final ObjectID objectID,
			final ExecutionEnvironmentID optionalDestBackendID, final boolean recursive) {
		final NewReplicaRequest request = NewReplicaRequest.newBuilder().setSessionID(Utils.getMsgID(sessionID))
				.setObjectID(Utils.getMsgID(objectID)).setDestBackendID(Utils.getMsgID(optionalDestBackendID))
				.setRecursive(recursive).build();
		final NewReplicaResponse response;
		final Function<NewReplicaRequest, NewReplicaResponse> f = req -> getBlockingStub().newReplica(req);
		response = this.<NewReplicaRequest, NewReplicaResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());

		return Utils.getID(response.getDestBackendID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#moveObject(dataClay.util.ids.SessionID,
	 * util.ids.ObjectID, util.ids.StorageLocationID, util.ids.StorageLocationID,
	 * boolean)
	 */
	@Override
	public List<ObjectID> moveObject(final SessionID sessionID, final ObjectID objectID,
			final ExecutionEnvironmentID srcBackendID, final ExecutionEnvironmentID destBackendID,
			final boolean recursive) {
		final MoveObjectRequest request = MoveObjectRequest.newBuilder().setSessionID(Utils.getMsgID(sessionID))
				.setObjectID(Utils.getMsgID(objectID)).setSrcBackendID(Utils.getMsgID(srcBackendID))
				.setDestBackendID(Utils.getMsgID(destBackendID)).setRecursive(recursive).build();
		MoveObjectResponse response = null;
		final Function<MoveObjectRequest, MoveObjectResponse> f = req -> getBlockingStub().moveObject(req);
		response = this.<MoveObjectRequest, MoveObjectResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());

		final List<ObjectID> result = new ArrayList<>();
		for (final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ObjectID curObjID : response
				.getObjectIDsList()) {
			result.add(Utils.getID(curObjID));
		}
		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#setObjectReadOnly(dataClay.util.ids.SessionID,
	 * util.ids.ObjectID)
	 */
	@Override
	public void setObjectReadOnly(final SessionID sessionID, final ObjectID objectID) {
		final SetObjectReadOnlyRequest request = SetObjectReadOnlyRequest.newBuilder()
				.setSessionID(Utils.getMsgID(sessionID)).setObjectID(Utils.getMsgID(objectID)).build();
		final ExceptionInfo response;
		final Function<SetObjectReadOnlyRequest, ExceptionInfo> f = req -> getBlockingStub().setObjectReadOnly(req);
		response = this.<SetObjectReadOnlyRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);

	}

	@Override
	public boolean isPrefetchingEnabled() {
		final IsPrefetchingEnabledResponse response;
		final Function<EmptyMessage, IsPrefetchingEnabledResponse> f = req -> getBlockingStub()
				.isPrefetchingEnabled(req);
		response = this.<EmptyMessage, IsPrefetchingEnabledResponse>callLogicModule(EmptyMessage.newBuilder().build(),
				f);
		Utils.checkIsExc(response.getExcInfo());
		return response.getEnabled();
	}

	@Override
	public boolean objectExistsInDataClay(final ObjectID objectID) {
		final ObjectExistsInDataClayRequest request = ObjectExistsInDataClayRequest.newBuilder()
				.setObjectID(Utils.getMsgID(objectID)).build();
		final ObjectExistsInDataClayResponse response;
		final Function<ObjectExistsInDataClayRequest, ObjectExistsInDataClayResponse> f = req -> getBlockingStub()
				.objectExistsInDataClay(req);
		response = this.<ObjectExistsInDataClayRequest, ObjectExistsInDataClayResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return response.getExists();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#setObjectReadWrite(dataClay.util.ids.SessionID,
	 * util.ids.ObjectID)
	 */
	@Override
	public void setObjectReadWrite(final SessionID sessionID, final ObjectID objectID) {
		final SetObjectReadWriteRequest request = SetObjectReadWriteRequest.newBuilder()
				.setSessionID(Utils.getMsgID(sessionID)).setObjectID(Utils.getMsgID(objectID)).build();
		final ExceptionInfo response;
		final Function<SetObjectReadWriteRequest, ExceptionInfo> f = req -> getBlockingStub().setObjectReadWrite(req);
		response = this.<SetObjectReadWriteRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getMetadataByOID(dataClay.util.ids.SessionID,
	 * util.ids.ObjectID)
	 */
	@Override
	public MetaDataInfo getMetadataByOID(final SessionID sessionID, final ObjectID objectID) {
		final GetMetadataByOIDRequest request = GetMetadataByOIDRequest.newBuilder()
				.setSessionID(Utils.getMsgID(sessionID)).setObjectID(Utils.getMsgID(objectID)).build();
		final GetMetadataByOIDResponse response;
		final Function<GetMetadataByOIDRequest, GetMetadataByOIDResponse> f = req -> getBlockingStub()
				.getMetadataByOID(req);
		response = this.<GetMetadataByOIDRequest, GetMetadataByOIDResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		MetaDataInfo objMdata = null;
		if (response.getObjMdataYaml() != null) {
			objMdata = (MetaDataInfo) CommonYAML.getYamlObject().load(response.getObjMdataYaml());
		}
		return objMdata;
	}

	@Override
	public MetaDataInfo getMetadataByOIDForDS(final ObjectID objectID) {
		final GetMetadataByOIDForDSRequest request = GetMetadataByOIDForDSRequest.newBuilder()
				.setObjectID(Utils.getMsgID(objectID)).build();
		final GetMetadataByOIDForDSResponse response;
		final Function<GetMetadataByOIDForDSRequest, GetMetadataByOIDForDSResponse> f = req -> getBlockingStub()
				.getMetadataByOIDForDS(req);
		response = this.<GetMetadataByOIDForDSRequest, GetMetadataByOIDForDSResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());

		final MetaDataInfo objMdata = (MetaDataInfo) CommonYAML.getYamlObject().load(response.getObjMdataYaml());
		return objMdata;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#executeImplementation(dataClay.util.ids.SessionID,
	 * util.ids.OperationID, util.structs.Triple, util.ids.ObjectID, java.util.Map)
	 */
	@Override
	public SerializedParametersOrReturn executeImplementation(final SessionID sessionID, final OperationID operationID,
			final Triple<ImplementationID, ContractID, InterfaceID> remoteImplementation, final ObjectID objectID,
			final SerializedParametersOrReturn params) {
		final ExecuteImplementationRequest.Builder builder = ExecuteImplementationRequest.newBuilder();
		builder.setSessionID(Utils.getMsgID(sessionID));
		builder.setOperationID(Utils.getMsgID(operationID));
		builder.setImplementationID(Utils.getMsgID(remoteImplementation.getFirst()));
		builder.setContractID(Utils.getMsgID(remoteImplementation.getSecond()));
		builder.setInterfaceID(Utils.getMsgID(remoteImplementation.getThird()));
		builder.setObjectID(Utils.getMsgID(objectID));

		if (params != null) {
			builder.setParams(Utils.getParamsOrReturn(params));
		}
		final ExecuteImplementationRequest request = builder.build();
		final ExecuteImplementationResponse response;
		final Function<ExecuteImplementationRequest, ExecuteImplementationResponse> f = req -> getBlockingStub()
				.executeImplementation(req);
		response = this.<ExecuteImplementationRequest, ExecuteImplementationResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());

		if (response.hasRet()) {
			return Utils.getParamsOrReturn(response.getRet());
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#executeMethodOnTarget(dataClay.util.ids.SessionID,
	 * util.ids.ObjectID, java.lang.String, java.util.Map,
	 * util.ids.StorageLocationID)
	 */
	@Override
	public SerializedParametersOrReturn executeMethodOnTarget(final SessionID sessionID, final ObjectID objectID,
			final String operationSignature, final SerializedParametersOrReturn params,
			final ExecutionEnvironmentID backendID) {
		final ExecuteMethodOnTargetRequest.Builder builder = ExecuteMethodOnTargetRequest.newBuilder();
		builder.setSessionID(Utils.getMsgID(sessionID));
		builder.setOperationNameAndSignature(operationSignature);
		builder.setObjectID(Utils.getMsgID(objectID));
		builder.setTargetBackendID(Utils.getMsgID(backendID));

		if (params != null) {
			builder.setParams(Utils.getParamsOrReturn(params));
		}
		final ExecuteMethodOnTargetRequest request = builder.build();
		final ExecuteMethodOnTargetResponse response;
		final Function<ExecuteMethodOnTargetRequest, ExecuteMethodOnTargetResponse> f = req -> getBlockingStub()
				.executeMethodOnTarget(req);
		response = this.<ExecuteMethodOnTargetRequest, ExecuteMethodOnTargetResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());

		if (response.hasRet()) {
			return Utils.getParamsOrReturn(response.getRet());
		} else {
			return null;
		}
	}

	@Override
	public void synchronizeFederatedObject(final DataClayInstanceID dataClayID, final ObjectID objectID,
			final ImplementationID implID, final SerializedParametersOrReturn params, final boolean allBackends) {
		final SynchronizeFederatedObjectRequest.Builder builder = SynchronizeFederatedObjectRequest.newBuilder();
		builder.setExtDataClayID(Utils.getMsgID(dataClayID));
		builder.setObjectID(Utils.getMsgID(objectID));
		builder.setImplementationID(Utils.getMsgID(implID));
		if (params != null) {
			builder.setParams(Utils.getParamsOrReturn(params));
		}
		builder.setAllBackends(allBackends);

		final SynchronizeFederatedObjectRequest request = builder.build();
		final ExceptionInfo response;
		final Function<SynchronizeFederatedObjectRequest, ExceptionInfo> f = req -> getBlockingStub()
				.synchronizeFederatedObject(req);
		response = this.<SynchronizeFederatedObjectRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getStubs(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential,
	 * communication.grpc.messages.common.CommonMessages.Langs, java.util.List)
	 */
	@Override
	public Map<String, byte[]> getStubs(final AccountID applicantAccountID,
			final PasswordCredential applicantCredential, final Langs language, final List<ContractID> contractsIDs) {
		final GetStubsRequest.Builder builder = GetStubsRequest.newBuilder();
		builder.setApplicantAccountID(Utils.getMsgID(applicantAccountID));
		builder.setCredentials(Utils.getCredential(applicantCredential));
		builder.setLanguage(language);
		for (final ContractID cID : contractsIDs) {
			builder.addContractIDs(Utils.getMsgID(cID));
		}
		final GetStubsRequest request = builder.build();
		final GetStubsResponse response;
		final Function<GetStubsRequest, GetStubsResponse> f = req -> getBlockingStub().getStubs(req);
		response = this.<GetStubsRequest, GetStubsResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		final Map<String, byte[]> result = new HashMap<>();
		for (final Entry<String, ByteString> entry : response.getStubsMap().entrySet()) {
			final byte[] objBytes = entry.getValue().toByteArray();
			result.put(entry.getKey(), objBytes);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#getBabelStubs(dataClay.util.ids.AccountID,
	 * dataClay.util.management.accountmgr.Credential, java.util.List)
	 */
	@Override
	public byte[] getBabelStubs(final AccountID applicantAccountID, final PasswordCredential applicantCredential,
			final List<ContractID> contractsIDs) {
		final GetBabelStubsRequest.Builder builder = GetBabelStubsRequest.newBuilder();
		builder.setAccountID(Utils.getMsgID(applicantAccountID));
		builder.setCredentials(Utils.getCredential(applicantCredential));
		for (final ContractID cID : contractsIDs) {
			builder.addContractIDs(Utils.getMsgID(cID));
		}
		final GetBabelStubsRequest request = builder.build();
		final GetBabelStubsResponse response;
		final Function<GetBabelStubsRequest, GetBabelStubsResponse> f = req -> getBlockingStub().getBabelStubs(req);
		response = this.<GetBabelStubsRequest, GetBabelStubsResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return response.getYamlStub().toByteArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#getClassNameForDS(dataClay.util.ids.MetaClassID)
	 */
	@Override
	public String getClassNameForDS(final MetaClassID classID) {
		final GetClassNameForDSRequest request = GetClassNameForDSRequest.newBuilder()
				.setClassID(Utils.getMsgID(classID)).build();
		final GetClassNameForDSResponse response;
		final Function<GetClassNameForDSRequest, GetClassNameForDSResponse> f = req -> getBlockingStub()
				.getClassNameForDS(req);
		response = this.<GetClassNameForDSRequest, GetClassNameForDSResponse>callLogicModule(request, f);
		Utils.checkIsExc(response.getExcInfo());
		return response.getClassName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#getClassNameAndNamespaceForDS(dataClay.util.ids.
	 * MetaClassID)
	 */
	@Override
	public Tuple<String, String> getClassNameAndNamespaceForDS(final MetaClassID classID) {
		final GetClassNameAndNamespaceForDSRequest request = GetClassNameAndNamespaceForDSRequest.newBuilder()
				.setClassID(Utils.getMsgID(classID)).build();
		final GetClassNameAndNamespaceForDSResponse response;
		final Function<GetClassNameAndNamespaceForDSRequest, GetClassNameAndNamespaceForDSResponse> f = req -> getBlockingStub()
				.getClassNameAndNamespaceForDS(req);
		response = this.<GetClassNameAndNamespaceForDSRequest, GetClassNameAndNamespaceForDSResponse>callLogicModule(
				request, f);
		Utils.checkIsExc(response.getExcInfo());
		return new Tuple<>(response.getClassName(), response.getNamespace());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#registerEventListenerImplementation(dataClay.util.
	 * ids.AccountID, dataClay.util.management.accountmgr.Credential,
	 * util.events.listeners.ECA)
	 */
	@Override
	public void registerEventListenerImplementation(final AccountID accountID, final PasswordCredential credential,
			final ECA newEventListener) {
		final RegisterECARequest request = RegisterECARequest.newBuilder()
				.setApplicantAccountID(Utils.getMsgID(accountID)).setCredentials(Utils.getCredential(credential))
				.setEca(CommonYAML.getYamlObject().dump(newEventListener)).build();
		final ExceptionInfo response;
		final Function<RegisterECARequest, ExceptionInfo> f = req -> getBlockingStub().registerECA(req);
		response = this.<RegisterECARequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#adviseEvent(util.events.message.EventMessage)
	 */
	@Override
	public void adviseEvent(final EventMessage newEvent) {
		final AdviseEventRequest request = AdviseEventRequest.newBuilder()
				.setEventYaml(CommonYAML.getYamlObject().dump(newEvent)).build();
		final ExceptionInfo response;
		final Function<AdviseEventRequest, ExceptionInfo> f = req -> getBlockingStub().adviseEvent(req);
		response = this.<AdviseEventRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#activateTracing()
	 */
	@Override
	public void activateTracing(final int currentAvailableTaskID) {
		final ExceptionInfo response;
		final ActivateTracingRequest request = ActivateTracingRequest.newBuilder().setTaskid(currentAvailableTaskID)
				.build();
		final Function<ActivateTracingRequest, ExceptionInfo> f = req -> getBlockingStub().activateTracing(req);
		response = this.<ActivateTracingRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#deactivateTracing()
	 */
	@Override
	public void deactivateTracing() {
		final ExceptionInfo response;
		final Function<EmptyMessage, ExceptionInfo> f = req -> getBlockingStub().deactivateTracing(req);
		response = this.<EmptyMessage, ExceptionInfo>callLogicModule(EmptyMessage.newBuilder().build(), f);
		Utils.checkIsExc(response);
	}
	
	@Override
	public Map<String, byte[]> getTraces() { 
		final GetTracesResponse response;
		final Function<EmptyMessage, GetTracesResponse> f = req -> getBlockingStub().getTraces(req);
		response = this.<EmptyMessage, GetTracesResponse>callLogicModule(EmptyMessage.newBuilder().build(), f);
		Utils.checkIsExc(response.getExcInfo());
		final Map<String, byte[]> result = new HashMap<>();
		for (final Entry<String, ByteString> entry : response.getTracesMap().entrySet()) {
			final byte[] objBytes = entry.getValue().toByteArray();
			result.put(entry.getKey(), objBytes);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#cleanMetaDataCaches()
	 */
	@Override
	public void cleanMetaDataCaches() {
		final ExceptionInfo response;
		final Function<EmptyMessage, ExceptionInfo> f = req -> getBlockingStub().cleanMetaDataCaches(req);
		response = this.<EmptyMessage, ExceptionInfo>callLogicModule(EmptyMessage.newBuilder().build(), f);
		Utils.checkIsExc(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#closeManagerDb()
	 */
	@Override
	public void closeManagerDb() {
		final ExceptionInfo response;
		final Function<EmptyMessage, ExceptionInfo> f = req -> getBlockingStub().closeManagerDb(req);
		response = this.<EmptyMessage, ExceptionInfo>callLogicModule(EmptyMessage.newBuilder().build(), f);
		Utils.checkIsExc(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logic.api.LogicModuleAPI#closeDb()
	 */
	@Override
	public void closeDb() {
		final ExceptionInfo response;
		final Function<EmptyMessage, ExceptionInfo> f = req -> getBlockingStub().closeDb(req);
		response = this.<EmptyMessage, ExceptionInfo>callLogicModule(EmptyMessage.newBuilder().build(), f);
		Utils.checkIsExc(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * logic.api.LogicModuleAPI#getContractIDOfDataClayProvider(dataClay.util.ids.
	 * AccountID, dataClay.util.management.accountmgr.Credential)
	 */
	@Override
	public ContractID getContractIDOfDataClayProvider(final AccountID accountID, final PasswordCredential credential) {
		final GetContractIDOfDataClayProviderRequest request = GetContractIDOfDataClayProviderRequest.newBuilder()
				.setApplicantAccountID(Utils.getMsgID(accountID)).setCredentials(Utils.getCredential(credential))
				.build();
		final GetContractIDOfDataClayProviderResponse response;
		final Function<GetContractIDOfDataClayProviderRequest, GetContractIDOfDataClayProviderResponse> f = req -> getBlockingStub()
				.getContractIDOfDataClayProvider(req);
		response = this
				.<GetContractIDOfDataClayProviderRequest, GetContractIDOfDataClayProviderResponse>callLogicModule(
						request, f);
		Utils.checkIsExc(response.getExcInfo());
		return Utils.getID(response.getContractID());
	}

	/**
	 * Wait all async. requests
	 */
	public void waitAndProcessAllAsyncRequests() {
		while (asyncReqSend.get() != asyncReqReceived.get()) {
			try {
				Thread.sleep(ASYNC_WAIT_MILLIS);
			} catch (final InterruptedException ex) {
				logger.debug("waitAndProcessAllAsyncRequests was interrupted while sleeping", ex);
			}
		}
	}

	@Override
	public void unregisterObjects(final Set<ObjectID> objectsToUnregister) {
		final UnregisterObjectsRequest.Builder requestBuilder = UnregisterObjectsRequest.newBuilder();
		for (final ObjectID oid : objectsToUnregister) {
			requestBuilder.addObjectsToUnregister(Utils.getMsgID(oid));
		}
		final UnregisterObjectsRequest request = requestBuilder.build();
		final ExceptionInfo response;
		final Function<UnregisterObjectsRequest, ExceptionInfo> f = req -> getBlockingStub().unregisterObjects(req);
		response = this.<UnregisterObjectsRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);

	}

	@Override
	public void publishAddress(final String hostname, final int port) {
		final PublishAddressRequest.Builder requestBuilder = PublishAddressRequest.newBuilder();
		requestBuilder.setHostname(hostname);
		requestBuilder.setPort(port);
		final PublishAddressRequest request = requestBuilder.build();
		final ExceptionInfo response;
		final Function<PublishAddressRequest, ExceptionInfo> f = req -> getBlockingStub().publishAddress(req);
		response = this.<PublishAddressRequest, ExceptionInfo>callLogicModule(request, f);
		Utils.checkIsExc(response);
	}
}
