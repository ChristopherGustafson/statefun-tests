#!/bin/bash

source $(dirname "$0")/utils.sh

######## Scenario 1:
#  1) add socks to stock (via StockFn)
#  2) put socks for userId "1" into the shopping cart (via UserShoppingCartFn)
#  3) checkout (via UserShoppingCartFn)
#--------------------------------
# 1)
key="socks" # itemId
json=$(cat <<JSON
  {"itemId":"socks","quantity":2000}
JSON
)
ingress_topic="restock-items" # StockFn
send_to_kafka $key $json $ingress_topic
sleep 1
#--------------------------------
# 2)

for i in {1..300}
do
sleep 2
key="1" # userId
json=$(cat <<JSON
  {"userId":"1","quantity":3,"itemId":"socks"}
JSON
)
ingress_topic="add-to-cart" # UserShoppingCartFn
send_to_kafka $key $json $ingress_topic
key="1" # userId
json=$(cat <<JSON
  {"userId":"1"}
JSON
)
ingress_topic="checkout" # UserShoppingCartFn
send_to_kafka $key $json $ingress_topic
done

#--------------------------------
# 3)

