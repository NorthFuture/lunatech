package com.lunatech.web.routes

import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext

trait RestService extends QueryService with ReportService with AssetsRoutes {

  implicit def executionContext: ExecutionContext

  val routes: Route = assetsRoutes ~ pathPrefix("rs") {
    queryServiceRoutes ~
      reportyServiceRoutes
  }
}