// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: dataclay/communication/grpc/generated/dataservice/dataservice.proto

package es.bsc.dataclay.communication.grpc.generated.dataservice;

public final class DataServiceGrpcService {
  private DataServiceGrpcService() {}
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
      "dataservice/dataservice.proto\022\'dataclay." +
      "communication.grpc.dataservice\032Kdataclay" +
      "/communication/grpc/messages/dataservice" +
      "/dataservice_messages.proto\032Adataclay/co" +
      "mmunication/grpc/messages/common/common_" +
      "messages.proto2\3353\n\013DataService\022\203\001\n\rinitB" +
      "ackendID\022=.dataclay.communication.grpc.d" +
      "ataservice.InitBackendIDRequest\0321.datacl" +
      "ay.communication.grpc.common.ExceptionIn" +
      "fo\"\000\022\243\001\n\035associateExecutionEnvironment\022M" +
      ".dataclay.communication.grpc.dataservice" +
      ".AssociateExecutionEnvironmentRequest\0321." +
      "dataclay.communication.grpc.common.Excep" +
      "tionInfo\"\000\022\213\001\n\021deployMetaClasses\022A.datac" +
      "lay.communication.grpc.dataservice.Deplo" +
      "yMetaClassesRequest\0321.dataclay.communica" +
      "tion.grpc.common.ExceptionInfo\"\000\022\203\001\n\rdep" +
      "loyClasses\022=.dataclay.communication.grpc" +
      ".dataservice.DeployClassesRequest\0321.data" +
      "clay.communication.grpc.common.Exception" +
      "Info\"\000\022\177\n\013enrichClass\022;.dataclay.communi" +
      "cation.grpc.dataservice.EnrichClassReque" +
      "st\0321.dataclay.communication.grpc.common." +
      "ExceptionInfo\"\000\022\250\001\n\025newPersistentInstanc" +
      "e\022E.dataclay.communication.grpc.dataserv" +
      "ice.NewPersistentInstanceRequest\032F.datac" +
      "lay.communication.grpc.dataservice.NewPe" +
      "rsistentInstanceResponse\"\000\022\201\001\n\014storeObje" +
      "cts\022<.dataclay.communication.grpc.datase" +
      "rvice.StoreObjectsRequest\0321.dataclay.com" +
      "munication.grpc.common.ExceptionInfo\"\000\022\226" +
      "\001\n\017getCopyOfObject\022?.dataclay.communicat" +
      "ion.grpc.dataservice.GetCopyOfObjectRequ" +
      "est\032@.dataclay.communication.grpc.datase" +
      "rvice.GetCopyOfObjectResponse\"\000\022\201\001\n\014upda" +
      "teObject\022<.dataclay.communication.grpc.d" +
      "ataservice.UpdateObjectRequest\0321.datacla" +
      "y.communication.grpc.common.ExceptionInf" +
      "o\"\000\022\207\001\n\ngetObjects\022:.dataclay.communicat" +
      "ion.grpc.dataservice.GetObjectsRequest\032;" +
      ".dataclay.communication.grpc.dataservice" +
      ".GetObjectsResponse\"\000\022\207\001\n\nnewVersion\022:.d" +
      "ataclay.communication.grpc.dataservice.N" +
      "ewVersionRequest\032;.dataclay.communicatio" +
      "n.grpc.dataservice.NewVersionResponse\"\000\022" +
      "\215\001\n\022consolidateVersion\022B.dataclay.commun" +
      "ication.grpc.dataservice.ConsolidateVers" +
      "ionRequest\0321.dataclay.communication.grpc" +
      ".common.ExceptionInfo\"\000\022\203\001\n\rupsertObject" +
      "s\022=.dataclay.communication.grpc.dataserv" +
      "ice.UpsertObjectsRequest\0321.dataclay.comm" +
      "unication.grpc.common.ExceptionInfo\"\000\022\207\001" +
      "\n\nnewReplica\022:.dataclay.communication.gr" +
      "pc.dataservice.NewReplicaRequest\032;.datac" +
      "lay.communication.grpc.dataservice.NewRe" +
      "plicaResponse\"\000\022\212\001\n\013moveObjects\022;.datacl" +
      "ay.communication.grpc.dataservice.MoveOb" +
      "jectsRequest\032<.dataclay.communication.gr" +
      "pc.dataservice.MoveObjectsResponse\"\000\022\220\001\n" +
      "\rremoveObjects\022=.dataclay.communication." +
      "grpc.dataservice.RemoveObjectsRequest\032>." +
      "dataclay.communication.grpc.dataservice." +
      "RemoveObjectsResponse\"\000\022\235\001\n\030migrateObjec" +
      "tsToBackends\022>.dataclay.communication.gr" +
      "pc.dataservice.MigrateObjectsRequest\032?.d" +
      "ataclay.communication.grpc.dataservice.M" +
      "igrateObjectsResponse\"\000\022\275\001\n\034getClassIDFr" +
      "omObjectInMemory\022L.dataclay.communicatio" +
      "n.grpc.dataservice.GetClassIDFromObjectI" +
      "nMemoryRequest\032M.dataclay.communication." +
      "grpc.dataservice.GetClassIDFromObjectInM" +
      "emoryResponse\"\000\022\250\001\n\025executeImplementatio" +
      "n\022E.dataclay.communication.grpc.dataserv" +
      "ice.ExecuteImplementationRequest\032F.datac" +
      "lay.communication.grpc.dataservice.Execu" +
      "teImplementationResponse\"\000\022\205\001\n\016makePersi" +
      "stent\022>.dataclay.communication.grpc.data" +
      "service.MakePersistentRequest\0321.dataclay" +
      ".communication.grpc.common.ExceptionInfo" +
      "\"\000\022y\n\010federate\0228.dataclay.communication." +
      "grpc.dataservice.FederateRequest\0321.datac" +
      "lay.communication.grpc.common.ExceptionI" +
      "nfo\"\000\022}\n\nunfederate\022:.dataclay.communica" +
      "tion.grpc.dataservice.UnfederateRequest\032" +
      "1.dataclay.communication.grpc.common.Exc" +
      "eptionInfo\"\000\022\211\001\n\020notifyFederation\022@.data" +
      "clay.communication.grpc.dataservice.Noti" +
      "fyFederationRequest\0321.dataclay.communica" +
      "tion.grpc.common.ExceptionInfo\"\000\022\215\001\n\022not" +
      "ifyUnfederation\022B.dataclay.communication" +
      ".grpc.dataservice.NotifyUnfederationRequ" +
      "est\0321.dataclay.communication.grpc.common" +
      ".ExceptionInfo\"\000\022{\n\006exists\0226.dataclay.co" +
      "mmunication.grpc.dataservice.ExistsReque" +
      "st\0327.dataclay.communication.grpc.dataser" +
      "vice.ExistsResponse\"\000\022\177\n\013synchronize\022;.d" +
      "ataclay.communication.grpc.dataservice.S" +
      "ynchronizeRequest\0321.dataclay.communicati" +
      "on.grpc.common.ExceptionInfo\"\000\022{\n\tstoreT" +
      "oDB\0229.dataclay.communication.grpc.datase" +
      "rvice.StoreToDBRequest\0321.dataclay.commun" +
      "ication.grpc.common.ExceptionInfo\"\000\022\204\001\n\t" +
      "getFromDB\0229.dataclay.communication.grpc." +
      "dataservice.GetFromDBRequest\032:.dataclay." +
      "communication.grpc.dataservice.GetFromDB" +
      "Response\"\000\022}\n\nupdateToDB\022:.dataclay.comm" +
      "unication.grpc.dataservice.UpdateToDBReq" +
      "uest\0321.dataclay.communication.grpc.commo" +
      "n.ExceptionInfo\"\000\022}\n\ndeleteToDB\022:.datacl" +
      "ay.communication.grpc.dataservice.Delete" +
      "ToDBRequest\0321.dataclay.communication.grp" +
      "c.common.ExceptionInfo\"\000\022\207\001\n\017deleteSetFr" +
      "omDB\022?.dataclay.communication.grpc.datas" +
      "ervice.DeleteSetFromDBRequest\0321.dataclay" +
      ".communication.grpc.common.ExceptionInfo" +
      "\"\000\022\207\001\n\nexistsInDB\022:.dataclay.communicati" +
      "on.grpc.dataservice.ExistsInDBRequest\032;." +
      "dataclay.communication.grpc.dataservice." +
      "ExistsInDBResponse\"\000\022\205\001\n\034cleanExecutionC" +
      "lassDirectory\0220.dataclay.communication.g" +
      "rpc.common.EmptyMessage\0321.dataclay.commu" +
      "nication.grpc.common.ExceptionInfo\"\000\022w\n\016" +
      "closeDbHandler\0220.dataclay.communication." +
      "grpc.common.EmptyMessage\0321.dataclay.comm" +
      "unication.grpc.common.ExceptionInfo\"\000\022q\n" +
      "\010shutDown\0220.dataclay.communication.grpc." +
      "common.EmptyMessage\0321.dataclay.communica" +
      "tion.grpc.common.ExceptionInfo\"\000\022}\n\024disc" +
      "onnectFromOthers\0220.dataclay.communicatio" +
      "n.grpc.common.EmptyMessage\0321.dataclay.co" +
      "mmunication.grpc.common.ExceptionInfo\"\000\022" +
      "\177\n\026registerPendingObjects\0220.dataclay.com" +
      "munication.grpc.common.EmptyMessage\0321.da" +
      "taclay.communication.grpc.common.Excepti" +
      "onInfo\"\000\022t\n\013cleanCaches\0220.dataclay.commu" +
      "nication.grpc.common.EmptyMessage\0321.data" +
      "clay.communication.grpc.common.Exception" +
      "Info\"\000\022\207\001\n\017activateTracing\022?.dataclay.co" +
      "mmunication.grpc.dataservice.ActivateTra" +
      "cingRequest\0321.dataclay.communication.grp" +
      "c.common.ExceptionInfo\"\000\022z\n\021deactivateTr" +
      "acing\0220.dataclay.communication.grpc.comm" +
      "on.EmptyMessage\0321.dataclay.communication" +
      ".grpc.common.ExceptionInfo\"\000\022v\n\tgetTrace" +
      "s\0220.dataclay.communication.grpc.common.E" +
      "mptyMessage\0325.dataclay.communication.grp" +
      "c.common.GetTracesResponse\"\000\022\177\n\013deleteAl" +
      "ias\022;.dataclay.communication.grpc.datase" +
      "rvice.DeleteAliasRequest\0321.dataclay.comm" +
      "unication.grpc.common.ExceptionInfo\"\000\022\227\001" +
      "\n\027detachObjectFromSession\022G.dataclay.com" +
      "munication.grpc.dataservice.DetachObject" +
      "FromSessionRequest\0321.dataclay.communicat" +
      "ion.grpc.common.ExceptionInfo\"\000\022\211\001\n\020clos" +
      "eSessionInDS\022@.dataclay.communication.gr" +
      "pc.dataservice.CloseSessionInDSRequest\0321" +
      ".dataclay.communication.grpc.common.Exce" +
      "ptionInfo\"\000\022\223\001\n\025getRetainedReferences\0220." +
      "dataclay.communication.grpc.common.Empty" +
      "Message\032F.dataclay.communication.grpc.da" +
      "taservice.GetRetainedReferencesResponse\"" +
      "\000\022~\n\rgetNumObjects\0220.dataclay.communicat" +
      "ion.grpc.common.EmptyMessage\0329.dataclay." +
      "communication.grpc.common.GetNumObjectsR" +
      "esponse\"\000\022\202\001\n\021getNumObjectsInEE\0220.datacl" +
      "ay.communication.grpc.common.EmptyMessag" +
      "e\0329.dataclay.communication.grpc.common.G" +
      "etNumObjectsResponse\"\000\022\205\001\n\016getObjectGrap" +
      "h\0220.dataclay.communication.grpc.common.E" +
      "mptyMessage\032?.dataclay.communication.grp" +
      "c.dataservice.GetObjectGraphResponse\"\000BR" +
      "\n8es.bsc.dataclay.communication.grpc.gen" +
      "erated.dataserviceB\026DataServiceGrpcServi" +
      "ceb\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.getDescriptor(),
          es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.getDescriptor(),
        });
    es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.getDescriptor();
    es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
