package com.lunatech.bean

import com.lunatech.model.{Airport, Runway}

case class AirportWithRunways(airport: Airport, runways: Seq[Runway])

