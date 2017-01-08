package com.lunatech.service.repository

import java.io.InputStream

import com.lunatech.model.{Surface, _}
import com.lunatech.service.support.CSVHelper
import com.lunatech.service.support.Utils.tryClose
import com.typesafe.scalalogging.Logger

import scala.io.Source
import scala.util.Try

class InMemoryDataRepository {

}

object InMemoryDataRepository extends DataRepository {
  val logger = Logger[InMemoryDataRepository]

  private val countries: Seq[Country] = tryClose(getClass.getResourceAsStream("/countries.csv"))(readCountriesFromStream).getOrElse(Seq[Country]())
  private val continents: Seq[Continent] = countries.map(_.continent).distinct
  private val airports: Seq[Airport] = tryClose(getClass.getResourceAsStream("/airports.csv"))(readAirportsFromStream).getOrElse(Seq[Airport]())
  private val runways: Seq[Runway] = tryClose(getClass.getResourceAsStream("/runways.csv"))(readRunwaysFromStream).getOrElse(Seq[Runway]())

  def getCountries() = countries

  def getContinents() = continents

  def getAirports() = airports

  def getRunways() = runways

  def profile[R](id: String, block: => R): R = {
    val t0 = System.nanoTime()
    val result = block
    val t1 = System.nanoTime()

    val elapsed = (t1 - t0) / (1000 * 1000)

    logger.info(s"$id loaded in $elapsed ms")
    result
  }


  private def parseStringToOptionBigDecimal(value: Option[String]): Option[BigDecimal] = {
    //Try(value.map(BigDecimal.apply)).getOrElse(None)

    value.map(BigDecimal.apply)
  }

  private def parseStringToBoolean(value: Option[String]) = {
    Try(value.map(_.toBoolean).getOrElse(false)).getOrElse(false)
  }

  private def readRunwaysFromStream(is: InputStream): Seq[Runway] = {
    profile("readRunways", {

      // To speedup the search
      val airportsById = airports.map(a => (a.id, a)).toMap

      Source.fromInputStream(is).getLines.drop(1).map(line => {
        val cols = CSVHelper.splitLine(line)

        try {
          // "id","airport_ref","airport_ident","length_ft","width_ft","surface","lighted","closed","le_ident","le_latitude_deg","le_longitude_deg","le_elevation_ft","le_heading_degT","le_displaced_threshold_ft","he_ident","he_latitude_deg","he_longitude_deg","he_elevation_ft","he_heading_degT","he_displaced_threshold_ft",
          // 269408,6523,"00A",80,80,"ASPH-G",1,0,"H1",,,,,,,,,,,

          val id = cols(0).get.toLong
          val airport = airportsById.get(cols(1).get.toLong).head

          val length = parseStringToOptionBigDecimal(cols(3))
          val width = parseStringToOptionBigDecimal(cols(4))

          val surface = cols(5).map(Surface(_))

          val lighted = parseStringToBoolean(cols(6))
          val closed = parseStringToBoolean(cols(7))

          val leIdent = cols(8)

          // Many latitudes and longitudes for runways are missing....if one of the two are missing i discard the location alltogheter
          val leLocation = Try(GeoLocation(BigDecimal(cols(9).get), BigDecimal(cols(10).get), BigDecimal(cols(11).getOrElse("0")))).toOption

          val leHeading = parseStringToOptionBigDecimal(cols(12))
          val leThreshold = parseStringToOptionBigDecimal(cols(13))
          val heIdent = cols(14)


          val heLocation = Try(GeoLocation(BigDecimal(cols(15).get), BigDecimal(cols(16).get), BigDecimal(cols(17).getOrElse("0")))).toOption
          val heHeading = parseStringToOptionBigDecimal(cols(18))
          val heThreshold = parseStringToOptionBigDecimal(cols(19))

          Runway(id, airport, length, width, surface, lighted, closed, leIdent, leLocation, leHeading, leThreshold, heIdent, heLocation, heHeading, heThreshold)
        }
        catch {
          case e: Exception => logger.error(s"can't parse Runway with $line", e)
            null
        }
      }).filter(_ != null).toIndexedSeq
    })
  }

  private def readAirportsFromStream(is: InputStream): Seq[Airport] = {
    profile("readAirports", {
      Source.fromInputStream(is).getLines.drop(1).map(line => {
        val cols = CSVHelper.splitLine(line)

        try {
          // "id","ident","type","name","latitude_deg","longitude_deg","elevation_ft","continent","iso_country","iso_region","municipality","scheduled_service","gps_code","iata_code","local_code","home_link","wikipedia_link","keywords"
          // 6523,"00A","heliport","Total Rf Heliport",40.07080078125,-74.93360137939453,11,"NA","US","US-PA","Bensalem","no","00A",,"00A",,,

          val id = cols(0).get.toLong
          val ident = cols(1).get
          val airportType = AirportType.parseAirportType(cols(2).get)
          val name = cols(3).get
          val location = GeoLocation(BigDecimal(cols(4).getOrElse("0")), BigDecimal(cols(5).getOrElse("0")), BigDecimal(cols(6).getOrElse("0")))

          // Let it crash if the country is not found
          val country = countries.filter(x => x.code == cols(8).get).head

          // I found some inconsistency between continent set in airports and in countries
          val continent = continents.filter(x => x.name == cols(7).get /*&& x.name == country.continent.name*/).head

          val region = Region(cols(9).get, country)
          val municipality = cols(10)
          val scheduledService: Boolean = parseStringToBoolean(cols(11))
          val gpsCode = cols(12)
          val iataCode = cols(13)
          val localCode = cols(14)
          val homeLink = cols(15)
          val wikipediaLink = cols(16)
          val keywords = cols(17)

          Airport(id, ident, airportType, name, location, continent, country, region, municipality, scheduledService, gpsCode, iataCode, localCode, homeLink, wikipediaLink, keywords)
        } catch {
          case e: Exception => logger.error(s"can't parse Airport with $line", e)
            null
        }
      }).filter(_ != null).toIndexedSeq
    })
  }

  private def readCountriesFromStream(is: InputStream): Seq[Country] = {

    profile("readCountries", {
      Source.fromInputStream(is).getLines.drop(1).map(line => {
        val cols = CSVHelper.splitLine(line)

        try {
          // "id","code","name","continent","wikipedia_link","keywords"
          // 302672,"AD","Andorra","EU","http://en.wikipedia.org/wiki/Andorra",

          Country(cols(0).get.toLong, cols(1).get, cols(2).get, Continent(cols(3).get), cols(4), cols(5))
        } catch {
          case e: Exception => logger.error(s"can't parse Country with $line", e)
            null
        }
      }).filter(_ != null).toIndexedSeq
    })
  }

}
