A [Giter8][g8] template for a sample lagom scala application which uses kafka and elasticsearch.

# lagom-scala-kafka-es

This lagom scala project provides the implementation of cassandra, kafka and elasticsearch.

**1) Setup and run Zookeeper and Kafka**

**Step 1**: Download it from [here](https://kafka.apache.org/downloads)

**Step 2**: Extract it

**Step 3**: Move into the kafka directory

**Step 4**: Run zookeeper and kafka

` $ bin/zookeeper-server-start.sh config/zookeeper.properties`
`$ bin/kafka-server-start.sh config/server.properties`


##### 2) Setup Elasticsearch

**Step 1**: Download it from [here](https://www.elastic.co/downloads/elasticsearch)
**Step 2**: Extract it
**Step 3**: Move into the elasicsearch directory
**Step 4**: Run Elasticsearch

` $ bin/elasticsearch`

##### 3) Setup Kibana

**Step 1**: Download it from [here](https://www.elastic.co/downloads/kibana)
**Step 2**: Extract it
**Step 3**: Move into the kibana directory
**Step 4**: Run Kibana

` $ bin/kibana`

##### 4) Setup Cassandra

**Step 1**: Download it from [here](http://cassandra.apache.org/download/)
**Step 2**: Extract it
**Step 3**: Move into the cassandra directory
**Step 4**: Run Cassandra

` $ bin/cassandra`

**Step 5**: Run cqlsh

` $ bin/cqlsh `

##### 5) Run the application

`sbt runAll`

Then hit the routes:

>url : localhost:9000/postrequest
json body :
`{
  "id" : "1100",
  "message" : "My New Message"
}`

>url : localhost:9000/getrequest
json body :
`{
  "id" : "1100"
}`

Template license
----------------
Written in 2018 by Knoldus Inc.

To the extent possible under law, the author(s) have dedicated all copyright and related
and neighboring rights to this template to the public domain worldwide.
This template is distributed without any warranty. See <http://creativecommons.org/publicdomain/zero/1.0/>.

[g8]: http://www.foundweekends.org/giter8/
