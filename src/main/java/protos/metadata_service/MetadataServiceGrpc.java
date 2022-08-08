package protos.metadata_service;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.45.0)",
    comments = "Source: protos/metadata_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class MetadataServiceGrpc {

  private MetadataServiceGrpc() {}

  public static final String SERVICE_NAME = "protos.metadata_service.MetadataService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.NewAccountRequest,
      com.google.protobuf.Empty> getNewAccountMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "NewAccount",
      requestType = protos.metadata_service.MetadataServiceOuterClass.NewAccountRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.NewAccountRequest,
      com.google.protobuf.Empty> getNewAccountMethod() {
    io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.NewAccountRequest, com.google.protobuf.Empty> getNewAccountMethod;
    if ((getNewAccountMethod = MetadataServiceGrpc.getNewAccountMethod) == null) {
      synchronized (MetadataServiceGrpc.class) {
        if ((getNewAccountMethod = MetadataServiceGrpc.getNewAccountMethod) == null) {
          MetadataServiceGrpc.getNewAccountMethod = getNewAccountMethod =
              io.grpc.MethodDescriptor.<protos.metadata_service.MetadataServiceOuterClass.NewAccountRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "NewAccount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.metadata_service.MetadataServiceOuterClass.NewAccountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new MetadataServiceMethodDescriptorSupplier("NewAccount"))
              .build();
        }
      }
    }
    return getNewAccountMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.GetAccountRequest,
      protos.metadata_service.MetadataServiceOuterClass.GetAccountResponse> getGetAccountMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAccount",
      requestType = protos.metadata_service.MetadataServiceOuterClass.GetAccountRequest.class,
      responseType = protos.metadata_service.MetadataServiceOuterClass.GetAccountResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.GetAccountRequest,
      protos.metadata_service.MetadataServiceOuterClass.GetAccountResponse> getGetAccountMethod() {
    io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.GetAccountRequest, protos.metadata_service.MetadataServiceOuterClass.GetAccountResponse> getGetAccountMethod;
    if ((getGetAccountMethod = MetadataServiceGrpc.getGetAccountMethod) == null) {
      synchronized (MetadataServiceGrpc.class) {
        if ((getGetAccountMethod = MetadataServiceGrpc.getGetAccountMethod) == null) {
          MetadataServiceGrpc.getGetAccountMethod = getGetAccountMethod =
              io.grpc.MethodDescriptor.<protos.metadata_service.MetadataServiceOuterClass.GetAccountRequest, protos.metadata_service.MetadataServiceOuterClass.GetAccountResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAccount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.metadata_service.MetadataServiceOuterClass.GetAccountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.metadata_service.MetadataServiceOuterClass.GetAccountResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MetadataServiceMethodDescriptorSupplier("GetAccount"))
              .build();
        }
      }
    }
    return getGetAccountMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.NewSessionRequest,
      protos.metadata_service.MetadataServiceOuterClass.NewSessionResponse> getNewSessionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "NewSession",
      requestType = protos.metadata_service.MetadataServiceOuterClass.NewSessionRequest.class,
      responseType = protos.metadata_service.MetadataServiceOuterClass.NewSessionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.NewSessionRequest,
      protos.metadata_service.MetadataServiceOuterClass.NewSessionResponse> getNewSessionMethod() {
    io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.NewSessionRequest, protos.metadata_service.MetadataServiceOuterClass.NewSessionResponse> getNewSessionMethod;
    if ((getNewSessionMethod = MetadataServiceGrpc.getNewSessionMethod) == null) {
      synchronized (MetadataServiceGrpc.class) {
        if ((getNewSessionMethod = MetadataServiceGrpc.getNewSessionMethod) == null) {
          MetadataServiceGrpc.getNewSessionMethod = getNewSessionMethod =
              io.grpc.MethodDescriptor.<protos.metadata_service.MetadataServiceOuterClass.NewSessionRequest, protos.metadata_service.MetadataServiceOuterClass.NewSessionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "NewSession"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.metadata_service.MetadataServiceOuterClass.NewSessionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.metadata_service.MetadataServiceOuterClass.NewSessionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MetadataServiceMethodDescriptorSupplier("NewSession"))
              .build();
        }
      }
    }
    return getNewSessionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.CloseSessionRequest,
      com.google.protobuf.Empty> getCloseSessionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CloseSession",
      requestType = protos.metadata_service.MetadataServiceOuterClass.CloseSessionRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.CloseSessionRequest,
      com.google.protobuf.Empty> getCloseSessionMethod() {
    io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.CloseSessionRequest, com.google.protobuf.Empty> getCloseSessionMethod;
    if ((getCloseSessionMethod = MetadataServiceGrpc.getCloseSessionMethod) == null) {
      synchronized (MetadataServiceGrpc.class) {
        if ((getCloseSessionMethod = MetadataServiceGrpc.getCloseSessionMethod) == null) {
          MetadataServiceGrpc.getCloseSessionMethod = getCloseSessionMethod =
              io.grpc.MethodDescriptor.<protos.metadata_service.MetadataServiceOuterClass.CloseSessionRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CloseSession"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.metadata_service.MetadataServiceOuterClass.CloseSessionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new MetadataServiceMethodDescriptorSupplier("CloseSession"))
              .build();
        }
      }
    }
    return getCloseSessionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.NewDatasetRequest,
      com.google.protobuf.Empty> getNewDatasetMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "NewDataset",
      requestType = protos.metadata_service.MetadataServiceOuterClass.NewDatasetRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.NewDatasetRequest,
      com.google.protobuf.Empty> getNewDatasetMethod() {
    io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.NewDatasetRequest, com.google.protobuf.Empty> getNewDatasetMethod;
    if ((getNewDatasetMethod = MetadataServiceGrpc.getNewDatasetMethod) == null) {
      synchronized (MetadataServiceGrpc.class) {
        if ((getNewDatasetMethod = MetadataServiceGrpc.getNewDatasetMethod) == null) {
          MetadataServiceGrpc.getNewDatasetMethod = getNewDatasetMethod =
              io.grpc.MethodDescriptor.<protos.metadata_service.MetadataServiceOuterClass.NewDatasetRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "NewDataset"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.metadata_service.MetadataServiceOuterClass.NewDatasetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new MetadataServiceMethodDescriptorSupplier("NewDataset"))
              .build();
        }
      }
    }
    return getNewDatasetMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsRequest,
      protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsResponse> getGetAllExecutionEnvironmentsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAllExecutionEnvironments",
      requestType = protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsRequest.class,
      responseType = protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsRequest,
      protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsResponse> getGetAllExecutionEnvironmentsMethod() {
    io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsRequest, protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsResponse> getGetAllExecutionEnvironmentsMethod;
    if ((getGetAllExecutionEnvironmentsMethod = MetadataServiceGrpc.getGetAllExecutionEnvironmentsMethod) == null) {
      synchronized (MetadataServiceGrpc.class) {
        if ((getGetAllExecutionEnvironmentsMethod = MetadataServiceGrpc.getGetAllExecutionEnvironmentsMethod) == null) {
          MetadataServiceGrpc.getGetAllExecutionEnvironmentsMethod = getGetAllExecutionEnvironmentsMethod =
              io.grpc.MethodDescriptor.<protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsRequest, protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAllExecutionEnvironments"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MetadataServiceMethodDescriptorSupplier("GetAllExecutionEnvironments"))
              .build();
        }
      }
    }
    return getGetAllExecutionEnvironmentsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      protos.metadata_service.MetadataServiceOuterClass.GetDataclayIDResponse> getGetDataclayIDMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetDataclayID",
      requestType = com.google.protobuf.Empty.class,
      responseType = protos.metadata_service.MetadataServiceOuterClass.GetDataclayIDResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      protos.metadata_service.MetadataServiceOuterClass.GetDataclayIDResponse> getGetDataclayIDMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, protos.metadata_service.MetadataServiceOuterClass.GetDataclayIDResponse> getGetDataclayIDMethod;
    if ((getGetDataclayIDMethod = MetadataServiceGrpc.getGetDataclayIDMethod) == null) {
      synchronized (MetadataServiceGrpc.class) {
        if ((getGetDataclayIDMethod = MetadataServiceGrpc.getGetDataclayIDMethod) == null) {
          MetadataServiceGrpc.getGetDataclayIDMethod = getGetDataclayIDMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, protos.metadata_service.MetadataServiceOuterClass.GetDataclayIDResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetDataclayID"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.metadata_service.MetadataServiceOuterClass.GetDataclayIDResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MetadataServiceMethodDescriptorSupplier("GetDataclayID"))
              .build();
        }
      }
    }
    return getGetDataclayIDMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.AutoRegisterEERequest,
      com.google.protobuf.Empty> getAutoregisterEEMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AutoregisterEE",
      requestType = protos.metadata_service.MetadataServiceOuterClass.AutoRegisterEERequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.AutoRegisterEERequest,
      com.google.protobuf.Empty> getAutoregisterEEMethod() {
    io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.AutoRegisterEERequest, com.google.protobuf.Empty> getAutoregisterEEMethod;
    if ((getAutoregisterEEMethod = MetadataServiceGrpc.getAutoregisterEEMethod) == null) {
      synchronized (MetadataServiceGrpc.class) {
        if ((getAutoregisterEEMethod = MetadataServiceGrpc.getAutoregisterEEMethod) == null) {
          MetadataServiceGrpc.getAutoregisterEEMethod = getAutoregisterEEMethod =
              io.grpc.MethodDescriptor.<protos.metadata_service.MetadataServiceOuterClass.AutoRegisterEERequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AutoregisterEE"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.metadata_service.MetadataServiceOuterClass.AutoRegisterEERequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new MetadataServiceMethodDescriptorSupplier("AutoregisterEE"))
              .build();
        }
      }
    }
    return getAutoregisterEEMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.RegisterObjectRequest,
      com.google.protobuf.Empty> getRegisterObjectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegisterObject",
      requestType = protos.metadata_service.MetadataServiceOuterClass.RegisterObjectRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.RegisterObjectRequest,
      com.google.protobuf.Empty> getRegisterObjectMethod() {
    io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.RegisterObjectRequest, com.google.protobuf.Empty> getRegisterObjectMethod;
    if ((getRegisterObjectMethod = MetadataServiceGrpc.getRegisterObjectMethod) == null) {
      synchronized (MetadataServiceGrpc.class) {
        if ((getRegisterObjectMethod = MetadataServiceGrpc.getRegisterObjectMethod) == null) {
          MetadataServiceGrpc.getRegisterObjectMethod = getRegisterObjectMethod =
              io.grpc.MethodDescriptor.<protos.metadata_service.MetadataServiceOuterClass.RegisterObjectRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegisterObject"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.metadata_service.MetadataServiceOuterClass.RegisterObjectRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new MetadataServiceMethodDescriptorSupplier("RegisterObject"))
              .build();
        }
      }
    }
    return getRegisterObjectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasRequest,
      protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasResponse> getGetObjectFromAliasMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetObjectFromAlias",
      requestType = protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasRequest.class,
      responseType = protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasRequest,
      protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasResponse> getGetObjectFromAliasMethod() {
    io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasRequest, protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasResponse> getGetObjectFromAliasMethod;
    if ((getGetObjectFromAliasMethod = MetadataServiceGrpc.getGetObjectFromAliasMethod) == null) {
      synchronized (MetadataServiceGrpc.class) {
        if ((getGetObjectFromAliasMethod = MetadataServiceGrpc.getGetObjectFromAliasMethod) == null) {
          MetadataServiceGrpc.getGetObjectFromAliasMethod = getGetObjectFromAliasMethod =
              io.grpc.MethodDescriptor.<protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasRequest, protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetObjectFromAlias"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MetadataServiceMethodDescriptorSupplier("GetObjectFromAlias"))
              .build();
        }
      }
    }
    return getGetObjectFromAliasMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.DeleteAliasRequest,
      com.google.protobuf.Empty> getDeleteAliasMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteAlias",
      requestType = protos.metadata_service.MetadataServiceOuterClass.DeleteAliasRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.DeleteAliasRequest,
      com.google.protobuf.Empty> getDeleteAliasMethod() {
    io.grpc.MethodDescriptor<protos.metadata_service.MetadataServiceOuterClass.DeleteAliasRequest, com.google.protobuf.Empty> getDeleteAliasMethod;
    if ((getDeleteAliasMethod = MetadataServiceGrpc.getDeleteAliasMethod) == null) {
      synchronized (MetadataServiceGrpc.class) {
        if ((getDeleteAliasMethod = MetadataServiceGrpc.getDeleteAliasMethod) == null) {
          MetadataServiceGrpc.getDeleteAliasMethod = getDeleteAliasMethod =
              io.grpc.MethodDescriptor.<protos.metadata_service.MetadataServiceOuterClass.DeleteAliasRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteAlias"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.metadata_service.MetadataServiceOuterClass.DeleteAliasRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new MetadataServiceMethodDescriptorSupplier("DeleteAlias"))
              .build();
        }
      }
    }
    return getDeleteAliasMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MetadataServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MetadataServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MetadataServiceStub>() {
        @java.lang.Override
        public MetadataServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MetadataServiceStub(channel, callOptions);
        }
      };
    return MetadataServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MetadataServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MetadataServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MetadataServiceBlockingStub>() {
        @java.lang.Override
        public MetadataServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MetadataServiceBlockingStub(channel, callOptions);
        }
      };
    return MetadataServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MetadataServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MetadataServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MetadataServiceFutureStub>() {
        @java.lang.Override
        public MetadataServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MetadataServiceFutureStub(channel, callOptions);
        }
      };
    return MetadataServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class MetadataServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Account Manager
     * </pre>
     */
    public void newAccount(protos.metadata_service.MetadataServiceOuterClass.NewAccountRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getNewAccountMethod(), responseObserver);
    }

    /**
     */
    public void getAccount(protos.metadata_service.MetadataServiceOuterClass.GetAccountRequest request,
        io.grpc.stub.StreamObserver<protos.metadata_service.MetadataServiceOuterClass.GetAccountResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAccountMethod(), responseObserver);
    }

    /**
     * <pre>
     * Session Manager
     * </pre>
     */
    public void newSession(protos.metadata_service.MetadataServiceOuterClass.NewSessionRequest request,
        io.grpc.stub.StreamObserver<protos.metadata_service.MetadataServiceOuterClass.NewSessionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getNewSessionMethod(), responseObserver);
    }

    /**
     */
    public void closeSession(protos.metadata_service.MetadataServiceOuterClass.CloseSessionRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCloseSessionMethod(), responseObserver);
    }

    /**
     * <pre>
     * Dataset Manager
     * </pre>
     */
    public void newDataset(protos.metadata_service.MetadataServiceOuterClass.NewDatasetRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getNewDatasetMethod(), responseObserver);
    }

    /**
     * <pre>
     * EE-SL information
     * </pre>
     */
    public void getAllExecutionEnvironments(protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsRequest request,
        io.grpc.stub.StreamObserver<protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAllExecutionEnvironmentsMethod(), responseObserver);
    }

    /**
     * <pre>
     * Federation
     * </pre>
     */
    public void getDataclayID(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<protos.metadata_service.MetadataServiceOuterClass.GetDataclayIDResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetDataclayIDMethod(), responseObserver);
    }

    /**
     * <pre>
     * Autoregister
     * </pre>
     */
    public void autoregisterEE(protos.metadata_service.MetadataServiceOuterClass.AutoRegisterEERequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAutoregisterEEMethod(), responseObserver);
    }

    /**
     * <pre>
     * Object Metadata
     * </pre>
     */
    public void registerObject(protos.metadata_service.MetadataServiceOuterClass.RegisterObjectRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterObjectMethod(), responseObserver);
    }

    /**
     */
    public void getObjectFromAlias(protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasRequest request,
        io.grpc.stub.StreamObserver<protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetObjectFromAliasMethod(), responseObserver);
    }

    /**
     */
    public void deleteAlias(protos.metadata_service.MetadataServiceOuterClass.DeleteAliasRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteAliasMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getNewAccountMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                protos.metadata_service.MetadataServiceOuterClass.NewAccountRequest,
                com.google.protobuf.Empty>(
                  this, METHODID_NEW_ACCOUNT)))
          .addMethod(
            getGetAccountMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                protos.metadata_service.MetadataServiceOuterClass.GetAccountRequest,
                protos.metadata_service.MetadataServiceOuterClass.GetAccountResponse>(
                  this, METHODID_GET_ACCOUNT)))
          .addMethod(
            getNewSessionMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                protos.metadata_service.MetadataServiceOuterClass.NewSessionRequest,
                protos.metadata_service.MetadataServiceOuterClass.NewSessionResponse>(
                  this, METHODID_NEW_SESSION)))
          .addMethod(
            getCloseSessionMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                protos.metadata_service.MetadataServiceOuterClass.CloseSessionRequest,
                com.google.protobuf.Empty>(
                  this, METHODID_CLOSE_SESSION)))
          .addMethod(
            getNewDatasetMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                protos.metadata_service.MetadataServiceOuterClass.NewDatasetRequest,
                com.google.protobuf.Empty>(
                  this, METHODID_NEW_DATASET)))
          .addMethod(
            getGetAllExecutionEnvironmentsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsRequest,
                protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsResponse>(
                  this, METHODID_GET_ALL_EXECUTION_ENVIRONMENTS)))
          .addMethod(
            getGetDataclayIDMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                protos.metadata_service.MetadataServiceOuterClass.GetDataclayIDResponse>(
                  this, METHODID_GET_DATACLAY_ID)))
          .addMethod(
            getAutoregisterEEMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                protos.metadata_service.MetadataServiceOuterClass.AutoRegisterEERequest,
                com.google.protobuf.Empty>(
                  this, METHODID_AUTOREGISTER_EE)))
          .addMethod(
            getRegisterObjectMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                protos.metadata_service.MetadataServiceOuterClass.RegisterObjectRequest,
                com.google.protobuf.Empty>(
                  this, METHODID_REGISTER_OBJECT)))
          .addMethod(
            getGetObjectFromAliasMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasRequest,
                protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasResponse>(
                  this, METHODID_GET_OBJECT_FROM_ALIAS)))
          .addMethod(
            getDeleteAliasMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                protos.metadata_service.MetadataServiceOuterClass.DeleteAliasRequest,
                com.google.protobuf.Empty>(
                  this, METHODID_DELETE_ALIAS)))
          .build();
    }
  }

  /**
   */
  public static final class MetadataServiceStub extends io.grpc.stub.AbstractAsyncStub<MetadataServiceStub> {
    private MetadataServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MetadataServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MetadataServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Account Manager
     * </pre>
     */
    public void newAccount(protos.metadata_service.MetadataServiceOuterClass.NewAccountRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getNewAccountMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAccount(protos.metadata_service.MetadataServiceOuterClass.GetAccountRequest request,
        io.grpc.stub.StreamObserver<protos.metadata_service.MetadataServiceOuterClass.GetAccountResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAccountMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Session Manager
     * </pre>
     */
    public void newSession(protos.metadata_service.MetadataServiceOuterClass.NewSessionRequest request,
        io.grpc.stub.StreamObserver<protos.metadata_service.MetadataServiceOuterClass.NewSessionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getNewSessionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void closeSession(protos.metadata_service.MetadataServiceOuterClass.CloseSessionRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCloseSessionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Dataset Manager
     * </pre>
     */
    public void newDataset(protos.metadata_service.MetadataServiceOuterClass.NewDatasetRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getNewDatasetMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * EE-SL information
     * </pre>
     */
    public void getAllExecutionEnvironments(protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsRequest request,
        io.grpc.stub.StreamObserver<protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAllExecutionEnvironmentsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Federation
     * </pre>
     */
    public void getDataclayID(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<protos.metadata_service.MetadataServiceOuterClass.GetDataclayIDResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetDataclayIDMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Autoregister
     * </pre>
     */
    public void autoregisterEE(protos.metadata_service.MetadataServiceOuterClass.AutoRegisterEERequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAutoregisterEEMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Object Metadata
     * </pre>
     */
    public void registerObject(protos.metadata_service.MetadataServiceOuterClass.RegisterObjectRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterObjectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getObjectFromAlias(protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasRequest request,
        io.grpc.stub.StreamObserver<protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetObjectFromAliasMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteAlias(protos.metadata_service.MetadataServiceOuterClass.DeleteAliasRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteAliasMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class MetadataServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<MetadataServiceBlockingStub> {
    private MetadataServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MetadataServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MetadataServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Account Manager
     * </pre>
     */
    public com.google.protobuf.Empty newAccount(protos.metadata_service.MetadataServiceOuterClass.NewAccountRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getNewAccountMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.metadata_service.MetadataServiceOuterClass.GetAccountResponse getAccount(protos.metadata_service.MetadataServiceOuterClass.GetAccountRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAccountMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Session Manager
     * </pre>
     */
    public protos.metadata_service.MetadataServiceOuterClass.NewSessionResponse newSession(protos.metadata_service.MetadataServiceOuterClass.NewSessionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getNewSessionMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty closeSession(protos.metadata_service.MetadataServiceOuterClass.CloseSessionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCloseSessionMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Dataset Manager
     * </pre>
     */
    public com.google.protobuf.Empty newDataset(protos.metadata_service.MetadataServiceOuterClass.NewDatasetRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getNewDatasetMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * EE-SL information
     * </pre>
     */
    public protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsResponse getAllExecutionEnvironments(protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAllExecutionEnvironmentsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Federation
     * </pre>
     */
    public protos.metadata_service.MetadataServiceOuterClass.GetDataclayIDResponse getDataclayID(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetDataclayIDMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Autoregister
     * </pre>
     */
    public com.google.protobuf.Empty autoregisterEE(protos.metadata_service.MetadataServiceOuterClass.AutoRegisterEERequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAutoregisterEEMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Object Metadata
     * </pre>
     */
    public com.google.protobuf.Empty registerObject(protos.metadata_service.MetadataServiceOuterClass.RegisterObjectRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterObjectMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasResponse getObjectFromAlias(protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetObjectFromAliasMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty deleteAlias(protos.metadata_service.MetadataServiceOuterClass.DeleteAliasRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteAliasMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class MetadataServiceFutureStub extends io.grpc.stub.AbstractFutureStub<MetadataServiceFutureStub> {
    private MetadataServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MetadataServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MetadataServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Account Manager
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> newAccount(
        protos.metadata_service.MetadataServiceOuterClass.NewAccountRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getNewAccountMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.metadata_service.MetadataServiceOuterClass.GetAccountResponse> getAccount(
        protos.metadata_service.MetadataServiceOuterClass.GetAccountRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAccountMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Session Manager
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.metadata_service.MetadataServiceOuterClass.NewSessionResponse> newSession(
        protos.metadata_service.MetadataServiceOuterClass.NewSessionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getNewSessionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> closeSession(
        protos.metadata_service.MetadataServiceOuterClass.CloseSessionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCloseSessionMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Dataset Manager
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> newDataset(
        protos.metadata_service.MetadataServiceOuterClass.NewDatasetRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getNewDatasetMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * EE-SL information
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsResponse> getAllExecutionEnvironments(
        protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAllExecutionEnvironmentsMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Federation
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.metadata_service.MetadataServiceOuterClass.GetDataclayIDResponse> getDataclayID(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetDataclayIDMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Autoregister
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> autoregisterEE(
        protos.metadata_service.MetadataServiceOuterClass.AutoRegisterEERequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAutoregisterEEMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Object Metadata
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> registerObject(
        protos.metadata_service.MetadataServiceOuterClass.RegisterObjectRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterObjectMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasResponse> getObjectFromAlias(
        protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetObjectFromAliasMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> deleteAlias(
        protos.metadata_service.MetadataServiceOuterClass.DeleteAliasRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteAliasMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_NEW_ACCOUNT = 0;
  private static final int METHODID_GET_ACCOUNT = 1;
  private static final int METHODID_NEW_SESSION = 2;
  private static final int METHODID_CLOSE_SESSION = 3;
  private static final int METHODID_NEW_DATASET = 4;
  private static final int METHODID_GET_ALL_EXECUTION_ENVIRONMENTS = 5;
  private static final int METHODID_GET_DATACLAY_ID = 6;
  private static final int METHODID_AUTOREGISTER_EE = 7;
  private static final int METHODID_REGISTER_OBJECT = 8;
  private static final int METHODID_GET_OBJECT_FROM_ALIAS = 9;
  private static final int METHODID_DELETE_ALIAS = 10;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MetadataServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MetadataServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_NEW_ACCOUNT:
          serviceImpl.newAccount((protos.metadata_service.MetadataServiceOuterClass.NewAccountRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_GET_ACCOUNT:
          serviceImpl.getAccount((protos.metadata_service.MetadataServiceOuterClass.GetAccountRequest) request,
              (io.grpc.stub.StreamObserver<protos.metadata_service.MetadataServiceOuterClass.GetAccountResponse>) responseObserver);
          break;
        case METHODID_NEW_SESSION:
          serviceImpl.newSession((protos.metadata_service.MetadataServiceOuterClass.NewSessionRequest) request,
              (io.grpc.stub.StreamObserver<protos.metadata_service.MetadataServiceOuterClass.NewSessionResponse>) responseObserver);
          break;
        case METHODID_CLOSE_SESSION:
          serviceImpl.closeSession((protos.metadata_service.MetadataServiceOuterClass.CloseSessionRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_NEW_DATASET:
          serviceImpl.newDataset((protos.metadata_service.MetadataServiceOuterClass.NewDatasetRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_GET_ALL_EXECUTION_ENVIRONMENTS:
          serviceImpl.getAllExecutionEnvironments((protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsRequest) request,
              (io.grpc.stub.StreamObserver<protos.metadata_service.MetadataServiceOuterClass.GetAllExecutionEnvironmentsResponse>) responseObserver);
          break;
        case METHODID_GET_DATACLAY_ID:
          serviceImpl.getDataclayID((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<protos.metadata_service.MetadataServiceOuterClass.GetDataclayIDResponse>) responseObserver);
          break;
        case METHODID_AUTOREGISTER_EE:
          serviceImpl.autoregisterEE((protos.metadata_service.MetadataServiceOuterClass.AutoRegisterEERequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_REGISTER_OBJECT:
          serviceImpl.registerObject((protos.metadata_service.MetadataServiceOuterClass.RegisterObjectRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_GET_OBJECT_FROM_ALIAS:
          serviceImpl.getObjectFromAlias((protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasRequest) request,
              (io.grpc.stub.StreamObserver<protos.metadata_service.MetadataServiceOuterClass.GetObjectFromAliasResponse>) responseObserver);
          break;
        case METHODID_DELETE_ALIAS:
          serviceImpl.deleteAlias((protos.metadata_service.MetadataServiceOuterClass.DeleteAliasRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
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

  private static abstract class MetadataServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MetadataServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return protos.metadata_service.MetadataServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MetadataService");
    }
  }

  private static final class MetadataServiceFileDescriptorSupplier
      extends MetadataServiceBaseDescriptorSupplier {
    MetadataServiceFileDescriptorSupplier() {}
  }

  private static final class MetadataServiceMethodDescriptorSupplier
      extends MetadataServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MetadataServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (MetadataServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MetadataServiceFileDescriptorSupplier())
              .addMethod(getNewAccountMethod())
              .addMethod(getGetAccountMethod())
              .addMethod(getNewSessionMethod())
              .addMethod(getCloseSessionMethod())
              .addMethod(getNewDatasetMethod())
              .addMethod(getGetAllExecutionEnvironmentsMethod())
              .addMethod(getGetDataclayIDMethod())
              .addMethod(getAutoregisterEEMethod())
              .addMethod(getRegisterObjectMethod())
              .addMethod(getGetObjectFromAliasMethod())
              .addMethod(getDeleteAliasMethod())
              .build();
        }
      }
    }
    return result;
  }
}
