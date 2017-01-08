package com.lunatech.bean

import com.lunatech.model.Country

case class AirportsRunwayByCountrySearchResult(country: Option[Country], airports: Seq[AirportWithRunways], exact: Boolean)
