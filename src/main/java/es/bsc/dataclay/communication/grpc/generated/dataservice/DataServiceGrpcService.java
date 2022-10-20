// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: protos/dataservice.proto

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
      "\n\030protos/dataservice.proto\022\022protos.datas" +
      "ervice\032!protos/dataservice_messages.prot" +
      "o\032\034protos/common_messages.proto2\337#\n\013Data" +
      "Service\022Y\n\rinitBackendID\022(.protos.datase" +
      "rvice.InitBackendIDRequest\032\034.protos.comm" +
      "on.ExceptionInfo\"\000\022y\n\035associateExecution" +
      "Environment\0228.protos.dataservice.Associa" +
      "teExecutionEnvironmentRequest\032\034.protos.c" +
      "ommon.ExceptionInfo\"\000\022a\n\021deployMetaClass" +
      "es\022,.protos.dataservice.DeployMetaClasse" +
      "sRequest\032\034.protos.common.ExceptionInfo\"\000" +
      "\022Y\n\rdeployClasses\022(.protos.dataservice.D" +
      "eployClassesRequest\032\034.protos.common.Exce" +
      "ptionInfo\"\000\022U\n\013enrichClass\022&.protos.data" +
      "service.EnrichClassRequest\032\034.protos.comm" +
      "on.ExceptionInfo\"\000\022~\n\025newPersistentInsta" +
      "nce\0220.protos.dataservice.NewPersistentIn" +
      "stanceRequest\0321.protos.dataservice.NewPe" +
      "rsistentInstanceResponse\"\000\022W\n\014storeObjec" +
      "ts\022\'.protos.dataservice.StoreObjectsRequ" +
      "est\032\034.protos.common.ExceptionInfo\"\000\022l\n\017g" +
      "etCopyOfObject\022*.protos.dataservice.GetC" +
      "opyOfObjectRequest\032+.protos.dataservice." +
      "GetCopyOfObjectResponse\"\000\022W\n\014updateObjec" +
      "t\022\'.protos.dataservice.UpdateObjectReque" +
      "st\032\034.protos.common.ExceptionInfo\"\000\022]\n\nge" +
      "tObjects\022%.protos.dataservice.GetObjects" +
      "Request\032&.protos.dataservice.GetObjectsR" +
      "esponse\"\000\022]\n\nnewVersion\022%.protos.dataser" +
      "vice.NewVersionRequest\032&.protos.dataserv" +
      "ice.NewVersionResponse\"\000\022c\n\022consolidateV" +
      "ersion\022-.protos.dataservice.ConsolidateV" +
      "ersionRequest\032\034.protos.common.ExceptionI" +
      "nfo\"\000\022Y\n\rupsertObjects\022(.protos.dataserv" +
      "ice.UpsertObjectsRequest\032\034.protos.common" +
      ".ExceptionInfo\"\000\022]\n\nnewReplica\022%.protos." +
      "dataservice.NewReplicaRequest\032&.protos.d" +
      "ataservice.NewReplicaResponse\"\000\022`\n\013moveO" +
      "bjects\022&.protos.dataservice.MoveObjectsR" +
      "equest\032\'.protos.dataservice.MoveObjectsR" +
      "esponse\"\000\022f\n\rremoveObjects\022(.protos.data" +
      "service.RemoveObjectsRequest\032).protos.da" +
      "taservice.RemoveObjectsResponse\"\000\022s\n\030mig" +
      "rateObjectsToBackends\022).protos.dataservi" +
      "ce.MigrateObjectsRequest\032*.protos.datase" +
      "rvice.MigrateObjectsResponse\"\000\022\223\001\n\034getCl" +
      "assIDFromObjectInMemory\0227.protos.dataser" +
      "vice.GetClassIDFromObjectInMemoryRequest" +
      "\0328.protos.dataservice.GetClassIDFromObje" +
      "ctInMemoryResponse\"\000\022~\n\025executeImplement" +
      "ation\0220.protos.dataservice.ExecuteImplem" +
      "entationRequest\0321.protos.dataservice.Exe" +
      "cuteImplementationResponse\"\000\022[\n\016makePers" +
      "istent\022).protos.dataservice.MakePersiste" +
      "ntRequest\032\034.protos.common.ExceptionInfo\"" +
      "\000\022O\n\010federate\022#.protos.dataservice.Feder" +
      "ateRequest\032\034.protos.common.ExceptionInfo" +
      "\"\000\022S\n\nunfederate\022%.protos.dataservice.Un" +
      "federateRequest\032\034.protos.common.Exceptio" +
      "nInfo\"\000\022_\n\020notifyFederation\022+.protos.dat" +
      "aservice.NotifyFederationRequest\032\034.proto" +
      "s.common.ExceptionInfo\"\000\022c\n\022notifyUnfede" +
      "ration\022-.protos.dataservice.NotifyUnfede" +
      "rationRequest\032\034.protos.common.ExceptionI" +
      "nfo\"\000\022Q\n\006exists\022!.protos.dataservice.Exi" +
      "stsRequest\032\".protos.dataservice.ExistsRe" +
      "sponse\"\000\022U\n\013synchronize\022&.protos.dataser" +
      "vice.SynchronizeRequest\032\034.protos.common." +
      "ExceptionInfo\"\000\022Q\n\tstoreToDB\022$.protos.da" +
      "taservice.StoreToDBRequest\032\034.protos.comm" +
      "on.ExceptionInfo\"\000\022Z\n\tgetFromDB\022$.protos" +
      ".dataservice.GetFromDBRequest\032%.protos.d" +
      "ataservice.GetFromDBResponse\"\000\022S\n\nupdate" +
      "ToDB\022%.protos.dataservice.UpdateToDBRequ" +
      "est\032\034.protos.common.ExceptionInfo\"\000\022S\n\nd" +
      "eleteToDB\022%.protos.dataservice.DeleteToD" +
      "BRequest\032\034.protos.common.ExceptionInfo\"\000" +
      "\022]\n\017deleteSetFromDB\022*.protos.dataservice" +
      ".DeleteSetFromDBRequest\032\034.protos.common." +
      "ExceptionInfo\"\000\022]\n\nexistsInDB\022%.protos.d" +
      "ataservice.ExistsInDBRequest\032&.protos.da" +
      "taservice.ExistsInDBResponse\"\000\022[\n\034cleanE" +
      "xecutionClassDirectory\022\033.protos.common.E" +
      "mptyMessage\032\034.protos.common.ExceptionInf" +
      "o\"\000\022M\n\016closeDbHandler\022\033.protos.common.Em" +
      "ptyMessage\032\034.protos.common.ExceptionInfo" +
      "\"\000\022G\n\010shutDown\022\033.protos.common.EmptyMess" +
      "age\032\034.protos.common.ExceptionInfo\"\000\022S\n\024d" +
      "isconnectFromOthers\022\033.protos.common.Empt" +
      "yMessage\032\034.protos.common.ExceptionInfo\"\000" +
      "\022U\n\026registerPendingObjects\022\033.protos.comm" +
      "on.EmptyMessage\032\034.protos.common.Exceptio" +
      "nInfo\"\000\022J\n\013cleanCaches\022\033.protos.common.E" +
      "mptyMessage\032\034.protos.common.ExceptionInf" +
      "o\"\000\022]\n\017activateTracing\022*.protos.dataserv" +
      "ice.ActivateTracingRequest\032\034.protos.comm" +
      "on.ExceptionInfo\"\000\022P\n\021deactivateTracing\022" +
      "\033.protos.common.EmptyMessage\032\034.protos.co" +
      "mmon.ExceptionInfo\"\000\022L\n\tgetTraces\022\033.prot" +
      "os.common.EmptyMessage\032 .protos.common.G" +
      "etTracesResponse\"\000\022U\n\013deleteAlias\022&.prot" +
      "os.dataservice.DeleteAliasRequest\032\034.prot" +
      "os.common.ExceptionInfo\"\000\022m\n\027detachObjec" +
      "tFromSession\0222.protos.dataservice.Detach" +
      "ObjectFromSessionRequest\032\034.protos.common" +
      ".ExceptionInfo\"\000\022_\n\020closeSessionInDS\022+.p" +
      "rotos.dataservice.CloseSessionInDSReques" +
      "t\032\034.protos.common.ExceptionInfo\"\000\022i\n\025get" +
      "RetainedReferences\022\033.protos.common.Empty" +
      "Message\0321.protos.dataservice.GetRetained" +
      "ReferencesResponse\"\000\022T\n\rgetNumObjects\022\033." +
      "protos.common.EmptyMessage\032$.protos.comm" +
      "on.GetNumObjectsResponse\"\000\022X\n\021getNumObje" +
      "ctsInEE\022\033.protos.common.EmptyMessage\032$.p" +
      "rotos.common.GetNumObjectsResponse\"\000\022[\n\016" +
      "getObjectGraph\022\033.protos.common.EmptyMess" +
      "age\032*.protos.dataservice.GetObjectGraphR" +
      "esponse\"\000BR\n8es.bsc.dataclay.communicati" +
      "on.grpc.generated.dataserviceB\026DataServi" +
      "ceGrpcServiceb\006proto3"
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
