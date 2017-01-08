package com.lunatech.web.jsonformat

import com.lunatech.bean.{AirportWithRunways, AirportsRunwayByCountrySearchResult, CountrySearchResult}
import com.lunatech.model._
import com.lunatech.web.support.JsonSupport
import spray.json.{JsString, JsValue, RootJsonFormat}

trait LunatechQueryJsonFormats extends LunatechModelJsonFormats {

  implicit val airportWithRunwaysJsonFormat = jsonFormat2(AirportWithRunways.apply)

  implicit val airportsRunwayByCountrySearchResultFormat = jsonFormat3(AirportsRunwayByCountrySearchResult.apply)

  implicit val countrySearchResultFormat = jsonFormat2(CountrySearchResult.apply)
}
