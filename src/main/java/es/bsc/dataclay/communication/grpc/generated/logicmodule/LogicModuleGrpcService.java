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
      "messages.proto2\303\221\001\n\013LogicModule\022\205\001\n\016auto" +
      "registerSL\022>.dataclay.communication.grpc" +
      ".logicmodule.AutoRegisterSLRequest\0321.dat" +
      "aclay.communication.grpc.common.Exceptio" +
      "nInfo\"\000\022\223\001\n\016autoregisterEE\022>.dataclay.co" +
      "mmunication.grpc.logicmodule.AutoRegiste" +
      "rEERequest\032?.dataclay.communication.grpc" +
      ".logicmodule.AutoRegisterEEResponse\"\000\022\233\001" +
      "\n\031unregisterStorageLocation\022I.dataclay.c" +
      "ommunication.grpc.logicmodule.Unregister" +
      "StorageLocationRequest\0321.dataclay.commun" +
      "ication.grpc.common.ExceptionInfo\"\000\022\245\001\n\036" +
      "unregisterExecutionEnvironment\022N.datacla" +
      "y.communication.grpc.logicmodule.Unregis" +
      "terExecutionEnvironmentRequest\0321.datacla" +
      "y.communication.grpc.common.ExceptionInf" +
      "o\"\000\022s\n\ncheckAlive\0220.dataclay.communicati" +
      "on.grpc.common.EmptyMessage\0321.dataclay.c" +
      "ommunication.grpc.common.ExceptionInfo\"\000" +
      "\022\255\001\n\"notifyExecutionEnvironmentShutdown\022" +
      "R.dataclay.communication.grpc.logicmodul" +
      "e.NotifyExecutionEnvironmentShutdownRequ" +
      "est\0321.dataclay.communication.grpc.common" +
      ".ExceptionInfo\"\000\022\243\001\n\035notifyStorageLocati" +
      "onShutdown\022M.dataclay.communication.grpc" +
      ".logicmodule.NotifyStorageLocationShutdo" +
      "wnRequest\0321.dataclay.communication.grpc." +
      "common.ExceptionInfo\"\000\022\300\001\n\035existsActiveE" +
      "nvironmentsForSL\022M.dataclay.communicatio" +
      "n.grpc.logicmodule.ExistsActiveEnvironme" +
      "ntsForSLRequest\032N.dataclay.communication" +
      ".grpc.logicmodule.ExistsActiveEnvironmen" +
      "tsForSLResponse\"\000\022\244\001\n\027performSetOfNewAcc" +
      "ounts\022B.dataclay.communication.grpc.logi" +
      "cmodule.PerformSetAccountsRequest\032C.data" +
      "clay.communication.grpc.logicmodule.Perf" +
      "ormSetAccountsResponse\"\000\022\247\001\n\026performSetO" +
      "fOperations\022D.dataclay.communication.grp" +
      "c.logicmodule.PerformSetOperationsReques" +
      "t\032E.dataclay.communication.grpc.logicmod" +
      "ule.PerformSetOperationsResponse\"\000\022\205\001\n\016p" +
      "ublishAddress\022>.dataclay.communication.g" +
      "rpc.logicmodule.PublishAddressRequest\0321." +
      "dataclay.communication.grpc.common.Excep" +
      "tionInfo\"\000\022\225\001\n\021newAccountNoAdmin\022A.datac" +
      "lay.communication.grpc.logicmodule.NewAc" +
      "countNoAdminRequest\032;.dataclay.communica" +
      "tion.grpc.logicmodule.NewAccountResponse" +
      "\"\000\022\207\001\n\nnewAccount\022:.dataclay.communicati" +
      "on.grpc.logicmodule.NewAccountRequest\032;." +
      "dataclay.communication.grpc.logicmodule." +
      "NewAccountResponse\"\000\022\215\001\n\014getAccountID\022<." +
      "dataclay.communication.grpc.logicmodule." +
      "GetAccountIDRequest\032=.dataclay.communica" +
      "tion.grpc.logicmodule.GetAccountIDRespon" +
      "se\"\000\022\223\001\n\016getAccountList\022>.dataclay.commu" +
      "nication.grpc.logicmodule.GetAccountList" +
      "Request\032?.dataclay.communication.grpc.lo" +
      "gicmodule.GetAccountListResponse\"\000\022\207\001\n\nn" +
      "ewSession\022:.dataclay.communication.grpc." +
      "logicmodule.NewSessionRequest\032;.dataclay" +
      ".communication.grpc.logicmodule.NewSessi" +
      "onResponse\"\000\022\250\001\n\025getInfoOfSessionForDS\022E" +
      ".dataclay.communication.grpc.logicmodule" +
      ".GetInfoOfSessionForDSRequest\032F.dataclay" +
      ".communication.grpc.logicmodule.GetInfoO" +
      "fSessionForDSResponse\"\000\022\215\001\n\014newNamespace" +
      "\022<.dataclay.communication.grpc.logicmodu" +
      "le.NewNamespaceRequest\032=.dataclay.commun" +
      "ication.grpc.logicmodule.NewNamespaceRes" +
      "ponse\"\000\022\207\001\n\017removeNamespace\022?.dataclay.c" +
      "ommunication.grpc.logicmodule.RemoveName" +
      "spaceRequest\0321.dataclay.communication.gr" +
      "pc.common.ExceptionInfo\"\000\022\223\001\n\016getNamespa" +
      "ceID\022>.dataclay.communication.grpc.logic" +
      "module.GetNamespaceIDRequest\032?.dataclay." +
      "communication.grpc.logicmodule.GetNamesp" +
      "aceIDResponse\"\000\022\231\001\n\020getNamespaceLang\022@.d" +
      "ataclay.communication.grpc.logicmodule.G" +
      "etNamespaceLangRequest\032A.dataclay.commun" +
      "ication.grpc.logicmodule.GetNamespaceLan" +
      "gResponse\"\000\022\237\001\n\022getObjectDataSetID\022B.dat" +
      "aclay.communication.grpc.logicmodule.Get" +
      "ObjectDataSetIDRequest\032C.dataclay.commun" +
      "ication.grpc.logicmodule.GetObjectDataSe" +
      "tIDResponse\"\000\022\207\001\n\017importInterface\022?.data" +
      "clay.communication.grpc.logicmodule.Impo" +
      "rtInterfaceRequest\0321.dataclay.communicat" +
      "ion.grpc.common.ExceptionInfo\"\000\022\205\001\n\016impo" +
      "rtContract\022>.dataclay.communication.grpc" +
      ".logicmodule.ImportContractRequest\0321.dat" +
      "aclay.communication.grpc.common.Exceptio" +
      "nInfo\"\000\022\272\001\n\033getInfoOfClassesInNamespace\022" +
      "K.dataclay.communication.grpc.logicmodul" +
      "e.GetInfoOfClassesInNamespaceRequest\032L.d" +
      "ataclay.communication.grpc.logicmodule.G" +
      "etInfoOfClassesInNamespaceResponse\"\000\022\314\001\n" +
      "!getImportedClassesInfoInNamespace\022Q.dat" +
      "aclay.communication.grpc.logicmodule.Get" +
      "ImportedClassesInfoInNamespaceRequest\032R." +
      "dataclay.communication.grpc.logicmodule." +
      "GetImportedClassesInfoInNamespaceRespons" +
      "e\"\000\022\245\001\n\024getClassIDfromImport\022D.dataclay." +
      "communication.grpc.logicmodule.GetClassI" +
      "DFromImportRequest\032E.dataclay.communicat" +
      "ion.grpc.logicmodule.GetClassIDFromImpor" +
      "tResponse\"\000\022\220\001\n\rgetNamespaces\022=.dataclay" +
      ".communication.grpc.logicmodule.GetNames" +
      "pacesRequest\032>.dataclay.communication.gr" +
      "pc.logicmodule.GetNamespacesResponse\"\000\022\207" +
      "\001\n\nnewDataSet\022:.dataclay.communication.g" +
      "rpc.logicmodule.NewDataSetRequest\032;.data" +
      "clay.communication.grpc.logicmodule.NewD" +
      "ataSetResponse\"\000\022\203\001\n\rremoveDataSet\022=.dat" +
      "aclay.communication.grpc.logicmodule.Rem" +
      "oveDataSetRequest\0321.dataclay.communicati" +
      "on.grpc.common.ExceptionInfo\"\000\022\215\001\n\014getDa" +
      "taSetID\022<.dataclay.communication.grpc.lo" +
      "gicmodule.GetDataSetIDRequest\032=.dataclay" +
      ".communication.grpc.logicmodule.GetDataS" +
      "etIDResponse\"\000\022\245\001\n\024checkDataSetIsPublic\022" +
      "D.dataclay.communication.grpc.logicmodul" +
      "e.CheckDataSetIsPublicRequest\032E.dataclay" +
      ".communication.grpc.logicmodule.CheckDat" +
      "aSetIsPublicResponse\"\000\022\234\001\n\021getPublicData" +
      "Sets\022A.dataclay.communication.grpc.logic" +
      "module.GetPublicDataSetsRequest\032B.datacl" +
      "ay.communication.grpc.logicmodule.GetPub" +
      "licDataSetsResponse\"\000\022\237\001\n\022getAccountData" +
      "Sets\022B.dataclay.communication.grpc.logic" +
      "module.GetAccountDataSetsRequest\032C.datac" +
      "lay.communication.grpc.logicmodule.GetAc" +
      "countDataSetsResponse\"\000\022\201\001\n\010newClass\0228.d" +
      "ataclay.communication.grpc.logicmodule.N" +
      "ewClassRequest\0329.dataclay.communication." +
      "grpc.logicmodule.NewClassResponse\"\000\022\207\001\n\n" +
      "newClassID\022:.dataclay.communication.grpc" +
      ".logicmodule.NewClassIDRequest\032;.datacla" +
      "y.communication.grpc.logicmodule.NewClas" +
      "sIDResponse\"\000\022\177\n\013removeClass\022;.dataclay." +
      "communication.grpc.logicmodule.RemoveCla" +
      "ssRequest\0321.dataclay.communication.grpc." +
      "common.ExceptionInfo\"\000\022\207\001\n\017removeOperati" +
      "on\022?.dataclay.communication.grpc.logicmo" +
      "dule.RemoveOperationRequest\0321.dataclay.c" +
      "ommunication.grpc.common.ExceptionInfo\"\000" +
      "\022\221\001\n\024removeImplementation\022D.dataclay.com" +
      "munication.grpc.logicmodule.RemoveImplem" +
      "entationRequest\0321.dataclay.communication" +
      ".grpc.common.ExceptionInfo\"\000\022\223\001\n\016getOper" +
      "ationID\022>.dataclay.communication.grpc.lo" +
      "gicmodule.GetOperationIDRequest\032?.datacl" +
      "ay.communication.grpc.logicmodule.GetOpe" +
      "rationIDResponse\"\000\022\220\001\n\rgetPropertyID\022=.d" +
      "ataclay.communication.grpc.logicmodule.G" +
      "etPropertyIDRequest\032>.dataclay.communica" +
      "tion.grpc.logicmodule.GetPropertyIDRespo" +
      "nse\"\000\022\207\001\n\ngetClassID\022:.dataclay.communic" +
      "ation.grpc.logicmodule.GetClassIDRequest" +
      "\032;.dataclay.communication.grpc.logicmodu" +
      "le.GetClassIDResponse\"\000\022\215\001\n\014getClassInfo" +
      "\022<.dataclay.communication.grpc.logicmodu" +
      "le.GetClassInfoRequest\032=.dataclay.commun" +
      "ication.grpc.logicmodule.GetClassInfoRes" +
      "ponse\"\000\022\212\001\n\013newContract\022;.dataclay.commu" +
      "nication.grpc.logicmodule.NewContractReq" +
      "uest\032<.dataclay.communication.grpc.logic" +
      "module.NewContractResponse\"\000\022\231\001\n\030registe" +
      "rToPublicContract\022H.dataclay.communicati" +
      "on.grpc.logicmodule.RegisterToPublicCont" +
      "ractRequest\0321.dataclay.communication.grp" +
      "c.common.ExceptionInfo\"\000\022\322\001\n#registerToP" +
      "ublicContractOfNamespace\022S.dataclay.comm" +
      "unication.grpc.logicmodule.RegisterToPub" +
      "licContractOfNamespaceRequest\032T.dataclay" +
      ".communication.grpc.logicmodule.Register" +
      "ToPublicContractOfNamespaceResponse\"\000\022\264\001" +
      "\n\031getContractIDsOfApplicant\022I.dataclay.c" +
      "ommunication.grpc.logicmodule.GetContrac" +
      "tIDsOfApplicantRequest\032J.dataclay.commun" +
      "ication.grpc.logicmodule.GetContractIDsO" +
      "fApplicantResponse\"\000\022\261\001\n\030getContractIDsO" +
      "fProvider\022H.dataclay.communication.grpc." +
      "logicmodule.GetContractIDsOfProviderRequ" +
      "est\032I.dataclay.communication.grpc.logicm" +
      "odule.GetContractIDsOfProviderResponse\"\000" +
      "\022\314\001\n%getContractIDsOfApplicantWithProvid" +
      "er\022O.dataclay.communication.grpc.logicmo" +
      "dule.GetContractsOfApplicantWithProvRequ" +
      "est\032P.dataclay.communication.grpc.logicm" +
      "odule.GetContractsOfApplicantWithProvRes" +
      "ponse\"\000\022\226\001\n\017newDataContract\022?.dataclay.c" +
      "ommunication.grpc.logicmodule.NewDataCon" +
      "tractRequest\032@.dataclay.communication.gr" +
      "pc.logicmodule.NewDataContractResponse\"\000" +
      "\022\241\001\n\034registerToPublicDataContract\022L.data" +
      "clay.communication.grpc.logicmodule.Regi" +
      "sterToPublicDataContractRequest\0321.datacl" +
      "ay.communication.grpc.common.ExceptionIn" +
      "fo\"\000\022\300\001\n\035getDataContractIDsOfApplicant\022M" +
      ".dataclay.communication.grpc.logicmodule" +
      ".GetDataContractIDsOfApplicantRequest\032N." +
      "dataclay.communication.grpc.logicmodule." +
      "GetDataContractIDsOfApplicantResponse\"\000\022" +
      "\275\001\n\034getDataContractIDsOfProvider\022L.datac" +
      "lay.communication.grpc.logicmodule.GetDa" +
      "taContractIDsOfProviderRequest\032M.datacla" +
      "y.communication.grpc.logicmodule.GetData" +
      "ContractIDsOfProviderResponse\"\000\022\337\001\n*getD" +
      "ataContractInfoOfApplicantWithProvider\022V" +
      ".dataclay.communication.grpc.logicmodule" +
      ".GetDataContractInfoOfApplicantWithProvR" +
      "equest\032W.dataclay.communication.grpc.log" +
      "icmodule.GetDataContractInfoOfApplicantW" +
      "ithProvResponse\"\000\022\215\001\n\014newInterface\022<.dat" +
      "aclay.communication.grpc.logicmodule.New" +
      "InterfaceRequest\032=.dataclay.communicatio" +
      "n.grpc.logicmodule.NewInterfaceResponse\"" +
      "\000\022\231\001\n\020getInterfaceInfo\022@.dataclay.commun" +
      "ication.grpc.logicmodule.GetInterfaceInf" +
      "oRequest\032A.dataclay.communication.grpc.l" +
      "ogicmodule.GetInterfaceInfoResponse\"\000\022\207\001" +
      "\n\017removeInterface\022?.dataclay.communicati" +
      "on.grpc.logicmodule.RemoveInterfaceReque" +
      "st\0321.dataclay.communication.grpc.common." +
      "ExceptionInfo\"\000\022\306\001\n\037getAllExecutionEnvir" +
      "onmentsInfo\022O.dataclay.communication.grp" +
      "c.logicmodule.GetAllExecutionEnvironment" +
      "sInfoRequest\032P.dataclay.communication.gr" +
      "pc.logicmodule.GetAllExecutionEnvironmen" +
      "tsInfoResponse\"\000\022\245\001\n\024getStorageLocationI" +
      "D\022D.dataclay.communication.grpc.logicmod" +
      "ule.GetStorageLocationIDRequest\032E.datacl" +
      "ay.communication.grpc.logicmodule.GetSto" +
      "rageLocationIDResponse\"\000\022\253\001\n\026getStorageL" +
      "ocationInfo\022F.dataclay.communication.grp" +
      "c.logicmodule.GetStorageLocationInfoRequ" +
      "est\032G.dataclay.communication.grpc.logicm" +
      "odule.GetStorageLocationInfoResponse\"\000\022\265" +
      "\001\n\033getExecutionEnvironmentInfo\022F.datacla" +
      "y.communication.grpc.logicmodule.GetStor" +
      "ageLocationInfoRequest\032L.dataclay.commun" +
      "ication.grpc.logicmodule.GetExecutionEnv" +
      "ironmentInfoResponse\"\000\022\220\001\n\rgetObjectInfo" +
      "\022=.dataclay.communication.grpc.logicmodu" +
      "le.GetObjectInfoRequest\032>.dataclay.commu" +
      "nication.grpc.logicmodule.GetObjectInfoR" +
      "esponse\"\000\022\237\001\n\022getObjectFromAlias\022B.datac" +
      "lay.communication.grpc.logicmodule.GetOb" +
      "jectFromAliasRequest\032C.dataclay.communic" +
      "ation.grpc.logicmodule.GetObjectFromAlia" +
      "sResponse\"\000\022\177\n\013deleteAlias\022;.dataclay.co" +
      "mmunication.grpc.logicmodule.DeleteAlias" +
      "Request\0321.dataclay.communication.grpc.co" +
      "mmon.ExceptionInfo\"\000\022\317\001\n\"getObjectsMetaD" +
      "ataInfoOfClassForNM\022R.dataclay.communica" +
      "tion.grpc.logicmodule.GetObjectsMetaData" +
      "InfoOfClassForNMRequest\032S.dataclay.commu" +
      "nication.grpc.logicmodule.GetObjectsMeta" +
      "DataInfoOfClassForNMResponse\"\000\022y\n\010addAli" +
      "as\0228.dataclay.communication.grpc.logicmo" +
      "dule.AddAliasRequest\0321.dataclay.communic" +
      "ation.grpc.common.ExceptionInfo\"\000\022\220\001\n\024re" +
      "gisterObjectFromGC\022C.dataclay.communicat" +
      "ion.grpc.logicmodule.RegisterObjectForGC" +
      "Request\0321.dataclay.communication.grpc.co" +
      "mmon.ExceptionInfo\"\000\022\213\001\n\021unregisterObjec" +
      "ts\022A.dataclay.communication.grpc.logicmo" +
      "dule.UnregisterObjectsRequest\0321.dataclay" +
      ".communication.grpc.common.ExceptionInfo" +
      "\"\000\022\223\001\n\016registerObject\022>.dataclay.communi" +
      "cation.grpc.logicmodule.RegisterObjectRe" +
      "quest\032?.dataclay.communication.grpc.logi" +
      "cmodule.RegisterObjectResponse\"\000\022\251\001\n set" +
      "DataSetIDFromGarbageCollector\022P.dataclay" +
      ".communication.grpc.logicmodule.SetDataS" +
      "etIDFromGarbageCollectorRequest\0321.datacl" +
      "ay.communication.grpc.common.ExceptionIn" +
      "fo\"\000\022\201\001\n\014setDataSetID\022<.dataclay.communi" +
      "cation.grpc.logicmodule.SetDataSetIDRequ" +
      "est\0321.dataclay.communication.grpc.common" +
      ".ExceptionInfo\"\000\022\207\001\n\nnewVersion\022:.datacl" +
      "ay.communication.grpc.logicmodule.NewVer" +
      "sionRequest\032;.dataclay.communication.grp" +
      "c.logicmodule.NewVersionResponse\"\000\022\215\001\n\022c" +
      "onsolidateVersion\022B.dataclay.communicati" +
      "on.grpc.logicmodule.ConsolidateVersionRe" +
      "quest\0321.dataclay.communication.grpc.comm" +
      "on.ExceptionInfo\"\000\022\207\001\n\nnewReplica\022:.data" +
      "clay.communication.grpc.logicmodule.NewR" +
      "eplicaRequest\032;.dataclay.communication.g" +
      "rpc.logicmodule.NewReplicaResponse\"\000\022\207\001\n" +
      "\nmoveObject\022:.dataclay.communication.grp" +
      "c.logicmodule.MoveObjectRequest\032;.datacl" +
      "ay.communication.grpc.logicmodule.MoveOb" +
      "jectResponse\"\000\022\213\001\n\021setObjectReadOnly\022A.d" +
      "ataclay.communication.grpc.logicmodule.S" +
      "etObjectReadOnlyRequest\0321.dataclay.commu" +
      "nication.grpc.common.ExceptionInfo\"\000\022\215\001\n" +
      "\022setObjectReadWrite\022B.dataclay.communica" +
      "tion.grpc.logicmodule.SetObjectReadWrite" +
      "Request\0321.dataclay.communication.grpc.co" +
      "mmon.ExceptionInfo\"\000\022\231\001\n\020getMetadataByOI" +
      "D\022@.dataclay.communication.grpc.logicmod" +
      "ule.GetMetadataByOIDRequest\032A.dataclay.c" +
      "ommunication.grpc.logicmodule.GetMetadat" +
      "aByOIDResponse\"\000\022\250\001\n\025executeImplementati" +
      "on\022E.dataclay.communication.grpc.logicmo" +
      "dule.ExecuteImplementationRequest\032F.data" +
      "clay.communication.grpc.logicmodule.Exec" +
      "uteImplementationResponse\"\000\022\250\001\n\025executeM" +
      "ethodOnTarget\022E.dataclay.communication.g" +
      "rpc.logicmodule.ExecuteMethodOnTargetReq" +
      "uest\032F.dataclay.communication.grpc.logic" +
      "module.ExecuteMethodOnTargetResponse\"\000\022\235" +
      "\001\n\032synchronizeFederatedObject\022J.dataclay" +
      ".communication.grpc.logicmodule.Synchron" +
      "izeFederatedObjectRequest\0321.dataclay.com" +
      "munication.grpc.common.ExceptionInfo\"\000\022\203" +
      "\001\n\rgetDataClayID\0220.dataclay.communicatio" +
      "n.grpc.common.EmptyMessage\032>.dataclay.co" +
      "mmunication.grpc.logicmodule.GetDataClay" +
      "IDResponse\"\000\022\261\001\n\030registerExternalDataCla" +
      "y\022H.dataclay.communication.grpc.logicmod" +
      "ule.RegisterExternalDataClayRequest\032I.da" +
      "taclay.communication.grpc.logicmodule.Re" +
      "gisterExternalDataClayResponse\"\000\022\323\001\n)reg" +
      "isterExternalDataClayOverrideAuthority\022Y" +
      ".dataclay.communication.grpc.logicmodule" +
      ".RegisterExternalDataClayOverrideAuthori" +
      "tyRequest\032I.dataclay.communication.grpc." +
      "logicmodule.RegisterExternalDataClayResp" +
      "onse\"\000\022\325\001\n$notifyRegistrationOfExternalD" +
      "ataClay\022T.dataclay.communication.grpc.lo" +
      "gicmodule.NotifyRegistrationOfExternalDa" +
      "taClayRequest\032U.dataclay.communication.g" +
      "rpc.logicmodule.NotifyRegistrationOfExte" +
      "rnalDataClayResponse\"\000\022\244\001\n\027getExternalDa" +
      "taClayInfo\022B.dataclay.communication.grpc" +
      ".logicmodule.GetExtDataClayInfoRequest\032C" +
      ".dataclay.communication.grpc.logicmodule" +
      ".GetExtDataClayInfoResponse\"\000\022\250\001\n\025getExt" +
      "ernalDataclayId\022E.dataclay.communication" +
      ".grpc.logicmodule.GetExternalDataclayIDR" +
      "equest\032F.dataclay.communication.grpc.log" +
      "icmodule.GetExternalDataclayIDResponse\"\000" +
      "\022\205\001\n\016federateObject\022>.dataclay.communica" +
      "tion.grpc.logicmodule.FederateObjectRequ" +
      "est\0321.dataclay.communication.grpc.common" +
      ".ExceptionInfo\"\000\022\211\001\n\020unfederateObject\022@." +
      "dataclay.communication.grpc.logicmodule." +
      "UnfederateObjectRequest\0321.dataclay.commu" +
      "nication.grpc.common.ExceptionInfo\"\000\022\231\001\n" +
      "\030notifyUnfederatedObjects\022H.dataclay.com" +
      "munication.grpc.logicmodule.NotifyUnfede" +
      "ratedObjectsRequest\0321.dataclay.communica" +
      "tion.grpc.common.ExceptionInfo\"\000\022\225\001\n\026not" +
      "ifyFederatedObjects\022F.dataclay.communica" +
      "tion.grpc.logicmodule.NotifyFederatedObj" +
      "ectsRequest\0321.dataclay.communication.grp" +
      "c.common.ExceptionInfo\"\000\022\343\001\n*checkObject" +
      "IsFederatedWithDataClayInstance\022X.datacl" +
      "ay.communication.grpc.logicmodule.CheckO" +
      "bjectFederatedWithDataClayInstanceReques" +
      "t\032Y.dataclay.communication.grpc.logicmod" +
      "ule.CheckObjectFederatedWithDataClayInst" +
      "anceResponse\"\000\022\314\001\n!getDataClaysObjectIsF" +
      "ederatedWith\022Q.dataclay.communication.gr" +
      "pc.logicmodule.GetDataClaysObjectIsFeder" +
      "atedWithRequest\032R.dataclay.communication" +
      ".grpc.logicmodule.GetDataClaysObjectIsFe" +
      "deratedWithResponse\"\000\022\314\001\n!getExternalSou" +
      "rceDataClayOfObject\022Q.dataclay.communica" +
      "tion.grpc.logicmodule.GetExternalSourceD" +
      "ataClayOfObjectRequest\032R.dataclay.commun" +
      "ication.grpc.logicmodule.GetExternalSour" +
      "ceDataClayOfObjectResponse\"\000\022\221\001\n\024unfeder" +
      "ateAllObjects\022D.dataclay.communication.g" +
      "rpc.logicmodule.UnfederateAllObjectsRequ" +
      "est\0321.dataclay.communication.grpc.common" +
      ".ExceptionInfo\"\000\022\245\001\n\036unfederateAllObject" +
      "sWithAllDCs\022N.dataclay.communication.grp" +
      "c.logicmodule.UnfederateAllObjectsWithAl" +
      "lDCsRequest\0321.dataclay.communication.grp" +
      "c.common.ExceptionInfo\"\000\022\235\001\n\032unfederateO" +
      "bjectWithAllDCs\022J.dataclay.communication" +
      ".grpc.logicmodule.UnfederateObjectWithAl" +
      "lDCsRequest\0321.dataclay.communication.grp" +
      "c.common.ExceptionInfo\"\000\022\227\001\n\027migrateFede" +
      "ratedObjects\022G.dataclay.communication.gr" +
      "pc.logicmodule.MigrateFederatedObjectsRe" +
      "quest\0321.dataclay.communication.grpc.comm" +
      "on.ExceptionInfo\"\000\022\215\001\n\022federateAllObject" +
      "s\022B.dataclay.communication.grpc.logicmod",
      "ule.FederateAllObjectsRequest\0321.dataclay" +
      ".communication.grpc.common.ExceptionInfo" +
      "\"\000\022\250\001\n\025getClassesInNamespace\022E.dataclay." +
      "communication.grpc.logicmodule.GetClasse" +
      "sInNamespaceRequest\032F.dataclay.communica" +
      "tion.grpc.logicmodule.GetClassesInNamesp" +
      "aceResponse\"\000\022\251\001\n importModelsFromExtern" +
      "alDataClay\022P.dataclay.communication.grpc" +
      ".logicmodule.ImportModelsFromExternalDat" +
      "aClayRequest\0321.dataclay.communication.gr" +
      "pc.common.ExceptionInfo\"\000\022\201\001\n\010getStubs\0228" +
      ".dataclay.communication.grpc.logicmodule" +
      ".GetStubsRequest\0329.dataclay.communicatio" +
      "n.grpc.logicmodule.GetStubsResponse\"\000\022\220\001" +
      "\n\rgetBabelStubs\022=.dataclay.communication" +
      ".grpc.logicmodule.GetBabelStubsRequest\032>" +
      ".dataclay.communication.grpc.logicmodule" +
      ".GetBabelStubsResponse\"\000\022\177\n\013registerECA\022" +
      ";.dataclay.communication.grpc.logicmodul" +
      "e.RegisterECARequest\0321.dataclay.communic" +
      "ation.grpc.common.ExceptionInfo\"\000\022\177\n\013adv" +
      "iseEvent\022;.dataclay.communication.grpc.l" +
      "ogicmodule.AdviseEventRequest\0321.dataclay" +
      ".communication.grpc.common.ExceptionInfo" +
      "\"\000\022\221\001\n\024isPrefetchingEnabled\0220.dataclay.c" +
      "ommunication.grpc.common.EmptyMessage\032E." +
      "dataclay.communication.grpc.logicmodule." +
      "IsPrefetchingEnabledResponse\"\000\022\234\001\n\021getCl" +
      "assNameForDS\022A.dataclay.communication.gr" +
      "pc.logicmodule.GetClassNameForDSRequest\032" +
      "B.dataclay.communication.grpc.logicmodul" +
      "e.GetClassNameForDSResponse\"\000\022\300\001\n\035getCla" +
      "ssNameAndNamespaceForDS\022M.dataclay.commu" +
      "nication.grpc.logicmodule.GetClassNameAn" +
      "dNamespaceForDSRequest\032N.dataclay.commun" +
      "ication.grpc.logicmodule.GetClassNameAnd" +
      "NamespaceForDSResponse\"\000\022\306\001\n\037getContract" +
      "IDOfDataClayProvider\022O.dataclay.communic" +
      "ation.grpc.logicmodule.GetContractIDOfDa" +
      "taClayProviderRequest\032P.dataclay.communi" +
      "cation.grpc.logicmodule.GetContractIDOfD" +
      "ataClayProviderResponse\"\000\022\253\001\n\026objectExis" +
      "tsInDataClay\022F.dataclay.communication.gr" +
      "pc.logicmodule.ObjectExistsInDataClayReq" +
      "uest\032G.dataclay.communication.grpc.logic" +
      "module.ObjectExistsInDataClayResponse\"\000\022" +
      "\201\001\n\014closeSession\022<.dataclay.communicatio" +
      "n.grpc.logicmodule.CloseSessionRequest\0321" +
      ".dataclay.communication.grpc.common.Exce" +
      "ptionInfo\"\000\022\250\001\n\025getMetadataByOIDForDS\022E." +
      "dataclay.communication.grpc.logicmodule." +
      "GetMetadataByOIDForDSRequest\032F.dataclay." +
      "communication.grpc.logicmodule.GetMetada" +
      "taByOIDForDSResponse\"\000\022\207\001\n\017activateTraci" +
      "ng\022?.dataclay.communication.grpc.logicmo" +
      "dule.ActivateTracingRequest\0321.dataclay.c" +
      "ommunication.grpc.common.ExceptionInfo\"\000" +
      "\022z\n\021deactivateTracing\0220.dataclay.communi" +
      "cation.grpc.common.EmptyMessage\0321.datacl" +
      "ay.communication.grpc.common.ExceptionIn" +
      "fo\"\000\022v\n\tgetTraces\0220.dataclay.communicati" +
      "on.grpc.common.EmptyMessage\0325.dataclay.c" +
      "ommunication.grpc.common.GetTracesRespon" +
      "se\"\000\022|\n\023cleanMetaDataCaches\0220.dataclay.c" +
      "ommunication.grpc.common.EmptyMessage\0321." +
      "dataclay.communication.grpc.common.Excep" +
      "tionInfo\"\000\022w\n\016closeManagerDb\0220.dataclay." +
      "communication.grpc.common.EmptyMessage\0321" +
      ".dataclay.communication.grpc.common.Exce" +
      "ptionInfo\"\000\022p\n\007closeDb\0220.dataclay.commun" +
      "ication.grpc.common.EmptyMessage\0321.datac" +
      "lay.communication.grpc.common.ExceptionI" +
      "nfo\"\000BR\n8es.bsc.dataclay.communication.g" +
      "rpc.generated.logicmoduleB\026LogicModuleGr" +
      "pcServiceb\006proto3"
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
