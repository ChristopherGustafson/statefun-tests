package statefun_examples;

import org.apache.flink.statefun.sdk.Context;
import org.apache.flink.statefun.sdk.FunctionType;
import org.apache.flink.statefun.sdk.StatefulFunction;
import org.apache.flink.statefun.sdk.annotations.Persisted;
import org.apache.flink.statefun.sdk.reqreply.generated.TypedValue;
import org.apache.flink.statefun.sdk.state.PersistedValue;
import org.apache.flink.statefun.sdk.Address;

import com.google.protobuf.InvalidProtocolBufferException;
import harness.protos.OutputMsg;
import statefun_examples.MyMessages.InternalMessage;

import java.nio.charset.StandardCharsets;

public class MySecondFunction implements StatefulFunction {

    @Persisted
    private final PersistedValue<Double> SEEN = PersistedValue.of("seen", Double.class);

    @Override
    public void invoke(Context context, Object message) throws IllegalArgumentException {

        if(!(message instanceof TypedValue)){
            throw new IllegalArgumentException("Unexpected message type: " + message);
        }

        double newSeen = SEEN.getOrDefault(0.0) + 1.0;
        SEEN.set(newSeen);

        TypedValue typed = (TypedValue) message;
        OutputMsg outputMsg = null;
        try {
            outputMsg = OutputMsg.parseFrom(typed.toByteArray());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        context.send(MyConstants.MESSAGE_EGRESS, outputMsg);

    }
}
