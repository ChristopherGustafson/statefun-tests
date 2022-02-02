import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.apache.flink.statefun.sdk.java.StatefulFunctionSpec;
import org.apache.flink.statefun.sdk.java.StatefulFunctions;
import org.apache.flink.statefun.sdk.java.handler.RequestReplyHandler;
import org.apache.flink.statefun.sdk.java.slice.Slice;
import org.apache.flink.statefun.sdk.java.slice.Slices;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static io.undertow.UndertowOptions.ENABLE_HTTP2;

public class Main {

    public static void main(String[] args) {
        StatefulFunctionSpec flowFn = StatefulFunctionSpec.builder(FlowFn.TYPE)
                .withValueSpec(FlowFn.CARS_SEEN)
                .withSupplier(FlowFn::new)
                .build();

        StatefulFunctions functions = new StatefulFunctions();
        functions.withStatefulFunction(flowFn);
        RequestReplyHandler handler = functions.requestReplyHandler();

        /* This example uses the Undertow http server, but any HTTP server/framework will work as-well */
        Undertow server =
            Undertow.builder()
                    .addHttpListener(1108, "0.0.0.0")
                    .setHandler(new UndertowStateFunHandler(handler))
                    .setServerOption(ENABLE_HTTP2, true)
                    .build();

        server.start();

    }

    private static final class UndertowStateFunHandler implements HttpHandler {
        private final RequestReplyHandler handler;

        UndertowStateFunHandler(RequestReplyHandler handler) {
            this.handler = Objects.requireNonNull(handler);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) {
            exchange.getRequestReceiver().receiveFullBytes(this::onRequestBody);
        }

        private void onRequestBody(HttpServerExchange exchange, byte[] requestBytes) {
            try {
                CompletableFuture<Slice> future = handler.handle(Slices.wrap(requestBytes));
                exchange.dispatch();
                future.whenComplete(
                        (responseBytes, ex) -> {
                            if (ex != null) {
                                onException(exchange, ex);
                            } else {
                                onSuccess(exchange, responseBytes);
                            }
                        });
            } catch (Throwable t) {
                onException(exchange, t);
            }
        }

        private void onException(HttpServerExchange exchange, Throwable t) {
            t.printStackTrace(System.out);
            exchange.getResponseHeaders().put(Headers.STATUS, 500);
            exchange.endExchange();
        }

        private void onSuccess(HttpServerExchange exchange, Slice result) {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
            exchange.getResponseSender().send(result.asReadOnlyByteBuffer());
        }
    }
}
