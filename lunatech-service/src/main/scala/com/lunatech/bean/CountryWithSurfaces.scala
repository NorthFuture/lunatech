package com.lunatech.bean

import com.lunatech.model.{Country, Surface}

case class CountryWithSurfaces(country: Country, surfaces: Seq[Surface])

