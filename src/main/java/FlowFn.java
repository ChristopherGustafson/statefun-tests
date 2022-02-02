import org.apache.flink.statefun.sdk.java.*;
import org.apache.flink.statefun.sdk.java.io.KafkaEgressMessage;
import org.apache.flink.statefun.sdk.java.message.Message;

import java.util.concurrent.CompletableFuture;

public class FlowFn implements StatefulFunction {

    static final TypeName TYPE = TypeName.typeNameFromString("statefun-tests/flow-fn");

    static final TypeName FLOW_EGRESS = TypeName.typeNameFromString("statefun-tests/flow-egress");

    static final ValueSpec<Integer> CARS_SEEN = ValueSpec.named("carsSeen").withIntType();

    @Override
    public CompletableFuture<Void> apply(Context context, Message message) throws Throwable {

        if (message.is(Messages.FLOW_DATA)) {

            final Messages.FlowData flowData = message.as(Messages.FLOW_DATA);

            AddressScopedStorage storage = context.storage();
            int seen = storage.get(CARS_SEEN).orElse(0) + flowData.getCars();
            storage.set(CARS_SEEN, seen);

            context.send(
                    KafkaEgressMessage.forEgress(FLOW_EGRESS)
                            .withTopic("flow_output")
                            .withUtf8Key("location")
                            .withUtf8Value(String.format("%d cars seen so far", seen)).build()
            );

        }
        return context.done();

    }
}
