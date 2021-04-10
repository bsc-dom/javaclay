
package es.bsc.dataclay.communication.grpc.services.logicmodule;

import java.util.*;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.protobuf.ByteString;

import es.bsc.dataclay.communication.grpc.Utils;
import es.bsc.dataclay.communication.grpc.generated.logicmodule.LogicModuleGrpc;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.*;
import es.bsc.dataclay.logic.LogicModule;
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
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.ids.StorageLocationID;
import es.bsc.dataclay.util.info.VersionInfo;
import es.bsc.dataclay.util.management.accountmgr.Account;
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
import io.grpc.stub.StreamObserver;

/**
 * Implements all LogicModule GRPC methods.
 */
public final class LogicModuleService extends LogicModuleGrpc.LogicModuleImplBase {
	private static final Logger logger = LogManager.getLogger("communication.LogicModule.service");

	/** Actual logic module implementation. */
	private final LogicModule logicModule;

	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/**
	 * Constructor
	 * 
	 * @param theLogicModule
	 *            Logic module to process calls
	 */
	public LogicModuleService(final LogicModule theLogicModule) {
		this.logicModule = theLogicModule;
		logger.info("Initialized LogicModuleService");
	}

	@Override
	public void checkAlive(final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
			final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.checkAlive();

			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void autoregisterEE(final AutoRegisterEERequest request,
			final StreamObserver<AutoRegisterEEResponse> responseObserver) {

		try {
			final StorageLocationID result = logicModule.autoregisterEE(Utils.getExecutionEnvironmentID(request.getExecutionEnvironmentID()),
					request.getEeName(), request.getEeHostname(), request.getEePort(),
					Langs.values()[request.getLangValue()]);
			final AutoRegisterEEResponse resp = AutoRegisterEEResponse.newBuilder()
					.setStorageLocationID(Utils.getMsgID(result)).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final AutoRegisterEEResponse.Builder builder = AutoRegisterEEResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final AutoRegisterEEResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}
	
	@Override
	public void autoregisterSL(final AutoRegisterSLRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {

		try {
			logicModule.autoregisterSL(Utils.getStorageLocationID(request.getStorageLocationID()),
					request.getDsName(), request.getDsHostname(), request.getDsPort());
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void unregisterStorageLocation(final UnregisterStorageLocationRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.unregisterStorageLocation(Utils.getStorageLocationID(request.getStorageLocationID()));
			Utils.returnExceptionInfoMessage(responseObserver);

			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void unregisterExecutionEnvironment(final UnregisterExecutionEnvironmentRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.unregisterExecutionEnvironment(Utils.getExecutionEnvironmentID(request.getExecutionEnvironmentID()));
			Utils.returnExceptionInfoMessage(responseObserver);

			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}
	
	@Override
	public void publishAddress(final PublishAddressRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.publishAddress(request.getHostname(), request.getPort());
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void performSetOfNewAccounts(final PerformSetAccountsRequest request,
			final StreamObserver<PerformSetAccountsResponse> responseObserver) {

		try {
			final byte[] result = logicModule.performSetOfNewAccounts(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), request.getYaml().getBytes());
			final PerformSetAccountsResponse resp = PerformSetAccountsResponse.newBuilder()
					.setResultYaml(new String(result)).build();

			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final PerformSetAccountsResponse.Builder builder = PerformSetAccountsResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final PerformSetAccountsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void performSetOfOperations(final PerformSetOperationsRequest request,
			final StreamObserver<PerformSetOperationsResponse> responseObserver) {
		try {
			final byte[] result = logicModule.performSetOfOperations(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), request.getYaml().getBytes());
			final PerformSetOperationsResponse resp = PerformSetOperationsResponse.newBuilder()
					.setResultYaml(new String(result)).build();

			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception ex) {

			logger.debug("performSetOfOperations error", ex);

			final PerformSetOperationsResponse.Builder builder = PerformSetOperationsResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(ex));
			final PerformSetOperationsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	/**
	 * <pre>
	 * Account Manager.
	 * </pre>
	 */
	@Override
	public void newAccountNoAdmin(final NewAccountNoAdminRequest request,
			final StreamObserver<NewAccountResponse> responseObserver) {
		try {
			// String yamlStr = CommonYAML.getYamlObject().dump(params[0]);
			final Account acc = (Account) CommonYAML.getYamlObject().load(request.getYamlNewAccount());
			final AccountID result = logicModule.newAccountNoAdmin(acc);
			final NewAccountResponse resp = NewAccountResponse.newBuilder().setNewAccountID(Utils.getMsgID(result))
					.build();

			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final NewAccountResponse.Builder builder = NewAccountResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final NewAccountResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	/**
	 * <pre>
	 * Account Manager.
	 * </pre>
	 */
	@Override
	public void newAccount(final NewAccountRequest request, final StreamObserver<NewAccountResponse> responseObserver) {
		try {
			// String yamlStr = CommonYAML.getYamlObject().dump(params[0]);
			final Account acc = (Account) CommonYAML.getYamlObject().load(request.getYamlNewAccount());
			final AccountID result = logicModule.newAccount(Utils.getAccountID(request.getAdminID()),
					Utils.getCredential(request.getAdmincredential()), acc);
			final NewAccountResponse resp = NewAccountResponse.newBuilder().setNewAccountID(Utils.getMsgID(result))
					.build();

			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final NewAccountResponse.Builder builder = NewAccountResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final NewAccountResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	@Override
	public void getAccountID(final GetAccountIDRequest request,
			final StreamObserver<GetAccountIDResponse> responseObserver) {
		try {
			final AccountID result = logicModule.getAccountID(request.getAccountName());
			final GetAccountIDResponse resp = GetAccountIDResponse.newBuilder().setNewAccountID(Utils.getMsgID(result))
					.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetAccountIDResponse.Builder builder = GetAccountIDResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetAccountIDResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getAccountList(final GetAccountListRequest request,
			final StreamObserver<GetAccountListResponse> responseObserver) {
		try {
			final HashSet<AccountID> result = logicModule.getAccountList(Utils.getAccountID(request.getAdminID()),
					Utils.getCredential(request.getAdmincredential()));

			final GetAccountListResponse.Builder builder = GetAccountListResponse.newBuilder();
			for (final AccountID accID : result) {
				builder.addAccountIDs(accID.toString());
			}
			final GetAccountListResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetAccountListResponse.Builder builder = GetAccountListResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetAccountListResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	/**
	 * <pre>
	 * Session Manager.
	 * </pre>
	 */
	@Override
	public void newSession(final NewSessionRequest request, final StreamObserver<NewSessionResponse> responseObserver) {
		try {
			final Set<ContractID> contracts = new HashSet<>();
			for (final String cID : request.getContractIDsList()) {
				contracts.add(Utils.getContractID(cID));
			}
			final Set<DataSetID> dataSetIDs = new HashSet<>();
			for (final String dsID : request.getDataSetIDsList()) {
				dataSetIDs.add(Utils.getDataSetID(dsID));
			}
			final SessionInfo result = logicModule.newSession(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), contracts, dataSetIDs,
					Utils.getDataSetID(request.getStoreDataSet()),
					Langs.values()[request.getSessionLangValue()]);

			final NewSessionResponse resp = NewSessionResponse.newBuilder()
					.setSessionInfo(CommonYAML.getYamlObject().dump(result)).build();

			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception ex) {
			logger.debug("newSession error", ex);

			final NewSessionResponse.Builder builder = NewSessionResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(ex));
			final NewSessionResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void closeSession(final CloseSessionRequest request, final StreamObserver<ExceptionInfo> responseObserver) {
		try {

			logicModule.closeSession(Utils.getSessionID(request.getSessionID()));
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getInfoOfSessionForDS(final GetInfoOfSessionForDSRequest request,
			final StreamObserver<GetInfoOfSessionForDSResponse> responseObserver) {

		try {
			final Tuple<Tuple<DataSetID, Set<DataSetID>>, Calendar> result = logicModule
					.getInfoOfSessionForDS(Utils.getSessionID(request.getSessionID()));

			final GetInfoOfSessionForDSResponse.Builder builder = GetInfoOfSessionForDSResponse.newBuilder()
					.setDataSetID(Utils.getMsgID(result.getFirst().getFirst()))
					.setDate(result.getSecond().getTimeInMillis());
			for (final DataSetID dsID : result.getFirst().getSecond()) {
				builder.addDataSetIDs(Utils.getMsgID(dsID));
			}
			final GetInfoOfSessionForDSResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetInfoOfSessionForDSResponse.Builder builder = GetInfoOfSessionForDSResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetInfoOfSessionForDSResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	/**
	 * <pre>
	 * Namespace Manager.
	 * </pre>
	 */
	@Override
	public void newNamespace(final NewNamespaceRequest request,
			final StreamObserver<NewNamespaceResponse> responseObserver) {
		try {
			final Namespace newNamespace = (Namespace) CommonYAML.getYamlObject().load(request.getNewNamespaceYaml());
			final NamespaceID domID = logicModule.newNamespace(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), newNamespace);

			final NewNamespaceResponse resp = NewNamespaceResponse.newBuilder().setNamespaceID(Utils.getMsgID(domID))
					.build();

			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final NewNamespaceResponse.Builder builder = NewNamespaceResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final NewNamespaceResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void removeNamespace(final RemoveNamespaceRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.removeNamespace(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), request.getNamespaceName());

			Utils.returnExceptionInfoMessage(responseObserver);

			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getNamespaces(final GetNamespacesRequest request,
			final StreamObserver<GetNamespacesResponse> responseObserver) {
		try {
			final Set<String> result = logicModule.getNamespaces(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()));
			final GetNamespacesResponse resp = GetNamespacesResponse.newBuilder().addAllNamespaces(result).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetNamespacesResponse.Builder builder = GetNamespacesResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetNamespacesResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getNamespaceID(final GetNamespaceIDRequest request,
			final StreamObserver<GetNamespaceIDResponse> responseObserver) {
		try {
			final NamespaceID namespaceID = logicModule.getNamespaceID(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), request.getNamespaceName());
			final GetNamespaceIDResponse resp = GetNamespaceIDResponse.newBuilder()
					.setNamespaceID(Utils.getMsgID(namespaceID)).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetNamespaceIDResponse.Builder builder = GetNamespaceIDResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetNamespaceIDResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getNamespaceLang(final GetNamespaceLangRequest request,
			final StreamObserver<GetNamespaceLangResponse> responseObserver) {
		try {
			final Langs language = logicModule.getNamespaceLang(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), request.getNamespaceName());
			final GetNamespaceLangResponse resp = GetNamespaceLangResponse.newBuilder().setLanguage(language).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetNamespaceLangResponse.Builder builder = GetNamespaceLangResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetNamespaceLangResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getObjectDataSetID(final GetObjectDataSetIDRequest request,
			final StreamObserver<GetObjectDataSetIDResponse> responseObserver) {
		try {
			final DataSetID result = logicModule.getObjectDataSetID(Utils.getSessionID(request.getSessionID()),
					Utils.getObjectID(request.getObjectID()));
			final GetObjectDataSetIDResponse resp = GetObjectDataSetIDResponse.newBuilder()
					.setDataSetID(Utils.getMsgID(result)).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetObjectDataSetIDResponse.Builder builder = GetObjectDataSetIDResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetObjectDataSetIDResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void importInterface(final ImportInterfaceRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.importInterface(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), Utils.getNamespaceID(request.getNamespaceID()),
					Utils.getContractID(request.getContractID()), Utils.getInterfaceID(request.getInterfaceID()));

			Utils.returnExceptionInfoMessage(responseObserver);

			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void importContract(final ImportContractRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {

		try {
			logicModule.importContract(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), Utils.getNamespaceID(request.getNamespaceID()),
					Utils.getContractID(request.getContractID()));

			Utils.returnExceptionInfoMessage(responseObserver);

			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getInfoOfClassesInNamespace(final GetInfoOfClassesInNamespaceRequest request,
			final StreamObserver<GetInfoOfClassesInNamespaceResponse> responseObserver) {
		try {
			final Map<MetaClassID, MetaClass> result = logicModule.getInfoOfClassesInNamespace(
					Utils.getAccountID(request.getAccountID()), Utils.getCredential(request.getCredential()),
					Utils.getNamespaceID(request.getNamespaceID()));

			final GetInfoOfClassesInNamespaceResponse.Builder builder = GetInfoOfClassesInNamespaceResponse
					.newBuilder();
			for (final Entry<MetaClassID, MetaClass> entry : result.entrySet()) {
				final String yaml = CommonYAML.getYamlObject().dump(entry.getValue());
				builder.putClassesInfo(entry.getKey().toString(), yaml);
			}

			final GetInfoOfClassesInNamespaceResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetInfoOfClassesInNamespaceResponse.Builder builder = GetInfoOfClassesInNamespaceResponse
					.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetInfoOfClassesInNamespaceResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	/**
	 * <pre>
	 * DataSet Manager.
	 * </pre>
	 */
	@Override
	public void newDataSet(final NewDataSetRequest request, final StreamObserver<NewDataSetResponse> responseObserver) {
		try {
			final DataSet newDataSet = (DataSet) CommonYAML.getYamlObject().load(request.getDataSetYaml());
			final DataSetID result = logicModule.newDataSet(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), newDataSet);
			final NewDataSetResponse resp = NewDataSetResponse.newBuilder().setDataSetID(Utils.getMsgID(result))
					.build();

			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final NewDataSetResponse.Builder builder = NewDataSetResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final NewDataSetResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void removeDataSet(final RemoveDataSetRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.removeDataSet(Utils.getAccountID(request.getAccountID()), Utils.getCredential(request.getCredential()),
					request.getDataSetName());

			Utils.returnExceptionInfoMessage(responseObserver);

			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getDataSetID(final GetDataSetIDRequest request,
			final StreamObserver<GetDataSetIDResponse> responseObserver) {
		try {
			final DataSetID result = logicModule.getDataSetID(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), request.getDataSetName());
			final GetDataSetIDResponse resp = GetDataSetIDResponse.newBuilder().setDataSetID(Utils.getMsgID(result))
					.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetDataSetIDResponse.Builder builder = GetDataSetIDResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetDataSetIDResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void checkDataSetIsPublic(final CheckDataSetIsPublicRequest request,
			final StreamObserver<CheckDataSetIsPublicResponse> responseObserver) {
		try {
			final boolean result = logicModule.checkDataSetIsPublic(Utils.getDataSetID(request.getDataSetID()));
			final CheckDataSetIsPublicResponse resp = CheckDataSetIsPublicResponse.newBuilder().setIsPublic(result)
					.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final CheckDataSetIsPublicResponse.Builder builder = CheckDataSetIsPublicResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final CheckDataSetIsPublicResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void isPrefetchingEnabled(final es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
			final StreamObserver<IsPrefetchingEnabledResponse> responseObserver) {
		try {
			final boolean result = logicModule.isPrefetchingEnabled();
			final IsPrefetchingEnabledResponse resp = IsPrefetchingEnabledResponse.newBuilder().setEnabled(result)
					.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final IsPrefetchingEnabledResponse.Builder builder = IsPrefetchingEnabledResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final IsPrefetchingEnabledResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}
	
	@Override
	public void objectExistsInDataClay(final ObjectExistsInDataClayRequest request,
			final StreamObserver<ObjectExistsInDataClayResponse> responseObserver) {
		try {
			final boolean result = logicModule.objectExistsInDataClay(Utils.getObjectID(request.getObjectID()));
			final ObjectExistsInDataClayResponse resp = ObjectExistsInDataClayResponse.newBuilder().setExists(result)
					.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ObjectExistsInDataClayResponse.Builder builder = ObjectExistsInDataClayResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final ObjectExistsInDataClayResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}
	
	@Override
	public void getPublicDataSets(final GetPublicDataSetsRequest request,
			final StreamObserver<GetPublicDataSetsResponse> responseObserver) {
		try {
			final Set<String> result = logicModule.getPublicDataSets(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()));
			final GetPublicDataSetsResponse resp = GetPublicDataSetsResponse.newBuilder().addAllDataSets(result)
					.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetPublicDataSetsResponse.Builder builder = GetPublicDataSetsResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetPublicDataSetsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getAccountDataSets(final GetAccountDataSetsRequest request,
			final StreamObserver<GetAccountDataSetsResponse> responseObserver) {
		try {
			final Set<String> result = logicModule.getAccountDataSets(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()));
			final GetAccountDataSetsResponse resp = GetAccountDataSetsResponse.newBuilder().addAllDataSets(result)
					.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetAccountDataSetsResponse.Builder builder = GetAccountDataSetsResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetAccountDataSetsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	/**
	 * <pre>
	 * Class Manager.
	 * </pre>
	 */
	@Override
	public void newClass(final NewClassRequest request, final StreamObserver<NewClassResponse> responseObserver) {

		try {
			final Map<String, MetaClass> newClasses = new HashMap<>();
			for (final Entry<String, String> clazz : request.getNewClassesMap().entrySet()) {
				final MetaClass mClass = (MetaClass) CommonYAML.getYamlObject().load(clazz.getValue());
				newClasses.put(clazz.getKey(), mClass);
			}
			final Map<String, MetaClass> result = logicModule.newClass(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), Langs.values()[request.getLanguageValue()],
					newClasses);

			final NewClassResponse.Builder builder = NewClassResponse.newBuilder();
			for (final Entry<String, MetaClass> entry : result.entrySet()) {
				final String yaml = CommonYAML.getYamlObject().dump(entry.getValue());
				builder.putNewClasses(entry.getKey(), yaml);
			}

			final NewClassResponse resp = builder.build();

			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception ex) {
			logger.debug("newClass error", ex);

			final NewClassResponse.Builder builder = NewClassResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(ex));
			final NewClassResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	@Override
	public void removeClass(final RemoveClassRequest request, final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.removeClass(Utils.getAccountID(request.getAccountID()), Utils.getCredential(request.getCredential()),
					Utils.getNamespaceID(request.getNamespaceID()), request.getClassName());

			Utils.returnExceptionInfoMessage(responseObserver);

			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void removeOperation(final RemoveOperationRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.removeOperation(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), Utils.getNamespaceID(request.getNamespaceID()),
					request.getClassName(), request.getOperationNameAndSignature());

			Utils.returnExceptionInfoMessage(responseObserver);

			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void removeImplementation(final RemoveImplementationRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.removeImplementation(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), Utils.getNamespaceID(request.getNamespaceID()),
					Utils.getImplementationID(request.getImplementationID()));

			Utils.returnExceptionInfoMessage(responseObserver);

			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getOperationID(final GetOperationIDRequest request,
			final StreamObserver<GetOperationIDResponse> responseObserver) {
		try {
			final OperationID opID = logicModule.getOperationID(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), Utils.getNamespaceID(request.getNamespaceID()),
					request.getClassName(), request.getOperationNameAndSignature());

			final GetOperationIDResponse resp = GetOperationIDResponse.newBuilder().setOperationID(Utils.getMsgID(opID))
					.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetOperationIDResponse.Builder builder = GetOperationIDResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetOperationIDResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	@Override
	public void getPropertyID(final GetPropertyIDRequest request,
			final StreamObserver<GetPropertyIDResponse> responseObserver) {
		try {
			final PropertyID result = logicModule.getPropertyID(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), Utils.getNamespaceID(request.getNamespaceID()),
					request.getClassName(), request.getPropertyName());
			final GetPropertyIDResponse resp = GetPropertyIDResponse.newBuilder().setPropertyID(Utils.getMsgID(result))
					.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetPropertyIDResponse.Builder builder = GetPropertyIDResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetPropertyIDResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getClassID(final GetClassIDRequest request, final StreamObserver<GetClassIDResponse> responseObserver) {

		try {
			final MetaClassID result = logicModule.getClassID(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), Utils.getNamespaceID(request.getNamespaceID()),
					request.getClassName());
			final GetClassIDResponse resp = GetClassIDResponse.newBuilder().setClassID(Utils.getMsgID(result)).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetClassIDResponse.Builder builder = GetClassIDResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetClassIDResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getClassInfo(final GetClassInfoRequest request,
			final StreamObserver<GetClassInfoResponse> responseObserver) {
		try {
			final MetaClass result = logicModule.getClassInfo(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), Utils.getNamespaceID(request.getNamespaceID()),
					request.getClassName());
			final String yaml = CommonYAML.getYamlObject().dump(result);
			final GetClassInfoResponse resp = GetClassInfoResponse.newBuilder().setMetaClassYaml(yaml).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetClassInfoResponse.Builder builder = GetClassInfoResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetClassInfoResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}
	
	/**
	 * <pre>
	 * Contract Manager.
	 * </pre>
	 */
	@Override
	public void newContract(final NewContractRequest request,
			final StreamObserver<NewContractResponse> responseObserver) {
		try {
			final Contract newContract = (Contract) CommonYAML.getYamlObject().load(request.getNewContractYaml());
			final ContractID cID = logicModule.newContract(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), newContract);

			final NewContractResponse resp = NewContractResponse.newBuilder().setContractID(Utils.getMsgID(cID))
					.build();

			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception ex) {
			logger.debug("newContract error", ex);

			final NewContractResponse.Builder builder = NewContractResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(ex));
			final NewContractResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	@Override
	public void registerToPublicContract(final RegisterToPublicContractRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.registerToPublicContract(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), Utils.getContractID(request.getContractID()));

			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void registerToPublicContractOfNamespace(final RegisterToPublicContractOfNamespaceRequest request,
			final StreamObserver<RegisterToPublicContractOfNamespaceResponse> responseObserver) {
		try {
			final ContractID cID = logicModule.registerToPublicContractOfNamespace(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), Utils.getNamespaceID(request.getNamespaceID()));

			final RegisterToPublicContractOfNamespaceResponse resp = RegisterToPublicContractOfNamespaceResponse
					.newBuilder().setContractID(Utils.getMsgID(cID)).build();

			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final RegisterToPublicContractOfNamespaceResponse.Builder builder = RegisterToPublicContractOfNamespaceResponse
					.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final RegisterToPublicContractOfNamespaceResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getContractIDsOfApplicant(final GetContractIDsOfApplicantRequest request,
			final StreamObserver<GetContractIDsOfApplicantResponse> responseObserver) {
		try {
			final Map<ContractID, Contract> result = logicModule.getContractIDsOfApplicant(
					Utils.getAccountID(request.getApplicantID()), Utils.getCredential(request.getCredential()));

			final GetContractIDsOfApplicantResponse.Builder builder = GetContractIDsOfApplicantResponse.newBuilder();
			for (final Entry<ContractID, Contract> entry : result.entrySet()) {
				builder.putContracts(entry.getKey().toString(), CommonYAML.getYamlObject().dump(entry.getValue()));
			}

			final GetContractIDsOfApplicantResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetContractIDsOfApplicantResponse.Builder builder = GetContractIDsOfApplicantResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetContractIDsOfApplicantResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getContractIDsOfProvider(final GetContractIDsOfProviderRequest request,
			final StreamObserver<GetContractIDsOfProviderResponse> responseObserver) {

		try {
			final Map<ContractID, Contract> result = logicModule.getContractIDsOfProvider(
					Utils.getAccountID(request.getProviderID()), Utils.getCredential(request.getCredential()),
					Utils.getNamespaceID(request.getNamespaceIDOfProvider()));

			final GetContractIDsOfProviderResponse.Builder builder = GetContractIDsOfProviderResponse.newBuilder();
			for (final Entry<ContractID, Contract> entry : result.entrySet()) {
				builder.putContracts(entry.getKey().toString(), CommonYAML.getYamlObject().dump(entry.getValue()));
			}

			final GetContractIDsOfProviderResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetContractIDsOfProviderResponse.Builder builder = GetContractIDsOfProviderResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetContractIDsOfProviderResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getContractIDsOfApplicantWithProvider(final GetContractsOfApplicantWithProvRequest request,
			final StreamObserver<GetContractsOfApplicantWithProvResponse> responseObserver) {
		try {
			final Map<ContractID, Contract> result = logicModule.getContractIDsOfApplicantWithProvider(
					Utils.getAccountID(request.getApplicantID()), Utils.getCredential(request.getCredential()),
					Utils.getNamespaceID(request.getNamespaceIDOfProvider()));
			final GetContractsOfApplicantWithProvResponse.Builder builder = GetContractsOfApplicantWithProvResponse
					.newBuilder();
			for (final Entry<ContractID, Contract> entry : result.entrySet()) {
				builder.putContracts(entry.getKey().toString(), CommonYAML.getYamlObject().dump(entry.getValue()));
			}

			final GetContractsOfApplicantWithProvResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetContractsOfApplicantWithProvResponse.Builder builder = GetContractsOfApplicantWithProvResponse
					.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetContractsOfApplicantWithProvResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	/**
	 * <pre>
	 * DataContract Manager.
	 * </pre>
	 */
	@Override
	public void newDataContract(final NewDataContractRequest request,
			final StreamObserver<NewDataContractResponse> responseObserver) {

		try {
			final DataContract newContract = (DataContract) CommonYAML.getYamlObject()
					.load(request.getDataContractYaml());
			final DataContractID cID = logicModule.newDataContract(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), newContract);

			final NewDataContractResponse resp = NewDataContractResponse.newBuilder()
					.setDataContractID(Utils.getMsgID(cID)).build();

			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final NewDataContractResponse.Builder builder = NewDataContractResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final NewDataContractResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void registerToPublicDataContract(final RegisterToPublicDataContractRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.registerToPublicDataContract(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredential()), Utils.getDataContractID(request.getDataContractID()));

			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getDataContractIDsOfApplicant(final GetDataContractIDsOfApplicantRequest request,
			final StreamObserver<GetDataContractIDsOfApplicantResponse> responseObserver) {
		try {
			final Map<DataContractID, DataContract> result = logicModule.getDataContractIDsOfApplicant(
					Utils.getAccountID(request.getApplicantID()), Utils.getCredential(request.getCredential()));

			final GetDataContractIDsOfApplicantResponse.Builder builder = GetDataContractIDsOfApplicantResponse
					.newBuilder();
			for (final Entry<DataContractID, DataContract> entry : result.entrySet()) {
				builder.putDatacontracts(entry.getKey().toString(), CommonYAML.getYamlObject().dump(entry.getValue()));
			}

			final GetDataContractIDsOfApplicantResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetDataContractIDsOfApplicantResponse.Builder builder = GetDataContractIDsOfApplicantResponse
					.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetDataContractIDsOfApplicantResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	@Override
	public void getDataContractIDsOfProvider(final GetDataContractIDsOfProviderRequest request,
			final StreamObserver<GetDataContractIDsOfProviderResponse> responseObserver) {
		try {
			final Map<DataContractID, DataContract> result = logicModule.getDataContractIDsOfProvider(
					Utils.getAccountID(request.getProviderID()), Utils.getCredential(request.getCredential()),
					Utils.getDataSetID(request.getDataSetIDOfProvider()));

			final GetDataContractIDsOfProviderResponse.Builder builder = GetDataContractIDsOfProviderResponse
					.newBuilder();
			for (final Entry<DataContractID, DataContract> entry : result.entrySet()) {
				builder.putDatacontracts(entry.getKey().toString(), CommonYAML.getYamlObject().dump(entry.getValue()));
			}

			final GetDataContractIDsOfProviderResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetDataContractIDsOfProviderResponse.Builder builder = GetDataContractIDsOfProviderResponse
					.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetDataContractIDsOfProviderResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	@Override
	public void getDataContractInfoOfApplicantWithProvider(final GetDataContractInfoOfApplicantWithProvRequest request,
			final StreamObserver<GetDataContractInfoOfApplicantWithProvResponse> responseObserver) {
		try {
			final DataContract result = logicModule.getDataContractInfoOfApplicantWithProvider(
					Utils.getAccountID(request.getApplicantID()), Utils.getCredential(request.getCredential()),
					Utils.getDataSetID(request.getDataSetIDOfProvider()));

			final GetDataContractInfoOfApplicantWithProvResponse.Builder builder = GetDataContractInfoOfApplicantWithProvResponse
					.newBuilder().setDataContractInfo(CommonYAML.getYamlObject().dump(result));
			final GetDataContractInfoOfApplicantWithProvResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetDataContractInfoOfApplicantWithProvResponse.Builder builder = GetDataContractInfoOfApplicantWithProvResponse
					.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetDataContractInfoOfApplicantWithProvResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	/**
	 * <pre>
	 * Interface Manager.
	 * </pre>
	 */
	@Override
	public void newInterface(final NewInterfaceRequest request,
			final StreamObserver<NewInterfaceResponse> responseObserver) {
		try {
			final Interface newInterface = (Interface) CommonYAML.getYamlObject().load(request.getInterfaceYaml());
			final InterfaceID result = logicModule.newInterface(Utils.getAccountID(request.getApplicantID()),
					Utils.getCredential(request.getCredential()), newInterface);

			final NewInterfaceResponse resp = NewInterfaceResponse.newBuilder().setInterfaceID(Utils.getMsgID(result))
					.build();

			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final NewInterfaceResponse.Builder builder = NewInterfaceResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final NewInterfaceResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	@Override
	public void getInterfaceInfo(final GetInterfaceInfoRequest request,
			final StreamObserver<GetInterfaceInfoResponse> responseObserver) {
		try {
			final Interface iface = logicModule.getInterfaceInfo(Utils.getAccountID(request.getApplicantID()),
					Utils.getCredential(request.getCredential()), Utils.getInterfaceID(request.getInterfaceID()));

			final GetInterfaceInfoResponse resp = GetInterfaceInfoResponse.newBuilder()
					.setInterfaceYaml(CommonYAML.getYamlObject().dump(iface)).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetInterfaceInfoResponse.Builder builder = GetInterfaceInfoResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetInterfaceInfoResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void removeInterface(final RemoveInterfaceRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.removeInterface(Utils.getAccountID(request.getApplicantID()),
					Utils.getCredential(request.getCredential()), Utils.getNamespaceID(request.getNamespaceID()),
					Utils.getInterfaceID(request.getInterfaceID()));

			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	@Override
	public void getStorageLocationInfo(final GetStorageLocationInfoRequest request,
			final StreamObserver<GetStorageLocationInfoResponse> responseObserver) {
		try {
			final StorageLocation stLoc = logicModule
					.getStorageLocationInfo(Utils.getStorageLocationID(request.getStorageLocationID()));

			final GetStorageLocationInfoResponse resp = GetStorageLocationInfoResponse.newBuilder()
					.setStorageLocationInfo(Utils.getStorageLocation(stLoc)).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetStorageLocationInfoResponse.Builder builder = GetStorageLocationInfoResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetStorageLocationInfoResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getExecutionEnvironmentInfo(final GetExecutionEnvironmentInfoRequest request,
			final StreamObserver<GetExecutionEnvironmentInfoResponse> responseObserver) {
		try {
			final ExecutionEnvironment result = logicModule
					.getExecutionEnvironmentInfo(Utils.getExecutionEnvironmentID(request.getExecEnvID()));

			final GetExecutionEnvironmentInfoResponse resp = GetExecutionEnvironmentInfoResponse.newBuilder()
					.setExecutionEnvironmentInfo(Utils.getExecutionEnvironment(result)).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetExecutionEnvironmentInfoResponse.Builder builder = GetExecutionEnvironmentInfoResponse
					.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetExecutionEnvironmentInfoResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getDataClayID(final EmptyMessage request,
			final StreamObserver<GetDataClayIDResponse> responseObserver) {
		final GetDataClayIDResponse.Builder builder = GetDataClayIDResponse.newBuilder();
		try {
			final DataClayInstanceID result = logicModule.getDataClayID();
			final GetDataClayIDResponse resp = builder.setDataClayID(Utils.getMsgID(result)).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			builder.setExcInfo(Utils.serializeException(e));
			final GetDataClayIDResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void registerExternalDataClay(final RegisterExternalDataClayRequest request,
			final StreamObserver<RegisterExternalDataClayResponse> responseObserver) {
		final RegisterExternalDataClayResponse.Builder builder = RegisterExternalDataClayResponse.newBuilder();
		try {
			final DataClayInstanceID result = logicModule.registerExternalDataClay(request.getHostname(),
					request.getPort());

			final RegisterExternalDataClayResponse resp = builder.setExtDataClayID(Utils.getMsgID(result)).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			builder.setExcInfo(Utils.serializeException(e));
			final RegisterExternalDataClayResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	
	@Override
	public void registerExternalDataClayOverrideAuthority(final RegisterExternalDataClayOverrideAuthorityRequest request,
			final StreamObserver<RegisterExternalDataClayResponse> responseObserver) {
		final RegisterExternalDataClayResponse.Builder builder = RegisterExternalDataClayResponse.newBuilder();
		try {
			final DataClayInstanceID result = logicModule.registerExternalDataClayOverrideAuthority(Utils.getAccountID(request.getAdminAccountID()), 
					Utils.getCredential(request.getAdminCredential()), request.getHostname(),
					request.getPort(), request.getAuthority());

			final RegisterExternalDataClayResponse resp = builder.setExtDataClayID(Utils.getMsgID(result)).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			builder.setExcInfo(Utils.serializeException(e));
			final RegisterExternalDataClayResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}
	
	@Override
	public void notifyRegistrationOfExternalDataClay(final NotifyRegistrationOfExternalDataClayRequest request,
			final StreamObserver<NotifyRegistrationOfExternalDataClayResponse> responseObserver) {
		final NotifyRegistrationOfExternalDataClayResponse.Builder builder = NotifyRegistrationOfExternalDataClayResponse
				.newBuilder();
		try {
			final DataClayInstanceID result = logicModule.notifyRegistrationOfExternalDataClay(
					Utils.getDataClayInstanceID(request.getExtDataClayID()), request.getHostname(), request.getPort());

			final NotifyRegistrationOfExternalDataClayResponse resp = builder.setExtDataClayID(Utils.getMsgID(result))
					.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			builder.setExcInfo(Utils.serializeException(e));
			final NotifyRegistrationOfExternalDataClayResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getExternalDataClayInfo(final GetExtDataClayInfoRequest request,
			final StreamObserver<GetExtDataClayInfoResponse> responseObserver) {
		final GetExtDataClayInfoResponse.Builder builder = GetExtDataClayInfoResponse.newBuilder();
		try {
			final DataClayInstance result = logicModule
					.getExternalDataClayInfo(Utils.getDataClayInstanceID(request.getExtDataClayID()));
			final GetExtDataClayInfoResponse resp = builder.setExtDataClayInfo(Utils.getDataClayInstance(result))
					.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			builder.setExcInfo(Utils.serializeException(e));
			final GetExtDataClayInfoResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getExternalDataclayId(final GetExternalDataclayIDRequest request,
			final StreamObserver<GetExternalDataclayIDResponse> responseObserver) {
		final GetExternalDataclayIDResponse.Builder builder = GetExternalDataclayIDResponse.newBuilder();
		try {
			final DataClayInstanceID result = logicModule.getExternalDataClayID(request.getHost(), request.getPort());
			final GetExternalDataclayIDResponse resp = builder.setExtDataClayID(Utils.getMsgID(result)).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			builder.setExcInfo(Utils.serializeException(e));
			final GetExternalDataclayIDResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void registerObjectFromGC(final RegisterObjectForGCRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			final CommonMessages.RegistrationInfo regInfoMsg = request.getRegInfo();
			final RegistrationInfo regInfo = new RegistrationInfo(
					Utils.getObjectID(regInfoMsg.getObjectID()),
					Utils.getMetaClassID(regInfoMsg.getClassID()),
					Utils.getSessionID(regInfoMsg.getSessionID()),
					Utils.getDataSetID(regInfoMsg.getDataSetID()),
					regInfoMsg.getAlias());

			logicModule.registerObjectFromGC(regInfo, Utils.getExecutionEnvironmentID(request.getBackendID()), null);

			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	@Override
	public void registerObjects(final RegisterObjectsRequest request,
			final StreamObserver<RegisterObjectsResponse> responseObserver) {
		try {

			List<RegistrationInfo> regInfos = new ArrayList<>();
			for (CommonMessages.RegistrationInfo regInfoMsg : request.getRegInfosList()) {
				String alias = null;
				if (regInfoMsg.getAlias() != null && !regInfoMsg.getAlias().isEmpty()) {
					alias = regInfoMsg.getAlias();
				}
				final RegistrationInfo regInfo = new RegistrationInfo(Utils.getObjectID(regInfoMsg.getObjectID()),
						Utils.getMetaClassID(regInfoMsg.getClassID()), Utils.getSessionID(regInfoMsg.getSessionID()),
						Utils.getDataSetID(regInfoMsg.getDataSetID()), alias);
				regInfos.add(regInfo);
			}


			List<ObjectID> registeredObjects = logicModule.registerObjects(regInfos,
					Utils.getExecutionEnvironmentID(request.getBackendID()),
					request.getLang());

			final RegisterObjectsResponse.Builder builder = RegisterObjectsResponse.newBuilder();
			for (ObjectID registeredObjectID : registeredObjects) {
				builder.addObjectIDs(Utils.getMsgID(registeredObjectID));
			}
			final RegisterObjectsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final RegisterObjectsResponse.Builder builder = RegisterObjectsResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final RegisterObjectsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	@Override
	public void getAllExecutionEnvironmentsInfo(final GetAllExecutionEnvironmentsInfoRequest request,
			final StreamObserver<GetAllExecutionEnvironmentsInfoResponse> responseObserver) {
		try {
			final Map<ExecutionEnvironmentID, ExecutionEnvironment> result = logicModule
					.getAllExecutionEnvironmentsInfo(request.getExecEnvLang(), request.getGetExternal());
			final GetAllExecutionEnvironmentsInfoResponse.Builder builder = GetAllExecutionEnvironmentsInfoResponse
					.newBuilder();
			for (final Entry<ExecutionEnvironmentID, ExecutionEnvironment> entry : result.entrySet()) {
				builder.putExecEnvs(entry.getKey().toString(), Utils.getExecutionEnvironment(entry.getValue()));
			}
			final GetAllExecutionEnvironmentsInfoResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetAllExecutionEnvironmentsInfoResponse.Builder builder = GetAllExecutionEnvironmentsInfoResponse
					.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetAllExecutionEnvironmentsInfoResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getObjectInfo(final GetObjectInfoRequest request,
			final StreamObserver<GetObjectInfoResponse> responseObserver) {
		try {
			final Tuple<String, String> objInfo = logicModule.getObjectInfo(Utils.getSessionID(request.getSessionID()),
					Utils.getObjectID(request.getObjectID()));
			final GetObjectInfoResponse resp = GetObjectInfoResponse.newBuilder().setClassname(objInfo.getFirst())
					.setNamespace(objInfo.getSecond()).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetObjectInfoResponse.Builder builder = GetObjectInfoResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetObjectInfoResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getObjectFromAlias(final GetObjectFromAliasRequest request,
			final StreamObserver<GetObjectFromAliasResponse> responseObserver) {
		try {
			final Triple<ObjectID, MetaClassID, ExecutionEnvironmentID> result = logicModule
					.getObjectFromAlias(Utils.getSessionID(request.getSessionID()), request.getAlias());
			final GetObjectFromAliasResponse resp = GetObjectFromAliasResponse.newBuilder()
					.setObjectID(Utils.getMsgID(result.getFirst())).setClassID(Utils.getMsgID(result.getSecond()))
					.setHint(Utils.getMsgID(result.getThird())).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetObjectFromAliasResponse.Builder builder = GetObjectFromAliasResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetObjectFromAliasResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void deleteAlias(final DeleteAliasRequest request, final StreamObserver<DeleteAliasResponse> responseObserver) {
		try {
			ObjectID objectID = logicModule.deleteAlias(Utils.getSessionID(request.getSessionID()), request.getAlias());

			final DeleteAliasResponse resp = DeleteAliasResponse.newBuilder().setObjectID(Utils.getMsgID(objectID))
					.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final DeleteAliasResponse.Builder builder = DeleteAliasResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final DeleteAliasResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getObjectsMetaDataInfoOfClassForNM(final GetObjectsMetaDataInfoOfClassForNMRequest request,
			final StreamObserver<GetObjectsMetaDataInfoOfClassForNMResponse> responseObserver) {
		try {
			final Map<ObjectID, MetaDataInfo> result = logicModule
					.getObjectsMetaDataInfoOfClassForNM(Utils.getMetaClassID(request.getClassID()));

			final GetObjectsMetaDataInfoOfClassForNMResponse.Builder builder = GetObjectsMetaDataInfoOfClassForNMResponse
					.newBuilder();
			for (final Entry<ObjectID, MetaDataInfo> entry : result.entrySet()) {
				builder.putMdataInfo(entry.getKey().toString(), Utils.getMetaDataInfo(entry.getValue()));
			}
			final GetObjectsMetaDataInfoOfClassForNMResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetObjectsMetaDataInfoOfClassForNMResponse.Builder builder = GetObjectsMetaDataInfoOfClassForNMResponse
					.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetObjectsMetaDataInfoOfClassForNMResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void moveObject(final MoveObjectRequest request, final StreamObserver<MoveObjectResponse> responseObserver) {
		try {

			final List<ObjectID> result = logicModule.moveObject(Utils.getSessionID(request.getSessionID()),
					Utils.getObjectID(request.getObjectID()), Utils.getExecutionEnvironmentID(request.getSrcBackendID()),
					Utils.getExecutionEnvironmentID(request.getDestBackendID()), request.getRecursive());

			final MoveObjectResponse.Builder builder = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectResponse
					.newBuilder();
			if (result != null) {
				for (final ObjectID curObjID : result) {
					builder.addObjectIDs(Utils.getMsgID(curObjID));
				}
			}
			final MoveObjectResponse resp = builder.build();

			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final MoveObjectResponse.Builder builder = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectResponse
					.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final MoveObjectResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void setObjectReadOnly(final SetObjectReadOnlyRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {

		try {

			logicModule.setObjectReadOnly(Utils.getSessionID(request.getSessionID()), Utils.getObjectID(request.getObjectID()));

			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void setObjectReadWrite(final SetObjectReadWriteRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {
		try {

			logicModule.setObjectReadWrite(Utils.getSessionID(request.getSessionID()), Utils.getObjectID(request.getObjectID()));

			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getMetadataByOID(final GetMetadataByOIDRequest request,
			final StreamObserver<GetMetadataByOIDResponse> responseObserver) {

		try {

			final MetaDataInfo result = logicModule.getMetadataByOID(Utils.getSessionID(request.getSessionID()),
					Utils.getObjectID(request.getObjectID()));
			final GetMetadataByOIDResponse.Builder builder = GetMetadataByOIDResponse.newBuilder();
			builder.setMdInfo(Utils.getMetaDataInfo(result));

			final GetMetadataByOIDResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetMetadataByOIDResponse.Builder builder = GetMetadataByOIDResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetMetadataByOIDResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	@Override
	public void getMetadataByOIDForDS(final GetMetadataByOIDForDSRequest request,
			final StreamObserver<GetMetadataByOIDForDSResponse> responseObserver) {

		try {

			final MetaDataInfo result = logicModule.getMetadataByOIDForDS(Utils.getObjectID(request.getObjectID()));

			final GetMetadataByOIDForDSResponse.Builder builder = GetMetadataByOIDForDSResponse.newBuilder();
			builder.setMdInfo(Utils.getMetaDataInfo(result));
			final GetMetadataByOIDForDSResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetMetadataByOIDForDSResponse.Builder builder = GetMetadataByOIDForDSResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetMetadataByOIDForDSResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	/**
	 * <pre>
	 * Execution Environment.
	 * </pre>
	 */
	@Override
	public void executeImplementation(final ExecuteImplementationRequest request,
			final StreamObserver<ExecuteImplementationResponse> responseObserver) {

		try {
			SerializedParametersOrReturn params = null;
			if (request.hasParams()) {
				params = Utils.getParamsOrReturn(request.getParams());
			}
			final SerializedParametersOrReturn result = logicModule
					.executeImplementation(Utils.getSessionID(request.getSessionID()), Utils.getOperationID(request.getOperationID()),
							new Triple<>(Utils.getImplementationID(request.getImplementationID()),
									Utils.getContractID(request.getContractID()), Utils.getInterfaceID(request.getInterfaceID())),
							Utils.getObjectID(request.getObjectID()), params);

			final ExecuteImplementationResponse.Builder builder = ExecuteImplementationResponse.newBuilder();
			if (result != null) {
				builder.setRet(Utils.getParamsOrReturn(result));
			}
			final ExecuteImplementationResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExecuteImplementationResponse.Builder builder = ExecuteImplementationResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final ExecuteImplementationResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	@Override
	public void executeMethodOnTarget(final ExecuteMethodOnTargetRequest request,
			final StreamObserver<ExecuteMethodOnTargetResponse> responseObserver) {
		try {
			SerializedParametersOrReturn params = null;
			if (request.hasParams()) {
				params = Utils.getParamsOrReturn(request.getParams());
			}
			final SerializedParametersOrReturn result = logicModule.executeMethodOnTarget(
					Utils.getSessionID(request.getSessionID()), Utils.getObjectID(request.getObjectID()),
					request.getOperationNameAndSignature(), params,
					Utils.getExecutionEnvironmentID(request.getTargetBackendID()));

			final ExecuteMethodOnTargetResponse.Builder builder = ExecuteMethodOnTargetResponse.newBuilder();
			if (result != null) {
				builder.setRet(Utils.getParamsOrReturn(result));
			}
			final ExecuteMethodOnTargetResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExecuteMethodOnTargetResponse.Builder builder = ExecuteMethodOnTargetResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final ExecuteMethodOnTargetResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	/**
	 * <pre>
	 * Stubs.
	 * </pre>
	 */
	@Override
	public void getStubs(final GetStubsRequest request, final StreamObserver<GetStubsResponse> responseObserver) {
		try {

			final List<ContractID> contractsIDs = new LinkedList<>();
			for (final String cID : request.getContractIDsList()) {
				contractsIDs.add(Utils.getContractID(cID));
			}

			final Map<String, byte[]> stubs = logicModule.getStubs(Utils.getAccountID(request.getApplicantAccountID()),
					Utils.getCredential(request.getCredentials()), request.getLanguage(), contractsIDs);

			final GetStubsResponse.Builder builder = GetStubsResponse.newBuilder();
			for (final Entry<String, byte[]> entry : stubs.entrySet()) {
				builder.putStubs(entry.getKey(), ByteString.copyFrom(entry.getValue()));
			}

			final GetStubsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetStubsResponse.Builder builder = GetStubsResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetStubsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}

	}

	@Override
	public void getBabelStubs(final GetBabelStubsRequest request,
			final StreamObserver<GetBabelStubsResponse> responseObserver) {
		try {

			final List<ContractID> contractsIDs = new LinkedList<>();
			for (final String cID : request.getContractIDsList()) {
				contractsIDs.add(Utils.getContractID(cID));
			}

			final byte[] result = logicModule.getBabelStubs(Utils.getAccountID(request.getAccountID()),
					Utils.getCredential(request.getCredentials()), contractsIDs);

			final GetBabelStubsResponse.Builder builder = GetBabelStubsResponse.newBuilder();
			builder.setYamlStub(ByteString.copyFrom(result));
			final GetBabelStubsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetBabelStubsResponse.Builder builder = GetBabelStubsResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetBabelStubsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	/**
	 * <pre>
	 * Notification Manager.
	 * </pre>
	 */
	@Override
	public void registerECA(final RegisterECARequest request, final StreamObserver<ExceptionInfo> responseObserver) {
		try {

			final ECA eca = (ECA) CommonYAML.getYamlObject().load(request.getEca());
			logicModule.registerEventListenerImplementation(Utils.getAccountID(request.getApplicantAccountID()),
					Utils.getCredential(request.getCredentials()), eca);

			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void adviseEvent(final AdviseEventRequest request, final StreamObserver<ExceptionInfo> responseObserver) {
		try {

			final EventMessage newEvent = (EventMessage) CommonYAML.getYamlObject().load(request.getEventYaml());
			logicModule.adviseEvent(newEvent);

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
	 * Others.
	 * </pre>
	 */
	@Override
	public void getClassNameForDS(final GetClassNameForDSRequest request,
			final StreamObserver<GetClassNameForDSResponse> responseObserver) {
		try {

			final String className = logicModule.getClassNameForDS(Utils.getMetaClassID(request.getClassID()));
			final GetClassNameForDSResponse resp = GetClassNameForDSResponse.newBuilder().setClassName(className)
					.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetClassNameForDSResponse.Builder builder = GetClassNameForDSResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetClassNameForDSResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getClassNameAndNamespaceForDS(final GetClassNameAndNamespaceForDSRequest request,
			final StreamObserver<GetClassNameAndNamespaceForDSResponse> responseObserver) {
		try {

			final Tuple<String, String> result = logicModule
					.getClassNameAndNamespaceForDS(Utils.getMetaClassID(request.getClassID()));
			final GetClassNameAndNamespaceForDSResponse resp = GetClassNameAndNamespaceForDSResponse.newBuilder()
					.setClassName(result.getFirst()).setNamespace(result.getSecond()).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception ex) {
			ex.printStackTrace();
			logger.debug("getClassNameAndNamespaceForDS error", ex);

			final GetClassNameAndNamespaceForDSResponse.Builder builder = GetClassNameAndNamespaceForDSResponse
					.newBuilder();
			builder.setExcInfo(Utils.serializeException(ex));
			final GetClassNameAndNamespaceForDSResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getContractIDOfDataClayProvider(final GetContractIDOfDataClayProviderRequest request,
			final StreamObserver<GetContractIDOfDataClayProviderResponse> responseObserver) {
		try {

			final ContractID result = logicModule.getContractIDOfDataClayProvider(
					Utils.getAccountID(request.getApplicantAccountID()), Utils.getCredential(request.getCredentials()));
			final GetContractIDOfDataClayProviderResponse resp = GetContractIDOfDataClayProviderResponse.newBuilder()
					.setContractID(Utils.getMsgID(result)).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetContractIDOfDataClayProviderResponse.Builder builder = GetContractIDOfDataClayProviderResponse
					.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetContractIDOfDataClayProviderResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void activateTracing(final ActivateTracingRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {

		final int currentAvailableTaskID = request.getTaskid();
		try {
			logicModule.activateTracing(currentAvailableTaskID);
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
		
	}

	@Override
	public void deactivateTracing(final EmptyMessage request, final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.deactivateTracing();
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
			final Map<String, byte[]> traces = logicModule.getTraces();
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
	public void cleanMetaDataCaches(final EmptyMessage request, final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.cleanMetaDataCaches();

			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void closeManagerDb(final EmptyMessage request, final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.closeManagerDb();

			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void closeDb(final EmptyMessage request, final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.closeDb();
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void setDataSetID(final SetDataSetIDRequest request, final StreamObserver<ExceptionInfo> responseObserver) {

		try {

			logicModule.setDataSetID(Utils.getSessionID(request.getSessionID()),
					Utils.getObjectID(request.getObjectID()),
					Utils.getDataSetID(request.getDatasetID()));

			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void setDataSetIDFromGarbageCollector(final SetDataSetIDFromGarbageCollectorRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {

		try {

			logicModule.setDataSetIDFromGarbageCollector(Utils.getObjectID(request.getObjectID()),
					Utils.getDataSetID(request.getDatasetID()));

			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void unregisterObjects(final UnregisterObjectsRequest request,
			final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			final Set<ObjectID> objectsToUnregister = new HashSet<>();
			for (final String oid : request
					.getObjectsToUnregisterList()) {
				objectsToUnregister.add(Utils.getObjectID(oid));
			}
			logicModule.unregisterObjects(objectsToUnregister);

			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void getClassesInNamespace(final GetClassesInNamespaceRequest request,
								  final StreamObserver<GetClassesInNamespaceResponse> responseObserver) {
		try {

			Tuple<Namespace, Set<MetaClass>> result = logicModule.getClassesInNamespace(request.getNamespaceName());
			Namespace namespace = result.getFirst();
			Set<MetaClass> metaClasses = result.getSecond();
			final GetClassesInNamespaceResponse.Builder builder = GetClassesInNamespaceResponse.newBuilder();
			builder.setNamespaceYaml(CommonYAML.getYamlObject().dump(namespace));
			for (final MetaClass mClass : metaClasses) {
				builder.addMetaClassYaml(CommonYAML.getYamlObject().dump(mClass));
			}

			final GetClassesInNamespaceResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetClassesInNamespaceResponse.Builder builder = GetClassesInNamespaceResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetClassesInNamespaceResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void importModelsFromExternalDataClay(final ImportModelsFromExternalDataClayRequest request,
									   final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.importModelsFromExternalDataClay(request.getNamespaceName(),
					Utils.getDataClayInstanceID(request.getDataClayID()));
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}


	@Override
	public void getStorageLocationID(final GetStorageLocationIDRequest request,
									  final StreamObserver<GetStorageLocationIDResponse> responseObserver) {
		try {

			StorageLocationID result = logicModule.getStorageLocationID(request.getSlName());
			final GetStorageLocationIDResponse resp = GetStorageLocationIDResponse.newBuilder()
					.setStorageLocationID(Utils.getMsgID(result)).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final GetStorageLocationIDResponse.Builder builder = GetStorageLocationIDResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final GetStorageLocationIDResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void notifyExecutionEnvironmentShutdown(final NotifyExecutionEnvironmentShutdownRequest request,
												 final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.notifyExecutionEnvironmentShutdown(
					Utils.getExecutionEnvironmentID(request.getExecutionEnvironmentID()));
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void notifyStorageLocationShutdown(final NotifyStorageLocationShutdownRequest request,
												   final StreamObserver<ExceptionInfo> responseObserver) {
		try {
			logicModule.notifyStorageLocationShutdown(
					Utils.getStorageLocationID(request.getStorageLocationID()));
			Utils.returnExceptionInfoMessage(responseObserver);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExceptionInfo resp = Utils.serializeException(e);
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void existsActiveEnvironmentsForSL(final ExistsActiveEnvironmentsForSLRequest request,
									 final StreamObserver<ExistsActiveEnvironmentsForSLResponse> responseObserver) {
		try {

			boolean result = logicModule.existsActiveEnvironmentsForSL(Utils.getStorageLocationID(request.getStorageLocationID()));
			final ExistsActiveEnvironmentsForSLResponse resp = ExistsActiveEnvironmentsForSLResponse.newBuilder()
					.setExists(result).build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();

		} catch (final Exception e) {
			final ExistsActiveEnvironmentsForSLResponse.Builder builder = ExistsActiveEnvironmentsForSLResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final ExistsActiveEnvironmentsForSLResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}


	@Override
	public void getNumObjects(final EmptyMessage request,
							  final io.grpc.stub.StreamObserver<CommonMessages.GetNumObjectsResponse> responseObserver) {
		try {
			final int result = logicModule.getNumObjects();
			final CommonMessages.GetNumObjectsResponse.Builder builder = CommonMessages.GetNumObjectsResponse.newBuilder();
			builder.setNumObjs(result);
			final CommonMessages.GetNumObjectsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			final CommonMessages.GetNumObjectsResponse.Builder builder = CommonMessages.GetNumObjectsResponse.newBuilder();
			builder.setExcInfo(Utils.serializeException(e));
			final CommonMessages.GetNumObjectsResponse resp = builder.build();
			responseObserver.onNext(resp);
			responseObserver.onCompleted();
		}
	}


}
