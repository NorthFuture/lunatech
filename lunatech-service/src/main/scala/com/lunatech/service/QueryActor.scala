package com.lunatech.service

import akka.actor.{Actor, ActorRef, Props}
import akka.event.Logging
import com.lunatech.service.repository.{AirportRepositoryActor, CountryRepositoryActor, DataRepository, RunwayRepositoryActor}
import akka.contrib.pattern.Aggregator
import com.lunatech.bean.{AirportWithRunways, AirportsRunwayByCountrySearchResult, CountrySearchResult, CountryWithAirports}
import com.lunatech.model.{Airport, Country, Runway}

class QueryActor(dataRepository: DataRepository) extends Actor with Aggregator {

  val log = Logging(context.system, this)

  val countryRepositoryActor = context.system.actorOf(Props(classOf[CountryRepositoryActor], dataRepository))

  val airportRepositoryActor = context.system.actorOf(Props(classOf[AirportRepositoryActor], dataRepository))

  val runwayRepositoryActor = context.system.actorOf(Props(classOf[RunwayRepositoryActor], dataRepository))

  expectOnce {
    case search: String ⇒ new QueryActorHandler(sender(), search)
  }

  class QueryActorHandler(originalSender: ActorRef, search: String) {

    countryRepositoryActor ! CountryRepositoryActor.SearchCountryRequest(search)

    var response = AirportsRunwayByCountrySearchResult(None, Seq(), false)
    var airportsLeft = Seq[Airport]()

    val expectHandler = expect {
      case countrySearchResult: CountrySearchResult =>
        if (countrySearchResult.result.isEmpty) {
          sendResponse(response)
        } else {
          response = response.copy(country = countrySearchResult.result, exact = countrySearchResult.exact)

          airportRepositoryActor ! AirportRepositoryActor.SearchAirportsByCountryRequest(countrySearchResult.result.get)
        }
      case countryWithAirports: CountryWithAirports =>

        airportsLeft = countryWithAirports.airports

        if (airportsLeft.length == 0) {
          sendResponse(response)
        } else {
          countryWithAirports.airports.foreach(runwayRepositoryActor ! RunwayRepositoryActor.SearchRunwaysByAirport(_))
        }

      case airportWithRunways: AirportWithRunways =>

        response = response.copy(airports = response.airports :+ airportWithRunways)

        airportsLeft = airportsLeft.filter(_ != airportWithRunways.airport)

        if (airportsLeft.size == 0) {
          sendResponse(response)
        }
    }

    def sendResponse(response: AirportsRunwayByCountrySearchResult): Unit = {
      unexpect(expectHandler)
      originalSender ! response

      context.stop(self)
    }
  }

}