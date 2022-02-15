package statefun_examples;

import org.apache.flink.statefun.sdk.Context;
import org.apache.flink.statefun.sdk.StatefulFunction;
import statefun_examples.MyMessages.InputMsg;
import statefun_examples.MyMessages.OutputMsg;

public class MyFunction implements StatefulFunction {

    @Override
    public void invoke(Context context, Object input) {
        if(!(input instanceof InputMsg)){
            throw new IllegalArgumentException("Unexpected message type: " + input);
        }
        InputMsg inputMsg = (InputMsg) input;
        OutputMsg outputMsg = new OutputMsg(inputMsg.getUserId(), inputMsg.getMessage());
        context.send(MyConstants.MESSAGE_EGRESS, outputMsg);
    }
}
