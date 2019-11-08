package es.bsc.dataclay.communication.grpc.generated.logicmodule;

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
    comments = "Source: dataclay/communication/grpc/generated/logicmodule/logicmodule.proto")
public final class LogicModuleGrpc {

  private LogicModuleGrpc() {}

  public static final String SERVICE_NAME = "dataclay.communication.grpc.logicmodule.LogicModule";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterSLRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getAutoregisterSLMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "autoregisterSL",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterSLRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterSLRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getAutoregisterSLMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterSLRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getAutoregisterSLMethod;
    if ((getAutoregisterSLMethod = LogicModuleGrpc.getAutoregisterSLMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getAutoregisterSLMethod = LogicModuleGrpc.getAutoregisterSLMethod) == null) {
          LogicModuleGrpc.getAutoregisterSLMethod = getAutoregisterSLMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterSLRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "autoregisterSL"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterSLRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("autoregisterSL"))
              .build();
        }
      }
    }
    return getAutoregisterSLMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEERequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEEResponse> getAutoregisterEEMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "autoregisterEE",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEERequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEEResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEERequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEEResponse> getAutoregisterEEMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEERequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEEResponse> getAutoregisterEEMethod;
    if ((getAutoregisterEEMethod = LogicModuleGrpc.getAutoregisterEEMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getAutoregisterEEMethod = LogicModuleGrpc.getAutoregisterEEMethod) == null) {
          LogicModuleGrpc.getAutoregisterEEMethod = getAutoregisterEEMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEERequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEEResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "autoregisterEE"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEERequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEEResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("autoregisterEE"))
              .build();
        }
      }
    }
    return getAutoregisterEEMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterStorageLocationRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnregisterStorageLocationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "unregisterStorageLocation",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterStorageLocationRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterStorageLocationRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnregisterStorageLocationMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterStorageLocationRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnregisterStorageLocationMethod;
    if ((getUnregisterStorageLocationMethod = LogicModuleGrpc.getUnregisterStorageLocationMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getUnregisterStorageLocationMethod = LogicModuleGrpc.getUnregisterStorageLocationMethod) == null) {
          LogicModuleGrpc.getUnregisterStorageLocationMethod = getUnregisterStorageLocationMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterStorageLocationRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "unregisterStorageLocation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterStorageLocationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("unregisterStorageLocation"))
              .build();
        }
      }
    }
    return getUnregisterStorageLocationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterExecutionEnvironmentRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnregisterExecutionEnvironmentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "unregisterExecutionEnvironment",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterExecutionEnvironmentRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterExecutionEnvironmentRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnregisterExecutionEnvironmentMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterExecutionEnvironmentRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnregisterExecutionEnvironmentMethod;
    if ((getUnregisterExecutionEnvironmentMethod = LogicModuleGrpc.getUnregisterExecutionEnvironmentMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getUnregisterExecutionEnvironmentMethod = LogicModuleGrpc.getUnregisterExecutionEnvironmentMethod) == null) {
          LogicModuleGrpc.getUnregisterExecutionEnvironmentMethod = getUnregisterExecutionEnvironmentMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterExecutionEnvironmentRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "unregisterExecutionEnvironment"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterExecutionEnvironmentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("unregisterExecutionEnvironment"))
              .build();
        }
      }
    }
    return getUnregisterExecutionEnvironmentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCheckAliveMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "checkAlive",
      requestType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCheckAliveMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCheckAliveMethod;
    if ((getCheckAliveMethod = LogicModuleGrpc.getCheckAliveMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getCheckAliveMethod = LogicModuleGrpc.getCheckAliveMethod) == null) {
          LogicModuleGrpc.getCheckAliveMethod = getCheckAliveMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "checkAlive"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("checkAlive"))
              .build();
        }
      }
    }
    return getCheckAliveMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsResponse> getPerformSetOfNewAccountsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "performSetOfNewAccounts",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsResponse> getPerformSetOfNewAccountsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsResponse> getPerformSetOfNewAccountsMethod;
    if ((getPerformSetOfNewAccountsMethod = LogicModuleGrpc.getPerformSetOfNewAccountsMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getPerformSetOfNewAccountsMethod = LogicModuleGrpc.getPerformSetOfNewAccountsMethod) == null) {
          LogicModuleGrpc.getPerformSetOfNewAccountsMethod = getPerformSetOfNewAccountsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "performSetOfNewAccounts"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("performSetOfNewAccounts"))
              .build();
        }
      }
    }
    return getPerformSetOfNewAccountsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsResponse> getPerformSetOfOperationsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "performSetOfOperations",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsResponse> getPerformSetOfOperationsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsResponse> getPerformSetOfOperationsMethod;
    if ((getPerformSetOfOperationsMethod = LogicModuleGrpc.getPerformSetOfOperationsMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getPerformSetOfOperationsMethod = LogicModuleGrpc.getPerformSetOfOperationsMethod) == null) {
          LogicModuleGrpc.getPerformSetOfOperationsMethod = getPerformSetOfOperationsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "performSetOfOperations"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("performSetOfOperations"))
              .build();
        }
      }
    }
    return getPerformSetOfOperationsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PublishAddressRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getPublishAddressMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "publishAddress",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PublishAddressRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PublishAddressRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getPublishAddressMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PublishAddressRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getPublishAddressMethod;
    if ((getPublishAddressMethod = LogicModuleGrpc.getPublishAddressMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getPublishAddressMethod = LogicModuleGrpc.getPublishAddressMethod) == null) {
          LogicModuleGrpc.getPublishAddressMethod = getPublishAddressMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PublishAddressRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "publishAddress"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PublishAddressRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("publishAddress"))
              .build();
        }
      }
    }
    return getPublishAddressMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountNoAdminRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse> getNewAccountNoAdminMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "newAccountNoAdmin",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountNoAdminRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountNoAdminRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse> getNewAccountNoAdminMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountNoAdminRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse> getNewAccountNoAdminMethod;
    if ((getNewAccountNoAdminMethod = LogicModuleGrpc.getNewAccountNoAdminMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getNewAccountNoAdminMethod = LogicModuleGrpc.getNewAccountNoAdminMethod) == null) {
          LogicModuleGrpc.getNewAccountNoAdminMethod = getNewAccountNoAdminMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountNoAdminRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "newAccountNoAdmin"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountNoAdminRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("newAccountNoAdmin"))
              .build();
        }
      }
    }
    return getNewAccountNoAdminMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse> getNewAccountMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "newAccount",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse> getNewAccountMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse> getNewAccountMethod;
    if ((getNewAccountMethod = LogicModuleGrpc.getNewAccountMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getNewAccountMethod = LogicModuleGrpc.getNewAccountMethod) == null) {
          LogicModuleGrpc.getNewAccountMethod = getNewAccountMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "newAccount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("newAccount"))
              .build();
        }
      }
    }
    return getNewAccountMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDResponse> getGetAccountIDMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getAccountID",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDResponse> getGetAccountIDMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDResponse> getGetAccountIDMethod;
    if ((getGetAccountIDMethod = LogicModuleGrpc.getGetAccountIDMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetAccountIDMethod = LogicModuleGrpc.getGetAccountIDMethod) == null) {
          LogicModuleGrpc.getGetAccountIDMethod = getGetAccountIDMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getAccountID"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getAccountID"))
              .build();
        }
      }
    }
    return getGetAccountIDMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListResponse> getGetAccountListMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getAccountList",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListResponse> getGetAccountListMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListResponse> getGetAccountListMethod;
    if ((getGetAccountListMethod = LogicModuleGrpc.getGetAccountListMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetAccountListMethod = LogicModuleGrpc.getGetAccountListMethod) == null) {
          LogicModuleGrpc.getGetAccountListMethod = getGetAccountListMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getAccountList"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getAccountList"))
              .build();
        }
      }
    }
    return getGetAccountListMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionResponse> getNewSessionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "newSession",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionResponse> getNewSessionMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionResponse> getNewSessionMethod;
    if ((getNewSessionMethod = LogicModuleGrpc.getNewSessionMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getNewSessionMethod = LogicModuleGrpc.getNewSessionMethod) == null) {
          LogicModuleGrpc.getNewSessionMethod = getNewSessionMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "newSession"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("newSession"))
              .build();
        }
      }
    }
    return getNewSessionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSResponse> getGetInfoOfSessionForDSMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getInfoOfSessionForDS",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSResponse> getGetInfoOfSessionForDSMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSResponse> getGetInfoOfSessionForDSMethod;
    if ((getGetInfoOfSessionForDSMethod = LogicModuleGrpc.getGetInfoOfSessionForDSMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetInfoOfSessionForDSMethod = LogicModuleGrpc.getGetInfoOfSessionForDSMethod) == null) {
          LogicModuleGrpc.getGetInfoOfSessionForDSMethod = getGetInfoOfSessionForDSMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getInfoOfSessionForDS"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getInfoOfSessionForDS"))
              .build();
        }
      }
    }
    return getGetInfoOfSessionForDSMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceResponse> getNewNamespaceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "newNamespace",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceResponse> getNewNamespaceMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceResponse> getNewNamespaceMethod;
    if ((getNewNamespaceMethod = LogicModuleGrpc.getNewNamespaceMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getNewNamespaceMethod = LogicModuleGrpc.getNewNamespaceMethod) == null) {
          LogicModuleGrpc.getNewNamespaceMethod = getNewNamespaceMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "newNamespace"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("newNamespace"))
              .build();
        }
      }
    }
    return getNewNamespaceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveNamespaceRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveNamespaceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "removeNamespace",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveNamespaceRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveNamespaceRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveNamespaceMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveNamespaceRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveNamespaceMethod;
    if ((getRemoveNamespaceMethod = LogicModuleGrpc.getRemoveNamespaceMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getRemoveNamespaceMethod = LogicModuleGrpc.getRemoveNamespaceMethod) == null) {
          LogicModuleGrpc.getRemoveNamespaceMethod = getRemoveNamespaceMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveNamespaceRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "removeNamespace"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveNamespaceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("removeNamespace"))
              .build();
        }
      }
    }
    return getRemoveNamespaceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDResponse> getGetNamespaceIDMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getNamespaceID",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDResponse> getGetNamespaceIDMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDResponse> getGetNamespaceIDMethod;
    if ((getGetNamespaceIDMethod = LogicModuleGrpc.getGetNamespaceIDMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetNamespaceIDMethod = LogicModuleGrpc.getGetNamespaceIDMethod) == null) {
          LogicModuleGrpc.getGetNamespaceIDMethod = getGetNamespaceIDMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getNamespaceID"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getNamespaceID"))
              .build();
        }
      }
    }
    return getGetNamespaceIDMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangResponse> getGetNamespaceLangMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getNamespaceLang",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangResponse> getGetNamespaceLangMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangResponse> getGetNamespaceLangMethod;
    if ((getGetNamespaceLangMethod = LogicModuleGrpc.getGetNamespaceLangMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetNamespaceLangMethod = LogicModuleGrpc.getGetNamespaceLangMethod) == null) {
          LogicModuleGrpc.getGetNamespaceLangMethod = getGetNamespaceLangMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getNamespaceLang"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getNamespaceLang"))
              .build();
        }
      }
    }
    return getGetNamespaceLangMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDResponse> getGetObjectDataSetIDMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getObjectDataSetID",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDResponse> getGetObjectDataSetIDMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDResponse> getGetObjectDataSetIDMethod;
    if ((getGetObjectDataSetIDMethod = LogicModuleGrpc.getGetObjectDataSetIDMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetObjectDataSetIDMethod = LogicModuleGrpc.getGetObjectDataSetIDMethod) == null) {
          LogicModuleGrpc.getGetObjectDataSetIDMethod = getGetObjectDataSetIDMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getObjectDataSetID"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getObjectDataSetID"))
              .build();
        }
      }
    }
    return getGetObjectDataSetIDMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportInterfaceRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getImportInterfaceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "importInterface",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportInterfaceRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportInterfaceRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getImportInterfaceMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportInterfaceRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getImportInterfaceMethod;
    if ((getImportInterfaceMethod = LogicModuleGrpc.getImportInterfaceMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getImportInterfaceMethod = LogicModuleGrpc.getImportInterfaceMethod) == null) {
          LogicModuleGrpc.getImportInterfaceMethod = getImportInterfaceMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportInterfaceRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "importInterface"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportInterfaceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("importInterface"))
              .build();
        }
      }
    }
    return getImportInterfaceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportContractRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getImportContractMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "importContract",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportContractRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportContractRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getImportContractMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportContractRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getImportContractMethod;
    if ((getImportContractMethod = LogicModuleGrpc.getImportContractMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getImportContractMethod = LogicModuleGrpc.getImportContractMethod) == null) {
          LogicModuleGrpc.getImportContractMethod = getImportContractMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportContractRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "importContract"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportContractRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("importContract"))
              .build();
        }
      }
    }
    return getImportContractMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceResponse> getGetInfoOfClassesInNamespaceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getInfoOfClassesInNamespace",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceResponse> getGetInfoOfClassesInNamespaceMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceResponse> getGetInfoOfClassesInNamespaceMethod;
    if ((getGetInfoOfClassesInNamespaceMethod = LogicModuleGrpc.getGetInfoOfClassesInNamespaceMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetInfoOfClassesInNamespaceMethod = LogicModuleGrpc.getGetInfoOfClassesInNamespaceMethod) == null) {
          LogicModuleGrpc.getGetInfoOfClassesInNamespaceMethod = getGetInfoOfClassesInNamespaceMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getInfoOfClassesInNamespace"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getInfoOfClassesInNamespace"))
              .build();
        }
      }
    }
    return getGetInfoOfClassesInNamespaceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceResponse> getGetImportedClassesInfoInNamespaceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getImportedClassesInfoInNamespace",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceResponse> getGetImportedClassesInfoInNamespaceMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceResponse> getGetImportedClassesInfoInNamespaceMethod;
    if ((getGetImportedClassesInfoInNamespaceMethod = LogicModuleGrpc.getGetImportedClassesInfoInNamespaceMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetImportedClassesInfoInNamespaceMethod = LogicModuleGrpc.getGetImportedClassesInfoInNamespaceMethod) == null) {
          LogicModuleGrpc.getGetImportedClassesInfoInNamespaceMethod = getGetImportedClassesInfoInNamespaceMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getImportedClassesInfoInNamespace"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getImportedClassesInfoInNamespace"))
              .build();
        }
      }
    }
    return getGetImportedClassesInfoInNamespaceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportResponse> getGetClassIDfromImportMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getClassIDfromImport",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportResponse> getGetClassIDfromImportMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportResponse> getGetClassIDfromImportMethod;
    if ((getGetClassIDfromImportMethod = LogicModuleGrpc.getGetClassIDfromImportMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetClassIDfromImportMethod = LogicModuleGrpc.getGetClassIDfromImportMethod) == null) {
          LogicModuleGrpc.getGetClassIDfromImportMethod = getGetClassIDfromImportMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getClassIDfromImport"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getClassIDfromImport"))
              .build();
        }
      }
    }
    return getGetClassIDfromImportMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesResponse> getGetNamespacesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getNamespaces",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesResponse> getGetNamespacesMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesResponse> getGetNamespacesMethod;
    if ((getGetNamespacesMethod = LogicModuleGrpc.getGetNamespacesMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetNamespacesMethod = LogicModuleGrpc.getGetNamespacesMethod) == null) {
          LogicModuleGrpc.getGetNamespacesMethod = getGetNamespacesMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getNamespaces"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getNamespaces"))
              .build();
        }
      }
    }
    return getGetNamespacesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetResponse> getNewDataSetMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "newDataSet",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetResponse> getNewDataSetMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetResponse> getNewDataSetMethod;
    if ((getNewDataSetMethod = LogicModuleGrpc.getNewDataSetMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getNewDataSetMethod = LogicModuleGrpc.getNewDataSetMethod) == null) {
          LogicModuleGrpc.getNewDataSetMethod = getNewDataSetMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "newDataSet"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("newDataSet"))
              .build();
        }
      }
    }
    return getNewDataSetMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveDataSetRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveDataSetMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "removeDataSet",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveDataSetRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveDataSetRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveDataSetMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveDataSetRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveDataSetMethod;
    if ((getRemoveDataSetMethod = LogicModuleGrpc.getRemoveDataSetMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getRemoveDataSetMethod = LogicModuleGrpc.getRemoveDataSetMethod) == null) {
          LogicModuleGrpc.getRemoveDataSetMethod = getRemoveDataSetMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveDataSetRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "removeDataSet"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveDataSetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("removeDataSet"))
              .build();
        }
      }
    }
    return getRemoveDataSetMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDResponse> getGetDataSetIDMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getDataSetID",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDResponse> getGetDataSetIDMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDResponse> getGetDataSetIDMethod;
    if ((getGetDataSetIDMethod = LogicModuleGrpc.getGetDataSetIDMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetDataSetIDMethod = LogicModuleGrpc.getGetDataSetIDMethod) == null) {
          LogicModuleGrpc.getGetDataSetIDMethod = getGetDataSetIDMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getDataSetID"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getDataSetID"))
              .build();
        }
      }
    }
    return getGetDataSetIDMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicResponse> getCheckDataSetIsPublicMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "checkDataSetIsPublic",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicResponse> getCheckDataSetIsPublicMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicResponse> getCheckDataSetIsPublicMethod;
    if ((getCheckDataSetIsPublicMethod = LogicModuleGrpc.getCheckDataSetIsPublicMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getCheckDataSetIsPublicMethod = LogicModuleGrpc.getCheckDataSetIsPublicMethod) == null) {
          LogicModuleGrpc.getCheckDataSetIsPublicMethod = getCheckDataSetIsPublicMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "checkDataSetIsPublic"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("checkDataSetIsPublic"))
              .build();
        }
      }
    }
    return getCheckDataSetIsPublicMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsResponse> getGetPublicDataSetsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getPublicDataSets",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsResponse> getGetPublicDataSetsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsResponse> getGetPublicDataSetsMethod;
    if ((getGetPublicDataSetsMethod = LogicModuleGrpc.getGetPublicDataSetsMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetPublicDataSetsMethod = LogicModuleGrpc.getGetPublicDataSetsMethod) == null) {
          LogicModuleGrpc.getGetPublicDataSetsMethod = getGetPublicDataSetsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getPublicDataSets"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getPublicDataSets"))
              .build();
        }
      }
    }
    return getGetPublicDataSetsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsResponse> getGetAccountDataSetsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getAccountDataSets",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsResponse> getGetAccountDataSetsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsResponse> getGetAccountDataSetsMethod;
    if ((getGetAccountDataSetsMethod = LogicModuleGrpc.getGetAccountDataSetsMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetAccountDataSetsMethod = LogicModuleGrpc.getGetAccountDataSetsMethod) == null) {
          LogicModuleGrpc.getGetAccountDataSetsMethod = getGetAccountDataSetsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getAccountDataSets"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getAccountDataSets"))
              .build();
        }
      }
    }
    return getGetAccountDataSetsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassResponse> getNewClassMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "newClass",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassResponse> getNewClassMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassResponse> getNewClassMethod;
    if ((getNewClassMethod = LogicModuleGrpc.getNewClassMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getNewClassMethod = LogicModuleGrpc.getNewClassMethod) == null) {
          LogicModuleGrpc.getNewClassMethod = getNewClassMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "newClass"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("newClass"))
              .build();
        }
      }
    }
    return getNewClassMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDResponse> getNewClassIDMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "newClassID",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDResponse> getNewClassIDMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDResponse> getNewClassIDMethod;
    if ((getNewClassIDMethod = LogicModuleGrpc.getNewClassIDMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getNewClassIDMethod = LogicModuleGrpc.getNewClassIDMethod) == null) {
          LogicModuleGrpc.getNewClassIDMethod = getNewClassIDMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "newClassID"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("newClassID"))
              .build();
        }
      }
    }
    return getNewClassIDMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveClassRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveClassMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "removeClass",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveClassRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveClassRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveClassMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveClassRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveClassMethod;
    if ((getRemoveClassMethod = LogicModuleGrpc.getRemoveClassMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getRemoveClassMethod = LogicModuleGrpc.getRemoveClassMethod) == null) {
          LogicModuleGrpc.getRemoveClassMethod = getRemoveClassMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveClassRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "removeClass"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveClassRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("removeClass"))
              .build();
        }
      }
    }
    return getRemoveClassMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveOperationRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveOperationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "removeOperation",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveOperationRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveOperationRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveOperationMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveOperationRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveOperationMethod;
    if ((getRemoveOperationMethod = LogicModuleGrpc.getRemoveOperationMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getRemoveOperationMethod = LogicModuleGrpc.getRemoveOperationMethod) == null) {
          LogicModuleGrpc.getRemoveOperationMethod = getRemoveOperationMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveOperationRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "removeOperation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveOperationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("removeOperation"))
              .build();
        }
      }
    }
    return getRemoveOperationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveImplementationRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveImplementationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "removeImplementation",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveImplementationRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveImplementationRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveImplementationMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveImplementationRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveImplementationMethod;
    if ((getRemoveImplementationMethod = LogicModuleGrpc.getRemoveImplementationMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getRemoveImplementationMethod = LogicModuleGrpc.getRemoveImplementationMethod) == null) {
          LogicModuleGrpc.getRemoveImplementationMethod = getRemoveImplementationMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveImplementationRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "removeImplementation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveImplementationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("removeImplementation"))
              .build();
        }
      }
    }
    return getRemoveImplementationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDResponse> getGetOperationIDMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getOperationID",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDResponse> getGetOperationIDMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDResponse> getGetOperationIDMethod;
    if ((getGetOperationIDMethod = LogicModuleGrpc.getGetOperationIDMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetOperationIDMethod = LogicModuleGrpc.getGetOperationIDMethod) == null) {
          LogicModuleGrpc.getGetOperationIDMethod = getGetOperationIDMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getOperationID"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getOperationID"))
              .build();
        }
      }
    }
    return getGetOperationIDMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDResponse> getGetPropertyIDMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getPropertyID",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDResponse> getGetPropertyIDMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDResponse> getGetPropertyIDMethod;
    if ((getGetPropertyIDMethod = LogicModuleGrpc.getGetPropertyIDMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetPropertyIDMethod = LogicModuleGrpc.getGetPropertyIDMethod) == null) {
          LogicModuleGrpc.getGetPropertyIDMethod = getGetPropertyIDMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getPropertyID"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getPropertyID"))
              .build();
        }
      }
    }
    return getGetPropertyIDMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDResponse> getGetClassIDMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getClassID",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDResponse> getGetClassIDMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDResponse> getGetClassIDMethod;
    if ((getGetClassIDMethod = LogicModuleGrpc.getGetClassIDMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetClassIDMethod = LogicModuleGrpc.getGetClassIDMethod) == null) {
          LogicModuleGrpc.getGetClassIDMethod = getGetClassIDMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getClassID"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getClassID"))
              .build();
        }
      }
    }
    return getGetClassIDMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoResponse> getGetClassInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getClassInfo",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoResponse> getGetClassInfoMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoResponse> getGetClassInfoMethod;
    if ((getGetClassInfoMethod = LogicModuleGrpc.getGetClassInfoMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetClassInfoMethod = LogicModuleGrpc.getGetClassInfoMethod) == null) {
          LogicModuleGrpc.getGetClassInfoMethod = getGetClassInfoMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getClassInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getClassInfo"))
              .build();
        }
      }
    }
    return getGetClassInfoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractResponse> getNewContractMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "newContract",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractResponse> getNewContractMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractResponse> getNewContractMethod;
    if ((getNewContractMethod = LogicModuleGrpc.getNewContractMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getNewContractMethod = LogicModuleGrpc.getNewContractMethod) == null) {
          LogicModuleGrpc.getNewContractMethod = getNewContractMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "newContract"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("newContract"))
              .build();
        }
      }
    }
    return getNewContractMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterToPublicContractMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "registerToPublicContract",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterToPublicContractMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterToPublicContractMethod;
    if ((getRegisterToPublicContractMethod = LogicModuleGrpc.getRegisterToPublicContractMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getRegisterToPublicContractMethod = LogicModuleGrpc.getRegisterToPublicContractMethod) == null) {
          LogicModuleGrpc.getRegisterToPublicContractMethod = getRegisterToPublicContractMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "registerToPublicContract"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("registerToPublicContract"))
              .build();
        }
      }
    }
    return getRegisterToPublicContractMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceResponse> getRegisterToPublicContractOfNamespaceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "registerToPublicContractOfNamespace",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceResponse> getRegisterToPublicContractOfNamespaceMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceResponse> getRegisterToPublicContractOfNamespaceMethod;
    if ((getRegisterToPublicContractOfNamespaceMethod = LogicModuleGrpc.getRegisterToPublicContractOfNamespaceMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getRegisterToPublicContractOfNamespaceMethod = LogicModuleGrpc.getRegisterToPublicContractOfNamespaceMethod) == null) {
          LogicModuleGrpc.getRegisterToPublicContractOfNamespaceMethod = getRegisterToPublicContractOfNamespaceMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "registerToPublicContractOfNamespace"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("registerToPublicContractOfNamespace"))
              .build();
        }
      }
    }
    return getRegisterToPublicContractOfNamespaceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantResponse> getGetContractIDsOfApplicantMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getContractIDsOfApplicant",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantResponse> getGetContractIDsOfApplicantMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantResponse> getGetContractIDsOfApplicantMethod;
    if ((getGetContractIDsOfApplicantMethod = LogicModuleGrpc.getGetContractIDsOfApplicantMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetContractIDsOfApplicantMethod = LogicModuleGrpc.getGetContractIDsOfApplicantMethod) == null) {
          LogicModuleGrpc.getGetContractIDsOfApplicantMethod = getGetContractIDsOfApplicantMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getContractIDsOfApplicant"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getContractIDsOfApplicant"))
              .build();
        }
      }
    }
    return getGetContractIDsOfApplicantMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderResponse> getGetContractIDsOfProviderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getContractIDsOfProvider",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderResponse> getGetContractIDsOfProviderMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderResponse> getGetContractIDsOfProviderMethod;
    if ((getGetContractIDsOfProviderMethod = LogicModuleGrpc.getGetContractIDsOfProviderMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetContractIDsOfProviderMethod = LogicModuleGrpc.getGetContractIDsOfProviderMethod) == null) {
          LogicModuleGrpc.getGetContractIDsOfProviderMethod = getGetContractIDsOfProviderMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getContractIDsOfProvider"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getContractIDsOfProvider"))
              .build();
        }
      }
    }
    return getGetContractIDsOfProviderMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvResponse> getGetContractIDsOfApplicantWithProviderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getContractIDsOfApplicantWithProvider",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvResponse> getGetContractIDsOfApplicantWithProviderMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvResponse> getGetContractIDsOfApplicantWithProviderMethod;
    if ((getGetContractIDsOfApplicantWithProviderMethod = LogicModuleGrpc.getGetContractIDsOfApplicantWithProviderMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetContractIDsOfApplicantWithProviderMethod = LogicModuleGrpc.getGetContractIDsOfApplicantWithProviderMethod) == null) {
          LogicModuleGrpc.getGetContractIDsOfApplicantWithProviderMethod = getGetContractIDsOfApplicantWithProviderMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getContractIDsOfApplicantWithProvider"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getContractIDsOfApplicantWithProvider"))
              .build();
        }
      }
    }
    return getGetContractIDsOfApplicantWithProviderMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractResponse> getNewDataContractMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "newDataContract",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractResponse> getNewDataContractMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractResponse> getNewDataContractMethod;
    if ((getNewDataContractMethod = LogicModuleGrpc.getNewDataContractMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getNewDataContractMethod = LogicModuleGrpc.getNewDataContractMethod) == null) {
          LogicModuleGrpc.getNewDataContractMethod = getNewDataContractMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "newDataContract"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("newDataContract"))
              .build();
        }
      }
    }
    return getNewDataContractMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicDataContractRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterToPublicDataContractMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "registerToPublicDataContract",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicDataContractRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicDataContractRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterToPublicDataContractMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicDataContractRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterToPublicDataContractMethod;
    if ((getRegisterToPublicDataContractMethod = LogicModuleGrpc.getRegisterToPublicDataContractMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getRegisterToPublicDataContractMethod = LogicModuleGrpc.getRegisterToPublicDataContractMethod) == null) {
          LogicModuleGrpc.getRegisterToPublicDataContractMethod = getRegisterToPublicDataContractMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicDataContractRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "registerToPublicDataContract"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicDataContractRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("registerToPublicDataContract"))
              .build();
        }
      }
    }
    return getRegisterToPublicDataContractMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantResponse> getGetDataContractIDsOfApplicantMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getDataContractIDsOfApplicant",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantResponse> getGetDataContractIDsOfApplicantMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantResponse> getGetDataContractIDsOfApplicantMethod;
    if ((getGetDataContractIDsOfApplicantMethod = LogicModuleGrpc.getGetDataContractIDsOfApplicantMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetDataContractIDsOfApplicantMethod = LogicModuleGrpc.getGetDataContractIDsOfApplicantMethod) == null) {
          LogicModuleGrpc.getGetDataContractIDsOfApplicantMethod = getGetDataContractIDsOfApplicantMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getDataContractIDsOfApplicant"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getDataContractIDsOfApplicant"))
              .build();
        }
      }
    }
    return getGetDataContractIDsOfApplicantMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderResponse> getGetDataContractIDsOfProviderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getDataContractIDsOfProvider",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderResponse> getGetDataContractIDsOfProviderMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderResponse> getGetDataContractIDsOfProviderMethod;
    if ((getGetDataContractIDsOfProviderMethod = LogicModuleGrpc.getGetDataContractIDsOfProviderMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetDataContractIDsOfProviderMethod = LogicModuleGrpc.getGetDataContractIDsOfProviderMethod) == null) {
          LogicModuleGrpc.getGetDataContractIDsOfProviderMethod = getGetDataContractIDsOfProviderMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getDataContractIDsOfProvider"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getDataContractIDsOfProvider"))
              .build();
        }
      }
    }
    return getGetDataContractIDsOfProviderMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvResponse> getGetDataContractInfoOfApplicantWithProviderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getDataContractInfoOfApplicantWithProvider",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvResponse> getGetDataContractInfoOfApplicantWithProviderMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvResponse> getGetDataContractInfoOfApplicantWithProviderMethod;
    if ((getGetDataContractInfoOfApplicantWithProviderMethod = LogicModuleGrpc.getGetDataContractInfoOfApplicantWithProviderMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetDataContractInfoOfApplicantWithProviderMethod = LogicModuleGrpc.getGetDataContractInfoOfApplicantWithProviderMethod) == null) {
          LogicModuleGrpc.getGetDataContractInfoOfApplicantWithProviderMethod = getGetDataContractInfoOfApplicantWithProviderMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getDataContractInfoOfApplicantWithProvider"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getDataContractInfoOfApplicantWithProvider"))
              .build();
        }
      }
    }
    return getGetDataContractInfoOfApplicantWithProviderMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceResponse> getNewInterfaceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "newInterface",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceResponse> getNewInterfaceMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceResponse> getNewInterfaceMethod;
    if ((getNewInterfaceMethod = LogicModuleGrpc.getNewInterfaceMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getNewInterfaceMethod = LogicModuleGrpc.getNewInterfaceMethod) == null) {
          LogicModuleGrpc.getNewInterfaceMethod = getNewInterfaceMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "newInterface"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("newInterface"))
              .build();
        }
      }
    }
    return getNewInterfaceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoResponse> getGetInterfaceInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getInterfaceInfo",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoResponse> getGetInterfaceInfoMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoResponse> getGetInterfaceInfoMethod;
    if ((getGetInterfaceInfoMethod = LogicModuleGrpc.getGetInterfaceInfoMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetInterfaceInfoMethod = LogicModuleGrpc.getGetInterfaceInfoMethod) == null) {
          LogicModuleGrpc.getGetInterfaceInfoMethod = getGetInterfaceInfoMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getInterfaceInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getInterfaceInfo"))
              .build();
        }
      }
    }
    return getGetInterfaceInfoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveInterfaceRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveInterfaceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "removeInterface",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveInterfaceRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveInterfaceRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveInterfaceMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveInterfaceRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRemoveInterfaceMethod;
    if ((getRemoveInterfaceMethod = LogicModuleGrpc.getRemoveInterfaceMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getRemoveInterfaceMethod = LogicModuleGrpc.getRemoveInterfaceMethod) == null) {
          LogicModuleGrpc.getRemoveInterfaceMethod = getRemoveInterfaceMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveInterfaceRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "removeInterface"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveInterfaceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("removeInterface"))
              .build();
        }
      }
    }
    return getRemoveInterfaceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoResponse> getGetExecutionEnvironmentsInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getExecutionEnvironmentsInfo",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoResponse> getGetExecutionEnvironmentsInfoMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoResponse> getGetExecutionEnvironmentsInfoMethod;
    if ((getGetExecutionEnvironmentsInfoMethod = LogicModuleGrpc.getGetExecutionEnvironmentsInfoMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetExecutionEnvironmentsInfoMethod = LogicModuleGrpc.getGetExecutionEnvironmentsInfoMethod) == null) {
          LogicModuleGrpc.getGetExecutionEnvironmentsInfoMethod = getGetExecutionEnvironmentsInfoMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getExecutionEnvironmentsInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getExecutionEnvironmentsInfo"))
              .build();
        }
      }
    }
    return getGetExecutionEnvironmentsInfoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesResponse> getGetExecutionEnvironmentsNamesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getExecutionEnvironmentsNames",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesResponse> getGetExecutionEnvironmentsNamesMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesResponse> getGetExecutionEnvironmentsNamesMethod;
    if ((getGetExecutionEnvironmentsNamesMethod = LogicModuleGrpc.getGetExecutionEnvironmentsNamesMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetExecutionEnvironmentsNamesMethod = LogicModuleGrpc.getGetExecutionEnvironmentsNamesMethod) == null) {
          LogicModuleGrpc.getGetExecutionEnvironmentsNamesMethod = getGetExecutionEnvironmentsNamesMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getExecutionEnvironmentsNames"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getExecutionEnvironmentsNames"))
              .build();
        }
      }
    }
    return getGetExecutionEnvironmentsNamesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSResponse> getGetExecutionEnvironmentForDSMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getExecutionEnvironmentForDS",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSResponse> getGetExecutionEnvironmentForDSMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSResponse> getGetExecutionEnvironmentForDSMethod;
    if ((getGetExecutionEnvironmentForDSMethod = LogicModuleGrpc.getGetExecutionEnvironmentForDSMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetExecutionEnvironmentForDSMethod = LogicModuleGrpc.getGetExecutionEnvironmentForDSMethod) == null) {
          LogicModuleGrpc.getGetExecutionEnvironmentForDSMethod = getGetExecutionEnvironmentForDSMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getExecutionEnvironmentForDS"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getExecutionEnvironmentForDS"))
              .build();
        }
      }
    }
    return getGetExecutionEnvironmentForDSMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSResponse> getGetStorageLocationForDSMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getStorageLocationForDS",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSResponse> getGetStorageLocationForDSMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSResponse> getGetStorageLocationForDSMethod;
    if ((getGetStorageLocationForDSMethod = LogicModuleGrpc.getGetStorageLocationForDSMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetStorageLocationForDSMethod = LogicModuleGrpc.getGetStorageLocationForDSMethod) == null) {
          LogicModuleGrpc.getGetStorageLocationForDSMethod = getGetStorageLocationForDSMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getStorageLocationForDS"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getStorageLocationForDS"))
              .build();
        }
      }
    }
    return getGetStorageLocationForDSMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoResponse> getGetObjectInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getObjectInfo",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoResponse> getGetObjectInfoMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoResponse> getGetObjectInfoMethod;
    if ((getGetObjectInfoMethod = LogicModuleGrpc.getGetObjectInfoMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetObjectInfoMethod = LogicModuleGrpc.getGetObjectInfoMethod) == null) {
          LogicModuleGrpc.getGetObjectInfoMethod = getGetObjectInfoMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getObjectInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getObjectInfo"))
              .build();
        }
      }
    }
    return getGetObjectInfoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasResponse> getGetObjectFromAliasMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getObjectFromAlias",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasResponse> getGetObjectFromAliasMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasResponse> getGetObjectFromAliasMethod;
    if ((getGetObjectFromAliasMethod = LogicModuleGrpc.getGetObjectFromAliasMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetObjectFromAliasMethod = LogicModuleGrpc.getGetObjectFromAliasMethod) == null) {
          LogicModuleGrpc.getGetObjectFromAliasMethod = getGetObjectFromAliasMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getObjectFromAlias"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getObjectFromAlias"))
              .build();
        }
      }
    }
    return getGetObjectFromAliasMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.DeleteAliasRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDeleteAliasMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "deleteAlias",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.DeleteAliasRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.DeleteAliasRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDeleteAliasMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.DeleteAliasRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getDeleteAliasMethod;
    if ((getDeleteAliasMethod = LogicModuleGrpc.getDeleteAliasMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getDeleteAliasMethod = LogicModuleGrpc.getDeleteAliasMethod) == null) {
          LogicModuleGrpc.getDeleteAliasMethod = getDeleteAliasMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.DeleteAliasRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "deleteAlias"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.DeleteAliasRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("deleteAlias"))
              .build();
        }
      }
    }
    return getDeleteAliasMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMResponse> getGetObjectsMetaDataInfoOfClassForNMMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getObjectsMetaDataInfoOfClassForNM",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMResponse> getGetObjectsMetaDataInfoOfClassForNMMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMResponse> getGetObjectsMetaDataInfoOfClassForNMMethod;
    if ((getGetObjectsMetaDataInfoOfClassForNMMethod = LogicModuleGrpc.getGetObjectsMetaDataInfoOfClassForNMMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetObjectsMetaDataInfoOfClassForNMMethod = LogicModuleGrpc.getGetObjectsMetaDataInfoOfClassForNMMethod) == null) {
          LogicModuleGrpc.getGetObjectsMetaDataInfoOfClassForNMMethod = getGetObjectsMetaDataInfoOfClassForNMMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getObjectsMetaDataInfoOfClassForNM"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getObjectsMetaDataInfoOfClassForNM"))
              .build();
        }
      }
    }
    return getGetObjectsMetaDataInfoOfClassForNMMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AddAliasRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getAddAliasMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "addAlias",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AddAliasRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AddAliasRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getAddAliasMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AddAliasRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getAddAliasMethod;
    if ((getAddAliasMethod = LogicModuleGrpc.getAddAliasMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getAddAliasMethod = LogicModuleGrpc.getAddAliasMethod) == null) {
          LogicModuleGrpc.getAddAliasMethod = getAddAliasMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AddAliasRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "addAlias"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AddAliasRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("addAlias"))
              .build();
        }
      }
    }
    return getAddAliasMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectForGCRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterObjectFromGCMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "registerObjectFromGC",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectForGCRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectForGCRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterObjectFromGCMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectForGCRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterObjectFromGCMethod;
    if ((getRegisterObjectFromGCMethod = LogicModuleGrpc.getRegisterObjectFromGCMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getRegisterObjectFromGCMethod = LogicModuleGrpc.getRegisterObjectFromGCMethod) == null) {
          LogicModuleGrpc.getRegisterObjectFromGCMethod = getRegisterObjectFromGCMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectForGCRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "registerObjectFromGC"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectForGCRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("registerObjectFromGC"))
              .build();
        }
      }
    }
    return getRegisterObjectFromGCMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnregisterObjectsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "unregisterObjects",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterObjectsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnregisterObjectsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterObjectsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnregisterObjectsMethod;
    if ((getUnregisterObjectsMethod = LogicModuleGrpc.getUnregisterObjectsMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getUnregisterObjectsMethod = LogicModuleGrpc.getUnregisterObjectsMethod) == null) {
          LogicModuleGrpc.getUnregisterObjectsMethod = getUnregisterObjectsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterObjectsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "unregisterObjects"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterObjectsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("unregisterObjects"))
              .build();
        }
      }
    }
    return getUnregisterObjectsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterObjectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "registerObject",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterObjectMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterObjectMethod;
    if ((getRegisterObjectMethod = LogicModuleGrpc.getRegisterObjectMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getRegisterObjectMethod = LogicModuleGrpc.getRegisterObjectMethod) == null) {
          LogicModuleGrpc.getRegisterObjectMethod = getRegisterObjectMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "registerObject"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("registerObject"))
              .build();
        }
      }
    }
    return getRegisterObjectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDFromGarbageCollectorRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSetDataSetIDFromGarbageCollectorMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "setDataSetIDFromGarbageCollector",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDFromGarbageCollectorRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDFromGarbageCollectorRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSetDataSetIDFromGarbageCollectorMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDFromGarbageCollectorRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSetDataSetIDFromGarbageCollectorMethod;
    if ((getSetDataSetIDFromGarbageCollectorMethod = LogicModuleGrpc.getSetDataSetIDFromGarbageCollectorMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getSetDataSetIDFromGarbageCollectorMethod = LogicModuleGrpc.getSetDataSetIDFromGarbageCollectorMethod) == null) {
          LogicModuleGrpc.getSetDataSetIDFromGarbageCollectorMethod = getSetDataSetIDFromGarbageCollectorMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDFromGarbageCollectorRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "setDataSetIDFromGarbageCollector"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDFromGarbageCollectorRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("setDataSetIDFromGarbageCollector"))
              .build();
        }
      }
    }
    return getSetDataSetIDFromGarbageCollectorMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSetDataSetIDMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "setDataSetID",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSetDataSetIDMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSetDataSetIDMethod;
    if ((getSetDataSetIDMethod = LogicModuleGrpc.getSetDataSetIDMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getSetDataSetIDMethod = LogicModuleGrpc.getSetDataSetIDMethod) == null) {
          LogicModuleGrpc.getSetDataSetIDMethod = getSetDataSetIDMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "setDataSetID"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("setDataSetID"))
              .build();
        }
      }
    }
    return getSetDataSetIDMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionResponse> getNewVersionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "newVersion",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionResponse> getNewVersionMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionResponse> getNewVersionMethod;
    if ((getNewVersionMethod = LogicModuleGrpc.getNewVersionMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getNewVersionMethod = LogicModuleGrpc.getNewVersionMethod) == null) {
          LogicModuleGrpc.getNewVersionMethod = getNewVersionMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "newVersion"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("newVersion"))
              .build();
        }
      }
    }
    return getNewVersionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ConsolidateVersionRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getConsolidateVersionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "consolidateVersion",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ConsolidateVersionRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ConsolidateVersionRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getConsolidateVersionMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ConsolidateVersionRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getConsolidateVersionMethod;
    if ((getConsolidateVersionMethod = LogicModuleGrpc.getConsolidateVersionMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getConsolidateVersionMethod = LogicModuleGrpc.getConsolidateVersionMethod) == null) {
          LogicModuleGrpc.getConsolidateVersionMethod = getConsolidateVersionMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ConsolidateVersionRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "consolidateVersion"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ConsolidateVersionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("consolidateVersion"))
              .build();
        }
      }
    }
    return getConsolidateVersionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaResponse> getNewReplicaMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "newReplica",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaResponse> getNewReplicaMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaResponse> getNewReplicaMethod;
    if ((getNewReplicaMethod = LogicModuleGrpc.getNewReplicaMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getNewReplicaMethod = LogicModuleGrpc.getNewReplicaMethod) == null) {
          LogicModuleGrpc.getNewReplicaMethod = getNewReplicaMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "newReplica"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("newReplica"))
              .build();
        }
      }
    }
    return getNewReplicaMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectResponse> getMoveObjectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "moveObject",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectResponse> getMoveObjectMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectResponse> getMoveObjectMethod;
    if ((getMoveObjectMethod = LogicModuleGrpc.getMoveObjectMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getMoveObjectMethod = LogicModuleGrpc.getMoveObjectMethod) == null) {
          LogicModuleGrpc.getMoveObjectMethod = getMoveObjectMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "moveObject"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("moveObject"))
              .build();
        }
      }
    }
    return getMoveObjectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadOnlyRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSetObjectReadOnlyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "setObjectReadOnly",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadOnlyRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadOnlyRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSetObjectReadOnlyMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadOnlyRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSetObjectReadOnlyMethod;
    if ((getSetObjectReadOnlyMethod = LogicModuleGrpc.getSetObjectReadOnlyMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getSetObjectReadOnlyMethod = LogicModuleGrpc.getSetObjectReadOnlyMethod) == null) {
          LogicModuleGrpc.getSetObjectReadOnlyMethod = getSetObjectReadOnlyMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadOnlyRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "setObjectReadOnly"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadOnlyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("setObjectReadOnly"))
              .build();
        }
      }
    }
    return getSetObjectReadOnlyMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadWriteRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSetObjectReadWriteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "setObjectReadWrite",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadWriteRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadWriteRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSetObjectReadWriteMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadWriteRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSetObjectReadWriteMethod;
    if ((getSetObjectReadWriteMethod = LogicModuleGrpc.getSetObjectReadWriteMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getSetObjectReadWriteMethod = LogicModuleGrpc.getSetObjectReadWriteMethod) == null) {
          LogicModuleGrpc.getSetObjectReadWriteMethod = getSetObjectReadWriteMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadWriteRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "setObjectReadWrite"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadWriteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("setObjectReadWrite"))
              .build();
        }
      }
    }
    return getSetObjectReadWriteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDResponse> getGetMetadataByOIDMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getMetadataByOID",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDResponse> getGetMetadataByOIDMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDResponse> getGetMetadataByOIDMethod;
    if ((getGetMetadataByOIDMethod = LogicModuleGrpc.getGetMetadataByOIDMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetMetadataByOIDMethod = LogicModuleGrpc.getGetMetadataByOIDMethod) == null) {
          LogicModuleGrpc.getGetMetadataByOIDMethod = getGetMetadataByOIDMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getMetadataByOID"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getMetadataByOID"))
              .build();
        }
      }
    }
    return getGetMetadataByOIDMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationResponse> getExecuteImplementationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "executeImplementation",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationResponse> getExecuteImplementationMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationResponse> getExecuteImplementationMethod;
    if ((getExecuteImplementationMethod = LogicModuleGrpc.getExecuteImplementationMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getExecuteImplementationMethod = LogicModuleGrpc.getExecuteImplementationMethod) == null) {
          LogicModuleGrpc.getExecuteImplementationMethod = getExecuteImplementationMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "executeImplementation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("executeImplementation"))
              .build();
        }
      }
    }
    return getExecuteImplementationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetResponse> getExecuteMethodOnTargetMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "executeMethodOnTarget",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetResponse> getExecuteMethodOnTargetMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetResponse> getExecuteMethodOnTargetMethod;
    if ((getExecuteMethodOnTargetMethod = LogicModuleGrpc.getExecuteMethodOnTargetMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getExecuteMethodOnTargetMethod = LogicModuleGrpc.getExecuteMethodOnTargetMethod) == null) {
          LogicModuleGrpc.getExecuteMethodOnTargetMethod = getExecuteMethodOnTargetMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "executeMethodOnTarget"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("executeMethodOnTarget"))
              .build();
        }
      }
    }
    return getExecuteMethodOnTargetMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SynchronizeFederatedObjectRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSynchronizeFederatedObjectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "synchronizeFederatedObject",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SynchronizeFederatedObjectRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SynchronizeFederatedObjectRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSynchronizeFederatedObjectMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SynchronizeFederatedObjectRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getSynchronizeFederatedObjectMethod;
    if ((getSynchronizeFederatedObjectMethod = LogicModuleGrpc.getSynchronizeFederatedObjectMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getSynchronizeFederatedObjectMethod = LogicModuleGrpc.getSynchronizeFederatedObjectMethod) == null) {
          LogicModuleGrpc.getSynchronizeFederatedObjectMethod = getSynchronizeFederatedObjectMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SynchronizeFederatedObjectRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "synchronizeFederatedObject"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SynchronizeFederatedObjectRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("synchronizeFederatedObject"))
              .build();
        }
      }
    }
    return getSynchronizeFederatedObjectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClayIDResponse> getGetDataClayIDMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getDataClayID",
      requestType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClayIDResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClayIDResponse> getGetDataClayIDMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClayIDResponse> getGetDataClayIDMethod;
    if ((getGetDataClayIDMethod = LogicModuleGrpc.getGetDataClayIDMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetDataClayIDMethod = LogicModuleGrpc.getGetDataClayIDMethod) == null) {
          LogicModuleGrpc.getGetDataClayIDMethod = getGetDataClayIDMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClayIDResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getDataClayID"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClayIDResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getDataClayID"))
              .build();
        }
      }
    }
    return getGetDataClayIDMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse> getRegisterExternalDataClayMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "registerExternalDataClay",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse> getRegisterExternalDataClayMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse> getRegisterExternalDataClayMethod;
    if ((getRegisterExternalDataClayMethod = LogicModuleGrpc.getRegisterExternalDataClayMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getRegisterExternalDataClayMethod = LogicModuleGrpc.getRegisterExternalDataClayMethod) == null) {
          LogicModuleGrpc.getRegisterExternalDataClayMethod = getRegisterExternalDataClayMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "registerExternalDataClay"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("registerExternalDataClay"))
              .build();
        }
      }
    }
    return getRegisterExternalDataClayMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayOverrideAuthorityRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse> getRegisterExternalDataClayOverrideAuthorityMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "registerExternalDataClayOverrideAuthority",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayOverrideAuthorityRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayOverrideAuthorityRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse> getRegisterExternalDataClayOverrideAuthorityMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayOverrideAuthorityRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse> getRegisterExternalDataClayOverrideAuthorityMethod;
    if ((getRegisterExternalDataClayOverrideAuthorityMethod = LogicModuleGrpc.getRegisterExternalDataClayOverrideAuthorityMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getRegisterExternalDataClayOverrideAuthorityMethod = LogicModuleGrpc.getRegisterExternalDataClayOverrideAuthorityMethod) == null) {
          LogicModuleGrpc.getRegisterExternalDataClayOverrideAuthorityMethod = getRegisterExternalDataClayOverrideAuthorityMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayOverrideAuthorityRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "registerExternalDataClayOverrideAuthority"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayOverrideAuthorityRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("registerExternalDataClayOverrideAuthority"))
              .build();
        }
      }
    }
    return getRegisterExternalDataClayOverrideAuthorityMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayResponse> getNotifyRegistrationOfExternalDataClayMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "notifyRegistrationOfExternalDataClay",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayResponse> getNotifyRegistrationOfExternalDataClayMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayResponse> getNotifyRegistrationOfExternalDataClayMethod;
    if ((getNotifyRegistrationOfExternalDataClayMethod = LogicModuleGrpc.getNotifyRegistrationOfExternalDataClayMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getNotifyRegistrationOfExternalDataClayMethod = LogicModuleGrpc.getNotifyRegistrationOfExternalDataClayMethod) == null) {
          LogicModuleGrpc.getNotifyRegistrationOfExternalDataClayMethod = getNotifyRegistrationOfExternalDataClayMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "notifyRegistrationOfExternalDataClay"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("notifyRegistrationOfExternalDataClay"))
              .build();
        }
      }
    }
    return getNotifyRegistrationOfExternalDataClayMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoResponse> getGetExternalDataClayInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getExternalDataClayInfo",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoResponse> getGetExternalDataClayInfoMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoResponse> getGetExternalDataClayInfoMethod;
    if ((getGetExternalDataClayInfoMethod = LogicModuleGrpc.getGetExternalDataClayInfoMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetExternalDataClayInfoMethod = LogicModuleGrpc.getGetExternalDataClayInfoMethod) == null) {
          LogicModuleGrpc.getGetExternalDataClayInfoMethod = getGetExternalDataClayInfoMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getExternalDataClayInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getExternalDataClayInfo"))
              .build();
        }
      }
    }
    return getGetExternalDataClayInfoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDResponse> getGetExternalDataclayIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getExternalDataclayId",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDResponse> getGetExternalDataclayIdMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDResponse> getGetExternalDataclayIdMethod;
    if ((getGetExternalDataclayIdMethod = LogicModuleGrpc.getGetExternalDataclayIdMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetExternalDataclayIdMethod = LogicModuleGrpc.getGetExternalDataclayIdMethod) == null) {
          LogicModuleGrpc.getGetExternalDataclayIdMethod = getGetExternalDataclayIdMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getExternalDataclayId"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getExternalDataclayId"))
              .build();
        }
      }
    }
    return getGetExternalDataclayIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateObjectRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getFederateObjectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "federateObject",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateObjectRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateObjectRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getFederateObjectMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateObjectRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getFederateObjectMethod;
    if ((getFederateObjectMethod = LogicModuleGrpc.getFederateObjectMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getFederateObjectMethod = LogicModuleGrpc.getFederateObjectMethod) == null) {
          LogicModuleGrpc.getFederateObjectMethod = getFederateObjectMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateObjectRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "federateObject"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateObjectRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("federateObject"))
              .build();
        }
      }
    }
    return getFederateObjectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnfederateObjectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "unfederateObject",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnfederateObjectMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnfederateObjectMethod;
    if ((getUnfederateObjectMethod = LogicModuleGrpc.getUnfederateObjectMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getUnfederateObjectMethod = LogicModuleGrpc.getUnfederateObjectMethod) == null) {
          LogicModuleGrpc.getUnfederateObjectMethod = getUnfederateObjectMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "unfederateObject"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("unfederateObject"))
              .build();
        }
      }
    }
    return getUnfederateObjectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyUnfederatedObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getNotifyUnfederatedObjectsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "notifyUnfederatedObjects",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyUnfederatedObjectsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyUnfederatedObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getNotifyUnfederatedObjectsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyUnfederatedObjectsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getNotifyUnfederatedObjectsMethod;
    if ((getNotifyUnfederatedObjectsMethod = LogicModuleGrpc.getNotifyUnfederatedObjectsMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getNotifyUnfederatedObjectsMethod = LogicModuleGrpc.getNotifyUnfederatedObjectsMethod) == null) {
          LogicModuleGrpc.getNotifyUnfederatedObjectsMethod = getNotifyUnfederatedObjectsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyUnfederatedObjectsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "notifyUnfederatedObjects"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyUnfederatedObjectsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("notifyUnfederatedObjects"))
              .build();
        }
      }
    }
    return getNotifyUnfederatedObjectsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyFederatedObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getNotifyFederatedObjectsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "notifyFederatedObjects",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyFederatedObjectsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyFederatedObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getNotifyFederatedObjectsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyFederatedObjectsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getNotifyFederatedObjectsMethod;
    if ((getNotifyFederatedObjectsMethod = LogicModuleGrpc.getNotifyFederatedObjectsMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getNotifyFederatedObjectsMethod = LogicModuleGrpc.getNotifyFederatedObjectsMethod) == null) {
          LogicModuleGrpc.getNotifyFederatedObjectsMethod = getNotifyFederatedObjectsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyFederatedObjectsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "notifyFederatedObjects"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyFederatedObjectsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("notifyFederatedObjects"))
              .build();
        }
      }
    }
    return getNotifyFederatedObjectsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceResponse> getCheckObjectIsFederatedWithDataClayInstanceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "checkObjectIsFederatedWithDataClayInstance",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceResponse> getCheckObjectIsFederatedWithDataClayInstanceMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceResponse> getCheckObjectIsFederatedWithDataClayInstanceMethod;
    if ((getCheckObjectIsFederatedWithDataClayInstanceMethod = LogicModuleGrpc.getCheckObjectIsFederatedWithDataClayInstanceMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getCheckObjectIsFederatedWithDataClayInstanceMethod = LogicModuleGrpc.getCheckObjectIsFederatedWithDataClayInstanceMethod) == null) {
          LogicModuleGrpc.getCheckObjectIsFederatedWithDataClayInstanceMethod = getCheckObjectIsFederatedWithDataClayInstanceMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "checkObjectIsFederatedWithDataClayInstance"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("checkObjectIsFederatedWithDataClayInstance"))
              .build();
        }
      }
    }
    return getCheckObjectIsFederatedWithDataClayInstanceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithResponse> getGetDataClaysObjectIsFederatedWithMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getDataClaysObjectIsFederatedWith",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithResponse> getGetDataClaysObjectIsFederatedWithMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithResponse> getGetDataClaysObjectIsFederatedWithMethod;
    if ((getGetDataClaysObjectIsFederatedWithMethod = LogicModuleGrpc.getGetDataClaysObjectIsFederatedWithMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetDataClaysObjectIsFederatedWithMethod = LogicModuleGrpc.getGetDataClaysObjectIsFederatedWithMethod) == null) {
          LogicModuleGrpc.getGetDataClaysObjectIsFederatedWithMethod = getGetDataClaysObjectIsFederatedWithMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getDataClaysObjectIsFederatedWith"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getDataClaysObjectIsFederatedWith"))
              .build();
        }
      }
    }
    return getGetDataClaysObjectIsFederatedWithMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectResponse> getGetExternalSourceDataClayOfObjectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getExternalSourceDataClayOfObject",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectResponse> getGetExternalSourceDataClayOfObjectMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectResponse> getGetExternalSourceDataClayOfObjectMethod;
    if ((getGetExternalSourceDataClayOfObjectMethod = LogicModuleGrpc.getGetExternalSourceDataClayOfObjectMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetExternalSourceDataClayOfObjectMethod = LogicModuleGrpc.getGetExternalSourceDataClayOfObjectMethod) == null) {
          LogicModuleGrpc.getGetExternalSourceDataClayOfObjectMethod = getGetExternalSourceDataClayOfObjectMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getExternalSourceDataClayOfObject"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getExternalSourceDataClayOfObject"))
              .build();
        }
      }
    }
    return getGetExternalSourceDataClayOfObjectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnfederateAllObjectsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "unfederateAllObjects",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnfederateAllObjectsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnfederateAllObjectsMethod;
    if ((getUnfederateAllObjectsMethod = LogicModuleGrpc.getUnfederateAllObjectsMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getUnfederateAllObjectsMethod = LogicModuleGrpc.getUnfederateAllObjectsMethod) == null) {
          LogicModuleGrpc.getUnfederateAllObjectsMethod = getUnfederateAllObjectsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "unfederateAllObjects"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("unfederateAllObjects"))
              .build();
        }
      }
    }
    return getUnfederateAllObjectsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsWithAllDCsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnfederateAllObjectsWithAllDCsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "unfederateAllObjectsWithAllDCs",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsWithAllDCsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsWithAllDCsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnfederateAllObjectsWithAllDCsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsWithAllDCsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnfederateAllObjectsWithAllDCsMethod;
    if ((getUnfederateAllObjectsWithAllDCsMethod = LogicModuleGrpc.getUnfederateAllObjectsWithAllDCsMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getUnfederateAllObjectsWithAllDCsMethod = LogicModuleGrpc.getUnfederateAllObjectsWithAllDCsMethod) == null) {
          LogicModuleGrpc.getUnfederateAllObjectsWithAllDCsMethod = getUnfederateAllObjectsWithAllDCsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsWithAllDCsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "unfederateAllObjectsWithAllDCs"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsWithAllDCsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("unfederateAllObjectsWithAllDCs"))
              .build();
        }
      }
    }
    return getUnfederateAllObjectsWithAllDCsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectWithAllDCsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnfederateObjectWithAllDCsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "unfederateObjectWithAllDCs",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectWithAllDCsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectWithAllDCsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnfederateObjectWithAllDCsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectWithAllDCsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getUnfederateObjectWithAllDCsMethod;
    if ((getUnfederateObjectWithAllDCsMethod = LogicModuleGrpc.getUnfederateObjectWithAllDCsMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getUnfederateObjectWithAllDCsMethod = LogicModuleGrpc.getUnfederateObjectWithAllDCsMethod) == null) {
          LogicModuleGrpc.getUnfederateObjectWithAllDCsMethod = getUnfederateObjectWithAllDCsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectWithAllDCsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "unfederateObjectWithAllDCs"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectWithAllDCsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("unfederateObjectWithAllDCs"))
              .build();
        }
      }
    }
    return getUnfederateObjectWithAllDCsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MigrateFederatedObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getMigrateFederatedObjectsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "migrateFederatedObjects",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MigrateFederatedObjectsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MigrateFederatedObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getMigrateFederatedObjectsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MigrateFederatedObjectsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getMigrateFederatedObjectsMethod;
    if ((getMigrateFederatedObjectsMethod = LogicModuleGrpc.getMigrateFederatedObjectsMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getMigrateFederatedObjectsMethod = LogicModuleGrpc.getMigrateFederatedObjectsMethod) == null) {
          LogicModuleGrpc.getMigrateFederatedObjectsMethod = getMigrateFederatedObjectsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MigrateFederatedObjectsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "migrateFederatedObjects"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MigrateFederatedObjectsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("migrateFederatedObjects"))
              .build();
        }
      }
    }
    return getMigrateFederatedObjectsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateAllObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getFederateAllObjectsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "federateAllObjects",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateAllObjectsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateAllObjectsRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getFederateAllObjectsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateAllObjectsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getFederateAllObjectsMethod;
    if ((getFederateAllObjectsMethod = LogicModuleGrpc.getFederateAllObjectsMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getFederateAllObjectsMethod = LogicModuleGrpc.getFederateAllObjectsMethod) == null) {
          LogicModuleGrpc.getFederateAllObjectsMethod = getFederateAllObjectsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateAllObjectsRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "federateAllObjects"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateAllObjectsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("federateAllObjects"))
              .build();
        }
      }
    }
    return getFederateAllObjectsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsResponse> getGetStubsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getStubs",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsResponse> getGetStubsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsResponse> getGetStubsMethod;
    if ((getGetStubsMethod = LogicModuleGrpc.getGetStubsMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetStubsMethod = LogicModuleGrpc.getGetStubsMethod) == null) {
          LogicModuleGrpc.getGetStubsMethod = getGetStubsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getStubs"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getStubs"))
              .build();
        }
      }
    }
    return getGetStubsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsResponse> getGetBabelStubsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getBabelStubs",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsResponse> getGetBabelStubsMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsResponse> getGetBabelStubsMethod;
    if ((getGetBabelStubsMethod = LogicModuleGrpc.getGetBabelStubsMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetBabelStubsMethod = LogicModuleGrpc.getGetBabelStubsMethod) == null) {
          LogicModuleGrpc.getGetBabelStubsMethod = getGetBabelStubsMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getBabelStubs"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getBabelStubs"))
              .build();
        }
      }
    }
    return getGetBabelStubsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterECARequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterECAMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "registerECA",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterECARequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterECARequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterECAMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterECARequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getRegisterECAMethod;
    if ((getRegisterECAMethod = LogicModuleGrpc.getRegisterECAMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getRegisterECAMethod = LogicModuleGrpc.getRegisterECAMethod) == null) {
          LogicModuleGrpc.getRegisterECAMethod = getRegisterECAMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterECARequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "registerECA"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterECARequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("registerECA"))
              .build();
        }
      }
    }
    return getRegisterECAMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AdviseEventRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getAdviseEventMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "adviseEvent",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AdviseEventRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AdviseEventRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getAdviseEventMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AdviseEventRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getAdviseEventMethod;
    if ((getAdviseEventMethod = LogicModuleGrpc.getAdviseEventMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getAdviseEventMethod = LogicModuleGrpc.getAdviseEventMethod) == null) {
          LogicModuleGrpc.getAdviseEventMethod = getAdviseEventMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AdviseEventRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "adviseEvent"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AdviseEventRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("adviseEvent"))
              .build();
        }
      }
    }
    return getAdviseEventMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.IsPrefetchingEnabledResponse> getIsPrefetchingEnabledMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "isPrefetchingEnabled",
      requestType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.IsPrefetchingEnabledResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.IsPrefetchingEnabledResponse> getIsPrefetchingEnabledMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.IsPrefetchingEnabledResponse> getIsPrefetchingEnabledMethod;
    if ((getIsPrefetchingEnabledMethod = LogicModuleGrpc.getIsPrefetchingEnabledMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getIsPrefetchingEnabledMethod = LogicModuleGrpc.getIsPrefetchingEnabledMethod) == null) {
          LogicModuleGrpc.getIsPrefetchingEnabledMethod = getIsPrefetchingEnabledMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.IsPrefetchingEnabledResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "isPrefetchingEnabled"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.IsPrefetchingEnabledResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("isPrefetchingEnabled"))
              .build();
        }
      }
    }
    return getIsPrefetchingEnabledMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSResponse> getGetClassNameForDSMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getClassNameForDS",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSResponse> getGetClassNameForDSMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSResponse> getGetClassNameForDSMethod;
    if ((getGetClassNameForDSMethod = LogicModuleGrpc.getGetClassNameForDSMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetClassNameForDSMethod = LogicModuleGrpc.getGetClassNameForDSMethod) == null) {
          LogicModuleGrpc.getGetClassNameForDSMethod = getGetClassNameForDSMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getClassNameForDS"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getClassNameForDS"))
              .build();
        }
      }
    }
    return getGetClassNameForDSMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSResponse> getGetClassNameAndNamespaceForDSMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getClassNameAndNamespaceForDS",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSResponse> getGetClassNameAndNamespaceForDSMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSResponse> getGetClassNameAndNamespaceForDSMethod;
    if ((getGetClassNameAndNamespaceForDSMethod = LogicModuleGrpc.getGetClassNameAndNamespaceForDSMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetClassNameAndNamespaceForDSMethod = LogicModuleGrpc.getGetClassNameAndNamespaceForDSMethod) == null) {
          LogicModuleGrpc.getGetClassNameAndNamespaceForDSMethod = getGetClassNameAndNamespaceForDSMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getClassNameAndNamespaceForDS"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getClassNameAndNamespaceForDS"))
              .build();
        }
      }
    }
    return getGetClassNameAndNamespaceForDSMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderResponse> getGetContractIDOfDataClayProviderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getContractIDOfDataClayProvider",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderResponse> getGetContractIDOfDataClayProviderMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderResponse> getGetContractIDOfDataClayProviderMethod;
    if ((getGetContractIDOfDataClayProviderMethod = LogicModuleGrpc.getGetContractIDOfDataClayProviderMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetContractIDOfDataClayProviderMethod = LogicModuleGrpc.getGetContractIDOfDataClayProviderMethod) == null) {
          LogicModuleGrpc.getGetContractIDOfDataClayProviderMethod = getGetContractIDOfDataClayProviderMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getContractIDOfDataClayProvider"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getContractIDOfDataClayProvider"))
              .build();
        }
      }
    }
    return getGetContractIDOfDataClayProviderMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayResponse> getObjectExistsInDataClayMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "objectExistsInDataClay",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayResponse> getObjectExistsInDataClayMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayResponse> getObjectExistsInDataClayMethod;
    if ((getObjectExistsInDataClayMethod = LogicModuleGrpc.getObjectExistsInDataClayMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getObjectExistsInDataClayMethod = LogicModuleGrpc.getObjectExistsInDataClayMethod) == null) {
          LogicModuleGrpc.getObjectExistsInDataClayMethod = getObjectExistsInDataClayMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "objectExistsInDataClay"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("objectExistsInDataClay"))
              .build();
        }
      }
    }
    return getObjectExistsInDataClayMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CloseSessionRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCloseSessionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "closeSession",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CloseSessionRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CloseSessionRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCloseSessionMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CloseSessionRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCloseSessionMethod;
    if ((getCloseSessionMethod = LogicModuleGrpc.getCloseSessionMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getCloseSessionMethod = LogicModuleGrpc.getCloseSessionMethod) == null) {
          LogicModuleGrpc.getCloseSessionMethod = getCloseSessionMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CloseSessionRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "closeSession"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CloseSessionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("closeSession"))
              .build();
        }
      }
    }
    return getCloseSessionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSResponse> getGetMetadataByOIDForDSMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getMetadataByOIDForDS",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSRequest,
      es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSResponse> getGetMetadataByOIDForDSMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSResponse> getGetMetadataByOIDForDSMethod;
    if ((getGetMetadataByOIDForDSMethod = LogicModuleGrpc.getGetMetadataByOIDForDSMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetMetadataByOIDForDSMethod = LogicModuleGrpc.getGetMetadataByOIDForDSMethod) == null) {
          LogicModuleGrpc.getGetMetadataByOIDForDSMethod = getGetMetadataByOIDForDSMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSRequest, es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getMetadataByOIDForDS"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getMetadataByOIDForDS"))
              .build();
        }
      }
    }
    return getGetMetadataByOIDForDSMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ActivateTracingRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getActivateTracingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "activateTracing",
      requestType = es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ActivateTracingRequest.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ActivateTracingRequest,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getActivateTracingMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ActivateTracingRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getActivateTracingMethod;
    if ((getActivateTracingMethod = LogicModuleGrpc.getActivateTracingMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getActivateTracingMethod = LogicModuleGrpc.getActivateTracingMethod) == null) {
          LogicModuleGrpc.getActivateTracingMethod = getActivateTracingMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ActivateTracingRequest, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "activateTracing"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ActivateTracingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("activateTracing"))
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
    if ((getDeactivateTracingMethod = LogicModuleGrpc.getDeactivateTracingMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getDeactivateTracingMethod = LogicModuleGrpc.getDeactivateTracingMethod) == null) {
          LogicModuleGrpc.getDeactivateTracingMethod = getDeactivateTracingMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "deactivateTracing"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("deactivateTracing"))
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
    if ((getGetTracesMethod = LogicModuleGrpc.getGetTracesMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getGetTracesMethod = LogicModuleGrpc.getGetTracesMethod) == null) {
          LogicModuleGrpc.getGetTracesMethod = getGetTracesMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getTraces"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.GetTracesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("getTraces"))
              .build();
        }
      }
    }
    return getGetTracesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCleanMetaDataCachesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "cleanMetaDataCaches",
      requestType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCleanMetaDataCachesMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCleanMetaDataCachesMethod;
    if ((getCleanMetaDataCachesMethod = LogicModuleGrpc.getCleanMetaDataCachesMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getCleanMetaDataCachesMethod = LogicModuleGrpc.getCleanMetaDataCachesMethod) == null) {
          LogicModuleGrpc.getCleanMetaDataCachesMethod = getCleanMetaDataCachesMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "cleanMetaDataCaches"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("cleanMetaDataCaches"))
              .build();
        }
      }
    }
    return getCleanMetaDataCachesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCloseManagerDbMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "closeManagerDb",
      requestType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCloseManagerDbMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCloseManagerDbMethod;
    if ((getCloseManagerDbMethod = LogicModuleGrpc.getCloseManagerDbMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getCloseManagerDbMethod = LogicModuleGrpc.getCloseManagerDbMethod) == null) {
          LogicModuleGrpc.getCloseManagerDbMethod = getCloseManagerDbMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "closeManagerDb"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("closeManagerDb"))
              .build();
        }
      }
    }
    return getCloseManagerDbMethod;
  }

  private static volatile io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCloseDbMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "closeDb",
      requestType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.class,
      responseType = es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
      es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCloseDbMethod() {
    io.grpc.MethodDescriptor<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> getCloseDbMethod;
    if ((getCloseDbMethod = LogicModuleGrpc.getCloseDbMethod) == null) {
      synchronized (LogicModuleGrpc.class) {
        if ((getCloseDbMethod = LogicModuleGrpc.getCloseDbMethod) == null) {
          LogicModuleGrpc.getCloseDbMethod = getCloseDbMethod =
              io.grpc.MethodDescriptor.<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage, es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "closeDb"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo.getDefaultInstance()))
              .setSchemaDescriptor(new LogicModuleMethodDescriptorSupplier("closeDb"))
              .build();
        }
      }
    }
    return getCloseDbMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static LogicModuleStub newStub(io.grpc.Channel channel) {
    return new LogicModuleStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static LogicModuleBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new LogicModuleBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static LogicModuleFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new LogicModuleFutureStub(channel);
  }

  /**
   * <pre>
   * Interface exported by the server.
   * </pre>
   */
  public static abstract class LogicModuleImplBase implements io.grpc.BindableService {

    /**
     */
    public void autoregisterSL(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterSLRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getAutoregisterSLMethod(), responseObserver);
    }

    /**
     */
    public void autoregisterEE(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEERequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEEResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAutoregisterEEMethod(), responseObserver);
    }

    /**
     */
    public void unregisterStorageLocation(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterStorageLocationRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getUnregisterStorageLocationMethod(), responseObserver);
    }

    /**
     */
    public void unregisterExecutionEnvironment(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterExecutionEnvironmentRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getUnregisterExecutionEnvironmentMethod(), responseObserver);
    }

    /**
     */
    public void checkAlive(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getCheckAliveMethod(), responseObserver);
    }

    /**
     */
    public void performSetOfNewAccounts(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPerformSetOfNewAccountsMethod(), responseObserver);
    }

    /**
     */
    public void performSetOfOperations(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPerformSetOfOperationsMethod(), responseObserver);
    }

    /**
     */
    public void publishAddress(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PublishAddressRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getPublishAddressMethod(), responseObserver);
    }

    /**
     * <pre>
     * Account Manager
     * </pre>
     */
    public void newAccountNoAdmin(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountNoAdminRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNewAccountNoAdminMethod(), responseObserver);
    }

    /**
     */
    public void newAccount(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNewAccountMethod(), responseObserver);
    }

    /**
     */
    public void getAccountID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetAccountIDMethod(), responseObserver);
    }

    /**
     */
    public void getAccountList(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetAccountListMethod(), responseObserver);
    }

    /**
     * <pre>
     * Session Manager
     * </pre>
     */
    public void newSession(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNewSessionMethod(), responseObserver);
    }

    /**
     */
    public void getInfoOfSessionForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetInfoOfSessionForDSMethod(), responseObserver);
    }

    /**
     * <pre>
     * Namespace Manager
     * </pre>
     */
    public void newNamespace(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNewNamespaceMethod(), responseObserver);
    }

    /**
     */
    public void removeNamespace(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveNamespaceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getRemoveNamespaceMethod(), responseObserver);
    }

    /**
     */
    public void getNamespaceID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetNamespaceIDMethod(), responseObserver);
    }

    /**
     */
    public void getNamespaceLang(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetNamespaceLangMethod(), responseObserver);
    }

    /**
     */
    public void getObjectDataSetID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetObjectDataSetIDMethod(), responseObserver);
    }

    /**
     */
    public void importInterface(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportInterfaceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getImportInterfaceMethod(), responseObserver);
    }

    /**
     */
    public void importContract(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportContractRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getImportContractMethod(), responseObserver);
    }

    /**
     */
    public void getInfoOfClassesInNamespace(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetInfoOfClassesInNamespaceMethod(), responseObserver);
    }

    /**
     */
    public void getImportedClassesInfoInNamespace(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetImportedClassesInfoInNamespaceMethod(), responseObserver);
    }

    /**
     */
    public void getClassIDfromImport(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetClassIDfromImportMethod(), responseObserver);
    }

    /**
     */
    public void getNamespaces(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetNamespacesMethod(), responseObserver);
    }

    /**
     * <pre>
     * DataSet Manager
     * </pre>
     */
    public void newDataSet(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNewDataSetMethod(), responseObserver);
    }

    /**
     */
    public void removeDataSet(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveDataSetRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getRemoveDataSetMethod(), responseObserver);
    }

    /**
     */
    public void getDataSetID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetDataSetIDMethod(), responseObserver);
    }

    /**
     */
    public void checkDataSetIsPublic(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCheckDataSetIsPublicMethod(), responseObserver);
    }

    /**
     */
    public void getPublicDataSets(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetPublicDataSetsMethod(), responseObserver);
    }

    /**
     */
    public void getAccountDataSets(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetAccountDataSetsMethod(), responseObserver);
    }

    /**
     * <pre>
     * Class Manager
     * </pre>
     */
    public void newClass(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNewClassMethod(), responseObserver);
    }

    /**
     */
    public void newClassID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNewClassIDMethod(), responseObserver);
    }

    /**
     */
    public void removeClass(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveClassRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getRemoveClassMethod(), responseObserver);
    }

    /**
     */
    public void removeOperation(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveOperationRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getRemoveOperationMethod(), responseObserver);
    }

    /**
     */
    public void removeImplementation(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveImplementationRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getRemoveImplementationMethod(), responseObserver);
    }

    /**
     */
    public void getOperationID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetOperationIDMethod(), responseObserver);
    }

    /**
     */
    public void getPropertyID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetPropertyIDMethod(), responseObserver);
    }

    /**
     */
    public void getClassID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetClassIDMethod(), responseObserver);
    }

    /**
     */
    public void getClassInfo(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetClassInfoMethod(), responseObserver);
    }

    /**
     * <pre>
     * Contract Manager
     * </pre>
     */
    public void newContract(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNewContractMethod(), responseObserver);
    }

    /**
     */
    public void registerToPublicContract(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getRegisterToPublicContractMethod(), responseObserver);
    }

    /**
     */
    public void registerToPublicContractOfNamespace(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRegisterToPublicContractOfNamespaceMethod(), responseObserver);
    }

    /**
     */
    public void getContractIDsOfApplicant(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetContractIDsOfApplicantMethod(), responseObserver);
    }

    /**
     */
    public void getContractIDsOfProvider(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetContractIDsOfProviderMethod(), responseObserver);
    }

    /**
     */
    public void getContractIDsOfApplicantWithProvider(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetContractIDsOfApplicantWithProviderMethod(), responseObserver);
    }

    /**
     * <pre>
     * DataContract Manager
     * </pre>
     */
    public void newDataContract(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNewDataContractMethod(), responseObserver);
    }

    /**
     */
    public void registerToPublicDataContract(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicDataContractRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getRegisterToPublicDataContractMethod(), responseObserver);
    }

    /**
     */
    public void getDataContractIDsOfApplicant(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetDataContractIDsOfApplicantMethod(), responseObserver);
    }

    /**
     */
    public void getDataContractIDsOfProvider(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetDataContractIDsOfProviderMethod(), responseObserver);
    }

    /**
     */
    public void getDataContractInfoOfApplicantWithProvider(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetDataContractInfoOfApplicantWithProviderMethod(), responseObserver);
    }

    /**
     * <pre>
     * Interface Manager
     * </pre>
     */
    public void newInterface(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNewInterfaceMethod(), responseObserver);
    }

    /**
     */
    public void getInterfaceInfo(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetInterfaceInfoMethod(), responseObserver);
    }

    /**
     */
    public void removeInterface(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveInterfaceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getRemoveInterfaceMethod(), responseObserver);
    }

    /**
     * <pre>
     * EE-SL information
     * </pre>
     */
    public void getExecutionEnvironmentsInfo(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetExecutionEnvironmentsInfoMethod(), responseObserver);
    }

    /**
     */
    public void getExecutionEnvironmentsNames(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetExecutionEnvironmentsNamesMethod(), responseObserver);
    }

    /**
     * <pre>
     * TODO: modify next functions also called from Python to more scalable information
     * </pre>
     */
    public void getExecutionEnvironmentForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetExecutionEnvironmentForDSMethod(), responseObserver);
    }

    /**
     */
    public void getStorageLocationForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetStorageLocationForDSMethod(), responseObserver);
    }

    /**
     * <pre>
     * Object Metadata
     * </pre>
     */
    public void getObjectInfo(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetObjectInfoMethod(), responseObserver);
    }

    /**
     */
    public void getObjectFromAlias(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetObjectFromAliasMethod(), responseObserver);
    }

    /**
     */
    public void deleteAlias(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.DeleteAliasRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getDeleteAliasMethod(), responseObserver);
    }

    /**
     */
    public void getObjectsMetaDataInfoOfClassForNM(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetObjectsMetaDataInfoOfClassForNMMethod(), responseObserver);
    }

    /**
     */
    public void addAlias(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AddAliasRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getAddAliasMethod(), responseObserver);
    }

    /**
     */
    public void registerObjectFromGC(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectForGCRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getRegisterObjectFromGCMethod(), responseObserver);
    }

    /**
     */
    public void unregisterObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getUnregisterObjectsMethod(), responseObserver);
    }

    /**
     */
    public void registerObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getRegisterObjectMethod(), responseObserver);
    }

    /**
     */
    public void setDataSetIDFromGarbageCollector(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDFromGarbageCollectorRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getSetDataSetIDFromGarbageCollectorMethod(), responseObserver);
    }

    /**
     * <pre>
     * Storage Location
     * </pre>
     */
    public void setDataSetID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getSetDataSetIDMethod(), responseObserver);
    }

    /**
     */
    public void newVersion(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNewVersionMethod(), responseObserver);
    }

    /**
     */
    public void consolidateVersion(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ConsolidateVersionRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getConsolidateVersionMethod(), responseObserver);
    }

    /**
     */
    public void newReplica(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNewReplicaMethod(), responseObserver);
    }

    /**
     */
    public void moveObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getMoveObjectMethod(), responseObserver);
    }

    /**
     */
    public void setObjectReadOnly(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadOnlyRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getSetObjectReadOnlyMethod(), responseObserver);
    }

    /**
     */
    public void setObjectReadWrite(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadWriteRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getSetObjectReadWriteMethod(), responseObserver);
    }

    /**
     */
    public void getMetadataByOID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetMetadataByOIDMethod(), responseObserver);
    }

    /**
     * <pre>
     * Execution Environment
     * </pre>
     */
    public void executeImplementation(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getExecuteImplementationMethod(), responseObserver);
    }

    /**
     */
    public void executeMethodOnTarget(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getExecuteMethodOnTargetMethod(), responseObserver);
    }

    /**
     */
    public void synchronizeFederatedObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SynchronizeFederatedObjectRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getSynchronizeFederatedObjectMethod(), responseObserver);
    }

    /**
     * <pre>
     * Federation 
     * </pre>
     */
    public void getDataClayID(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClayIDResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetDataClayIDMethod(), responseObserver);
    }

    /**
     */
    public void registerExternalDataClay(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRegisterExternalDataClayMethod(), responseObserver);
    }

    /**
     */
    public void registerExternalDataClayOverrideAuthority(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayOverrideAuthorityRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRegisterExternalDataClayOverrideAuthorityMethod(), responseObserver);
    }

    /**
     */
    public void notifyRegistrationOfExternalDataClay(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNotifyRegistrationOfExternalDataClayMethod(), responseObserver);
    }

    /**
     */
    public void getExternalDataClayInfo(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetExternalDataClayInfoMethod(), responseObserver);
    }

    /**
     */
    public void getExternalDataclayId(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetExternalDataclayIdMethod(), responseObserver);
    }

    /**
     */
    public void federateObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateObjectRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getFederateObjectMethod(), responseObserver);
    }

    /**
     */
    public void unfederateObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getUnfederateObjectMethod(), responseObserver);
    }

    /**
     */
    public void notifyUnfederatedObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyUnfederatedObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getNotifyUnfederatedObjectsMethod(), responseObserver);
    }

    /**
     */
    public void notifyFederatedObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyFederatedObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getNotifyFederatedObjectsMethod(), responseObserver);
    }

    /**
     */
    public void checkObjectIsFederatedWithDataClayInstance(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCheckObjectIsFederatedWithDataClayInstanceMethod(), responseObserver);
    }

    /**
     */
    public void getDataClaysObjectIsFederatedWith(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetDataClaysObjectIsFederatedWithMethod(), responseObserver);
    }

    /**
     */
    public void getExternalSourceDataClayOfObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetExternalSourceDataClayOfObjectMethod(), responseObserver);
    }

    /**
     */
    public void unfederateAllObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getUnfederateAllObjectsMethod(), responseObserver);
    }

    /**
     */
    public void unfederateAllObjectsWithAllDCs(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsWithAllDCsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getUnfederateAllObjectsWithAllDCsMethod(), responseObserver);
    }

    /**
     */
    public void unfederateObjectWithAllDCs(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectWithAllDCsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getUnfederateObjectWithAllDCsMethod(), responseObserver);
    }

    /**
     */
    public void migrateFederatedObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MigrateFederatedObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getMigrateFederatedObjectsMethod(), responseObserver);
    }

    /**
     */
    public void federateAllObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateAllObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getFederateAllObjectsMethod(), responseObserver);
    }

    /**
     * <pre>
     * Stubs
     * </pre>
     */
    public void getStubs(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetStubsMethod(), responseObserver);
    }

    /**
     */
    public void getBabelStubs(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetBabelStubsMethod(), responseObserver);
    }

    /**
     * <pre>
     * Notification Manager
     * </pre>
     */
    public void registerECA(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterECARequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getRegisterECAMethod(), responseObserver);
    }

    /**
     */
    public void adviseEvent(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AdviseEventRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getAdviseEventMethod(), responseObserver);
    }

    /**
     * <pre>
     * Prefetching 
     * </pre>
     */
    public void isPrefetchingEnabled(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.IsPrefetchingEnabledResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getIsPrefetchingEnabledMethod(), responseObserver);
    }

    /**
     * <pre>
     * Others
     * </pre>
     */
    public void getClassNameForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetClassNameForDSMethod(), responseObserver);
    }

    /**
     */
    public void getClassNameAndNamespaceForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetClassNameAndNamespaceForDSMethod(), responseObserver);
    }

    /**
     */
    public void getContractIDOfDataClayProvider(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetContractIDOfDataClayProviderMethod(), responseObserver);
    }

    /**
     * <pre>
     * Testing
     * </pre>
     */
    public void objectExistsInDataClay(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getObjectExistsInDataClayMethod(), responseObserver);
    }

    /**
     * <pre>
     * Others
     * </pre>
     */
    public void closeSession(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CloseSessionRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getCloseSessionMethod(), responseObserver);
    }

    /**
     */
    public void getMetadataByOIDForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetMetadataByOIDForDSMethod(), responseObserver);
    }

    /**
     * <pre>
     * Paraver
     * </pre>
     */
    public void activateTracing(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ActivateTracingRequest request,
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
     */
    public void cleanMetaDataCaches(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getCleanMetaDataCachesMethod(), responseObserver);
    }

    /**
     */
    public void closeManagerDb(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getCloseManagerDbMethod(), responseObserver);
    }

    /**
     */
    public void closeDb(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getCloseDbMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getAutoregisterSLMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterSLRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_AUTOREGISTER_SL)))
          .addMethod(
            getAutoregisterEEMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEERequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEEResponse>(
                  this, METHODID_AUTOREGISTER_EE)))
          .addMethod(
            getUnregisterStorageLocationMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterStorageLocationRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_UNREGISTER_STORAGE_LOCATION)))
          .addMethod(
            getUnregisterExecutionEnvironmentMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterExecutionEnvironmentRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_UNREGISTER_EXECUTION_ENVIRONMENT)))
          .addMethod(
            getCheckAliveMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_CHECK_ALIVE)))
          .addMethod(
            getPerformSetOfNewAccountsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsResponse>(
                  this, METHODID_PERFORM_SET_OF_NEW_ACCOUNTS)))
          .addMethod(
            getPerformSetOfOperationsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsResponse>(
                  this, METHODID_PERFORM_SET_OF_OPERATIONS)))
          .addMethod(
            getPublishAddressMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PublishAddressRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_PUBLISH_ADDRESS)))
          .addMethod(
            getNewAccountNoAdminMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountNoAdminRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse>(
                  this, METHODID_NEW_ACCOUNT_NO_ADMIN)))
          .addMethod(
            getNewAccountMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse>(
                  this, METHODID_NEW_ACCOUNT)))
          .addMethod(
            getGetAccountIDMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDResponse>(
                  this, METHODID_GET_ACCOUNT_ID)))
          .addMethod(
            getGetAccountListMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListResponse>(
                  this, METHODID_GET_ACCOUNT_LIST)))
          .addMethod(
            getNewSessionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionResponse>(
                  this, METHODID_NEW_SESSION)))
          .addMethod(
            getGetInfoOfSessionForDSMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSResponse>(
                  this, METHODID_GET_INFO_OF_SESSION_FOR_DS)))
          .addMethod(
            getNewNamespaceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceResponse>(
                  this, METHODID_NEW_NAMESPACE)))
          .addMethod(
            getRemoveNamespaceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveNamespaceRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_REMOVE_NAMESPACE)))
          .addMethod(
            getGetNamespaceIDMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDResponse>(
                  this, METHODID_GET_NAMESPACE_ID)))
          .addMethod(
            getGetNamespaceLangMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangResponse>(
                  this, METHODID_GET_NAMESPACE_LANG)))
          .addMethod(
            getGetObjectDataSetIDMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDResponse>(
                  this, METHODID_GET_OBJECT_DATA_SET_ID)))
          .addMethod(
            getImportInterfaceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportInterfaceRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_IMPORT_INTERFACE)))
          .addMethod(
            getImportContractMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportContractRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_IMPORT_CONTRACT)))
          .addMethod(
            getGetInfoOfClassesInNamespaceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceResponse>(
                  this, METHODID_GET_INFO_OF_CLASSES_IN_NAMESPACE)))
          .addMethod(
            getGetImportedClassesInfoInNamespaceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceResponse>(
                  this, METHODID_GET_IMPORTED_CLASSES_INFO_IN_NAMESPACE)))
          .addMethod(
            getGetClassIDfromImportMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportResponse>(
                  this, METHODID_GET_CLASS_IDFROM_IMPORT)))
          .addMethod(
            getGetNamespacesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesResponse>(
                  this, METHODID_GET_NAMESPACES)))
          .addMethod(
            getNewDataSetMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetResponse>(
                  this, METHODID_NEW_DATA_SET)))
          .addMethod(
            getRemoveDataSetMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveDataSetRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_REMOVE_DATA_SET)))
          .addMethod(
            getGetDataSetIDMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDResponse>(
                  this, METHODID_GET_DATA_SET_ID)))
          .addMethod(
            getCheckDataSetIsPublicMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicResponse>(
                  this, METHODID_CHECK_DATA_SET_IS_PUBLIC)))
          .addMethod(
            getGetPublicDataSetsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsResponse>(
                  this, METHODID_GET_PUBLIC_DATA_SETS)))
          .addMethod(
            getGetAccountDataSetsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsResponse>(
                  this, METHODID_GET_ACCOUNT_DATA_SETS)))
          .addMethod(
            getNewClassMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassResponse>(
                  this, METHODID_NEW_CLASS)))
          .addMethod(
            getNewClassIDMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDResponse>(
                  this, METHODID_NEW_CLASS_ID)))
          .addMethod(
            getRemoveClassMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveClassRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_REMOVE_CLASS)))
          .addMethod(
            getRemoveOperationMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveOperationRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_REMOVE_OPERATION)))
          .addMethod(
            getRemoveImplementationMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveImplementationRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_REMOVE_IMPLEMENTATION)))
          .addMethod(
            getGetOperationIDMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDResponse>(
                  this, METHODID_GET_OPERATION_ID)))
          .addMethod(
            getGetPropertyIDMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDResponse>(
                  this, METHODID_GET_PROPERTY_ID)))
          .addMethod(
            getGetClassIDMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDResponse>(
                  this, METHODID_GET_CLASS_ID)))
          .addMethod(
            getGetClassInfoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoResponse>(
                  this, METHODID_GET_CLASS_INFO)))
          .addMethod(
            getNewContractMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractResponse>(
                  this, METHODID_NEW_CONTRACT)))
          .addMethod(
            getRegisterToPublicContractMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_REGISTER_TO_PUBLIC_CONTRACT)))
          .addMethod(
            getRegisterToPublicContractOfNamespaceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceResponse>(
                  this, METHODID_REGISTER_TO_PUBLIC_CONTRACT_OF_NAMESPACE)))
          .addMethod(
            getGetContractIDsOfApplicantMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantResponse>(
                  this, METHODID_GET_CONTRACT_IDS_OF_APPLICANT)))
          .addMethod(
            getGetContractIDsOfProviderMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderResponse>(
                  this, METHODID_GET_CONTRACT_IDS_OF_PROVIDER)))
          .addMethod(
            getGetContractIDsOfApplicantWithProviderMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvResponse>(
                  this, METHODID_GET_CONTRACT_IDS_OF_APPLICANT_WITH_PROVIDER)))
          .addMethod(
            getNewDataContractMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractResponse>(
                  this, METHODID_NEW_DATA_CONTRACT)))
          .addMethod(
            getRegisterToPublicDataContractMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicDataContractRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_REGISTER_TO_PUBLIC_DATA_CONTRACT)))
          .addMethod(
            getGetDataContractIDsOfApplicantMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantResponse>(
                  this, METHODID_GET_DATA_CONTRACT_IDS_OF_APPLICANT)))
          .addMethod(
            getGetDataContractIDsOfProviderMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderResponse>(
                  this, METHODID_GET_DATA_CONTRACT_IDS_OF_PROVIDER)))
          .addMethod(
            getGetDataContractInfoOfApplicantWithProviderMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvResponse>(
                  this, METHODID_GET_DATA_CONTRACT_INFO_OF_APPLICANT_WITH_PROVIDER)))
          .addMethod(
            getNewInterfaceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceResponse>(
                  this, METHODID_NEW_INTERFACE)))
          .addMethod(
            getGetInterfaceInfoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoResponse>(
                  this, METHODID_GET_INTERFACE_INFO)))
          .addMethod(
            getRemoveInterfaceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveInterfaceRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_REMOVE_INTERFACE)))
          .addMethod(
            getGetExecutionEnvironmentsInfoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoResponse>(
                  this, METHODID_GET_EXECUTION_ENVIRONMENTS_INFO)))
          .addMethod(
            getGetExecutionEnvironmentsNamesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesResponse>(
                  this, METHODID_GET_EXECUTION_ENVIRONMENTS_NAMES)))
          .addMethod(
            getGetExecutionEnvironmentForDSMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSResponse>(
                  this, METHODID_GET_EXECUTION_ENVIRONMENT_FOR_DS)))
          .addMethod(
            getGetStorageLocationForDSMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSResponse>(
                  this, METHODID_GET_STORAGE_LOCATION_FOR_DS)))
          .addMethod(
            getGetObjectInfoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoResponse>(
                  this, METHODID_GET_OBJECT_INFO)))
          .addMethod(
            getGetObjectFromAliasMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasResponse>(
                  this, METHODID_GET_OBJECT_FROM_ALIAS)))
          .addMethod(
            getDeleteAliasMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.DeleteAliasRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_DELETE_ALIAS)))
          .addMethod(
            getGetObjectsMetaDataInfoOfClassForNMMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMResponse>(
                  this, METHODID_GET_OBJECTS_META_DATA_INFO_OF_CLASS_FOR_NM)))
          .addMethod(
            getAddAliasMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AddAliasRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_ADD_ALIAS)))
          .addMethod(
            getRegisterObjectFromGCMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectForGCRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_REGISTER_OBJECT_FROM_GC)))
          .addMethod(
            getUnregisterObjectsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterObjectsRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_UNREGISTER_OBJECTS)))
          .addMethod(
            getRegisterObjectMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_REGISTER_OBJECT)))
          .addMethod(
            getSetDataSetIDFromGarbageCollectorMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDFromGarbageCollectorRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_SET_DATA_SET_IDFROM_GARBAGE_COLLECTOR)))
          .addMethod(
            getSetDataSetIDMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_SET_DATA_SET_ID)))
          .addMethod(
            getNewVersionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionResponse>(
                  this, METHODID_NEW_VERSION)))
          .addMethod(
            getConsolidateVersionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ConsolidateVersionRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_CONSOLIDATE_VERSION)))
          .addMethod(
            getNewReplicaMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaResponse>(
                  this, METHODID_NEW_REPLICA)))
          .addMethod(
            getMoveObjectMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectResponse>(
                  this, METHODID_MOVE_OBJECT)))
          .addMethod(
            getSetObjectReadOnlyMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadOnlyRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_SET_OBJECT_READ_ONLY)))
          .addMethod(
            getSetObjectReadWriteMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadWriteRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_SET_OBJECT_READ_WRITE)))
          .addMethod(
            getGetMetadataByOIDMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDResponse>(
                  this, METHODID_GET_METADATA_BY_OID)))
          .addMethod(
            getExecuteImplementationMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationResponse>(
                  this, METHODID_EXECUTE_IMPLEMENTATION)))
          .addMethod(
            getExecuteMethodOnTargetMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetResponse>(
                  this, METHODID_EXECUTE_METHOD_ON_TARGET)))
          .addMethod(
            getSynchronizeFederatedObjectMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SynchronizeFederatedObjectRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_SYNCHRONIZE_FEDERATED_OBJECT)))
          .addMethod(
            getGetDataClayIDMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClayIDResponse>(
                  this, METHODID_GET_DATA_CLAY_ID)))
          .addMethod(
            getRegisterExternalDataClayMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse>(
                  this, METHODID_REGISTER_EXTERNAL_DATA_CLAY)))
          .addMethod(
            getRegisterExternalDataClayOverrideAuthorityMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayOverrideAuthorityRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse>(
                  this, METHODID_REGISTER_EXTERNAL_DATA_CLAY_OVERRIDE_AUTHORITY)))
          .addMethod(
            getNotifyRegistrationOfExternalDataClayMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayResponse>(
                  this, METHODID_NOTIFY_REGISTRATION_OF_EXTERNAL_DATA_CLAY)))
          .addMethod(
            getGetExternalDataClayInfoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoResponse>(
                  this, METHODID_GET_EXTERNAL_DATA_CLAY_INFO)))
          .addMethod(
            getGetExternalDataclayIdMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDResponse>(
                  this, METHODID_GET_EXTERNAL_DATACLAY_ID)))
          .addMethod(
            getFederateObjectMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateObjectRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_FEDERATE_OBJECT)))
          .addMethod(
            getUnfederateObjectMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_UNFEDERATE_OBJECT)))
          .addMethod(
            getNotifyUnfederatedObjectsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyUnfederatedObjectsRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_NOTIFY_UNFEDERATED_OBJECTS)))
          .addMethod(
            getNotifyFederatedObjectsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyFederatedObjectsRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_NOTIFY_FEDERATED_OBJECTS)))
          .addMethod(
            getCheckObjectIsFederatedWithDataClayInstanceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceResponse>(
                  this, METHODID_CHECK_OBJECT_IS_FEDERATED_WITH_DATA_CLAY_INSTANCE)))
          .addMethod(
            getGetDataClaysObjectIsFederatedWithMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithResponse>(
                  this, METHODID_GET_DATA_CLAYS_OBJECT_IS_FEDERATED_WITH)))
          .addMethod(
            getGetExternalSourceDataClayOfObjectMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectResponse>(
                  this, METHODID_GET_EXTERNAL_SOURCE_DATA_CLAY_OF_OBJECT)))
          .addMethod(
            getUnfederateAllObjectsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_UNFEDERATE_ALL_OBJECTS)))
          .addMethod(
            getUnfederateAllObjectsWithAllDCsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsWithAllDCsRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_UNFEDERATE_ALL_OBJECTS_WITH_ALL_DCS)))
          .addMethod(
            getUnfederateObjectWithAllDCsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectWithAllDCsRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_UNFEDERATE_OBJECT_WITH_ALL_DCS)))
          .addMethod(
            getMigrateFederatedObjectsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MigrateFederatedObjectsRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_MIGRATE_FEDERATED_OBJECTS)))
          .addMethod(
            getFederateAllObjectsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateAllObjectsRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_FEDERATE_ALL_OBJECTS)))
          .addMethod(
            getGetStubsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsResponse>(
                  this, METHODID_GET_STUBS)))
          .addMethod(
            getGetBabelStubsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsResponse>(
                  this, METHODID_GET_BABEL_STUBS)))
          .addMethod(
            getRegisterECAMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterECARequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_REGISTER_ECA)))
          .addMethod(
            getAdviseEventMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AdviseEventRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_ADVISE_EVENT)))
          .addMethod(
            getIsPrefetchingEnabledMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.IsPrefetchingEnabledResponse>(
                  this, METHODID_IS_PREFETCHING_ENABLED)))
          .addMethod(
            getGetClassNameForDSMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSResponse>(
                  this, METHODID_GET_CLASS_NAME_FOR_DS)))
          .addMethod(
            getGetClassNameAndNamespaceForDSMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSResponse>(
                  this, METHODID_GET_CLASS_NAME_AND_NAMESPACE_FOR_DS)))
          .addMethod(
            getGetContractIDOfDataClayProviderMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderResponse>(
                  this, METHODID_GET_CONTRACT_IDOF_DATA_CLAY_PROVIDER)))
          .addMethod(
            getObjectExistsInDataClayMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayResponse>(
                  this, METHODID_OBJECT_EXISTS_IN_DATA_CLAY)))
          .addMethod(
            getCloseSessionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CloseSessionRequest,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_CLOSE_SESSION)))
          .addMethod(
            getGetMetadataByOIDForDSMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSRequest,
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSResponse>(
                  this, METHODID_GET_METADATA_BY_OIDFOR_DS)))
          .addMethod(
            getActivateTracingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ActivateTracingRequest,
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
            getCleanMetaDataCachesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_CLEAN_META_DATA_CACHES)))
          .addMethod(
            getCloseManagerDbMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_CLOSE_MANAGER_DB)))
          .addMethod(
            getCloseDbMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage,
                es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>(
                  this, METHODID_CLOSE_DB)))
          .build();
    }
  }

  /**
   * <pre>
   * Interface exported by the server.
   * </pre>
   */
  public static final class LogicModuleStub extends io.grpc.stub.AbstractStub<LogicModuleStub> {
    private LogicModuleStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LogicModuleStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LogicModuleStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LogicModuleStub(channel, callOptions);
    }

    /**
     */
    public void autoregisterSL(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterSLRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAutoregisterSLMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void autoregisterEE(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEERequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEEResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAutoregisterEEMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void unregisterStorageLocation(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterStorageLocationRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUnregisterStorageLocationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void unregisterExecutionEnvironment(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterExecutionEnvironmentRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUnregisterExecutionEnvironmentMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void checkAlive(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCheckAliveMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void performSetOfNewAccounts(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPerformSetOfNewAccountsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void performSetOfOperations(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPerformSetOfOperationsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void publishAddress(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PublishAddressRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPublishAddressMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Account Manager
     * </pre>
     */
    public void newAccountNoAdmin(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountNoAdminRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNewAccountNoAdminMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void newAccount(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNewAccountMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAccountID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetAccountIDMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAccountList(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetAccountListMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Session Manager
     * </pre>
     */
    public void newSession(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNewSessionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getInfoOfSessionForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetInfoOfSessionForDSMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Namespace Manager
     * </pre>
     */
    public void newNamespace(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNewNamespaceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeNamespace(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveNamespaceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRemoveNamespaceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getNamespaceID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetNamespaceIDMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getNamespaceLang(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetNamespaceLangMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getObjectDataSetID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetObjectDataSetIDMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void importInterface(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportInterfaceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getImportInterfaceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void importContract(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportContractRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getImportContractMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getInfoOfClassesInNamespace(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetInfoOfClassesInNamespaceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getImportedClassesInfoInNamespace(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetImportedClassesInfoInNamespaceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getClassIDfromImport(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetClassIDfromImportMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getNamespaces(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetNamespacesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * DataSet Manager
     * </pre>
     */
    public void newDataSet(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNewDataSetMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeDataSet(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveDataSetRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRemoveDataSetMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getDataSetID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetDataSetIDMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void checkDataSetIsPublic(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCheckDataSetIsPublicMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getPublicDataSets(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetPublicDataSetsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAccountDataSets(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetAccountDataSetsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Class Manager
     * </pre>
     */
    public void newClass(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNewClassMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void newClassID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNewClassIDMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeClass(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveClassRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRemoveClassMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeOperation(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveOperationRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRemoveOperationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeImplementation(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveImplementationRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRemoveImplementationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getOperationID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetOperationIDMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getPropertyID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetPropertyIDMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getClassID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetClassIDMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getClassInfo(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetClassInfoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Contract Manager
     * </pre>
     */
    public void newContract(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNewContractMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void registerToPublicContract(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegisterToPublicContractMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void registerToPublicContractOfNamespace(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegisterToPublicContractOfNamespaceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getContractIDsOfApplicant(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetContractIDsOfApplicantMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getContractIDsOfProvider(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetContractIDsOfProviderMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getContractIDsOfApplicantWithProvider(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetContractIDsOfApplicantWithProviderMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * DataContract Manager
     * </pre>
     */
    public void newDataContract(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNewDataContractMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void registerToPublicDataContract(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicDataContractRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegisterToPublicDataContractMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getDataContractIDsOfApplicant(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetDataContractIDsOfApplicantMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getDataContractIDsOfProvider(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetDataContractIDsOfProviderMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getDataContractInfoOfApplicantWithProvider(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetDataContractInfoOfApplicantWithProviderMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Interface Manager
     * </pre>
     */
    public void newInterface(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNewInterfaceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getInterfaceInfo(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetInterfaceInfoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeInterface(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveInterfaceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRemoveInterfaceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * EE-SL information
     * </pre>
     */
    public void getExecutionEnvironmentsInfo(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetExecutionEnvironmentsInfoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getExecutionEnvironmentsNames(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetExecutionEnvironmentsNamesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * TODO: modify next functions also called from Python to more scalable information
     * </pre>
     */
    public void getExecutionEnvironmentForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetExecutionEnvironmentForDSMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getStorageLocationForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetStorageLocationForDSMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Object Metadata
     * </pre>
     */
    public void getObjectInfo(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetObjectInfoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getObjectFromAlias(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetObjectFromAliasMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteAlias(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.DeleteAliasRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeleteAliasMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getObjectsMetaDataInfoOfClassForNM(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetObjectsMetaDataInfoOfClassForNMMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addAlias(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AddAliasRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAddAliasMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void registerObjectFromGC(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectForGCRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegisterObjectFromGCMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void unregisterObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUnregisterObjectsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void registerObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegisterObjectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setDataSetIDFromGarbageCollector(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDFromGarbageCollectorRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetDataSetIDFromGarbageCollectorMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Storage Location
     * </pre>
     */
    public void setDataSetID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetDataSetIDMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void newVersion(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNewVersionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void consolidateVersion(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ConsolidateVersionRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getConsolidateVersionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void newReplica(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNewReplicaMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void moveObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getMoveObjectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setObjectReadOnly(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadOnlyRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetObjectReadOnlyMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setObjectReadWrite(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadWriteRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetObjectReadWriteMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getMetadataByOID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetMetadataByOIDMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Execution Environment
     * </pre>
     */
    public void executeImplementation(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getExecuteImplementationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void executeMethodOnTarget(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getExecuteMethodOnTargetMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void synchronizeFederatedObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SynchronizeFederatedObjectRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSynchronizeFederatedObjectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Federation 
     * </pre>
     */
    public void getDataClayID(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClayIDResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetDataClayIDMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void registerExternalDataClay(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegisterExternalDataClayMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void registerExternalDataClayOverrideAuthority(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayOverrideAuthorityRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegisterExternalDataClayOverrideAuthorityMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void notifyRegistrationOfExternalDataClay(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNotifyRegistrationOfExternalDataClayMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getExternalDataClayInfo(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetExternalDataClayInfoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getExternalDataclayId(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetExternalDataclayIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void federateObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateObjectRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getFederateObjectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void unfederateObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUnfederateObjectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void notifyUnfederatedObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyUnfederatedObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNotifyUnfederatedObjectsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void notifyFederatedObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyFederatedObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNotifyFederatedObjectsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void checkObjectIsFederatedWithDataClayInstance(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCheckObjectIsFederatedWithDataClayInstanceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getDataClaysObjectIsFederatedWith(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetDataClaysObjectIsFederatedWithMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getExternalSourceDataClayOfObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetExternalSourceDataClayOfObjectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void unfederateAllObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUnfederateAllObjectsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void unfederateAllObjectsWithAllDCs(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsWithAllDCsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUnfederateAllObjectsWithAllDCsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void unfederateObjectWithAllDCs(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectWithAllDCsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUnfederateObjectWithAllDCsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void migrateFederatedObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MigrateFederatedObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getMigrateFederatedObjectsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void federateAllObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateAllObjectsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getFederateAllObjectsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Stubs
     * </pre>
     */
    public void getStubs(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetStubsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getBabelStubs(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetBabelStubsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Notification Manager
     * </pre>
     */
    public void registerECA(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterECARequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegisterECAMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void adviseEvent(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AdviseEventRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAdviseEventMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Prefetching 
     * </pre>
     */
    public void isPrefetchingEnabled(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.IsPrefetchingEnabledResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getIsPrefetchingEnabledMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Others
     * </pre>
     */
    public void getClassNameForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetClassNameForDSMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getClassNameAndNamespaceForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetClassNameAndNamespaceForDSMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getContractIDOfDataClayProvider(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetContractIDOfDataClayProviderMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Testing
     * </pre>
     */
    public void objectExistsInDataClay(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getObjectExistsInDataClayMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Others
     * </pre>
     */
    public void closeSession(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CloseSessionRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCloseSessionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getMetadataByOIDForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSRequest request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetMetadataByOIDForDSMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Paraver
     * </pre>
     */
    public void activateTracing(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ActivateTracingRequest request,
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
     */
    public void cleanMetaDataCaches(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCleanMetaDataCachesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void closeManagerDb(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCloseManagerDbMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void closeDb(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request,
        io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCloseDbMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * Interface exported by the server.
   * </pre>
   */
  public static final class LogicModuleBlockingStub extends io.grpc.stub.AbstractStub<LogicModuleBlockingStub> {
    private LogicModuleBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LogicModuleBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LogicModuleBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LogicModuleBlockingStub(channel, callOptions);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo autoregisterSL(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterSLRequest request) {
      return blockingUnaryCall(
          getChannel(), getAutoregisterSLMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEEResponse autoregisterEE(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEERequest request) {
      return blockingUnaryCall(
          getChannel(), getAutoregisterEEMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo unregisterStorageLocation(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterStorageLocationRequest request) {
      return blockingUnaryCall(
          getChannel(), getUnregisterStorageLocationMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo unregisterExecutionEnvironment(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterExecutionEnvironmentRequest request) {
      return blockingUnaryCall(
          getChannel(), getUnregisterExecutionEnvironmentMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo checkAlive(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return blockingUnaryCall(
          getChannel(), getCheckAliveMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsResponse performSetOfNewAccounts(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsRequest request) {
      return blockingUnaryCall(
          getChannel(), getPerformSetOfNewAccountsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsResponse performSetOfOperations(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsRequest request) {
      return blockingUnaryCall(
          getChannel(), getPerformSetOfOperationsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo publishAddress(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PublishAddressRequest request) {
      return blockingUnaryCall(
          getChannel(), getPublishAddressMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Account Manager
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse newAccountNoAdmin(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountNoAdminRequest request) {
      return blockingUnaryCall(
          getChannel(), getNewAccountNoAdminMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse newAccount(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountRequest request) {
      return blockingUnaryCall(
          getChannel(), getNewAccountMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDResponse getAccountID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetAccountIDMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListResponse getAccountList(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetAccountListMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Session Manager
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionResponse newSession(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionRequest request) {
      return blockingUnaryCall(
          getChannel(), getNewSessionMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSResponse getInfoOfSessionForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetInfoOfSessionForDSMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Namespace Manager
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceResponse newNamespace(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceRequest request) {
      return blockingUnaryCall(
          getChannel(), getNewNamespaceMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo removeNamespace(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveNamespaceRequest request) {
      return blockingUnaryCall(
          getChannel(), getRemoveNamespaceMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDResponse getNamespaceID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetNamespaceIDMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangResponse getNamespaceLang(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetNamespaceLangMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDResponse getObjectDataSetID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetObjectDataSetIDMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo importInterface(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportInterfaceRequest request) {
      return blockingUnaryCall(
          getChannel(), getImportInterfaceMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo importContract(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportContractRequest request) {
      return blockingUnaryCall(
          getChannel(), getImportContractMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceResponse getInfoOfClassesInNamespace(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetInfoOfClassesInNamespaceMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceResponse getImportedClassesInfoInNamespace(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetImportedClassesInfoInNamespaceMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportResponse getClassIDfromImport(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetClassIDfromImportMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesResponse getNamespaces(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetNamespacesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * DataSet Manager
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetResponse newDataSet(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetRequest request) {
      return blockingUnaryCall(
          getChannel(), getNewDataSetMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo removeDataSet(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveDataSetRequest request) {
      return blockingUnaryCall(
          getChannel(), getRemoveDataSetMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDResponse getDataSetID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetDataSetIDMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicResponse checkDataSetIsPublic(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicRequest request) {
      return blockingUnaryCall(
          getChannel(), getCheckDataSetIsPublicMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsResponse getPublicDataSets(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetPublicDataSetsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsResponse getAccountDataSets(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetAccountDataSetsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Class Manager
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassResponse newClass(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassRequest request) {
      return blockingUnaryCall(
          getChannel(), getNewClassMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDResponse newClassID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDRequest request) {
      return blockingUnaryCall(
          getChannel(), getNewClassIDMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo removeClass(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveClassRequest request) {
      return blockingUnaryCall(
          getChannel(), getRemoveClassMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo removeOperation(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveOperationRequest request) {
      return blockingUnaryCall(
          getChannel(), getRemoveOperationMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo removeImplementation(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveImplementationRequest request) {
      return blockingUnaryCall(
          getChannel(), getRemoveImplementationMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDResponse getOperationID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetOperationIDMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDResponse getPropertyID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetPropertyIDMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDResponse getClassID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetClassIDMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoResponse getClassInfo(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetClassInfoMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Contract Manager
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractResponse newContract(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractRequest request) {
      return blockingUnaryCall(
          getChannel(), getNewContractMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo registerToPublicContract(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractRequest request) {
      return blockingUnaryCall(
          getChannel(), getRegisterToPublicContractMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceResponse registerToPublicContractOfNamespace(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceRequest request) {
      return blockingUnaryCall(
          getChannel(), getRegisterToPublicContractOfNamespaceMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantResponse getContractIDsOfApplicant(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetContractIDsOfApplicantMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderResponse getContractIDsOfProvider(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetContractIDsOfProviderMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvResponse getContractIDsOfApplicantWithProvider(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetContractIDsOfApplicantWithProviderMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * DataContract Manager
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractResponse newDataContract(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractRequest request) {
      return blockingUnaryCall(
          getChannel(), getNewDataContractMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo registerToPublicDataContract(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicDataContractRequest request) {
      return blockingUnaryCall(
          getChannel(), getRegisterToPublicDataContractMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantResponse getDataContractIDsOfApplicant(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetDataContractIDsOfApplicantMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderResponse getDataContractIDsOfProvider(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetDataContractIDsOfProviderMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvResponse getDataContractInfoOfApplicantWithProvider(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetDataContractInfoOfApplicantWithProviderMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Interface Manager
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceResponse newInterface(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceRequest request) {
      return blockingUnaryCall(
          getChannel(), getNewInterfaceMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoResponse getInterfaceInfo(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetInterfaceInfoMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo removeInterface(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveInterfaceRequest request) {
      return blockingUnaryCall(
          getChannel(), getRemoveInterfaceMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * EE-SL information
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoResponse getExecutionEnvironmentsInfo(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetExecutionEnvironmentsInfoMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesResponse getExecutionEnvironmentsNames(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetExecutionEnvironmentsNamesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * TODO: modify next functions also called from Python to more scalable information
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSResponse getExecutionEnvironmentForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetExecutionEnvironmentForDSMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSResponse getStorageLocationForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetStorageLocationForDSMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Object Metadata
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoResponse getObjectInfo(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetObjectInfoMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasResponse getObjectFromAlias(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetObjectFromAliasMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo deleteAlias(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.DeleteAliasRequest request) {
      return blockingUnaryCall(
          getChannel(), getDeleteAliasMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMResponse getObjectsMetaDataInfoOfClassForNM(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetObjectsMetaDataInfoOfClassForNMMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo addAlias(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AddAliasRequest request) {
      return blockingUnaryCall(
          getChannel(), getAddAliasMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo registerObjectFromGC(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectForGCRequest request) {
      return blockingUnaryCall(
          getChannel(), getRegisterObjectFromGCMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo unregisterObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterObjectsRequest request) {
      return blockingUnaryCall(
          getChannel(), getUnregisterObjectsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo registerObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectRequest request) {
      return blockingUnaryCall(
          getChannel(), getRegisterObjectMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo setDataSetIDFromGarbageCollector(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDFromGarbageCollectorRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetDataSetIDFromGarbageCollectorMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Storage Location
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo setDataSetID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetDataSetIDMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionResponse newVersion(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionRequest request) {
      return blockingUnaryCall(
          getChannel(), getNewVersionMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo consolidateVersion(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ConsolidateVersionRequest request) {
      return blockingUnaryCall(
          getChannel(), getConsolidateVersionMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaResponse newReplica(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaRequest request) {
      return blockingUnaryCall(
          getChannel(), getNewReplicaMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectResponse moveObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectRequest request) {
      return blockingUnaryCall(
          getChannel(), getMoveObjectMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo setObjectReadOnly(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadOnlyRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetObjectReadOnlyMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo setObjectReadWrite(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadWriteRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetObjectReadWriteMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDResponse getMetadataByOID(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetMetadataByOIDMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Execution Environment
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationResponse executeImplementation(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationRequest request) {
      return blockingUnaryCall(
          getChannel(), getExecuteImplementationMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetResponse executeMethodOnTarget(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetRequest request) {
      return blockingUnaryCall(
          getChannel(), getExecuteMethodOnTargetMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo synchronizeFederatedObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SynchronizeFederatedObjectRequest request) {
      return blockingUnaryCall(
          getChannel(), getSynchronizeFederatedObjectMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Federation 
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClayIDResponse getDataClayID(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return blockingUnaryCall(
          getChannel(), getGetDataClayIDMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse registerExternalDataClay(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayRequest request) {
      return blockingUnaryCall(
          getChannel(), getRegisterExternalDataClayMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse registerExternalDataClayOverrideAuthority(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayOverrideAuthorityRequest request) {
      return blockingUnaryCall(
          getChannel(), getRegisterExternalDataClayOverrideAuthorityMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayResponse notifyRegistrationOfExternalDataClay(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayRequest request) {
      return blockingUnaryCall(
          getChannel(), getNotifyRegistrationOfExternalDataClayMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoResponse getExternalDataClayInfo(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetExternalDataClayInfoMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDResponse getExternalDataclayId(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetExternalDataclayIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo federateObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateObjectRequest request) {
      return blockingUnaryCall(
          getChannel(), getFederateObjectMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo unfederateObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectRequest request) {
      return blockingUnaryCall(
          getChannel(), getUnfederateObjectMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo notifyUnfederatedObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyUnfederatedObjectsRequest request) {
      return blockingUnaryCall(
          getChannel(), getNotifyUnfederatedObjectsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo notifyFederatedObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyFederatedObjectsRequest request) {
      return blockingUnaryCall(
          getChannel(), getNotifyFederatedObjectsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceResponse checkObjectIsFederatedWithDataClayInstance(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceRequest request) {
      return blockingUnaryCall(
          getChannel(), getCheckObjectIsFederatedWithDataClayInstanceMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithResponse getDataClaysObjectIsFederatedWith(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetDataClaysObjectIsFederatedWithMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectResponse getExternalSourceDataClayOfObject(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetExternalSourceDataClayOfObjectMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo unfederateAllObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsRequest request) {
      return blockingUnaryCall(
          getChannel(), getUnfederateAllObjectsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo unfederateAllObjectsWithAllDCs(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsWithAllDCsRequest request) {
      return blockingUnaryCall(
          getChannel(), getUnfederateAllObjectsWithAllDCsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo unfederateObjectWithAllDCs(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectWithAllDCsRequest request) {
      return blockingUnaryCall(
          getChannel(), getUnfederateObjectWithAllDCsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo migrateFederatedObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MigrateFederatedObjectsRequest request) {
      return blockingUnaryCall(
          getChannel(), getMigrateFederatedObjectsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo federateAllObjects(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateAllObjectsRequest request) {
      return blockingUnaryCall(
          getChannel(), getFederateAllObjectsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Stubs
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsResponse getStubs(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetStubsMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsResponse getBabelStubs(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetBabelStubsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Notification Manager
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo registerECA(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterECARequest request) {
      return blockingUnaryCall(
          getChannel(), getRegisterECAMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo adviseEvent(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AdviseEventRequest request) {
      return blockingUnaryCall(
          getChannel(), getAdviseEventMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Prefetching 
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.IsPrefetchingEnabledResponse isPrefetchingEnabled(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return blockingUnaryCall(
          getChannel(), getIsPrefetchingEnabledMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Others
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSResponse getClassNameForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetClassNameForDSMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSResponse getClassNameAndNamespaceForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetClassNameAndNamespaceForDSMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderResponse getContractIDOfDataClayProvider(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetContractIDOfDataClayProviderMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Testing
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayResponse objectExistsInDataClay(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayRequest request) {
      return blockingUnaryCall(
          getChannel(), getObjectExistsInDataClayMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Others
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo closeSession(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CloseSessionRequest request) {
      return blockingUnaryCall(
          getChannel(), getCloseSessionMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSResponse getMetadataByOIDForDS(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetMetadataByOIDForDSMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Paraver
     * </pre>
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo activateTracing(es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ActivateTracingRequest request) {
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
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo cleanMetaDataCaches(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return blockingUnaryCall(
          getChannel(), getCleanMetaDataCachesMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo closeManagerDb(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return blockingUnaryCall(
          getChannel(), getCloseManagerDbMethod(), getCallOptions(), request);
    }

    /**
     */
    public es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo closeDb(es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return blockingUnaryCall(
          getChannel(), getCloseDbMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * Interface exported by the server.
   * </pre>
   */
  public static final class LogicModuleFutureStub extends io.grpc.stub.AbstractStub<LogicModuleFutureStub> {
    private LogicModuleFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LogicModuleFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LogicModuleFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LogicModuleFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> autoregisterSL(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterSLRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAutoregisterSLMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEEResponse> autoregisterEE(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEERequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAutoregisterEEMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> unregisterStorageLocation(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterStorageLocationRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUnregisterStorageLocationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> unregisterExecutionEnvironment(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterExecutionEnvironmentRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUnregisterExecutionEnvironmentMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> checkAlive(
        es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getCheckAliveMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsResponse> performSetOfNewAccounts(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPerformSetOfNewAccountsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsResponse> performSetOfOperations(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPerformSetOfOperationsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> publishAddress(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PublishAddressRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPublishAddressMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Account Manager
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse> newAccountNoAdmin(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountNoAdminRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNewAccountNoAdminMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse> newAccount(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNewAccountMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDResponse> getAccountID(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetAccountIDMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListResponse> getAccountList(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetAccountListMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Session Manager
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionResponse> newSession(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNewSessionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSResponse> getInfoOfSessionForDS(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetInfoOfSessionForDSMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Namespace Manager
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceResponse> newNamespace(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNewNamespaceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> removeNamespace(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveNamespaceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRemoveNamespaceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDResponse> getNamespaceID(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetNamespaceIDMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangResponse> getNamespaceLang(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetNamespaceLangMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDResponse> getObjectDataSetID(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetObjectDataSetIDMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> importInterface(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportInterfaceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getImportInterfaceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> importContract(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportContractRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getImportContractMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceResponse> getInfoOfClassesInNamespace(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetInfoOfClassesInNamespaceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceResponse> getImportedClassesInfoInNamespace(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetImportedClassesInfoInNamespaceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportResponse> getClassIDfromImport(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetClassIDfromImportMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesResponse> getNamespaces(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetNamespacesMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * DataSet Manager
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetResponse> newDataSet(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNewDataSetMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> removeDataSet(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveDataSetRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRemoveDataSetMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDResponse> getDataSetID(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetDataSetIDMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicResponse> checkDataSetIsPublic(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCheckDataSetIsPublicMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsResponse> getPublicDataSets(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetPublicDataSetsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsResponse> getAccountDataSets(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetAccountDataSetsMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Class Manager
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassResponse> newClass(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNewClassMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDResponse> newClassID(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNewClassIDMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> removeClass(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveClassRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRemoveClassMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> removeOperation(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveOperationRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRemoveOperationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> removeImplementation(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveImplementationRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRemoveImplementationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDResponse> getOperationID(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetOperationIDMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDResponse> getPropertyID(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetPropertyIDMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDResponse> getClassID(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetClassIDMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoResponse> getClassInfo(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetClassInfoMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Contract Manager
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractResponse> newContract(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNewContractMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> registerToPublicContract(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRegisterToPublicContractMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceResponse> registerToPublicContractOfNamespace(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRegisterToPublicContractOfNamespaceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantResponse> getContractIDsOfApplicant(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetContractIDsOfApplicantMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderResponse> getContractIDsOfProvider(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetContractIDsOfProviderMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvResponse> getContractIDsOfApplicantWithProvider(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetContractIDsOfApplicantWithProviderMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * DataContract Manager
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractResponse> newDataContract(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNewDataContractMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> registerToPublicDataContract(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicDataContractRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRegisterToPublicDataContractMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantResponse> getDataContractIDsOfApplicant(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetDataContractIDsOfApplicantMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderResponse> getDataContractIDsOfProvider(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetDataContractIDsOfProviderMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvResponse> getDataContractInfoOfApplicantWithProvider(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetDataContractInfoOfApplicantWithProviderMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Interface Manager
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceResponse> newInterface(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNewInterfaceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoResponse> getInterfaceInfo(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetInterfaceInfoMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> removeInterface(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveInterfaceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRemoveInterfaceMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * EE-SL information
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoResponse> getExecutionEnvironmentsInfo(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetExecutionEnvironmentsInfoMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesResponse> getExecutionEnvironmentsNames(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetExecutionEnvironmentsNamesMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * TODO: modify next functions also called from Python to more scalable information
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSResponse> getExecutionEnvironmentForDS(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetExecutionEnvironmentForDSMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSResponse> getStorageLocationForDS(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetStorageLocationForDSMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Object Metadata
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoResponse> getObjectInfo(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetObjectInfoMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasResponse> getObjectFromAlias(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetObjectFromAliasMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> deleteAlias(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.DeleteAliasRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDeleteAliasMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMResponse> getObjectsMetaDataInfoOfClassForNM(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetObjectsMetaDataInfoOfClassForNMMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> addAlias(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AddAliasRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAddAliasMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> registerObjectFromGC(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectForGCRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRegisterObjectFromGCMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> unregisterObjects(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterObjectsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUnregisterObjectsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> registerObject(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRegisterObjectMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> setDataSetIDFromGarbageCollector(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDFromGarbageCollectorRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetDataSetIDFromGarbageCollectorMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Storage Location
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> setDataSetID(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetDataSetIDMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionResponse> newVersion(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNewVersionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> consolidateVersion(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ConsolidateVersionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getConsolidateVersionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaResponse> newReplica(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNewReplicaMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectResponse> moveObject(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getMoveObjectMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> setObjectReadOnly(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadOnlyRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetObjectReadOnlyMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> setObjectReadWrite(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadWriteRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetObjectReadWriteMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDResponse> getMetadataByOID(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetMetadataByOIDMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Execution Environment
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationResponse> executeImplementation(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getExecuteImplementationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetResponse> executeMethodOnTarget(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getExecuteMethodOnTargetMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> synchronizeFederatedObject(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SynchronizeFederatedObjectRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSynchronizeFederatedObjectMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Federation 
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClayIDResponse> getDataClayID(
        es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getGetDataClayIDMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse> registerExternalDataClay(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRegisterExternalDataClayMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse> registerExternalDataClayOverrideAuthority(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayOverrideAuthorityRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRegisterExternalDataClayOverrideAuthorityMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayResponse> notifyRegistrationOfExternalDataClay(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNotifyRegistrationOfExternalDataClayMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoResponse> getExternalDataClayInfo(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetExternalDataClayInfoMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDResponse> getExternalDataclayId(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetExternalDataclayIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> federateObject(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateObjectRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getFederateObjectMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> unfederateObject(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUnfederateObjectMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> notifyUnfederatedObjects(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyUnfederatedObjectsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNotifyUnfederatedObjectsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> notifyFederatedObjects(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyFederatedObjectsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNotifyFederatedObjectsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceResponse> checkObjectIsFederatedWithDataClayInstance(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCheckObjectIsFederatedWithDataClayInstanceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithResponse> getDataClaysObjectIsFederatedWith(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetDataClaysObjectIsFederatedWithMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectResponse> getExternalSourceDataClayOfObject(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetExternalSourceDataClayOfObjectMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> unfederateAllObjects(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUnfederateAllObjectsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> unfederateAllObjectsWithAllDCs(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsWithAllDCsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUnfederateAllObjectsWithAllDCsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> unfederateObjectWithAllDCs(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectWithAllDCsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUnfederateObjectWithAllDCsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> migrateFederatedObjects(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MigrateFederatedObjectsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getMigrateFederatedObjectsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> federateAllObjects(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateAllObjectsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getFederateAllObjectsMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Stubs
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsResponse> getStubs(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetStubsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsResponse> getBabelStubs(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetBabelStubsMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Notification Manager
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> registerECA(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterECARequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRegisterECAMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> adviseEvent(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AdviseEventRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAdviseEventMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Prefetching 
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.IsPrefetchingEnabledResponse> isPrefetchingEnabled(
        es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getIsPrefetchingEnabledMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Others
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSResponse> getClassNameForDS(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetClassNameForDSMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSResponse> getClassNameAndNamespaceForDS(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetClassNameAndNamespaceForDSMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderResponse> getContractIDOfDataClayProvider(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetContractIDOfDataClayProviderMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Testing
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayResponse> objectExistsInDataClay(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getObjectExistsInDataClayMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Others
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> closeSession(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CloseSessionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCloseSessionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSResponse> getMetadataByOIDForDS(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetMetadataByOIDForDSMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Paraver
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> activateTracing(
        es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ActivateTracingRequest request) {
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
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> cleanMetaDataCaches(
        es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getCleanMetaDataCachesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> closeManagerDb(
        es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getCloseManagerDbMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo> closeDb(
        es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getCloseDbMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_AUTOREGISTER_SL = 0;
  private static final int METHODID_AUTOREGISTER_EE = 1;
  private static final int METHODID_UNREGISTER_STORAGE_LOCATION = 2;
  private static final int METHODID_UNREGISTER_EXECUTION_ENVIRONMENT = 3;
  private static final int METHODID_CHECK_ALIVE = 4;
  private static final int METHODID_PERFORM_SET_OF_NEW_ACCOUNTS = 5;
  private static final int METHODID_PERFORM_SET_OF_OPERATIONS = 6;
  private static final int METHODID_PUBLISH_ADDRESS = 7;
  private static final int METHODID_NEW_ACCOUNT_NO_ADMIN = 8;
  private static final int METHODID_NEW_ACCOUNT = 9;
  private static final int METHODID_GET_ACCOUNT_ID = 10;
  private static final int METHODID_GET_ACCOUNT_LIST = 11;
  private static final int METHODID_NEW_SESSION = 12;
  private static final int METHODID_GET_INFO_OF_SESSION_FOR_DS = 13;
  private static final int METHODID_NEW_NAMESPACE = 14;
  private static final int METHODID_REMOVE_NAMESPACE = 15;
  private static final int METHODID_GET_NAMESPACE_ID = 16;
  private static final int METHODID_GET_NAMESPACE_LANG = 17;
  private static final int METHODID_GET_OBJECT_DATA_SET_ID = 18;
  private static final int METHODID_IMPORT_INTERFACE = 19;
  private static final int METHODID_IMPORT_CONTRACT = 20;
  private static final int METHODID_GET_INFO_OF_CLASSES_IN_NAMESPACE = 21;
  private static final int METHODID_GET_IMPORTED_CLASSES_INFO_IN_NAMESPACE = 22;
  private static final int METHODID_GET_CLASS_IDFROM_IMPORT = 23;
  private static final int METHODID_GET_NAMESPACES = 24;
  private static final int METHODID_NEW_DATA_SET = 25;
  private static final int METHODID_REMOVE_DATA_SET = 26;
  private static final int METHODID_GET_DATA_SET_ID = 27;
  private static final int METHODID_CHECK_DATA_SET_IS_PUBLIC = 28;
  private static final int METHODID_GET_PUBLIC_DATA_SETS = 29;
  private static final int METHODID_GET_ACCOUNT_DATA_SETS = 30;
  private static final int METHODID_NEW_CLASS = 31;
  private static final int METHODID_NEW_CLASS_ID = 32;
  private static final int METHODID_REMOVE_CLASS = 33;
  private static final int METHODID_REMOVE_OPERATION = 34;
  private static final int METHODID_REMOVE_IMPLEMENTATION = 35;
  private static final int METHODID_GET_OPERATION_ID = 36;
  private static final int METHODID_GET_PROPERTY_ID = 37;
  private static final int METHODID_GET_CLASS_ID = 38;
  private static final int METHODID_GET_CLASS_INFO = 39;
  private static final int METHODID_NEW_CONTRACT = 40;
  private static final int METHODID_REGISTER_TO_PUBLIC_CONTRACT = 41;
  private static final int METHODID_REGISTER_TO_PUBLIC_CONTRACT_OF_NAMESPACE = 42;
  private static final int METHODID_GET_CONTRACT_IDS_OF_APPLICANT = 43;
  private static final int METHODID_GET_CONTRACT_IDS_OF_PROVIDER = 44;
  private static final int METHODID_GET_CONTRACT_IDS_OF_APPLICANT_WITH_PROVIDER = 45;
  private static final int METHODID_NEW_DATA_CONTRACT = 46;
  private static final int METHODID_REGISTER_TO_PUBLIC_DATA_CONTRACT = 47;
  private static final int METHODID_GET_DATA_CONTRACT_IDS_OF_APPLICANT = 48;
  private static final int METHODID_GET_DATA_CONTRACT_IDS_OF_PROVIDER = 49;
  private static final int METHODID_GET_DATA_CONTRACT_INFO_OF_APPLICANT_WITH_PROVIDER = 50;
  private static final int METHODID_NEW_INTERFACE = 51;
  private static final int METHODID_GET_INTERFACE_INFO = 52;
  private static final int METHODID_REMOVE_INTERFACE = 53;
  private static final int METHODID_GET_EXECUTION_ENVIRONMENTS_INFO = 54;
  private static final int METHODID_GET_EXECUTION_ENVIRONMENTS_NAMES = 55;
  private static final int METHODID_GET_EXECUTION_ENVIRONMENT_FOR_DS = 56;
  private static final int METHODID_GET_STORAGE_LOCATION_FOR_DS = 57;
  private static final int METHODID_GET_OBJECT_INFO = 58;
  private static final int METHODID_GET_OBJECT_FROM_ALIAS = 59;
  private static final int METHODID_DELETE_ALIAS = 60;
  private static final int METHODID_GET_OBJECTS_META_DATA_INFO_OF_CLASS_FOR_NM = 61;
  private static final int METHODID_ADD_ALIAS = 62;
  private static final int METHODID_REGISTER_OBJECT_FROM_GC = 63;
  private static final int METHODID_UNREGISTER_OBJECTS = 64;
  private static final int METHODID_REGISTER_OBJECT = 65;
  private static final int METHODID_SET_DATA_SET_IDFROM_GARBAGE_COLLECTOR = 66;
  private static final int METHODID_SET_DATA_SET_ID = 67;
  private static final int METHODID_NEW_VERSION = 68;
  private static final int METHODID_CONSOLIDATE_VERSION = 69;
  private static final int METHODID_NEW_REPLICA = 70;
  private static final int METHODID_MOVE_OBJECT = 71;
  private static final int METHODID_SET_OBJECT_READ_ONLY = 72;
  private static final int METHODID_SET_OBJECT_READ_WRITE = 73;
  private static final int METHODID_GET_METADATA_BY_OID = 74;
  private static final int METHODID_EXECUTE_IMPLEMENTATION = 75;
  private static final int METHODID_EXECUTE_METHOD_ON_TARGET = 76;
  private static final int METHODID_SYNCHRONIZE_FEDERATED_OBJECT = 77;
  private static final int METHODID_GET_DATA_CLAY_ID = 78;
  private static final int METHODID_REGISTER_EXTERNAL_DATA_CLAY = 79;
  private static final int METHODID_REGISTER_EXTERNAL_DATA_CLAY_OVERRIDE_AUTHORITY = 80;
  private static final int METHODID_NOTIFY_REGISTRATION_OF_EXTERNAL_DATA_CLAY = 81;
  private static final int METHODID_GET_EXTERNAL_DATA_CLAY_INFO = 82;
  private static final int METHODID_GET_EXTERNAL_DATACLAY_ID = 83;
  private static final int METHODID_FEDERATE_OBJECT = 84;
  private static final int METHODID_UNFEDERATE_OBJECT = 85;
  private static final int METHODID_NOTIFY_UNFEDERATED_OBJECTS = 86;
  private static final int METHODID_NOTIFY_FEDERATED_OBJECTS = 87;
  private static final int METHODID_CHECK_OBJECT_IS_FEDERATED_WITH_DATA_CLAY_INSTANCE = 88;
  private static final int METHODID_GET_DATA_CLAYS_OBJECT_IS_FEDERATED_WITH = 89;
  private static final int METHODID_GET_EXTERNAL_SOURCE_DATA_CLAY_OF_OBJECT = 90;
  private static final int METHODID_UNFEDERATE_ALL_OBJECTS = 91;
  private static final int METHODID_UNFEDERATE_ALL_OBJECTS_WITH_ALL_DCS = 92;
  private static final int METHODID_UNFEDERATE_OBJECT_WITH_ALL_DCS = 93;
  private static final int METHODID_MIGRATE_FEDERATED_OBJECTS = 94;
  private static final int METHODID_FEDERATE_ALL_OBJECTS = 95;
  private static final int METHODID_GET_STUBS = 96;
  private static final int METHODID_GET_BABEL_STUBS = 97;
  private static final int METHODID_REGISTER_ECA = 98;
  private static final int METHODID_ADVISE_EVENT = 99;
  private static final int METHODID_IS_PREFETCHING_ENABLED = 100;
  private static final int METHODID_GET_CLASS_NAME_FOR_DS = 101;
  private static final int METHODID_GET_CLASS_NAME_AND_NAMESPACE_FOR_DS = 102;
  private static final int METHODID_GET_CONTRACT_IDOF_DATA_CLAY_PROVIDER = 103;
  private static final int METHODID_OBJECT_EXISTS_IN_DATA_CLAY = 104;
  private static final int METHODID_CLOSE_SESSION = 105;
  private static final int METHODID_GET_METADATA_BY_OIDFOR_DS = 106;
  private static final int METHODID_ACTIVATE_TRACING = 107;
  private static final int METHODID_DEACTIVATE_TRACING = 108;
  private static final int METHODID_GET_TRACES = 109;
  private static final int METHODID_CLEAN_META_DATA_CACHES = 110;
  private static final int METHODID_CLOSE_MANAGER_DB = 111;
  private static final int METHODID_CLOSE_DB = 112;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final LogicModuleImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(LogicModuleImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_AUTOREGISTER_SL:
          serviceImpl.autoregisterSL((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterSLRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_AUTOREGISTER_EE:
          serviceImpl.autoregisterEE((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEERequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AutoRegisterEEResponse>) responseObserver);
          break;
        case METHODID_UNREGISTER_STORAGE_LOCATION:
          serviceImpl.unregisterStorageLocation((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterStorageLocationRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_UNREGISTER_EXECUTION_ENVIRONMENT:
          serviceImpl.unregisterExecutionEnvironment((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterExecutionEnvironmentRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_CHECK_ALIVE:
          serviceImpl.checkAlive((es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_PERFORM_SET_OF_NEW_ACCOUNTS:
          serviceImpl.performSetOfNewAccounts((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetAccountsResponse>) responseObserver);
          break;
        case METHODID_PERFORM_SET_OF_OPERATIONS:
          serviceImpl.performSetOfOperations((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PerformSetOperationsResponse>) responseObserver);
          break;
        case METHODID_PUBLISH_ADDRESS:
          serviceImpl.publishAddress((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.PublishAddressRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_NEW_ACCOUNT_NO_ADMIN:
          serviceImpl.newAccountNoAdmin((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountNoAdminRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse>) responseObserver);
          break;
        case METHODID_NEW_ACCOUNT:
          serviceImpl.newAccount((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewAccountResponse>) responseObserver);
          break;
        case METHODID_GET_ACCOUNT_ID:
          serviceImpl.getAccountID((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountIDResponse>) responseObserver);
          break;
        case METHODID_GET_ACCOUNT_LIST:
          serviceImpl.getAccountList((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountListResponse>) responseObserver);
          break;
        case METHODID_NEW_SESSION:
          serviceImpl.newSession((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewSessionResponse>) responseObserver);
          break;
        case METHODID_GET_INFO_OF_SESSION_FOR_DS:
          serviceImpl.getInfoOfSessionForDS((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfSessionForDSResponse>) responseObserver);
          break;
        case METHODID_NEW_NAMESPACE:
          serviceImpl.newNamespace((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewNamespaceResponse>) responseObserver);
          break;
        case METHODID_REMOVE_NAMESPACE:
          serviceImpl.removeNamespace((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveNamespaceRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_GET_NAMESPACE_ID:
          serviceImpl.getNamespaceID((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceIDResponse>) responseObserver);
          break;
        case METHODID_GET_NAMESPACE_LANG:
          serviceImpl.getNamespaceLang((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespaceLangResponse>) responseObserver);
          break;
        case METHODID_GET_OBJECT_DATA_SET_ID:
          serviceImpl.getObjectDataSetID((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectDataSetIDResponse>) responseObserver);
          break;
        case METHODID_IMPORT_INTERFACE:
          serviceImpl.importInterface((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportInterfaceRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_IMPORT_CONTRACT:
          serviceImpl.importContract((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ImportContractRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_GET_INFO_OF_CLASSES_IN_NAMESPACE:
          serviceImpl.getInfoOfClassesInNamespace((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInfoOfClassesInNamespaceResponse>) responseObserver);
          break;
        case METHODID_GET_IMPORTED_CLASSES_INFO_IN_NAMESPACE:
          serviceImpl.getImportedClassesInfoInNamespace((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetImportedClassesInfoInNamespaceResponse>) responseObserver);
          break;
        case METHODID_GET_CLASS_IDFROM_IMPORT:
          serviceImpl.getClassIDfromImport((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDFromImportResponse>) responseObserver);
          break;
        case METHODID_GET_NAMESPACES:
          serviceImpl.getNamespaces((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetNamespacesResponse>) responseObserver);
          break;
        case METHODID_NEW_DATA_SET:
          serviceImpl.newDataSet((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataSetResponse>) responseObserver);
          break;
        case METHODID_REMOVE_DATA_SET:
          serviceImpl.removeDataSet((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveDataSetRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_GET_DATA_SET_ID:
          serviceImpl.getDataSetID((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataSetIDResponse>) responseObserver);
          break;
        case METHODID_CHECK_DATA_SET_IS_PUBLIC:
          serviceImpl.checkDataSetIsPublic((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckDataSetIsPublicResponse>) responseObserver);
          break;
        case METHODID_GET_PUBLIC_DATA_SETS:
          serviceImpl.getPublicDataSets((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPublicDataSetsResponse>) responseObserver);
          break;
        case METHODID_GET_ACCOUNT_DATA_SETS:
          serviceImpl.getAccountDataSets((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetAccountDataSetsResponse>) responseObserver);
          break;
        case METHODID_NEW_CLASS:
          serviceImpl.newClass((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassResponse>) responseObserver);
          break;
        case METHODID_NEW_CLASS_ID:
          serviceImpl.newClassID((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewClassIDResponse>) responseObserver);
          break;
        case METHODID_REMOVE_CLASS:
          serviceImpl.removeClass((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveClassRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_REMOVE_OPERATION:
          serviceImpl.removeOperation((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveOperationRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_REMOVE_IMPLEMENTATION:
          serviceImpl.removeImplementation((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveImplementationRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_GET_OPERATION_ID:
          serviceImpl.getOperationID((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetOperationIDResponse>) responseObserver);
          break;
        case METHODID_GET_PROPERTY_ID:
          serviceImpl.getPropertyID((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetPropertyIDResponse>) responseObserver);
          break;
        case METHODID_GET_CLASS_ID:
          serviceImpl.getClassID((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassIDResponse>) responseObserver);
          break;
        case METHODID_GET_CLASS_INFO:
          serviceImpl.getClassInfo((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassInfoResponse>) responseObserver);
          break;
        case METHODID_NEW_CONTRACT:
          serviceImpl.newContract((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewContractResponse>) responseObserver);
          break;
        case METHODID_REGISTER_TO_PUBLIC_CONTRACT:
          serviceImpl.registerToPublicContract((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_REGISTER_TO_PUBLIC_CONTRACT_OF_NAMESPACE:
          serviceImpl.registerToPublicContractOfNamespace((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicContractOfNamespaceResponse>) responseObserver);
          break;
        case METHODID_GET_CONTRACT_IDS_OF_APPLICANT:
          serviceImpl.getContractIDsOfApplicant((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfApplicantResponse>) responseObserver);
          break;
        case METHODID_GET_CONTRACT_IDS_OF_PROVIDER:
          serviceImpl.getContractIDsOfProvider((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDsOfProviderResponse>) responseObserver);
          break;
        case METHODID_GET_CONTRACT_IDS_OF_APPLICANT_WITH_PROVIDER:
          serviceImpl.getContractIDsOfApplicantWithProvider((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractsOfApplicantWithProvResponse>) responseObserver);
          break;
        case METHODID_NEW_DATA_CONTRACT:
          serviceImpl.newDataContract((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewDataContractResponse>) responseObserver);
          break;
        case METHODID_REGISTER_TO_PUBLIC_DATA_CONTRACT:
          serviceImpl.registerToPublicDataContract((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterToPublicDataContractRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_GET_DATA_CONTRACT_IDS_OF_APPLICANT:
          serviceImpl.getDataContractIDsOfApplicant((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfApplicantResponse>) responseObserver);
          break;
        case METHODID_GET_DATA_CONTRACT_IDS_OF_PROVIDER:
          serviceImpl.getDataContractIDsOfProvider((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractIDsOfProviderResponse>) responseObserver);
          break;
        case METHODID_GET_DATA_CONTRACT_INFO_OF_APPLICANT_WITH_PROVIDER:
          serviceImpl.getDataContractInfoOfApplicantWithProvider((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataContractInfoOfApplicantWithProvResponse>) responseObserver);
          break;
        case METHODID_NEW_INTERFACE:
          serviceImpl.newInterface((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewInterfaceResponse>) responseObserver);
          break;
        case METHODID_GET_INTERFACE_INFO:
          serviceImpl.getInterfaceInfo((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetInterfaceInfoResponse>) responseObserver);
          break;
        case METHODID_REMOVE_INTERFACE:
          serviceImpl.removeInterface((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RemoveInterfaceRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_GET_EXECUTION_ENVIRONMENTS_INFO:
          serviceImpl.getExecutionEnvironmentsInfo((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsInfoResponse>) responseObserver);
          break;
        case METHODID_GET_EXECUTION_ENVIRONMENTS_NAMES:
          serviceImpl.getExecutionEnvironmentsNames((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentsNamesResponse>) responseObserver);
          break;
        case METHODID_GET_EXECUTION_ENVIRONMENT_FOR_DS:
          serviceImpl.getExecutionEnvironmentForDS((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExecutionEnvironmentForDSResponse>) responseObserver);
          break;
        case METHODID_GET_STORAGE_LOCATION_FOR_DS:
          serviceImpl.getStorageLocationForDS((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStorageLocationForDSResponse>) responseObserver);
          break;
        case METHODID_GET_OBJECT_INFO:
          serviceImpl.getObjectInfo((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectInfoResponse>) responseObserver);
          break;
        case METHODID_GET_OBJECT_FROM_ALIAS:
          serviceImpl.getObjectFromAlias((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectFromAliasResponse>) responseObserver);
          break;
        case METHODID_DELETE_ALIAS:
          serviceImpl.deleteAlias((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.DeleteAliasRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_GET_OBJECTS_META_DATA_INFO_OF_CLASS_FOR_NM:
          serviceImpl.getObjectsMetaDataInfoOfClassForNM((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetObjectsMetaDataInfoOfClassForNMResponse>) responseObserver);
          break;
        case METHODID_ADD_ALIAS:
          serviceImpl.addAlias((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AddAliasRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_REGISTER_OBJECT_FROM_GC:
          serviceImpl.registerObjectFromGC((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectForGCRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_UNREGISTER_OBJECTS:
          serviceImpl.unregisterObjects((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnregisterObjectsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_REGISTER_OBJECT:
          serviceImpl.registerObject((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterObjectRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_SET_DATA_SET_IDFROM_GARBAGE_COLLECTOR:
          serviceImpl.setDataSetIDFromGarbageCollector((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDFromGarbageCollectorRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_SET_DATA_SET_ID:
          serviceImpl.setDataSetID((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetDataSetIDRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_NEW_VERSION:
          serviceImpl.newVersion((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewVersionResponse>) responseObserver);
          break;
        case METHODID_CONSOLIDATE_VERSION:
          serviceImpl.consolidateVersion((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ConsolidateVersionRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_NEW_REPLICA:
          serviceImpl.newReplica((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NewReplicaResponse>) responseObserver);
          break;
        case METHODID_MOVE_OBJECT:
          serviceImpl.moveObject((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MoveObjectResponse>) responseObserver);
          break;
        case METHODID_SET_OBJECT_READ_ONLY:
          serviceImpl.setObjectReadOnly((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadOnlyRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_SET_OBJECT_READ_WRITE:
          serviceImpl.setObjectReadWrite((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SetObjectReadWriteRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_GET_METADATA_BY_OID:
          serviceImpl.getMetadataByOID((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDResponse>) responseObserver);
          break;
        case METHODID_EXECUTE_IMPLEMENTATION:
          serviceImpl.executeImplementation((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteImplementationResponse>) responseObserver);
          break;
        case METHODID_EXECUTE_METHOD_ON_TARGET:
          serviceImpl.executeMethodOnTarget((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ExecuteMethodOnTargetResponse>) responseObserver);
          break;
        case METHODID_SYNCHRONIZE_FEDERATED_OBJECT:
          serviceImpl.synchronizeFederatedObject((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.SynchronizeFederatedObjectRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_GET_DATA_CLAY_ID:
          serviceImpl.getDataClayID((es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClayIDResponse>) responseObserver);
          break;
        case METHODID_REGISTER_EXTERNAL_DATA_CLAY:
          serviceImpl.registerExternalDataClay((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse>) responseObserver);
          break;
        case METHODID_REGISTER_EXTERNAL_DATA_CLAY_OVERRIDE_AUTHORITY:
          serviceImpl.registerExternalDataClayOverrideAuthority((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayOverrideAuthorityRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterExternalDataClayResponse>) responseObserver);
          break;
        case METHODID_NOTIFY_REGISTRATION_OF_EXTERNAL_DATA_CLAY:
          serviceImpl.notifyRegistrationOfExternalDataClay((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyRegistrationOfExternalDataClayResponse>) responseObserver);
          break;
        case METHODID_GET_EXTERNAL_DATA_CLAY_INFO:
          serviceImpl.getExternalDataClayInfo((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExtDataClayInfoResponse>) responseObserver);
          break;
        case METHODID_GET_EXTERNAL_DATACLAY_ID:
          serviceImpl.getExternalDataclayId((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalDataclayIDResponse>) responseObserver);
          break;
        case METHODID_FEDERATE_OBJECT:
          serviceImpl.federateObject((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateObjectRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_UNFEDERATE_OBJECT:
          serviceImpl.unfederateObject((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_NOTIFY_UNFEDERATED_OBJECTS:
          serviceImpl.notifyUnfederatedObjects((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyUnfederatedObjectsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_NOTIFY_FEDERATED_OBJECTS:
          serviceImpl.notifyFederatedObjects((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.NotifyFederatedObjectsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_CHECK_OBJECT_IS_FEDERATED_WITH_DATA_CLAY_INSTANCE:
          serviceImpl.checkObjectIsFederatedWithDataClayInstance((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CheckObjectFederatedWithDataClayInstanceResponse>) responseObserver);
          break;
        case METHODID_GET_DATA_CLAYS_OBJECT_IS_FEDERATED_WITH:
          serviceImpl.getDataClaysObjectIsFederatedWith((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetDataClaysObjectIsFederatedWithResponse>) responseObserver);
          break;
        case METHODID_GET_EXTERNAL_SOURCE_DATA_CLAY_OF_OBJECT:
          serviceImpl.getExternalSourceDataClayOfObject((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetExternalSourceDataClayOfObjectResponse>) responseObserver);
          break;
        case METHODID_UNFEDERATE_ALL_OBJECTS:
          serviceImpl.unfederateAllObjects((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_UNFEDERATE_ALL_OBJECTS_WITH_ALL_DCS:
          serviceImpl.unfederateAllObjectsWithAllDCs((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateAllObjectsWithAllDCsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_UNFEDERATE_OBJECT_WITH_ALL_DCS:
          serviceImpl.unfederateObjectWithAllDCs((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.UnfederateObjectWithAllDCsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_MIGRATE_FEDERATED_OBJECTS:
          serviceImpl.migrateFederatedObjects((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.MigrateFederatedObjectsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_FEDERATE_ALL_OBJECTS:
          serviceImpl.federateAllObjects((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.FederateAllObjectsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_GET_STUBS:
          serviceImpl.getStubs((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetStubsResponse>) responseObserver);
          break;
        case METHODID_GET_BABEL_STUBS:
          serviceImpl.getBabelStubs((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetBabelStubsResponse>) responseObserver);
          break;
        case METHODID_REGISTER_ECA:
          serviceImpl.registerECA((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.RegisterECARequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_ADVISE_EVENT:
          serviceImpl.adviseEvent((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.AdviseEventRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_IS_PREFETCHING_ENABLED:
          serviceImpl.isPrefetchingEnabled((es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.IsPrefetchingEnabledResponse>) responseObserver);
          break;
        case METHODID_GET_CLASS_NAME_FOR_DS:
          serviceImpl.getClassNameForDS((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameForDSResponse>) responseObserver);
          break;
        case METHODID_GET_CLASS_NAME_AND_NAMESPACE_FOR_DS:
          serviceImpl.getClassNameAndNamespaceForDS((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetClassNameAndNamespaceForDSResponse>) responseObserver);
          break;
        case METHODID_GET_CONTRACT_IDOF_DATA_CLAY_PROVIDER:
          serviceImpl.getContractIDOfDataClayProvider((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetContractIDOfDataClayProviderResponse>) responseObserver);
          break;
        case METHODID_OBJECT_EXISTS_IN_DATA_CLAY:
          serviceImpl.objectExistsInDataClay((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ObjectExistsInDataClayResponse>) responseObserver);
          break;
        case METHODID_CLOSE_SESSION:
          serviceImpl.closeSession((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.CloseSessionRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_GET_METADATA_BY_OIDFOR_DS:
          serviceImpl.getMetadataByOIDForDS((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSRequest) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.GetMetadataByOIDForDSResponse>) responseObserver);
          break;
        case METHODID_ACTIVATE_TRACING:
          serviceImpl.activateTracing((es.bsc.dataclay.communication.grpc.messages.logicmodule.LogicmoduleMessages.ActivateTracingRequest) request,
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
        case METHODID_CLEAN_META_DATA_CACHES:
          serviceImpl.cleanMetaDataCaches((es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_CLOSE_MANAGER_DB:
          serviceImpl.closeManagerDb((es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
          break;
        case METHODID_CLOSE_DB:
          serviceImpl.closeDb((es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.ExceptionInfo>) responseObserver);
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

  private static abstract class LogicModuleBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    LogicModuleBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return es.bsc.dataclay.communication.grpc.generated.logicmodule.LogicModuleGrpcService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("LogicModule");
    }
  }

  private static final class LogicModuleFileDescriptorSupplier
      extends LogicModuleBaseDescriptorSupplier {
    LogicModuleFileDescriptorSupplier() {}
  }

  private static final class LogicModuleMethodDescriptorSupplier
      extends LogicModuleBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    LogicModuleMethodDescriptorSupplier(String methodName) {
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
      synchronized (LogicModuleGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new LogicModuleFileDescriptorSupplier())
              .addMethod(getAutoregisterSLMethod())
              .addMethod(getAutoregisterEEMethod())
              .addMethod(getUnregisterStorageLocationMethod())
              .addMethod(getUnregisterExecutionEnvironmentMethod())
              .addMethod(getCheckAliveMethod())
              .addMethod(getPerformSetOfNewAccountsMethod())
              .addMethod(getPerformSetOfOperationsMethod())
              .addMethod(getPublishAddressMethod())
              .addMethod(getNewAccountNoAdminMethod())
              .addMethod(getNewAccountMethod())
              .addMethod(getGetAccountIDMethod())
              .addMethod(getGetAccountListMethod())
              .addMethod(getNewSessionMethod())
              .addMethod(getGetInfoOfSessionForDSMethod())
              .addMethod(getNewNamespaceMethod())
              .addMethod(getRemoveNamespaceMethod())
              .addMethod(getGetNamespaceIDMethod())
              .addMethod(getGetNamespaceLangMethod())
              .addMethod(getGetObjectDataSetIDMethod())
              .addMethod(getImportInterfaceMethod())
              .addMethod(getImportContractMethod())
              .addMethod(getGetInfoOfClassesInNamespaceMethod())
              .addMethod(getGetImportedClassesInfoInNamespaceMethod())
              .addMethod(getGetClassIDfromImportMethod())
              .addMethod(getGetNamespacesMethod())
              .addMethod(getNewDataSetMethod())
              .addMethod(getRemoveDataSetMethod())
              .addMethod(getGetDataSetIDMethod())
              .addMethod(getCheckDataSetIsPublicMethod())
              .addMethod(getGetPublicDataSetsMethod())
              .addMethod(getGetAccountDataSetsMethod())
              .addMethod(getNewClassMethod())
              .addMethod(getNewClassIDMethod())
              .addMethod(getRemoveClassMethod())
              .addMethod(getRemoveOperationMethod())
              .addMethod(getRemoveImplementationMethod())
              .addMethod(getGetOperationIDMethod())
              .addMethod(getGetPropertyIDMethod())
              .addMethod(getGetClassIDMethod())
              .addMethod(getGetClassInfoMethod())
              .addMethod(getNewContractMethod())
              .addMethod(getRegisterToPublicContractMethod())
              .addMethod(getRegisterToPublicContractOfNamespaceMethod())
              .addMethod(getGetContractIDsOfApplicantMethod())
              .addMethod(getGetContractIDsOfProviderMethod())
              .addMethod(getGetContractIDsOfApplicantWithProviderMethod())
              .addMethod(getNewDataContractMethod())
              .addMethod(getRegisterToPublicDataContractMethod())
              .addMethod(getGetDataContractIDsOfApplicantMethod())
              .addMethod(getGetDataContractIDsOfProviderMethod())
              .addMethod(getGetDataContractInfoOfApplicantWithProviderMethod())
              .addMethod(getNewInterfaceMethod())
              .addMethod(getGetInterfaceInfoMethod())
              .addMethod(getRemoveInterfaceMethod())
              .addMethod(getGetExecutionEnvironmentsInfoMethod())
              .addMethod(getGetExecutionEnvironmentsNamesMethod())
              .addMethod(getGetExecutionEnvironmentForDSMethod())
              .addMethod(getGetStorageLocationForDSMethod())
              .addMethod(getGetObjectInfoMethod())
              .addMethod(getGetObjectFromAliasMethod())
              .addMethod(getDeleteAliasMethod())
              .addMethod(getGetObjectsMetaDataInfoOfClassForNMMethod())
              .addMethod(getAddAliasMethod())
              .addMethod(getRegisterObjectFromGCMethod())
              .addMethod(getUnregisterObjectsMethod())
              .addMethod(getRegisterObjectMethod())
              .addMethod(getSetDataSetIDFromGarbageCollectorMethod())
              .addMethod(getSetDataSetIDMethod())
              .addMethod(getNewVersionMethod())
              .addMethod(getConsolidateVersionMethod())
              .addMethod(getNewReplicaMethod())
              .addMethod(getMoveObjectMethod())
              .addMethod(getSetObjectReadOnlyMethod())
              .addMethod(getSetObjectReadWriteMethod())
              .addMethod(getGetMetadataByOIDMethod())
              .addMethod(getExecuteImplementationMethod())
              .addMethod(getExecuteMethodOnTargetMethod())
              .addMethod(getSynchronizeFederatedObjectMethod())
              .addMethod(getGetDataClayIDMethod())
              .addMethod(getRegisterExternalDataClayMethod())
              .addMethod(getRegisterExternalDataClayOverrideAuthorityMethod())
              .addMethod(getNotifyRegistrationOfExternalDataClayMethod())
              .addMethod(getGetExternalDataClayInfoMethod())
              .addMethod(getGetExternalDataclayIdMethod())
              .addMethod(getFederateObjectMethod())
              .addMethod(getUnfederateObjectMethod())
              .addMethod(getNotifyUnfederatedObjectsMethod())
              .addMethod(getNotifyFederatedObjectsMethod())
              .addMethod(getCheckObjectIsFederatedWithDataClayInstanceMethod())
              .addMethod(getGetDataClaysObjectIsFederatedWithMethod())
              .addMethod(getGetExternalSourceDataClayOfObjectMethod())
              .addMethod(getUnfederateAllObjectsMethod())
              .addMethod(getUnfederateAllObjectsWithAllDCsMethod())
              .addMethod(getUnfederateObjectWithAllDCsMethod())
              .addMethod(getMigrateFederatedObjectsMethod())
              .addMethod(getFederateAllObjectsMethod())
              .addMethod(getGetStubsMethod())
              .addMethod(getGetBabelStubsMethod())
              .addMethod(getRegisterECAMethod())
              .addMethod(getAdviseEventMethod())
              .addMethod(getIsPrefetchingEnabledMethod())
              .addMethod(getGetClassNameForDSMethod())
              .addMethod(getGetClassNameAndNamespaceForDSMethod())
              .addMethod(getGetContractIDOfDataClayProviderMethod())
              .addMethod(getObjectExistsInDataClayMethod())
              .addMethod(getCloseSessionMethod())
              .addMethod(getGetMetadataByOIDForDSMethod())
              .addMethod(getActivateTracingMethod())
              .addMethod(getDeactivateTracingMethod())
              .addMethod(getGetTracesMethod())
              .addMethod(getCleanMetaDataCachesMethod())
              .addMethod(getCloseManagerDbMethod())
              .addMethod(getCloseDbMethod())
              .build();
        }
      }
    }
    return result;
  }
}
