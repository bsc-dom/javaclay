// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: dataclay/communication/grpc/generated/logicmodule/logicmodule.proto

package es.bsc.dataclay.communication.grpc.generated.logicmodule;

public final class LogicModuleGrpcService {
  private LogicModuleGrpcService() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\nCdataclay/communication/grpc/generated/" +
      "logicmodule/logicmodule.proto\022\'dataclay." +
      "communication.grpc.logicmodule\032Kdataclay" +
      "/communication/grpc/messages/logicmodule" +
      "/logicmodule_messages.proto\032Adataclay/co" +
      "mmunication/grpc/messages/common/common_" +
      "messages.proto2\213\217\001\n\013LogicModule\022\205\001\n\016auto" +
      "registerSL\022>.dataclay.communication.grpc" +
      ".logicmodule.AutoRegisterSLRequest\0321.dat" +
      "aclay.communication.grpc.common.Exceptio" +
      "nInfo\"\000\022\223\001\n\016autoregisterEE\022>.dataclay.co" +
      "mmunication.grpc.logicmodule.AutoRegiste" +
      "rEERequest\032?.dataclay.communication.grpc" +
      ".logicmodule.AutoRegisterEEResponse\"\000\022\245\001" +
      "\n\024getStorageLocationID\022D.dataclay.commun" +
      "ication.grpc.logicmodule.GetStorageLocat" +
      "ionIDRequest\032E.dataclay.communication.gr" +
      "pc.logicmodule.GetStorageLocationIDRespo" +
      "nse\"\000\022\233\001\n\031unregisterStorageLocation\022I.da" +
      "taclay.communication.grpc.logicmodule.Un" +
      "registerStorageLocationRequest\0321.datacla" +
      "y.communication.grpc.common.ExceptionInf" +
      "o\"\000\022\245\001\n\036unregisterExecutionEnvironment\022N" +
      ".dataclay.communication.grpc.logicmodule" +
      ".UnregisterExecutionEnvironmentRequest\0321" +
      ".dataclay.communication.grpc.common.Exce" +
      "ptionInfo\"\000\022s\n\ncheckAlive\0220.dataclay.com" +
      "munication.grpc.common.EmptyMessage\0321.da" +
      "taclay.communication.grpc.common.Excepti" +
      "onInfo\"\000\022\244\001\n\027performSetOfNewAccounts\022B.d" +
      "ataclay.communication.grpc.logicmodule.P" +
      "erformSetAccountsRequest\032C.dataclay.comm" +
      "unication.grpc.logicmodule.PerformSetAcc" +
      "ountsResponse\"\000\022\247\001\n\026performSetOfOperatio" +
      "ns\022D.dataclay.communication.grpc.logicmo" +
      "dule.PerformSetOperationsRequest\032E.datac" +
      "lay.communication.grpc.logicmodule.Perfo" +
      "rmSetOperationsResponse\"\000\022\205\001\n\016publishAdd" +
      "ress\022>.dataclay.communication.grpc.logic" +
      "module.PublishAddressRequest\0321.dataclay." +
      "communication.grpc.common.ExceptionInfo\"" +
      "\000\022\225\001\n\021newAccountNoAdmin\022A.dataclay.commu" +
      "nication.grpc.logicmodule.NewAccountNoAd" +
      "minRequest\032;.dataclay.communication.grpc" +
      ".logicmodule.NewAccountResponse\"\000\022\207\001\n\nne" +
      "wAccount\022:.dataclay.communication.grpc.l" +
      "ogicmodule.NewAccountRequest\032;.dataclay." +
      "communication.grpc.logicmodule.NewAccoun" +
      "tResponse\"\000\022\215\001\n\014getAccountID\022<.dataclay." +
      "communication.grpc.logicmodule.GetAccoun" +
      "tIDRequest\032=.dataclay.communication.grpc" +
      ".logicmodule.GetAccountIDResponse\"\000\022\223\001\n\016" +
      "getAccountList\022>.dataclay.communication." +
      "grpc.logicmodule.GetAccountListRequest\032?" +
      ".dataclay.communication.grpc.logicmodule" +
      ".GetAccountListResponse\"\000\022\207\001\n\nnewSession" +
      "\022:.dataclay.communication.grpc.logicmodu" +
      "le.NewSessionRequest\032;.dataclay.communic" +
      "ation.grpc.logicmodule.NewSessionRespons" +
      "e\"\000\022\250\001\n\025getInfoOfSessionForDS\022E.dataclay" +
      ".communication.grpc.logicmodule.GetInfoO" +
      "fSessionForDSRequest\032F.dataclay.communic" +
      "ation.grpc.logicmodule.GetInfoOfSessionF" +
      "orDSResponse\"\000\022\215\001\n\014newNamespace\022<.datacl" +
      "ay.communication.grpc.logicmodule.NewNam" +
      "espaceRequest\032=.dataclay.communication.g" +
      "rpc.logicmodule.NewNamespaceResponse\"\000\022\207" +
      "\001\n\017removeNamespace\022?.dataclay.communicat" +
      "ion.grpc.logicmodule.RemoveNamespaceRequ" +
      "est\0321.dataclay.communication.grpc.common" +
      ".ExceptionInfo\"\000\022\223\001\n\016getNamespaceID\022>.da" +
      "taclay.communication.grpc.logicmodule.Ge" +
      "tNamespaceIDRequest\032?.dataclay.communica" +
      "tion.grpc.logicmodule.GetNamespaceIDResp" +
      "onse\"\000\022\231\001\n\020getNamespaceLang\022@.dataclay.c" +
      "ommunication.grpc.logicmodule.GetNamespa" +
      "ceLangRequest\032A.dataclay.communication.g" +
      "rpc.logicmodule.GetNamespaceLangResponse" +
      "\"\000\022\237\001\n\022getObjectDataSetID\022B.dataclay.com" +
      "munication.grpc.logicmodule.GetObjectDat" +
      "aSetIDRequest\032C.dataclay.communication.g" +
      "rpc.logicmodule.GetObjectDataSetIDRespon" +
      "se\"\000\022\207\001\n\017importInterface\022?.dataclay.comm" +
      "unication.grpc.logicmodule.ImportInterfa" +
      "ceRequest\0321.dataclay.communication.grpc." +
      "common.ExceptionInfo\"\000\022\205\001\n\016importContrac" +
      "t\022>.dataclay.communication.grpc.logicmod" +
      "ule.ImportContractRequest\0321.dataclay.com" +
      "munication.grpc.common.ExceptionInfo\"\000\022\272" +
      "\001\n\033getInfoOfClassesInNamespace\022K.datacla" +
      "y.communication.grpc.logicmodule.GetInfo" +
      "OfClassesInNamespaceRequest\032L.dataclay.c" +
      "ommunication.grpc.logicmodule.GetInfoOfC" +
      "lassesInNamespaceResponse\"\000\022\314\001\n!getImpor" +
      "tedClassesInfoInNamespace\022Q.dataclay.com" +
      "munication.grpc.logicmodule.GetImportedC" +
      "lassesInfoInNamespaceRequest\032R.dataclay." +
      "communication.grpc.logicmodule.GetImport" +
      "edClassesInfoInNamespaceResponse\"\000\022\245\001\n\024g" +
      "etClassIDfromImport\022D.dataclay.communica" +
      "tion.grpc.logicmodule.GetClassIDFromImpo" +
      "rtRequest\032E.dataclay.communication.grpc." +
      "logicmodule.GetClassIDFromImportResponse" +
      "\"\000\022\220\001\n\rgetNamespaces\022=.dataclay.communic" +
      "ation.grpc.logicmodule.GetNamespacesRequ" +
      "est\032>.dataclay.communication.grpc.logicm" +
      "odule.GetNamespacesResponse\"\000\022\207\001\n\nnewDat" +
      "aSet\022:.dataclay.communication.grpc.logic" +
      "module.NewDataSetRequest\032;.dataclay.comm" +
      "unication.grpc.logicmodule.NewDataSetRes" +
      "ponse\"\000\022\203\001\n\rremoveDataSet\022=.dataclay.com" +
      "munication.grpc.logicmodule.RemoveDataSe" +
      "tRequest\0321.dataclay.communication.grpc.c" +
      "ommon.ExceptionInfo\"\000\022\215\001\n\014getDataSetID\022<" +
      ".dataclay.communication.grpc.logicmodule" +
      ".GetDataSetIDRequest\032=.dataclay.communic" +
      "ation.grpc.logicmodule.GetDataSetIDRespo" +
      "nse\"\000\022\245\001\n\024checkDataSetIsPublic\022D.datacla" +
      "y.communication.grpc.logicmodule.CheckDa" +
      "taSetIsPublicRequest\032E.dataclay.communic" +
      "ation.grpc.logicmodule.CheckDataSetIsPub" +
      "licResponse\"\000\022\234\001\n\021getPublicDataSets\022A.da" +
      "taclay.communication.grpc.logicmodule.Ge" +
      "tPublicDataSetsRequest\032B.dataclay.commun" +
      "ication.grpc.logicmodule.GetPublicDataSe" +
      "tsResponse\"\000\022\237\001\n\022getAccountDataSets\022B.da" +
      "taclay.communication.grpc.logicmodule.Ge" +
      "tAccountDataSetsRequest\032C.dataclay.commu" +
      "nication.grpc.logicmodule.GetAccountData" +
      "SetsResponse\"\000\022\201\001\n\010newClass\0228.dataclay.c" +
      "ommunication.grpc.logicmodule.NewClassRe" +
      "quest\0329.dataclay.communication.grpc.logi" +
      "cmodule.NewClassResponse\"\000\022\207\001\n\nnewClassI" +
      "D\022:.dataclay.communication.grpc.logicmod" +
      "ule.NewClassIDRequest\032;.dataclay.communi" +
      "cation.grpc.logicmodule.NewClassIDRespon" +
      "se\"\000\022\177\n\013removeClass\022;.dataclay.communica" +
      "tion.grpc.logicmodule.RemoveClassRequest" +
      "\0321.dataclay.communication.grpc.common.Ex" +
      "ceptionInfo\"\000\022\207\001\n\017removeOperation\022?.data" +
      "clay.communication.grpc.logicmodule.Remo" +
      "veOperationRequest\0321.dataclay.communicat" +
      "ion.grpc.common.ExceptionInfo\"\000\022\221\001\n\024remo" +
      "veImplementation\022D.dataclay.communicatio" +
      "n.grpc.logicmodule.RemoveImplementationR" +
      "equest\0321.dataclay.communication.grpc.com" +
      "mon.ExceptionInfo\"\000\022\223\001\n\016getOperationID\022>" +
      ".dataclay.communication.grpc.logicmodule" +
      ".GetOperationIDRequest\032?.dataclay.commun" +
      "ication.grpc.logicmodule.GetOperationIDR" +
      "esponse\"\000\022\220\001\n\rgetPropertyID\022=.dataclay.c" +
      "ommunication.grpc.logicmodule.GetPropert" +
      "yIDRequest\032>.dataclay.communication.grpc" +
      ".logicmodule.GetPropertyIDResponse\"\000\022\207\001\n" +
      "\ngetClassID\022:.dataclay.communication.grp" +
      "c.logicmodule.GetClassIDRequest\032;.datacl" +
      "ay.communication.grpc.logicmodule.GetCla" +
      "ssIDResponse\"\000\022\215\001\n\014getClassInfo\022<.datacl" +
      "ay.communication.grpc.logicmodule.GetCla" +
      "ssInfoRequest\032=.dataclay.communication.g" +
      "rpc.logicmodule.GetClassInfoResponse\"\000\022\212" +
      "\001\n\013newContract\022;.dataclay.communication." +
      "grpc.logicmodule.NewContractRequest\032<.da" +
      "taclay.communication.grpc.logicmodule.Ne" +
      "wContractResponse\"\000\022\231\001\n\030registerToPublic" +
      "Contract\022H.dataclay.communication.grpc.l" +
      "ogicmodule.RegisterToPublicContractReque" +
      "st\0321.dataclay.communication.grpc.common." +
      "ExceptionInfo\"\000\022\322\001\n#registerToPublicCont" +
      "ractOfNamespace\022S.dataclay.communication" +
      ".grpc.logicmodule.RegisterToPublicContra" +
      "ctOfNamespaceRequest\032T.dataclay.communic" +
      "ation.grpc.logicmodule.RegisterToPublicC" +
      "ontractOfNamespaceResponse\"\000\022\264\001\n\031getCont" +
      "ractIDsOfApplicant\022I.dataclay.communicat" +
      "ion.grpc.logicmodule.GetContractIDsOfApp" +
      "licantRequest\032J.dataclay.communication.g" +
      "rpc.logicmodule.GetContractIDsOfApplican" +
      "tResponse\"\000\022\261\001\n\030getContractIDsOfProvider" +
      "\022H.dataclay.communication.grpc.logicmodu" +
      "le.GetContractIDsOfProviderRequest\032I.dat" +
      "aclay.communication.grpc.logicmodule.Get" +
      "ContractIDsOfProviderResponse\"\000\022\314\001\n%getC" +
      "ontractIDsOfApplicantWithProvider\022O.data" +
      "clay.communication.grpc.logicmodule.GetC" +
      "ontractsOfApplicantWithProvRequest\032P.dat" +
      "aclay.communication.grpc.logicmodule.Get" +
      "ContractsOfApplicantWithProvResponse\"\000\022\226" +
      "\001\n\017newDataContract\022?.dataclay.communicat" +
      "ion.grpc.logicmodule.NewDataContractRequ" +
      "est\032@.dataclay.communication.grpc.logicm" +
      "odule.NewDataContractResponse\"\000\022\241\001\n\034regi" +
      "sterToPublicDataContract\022L.dataclay.comm" +
      "unication.grpc.logicmodule.RegisterToPub" +
      "licDataContractRequest\0321.dataclay.commun" +
      "ication.grpc.common.ExceptionInfo\"\000\022\300\001\n\035" +
      "getDataContractIDsOfApplicant\022M.dataclay" +
      ".communication.grpc.logicmodule.GetDataC" +
      "ontractIDsOfApplicantRequest\032N.dataclay." +
      "communication.grpc.logicmodule.GetDataCo" +
      "ntractIDsOfApplicantResponse\"\000\022\275\001\n\034getDa" +
      "taContractIDsOfProvider\022L.dataclay.commu" +
      "nication.grpc.logicmodule.GetDataContrac" +
      "tIDsOfProviderRequest\032M.dataclay.communi" +
      "cation.grpc.logicmodule.GetDataContractI" +
      "DsOfProviderResponse\"\000\022\337\001\n*getDataContra" +
      "ctInfoOfApplicantWithProvider\022V.dataclay" +
      ".communication.grpc.logicmodule.GetDataC" +
      "ontractInfoOfApplicantWithProvRequest\032W." +
      "dataclay.communication.grpc.logicmodule." +
      "GetDataContractInfoOfApplicantWithProvRe" +
      "sponse\"\000\022\215\001\n\014newInterface\022<.dataclay.com" +
      "munication.grpc.logicmodule.NewInterface" +
      "Request\032=.dataclay.communication.grpc.lo" +
      "gicmodule.NewInterfaceResponse\"\000\022\231\001\n\020get" +
      "InterfaceInfo\022@.dataclay.communication.g" +
      "rpc.logicmodule.GetInterfaceInfoRequest\032" +
      "A.dataclay.communication.grpc.logicmodul" +
      "e.GetInterfaceInfoResponse\"\000\022\207\001\n\017removeI" +
      "nterface\022?.dataclay.communication.grpc.l" +
      "ogicmodule.RemoveInterfaceRequest\0321.data" +
      "clay.communication.grpc.common.Exception" +
      "Info\"\000\022\275\001\n\034getExecutionEnvironmentsInfo\022" +
      "L.dataclay.communication.grpc.logicmodul" +
      "e.GetExecutionEnvironmentsInfoRequest\032M." +
      "dataclay.communication.grpc.logicmodule." +
      "GetExecutionEnvironmentsInfoResponse\"\000\022\300" +
      "\001\n\035getExecutionEnvironmentsNames\022M.datac" +
      "lay.communication.grpc.logicmodule.GetEx" +
      "ecutionEnvironmentsNamesRequest\032N.datacl" +
      "ay.communication.grpc.logicmodule.GetExe" +
      "cutionEnvironmentsNamesResponse\"\000\022\275\001\n\034ge" +
      "tExecutionEnvironmentForDS\022L.dataclay.co" +
      "mmunication.grpc.logicmodule.GetExecutio" +
      "nEnvironmentForDSRequest\032M.dataclay.comm" +
      "unication.grpc.logicmodule.GetExecutionE" +
      "nvironmentForDSResponse\"\000\022\256\001\n\027getStorage" +
      "LocationForDS\022G.dataclay.communication.g" +
      "rpc.logicmodule.GetStorageLocationForDSR" +
      "equest\032H.dataclay.communication.grpc.log" +
      "icmodule.GetStorageLocationForDSResponse" +
      "\"\000\022\220\001\n\rgetObjectInfo\022=.dataclay.communic" +
      "ation.grpc.logicmodule.GetObjectInfoRequ" +
      "est\032>.dataclay.communication.grpc.logicm" +
      "odule.GetObjectInfoResponse\"\000\022\237\001\n\022getObj" +
      "ectFromAlias\022B.dataclay.communication.gr" +
      "pc.logicmodule.GetObjectFromAliasRequest" +
      "\032C.dataclay.communication.grpc.logicmodu" +
      "le.GetObjectFromAliasResponse\"\000\022\177\n\013delet" +
      "eAlias\022;.dataclay.communication.grpc.log" +
      "icmodule.DeleteAliasRequest\0321.dataclay.c" +
      "ommunication.grpc.common.ExceptionInfo\"\000" +
      "\022\317\001\n\"getObjectsMetaDataInfoOfClassForNM\022" +
      "R.dataclay.communication.grpc.logicmodul" +
      "e.GetObjectsMetaDataInfoOfClassForNMRequ" +
      "est\032S.dataclay.communication.grpc.logicm" +
      "odule.GetObjectsMetaDataInfoOfClassForNM" +
      "Response\"\000\022y\n\010addAlias\0228.dataclay.commun" +
      "ication.grpc.logicmodule.AddAliasRequest" +
      "\0321.dataclay.communication.grpc.common.Ex" +
      "ceptionInfo\"\000\022\220\001\n\024registerObjectFromGC\022C" +
      ".dataclay.communication.grpc.logicmodule" +
      ".RegisterObjectForGCRequest\0321.dataclay.c" +
      "ommunication.grpc.common.ExceptionInfo\"\000" +
      "\022\213\001\n\021unregisterObjects\022A.dataclay.commun" +
      "ication.grpc.logicmodule.UnregisterObjec" +
      "tsRequest\0321.dataclay.communication.grpc." +
      "common.ExceptionInfo\"\000\022\223\001\n\016registerObjec" +
      "t\022>.dataclay.communication.grpc.logicmod" +
      "ule.RegisterObjectRequest\032?.dataclay.com" +
      "munication.grpc.logicmodule.RegisterObje" +
      "ctResponse\"\000\022\251\001\n setDataSetIDFromGarbage" +
      "Collector\022P.dataclay.communication.grpc." +
      "logicmodule.SetDataSetIDFromGarbageColle" +
      "ctorRequest\0321.dataclay.communication.grp" +
      "c.common.ExceptionInfo\"\000\022\201\001\n\014setDataSetI" +
      "D\022<.dataclay.communication.grpc.logicmod" +
      "ule.SetDataSetIDRequest\0321.dataclay.commu" +
      "nication.grpc.common.ExceptionInfo\"\000\022\207\001\n" +
      "\nnewVersion\022:.dataclay.communication.grp" +
      "c.logicmodule.NewVersionRequest\032;.datacl" +
      "ay.communication.grpc.logicmodule.NewVer" +
      "sionResponse\"\000\022\215\001\n\022consolidateVersion\022B." +
      "dataclay.communication.grpc.logicmodule." +
      "ConsolidateVersionRequest\0321.dataclay.com" +
      "munication.grpc.common.ExceptionInfo\"\000\022\207" +
      "\001\n\nnewReplica\022:.dataclay.communication.g" +
      "rpc.logicmodule.NewReplicaRequest\032;.data" +
      "clay.communication.grpc.logicmodule.NewR" +
      "eplicaResponse\"\000\022\207\001\n\nmoveObject\022:.datacl" +
      "ay.communication.grpc.logicmodule.MoveOb" +
      "jectRequest\032;.dataclay.communication.grp" +
      "c.logicmodule.MoveObjectResponse\"\000\022\213\001\n\021s" +
      "etObjectReadOnly\022A.dataclay.communicatio" +
      "n.grpc.logicmodule.SetObjectReadOnlyRequ" +
      "est\0321.dataclay.communication.grpc.common" +
      ".ExceptionInfo\"\000\022\215\001\n\022setObjectReadWrite\022" +
      "B.dataclay.communication.grpc.logicmodul" +
      "e.SetObjectReadWriteRequest\0321.dataclay.c" +
      "ommunication.grpc.common.ExceptionInfo\"\000" +
      "\022\231\001\n\020getMetadataByOID\022@.dataclay.communi" +
      "cation.grpc.logicmodule.GetMetadataByOID" +
      "Request\032A.dataclay.communication.grpc.lo" +
      "gicmodule.GetMetadataByOIDResponse\"\000\022\250\001\n" +
      "\025executeImplementation\022E.dataclay.commun" +
      "ication.grpc.logicmodule.ExecuteImplemen" +
      "tationRequest\032F.dataclay.communication.g" +
      "rpc.logicmodule.ExecuteImplementationRes" +
      "ponse\"\000\022\250\001\n\025executeMethodOnTarget\022E.data" +
      "clay.communication.grpc.logicmodule.Exec" +
      "uteMethodOnTargetRequest\032F.dataclay.comm" +
      "unication.grpc.logicmodule.ExecuteMethod" +
      "OnTargetResponse\"\000\022\235\001\n\032synchronizeFedera" +
      "tedObject\022J.dataclay.communication.grpc." +
      "logicmodule.SynchronizeFederatedObjectRe" +
      "quest\0321.dataclay.communication.grpc.comm" +
      "on.ExceptionInfo\"\000\022\203\001\n\rgetDataClayID\0220.d" +
      "ataclay.communication.grpc.common.EmptyM" +
      "essage\032>.dataclay.communication.grpc.log" +
      "icmodule.GetDataClayIDResponse\"\000\022\261\001\n\030reg" +
      "isterExternalDataClay\022H.dataclay.communi" +
      "cation.grpc.logicmodule.RegisterExternal" +
      "DataClayRequest\032I.dataclay.communication" +
      ".grpc.logicmodule.RegisterExternalDataCl" +
      "ayResponse\"\000\022\323\001\n)registerExternalDataCla" +
      "yOverrideAuthority\022Y.dataclay.communicat" +
      "ion.grpc.logicmodule.RegisterExternalDat" +
      "aClayOverrideAuthorityRequest\032I.dataclay" +
      ".communication.grpc.logicmodule.Register" +
      "ExternalDataClayResponse\"\000\022\325\001\n$notifyReg" +
      "istrationOfExternalDataClay\022T.dataclay.c" +
      "ommunication.grpc.logicmodule.NotifyRegi" +
      "strationOfExternalDataClayRequest\032U.data" +
      "clay.communication.grpc.logicmodule.Noti" +
      "fyRegistrationOfExternalDataClayResponse" +
      "\"\000\022\244\001\n\027getExternalDataClayInfo\022B.datacla" +
      "y.communication.grpc.logicmodule.GetExtD" +
      "ataClayInfoRequest\032C.dataclay.communicat" +
      "ion.grpc.logicmodule.GetExtDataClayInfoR" +
      "esponse\"\000\022\250\001\n\025getExternalDataclayId\022E.da" +
      "taclay.communication.grpc.logicmodule.Ge" +
      "tExternalDataclayIDRequest\032F.dataclay.co" +
      "mmunication.grpc.logicmodule.GetExternal" +
      "DataclayIDResponse\"\000\022\205\001\n\016federateObject\022" +
      ">.dataclay.communication.grpc.logicmodul" +
      "e.FederateObjectRequest\0321.dataclay.commu" +
      "nication.grpc.common.ExceptionInfo\"\000\022\211\001\n" +
      "\020unfederateObject\022@.dataclay.communicati" +
      "on.grpc.logicmodule.UnfederateObjectRequ" +
      "est\0321.dataclay.communication.grpc.common" +
      ".ExceptionInfo\"\000\022\231\001\n\030notifyUnfederatedOb" +
      "jects\022H.dataclay.communication.grpc.logi" +
      "cmodule.NotifyUnfederatedObjectsRequest\032" +
      "1.dataclay.communication.grpc.common.Exc" +
      "eptionInfo\"\000\022\225\001\n\026notifyFederatedObjects\022" +
      "F.dataclay.communication.grpc.logicmodul" +
      "e.NotifyFederatedObjectsRequest\0321.datacl" +
      "ay.communication.grpc.common.ExceptionIn" +
      "fo\"\000\022\343\001\n*checkObjectIsFederatedWithDataC" +
      "layInstance\022X.dataclay.communication.grp" +
      "c.logicmodule.CheckObjectFederatedWithDa" +
      "taClayInstanceRequest\032Y.dataclay.communi" +
      "cation.grpc.logicmodule.CheckObjectFeder" +
      "atedWithDataClayInstanceResponse\"\000\022\314\001\n!g" +
      "etDataClaysObjectIsFederatedWith\022Q.datac" +
      "lay.communication.grpc.logicmodule.GetDa" +
      "taClaysObjectIsFederatedWithRequest\032R.da" +
      "taclay.communication.grpc.logicmodule.Ge" +
      "tDataClaysObjectIsFederatedWithResponse\"" +
      "\000\022\314\001\n!getExternalSourceDataClayOfObject\022" +
      "Q.dataclay.communication.grpc.logicmodul" +
      "e.GetExternalSourceDataClayOfObjectReque" +
      "st\032R.dataclay.communication.grpc.logicmo" +
      "dule.GetExternalSourceDataClayOfObjectRe" +
      "sponse\"\000\022\221\001\n\024unfederateAllObjects\022D.data" +
      "clay.communication.grpc.logicmodule.Unfe" +
      "derateAllObjectsRequest\0321.dataclay.commu" +
      "nication.grpc.common.ExceptionInfo\"\000\022\245\001\n" +
      "\036unfederateAllObjectsWithAllDCs\022N.datacl" +
      "ay.communication.grpc.logicmodule.Unfede" +
      "rateAllObjectsWithAllDCsRequest\0321.datacl" +
      "ay.communication.grpc.common.ExceptionIn" +
      "fo\"\000\022\235\001\n\032unfederateObjectWithAllDCs\022J.da" +
      "taclay.communication.grpc.logicmodule.Un" +
      "federateObjectWithAllDCsRequest\0321.datacl" +
      "ay.communication.grpc.common.ExceptionIn" +
      "fo\"\000\022\227\001\n\027migrateFederatedObjects\022G.datac" +
      "lay.communication.grpc.logicmodule.Migra" +
      "teFederatedObjectsRequest\0321.dataclay.com" +
      "munication.grpc.common.ExceptionInfo\"\000\022\215" +
      "\001\n\022federateAllObjects\022B.dataclay.communi" +
      "cation.grpc.logicmodule.FederateAllObjec" +
      "tsRequest\0321.dataclay.communication.grpc." +
      "common.ExceptionInfo\"\000\022\250\001\n\025getClassesInN" +
      "amespace\022E.dataclay.communication.grpc.l" +
      "ogicmodule.GetClassesInNamespaceRequest\032" +
      "F.dataclay.communication.grpc.logicmodul" +
      "e.GetClassesInNamespaceResponse\"\000\022\305\001\n.re" +
      "gisterClassesInNamespaceFromExternalData" +
      "Clay\022^.dataclay.communication.grpc.logic",
      "module.RegisterClassesInNamespaceFromExt" +
      "ernalDataClayRequest\0321.dataclay.communic" +
      "ation.grpc.common.ExceptionInfo\"\000\022\201\001\n\010ge" +
      "tStubs\0228.dataclay.communication.grpc.log" +
      "icmodule.GetStubsRequest\0329.dataclay.comm" +
      "unication.grpc.logicmodule.GetStubsRespo" +
      "nse\"\000\022\220\001\n\rgetBabelStubs\022=.dataclay.commu" +
      "nication.grpc.logicmodule.GetBabelStubsR" +
      "equest\032>.dataclay.communication.grpc.log" +
      "icmodule.GetBabelStubsResponse\"\000\022\177\n\013regi" +
      "sterECA\022;.dataclay.communication.grpc.lo" +
      "gicmodule.RegisterECARequest\0321.dataclay." +
      "communication.grpc.common.ExceptionInfo\"" +
      "\000\022\177\n\013adviseEvent\022;.dataclay.communicatio" +
      "n.grpc.logicmodule.AdviseEventRequest\0321." +
      "dataclay.communication.grpc.common.Excep" +
      "tionInfo\"\000\022\221\001\n\024isPrefetchingEnabled\0220.da" +
      "taclay.communication.grpc.common.EmptyMe" +
      "ssage\032E.dataclay.communication.grpc.logi" +
      "cmodule.IsPrefetchingEnabledResponse\"\000\022\234" +
      "\001\n\021getClassNameForDS\022A.dataclay.communic" +
      "ation.grpc.logicmodule.GetClassNameForDS" +
      "Request\032B.dataclay.communication.grpc.lo" +
      "gicmodule.GetClassNameForDSResponse\"\000\022\300\001" +
      "\n\035getClassNameAndNamespaceForDS\022M.datacl" +
      "ay.communication.grpc.logicmodule.GetCla" +
      "ssNameAndNamespaceForDSRequest\032N.datacla" +
      "y.communication.grpc.logicmodule.GetClas" +
      "sNameAndNamespaceForDSResponse\"\000\022\306\001\n\037get" +
      "ContractIDOfDataClayProvider\022O.dataclay." +
      "communication.grpc.logicmodule.GetContra" +
      "ctIDOfDataClayProviderRequest\032P.dataclay" +
      ".communication.grpc.logicmodule.GetContr" +
      "actIDOfDataClayProviderResponse\"\000\022\253\001\n\026ob" +
      "jectExistsInDataClay\022F.dataclay.communic" +
      "ation.grpc.logicmodule.ObjectExistsInDat" +
      "aClayRequest\032G.dataclay.communication.gr" +
      "pc.logicmodule.ObjectExistsInDataClayRes" +
      "ponse\"\000\022\201\001\n\014closeSession\022<.dataclay.comm" +
      "unication.grpc.logicmodule.CloseSessionR" +
      "equest\0321.dataclay.communication.grpc.com" +
      "mon.ExceptionInfo\"\000\022\250\001\n\025getMetadataByOID" +
      "ForDS\022E.dataclay.communication.grpc.logi" +
      "cmodule.GetMetadataByOIDForDSRequest\032F.d" +
      "ataclay.communication.grpc.logicmodule.G" +
      "etMetadataByOIDForDSResponse\"\000\022\207\001\n\017activ" +
      "ateTracing\022?.dataclay.communication.grpc" +
      ".logicmodule.ActivateTracingRequest\0321.da" +
      "taclay.communication.grpc.common.Excepti" +
      "onInfo\"\000\022z\n\021deactivateTracing\0220.dataclay" +
      ".communication.grpc.common.EmptyMessage\032" +
      "1.dataclay.communication.grpc.common.Exc" +
      "eptionInfo\"\000\022v\n\tgetTraces\0220.dataclay.com" +
      "munication.grpc.common.EmptyMessage\0325.da" +
      "taclay.communication.grpc.common.GetTrac" +
      "esResponse\"\000\022|\n\023cleanMetaDataCaches\0220.da" +
      "taclay.communication.grpc.common.EmptyMe" +
      "ssage\0321.dataclay.communication.grpc.comm" +
      "on.ExceptionInfo\"\000\022w\n\016closeManagerDb\0220.d" +
      "ataclay.communication.grpc.common.EmptyM" +
      "essage\0321.dataclay.communication.grpc.com" +
      "mon.ExceptionInfo\"\000\022p\n\007closeDb\0220.datacla" +
      "y.communication.grpc.common.EmptyMessage" +
      "\0321.dataclay.communication.grpc.common.Ex" +
      "ceptionInfo\"\000BR\n8es.bsc.dataclay.communi" +
      "cation.grpc.generated.logicmoduleB\026Logic" +
      "ModuleGrpcServiceb\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.getDescriptor(),
          es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.getDescriptor(),
        });
    es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.getDescriptor();
    es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
