FROM flink-statefun-local:latest

RUN mkdir -p /opt/statefun/modules/shopping-cart
RUN mkdir -p /opt/statefun/modules/remote

COPY runtime-docker/shopping-cart-osp-3.2.0-jar-with-dependencies.jar /opt/statefun/modules/shopping-cart/shopping-cart.jar
COPY module.yaml /opt/statefun/modules/remote/module.yaml