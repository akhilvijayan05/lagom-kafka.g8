package com.example.impl.elasticsearch

import com.example.impl.utils.constant.Constants
import com.example.models.Request
import io.searchbox.client.JestClientFactory
import io.searchbox.client.config.HttpClientConfig
import io.searchbox.core.{Delete, Get, Index, Update}
import play.api.libs.json.Json

/**
  * Jest is a Java HTTP Rest client for ElasticSearch.
  */
trait JestClient {

  val jestClientFactory: JestClientFactory = new JestClientFactory()
  lazy val address = Constants.ADDRESS
  jestClientFactory.setHttpClientConfig(new HttpClientConfig
  .Builder(address)
    .multiThreaded(true)
    .build())

  val jestClient = jestClientFactory.getObject

  /**
    * creates a document in elasticsearch
    * @param request is the incoming request
    * @return the request id
    */
  def createDocument(request: Request): String = {
    val jsonData = Json.toJson(request).toString()
    val documentResult = jestClient.execute(new Index.Builder(jsonData).index(Constants.INDEX).`type`(request.getClass.getSimpleName.toLowerCase).id(request.id).build())
    documentResult.getId
  }

  /**
    * gets a document in elasticsearch
    * @param request is the incoming request
    * @return the request object
    */
  def getDocument(request: Request): Request = {

    val getResult = new Get.Builder(Constants.INDEX, request.id).`type`(request.getClass.getSimpleName.toLowerCase).build()
    val documentResult = jestClient.execute(getResult)
    val requestData = Json.parse(documentResult.getSourceAsString).as[Request]
    requestData
  }

  /**
    * updates a document in elasticsearch
    * @param request is the incoming request
    * @return boolean value
    */
  def updateDocument(request: Request): Boolean = {

    val jsonData = Json.toJson(request).toString()
    jestClient.execute(new Update.Builder(jsonData).index(Constants.INDEX).`type`(request.getClass.getSimpleName.toLowerCase).id(request.id).build())
    true
  }

  /**
    * deletes a document in elasticsearch
    * @param request is the incoming request
    * @return boolean value
    */
  def deleteDocument(request: Request): Boolean = {

    jestClient.execute(new Delete.Builder(request.id).index(Constants.INDEX).`type`(request.getClass.getSimpleName.toLowerCase).build())
    true
  }
}
