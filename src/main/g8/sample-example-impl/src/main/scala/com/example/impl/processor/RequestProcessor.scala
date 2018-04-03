package com.example.impl.processor

import com.example.impl.elasticsearch.JestClient
import com.example.impl.kafka.Producer
import com.example.models.{Log, Request, Response}

/**
  * Handles the json body coming from the request
  */
class RequestProcessor extends Producer with JestClient {

  def storeDataInKafka(timestamp: String, request: Request, response: Response, topic:String) = {
    val log = Log(timestamp, request, response)
    produceData(log, topic)
  }

}
