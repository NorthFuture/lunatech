package com.lunatech.model

case class Airport(id: Long,
                   ident: String,
                   airportType: AirportType,
                   name: String,
                   location: GeoLocation,
                   continent: Continent,
                   country: Country,
                   region: Region,
                   municipality: Option[String],
                   scheduledService: Boolean,
                   gpsCode: Option[String],
                   iataCode: Option[String],
                   localCode: Option[String],
                   homeLink: Option[String],
                   wikipediaLink: Option[String],
                   keywords: Option[String]
                  )