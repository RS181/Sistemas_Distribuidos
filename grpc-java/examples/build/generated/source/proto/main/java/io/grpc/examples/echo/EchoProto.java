// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: grpc/examples/echo/echo.proto

// Protobuf Java Version: 3.25.3
package io.grpc.examples.echo;

public final class EchoProto {
  private EchoProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_grpc_examples_echo_EchoRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_grpc_examples_echo_EchoRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_grpc_examples_echo_EchoResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_grpc_examples_echo_EchoResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\035grpc/examples/echo/echo.proto\022\022grpc.ex" +
      "amples.echo\"\036\n\013EchoRequest\022\017\n\007message\030\001 " +
      "\001(\t\"\037\n\014EchoResponse\022\017\n\007message\030\001 \001(\t2\373\002\n" +
      "\004Echo\022P\n\tUnaryEcho\022\037.grpc.examples.echo." +
      "EchoRequest\032 .grpc.examples.echo.EchoRes" +
      "ponse\"\000\022\\\n\023ServerStreamingEcho\022\037.grpc.ex" +
      "amples.echo.EchoRequest\032 .grpc.examples." +
      "echo.EchoResponse\"\0000\001\022\\\n\023ClientStreaming" +
      "Echo\022\037.grpc.examples.echo.EchoRequest\032 ." +
      "grpc.examples.echo.EchoResponse\"\000(\001\022e\n\032B" +
      "idirectionalStreamingEcho\022\037.grpc.example" +
      "s.echo.EchoRequest\032 .grpc.examples.echo." +
      "EchoResponse\"\000(\0010\001BY\n\025io.grpc.examples.e" +
      "choB\tEchoProtoP\001Z3google.golang.org/grpc" +
      "/examples/features/proto/echob\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_grpc_examples_echo_EchoRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_grpc_examples_echo_EchoRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_grpc_examples_echo_EchoRequest_descriptor,
        new java.lang.String[] { "Message", });
    internal_static_grpc_examples_echo_EchoResponse_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_grpc_examples_echo_EchoResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_grpc_examples_echo_EchoResponse_descriptor,
        new java.lang.String[] { "Message", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
