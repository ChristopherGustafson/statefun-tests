rm runtime-docker/*.jar
mvn spotless:apply
mvn clean install -nsu
cp target/shopping-cart-3.2.0-jar-with-dependencies.jar runtime-docker/

docker-compose build
docker-compose up