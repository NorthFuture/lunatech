package com.lunatech.web.jsonformat

import com.lunatech.bean.{AirportWithRunways, AirportsRunwayByCountrySearchResult, CountrySearchResult}
import com.lunatech.model._
import com.lunatech.web.support.JsonSupport
import spray.json.{JsString, JsValue, RootJsonFormat}

trait LunatechModelJsonFormats extends JsonSupport {
  implicit val continentJsonFormat = jsonFormat1(Continent.apply)

  implicit val countryJsonFormat = jsonFormat6(Country.apply)

  implicit val regionJsonFormat = jsonFormat2(Region.apply)

  implicit val geoLocationJsonFormat = jsonFormat3(GeoLocation.apply)

  implicit object AirportTypeFormat extends RootJsonFormat[AirportType] {
    def write(c: AirportType) = JsString(c.value)

    def read(value: JsValue) = AirportType.parseAirportType(value.toString)
  }

  implicit val airportJsonFormat = jsonFormat16(Airport.apply)

  implicit val surfaceJsonFormat = jsonFormat1(Surface.apply)

  implicit val runwayJsonFormat = jsonFormat15(Runway.apply)
}
