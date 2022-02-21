package statefun_examples;

import org.apache.flink.statefun.sdk.FunctionType;
import org.apache.flink.statefun.sdk.io.EgressIdentifier;
import org.apache.flink.statefun.sdk.io.IngressIdentifier;
import org.apache.flink.statefun.sdk.io.IngressSpec;
import harness.protos.InputMsg;
import harness.protos.InternalMsg;
import harness.protos.OutputMsg;


public final class MyConstants {

    public static final String NAMESPACE = "statefun-harness";

    public static final IngressIdentifier<InputMsg> MESSAGE_INGRESS = new IngressIdentifier<>(InputMsg.class, NAMESPACE, "ingress");

    public static final EgressIdentifier<OutputMsg> MESSAGE_EGRESS = new EgressIdentifier<>(NAMESPACE, "egress", OutputMsg.class);

    static final FunctionType MY_FUNCTION_TYPE = new FunctionType(NAMESPACE, "my-function");

    static final FunctionType MY_SECOND_FUNCTION_TYPE = new FunctionType(NAMESPACE, "my-second_function");

    static final FunctionType MY_REMOTE_FUNCTION_TYPE = new FunctionType(NAMESPACE, "remotefn");

}
