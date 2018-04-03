package com.example.impl.kafka

import java.util.{Collections, Properties}

import com.example.impl.utils.constant.Constants
import com.example.models.Log
import com.fasterxml.jackson.databind.JsonNode
import org.apache.kafka.clients.consumer.KafkaConsumer
import play.api.libs.json.Json

import scala.collection.JavaConversions._
import scala.io.Source.fromURL

trait Consumer {

  private lazy val subscribe = (topic: String) => consumer.subscribe(Collections.singletonList(topic))

  val props = new Properties()
  val reader = fromURL(getClass.getResource(Constants.KAFKA_CONSUMER_CONFIG_FILE)).bufferedReader()
  props.load(reader)

  val consumer = new KafkaConsumer[String, JsonNode](props)

  /**
    * consumes data from the topic
    * @param topic is the name of the topic
    * @return the data in a Map
    */
  def consumeData(topic: String): Map[String, Log] = {
    subscribe(topic)
    val consumerRecords = consumer.poll(5000)

    val result = for {
      record <- consumerRecords
    } yield {
      record.key() -> Json.parse(record.value().toString).as[Log]
    }

    result.toMap
  }

}

/**
  * Run this to start consuming data from the specified topic
  */
object ConsumerApplication extends App with Consumer {

  while (true) {
    consumeData("MyTopic")
  }
}
