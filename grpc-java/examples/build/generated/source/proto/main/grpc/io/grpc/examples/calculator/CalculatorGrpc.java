package io.grpc.examples.calculator;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * The calculator service definition.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.66.0)",
    comments = "Source: calculator.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class CalculatorGrpc {

  private CalculatorGrpc() {}

  public static final java.lang.String SERVICE_NAME = "calculator.Calculator";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<io.grpc.examples.calculator.CalculatorRequest,
      io.grpc.examples.calculator.CalculatorReply> getAddMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "add",
      requestType = io.grpc.examples.calculator.CalculatorRequest.class,
      responseType = io.grpc.examples.calculator.CalculatorReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<io.grpc.examples.calculator.CalculatorRequest,
      io.grpc.examples.calculator.CalculatorReply> getAddMethod() {
    io.grpc.MethodDescriptor<io.grpc.examples.calculator.CalculatorRequest, io.grpc.examples.calculator.CalculatorReply> getAddMethod;
    if ((getAddMethod = CalculatorGrpc.getAddMethod) == null) {
      synchronized (CalculatorGrpc.class) {
        if ((getAddMethod = CalculatorGrpc.getAddMethod) == null) {
          CalculatorGrpc.getAddMethod = getAddMethod =
              io.grpc.MethodDescriptor.<io.grpc.examples.calculator.CalculatorRequest, io.grpc.examples.calculator.CalculatorReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "add"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.calculator.CalculatorRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.calculator.CalculatorReply.getDefaultInstance()))
              .setSchemaDescriptor(new CalculatorMethodDescriptorSupplier("add"))
              .build();
        }
      }
    }
    return getAddMethod;
  }

  private static volatile io.grpc.MethodDescriptor<io.grpc.examples.calculator.CalculatorRequest,
      io.grpc.examples.calculator.CalculatorReply> getSubMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sub",
      requestType = io.grpc.examples.calculator.CalculatorRequest.class,
      responseType = io.grpc.examples.calculator.CalculatorReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<io.grpc.examples.calculator.CalculatorRequest,
      io.grpc.examples.calculator.CalculatorReply> getSubMethod() {
    io.grpc.MethodDescriptor<io.grpc.examples.calculator.CalculatorRequest, io.grpc.examples.calculator.CalculatorReply> getSubMethod;
    if ((getSubMethod = CalculatorGrpc.getSubMethod) == null) {
      synchronized (CalculatorGrpc.class) {
        if ((getSubMethod = CalculatorGrpc.getSubMethod) == null) {
          CalculatorGrpc.getSubMethod = getSubMethod =
              io.grpc.MethodDescriptor.<io.grpc.examples.calculator.CalculatorRequest, io.grpc.examples.calculator.CalculatorReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sub"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.calculator.CalculatorRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.calculator.CalculatorReply.getDefaultInstance()))
              .setSchemaDescriptor(new CalculatorMethodDescriptorSupplier("sub"))
              .build();
        }
      }
    }
    return getSubMethod;
  }

  private static volatile io.grpc.MethodDescriptor<io.grpc.examples.calculator.CalculatorRequest,
      io.grpc.examples.calculator.CalculatorReply> getMulMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "mul",
      requestType = io.grpc.examples.calculator.CalculatorRequest.class,
      responseType = io.grpc.examples.calculator.CalculatorReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<io.grpc.examples.calculator.CalculatorRequest,
      io.grpc.examples.calculator.CalculatorReply> getMulMethod() {
    io.grpc.MethodDescriptor<io.grpc.examples.calculator.CalculatorRequest, io.grpc.examples.calculator.CalculatorReply> getMulMethod;
    if ((getMulMethod = CalculatorGrpc.getMulMethod) == null) {
      synchronized (CalculatorGrpc.class) {
        if ((getMulMethod = CalculatorGrpc.getMulMethod) == null) {
          CalculatorGrpc.getMulMethod = getMulMethod =
              io.grpc.MethodDescriptor.<io.grpc.examples.calculator.CalculatorRequest, io.grpc.examples.calculator.CalculatorReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "mul"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.calculator.CalculatorRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.calculator.CalculatorReply.getDefaultInstance()))
              .setSchemaDescriptor(new CalculatorMethodDescriptorSupplier("mul"))
              .build();
        }
      }
    }
    return getMulMethod;
  }

  private static volatile io.grpc.MethodDescriptor<io.grpc.examples.calculator.CalculatorRequest,
      io.grpc.examples.calculator.CalculatorReply> getDivMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "div",
      requestType = io.grpc.examples.calculator.CalculatorRequest.class,
      responseType = io.grpc.examples.calculator.CalculatorReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<io.grpc.examples.calculator.CalculatorRequest,
      io.grpc.examples.calculator.CalculatorReply> getDivMethod() {
    io.grpc.MethodDescriptor<io.grpc.examples.calculator.CalculatorRequest, io.grpc.examples.calculator.CalculatorReply> getDivMethod;
    if ((getDivMethod = CalculatorGrpc.getDivMethod) == null) {
      synchronized (CalculatorGrpc.class) {
        if ((getDivMethod = CalculatorGrpc.getDivMethod) == null) {
          CalculatorGrpc.getDivMethod = getDivMethod =
              io.grpc.MethodDescriptor.<io.grpc.examples.calculator.CalculatorRequest, io.grpc.examples.calculator.CalculatorReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "div"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.calculator.CalculatorRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.calculator.CalculatorReply.getDefaultInstance()))
              .setSchemaDescriptor(new CalculatorMethodDescriptorSupplier("div"))
              .build();
        }
      }
    }
    return getDivMethod;
  }

  private static volatile io.grpc.MethodDescriptor<io.grpc.examples.calculator.NewCalculatorRequest,
      io.grpc.examples.calculator.NewCalculatorReply> getLengthMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "length",
      requestType = io.grpc.examples.calculator.NewCalculatorRequest.class,
      responseType = io.grpc.examples.calculator.NewCalculatorReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<io.grpc.examples.calculator.NewCalculatorRequest,
      io.grpc.examples.calculator.NewCalculatorReply> getLengthMethod() {
    io.grpc.MethodDescriptor<io.grpc.examples.calculator.NewCalculatorRequest, io.grpc.examples.calculator.NewCalculatorReply> getLengthMethod;
    if ((getLengthMethod = CalculatorGrpc.getLengthMethod) == null) {
      synchronized (CalculatorGrpc.class) {
        if ((getLengthMethod = CalculatorGrpc.getLengthMethod) == null) {
          CalculatorGrpc.getLengthMethod = getLengthMethod =
              io.grpc.MethodDescriptor.<io.grpc.examples.calculator.NewCalculatorRequest, io.grpc.examples.calculator.NewCalculatorReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "length"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.calculator.NewCalculatorRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.calculator.NewCalculatorReply.getDefaultInstance()))
              .setSchemaDescriptor(new CalculatorMethodDescriptorSupplier("length"))
              .build();
        }
      }
    }
    return getLengthMethod;
  }

  private static volatile io.grpc.MethodDescriptor<io.grpc.examples.calculator.NewCalculatorRequest,
      io.grpc.examples.calculator.BoolCalculatorReply> getEqualsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "equals",
      requestType = io.grpc.examples.calculator.NewCalculatorRequest.class,
      responseType = io.grpc.examples.calculator.BoolCalculatorReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<io.grpc.examples.calculator.NewCalculatorRequest,
      io.grpc.examples.calculator.BoolCalculatorReply> getEqualsMethod() {
    io.grpc.MethodDescriptor<io.grpc.examples.calculator.NewCalculatorRequest, io.grpc.examples.calculator.BoolCalculatorReply> getEqualsMethod;
    if ((getEqualsMethod = CalculatorGrpc.getEqualsMethod) == null) {
      synchronized (CalculatorGrpc.class) {
        if ((getEqualsMethod = CalculatorGrpc.getEqualsMethod) == null) {
          CalculatorGrpc.getEqualsMethod = getEqualsMethod =
              io.grpc.MethodDescriptor.<io.grpc.examples.calculator.NewCalculatorRequest, io.grpc.examples.calculator.BoolCalculatorReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "equals"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.calculator.NewCalculatorRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.examples.calculator.BoolCalculatorReply.getDefaultInstance()))
              .setSchemaDescriptor(new CalculatorMethodDescriptorSupplier("equals"))
              .build();
        }
      }
    }
    return getEqualsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CalculatorStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CalculatorStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CalculatorStub>() {
        @java.lang.Override
        public CalculatorStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CalculatorStub(channel, callOptions);
        }
      };
    return CalculatorStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CalculatorBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CalculatorBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CalculatorBlockingStub>() {
        @java.lang.Override
        public CalculatorBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CalculatorBlockingStub(channel, callOptions);
        }
      };
    return CalculatorBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CalculatorFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CalculatorFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CalculatorFutureStub>() {
        @java.lang.Override
        public CalculatorFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CalculatorFutureStub(channel, callOptions);
        }
      };
    return CalculatorFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * The calculator service definition.
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Floating Point Arithmetic
     * </pre>
     */
    default void add(io.grpc.examples.calculator.CalculatorRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.calculator.CalculatorReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddMethod(), responseObserver);
    }

    /**
     */
    default void sub(io.grpc.examples.calculator.CalculatorRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.calculator.CalculatorReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSubMethod(), responseObserver);
    }

    /**
     */
    default void mul(io.grpc.examples.calculator.CalculatorRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.calculator.CalculatorReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getMulMethod(), responseObserver);
    }

    /**
     */
    default void div(io.grpc.examples.calculator.CalculatorRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.calculator.CalculatorReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDivMethod(), responseObserver);
    }

    /**
     * <pre>
     * Adding new stuff 
     * </pre>
     */
    default void length(io.grpc.examples.calculator.NewCalculatorRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.calculator.NewCalculatorReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLengthMethod(), responseObserver);
    }

    /**
     */
    default void equals(io.grpc.examples.calculator.NewCalculatorRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.calculator.BoolCalculatorReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEqualsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service Calculator.
   * <pre>
   * The calculator service definition.
   * </pre>
   */
  public static abstract class CalculatorImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return CalculatorGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service Calculator.
   * <pre>
   * The calculator service definition.
   * </pre>
   */
  public static final class CalculatorStub
      extends io.grpc.stub.AbstractAsyncStub<CalculatorStub> {
    private CalculatorStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CalculatorStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CalculatorStub(channel, callOptions);
    }

    /**
     * <pre>
     * Floating Point Arithmetic
     * </pre>
     */
    public void add(io.grpc.examples.calculator.CalculatorRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.calculator.CalculatorReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sub(io.grpc.examples.calculator.CalculatorRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.calculator.CalculatorReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSubMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void mul(io.grpc.examples.calculator.CalculatorRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.calculator.CalculatorReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getMulMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void div(io.grpc.examples.calculator.CalculatorRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.calculator.CalculatorReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDivMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Adding new stuff 
     * </pre>
     */
    public void length(io.grpc.examples.calculator.NewCalculatorRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.calculator.NewCalculatorReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getLengthMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void equals(io.grpc.examples.calculator.NewCalculatorRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.calculator.BoolCalculatorReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getEqualsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service Calculator.
   * <pre>
   * The calculator service definition.
   * </pre>
   */
  public static final class CalculatorBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<CalculatorBlockingStub> {
    private CalculatorBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CalculatorBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CalculatorBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Floating Point Arithmetic
     * </pre>
     */
    public io.grpc.examples.calculator.CalculatorReply add(io.grpc.examples.calculator.CalculatorRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddMethod(), getCallOptions(), request);
    }

    /**
     */
    public io.grpc.examples.calculator.CalculatorReply sub(io.grpc.examples.calculator.CalculatorRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSubMethod(), getCallOptions(), request);
    }

    /**
     */
    public io.grpc.examples.calculator.CalculatorReply mul(io.grpc.examples.calculator.CalculatorRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getMulMethod(), getCallOptions(), request);
    }

    /**
     */
    public io.grpc.examples.calculator.CalculatorReply div(io.grpc.examples.calculator.CalculatorRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDivMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Adding new stuff 
     * </pre>
     */
    public io.grpc.examples.calculator.NewCalculatorReply length(io.grpc.examples.calculator.NewCalculatorRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getLengthMethod(), getCallOptions(), request);
    }

    /**
     */
    public io.grpc.examples.calculator.BoolCalculatorReply equals(io.grpc.examples.calculator.NewCalculatorRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getEqualsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service Calculator.
   * <pre>
   * The calculator service definition.
   * </pre>
   */
  public static final class CalculatorFutureStub
      extends io.grpc.stub.AbstractFutureStub<CalculatorFutureStub> {
    private CalculatorFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CalculatorFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CalculatorFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Floating Point Arithmetic
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<io.grpc.examples.calculator.CalculatorReply> add(
        io.grpc.examples.calculator.CalculatorRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<io.grpc.examples.calculator.CalculatorReply> sub(
        io.grpc.examples.calculator.CalculatorRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSubMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<io.grpc.examples.calculator.CalculatorReply> mul(
        io.grpc.examples.calculator.CalculatorRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getMulMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<io.grpc.examples.calculator.CalculatorReply> div(
        io.grpc.examples.calculator.CalculatorRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDivMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Adding new stuff 
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<io.grpc.examples.calculator.NewCalculatorReply> length(
        io.grpc.examples.calculator.NewCalculatorRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getLengthMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<io.grpc.examples.calculator.BoolCalculatorReply> equals(
        io.grpc.examples.calculator.NewCalculatorRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getEqualsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ADD = 0;
  private static final int METHODID_SUB = 1;
  private static final int METHODID_MUL = 2;
  private static final int METHODID_DIV = 3;
  private static final int METHODID_LENGTH = 4;
  private static final int METHODID_EQUALS = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ADD:
          serviceImpl.add((io.grpc.examples.calculator.CalculatorRequest) request,
              (io.grpc.stub.StreamObserver<io.grpc.examples.calculator.CalculatorReply>) responseObserver);
          break;
        case METHODID_SUB:
          serviceImpl.sub((io.grpc.examples.calculator.CalculatorRequest) request,
              (io.grpc.stub.StreamObserver<io.grpc.examples.calculator.CalculatorReply>) responseObserver);
          break;
        case METHODID_MUL:
          serviceImpl.mul((io.grpc.examples.calculator.CalculatorRequest) request,
              (io.grpc.stub.StreamObserver<io.grpc.examples.calculator.CalculatorReply>) responseObserver);
          break;
        case METHODID_DIV:
          serviceImpl.div((io.grpc.examples.calculator.CalculatorRequest) request,
              (io.grpc.stub.StreamObserver<io.grpc.examples.calculator.CalculatorReply>) responseObserver);
          break;
        case METHODID_LENGTH:
          serviceImpl.length((io.grpc.examples.calculator.NewCalculatorRequest) request,
              (io.grpc.stub.StreamObserver<io.grpc.examples.calculator.NewCalculatorReply>) responseObserver);
          break;
        case METHODID_EQUALS:
          serviceImpl.equals((io.grpc.examples.calculator.NewCalculatorRequest) request,
              (io.grpc.stub.StreamObserver<io.grpc.examples.calculator.BoolCalculatorReply>) responseObserver);
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

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getAddMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              io.grpc.examples.calculator.CalculatorRequest,
              io.grpc.examples.calculator.CalculatorReply>(
                service, METHODID_ADD)))
        .addMethod(
          getSubMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              io.grpc.examples.calculator.CalculatorRequest,
              io.grpc.examples.calculator.CalculatorReply>(
                service, METHODID_SUB)))
        .addMethod(
          getMulMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              io.grpc.examples.calculator.CalculatorRequest,
              io.grpc.examples.calculator.CalculatorReply>(
                service, METHODID_MUL)))
        .addMethod(
          getDivMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              io.grpc.examples.calculator.CalculatorRequest,
              io.grpc.examples.calculator.CalculatorReply>(
                service, METHODID_DIV)))
        .addMethod(
          getLengthMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              io.grpc.examples.calculator.NewCalculatorRequest,
              io.grpc.examples.calculator.NewCalculatorReply>(
                service, METHODID_LENGTH)))
        .addMethod(
          getEqualsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              io.grpc.examples.calculator.NewCalculatorRequest,
              io.grpc.examples.calculator.BoolCalculatorReply>(
                service, METHODID_EQUALS)))
        .build();
  }

  private static abstract class CalculatorBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CalculatorBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return io.grpc.examples.calculator.CalculatorProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Calculator");
    }
  }

  private static final class CalculatorFileDescriptorSupplier
      extends CalculatorBaseDescriptorSupplier {
    CalculatorFileDescriptorSupplier() {}
  }

  private static final class CalculatorMethodDescriptorSupplier
      extends CalculatorBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    CalculatorMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (CalculatorGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CalculatorFileDescriptorSupplier())
              .addMethod(getAddMethod())
              .addMethod(getSubMethod())
              .addMethod(getMulMethod())
              .addMethod(getDivMethod())
              .addMethod(getLengthMethod())
              .addMethod(getEqualsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
