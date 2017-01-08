package com.lunatech.service.repository

import akka.actor.Actor
import akka.event.Logging
import com.lunatech.model.Airport
import akka.pattern.pipe
import com.lunatech.bean.AirportWithRunways

import scala.concurrent.Future


class RunwayRepositoryActor(dataRepository:DataRepository) extends Actor {

  import context.dispatcher

  val log = Logging(context.system, this)

  def receive = {
    case searchRunwaysByAirport: RunwayRepositoryActor.SearchRunwaysByAirport =>

      // Let's simulate the query is async
      Future {
        AirportWithRunways(searchRunwaysByAirport.search, dataRepository.getRunways().filter(x => x.airport == searchRunwaysByAirport.search))
      } pipeTo sender
  }

}

object RunwayRepositoryActor {

  case class SearchRunwaysByAirport(search: Airport)

}

