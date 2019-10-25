// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: dataclay/communication/grpc/generated/dataservice/dataservice.proto

package es.bsc.dataclay.communication.grpc.generated.dataservice;

@SuppressWarnings("all")
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
      "messages.proto2\362/\n\013DataService\022\203\001\n\rinitB" +
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
      ".GetObjectsResponse\"\000\022\177\n\013newMetaData\022;.d" +
      "ataclay.communication.grpc.dataservice.N" +
      "ewMetaDataRequest\0321.dataclay.communicati" +
      "on.grpc.common.ExceptionInfo\"\000\022\207\001\n\nnewVe" +
      "rsion\022:.dataclay.communication.grpc.data" +
      "service.NewVersionRequest\032;.dataclay.com" +
      "munication.grpc.dataservice.NewVersionRe" +
      "sponse\"\000\022\215\001\n\022consolidateVersion\022B.datacl" +
      "ay.communication.grpc.dataservice.Consol" +
      "idateVersionRequest\0321.dataclay.communica" +
      "tion.grpc.common.ExceptionInfo\"\000\022\203\001\n\rups" +
      "ertObjects\022=.dataclay.communication.grpc" +
      ".dataservice.UpsertObjectsRequest\0321.data" +
      "clay.communication.grpc.common.Exception" +
      "Info\"\000\022\207\001\n\nnewReplica\022:.dataclay.communi" +
      "cation.grpc.dataservice.NewReplicaReques" +
      "t\032;.dataclay.communication.grpc.dataserv" +
      "ice.NewReplicaResponse\"\000\022\212\001\n\013moveObjects" +
      "\022;.dataclay.communication.grpc.dataservi" +
      "ce.MoveObjectsRequest\032<.dataclay.communi" +
      "cation.grpc.dataservice.MoveObjectsRespo" +
      "nse\"\000\022\220\001\n\rremoveObjects\022=.dataclay.commu" +
      "nication.grpc.dataservice.RemoveObjectsR" +
      "equest\032>.dataclay.communication.grpc.dat" +
      "aservice.RemoveObjectsResponse\"\000\022\235\001\n\030mig" +
      "rateObjectsToBackends\022>.dataclay.communi" +
      "cation.grpc.dataservice.MigrateObjectsRe" +
      "quest\032?.dataclay.communication.grpc.data" +
      "service.MigrateObjectsResponse\"\000\022\275\001\n\034get" +
      "ClassIDFromObjectInMemory\022L.dataclay.com" +
      "munication.grpc.dataservice.GetClassIDFr" +
      "omObjectInMemoryRequest\032M.dataclay.commu" +
      "nication.grpc.dataservice.GetClassIDFrom" +
      "ObjectInMemoryResponse\"\000\022\250\001\n\025executeImpl" +
      "ementation\022E.dataclay.communication.grpc" +
      ".dataservice.ExecuteImplementationReques" +
      "t\032F.dataclay.communication.grpc.dataserv" +
      "ice.ExecuteImplementationResponse\"\000\022\205\001\n\016" +
      "makePersistent\022>.dataclay.communication." +
      "grpc.dataservice.MakePersistentRequest\0321" +
      ".dataclay.communication.grpc.common.Exce" +
      "ptionInfo\"\000\022y\n\010federate\0228.dataclay.commu" +
      "nication.grpc.dataservice.FederateReques" +
      "t\0321.dataclay.communication.grpc.common.E" +
      "xceptionInfo\"\000\022}\n\nunfederate\022:.dataclay." +
      "communication.grpc.dataservice.Unfederat" +
      "eRequest\0321.dataclay.communication.grpc.c" +
      "ommon.ExceptionInfo\"\000\022{\n\006exists\0226.datacl" +
      "ay.communication.grpc.dataservice.Exists" +
      "Request\0327.dataclay.communication.grpc.da" +
      "taservice.ExistsResponse\"\000\022\242\001\n\023getFedera" +
      "tedObjects\022C.dataclay.communication.grpc" +
      ".dataservice.GetFederatedObjectsRequest\032" +
      "D.dataclay.communication.grpc.dataservic" +
      "e.GetFederatedObjectsResponse\"\000\022\254\001\n\027getR" +
      "eferencedObjectsIDs\022F.dataclay.communica" +
      "tion.grpc.dataservice.GetReferencedObjec" +
      "tIDsRequest\032G.dataclay.communication.grp" +
      "c.dataservice.GetReferencedObjectIDsResp" +
      "onse\"\000\022\215\001\n\014filterObject\022<.dataclay.commu" +
      "nication.grpc.dataservice.FilterObjectRe" +
      "quest\032=.dataclay.communication.grpc.data" +
      "service.FilterObjectResponse\"\000\022{\n\tstoreT" +
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
      "c.common.ExceptionInfo\"\000\022\207\001\n\nexistsInDB\022" +
      ":.dataclay.communication.grpc.dataservic" +
      "e.ExistsInDBRequest\032;.dataclay.communica" +
      "tion.grpc.dataservice.ExistsInDBResponse" +
      "\"\000\022\205\001\n\034cleanExecutionClassDirectory\0220.da" +
      "taclay.communication.grpc.common.EmptyMe" +
      "ssage\0321.dataclay.communication.grpc.comm" +
      "on.ExceptionInfo\"\000\022w\n\016closeDbHandler\0220.d" +
      "ataclay.communication.grpc.common.EmptyM" +
      "essage\0321.dataclay.communication.grpc.com" +
      "mon.ExceptionInfo\"\000\022q\n\010shutDown\0220.datacl" +
      "ay.communication.grpc.common.EmptyMessag" +
      "e\0321.dataclay.communication.grpc.common.E" +
      "xceptionInfo\"\000\022}\n\024disconnectFromOthers\0220" +
      ".dataclay.communication.grpc.common.Empt" +
      "yMessage\0321.dataclay.communication.grpc.c" +
      "ommon.ExceptionInfo\"\000\022\177\n\026registerPending" +
      "Objects\0220.dataclay.communication.grpc.co" +
      "mmon.EmptyMessage\0321.dataclay.communicati" +
      "on.grpc.common.ExceptionInfo\"\000\022t\n\013cleanC" +
      "aches\0220.dataclay.communication.grpc.comm" +
      "on.EmptyMessage\0321.dataclay.communication" +
      ".grpc.common.ExceptionInfo\"\000\022\207\001\n\017activat" +
      "eTracing\022?.dataclay.communication.grpc.d" +
      "ataservice.ActivateTracingRequest\0321.data" +
      "clay.communication.grpc.common.Exception" +
      "Info\"\000\022z\n\021deactivateTracing\0220.dataclay.c" +
      "ommunication.grpc.common.EmptyMessage\0321." +
      "dataclay.communication.grpc.common.Excep" +
      "tionInfo\"\000\022v\n\tgetTraces\0220.dataclay.commu" +
      "nication.grpc.common.EmptyMessage\0325.data" +
      "clay.communication.grpc.common.GetTraces" +
      "Response\"\000\022\211\001\n\020closeSessionInDS\022@.datacl" +
      "ay.communication.grpc.dataservice.CloseS" +
      "essionInDSRequest\0321.dataclay.communicati" +
      "on.grpc.common.ExceptionInfo\"\000\022}\n\nupdate" +
      "Refs\022:.dataclay.communication.grpc.datas" +
      "ervice.UpdateRefsRequest\0321.dataclay.comm" +
      "unication.grpc.common.ExceptionInfo\"\000\022\223\001" +
      "\n\025getRetainedReferences\0220.dataclay.commu" +
      "nication.grpc.common.EmptyMessage\032F.data" +
      "clay.communication.grpc.dataservice.GetR" +
      "etainedReferencesResponse\"\000BK\n1dataclay." +
      "communication.grpc.generated.dataservice" +
      "B\026DataServiceGrpcServiceb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.getDescriptor(),
          es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.getDescriptor(),
        }, assigner);
    es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.getDescriptor();
    es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
