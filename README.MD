# Docker Commands to run zookeeper, kafka and last we can init_kafka to create topics

- **1. Run Zookeeper**
  docker compose -f zookeeper.yml up
- **2. Run Kafka**
  docker compose -f kafka_cluster.yml up
- **3. Execute script to create kafka topics**
  docker compose -f kafka_cluster.yml up

## once all the above steps are done, you need to open below URL to see dashboard of kafka manager
- http://localhost:9000
