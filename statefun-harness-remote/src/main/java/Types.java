
import org.apache.flink.statefun.sdk.java.TypeName;
import org.apache.flink.statefun.sdk.java.ValueSpec;
import org.apache.flink.statefun.sdk.java.types.SimpleType;
import org.apache.flink.statefun.sdk.java.types.Type;
import org.apache.flink.statefun.sdk.java.types.TypeSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.AbstractMessageLite;
import harness.protos.InternalMsg;
import harness.protos.OutputMsg;

public class Types {

    private static final String NAMESPACE = "statefun-harness";

    private static final ObjectMapper mapper = new ObjectMapper();

    public static final Type<InternalMsg> INTERNAL_MSG_TYPE =
            SimpleType.simpleImmutableTypeFrom(
                    TypeName.typeNameOf(NAMESPACE, InternalMsg.class.getName()),
                    AbstractMessageLite::toByteArray,
                    InternalMsg::parseFrom);

    public static final Type<OutputMsg> OUTPUT_MSG_TYPE =
            SimpleType.simpleImmutableTypeFrom(
                    TypeName.typeNameOf(NAMESPACE, OutputMsg.class.getName()),
                    AbstractMessageLite::toByteArray,
                    OutputMsg::parseFrom);


    static final TypeName REMOTE_FUNCTION_TYPE = TypeName.typeNameOf(NAMESPACE, "remotefn");

    static final TypeName MY_SECOND_FUNCTION_TYPE = TypeName.typeNameOf(NAMESPACE, "my-second_function");

//    static final TypeName EGRESS = TypeName.typeNameOf(NAMESPACE, "egress");

}
