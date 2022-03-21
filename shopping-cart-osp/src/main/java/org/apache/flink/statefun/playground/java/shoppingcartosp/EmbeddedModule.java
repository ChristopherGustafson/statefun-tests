package org.apache.flink.statefun.playground.java.shoppingcartosp;

import com.google.auto.service.AutoService;
import java.util.Map;
import org.apache.flink.statefun.playground.java.shoppingcartosp.generated.AddToCart;
import org.apache.flink.statefun.sdk.FunctionType;
import org.apache.flink.statefun.sdk.io.IngressIdentifier;
import org.apache.flink.statefun.sdk.io.IngressSpec;
import org.apache.flink.statefun.sdk.io.Router;
import org.apache.flink.statefun.sdk.kafka.KafkaIngressBuilder;
import org.apache.flink.statefun.sdk.kafka.KafkaIngressStartupPosition;
import org.apache.flink.statefun.sdk.spi.StatefulFunctionModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(StatefulFunctionModule.class)
public class EmbeddedModule implements StatefulFunctionModule {

  private static final Logger LOG = LoggerFactory.getLogger(EmbeddedModule.class);

  @Override
  public void configure(Map<String, String> globalConfiguration, Binder binder) {

    IngressIdentifier<AddToCart> ingressId =
        new IngressIdentifier<>(AddToCart.class, "statefun.ndb", "add-to-cart");
    IngressSpec<AddToCart> kafkaIngress =
        KafkaIngressBuilder.forIdentifier(ingressId)
            .withKafkaAddress("localhost:9092")
            .withConsumerGroupId("my-group-id")
            .withTopic("add-to-cart")
            .withDeserializer(KafkaDeserializer.class)
            .withStartupPosition(KafkaIngressStartupPosition.fromLatest())
            .build();
    binder.bindIngress(kafkaIngress);
    binder.bindIngressRouter(ingressId, new CustomRouter());
  }

  private class CustomRouter implements Router<AddToCart> {

    @Override
    public void route(AddToCart message, Downstream<AddToCart> downstream) {
      LOG.info("Routing AddToCart message to " + UserShoppingCartFn.TYPE);
      FunctionType functionType =
          new FunctionType(UserShoppingCartFn.TYPE.namespace(), UserShoppingCartFn.TYPE.name());
      downstream.forward(functionType, message.getUserId(), message);
    }
  }
}
