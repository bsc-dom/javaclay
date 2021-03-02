package es.bsc.dataclay.communication.grpc.generated.dataservice;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * Interface exported by the server.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: dataclay/communication/grpc/generated/dataservice/dataservice.proto")
public final class DataServiceGrpc {

  private DataServiceGrpc() {}

  public static final String SERVICE_NAME = "dataclay.communication.grpc.dataservice.DataService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.InitBackendIDRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getInitBackendIDMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "initBackendID",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.InitBackendIDRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.InitBackendIDRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getInitBackendIDMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.InitBackendIDRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getInitBackendIDMethod;
    if ((getInitBackendIDMethod = DataServiceGrpc.getInitBackendIDMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getInitBackendIDMethod = DataServiceGrpc.getInitBackendIDMethod) == null) {
          DataServiceGrpc.getInitBackendIDMethod = getInitBackendIDMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.InitBackendIDRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "initBackendID"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.InitBackendIDRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("initBackendID"))
              .build();
        }
      }
    }
    return getInitBackendIDMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.AssociateExecutionEnvironmentRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getAssociateExecutionEnvironmentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "associateExecutionEnvironment",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.AssociateExecutionEnvironmentRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.AssociateExecutionEnvironmentRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getAssociateExecutionEnvironmentMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.AssociateExecutionEnvironmentRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getAssociateExecutionEnvironmentMethod;
    if ((getAssociateExecutionEnvironmentMethod = DataServiceGrpc.getAssociateExecutionEnvironmentMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getAssociateExecutionEnvironmentMethod = DataServiceGrpc.getAssociateExecutionEnvironmentMethod) == null) {
          DataServiceGrpc.getAssociateExecutionEnvironmentMethod = getAssociateExecutionEnvironmentMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.AssociateExecutionEnvironmentRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "associateExecutionEnvironment"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.AssociateExecutionEnvironmentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("associateExecutionEnvironment"))
              .build();
        }
      }
    }
    return getAssociateExecutionEnvironmentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployMetaClassesRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDeployMetaClassesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "deployMetaClasses",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployMetaClassesRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployMetaClassesRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDeployMetaClassesMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployMetaClassesRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDeployMetaClassesMethod;
    if ((getDeployMetaClassesMethod = DataServiceGrpc.getDeployMetaClassesMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getDeployMetaClassesMethod = DataServiceGrpc.getDeployMetaClassesMethod) == null) {
          DataServiceGrpc.getDeployMetaClassesMethod = getDeployMetaClassesMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployMetaClassesRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "deployMetaClasses"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployMetaClassesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("deployMetaClasses"))
              .build();
        }
      }
    }
    return getDeployMetaClassesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployClassesRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDeployClassesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "deployClasses",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployClassesRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployClassesRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDeployClassesMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployClassesRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDeployClassesMethod;
    if ((getDeployClassesMethod = DataServiceGrpc.getDeployClassesMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getDeployClassesMethod = DataServiceGrpc.getDeployClassesMethod) == null) {
          DataServiceGrpc.getDeployClassesMethod = getDeployClassesMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployClassesRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "deployClasses"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployClassesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("deployClasses"))
              .build();
        }
      }
    }
    return getDeployClassesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.EnrichClassRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getEnrichClassMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "enrichClass",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.EnrichClassRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.EnrichClassRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getEnrichClassMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.EnrichClassRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getEnrichClassMethod;
    if ((getEnrichClassMethod = DataServiceGrpc.getEnrichClassMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getEnrichClassMethod = DataServiceGrpc.getEnrichClassMethod) == null) {
          DataServiceGrpc.getEnrichClassMethod = getEnrichClassMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.EnrichClassRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "enrichClass"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.EnrichClassRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("enrichClass"))
              .build();
        }
      }
    }
    return getEnrichClassMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceResponse> getNewPersistentInstanceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "newPersistentInstance",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceResponse> getNewPersistentInstanceMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceResponse> getNewPersistentInstanceMethod;
    if ((getNewPersistentInstanceMethod = DataServiceGrpc.getNewPersistentInstanceMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getNewPersistentInstanceMethod = DataServiceGrpc.getNewPersistentInstanceMethod) == null) {
          DataServiceGrpc.getNewPersistentInstanceMethod = getNewPersistentInstanceMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "newPersistentInstance"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("newPersistentInstance"))
              .build();
        }
      }
    }
    return getNewPersistentInstanceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getStoreObjectsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "storeObjects",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreObjectsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getStoreObjectsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreObjectsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getStoreObjectsMethod;
    if ((getStoreObjectsMethod = DataServiceGrpc.getStoreObjectsMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getStoreObjectsMethod = DataServiceGrpc.getStoreObjectsMethod) == null) {
          DataServiceGrpc.getStoreObjectsMethod = getStoreObjectsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreObjectsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "storeObjects"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreObjectsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("storeObjects"))
              .build();
        }
      }
    }
    return getStoreObjectsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectResponse> getGetCopyOfObjectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getCopyOfObject",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectResponse> getGetCopyOfObjectMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectResponse> getGetCopyOfObjectMethod;
    if ((getGetCopyOfObjectMethod = DataServiceGrpc.getGetCopyOfObjectMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getGetCopyOfObjectMethod = DataServiceGrpc.getGetCopyOfObjectMethod) == null) {
          DataServiceGrpc.getGetCopyOfObjectMethod = getGetCopyOfObjectMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getCopyOfObject"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("getCopyOfObject"))
              .build();
        }
      }
    }
    return getGetCopyOfObjectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateObjectRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUpdateObjectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "updateObject",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateObjectRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateObjectRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUpdateObjectMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateObjectRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUpdateObjectMethod;
    if ((getUpdateObjectMethod = DataServiceGrpc.getUpdateObjectMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getUpdateObjectMethod = DataServiceGrpc.getUpdateObjectMethod) == null) {
          DataServiceGrpc.getUpdateObjectMethod = getUpdateObjectMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateObjectRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "updateObject"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateObjectRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("updateObject"))
              .build();
        }
      }
    }
    return getUpdateObjectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsResponse> getGetObjectsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getObjects",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsResponse> getGetObjectsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsResponse> getGetObjectsMethod;
    if ((getGetObjectsMethod = DataServiceGrpc.getGetObjectsMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getGetObjectsMethod = DataServiceGrpc.getGetObjectsMethod) == null) {
          DataServiceGrpc.getGetObjectsMethod = getGetObjectsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getObjects"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("getObjects"))
              .build();
        }
      }
    }
    return getGetObjectsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionResponse> getNewVersionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "newVersion",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionResponse> getNewVersionMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionResponse> getNewVersionMethod;
    if ((getNewVersionMethod = DataServiceGrpc.getNewVersionMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getNewVersionMethod = DataServiceGrpc.getNewVersionMethod) == null) {
          DataServiceGrpc.getNewVersionMethod = getNewVersionMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "newVersion"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("newVersion"))
              .build();
        }
      }
    }
    return getNewVersionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ConsolidateVersionRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getConsolidateVersionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "consolidateVersion",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ConsolidateVersionRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ConsolidateVersionRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getConsolidateVersionMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ConsolidateVersionRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getConsolidateVersionMethod;
    if ((getConsolidateVersionMethod = DataServiceGrpc.getConsolidateVersionMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getConsolidateVersionMethod = DataServiceGrpc.getConsolidateVersionMethod) == null) {
          DataServiceGrpc.getConsolidateVersionMethod = getConsolidateVersionMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ConsolidateVersionRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "consolidateVersion"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ConsolidateVersionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("consolidateVersion"))
              .build();
        }
      }
    }
    return getConsolidateVersionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpsertObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUpsertObjectsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "upsertObjects",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpsertObjectsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpsertObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUpsertObjectsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpsertObjectsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUpsertObjectsMethod;
    if ((getUpsertObjectsMethod = DataServiceGrpc.getUpsertObjectsMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getUpsertObjectsMethod = DataServiceGrpc.getUpsertObjectsMethod) == null) {
          DataServiceGrpc.getUpsertObjectsMethod = getUpsertObjectsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpsertObjectsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "upsertObjects"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpsertObjectsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("upsertObjects"))
              .build();
        }
      }
    }
    return getUpsertObjectsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaResponse> getNewReplicaMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "newReplica",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaResponse> getNewReplicaMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaResponse> getNewReplicaMethod;
    if ((getNewReplicaMethod = DataServiceGrpc.getNewReplicaMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getNewReplicaMethod = DataServiceGrpc.getNewReplicaMethod) == null) {
          DataServiceGrpc.getNewReplicaMethod = getNewReplicaMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "newReplica"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("newReplica"))
              .build();
        }
      }
    }
    return getNewReplicaMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsResponse> getMoveObjectsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "moveObjects",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsResponse> getMoveObjectsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsResponse> getMoveObjectsMethod;
    if ((getMoveObjectsMethod = DataServiceGrpc.getMoveObjectsMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getMoveObjectsMethod = DataServiceGrpc.getMoveObjectsMethod) == null) {
          DataServiceGrpc.getMoveObjectsMethod = getMoveObjectsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "moveObjects"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("moveObjects"))
              .build();
        }
      }
    }
    return getMoveObjectsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsResponse> getRemoveObjectsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "removeObjects",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsResponse> getRemoveObjectsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsResponse> getRemoveObjectsMethod;
    if ((getRemoveObjectsMethod = DataServiceGrpc.getRemoveObjectsMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getRemoveObjectsMethod = DataServiceGrpc.getRemoveObjectsMethod) == null) {
          DataServiceGrpc.getRemoveObjectsMethod = getRemoveObjectsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "removeObjects"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("removeObjects"))
              .build();
        }
      }
    }
    return getRemoveObjectsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsResponse> getMigrateObjectsToBackendsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "migrateObjectsToBackends",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsResponse> getMigrateObjectsToBackendsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsResponse> getMigrateObjectsToBackendsMethod;
    if ((getMigrateObjectsToBackendsMethod = DataServiceGrpc.getMigrateObjectsToBackendsMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getMigrateObjectsToBackendsMethod = DataServiceGrpc.getMigrateObjectsToBackendsMethod) == null) {
          DataServiceGrpc.getMigrateObjectsToBackendsMethod = getMigrateObjectsToBackendsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "migrateObjectsToBackends"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("migrateObjectsToBackends"))
              .build();
        }
      }
    }
    return getMigrateObjectsToBackendsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryResponse> getGetClassIDFromObjectInMemoryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getClassIDFromObjectInMemory",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryResponse> getGetClassIDFromObjectInMemoryMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryResponse> getGetClassIDFromObjectInMemoryMethod;
    if ((getGetClassIDFromObjectInMemoryMethod = DataServiceGrpc.getGetClassIDFromObjectInMemoryMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getGetClassIDFromObjectInMemoryMethod = DataServiceGrpc.getGetClassIDFromObjectInMemoryMethod) == null) {
          DataServiceGrpc.getGetClassIDFromObjectInMemoryMethod = getGetClassIDFromObjectInMemoryMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getClassIDFromObjectInMemory"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("getClassIDFromObjectInMemory"))
              .build();
        }
      }
    }
    return getGetClassIDFromObjectInMemoryMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationResponse> getExecuteImplementationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "executeImplementation",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationResponse> getExecuteImplementationMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationResponse> getExecuteImplementationMethod;
    if ((getExecuteImplementationMethod = DataServiceGrpc.getExecuteImplementationMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getExecuteImplementationMethod = DataServiceGrpc.getExecuteImplementationMethod) == null) {
          DataServiceGrpc.getExecuteImplementationMethod = getExecuteImplementationMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "executeImplementation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("executeImplementation"))
              .build();
        }
      }
    }
    return getExecuteImplementationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MakePersistentRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getMakePersistentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "makePersistent",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MakePersistentRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MakePersistentRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getMakePersistentMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MakePersistentRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getMakePersistentMethod;
    if ((getMakePersistentMethod = DataServiceGrpc.getMakePersistentMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getMakePersistentMethod = DataServiceGrpc.getMakePersistentMethod) == null) {
          DataServiceGrpc.getMakePersistentMethod = getMakePersistentMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MakePersistentRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "makePersistent"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MakePersistentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("makePersistent"))
              .build();
        }
      }
    }
    return getMakePersistentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.FederateRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getFederateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "federate",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.FederateRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.FederateRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getFederateMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.FederateRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getFederateMethod;
    if ((getFederateMethod = DataServiceGrpc.getFederateMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getFederateMethod = DataServiceGrpc.getFederateMethod) == null) {
          DataServiceGrpc.getFederateMethod = getFederateMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.FederateRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "federate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.FederateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("federate"))
              .build();
        }
      }
    }
    return getFederateMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UnfederateRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnfederateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "unfederate",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UnfederateRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UnfederateRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnfederateMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UnfederateRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnfederateMethod;
    if ((getUnfederateMethod = DataServiceGrpc.getUnfederateMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getUnfederateMethod = DataServiceGrpc.getUnfederateMethod) == null) {
          DataServiceGrpc.getUnfederateMethod = getUnfederateMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UnfederateRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "unfederate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UnfederateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("unfederate"))
              .build();
        }
      }
    }
    return getUnfederateMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyFederationRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getNotifyFederationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "notifyFederation",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyFederationRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyFederationRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getNotifyFederationMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyFederationRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getNotifyFederationMethod;
    if ((getNotifyFederationMethod = DataServiceGrpc.getNotifyFederationMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getNotifyFederationMethod = DataServiceGrpc.getNotifyFederationMethod) == null) {
          DataServiceGrpc.getNotifyFederationMethod = getNotifyFederationMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyFederationRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "notifyFederation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyFederationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("notifyFederation"))
              .build();
        }
      }
    }
    return getNotifyFederationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyUnfederationRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getNotifyUnfederationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "notifyUnfederation",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyUnfederationRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyUnfederationRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getNotifyUnfederationMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyUnfederationRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getNotifyUnfederationMethod;
    if ((getNotifyUnfederationMethod = DataServiceGrpc.getNotifyUnfederationMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getNotifyUnfederationMethod = DataServiceGrpc.getNotifyUnfederationMethod) == null) {
          DataServiceGrpc.getNotifyUnfederationMethod = getNotifyUnfederationMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyUnfederationRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "notifyUnfederation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyUnfederationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("notifyUnfederation"))
              .build();
        }
      }
    }
    return getNotifyUnfederationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsResponse> getExistsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "exists",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsResponse> getExistsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsResponse> getExistsMethod;
    if ((getExistsMethod = DataServiceGrpc.getExistsMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getExistsMethod = DataServiceGrpc.getExistsMethod) == null) {
          DataServiceGrpc.getExistsMethod = getExistsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "exists"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("exists"))
              .build();
        }
      }
    }
    return getExistsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.SynchronizeRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSynchronizeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "synchronize",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.SynchronizeRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.SynchronizeRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSynchronizeMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.SynchronizeRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSynchronizeMethod;
    if ((getSynchronizeMethod = DataServiceGrpc.getSynchronizeMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getSynchronizeMethod = DataServiceGrpc.getSynchronizeMethod) == null) {
          DataServiceGrpc.getSynchronizeMethod = getSynchronizeMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.SynchronizeRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "synchronize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.SynchronizeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("synchronize"))
              .build();
        }
      }
    }
    return getSynchronizeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreToDBRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getStoreToDBMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "storeToDB",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreToDBRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreToDBRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getStoreToDBMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreToDBRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getStoreToDBMethod;
    if ((getStoreToDBMethod = DataServiceGrpc.getStoreToDBMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getStoreToDBMethod = DataServiceGrpc.getStoreToDBMethod) == null) {
          DataServiceGrpc.getStoreToDBMethod = getStoreToDBMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreToDBRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "storeToDB"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreToDBRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("storeToDB"))
              .build();
        }
      }
    }
    return getStoreToDBMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBResponse> getGetFromDBMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getFromDB",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBResponse> getGetFromDBMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBResponse> getGetFromDBMethod;
    if ((getGetFromDBMethod = DataServiceGrpc.getGetFromDBMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getGetFromDBMethod = DataServiceGrpc.getGetFromDBMethod) == null) {
          DataServiceGrpc.getGetFromDBMethod = getGetFromDBMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getFromDB"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("getFromDB"))
              .build();
        }
      }
    }
    return getGetFromDBMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateToDBRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUpdateToDBMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "updateToDB",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateToDBRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateToDBRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUpdateToDBMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateToDBRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUpdateToDBMethod;
    if ((getUpdateToDBMethod = DataServiceGrpc.getUpdateToDBMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getUpdateToDBMethod = DataServiceGrpc.getUpdateToDBMethod) == null) {
          DataServiceGrpc.getUpdateToDBMethod = getUpdateToDBMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateToDBRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "updateToDB"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateToDBRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("updateToDB"))
              .build();
        }
      }
    }
    return getUpdateToDBMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeleteToDBRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDeleteToDBMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "deleteToDB",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeleteToDBRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeleteToDBRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDeleteToDBMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeleteToDBRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDeleteToDBMethod;
    if ((getDeleteToDBMethod = DataServiceGrpc.getDeleteToDBMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getDeleteToDBMethod = DataServiceGrpc.getDeleteToDBMethod) == null) {
          DataServiceGrpc.getDeleteToDBMethod = getDeleteToDBMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeleteToDBRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "deleteToDB"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeleteToDBRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("deleteToDB"))
              .build();
        }
      }
    }
    return getDeleteToDBMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBResponse> getExistsInDBMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "existsInDB",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBRequest,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBResponse> getExistsInDBMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBResponse> getExistsInDBMethod;
    if ((getExistsInDBMethod = DataServiceGrpc.getExistsInDBMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getExistsInDBMethod = DataServiceGrpc.getExistsInDBMethod) == null) {
          DataServiceGrpc.getExistsInDBMethod = getExistsInDBMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBRequest, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "existsInDB"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("existsInDB"))
              .build();
        }
      }
    }
    return getExistsInDBMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCleanExecutionClassDirectoryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "cleanExecutionClassDirectory",
      requestType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCleanExecutionClassDirectoryMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCleanExecutionClassDirectoryMethod;
    if ((getCleanExecutionClassDirectoryMethod = DataServiceGrpc.getCleanExecutionClassDirectoryMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getCleanExecutionClassDirectoryMethod = DataServiceGrpc.getCleanExecutionClassDirectoryMethod) == null) {
          DataServiceGrpc.getCleanExecutionClassDirectoryMethod = getCleanExecutionClassDirectoryMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "cleanExecutionClassDirectory"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("cleanExecutionClassDirectory"))
              .build();
        }
      }
    }
    return getCleanExecutionClassDirectoryMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCloseDbHandlerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "closeDbHandler",
      requestType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCloseDbHandlerMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCloseDbHandlerMethod;
    if ((getCloseDbHandlerMethod = DataServiceGrpc.getCloseDbHandlerMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getCloseDbHandlerMethod = DataServiceGrpc.getCloseDbHandlerMethod) == null) {
          DataServiceGrpc.getCloseDbHandlerMethod = getCloseDbHandlerMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "closeDbHandler"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("closeDbHandler"))
              .build();
        }
      }
    }
    return getCloseDbHandlerMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getShutDownMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "shutDown",
      requestType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getShutDownMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getShutDownMethod;
    if ((getShutDownMethod = DataServiceGrpc.getShutDownMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getShutDownMethod = DataServiceGrpc.getShutDownMethod) == null) {
          DataServiceGrpc.getShutDownMethod = getShutDownMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "shutDown"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("shutDown"))
              .build();
        }
      }
    }
    return getShutDownMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDisconnectFromOthersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "disconnectFromOthers",
      requestType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDisconnectFromOthersMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDisconnectFromOthersMethod;
    if ((getDisconnectFromOthersMethod = DataServiceGrpc.getDisconnectFromOthersMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getDisconnectFromOthersMethod = DataServiceGrpc.getDisconnectFromOthersMethod) == null) {
          DataServiceGrpc.getDisconnectFromOthersMethod = getDisconnectFromOthersMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "disconnectFromOthers"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("disconnectFromOthers"))
              .build();
        }
      }
    }
    return getDisconnectFromOthersMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterPendingObjectsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "registerPendingObjects",
      requestType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterPendingObjectsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterPendingObjectsMethod;
    if ((getRegisterPendingObjectsMethod = DataServiceGrpc.getRegisterPendingObjectsMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getRegisterPendingObjectsMethod = DataServiceGrpc.getRegisterPendingObjectsMethod) == null) {
          DataServiceGrpc.getRegisterPendingObjectsMethod = getRegisterPendingObjectsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "registerPendingObjects"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("registerPendingObjects"))
              .build();
        }
      }
    }
    return getRegisterPendingObjectsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCleanCachesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "cleanCaches",
      requestType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCleanCachesMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCleanCachesMethod;
    if ((getCleanCachesMethod = DataServiceGrpc.getCleanCachesMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getCleanCachesMethod = DataServiceGrpc.getCleanCachesMethod) == null) {
          DataServiceGrpc.getCleanCachesMethod = getCleanCachesMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "cleanCaches"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("cleanCaches"))
              .build();
        }
      }
    }
    return getCleanCachesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ActivateTracingRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getActivateTracingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "activateTracing",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ActivateTracingRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ActivateTracingRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getActivateTracingMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ActivateTracingRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getActivateTracingMethod;
    if ((getActivateTracingMethod = DataServiceGrpc.getActivateTracingMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getActivateTracingMethod = DataServiceGrpc.getActivateTracingMethod) == null) {
          DataServiceGrpc.getActivateTracingMethod = getActivateTracingMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ActivateTracingRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "activateTracing"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ActivateTracingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("activateTracing"))
              .build();
        }
      }
    }
    return getActivateTracingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDeactivateTracingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "deactivateTracing",
      requestType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDeactivateTracingMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDeactivateTracingMethod;
    if ((getDeactivateTracingMethod = DataServiceGrpc.getDeactivateTracingMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getDeactivateTracingMethod = DataServiceGrpc.getDeactivateTracingMethod) == null) {
          DataServiceGrpc.getDeactivateTracingMethod = getDeactivateTracingMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "deactivateTracing"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("deactivateTracing"))
              .build();
        }
      }
    }
    return getDeactivateTracingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse> getGetTracesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getTraces",
      requestType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse> getGetTracesMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse> getGetTracesMethod;
    if ((getGetTracesMethod = DataServiceGrpc.getGetTracesMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getGetTracesMethod = DataServiceGrpc.getGetTracesMethod) == null) {
          DataServiceGrpc.getGetTracesMethod = getGetTracesMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getTraces"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("getTraces"))
              .build();
        }
      }
    }
    return getGetTracesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.CloseSessionInDSRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCloseSessionInDSMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "closeSessionInDS",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.CloseSessionInDSRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.CloseSessionInDSRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCloseSessionInDSMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.CloseSessionInDSRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCloseSessionInDSMethod;
    if ((getCloseSessionInDSMethod = DataServiceGrpc.getCloseSessionInDSMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getCloseSessionInDSMethod = DataServiceGrpc.getCloseSessionInDSMethod) == null) {
          DataServiceGrpc.getCloseSessionInDSMethod = getCloseSessionInDSMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.CloseSessionInDSRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "closeSessionInDS"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.CloseSessionInDSRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("closeSessionInDS"))
              .build();
        }
      }
    }
    return getCloseSessionInDSMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateRefsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUpdateRefsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "updateRefs",
      requestType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateRefsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateRefsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUpdateRefsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateRefsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUpdateRefsMethod;
    if ((getUpdateRefsMethod = DataServiceGrpc.getUpdateRefsMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getUpdateRefsMethod = DataServiceGrpc.getUpdateRefsMethod) == null) {
          DataServiceGrpc.getUpdateRefsMethod = getUpdateRefsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateRefsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "updateRefs"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateRefsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("updateRefs"))
              .build();
        }
      }
    }
    return getUpdateRefsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetRetainedReferencesResponse> getGetRetainedReferencesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getRetainedReferences",
      requestType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetRetainedReferencesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetRetainedReferencesResponse> getGetRetainedReferencesMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetRetainedReferencesResponse> getGetRetainedReferencesMethod;
    if ((getGetRetainedReferencesMethod = DataServiceGrpc.getGetRetainedReferencesMethod) == null) {
      synchronized (DataServiceGrpc.class) {
        if ((getGetRetainedReferencesMethod = DataServiceGrpc.getGetRetainedReferencesMethod) == null) {
          DataServiceGrpc.getGetRetainedReferencesMethod = getGetRetainedReferencesMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetRetainedReferencesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getRetainedReferences"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetRetainedReferencesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataServiceMethodDescriptorSupplier("getRetainedReferences"))
              .build();
        }
      }
    }
    return getGetRetainedReferencesMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DataServiceStub newStub(io.grpc.Channel channel) {
    return new DataServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DataServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new DataServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DataServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new DataServiceFutureStub(channel);
  }

  /**
   * <pre>
   * Interface exported by the server.
   * </pre>
   */
  public static abstract class DataServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Deployment
     * </pre>
     */
    public void initBackendID(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.InitBackendIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getInitBackendIDMethod(), responseObserver);
    }

    /**
     */
    public void associateExecutionEnvironment(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.AssociateExecutionEnvironmentRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getAssociateExecutionEnvironmentMethod(), responseObserver);
    }

    /**
     */
    public void deployMetaClasses(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployMetaClassesRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getDeployMetaClassesMethod(), responseObserver);
    }

    /**
     */
    public void deployClasses(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployClassesRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getDeployClassesMethod(), responseObserver);
    }

    /**
     */
    public void enrichClass(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.EnrichClassRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getEnrichClassMethod(), responseObserver);
    }

    /**
     * <pre>
     * Execution Environment
     * </pre>
     */
    public void newPersistentInstance(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNewPersistentInstanceMethod(), responseObserver);
    }

    /**
     */
    public void storeObjects(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getStoreObjectsMethod(), responseObserver);
    }

    /**
     */
    public void getCopyOfObject(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetCopyOfObjectMethod(), responseObserver);
    }

    /**
     */
    public void updateObject(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateObjectRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getUpdateObjectMethod(), responseObserver);
    }

    /**
     */
    public void getObjects(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetObjectsMethod(), responseObserver);
    }

    /**
     */
    public void newVersion(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNewVersionMethod(), responseObserver);
    }

    /**
     */
    public void consolidateVersion(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ConsolidateVersionRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getConsolidateVersionMethod(), responseObserver);
    }

    /**
     */
    public void upsertObjects(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpsertObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getUpsertObjectsMethod(), responseObserver);
    }

    /**
     */
    public void newReplica(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNewReplicaMethod(), responseObserver);
    }

    /**
     */
    public void moveObjects(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getMoveObjectsMethod(), responseObserver);
    }

    /**
     */
    public void removeObjects(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRemoveObjectsMethod(), responseObserver);
    }

    /**
     */
    public void migrateObjectsToBackends(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getMigrateObjectsToBackendsMethod(), responseObserver);
    }

    /**
     */
    public void getClassIDFromObjectInMemory(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetClassIDFromObjectInMemoryMethod(), responseObserver);
    }

    /**
     */
    public void executeImplementation(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getExecuteImplementationMethod(), responseObserver);
    }

    /**
     */
    public void makePersistent(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MakePersistentRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getMakePersistentMethod(), responseObserver);
    }

    /**
     */
    public void federate(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.FederateRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getFederateMethod(), responseObserver);
    }

    /**
     */
    public void unfederate(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UnfederateRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getUnfederateMethod(), responseObserver);
    }

    /**
     */
    public void notifyFederation(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyFederationRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getNotifyFederationMethod(), responseObserver);
    }

    /**
     */
    public void notifyUnfederation(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyUnfederationRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getNotifyUnfederationMethod(), responseObserver);
    }

    /**
     */
    public void exists(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getExistsMethod(), responseObserver);
    }

    /**
     */
    public void synchronize(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.SynchronizeRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getSynchronizeMethod(), responseObserver);
    }

    /**
     * <pre>
     * Storage Location
     * </pre>
     */
    public void storeToDB(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreToDBRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getStoreToDBMethod(), responseObserver);
    }

    /**
     */
    public void getFromDB(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetFromDBMethod(), responseObserver);
    }

    /**
     */
    public void updateToDB(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateToDBRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getUpdateToDBMethod(), responseObserver);
    }

    /**
     */
    public void deleteToDB(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeleteToDBRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getDeleteToDBMethod(), responseObserver);
    }

    /**
     */
    public void existsInDB(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getExistsInDBMethod(), responseObserver);
    }

    /**
     * <pre>
     * Others
     * </pre>
     */
    public void cleanExecutionClassDirectory(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getCleanExecutionClassDirectoryMethod(), responseObserver);
    }

    /**
     */
    public void closeDbHandler(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getCloseDbHandlerMethod(), responseObserver);
    }

    /**
     */
    public void shutDown(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getShutDownMethod(), responseObserver);
    }

    /**
     */
    public void disconnectFromOthers(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getDisconnectFromOthersMethod(), responseObserver);
    }

    /**
     */
    public void registerPendingObjects(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getRegisterPendingObjectsMethod(), responseObserver);
    }

    /**
     * <pre>
     * Paraver
     * </pre>
     */
    public void cleanCaches(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getCleanCachesMethod(), responseObserver);
    }

    /**
     */
    public void activateTracing(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ActivateTracingRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getActivateTracingMethod(), responseObserver);
    }

    /**
     */
    public void deactivateTracing(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getDeactivateTracingMethod(), responseObserver);
    }

    /**
     */
    public void getTraces(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetTracesMethod(), responseObserver);
    }

    /**
     * <pre>
     * Garbage collection
     * </pre>
     */
    public void closeSessionInDS(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.CloseSessionInDSRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getCloseSessionInDSMethod(), responseObserver);
    }

    /**
     */
    public void updateRefs(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateRefsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getUpdateRefsMethod(), responseObserver);
    }

    /**
     */
    public void getRetainedReferences(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetRetainedReferencesResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetRetainedReferencesMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getInitBackendIDMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.InitBackendIDRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_INIT_BACKEND_ID)))
          .addMethod(
            getAssociateExecutionEnvironmentMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.AssociateExecutionEnvironmentRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_ASSOCIATE_EXECUTION_ENVIRONMENT)))
          .addMethod(
            getDeployMetaClassesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployMetaClassesRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_DEPLOY_META_CLASSES)))
          .addMethod(
            getDeployClassesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployClassesRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_DEPLOY_CLASSES)))
          .addMethod(
            getEnrichClassMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.EnrichClassRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_ENRICH_CLASS)))
          .addMethod(
            getNewPersistentInstanceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceRequest,
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceResponse>(
                  this, METHODID_NEW_PERSISTENT_INSTANCE)))
          .addMethod(
            getStoreObjectsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreObjectsRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_STORE_OBJECTS)))
          .addMethod(
            getGetCopyOfObjectMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectRequest,
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectResponse>(
                  this, METHODID_GET_COPY_OF_OBJECT)))
          .addMethod(
            getUpdateObjectMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateObjectRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_UPDATE_OBJECT)))
          .addMethod(
            getGetObjectsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsRequest,
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsResponse>(
                  this, METHODID_GET_OBJECTS)))
          .addMethod(
            getNewVersionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionRequest,
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionResponse>(
                  this, METHODID_NEW_VERSION)))
          .addMethod(
            getConsolidateVersionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ConsolidateVersionRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_CONSOLIDATE_VERSION)))
          .addMethod(
            getUpsertObjectsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpsertObjectsRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_UPSERT_OBJECTS)))
          .addMethod(
            getNewReplicaMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaRequest,
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaResponse>(
                  this, METHODID_NEW_REPLICA)))
          .addMethod(
            getMoveObjectsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsRequest,
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsResponse>(
                  this, METHODID_MOVE_OBJECTS)))
          .addMethod(
            getRemoveObjectsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsRequest,
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsResponse>(
                  this, METHODID_REMOVE_OBJECTS)))
          .addMethod(
            getMigrateObjectsToBackendsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsRequest,
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsResponse>(
                  this, METHODID_MIGRATE_OBJECTS_TO_BACKENDS)))
          .addMethod(
            getGetClassIDFromObjectInMemoryMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryRequest,
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryResponse>(
                  this, METHODID_GET_CLASS_IDFROM_OBJECT_IN_MEMORY)))
          .addMethod(
            getExecuteImplementationMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationRequest,
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationResponse>(
                  this, METHODID_EXECUTE_IMPLEMENTATION)))
          .addMethod(
            getMakePersistentMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MakePersistentRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_MAKE_PERSISTENT)))
          .addMethod(
            getFederateMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.FederateRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_FEDERATE)))
          .addMethod(
            getUnfederateMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UnfederateRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_UNFEDERATE)))
          .addMethod(
            getNotifyFederationMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyFederationRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_NOTIFY_FEDERATION)))
          .addMethod(
            getNotifyUnfederationMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyUnfederationRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_NOTIFY_UNFEDERATION)))
          .addMethod(
            getExistsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsRequest,
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsResponse>(
                  this, METHODID_EXISTS)))
          .addMethod(
            getSynchronizeMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.SynchronizeRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_SYNCHRONIZE)))
          .addMethod(
            getStoreToDBMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreToDBRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_STORE_TO_DB)))
          .addMethod(
            getGetFromDBMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBRequest,
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBResponse>(
                  this, METHODID_GET_FROM_DB)))
          .addMethod(
            getUpdateToDBMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateToDBRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_UPDATE_TO_DB)))
          .addMethod(
            getDeleteToDBMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeleteToDBRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_DELETE_TO_DB)))
          .addMethod(
            getExistsInDBMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBRequest,
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBResponse>(
                  this, METHODID_EXISTS_IN_DB)))
          .addMethod(
            getCleanExecutionClassDirectoryMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_CLEAN_EXECUTION_CLASS_DIRECTORY)))
          .addMethod(
            getCloseDbHandlerMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_CLOSE_DB_HANDLER)))
          .addMethod(
            getShutDownMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_SHUT_DOWN)))
          .addMethod(
            getDisconnectFromOthersMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_DISCONNECT_FROM_OTHERS)))
          .addMethod(
            getRegisterPendingObjectsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_REGISTER_PENDING_OBJECTS)))
          .addMethod(
            getCleanCachesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_CLEAN_CACHES)))
          .addMethod(
            getActivateTracingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ActivateTracingRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_ACTIVATE_TRACING)))
          .addMethod(
            getDeactivateTracingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_DEACTIVATE_TRACING)))
          .addMethod(
            getGetTracesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse>(
                  this, METHODID_GET_TRACES)))
          .addMethod(
            getCloseSessionInDSMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.CloseSessionInDSRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_CLOSE_SESSION_IN_DS)))
          .addMethod(
            getUpdateRefsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateRefsRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_UPDATE_REFS)))
          .addMethod(
            getGetRetainedReferencesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
                es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetRetainedReferencesResponse>(
                  this, METHODID_GET_RETAINED_REFERENCES)))
          .build();
    }
  }

  /**
   * <pre>
   * Interface exported by the server.
   * </pre>
   */
  public static final class DataServiceStub extends io.grpc.stub.AbstractStub<DataServiceStub> {
    private DataServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DataServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DataServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Deployment
     * </pre>
     */
    public void initBackendID(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.InitBackendIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getInitBackendIDMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void associateExecutionEnvironment(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.AssociateExecutionEnvironmentRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAssociateExecutionEnvironmentMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deployMetaClasses(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployMetaClassesRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeployMetaClassesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deployClasses(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployClassesRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeployClassesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void enrichClass(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.EnrichClassRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getEnrichClassMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Execution Environment
     * </pre>
     */
    public void newPersistentInstance(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNewPersistentInstanceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void storeObjects(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getStoreObjectsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getCopyOfObject(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetCopyOfObjectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateObject(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateObjectRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUpdateObjectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getObjects(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetObjectsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void newVersion(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNewVersionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void consolidateVersion(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ConsolidateVersionRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getConsolidateVersionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void upsertObjects(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpsertObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUpsertObjectsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void newReplica(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNewReplicaMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void moveObjects(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getMoveObjectsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeObjects(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRemoveObjectsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void migrateObjectsToBackends(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getMigrateObjectsToBackendsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getClassIDFromObjectInMemory(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetClassIDFromObjectInMemoryMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void executeImplementation(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getExecuteImplementationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void makePersistent(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MakePersistentRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getMakePersistentMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void federate(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.FederateRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getFederateMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void unfederate(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UnfederateRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUnfederateMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void notifyFederation(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyFederationRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNotifyFederationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void notifyUnfederation(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyUnfederationRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNotifyUnfederationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void exists(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getExistsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void synchronize(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.SynchronizeRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSynchronizeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Storage Location
     * </pre>
     */
    public void storeToDB(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreToDBRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getStoreToDBMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getFromDB(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetFromDBMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateToDB(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateToDBRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUpdateToDBMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteToDB(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeleteToDBRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeleteToDBMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void existsInDB(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getExistsInDBMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Others
     * </pre>
     */
    public void cleanExecutionClassDirectory(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCleanExecutionClassDirectoryMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void closeDbHandler(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCloseDbHandlerMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void shutDown(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getShutDownMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void disconnectFromOthers(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDisconnectFromOthersMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void registerPendingObjects(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegisterPendingObjectsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Paraver
     * </pre>
     */
    public void cleanCaches(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCleanCachesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void activateTracing(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ActivateTracingRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getActivateTracingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deactivateTracing(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeactivateTracingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getTraces(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetTracesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Garbage collection
     * </pre>
     */
    public void closeSessionInDS(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.CloseSessionInDSRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCloseSessionInDSMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateRefs(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateRefsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUpdateRefsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getRetainedReferences(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetRetainedReferencesResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetRetainedReferencesMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * Interface exported by the server.
   * </pre>
   */
  public static final class DataServiceBlockingStub extends io.grpc.stub.AbstractStub<DataServiceBlockingStub> {
    private DataServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DataServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DataServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Deployment
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo initBackendID(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.InitBackendIDRequest request) {
      return blockingUnaryCall(
          getChannel(), getInitBackendIDMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo associateExecutionEnvironment(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.AssociateExecutionEnvironmentRequest request) {
      return blockingUnaryCall(
          getChannel(), getAssociateExecutionEnvironmentMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo deployMetaClasses(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployMetaClassesRequest request) {
      return blockingUnaryCall(
          getChannel(), getDeployMetaClassesMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo deployClasses(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployClassesRequest request) {
      return blockingUnaryCall(
          getChannel(), getDeployClassesMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo enrichClass(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.EnrichClassRequest request) {
      return blockingUnaryCall(
          getChannel(), getEnrichClassMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Execution Environment
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceResponse newPersistentInstance(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceRequest request) {
      return blockingUnaryCall(
          getChannel(), getNewPersistentInstanceMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo storeObjects(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreObjectsRequest request) {
      return blockingUnaryCall(
          getChannel(), getStoreObjectsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectResponse getCopyOfObject(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetCopyOfObjectMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo updateObject(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateObjectRequest request) {
      return blockingUnaryCall(
          getChannel(), getUpdateObjectMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsResponse getObjects(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetObjectsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionResponse newVersion(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionRequest request) {
      return blockingUnaryCall(
          getChannel(), getNewVersionMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo consolidateVersion(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ConsolidateVersionRequest request) {
      return blockingUnaryCall(
          getChannel(), getConsolidateVersionMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo upsertObjects(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpsertObjectsRequest request) {
      return blockingUnaryCall(
          getChannel(), getUpsertObjectsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaResponse newReplica(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaRequest request) {
      return blockingUnaryCall(
          getChannel(), getNewReplicaMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsResponse moveObjects(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsRequest request) {
      return blockingUnaryCall(
          getChannel(), getMoveObjectsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsResponse removeObjects(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsRequest request) {
      return blockingUnaryCall(
          getChannel(), getRemoveObjectsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsResponse migrateObjectsToBackends(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsRequest request) {
      return blockingUnaryCall(
          getChannel(), getMigrateObjectsToBackendsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryResponse getClassIDFromObjectInMemory(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetClassIDFromObjectInMemoryMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationResponse executeImplementation(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationRequest request) {
      return blockingUnaryCall(
          getChannel(), getExecuteImplementationMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo makePersistent(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MakePersistentRequest request) {
      return blockingUnaryCall(
          getChannel(), getMakePersistentMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo federate(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.FederateRequest request) {
      return blockingUnaryCall(
          getChannel(), getFederateMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo unfederate(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UnfederateRequest request) {
      return blockingUnaryCall(
          getChannel(), getUnfederateMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo notifyFederation(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyFederationRequest request) {
      return blockingUnaryCall(
          getChannel(), getNotifyFederationMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo notifyUnfederation(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyUnfederationRequest request) {
      return blockingUnaryCall(
          getChannel(), getNotifyUnfederationMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsResponse exists(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsRequest request) {
      return blockingUnaryCall(
          getChannel(), getExistsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo synchronize(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.SynchronizeRequest request) {
      return blockingUnaryCall(
          getChannel(), getSynchronizeMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Storage Location
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo storeToDB(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreToDBRequest request) {
      return blockingUnaryCall(
          getChannel(), getStoreToDBMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBResponse getFromDB(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetFromDBMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo updateToDB(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateToDBRequest request) {
      return blockingUnaryCall(
          getChannel(), getUpdateToDBMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo deleteToDB(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeleteToDBRequest request) {
      return blockingUnaryCall(
          getChannel(), getDeleteToDBMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBResponse existsInDB(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBRequest request) {
      return blockingUnaryCall(
          getChannel(), getExistsInDBMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Others
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo cleanExecutionClassDirectory(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return blockingUnaryCall(
          getChannel(), getCleanExecutionClassDirectoryMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo closeDbHandler(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return blockingUnaryCall(
          getChannel(), getCloseDbHandlerMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo shutDown(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return blockingUnaryCall(
          getChannel(), getShutDownMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo disconnectFromOthers(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return blockingUnaryCall(
          getChannel(), getDisconnectFromOthersMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo registerPendingObjects(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return blockingUnaryCall(
          getChannel(), getRegisterPendingObjectsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Paraver
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo cleanCaches(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return blockingUnaryCall(
          getChannel(), getCleanCachesMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo activateTracing(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ActivateTracingRequest request) {
      return blockingUnaryCall(
          getChannel(), getActivateTracingMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo deactivateTracing(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return blockingUnaryCall(
          getChannel(), getDeactivateTracingMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse getTraces(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return blockingUnaryCall(
          getChannel(), getGetTracesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Garbage collection
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo closeSessionInDS(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.CloseSessionInDSRequest request) {
      return blockingUnaryCall(
          getChannel(), getCloseSessionInDSMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo updateRefs(es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateRefsRequest request) {
      return blockingUnaryCall(
          getChannel(), getUpdateRefsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetRetainedReferencesResponse getRetainedReferences(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return blockingUnaryCall(
          getChannel(), getGetRetainedReferencesMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * Interface exported by the server.
   * </pre>
   */
  public static final class DataServiceFutureStub extends io.grpc.stub.AbstractStub<DataServiceFutureStub> {
    private DataServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DataServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DataServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Deployment
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> initBackendID(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.InitBackendIDRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getInitBackendIDMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> associateExecutionEnvironment(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.AssociateExecutionEnvironmentRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAssociateExecutionEnvironmentMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> deployMetaClasses(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployMetaClassesRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDeployMetaClassesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> deployClasses(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployClassesRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDeployClassesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> enrichClass(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.EnrichClassRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getEnrichClassMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Execution Environment
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceResponse> newPersistentInstance(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNewPersistentInstanceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> storeObjects(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreObjectsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getStoreObjectsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectResponse> getCopyOfObject(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetCopyOfObjectMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> updateObject(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateObjectRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUpdateObjectMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsResponse> getObjects(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetObjectsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionResponse> newVersion(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNewVersionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> consolidateVersion(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ConsolidateVersionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getConsolidateVersionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> upsertObjects(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpsertObjectsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUpsertObjectsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaResponse> newReplica(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNewReplicaMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsResponse> moveObjects(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getMoveObjectsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsResponse> removeObjects(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRemoveObjectsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsResponse> migrateObjectsToBackends(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getMigrateObjectsToBackendsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryResponse> getClassIDFromObjectInMemory(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetClassIDFromObjectInMemoryMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationResponse> executeImplementation(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getExecuteImplementationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> makePersistent(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MakePersistentRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getMakePersistentMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> federate(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.FederateRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getFederateMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> unfederate(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UnfederateRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUnfederateMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> notifyFederation(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyFederationRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNotifyFederationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> notifyUnfederation(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyUnfederationRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNotifyUnfederationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsResponse> exists(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getExistsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> synchronize(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.SynchronizeRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSynchronizeMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Storage Location
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> storeToDB(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreToDBRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getStoreToDBMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBResponse> getFromDB(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetFromDBMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> updateToDB(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateToDBRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUpdateToDBMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> deleteToDB(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeleteToDBRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDeleteToDBMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBResponse> existsInDB(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getExistsInDBMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Others
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> cleanExecutionClassDirectory(
        es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getCleanExecutionClassDirectoryMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> closeDbHandler(
        es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getCloseDbHandlerMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> shutDown(
        es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getShutDownMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> disconnectFromOthers(
        es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getDisconnectFromOthersMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> registerPendingObjects(
        es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getRegisterPendingObjectsMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Paraver
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> cleanCaches(
        es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getCleanCachesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> activateTracing(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ActivateTracingRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getActivateTracingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> deactivateTracing(
        es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getDeactivateTracingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse> getTraces(
        es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getGetTracesMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Garbage collection
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> closeSessionInDS(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.CloseSessionInDSRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCloseSessionInDSMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> updateRefs(
        es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateRefsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUpdateRefsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetRetainedReferencesResponse> getRetainedReferences(
        es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getGetRetainedReferencesMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_INIT_BACKEND_ID = 0;
  private static final int METHODID_ASSOCIATE_EXECUTION_ENVIRONMENT = 1;
  private static final int METHODID_DEPLOY_META_CLASSES = 2;
  private static final int METHODID_DEPLOY_CLASSES = 3;
  private static final int METHODID_ENRICH_CLASS = 4;
  private static final int METHODID_NEW_PERSISTENT_INSTANCE = 5;
  private static final int METHODID_STORE_OBJECTS = 6;
  private static final int METHODID_GET_COPY_OF_OBJECT = 7;
  private static final int METHODID_UPDATE_OBJECT = 8;
  private static final int METHODID_GET_OBJECTS = 9;
  private static final int METHODID_NEW_VERSION = 10;
  private static final int METHODID_CONSOLIDATE_VERSION = 11;
  private static final int METHODID_UPSERT_OBJECTS = 12;
  private static final int METHODID_NEW_REPLICA = 13;
  private static final int METHODID_MOVE_OBJECTS = 14;
  private static final int METHODID_REMOVE_OBJECTS = 15;
  private static final int METHODID_MIGRATE_OBJECTS_TO_BACKENDS = 16;
  private static final int METHODID_GET_CLASS_IDFROM_OBJECT_IN_MEMORY = 17;
  private static final int METHODID_EXECUTE_IMPLEMENTATION = 18;
  private static final int METHODID_MAKE_PERSISTENT = 19;
  private static final int METHODID_FEDERATE = 20;
  private static final int METHODID_UNFEDERATE = 21;
  private static final int METHODID_NOTIFY_FEDERATION = 22;
  private static final int METHODID_NOTIFY_UNFEDERATION = 23;
  private static final int METHODID_EXISTS = 24;
  private static final int METHODID_SYNCHRONIZE = 25;
  private static final int METHODID_STORE_TO_DB = 26;
  private static final int METHODID_GET_FROM_DB = 27;
  private static final int METHODID_UPDATE_TO_DB = 28;
  private static final int METHODID_DELETE_TO_DB = 29;
  private static final int METHODID_EXISTS_IN_DB = 30;
  private static final int METHODID_CLEAN_EXECUTION_CLASS_DIRECTORY = 31;
  private static final int METHODID_CLOSE_DB_HANDLER = 32;
  private static final int METHODID_SHUT_DOWN = 33;
  private static final int METHODID_DISCONNECT_FROM_OTHERS = 34;
  private static final int METHODID_REGISTER_PENDING_OBJECTS = 35;
  private static final int METHODID_CLEAN_CACHES = 36;
  private static final int METHODID_ACTIVATE_TRACING = 37;
  private static final int METHODID_DEACTIVATE_TRACING = 38;
  private static final int METHODID_GET_TRACES = 39;
  private static final int METHODID_CLOSE_SESSION_IN_DS = 40;
  private static final int METHODID_UPDATE_REFS = 41;
  private static final int METHODID_GET_RETAINED_REFERENCES = 42;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final DataServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DataServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_INIT_BACKEND_ID:
          serviceImpl.initBackendID((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.InitBackendIDRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_ASSOCIATE_EXECUTION_ENVIRONMENT:
          serviceImpl.associateExecutionEnvironment((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.AssociateExecutionEnvironmentRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_DEPLOY_META_CLASSES:
          serviceImpl.deployMetaClasses((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployMetaClassesRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_DEPLOY_CLASSES:
          serviceImpl.deployClasses((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeployClassesRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_ENRICH_CLASS:
          serviceImpl.enrichClass((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.EnrichClassRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_NEW_PERSISTENT_INSTANCE:
          serviceImpl.newPersistentInstance((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewPersistentInstanceResponse>) responseObserver);
          break;
        case METHODID_STORE_OBJECTS:
          serviceImpl.storeObjects((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreObjectsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_GET_COPY_OF_OBJECT:
          serviceImpl.getCopyOfObject((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetCopyOfObjectResponse>) responseObserver);
          break;
        case METHODID_UPDATE_OBJECT:
          serviceImpl.updateObject((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateObjectRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_GET_OBJECTS:
          serviceImpl.getObjects((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetObjectsResponse>) responseObserver);
          break;
        case METHODID_NEW_VERSION:
          serviceImpl.newVersion((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewVersionResponse>) responseObserver);
          break;
        case METHODID_CONSOLIDATE_VERSION:
          serviceImpl.consolidateVersion((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ConsolidateVersionRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_UPSERT_OBJECTS:
          serviceImpl.upsertObjects((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpsertObjectsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_NEW_REPLICA:
          serviceImpl.newReplica((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NewReplicaResponse>) responseObserver);
          break;
        case METHODID_MOVE_OBJECTS:
          serviceImpl.moveObjects((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MoveObjectsResponse>) responseObserver);
          break;
        case METHODID_REMOVE_OBJECTS:
          serviceImpl.removeObjects((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.RemoveObjectsResponse>) responseObserver);
          break;
        case METHODID_MIGRATE_OBJECTS_TO_BACKENDS:
          serviceImpl.migrateObjectsToBackends((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MigrateObjectsResponse>) responseObserver);
          break;
        case METHODID_GET_CLASS_IDFROM_OBJECT_IN_MEMORY:
          serviceImpl.getClassIDFromObjectInMemory((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetClassIDFromObjectInMemoryResponse>) responseObserver);
          break;
        case METHODID_EXECUTE_IMPLEMENTATION:
          serviceImpl.executeImplementation((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExecuteImplementationResponse>) responseObserver);
          break;
        case METHODID_MAKE_PERSISTENT:
          serviceImpl.makePersistent((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.MakePersistentRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_FEDERATE:
          serviceImpl.federate((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.FederateRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_UNFEDERATE:
          serviceImpl.unfederate((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UnfederateRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_NOTIFY_FEDERATION:
          serviceImpl.notifyFederation((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyFederationRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_NOTIFY_UNFEDERATION:
          serviceImpl.notifyUnfederation((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.NotifyUnfederationRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_EXISTS:
          serviceImpl.exists((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsResponse>) responseObserver);
          break;
        case METHODID_SYNCHRONIZE:
          serviceImpl.synchronize((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.SynchronizeRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_STORE_TO_DB:
          serviceImpl.storeToDB((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.StoreToDBRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_GET_FROM_DB:
          serviceImpl.getFromDB((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetFromDBResponse>) responseObserver);
          break;
        case METHODID_UPDATE_TO_DB:
          serviceImpl.updateToDB((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateToDBRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_DELETE_TO_DB:
          serviceImpl.deleteToDB((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.DeleteToDBRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_EXISTS_IN_DB:
          serviceImpl.existsInDB((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ExistsInDBResponse>) responseObserver);
          break;
        case METHODID_CLEAN_EXECUTION_CLASS_DIRECTORY:
          serviceImpl.cleanExecutionClassDirectory((es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_CLOSE_DB_HANDLER:
          serviceImpl.closeDbHandler((es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_SHUT_DOWN:
          serviceImpl.shutDown((es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_DISCONNECT_FROM_OTHERS:
          serviceImpl.disconnectFromOthers((es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_REGISTER_PENDING_OBJECTS:
          serviceImpl.registerPendingObjects((es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_CLEAN_CACHES:
          serviceImpl.cleanCaches((es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_ACTIVATE_TRACING:
          serviceImpl.activateTracing((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.ActivateTracingRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_DEACTIVATE_TRACING:
          serviceImpl.deactivateTracing((es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_GET_TRACES:
          serviceImpl.getTraces((es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse>) responseObserver);
          break;
        case METHODID_CLOSE_SESSION_IN_DS:
          serviceImpl.closeSessionInDS((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.CloseSessionInDSRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_UPDATE_REFS:
          serviceImpl.updateRefs((es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.UpdateRefsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_GET_RETAINED_REFERENCES:
          serviceImpl.getRetainedReferences((es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.dataservice.DataserviceMessages.GetRetainedReferencesResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class DataServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DataServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return es.bsc.dataclay.communication.grpc.generated.dataservice.DataServiceGrpcService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DataService");
    }
  }

  private static final class DataServiceFileDescriptorSupplier
      extends DataServiceBaseDescriptorSupplier {
    DataServiceFileDescriptorSupplier() {}
  }

  private static final class DataServiceMethodDescriptorSupplier
      extends DataServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    DataServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (DataServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DataServiceFileDescriptorSupplier())
              .addMethod(getInitBackendIDMethod())
              .addMethod(getAssociateExecutionEnvironmentMethod())
              .addMethod(getDeployMetaClassesMethod())
              .addMethod(getDeployClassesMethod())
              .addMethod(getEnrichClassMethod())
              .addMethod(getNewPersistentInstanceMethod())
              .addMethod(getStoreObjectsMethod())
              .addMethod(getGetCopyOfObjectMethod())
              .addMethod(getUpdateObjectMethod())
              .addMethod(getGetObjectsMethod())
              .addMethod(getNewVersionMethod())
              .addMethod(getConsolidateVersionMethod())
              .addMethod(getUpsertObjectsMethod())
              .addMethod(getNewReplicaMethod())
              .addMethod(getMoveObjectsMethod())
              .addMethod(getRemoveObjectsMethod())
              .addMethod(getMigrateObjectsToBackendsMethod())
              .addMethod(getGetClassIDFromObjectInMemoryMethod())
              .addMethod(getExecuteImplementationMethod())
              .addMethod(getMakePersistentMethod())
              .addMethod(getFederateMethod())
              .addMethod(getUnfederateMethod())
              .addMethod(getNotifyFederationMethod())
              .addMethod(getNotifyUnfederationMethod())
              .addMethod(getExistsMethod())
              .addMethod(getSynchronizeMethod())
              .addMethod(getStoreToDBMethod())
              .addMethod(getGetFromDBMethod())
              .addMethod(getUpdateToDBMethod())
              .addMethod(getDeleteToDBMethod())
              .addMethod(getExistsInDBMethod())
              .addMethod(getCleanExecutionClassDirectoryMethod())
              .addMethod(getCloseDbHandlerMethod())
              .addMethod(getShutDownMethod())
              .addMethod(getDisconnectFromOthersMethod())
              .addMethod(getRegisterPendingObjectsMethod())
              .addMethod(getCleanCachesMethod())
              .addMethod(getActivateTracingMethod())
              .addMethod(getDeactivateTracingMethod())
              .addMethod(getGetTracesMethod())
              .addMethod(getCloseSessionInDSMethod())
              .addMethod(getUpdateRefsMethod())
              .addMethod(getGetRetainedReferencesMethod())
              .build();
        }
      }
    }
    return result;
  }
}
