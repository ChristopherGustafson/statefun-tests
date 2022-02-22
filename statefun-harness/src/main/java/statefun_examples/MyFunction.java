package statefun_examples;

import org.apache.flink.statefun.sdk.Context;
import org.apache.flink.statefun.sdk.annotations.Persisted;
import org.apache.flink.statefun.sdk.StatefulFunction;
import org.apache.flink.statefun.sdk.reqreply.generated.TypedValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Message;
import org.apache.flink.statefun.sdk.state.PersistedValue;
import harness.protos.InputMsg;
import harness.protos.InternalMsg;

import java.util.concurrent.CompletableFuture;

public class MyFunction implements StatefulFunction {

    private static final ObjectMapper mapper = new ObjectMapper();

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
        InternalMsg internalMsg = InternalMsg.newBuilder().setUserId(inputMsg.getUserId()).setMessage(inputMsg.getMessage() + " Total msg count: " + newSeen).build();
        context.send(MyConstants.MY_REMOTE_FUNCTION_TYPE, internalMsg.getUserId(), pack(internalMsg));
    }

    public static <M extends Message> TypedValue pack(M message) {
        return TypedValue.newBuilder()
                .setTypename(MyConstants.NAMESPACE + "/" + InternalMsg.class.getName())
                .setHasValue(true)
                .setValue(message.toByteString())
                .build();
    }


}
