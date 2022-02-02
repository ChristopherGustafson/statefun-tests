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
        private final int cars;
        private final double ratio;

        @JsonCreator
        public FlowData(@JsonProperty("flow") String cars, @JsonProperty("ratio") String ratio){
            this.cars =  Integer.parseInt(cars);
            this.ratio = Double.parseDouble(ratio);
        }

        public int getCars(){
            return cars;
        }

        public double getRatio(){
            return ratio;
        }

        @Override
        public String toString() {
            return "FlowData{cars=" + cars + "}";
        }

    }
}
