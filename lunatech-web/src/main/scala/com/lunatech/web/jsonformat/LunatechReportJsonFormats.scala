package com.lunatech.web.jsonformat

import com.lunatech.bean.{RunwayIdentCount, _}
import com.lunatech.model._
import com.lunatech.web.support.JsonSupport
import spray.json.{JsString, JsValue, RootJsonFormat}

trait LunatechReportJsonFormats extends LunatechModelJsonFormats {
  implicit val countryWithAirportCountJsonFormat = jsonFormat2(CountryWithAirportCount.apply)

  implicit val countryWithSurfacesJsonFormat = jsonFormat2(CountryWithSurfaces.apply)

  implicit val runwayIdentCountJsonFormat = jsonFormat2(RunwayIdentCount.apply)

  implicit val reportResultJsonFormat = jsonFormat4(ReportResult.apply)

}
