package com.example.impl.elasticsearch

import com.example.models.Request
import org.scalatest.{BeforeAndAfterAll, WordSpec}
import pl.allegro.tech.embeddedelasticsearch.{EmbeddedElastic, PopularProperties}


class JestClientSpec extends WordSpec with BeforeAndAfterAll with JestClient {

  override lazy val address = "http://localhost:9350"
  lazy val create = createDocument(request)
  val request = Request("123", Some("Message"))

  override protected def beforeAll(): Unit = {
    EmbeddedElastic.builder()
      .withElasticVersion("5.0.0")
      .withSetting(PopularProperties.HTTP_PORT, 9350)
      .withSetting(PopularProperties.CLUSTER_NAME, "my_cluster")
      .build()
      .start()
  }

  "JestClient" should {
    "able to store data to elasticsearch" in {
      val request = Request("123", Some("New Message"))
      assert(createDocument(request) == "123")
    }

    "able to get stored data from elasticsearch" in {
      val request = Request("123", None)
      create
      assert(getDocument(request) == Request("123", Some("Message")))
    }

    "able to update stored data in elasticsearch" in {
      val request = Request("123", Some("New Message"))
      create
      assert(updateDocument(request))
    }

    "able to delete stored data from elasticsearch" in {
      val request = Request("123", None)
      create
      assert(deleteDocument(request))
    }
  }
}
