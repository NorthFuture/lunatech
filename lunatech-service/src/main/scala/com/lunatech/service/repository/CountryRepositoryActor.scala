package com.lunatech.service.repository

import akka.actor.Actor
import akka.event.Logging
import akka.pattern.pipe
import com.lunatech.bean.{AirportsRunwayByCountrySearchResult, CountrySearchResult, CountryWithAirportCount}
import com.lunatech.model.Country
import com.lunatech.service.repository.CountryRepositoryActor.{CountAirportsByCountryRequest, SearchCountryRequest}

import scala.concurrent.Future

/**
  * This actor implements the repository pattern to query the data. For the coding test everything is held in memory
  */
class CountryRepositoryActor(dataRepository: DataRepository) extends Actor {

  import context.dispatcher

  val log = Logging(context.system, this)

  def searchExact(search: String): Option[Country] = {
    val searchUppercase = search.toUpperCase

    dataRepository.getCountries.filter(c => c.code.toUpperCase == searchUppercase || c.name.toUpperCase == searchUppercase).headOption
  }

  def searchPartialMatch(search: String): Option[Country] = {
    val searchUppercase = search.toUpperCase

    dataRepository.getCountries.filter(c => c.name.toUpperCase.contains(searchUppercase)).headOption
  }

  def receive = {
    case searchCountry: SearchCountryRequest =>

      // Let's simulate the query is async
      Future {
        val rExact = searchExact(searchCountry.search)

        if (!rExact.isEmpty) {
          CountrySearchResult(rExact, true)
        } else {
          val rPartial = searchPartialMatch(searchCountry.search)

          if (!rPartial.isEmpty) {
            CountrySearchResult(rPartial, false)
          } else {
            CountrySearchResult(None, false)
          }
        }

      } pipeTo sender

    case countAirports: CountAirportsByCountryRequest =>
      Future {
        CountAirportsResponse(
          dataRepository.getAirports().groupBy(x => x.country).map(x => CountryWithAirportCount(x._1, x._2.size)).toIndexedSeq
        )
      } pipeTo sender
  }
}

case class CountAirportsResponse(result: Seq[CountryWithAirportCount])

object CountryRepositoryActor {

  case class SearchCountryRequest(search: String)

  case class CountAirportsByCountryRequest()

}
