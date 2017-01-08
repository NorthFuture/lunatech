package com.lunatech.service.test

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.lunatech.bean.{AirportsRunwayByCountrySearchResult, ReportResult}
import com.lunatech.model.{Airport, _}
import com.lunatech.service.{QueryActor, ReportActor}
import com.lunatech.service.repository.DataRepository
import com.typesafe.config.ConfigFactory
import org.junit.runner.RunWith
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FunSuiteLike}

@RunWith(classOf[JUnitRunner])
class TestReportActor(_system: ActorSystem) extends TestKit(_system) with FunSuiteLike with BeforeAndAfter with BeforeAndAfterAll with ScalaFutures with ImplicitSender {

  def this() = this(ActorSystem("TestActorSystem", ConfigFactory.load("akka.conf")))

  override def afterAll {
    TestKit.shutdownActorSystem(_system)
  }

  val dataRepository = new DataRepository {

    def getCountries() = Seq(
      Country(0, "c1", "country01 name", Continent("continent"), None, None),
      Country(1, "c2", "country02 name", Continent("continent"), None, None),
      Country(2, "c3", "country03 name", Continent("continent"), None, None),
      Country(3, "c4", "country04 name", Continent("continent"), None, None),
      Country(4, "c5", "country05 name", Continent("continent"), None, None),
      Country(5, "c6", "country06 name", Continent("continent"), None, None),
      Country(6, "c7", "country07 name", Continent("continent"), None, None),
      Country(7, "c8", "country08 name", Continent("continent"), None, None),
      Country(8, "c9", "country09 name", Continent("continent"), None, None),
      Country(9, "c10", "country10 name", Continent("continent"), None, None),
      Country(10, "c11", "country11 name", Continent("continent"), None, None)
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
      Airport(7, "a7", AirportType.SmallAirport, "a7 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(1), Region("", getCountries()(1)), None, false, None, None, None, None, None, None),
      Airport(8, "a8", AirportType.SmallAirport, "a8 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(2), Region("", getCountries()(2)), None, false, None, None, None, None, None, None),
      Airport(9, "a9", AirportType.SmallAirport, "a9 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(3), Region("", getCountries()(3)), None, false, None, None, None, None, None, None),
      Airport(10, "a10", AirportType.SmallAirport, "a10 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(4), Region("", getCountries()(4)), None, false, None, None, None, None, None, None),
      Airport(11, "a11", AirportType.SmallAirport, "a11 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(5), Region("", getCountries()(5)), None, false, None, None, None, None, None, None),
      Airport(12, "a12", AirportType.SmallAirport, "a12 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(6), Region("", getCountries()(6)), None, false, None, None, None, None, None, None),
      Airport(13, "a13", AirportType.SmallAirport, "a13 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(7), Region("", getCountries()(7)), None, false, None, None, None, None, None, None),
      Airport(14, "a14", AirportType.SmallAirport, "a14 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(8), Region("", getCountries()(8)), None, false, None, None, None, None, None, None),
      Airport(15, "a15", AirportType.SmallAirport, "a15 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(9), Region("", getCountries()(9)), None, false, None, None, None, None, None, None),
      Airport(16, "a16", AirportType.SmallAirport, "a15 name", GeoLocation(0, 0, 0), Continent("continent"), getCountries()(10), Region("", getCountries()(10)), None, false, None, None, None, None, None, None)
    ).reverse

    def getRunways() = Seq(
      Runway(0, getAirports()(0), None, None, None, false, false, Some("leIdent1"), None, None, None, None, None, None, None),
      Runway(1, getAirports()(1), None, None, None, false, false, Some("leIdent1"), None, None, None, None, None, None, None),

      Runway(2, getAirports()(2), None, None, None, false, false, Some("leIdent2"), None, None, None, None, None, None, None),
      Runway(3, getAirports()(3), None, None, None, false, false, Some("leIdent2"), None, None, None, None, None, None, None),
      Runway(4, getAirports()(4), None, None, None, false, false, Some("leIdent2"), None, None, None, None, None, None, None),
      Runway(5, getAirports()(5), None, None, None, false, false, Some("leIdent2"), None, None, None, None, None, None, None),


      Runway(6, getAirports()(6), None, None, None, false, false, Some("leIdent1"), None, None, None, None, None, None, None),

      Runway(7, getAirports()(7), None, None, None, false, false, Some("leIdent2"), None, None, None, None, None, None, None),
      Runway(8, getAirports()(8), None, None, None, false, false, Some("leIdent1"), None, None, None, None, None, None, None),
      Runway(9, getAirports()(9), None, None, None, false, false, Some("leIdent2"), None, None, None, None, None, None, None),
      Runway(10, getAirports()(10), None, None, None, false, false, Some("leIdent1"), None, None, None, None, None, None, None),
      Runway(11, getAirports()(11), None, None, None, false, false, Some("leIdent2"), None, None, None, None, None, None, None),
      Runway(12, getAirports()(12), None, None, None, false, false, Some("leIdent1"), None, None, None, None, None, None, None),
      Runway(13, getAirports()(13), None, None, None, false, false, Some("leIdent2"), None, None, None, None, None, None, None),
      Runway(14, getAirports()(14), None, None, None, false, false, Some("leIdent1"), None, None, None, None, None, None, None),
      Runway(15, getAirports()(15), None, None, None, false, false, Some("leIdent2"), None, None, None, None, None, None, None)
    )

  }

  def target() = _system.actorOf(Props(classOf[ReportActor], dataRepository), "ReportActor")

  // TODO: test for a number of countries less than 10

  test("test report") {

    target ! ReportActor.StandardReportType()

    val result = expectMsgClass(classOf[ReportResult])

    assert(result.topCountries(0).country.code == "c1")
    assert(result.topCountries(0).count == 6)

    assert(result.topCountries(1).country.code == "c2")
    assert(result.topCountries(1).count == 2)

    assert(result.topCountries(2).country.code == "c3")
    assert(result.topCountries(2).count == 1)

    assert(result.topCountries(3).country.code == "c4")
    assert(result.topCountries(3).count == 1)

    assert(result.topCountries(4).country.code == "c5")
    assert(result.topCountries(4).count == 1)

    assert(result.topCountries(5).country.code == "c6")
    assert(result.topCountries(5).count == 1)

    assert(result.topCountries(6).country.code == "c7")
    assert(result.topCountries(6).count == 1)

    assert(result.topCountries(7).country.code == "c8")
    assert(result.topCountries(7).count == 1)

    assert(result.topCountries(8).country.code == "c9")
    assert(result.topCountries(8).count == 1)

    assert(result.topCountries(9).country.code == "c10")
    assert(result.topCountries(9).count == 1)

    assert(result.bottomCountries(0).country.code == "c3")
    assert(result.bottomCountries(0).count == 1)

    assert(result.bottomCountries(1).country.code == "c4")
    assert(result.bottomCountries(1).count == 1)

    assert(result.bottomCountries(9).country.code == "c2")
    assert(result.bottomCountries(9).count == 2)

    assert(result.mostCommonRunwayIdents(0).ident == "leIdent2")
    assert(result.mostCommonRunwayIdents(0).count == 9)

    assert(result.mostCommonRunwayIdents(1).ident == "leIdent1")
    assert(result.mostCommonRunwayIdents(1).count == 7)

    // TODO: missing test for surfaces
  }

}