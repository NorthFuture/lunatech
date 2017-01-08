package com.lunatech.bean

import com.lunatech.model.Runway

case class ReportResult(topCountries: Seq[CountryWithAirportCount], bottomCountries: Seq[CountryWithAirportCount],
                        countriesWithSurfaces: Seq[CountryWithSurfaces],
                        mostCommonRunwayIdents: Seq[RunwayIdentCount])

