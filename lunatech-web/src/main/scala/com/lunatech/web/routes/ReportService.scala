package com.lunatech.web.routes

import akka.actor.Props
import akka.pattern.ask
import akka.http.scaladsl.server.Route
import com.lunatech.bean.ReportResult
import com.lunatech.service.ReportActor
import com.lunatech.service.repository.DataRepository
import com.lunatech.web.jsonformat.LunatechReportJsonFormats
import com.lunatech.web.support.BaseService

import scala.concurrent.ExecutionContext

trait ReportService extends BaseService with LunatechReportJsonFormats {

  implicit def executionContext: ExecutionContext

  def dataRepository: DataRepository

  val reportyServiceRoutes: Route =

    path("report") {
      get {

        val reportActor = actorSystem.actorOf(Props(classOf[ReportActor], dataRepository))

        onSuccess(reportActor ? ReportActor.StandardReportType()) {
          case response: ReportResult =>
            complete(response)
        }
      }
    }
}