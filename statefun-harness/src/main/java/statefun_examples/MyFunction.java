package statefun_examples;

import org.apache.flink.statefun.sdk.Context;
import org.apache.flink.statefun.sdk.annotations.Persisted;
import org.apache.flink.statefun.sdk.StatefulFunction;
import org.apache.flink.statefun.sdk.state.PersistedValue;
import statefun_examples.MyMessages.InputMsg;
import statefun_examples.MyMessages.InternalMessage;

import java.util.concurrent.CompletableFuture;

public class MyFunction implements StatefulFunction {

    @Persisted
    private final PersistedValue<Double> SEEN = PersistedValue.of("seen", Double.class);

    @Override
    public void invoke(Context context, Object input) {
        if(!(input instanceof InputMsg)){
            throw new IllegalArgumentException("Unexpected message type: " + input);
        }

        double newSeen = SEEN.getOrDefault(0.0) + 1.0;
        SEEN.set(newSeen);

        InputMsg inputMsg = (InputMsg) input;
        InternalMessage internalMsg = new InternalMessage(inputMsg.getUserId(), inputMsg.getMessage() + " Total msg count: " + newSeen);
        context.send(MyConstants.MY_SECOND_FUNCTION_TYPE, internalMsg.getUserId(), internalMsg);
    }

}
