package statefun_examples;

import org.apache.flink.statefun.sdk.Context;
import org.apache.flink.statefun.sdk.FunctionType;
import org.apache.flink.statefun.sdk.StatefulFunction;
import org.apache.flink.statefun.sdk.annotations.Persisted;
import org.apache.flink.statefun.sdk.state.PersistedValue;
import org.apache.flink.statefun.sdk.Address;
import statefun_examples.MyMessages.InternalMessage;

public class MySecondFunction implements StatefulFunction {

    @Persisted
    private final PersistedValue<Double> SEEN = PersistedValue.of("seen", Double.class);


    @Override
    public void invoke(Context context, Object message) {
        if(!(message instanceof InternalMessage)){
            throw new IllegalArgumentException("Unexpected message type: " + message);
        }

        double newSeen = SEEN.getOrDefault(0.0) + 1.0;
        SEEN.set(newSeen);

        InternalMessage internalMsg = (InternalMessage) message;
        MyMessages.OutputMsg outputMsg = new MyMessages.OutputMsg(internalMsg.getUserId(), internalMsg.getMessage() + " Total msg count: " + newSeen);
        context.send(MyConstants.MESSAGE_EGRESS, outputMsg);

    }
}
