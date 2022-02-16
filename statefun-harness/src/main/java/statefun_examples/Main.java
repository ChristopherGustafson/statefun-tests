package statefun_examples;

import org.apache.flink.statefun.flink.harness.Harness;
import org.apache.flink.statefun.flink.harness.io.SerializableSupplier;
import org.apache.flink.util.StringUtils;

import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) throws Exception {
        Harness harness = new Harness();

        harness.withConfiguration("state.backend", "ndb");
        harness.withConfiguration("state.backend.ndb.connectionstring", "127.0.0.1");
        harness.withConfiguration("state.backend.ndb.dbname", "flinkndb");
        harness.withConfiguration("state.backend.ndb.truncatetableonstart", "false");

        harness.withConfiguration("state.checkpoints.dir", "file:///tmp/checkpoints");
        harness.withConfiguration("state.savepoints.dir", "file:///tmp/savepoints");

        harness.withConfiguration("execution.checkpointing.interval", "2sec");

        harness.withConfiguration(
                "classloader.parent-first-patterns.additional",
                "org.apache.flink.statefun;org.apache.kafka;com.google.protobuf");

        harness.withKryoMessageSerializer();
        harness.withSupplyingIngress(MyConstants.MESSAGE_INGRESS, new InputGenerator());
        harness.withPrintingEgress(MyConstants.MESSAGE_EGRESS);

        harness.start();
    }

    private static final class InputGenerator implements SerializableSupplier<MyMessages.InputMsg> {

        @Override
        public MyMessages.InputMsg get() {
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted", e);
            }
            return getRandomMessage();
        }

        private MyMessages.InputMsg getRandomMessage(){
            final ThreadLocalRandom r = ThreadLocalRandom.current();
//            final String userId = StringUtils.generateRandomAlphanumericString(r, 2);
            final String userId = "Chris";
            return new MyMessages.InputMsg(userId,"Hello user " + userId);
        }
    }

}
