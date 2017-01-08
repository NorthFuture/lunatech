package com.lunatech.web.routes

import akka.actor.Props
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import com.lunatech.bean.AirportsRunwayByCountrySearchResult
import com.lunatech.service.QueryActor
import com.lunatech.service.repository.DataRepository
import com.lunatech.web.jsonformat.{LunatechModelJsonFormats, LunatechQueryJsonFormats}
import com.lunatech.web.support.BaseService

trait QueryService extends BaseService with LunatechQueryJsonFormats {

  def dataRepository: DataRepository

  val queryServiceRoutes: Route =

    path("searchAirportsWithRunwaysByCountry" / Segment) { x =>
      get {

        val queryActor = actorSystem.actorOf(Props(classOf[QueryActor], dataRepository))

        onSuccess(queryActor ? x) {
          case response: AirportsRunwayByCountrySearchResult =>
            complete(response)
        }

      }
    }
}