package statefun_examples;

import org.apache.flink.statefun.sdk.FunctionType;
import org.apache.flink.statefun.sdk.io.EgressIdentifier;
import org.apache.flink.statefun.sdk.io.IngressIdentifier;
import org.apache.flink.statefun.sdk.io.IngressSpec;
import statefun_examples.MyMessages.InputMsg;
import statefun_examples.MyMessages.OutputMsg;


public final class MyConstants {

    public static final IngressIdentifier<InputMsg> MESSAGE_INGRESS = new IngressIdentifier<>(MyMessages.InputMsg.class, "statefun-harness", "ingress");

    public static final EgressIdentifier<MyMessages.OutputMsg> MESSAGE_EGRESS = new EgressIdentifier<>("statefun-harness", "egress", MyMessages.OutputMsg.class);

    static final FunctionType MY_FUNCTION_TYPE = new FunctionType("statefun-harness", "my-function");

}
