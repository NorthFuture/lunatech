package com.lunatech.web

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.lunatech.service.QueryActor
import com.lunatech.service.repository.InMemoryDataRepository
import com.lunatech.web.routes.RestService

import scala.io.StdIn

object WebServer extends App with RestService {

  implicit val actorSystem = ActorSystem("LunatechSystem")

  implicit val materializer = ActorMaterializer()

  implicit val executionContext = actorSystem.dispatcher

  def dataRepository = {
    InMemoryDataRepository
  }

  // Just to trigger the inizialization
  dataRepository

  val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

  StdIn.readLine()

  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => actorSystem.terminate()) // and shutdown akka
}
