import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.statefun.sdk.java.TypeName;
import org.apache.flink.statefun.sdk.java.types.SimpleType;
import org.apache.flink.statefun.sdk.java.types.Type;

public class Messages {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static final Type<FlowData> FLOW_DATA =
            SimpleType.simpleImmutableTypeFrom(
                    TypeName.typeNameFromString("statefun-tests/FlowData"),
                    mapper::writeValueAsBytes,
                    bytes -> mapper.readValue(bytes, FlowData.class)
            );


    public static class FlowData{
        private final String id;
        private final double latitude;
        private final double longitude;
        private final int flow;
        private final int period;
        private final int accuracy;
        private final String timestamp;
        private final int numLanes;

        @JsonCreator
        public FlowData(
                @JsonProperty("internalId") String id,
                @JsonProperty("lat") double latitude,
                @JsonProperty("long") double longitude,
                @JsonProperty("flow") int flow,
                @JsonProperty("period") int period,
                @JsonProperty("accuracy") int accuracy,
                @JsonProperty("timestamp") String timestamp,
                @JsonProperty("num_lanes") int numLanes
        ){
            this.id = id;
            this.latitude = latitude;
            this.longitude = longitude;
            this.flow = flow;
            this.period = period;
            this.accuracy = accuracy;
            this.timestamp = timestamp;
            this.numLanes = numLanes;
        }

        public int getFlow(){
            return flow;
        }

        public String getId() {
            return id;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public int getPeriod() {
            return period;
        }

        public int getAccuracy() {
            return accuracy;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public int getNumLanes() {
            return numLanes;
        }

        @Override
        public String toString() {
            return String.format("FlowData: {id: %s, lat: %f. long: %f, period: %d, accuracy: %d, timestamp: %s, numLanes: %d}",
            id,
            latitude,
            longitude,
            period,
            accuracy,
            timestamp,
            numLanes);
        }
    }
}
