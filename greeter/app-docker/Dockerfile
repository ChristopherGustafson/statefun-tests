FROM flink-statefun-test:latest
RUN mkdir -p /opt/statefun/modules/greeter-functions-app
RUN mkdir -p /opt/statefun/modules/remote
COPY target/greeter-functions-app*jar /opt/statefun/modules/greeter-functions-app/
COPY module.yaml /opt/statefun/modules/remote/module.yaml