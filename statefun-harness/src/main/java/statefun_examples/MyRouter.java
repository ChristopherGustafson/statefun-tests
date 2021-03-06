package statefun_examples;

import org.apache.flink.statefun.sdk.io.Router;
import harness.protos.InputMsg;
public class MyRouter implements Router<InputMsg> {

    @Override
    public void route(InputMsg inputMsg, Downstream<InputMsg> downstream) {
        downstream.forward(MyConstants.MY_FUNCTION_TYPE, inputMsg.getUserId(), inputMsg);
    }


}
