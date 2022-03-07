FROM flink-statefun-test:latest
RUN mkdir -p /opt/statefun/modules/shopping-cart-app
RUN mkdir -p /opt/statefun/modules/remote
COPY target/shopping-cart-3.2.0.jar /opt/statefun/modules/shopping-cart-app/
COPY module.yaml /opt/statefun/modules/remote/module.yaml