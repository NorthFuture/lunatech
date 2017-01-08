package com.lunatech.service

import akka.actor.{Actor, ActorRef, Props}
import akka.contrib.pattern.Aggregator
import akka.event.Logging
import com.lunatech.bean._
import com.lunatech.service.ReportActor.StandardReportType
import com.lunatech.service.repository._

class ReportActor(dataRepository: DataRepository) extends Actor with Aggregator {

  val log = Logging(context.system, this)

  val countryRepositoryActor = context.system.actorOf(Props(classOf[CountryRepositoryActor], dataRepository))

  val airportRepositoryActor = context.system.actorOf(Props(classOf[AirportRepositoryActor], dataRepository))

  expectOnce {
    case standardReportType: StandardReportType ⇒ new StandardReportyActorHandler(sender())
  }

  class StandardReportyActorHandler(originalSender: ActorRef) {

    countryRepositoryActor ! CountryRepositoryActor.CountAirportsByCountryRequest()
    airportRepositoryActor ! AirportRepositoryActor.CountryWithSurfacesRequest()
    airportRepositoryActor ! AirportRepositoryActor.TopNRunwayLeIdentRequest(AppConfig.NumberOfEntriesInScoring)

    var response = ReportResult(Seq(), Seq(), Seq(), Seq())
    var stepsLeft = 3;

    val expectHandler = expect {
      case countAirportsResponse: CountAirportsResponse =>

        response = response.copy(topCountries = countAirportsResponse.result.sortBy(x=>(-x.count,x.country.name)).take(AppConfig.NumberOfEntriesInScoring))
        response = response.copy(bottomCountries = countAirportsResponse.result.sortBy(x=>(x.count,x.country.name)).take(AppConfig.NumberOfEntriesInScoring))

        stepsLeft = stepsLeft - 1

        trySendResponse(response)

      case countryWithSurfacesRequestResponse: CountryWithSurfacesRequestResponse =>

        response = response.copy(countriesWithSurfaces = countryWithSurfacesRequestResponse.result)

        stepsLeft = stepsLeft - 1

        trySendResponse(response)

      case topNRunwayLeIdentResponse: TopNRunwayLeIdentResponse =>

        response = response.copy(mostCommonRunwayIdents = topNRunwayLeIdentResponse.result)

        stepsLeft = stepsLeft - 1

        trySendResponse(response)
    }

    def trySendResponse(response: ReportResult): Unit = {

      if (stepsLeft == 0) {
        unexpect(expectHandler)
        originalSender ! response

        context.stop(self)
      }
    }
  }

}

object ReportActor {

  case class StandardReportType()

}