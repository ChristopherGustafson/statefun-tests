package statefun_examples;

import com.google.auto.service.AutoService;

import org.apache.flink.statefun.flink.core.reqreply.RequestReplyFunction;
import org.apache.flink.statefun.sdk.spi.StatefulFunctionModule;

import java.util.Map;

@AutoService(StatefulFunctionModule.class)
public class MyModule implements StatefulFunctionModule {

    @Override
    public void configure(Map<String, String> config, Binder binder) {
        binder.bindIngressRouter(MyConstants.MESSAGE_INGRESS, new MyRouter());
        binder.bindFunctionProvider(MyConstants.MY_FUNCTION_TYPE, unused -> new MyFunction());
        binder.bindFunctionProvider(MyConstants.MY_SECOND_FUNCTION_TYPE, unused -> new MySecondFunction());
    }
}
