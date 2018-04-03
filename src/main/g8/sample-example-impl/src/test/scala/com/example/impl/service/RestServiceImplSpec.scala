package com.example.impl.service

import com.example.api.RestServiceApi
import com.example.impl.elasticsearch.JestClient
import com.example.impl.kafka.Producer
import com.example.impl.loader.Application
import com.example.impl.processor.RequestProcessor
import com.example.models.{Log, Request, Response}
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.mockito.Mockito.{verify, when}
import org.mockito.ArgumentMatchers._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

class RestServiceImplSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll with MockitoSugar {

  lazy val mockedRequestProcessor = mock[RequestProcessor]
  lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra(true)) { ctx =>
    new Application(ctx) with LocalServiceLocator {
      override lazy val requestProcessor = mockedRequestProcessor
    }
  }
  lazy val client = server.serviceClient.implement[RestServiceApi]

  "The RestServiceImpl" should {
    "get the correct response for the request" in {
      val request = Request("1111", Some("Test Message"))
      val response = Response("Test Message")
      when(mockedRequestProcessor.storeDataInKafka(anyString(), any(request.getClass), any(response.getClass), anyString())) thenReturn "TesTopic"
      when(mockedRequestProcessor.createDocument(any(request.getClass))) thenReturn "1111"
      client.postRequest.invoke(request).map { response =>
        verify(mockedRequestProcessor).storeDataInKafka(anyString(), any(request.getClass), any(response.getClass), anyString())
        verify(mockedRequestProcessor).createDocument(any(request.getClass))
        response.message should ===("Got Test Message")
      }
    }

    "fetch the stored data from elasticsearch" in {
      val request = Request("1111" , None)
      when(mockedRequestProcessor.getDocument(any(request.getClass))) thenReturn Request("1111", Some("Test Message"))
      client.getRequest.invoke(request).map { response =>
        verify(mockedRequestProcessor).getDocument(any(request.getClass))
        response.message should === (Some("Test Message"))
      }
    }
  }

  override protected def beforeAll() = server

  override protected def afterAll() = server.stop()
}
