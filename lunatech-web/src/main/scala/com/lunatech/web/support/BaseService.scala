package com.lunatech.web.support

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait BaseService extends Directives with JsonSupport {

  implicit def askTimeout: Timeout = 10.seconds

  implicit def executionContext: ExecutionContext

  // This will be implemented in the main WebServer and is declared here to get a reference to the actor system to each service class
  def actorSystem: ActorSystem
}