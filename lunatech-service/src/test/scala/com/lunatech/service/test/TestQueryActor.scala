package com.lunatech.service.test

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.lunatech.bean.{AirportsRunwayByCountrySearchResult, CountrySearchResult}
import com.lunatech.model.{Airport, Country, _}
import com.lunatech.service.QueryActor
import com.lunatech.service.repository.CountryRepositoryActor.SearchCountryRequest
import com.lunatech.service.repository.DataRepository
import com.typesafe.config.ConfigFactory
import org.junit.runner.RunWith
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FunSuiteLike}

@RunWith(classOf[JUnitRunner])
class TestQueryActor(_system: ActorSystem) extends TestKit(_system) with FunSuiteLike with BeforeAndAfter with BeforeAndAfterAll with ScalaFutures with ImplicitSender {

  def this() = this(ActorSystem("TestActorSystem", ConfigFactory.load("akka.conf")))

  override def afterAll {
    TestKit.shutdownActorSystem(_system)
  }

  val dataRepository = new DataRepository {

    def getCountries() = Seq(
      Country(0, "c0", "country0 name", Continent("continent"), None, None),
      Country(1, "c1", "country1 name", Continent("continent"), None, None),
      Country(2, "c2", "country2 name", Continent("continent"), None, None)
    )

    def getContinents() = Seq(Continent("continent"))

    def getAirports() = Seq(
      Airport(0, "a0", AirportType.SmallAirport, "a0 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(0), Region("", getCountries()(0)), None, false, None, None, None, None, None, None),
      Airport(1, "a1", AirportType.SmallAirport, "a1 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(0), Region("", getCountries()(0)), None, false, None, None, None, None, None, None),
      Airport(2, "a2", AirportType.SmallAirport, "a2 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(0), Region("", getCountries()(0)), None, false, None, None, None, None, None, None),
      Airport(3, "a3", AirportType.SmallAirport, "a3 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(0), Region("", getCountries()(0)), None, false, None, None, None, None, None, None),
      Airport(4, "a4", AirportType.SmallAirport, "a4 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(0), Region("", getCountries()(0)), None, false, None, None, None, None, None, None),
      Airport(5, "a5", AirportType.SmallAirport, "a5 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(0), Region("", getCountries()(0)), None, false, None, None, None, None, None, None),

      Airport(6, "a6", AirportType.SmallAirport, "a6 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(1), Region("", getCountries()(1)), None, false, None, None, None, None, None, None),
      Airport(7, "a7", AirportType.SmallAirport, "a7 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(2), Region("", getCountries()(2)), None, false, None, None, None, None, None, None)
    )

    def getRunways() = Seq()

  }

  def target() = _system.actorOf(Props(classOf[QueryActor], dataRepository), "QueryActor")

  test("test query country by code exact") {

    target ! "c0"

    val result = expectMsgClass(classOf[AirportsRunwayByCountrySearchResult])

    assert(result.exact)
    assert(result.country.get.code == "c0")
    assert(result.airports.size == 6)
    assert(result.airports.exists(_.airport.ident == "a0"))
    assert(result.airports.exists(_.airport.ident == "a1"))
    assert(result.airports.exists(_.airport.ident == "a2"))
    assert(result.airports.exists(_.airport.ident == "a3"))
    assert(result.airports.exists(_.airport.ident == "a4"))
    assert(result.airports.exists(_.airport.ident == "a5"))
  }

  test("test query country by name exact") {

    target ! "country2 name"

    val result = expectMsgClass(classOf[AirportsRunwayByCountrySearchResult])

    assert(result.exact)
    assert(result.country.get.code == "c2")
    assert(result.airports.size == 1)
    assert(result.airports.exists(_.airport.ident == "a7"))
  }

  test("test query country by partial name ") {

    target ! "country"

    val result = expectMsgClass(classOf[AirportsRunwayByCountrySearchResult])

    assert(!result.exact)
    assert(result.country.get.code == "c0")
    assert(result.airports.size == 6)
    assert(result.airports.exists(_.airport.ident == "a0"))
    assert(result.airports.exists(_.airport.ident == "a1"))
    assert(result.airports.exists(_.airport.ident == "a2"))
    assert(result.airports.exists(_.airport.ident == "a3"))
    assert(result.airports.exists(_.airport.ident == "a4"))
    assert(result.airports.exists(_.airport.ident == "a5"))
  }

  test("test query non country") {

    target ! "xyz"

    val result = expectMsgClass(classOf[AirportsRunwayByCountrySearchResult])

    assert(result.country == None)
  }

}