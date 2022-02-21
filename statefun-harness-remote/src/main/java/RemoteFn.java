
import org.apache.flink.statefun.sdk.java.AddressScopedStorage;
import org.apache.flink.statefun.sdk.java.Context;
import org.apache.flink.statefun.sdk.java.StatefulFunction;
import org.apache.flink.statefun.sdk.java.ValueSpec;
import org.apache.flink.statefun.sdk.java.message.EgressMessageBuilder;
import org.apache.flink.statefun.sdk.java.message.Message;
import org.apache.flink.statefun.sdk.java.message.MessageBuilder;

import harness.protos.InternalMsg;
import harness.protos.OutputMsg;

import java.util.concurrent.CompletableFuture;


public class RemoteFn implements StatefulFunction {

    static final ValueSpec<Integer> SEEN = ValueSpec.named("seen").withIntType();

    @Override
    public CompletableFuture<Void> apply(Context context, Message message) throws Throwable {


        if(!message.is(Types.INTERNAL_MSG_TYPE)){
            throw new IllegalArgumentException("Unexpected message type: " + message);
        }

        AddressScopedStorage storage = context.storage();
        int newSeenCount = storage.get(SEEN).orElse(0) + 1;
        storage.set(SEEN, newSeenCount);

        InternalMsg internalMsg = message.as(Types.INTERNAL_MSG_TYPE);
        System.out.println("Remote function invoked with message: " + internalMsg.getMessage() + " at functino with id " + internalMsg.getUserId());
        OutputMsg outputMsg = OutputMsg.newBuilder()
                .setUserId(internalMsg.getUserId())
                .setMessage(internalMsg.getMessage())
                .build();

        context.send(
                MessageBuilder.forAddress(Types.MY_SECOND_FUNCTION_TYPE, outputMsg.getUserId())
                .withCustomType(Types.OUTPUT_MSG_TYPE, outputMsg)
                .build());

        return context.done();
    }
}
