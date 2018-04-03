package com.example.impl.processor

import com.example.impl.kafka.Producer
import com.example.models.{Log, Request, Response}
import com.fasterxml.jackson.databind.JsonNode
import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.connect.json.JsonSerializer
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class RequestProcessorSpec extends WordSpec with EmbeddedKafka with Matchers with BeforeAndAfterAll {

  implicit val jsonSerializer = new JsonSerializer()
  implicit val config = EmbeddedKafkaConfig(kafkaPort = 7000, zooKeeperPort = 7001)

  val logHandler = new RequestProcessor with Producer {
    override val producer: KafkaProducer[String, JsonNode] = aKafkaProducer(jsonSerializer, config)
  }

  override def beforeAll(): Unit = {
    EmbeddedKafka.start()
  }

  override def afterAll(): Unit = {
    EmbeddedKafka.stop()
  }

  "LogHandler" should {
    "publish message to a topic" in {
      val log = Log("123", Request("9999",Some("New Json Message")), Response("Json Message"))
      assert(logHandler.produceData(log, "TestTopic") == "TestTopic")
    }
  }
}
