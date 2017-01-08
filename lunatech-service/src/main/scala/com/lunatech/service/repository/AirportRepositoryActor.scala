package com.lunatech.service.repository

import akka.actor.Actor
import akka.event.Logging
import akka.pattern.pipe
import com.lunatech.bean.{CountryWithAirportCount, CountryWithAirports, CountryWithSurfaces, RunwayIdentCount}
import com.lunatech.model.{Country, Surface}
import com.lunatech.service.repository.AirportRepositoryActor.CountryWithSurfacesRequest

import scala.concurrent.Future

class AirportRepositoryActor(dataRepository: DataRepository) extends Actor {

  import context.dispatcher

  val log = Logging(context.system, this)

  def receive = {
    case searchAirportByCountry: AirportRepositoryActor.SearchAirportsByCountryRequest =>

      // Let's simulate the query is async
      Future {
        CountryWithAirports(searchAirportByCountry.search, dataRepository.getAirports().filter(x => x.country == searchAirportByCountry.search))
      } pipeTo sender

    case surfacesByCountry: AirportRepositoryActor.CountryWithSurfacesRequest =>
      Future {

        CountryWithSurfacesRequestResponse(
          dataRepository.getRunways()
            .filter(r => r.surface != None)
            .map(r => (r.surface.get, r.airport.country))
            .distinct
            .groupBy(r => r._2)
            .map(r => CountryWithSurfaces(r._1, r._2.map(_._1).sortBy(_.name)))
            .toIndexedSeq
            .sortBy(_.country.name)
        )
      } pipeTo sender

    case topNRunwayLeIdentRequest: AirportRepositoryActor.TopNRunwayLeIdentRequest =>
      Future {
        TopNRunwayLeIdentResponse(
          dataRepository.getRunways().filter(r => r.leIdent != None).map(r => r.leIdent.get)
            .groupBy(x => x).map(x => RunwayIdentCount(x._1, x._2.size)).toIndexedSeq.sortBy(-_.count)
        )
      } pipeTo sender
  }

}

case class CountryWithSurfacesRequestResponse(result: Seq[CountryWithSurfaces])

case class TopNRunwayLeIdentResponse(result: Seq[RunwayIdentCount])

object AirportRepositoryActor {

  case class SearchAirportsByCountryRequest(search: Country)

  case class CountryWithSurfacesRequest()

  case class TopNRunwayLeIdentRequest(n: Int)

}

