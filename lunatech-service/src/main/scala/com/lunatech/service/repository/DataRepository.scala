package com.lunatech.service.repository

import com.lunatech.model.{Airport, Continent, Country, Runway}


trait DataRepository {
  def getCountries(): Seq[Country]

  def getContinents(): Seq[Continent]

  def getAirports(): Seq[Airport]

  def getRunways(): Seq[Runway]
}
