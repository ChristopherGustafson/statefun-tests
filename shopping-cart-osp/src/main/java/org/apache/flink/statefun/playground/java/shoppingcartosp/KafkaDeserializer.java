package org.apache.flink.statefun.playground.java.shoppingcartosp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.flink.statefun.playground.java.shoppingcartosp.generated.AddToCart;
import org.apache.flink.statefun.sdk.kafka.KafkaIngressDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaDeserializer implements KafkaIngressDeserializer<AddToCart> {

  private static Logger LOG = LoggerFactory.getLogger(KafkaDeserializer.class);

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public AddToCart deserialize(ConsumerRecord<byte[], byte[]> input) {
    try {

      LOG.info("Deserializing AddToCart: " + input.value().toString());

      Message.Builder msgBuilder = AddToCart.newBuilder();
      String inputString = new String(input.value(), StandardCharsets.UTF_8);
      JsonFormat.parser().merge(inputString, msgBuilder);
      AddToCart.Builder addBuilder = (AddToCart.Builder) msgBuilder;
      AddToCart msg = addBuilder.setPublishTimestamp(Long.toString(input.timestamp())).build();

      //      String parsed = mapper.readValue(input.value(), String.class);
      //      AddToCart parsed = AddToCart.parseFrom(input.value());
      //      AddToCart msg =
      //          AddToCart.newBuilder(parsed)
      //              .setPublishTimestamp(Long.toString(input.timestamp()))
      //              .build();

      return msg;
    } catch (IOException e) {
      LOG.warn("Failed to deserialize record", e);
      return null;
    }
  }

  //  @Override
  //  public StringValue deserialize(ConsumerRecord<byte[], byte[]> input) {
  //    // Hacky way to insert kafka timestamp into add-to-cart messages
  //    // Replaces the value of publishTimestamp in the incoming json
  //    // String, this is because embedded functions assume Protobuf
  //    // messages
  //    String json  = new String(input.value(), StandardCharsets.UTF_8);
  //    String tsString = "publishTimestamp\":\"";
  //    int startIndex = json.indexOf(tsString)+tsString.length();
  //    int endIndex = json.substring(startIndex).indexOf("\"") + startIndex;
  //    json = json.replaceFirst(json.substring(startIndex, endIndex),
  // Long.toString(input.timestamp()));
  //
  //    return StringValue.of(json);
  //  }
}
