package com.lunatech.bean

import com.lunatech.model.{Airport, Country}

case class CountryWithAirports(country: Country, airports: Seq[Airport])
