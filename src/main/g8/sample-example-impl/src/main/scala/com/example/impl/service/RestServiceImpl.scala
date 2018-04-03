package com.example.impl.service

import java.time.LocalDateTime

import akka.Done
import com.example.api.RestServiceApi
import com.example.impl.eventsourcing.command.NewCommand
import com.example.impl.eventsourcing.entity.Entity
import com.example.impl.processor.RequestProcessor
import com.example.models.{Request, Response}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.{ExecutionContext, Future}


class RestServiceImpl(persistentEntityRegistry: PersistentEntityRegistry, requestProcessor: RequestProcessor)(implicit ec: ExecutionContext) extends RestServiceApi {

  override def postRequest: ServiceCall[Request, Response] = ServiceCall { request: Request =>

    val ref = persistentEntityRegistry.refFor[Entity](request.id)
    val result = ref.ask(NewCommand(request))
    result.map { status =>
      if (status == Done) {
        requestProcessor.createDocument(request)

        val response =  request.message match {
          case Some(value) => Response(s"Got $value")
          case None => Response("Got No Message")
        }

        requestProcessor.storeDataInKafka(LocalDateTime.now.toString, request, response, "MyTopic")
        response
      } else {
        Response("Server Error. Please try later..")
      }
    }

  }

  override def getRequest: ServiceCall[Request, Request] = ServiceCall { request: Request =>

    try {
      val response = requestProcessor.getDocument(request)
      Future(response)
    } catch {
      case nullPointerException: NullPointerException => Future(Request(request.id, Some("Not found")))
      case _: Exception => Future(Request(request.id, Some("Not found")))
    }
  }
}
