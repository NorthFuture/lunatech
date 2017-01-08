package com.lunatech.model


sealed trait AirportType {
  def value: String
}

object AirportType {

  def parseAirportType(string: String): AirportType = {
    string match {
      case SmallAirport.value => SmallAirport
      case MediumAirport.value => MediumAirport
      case LargeAirport.value => LargeAirport
      case Heliport.value => Heliport
      case SeaPlane.value => SeaPlane
      case BalloonPort.value => BalloonPort
      case Closed.value => Closed
    }
  }

  case object SmallAirport extends AirportType {
    val value = "small_airport"
  }

  case object MediumAirport extends AirportType {
    val value = "medium_airport"
  }

  case object LargeAirport extends AirportType {
    val value = "large_airport"
  }

  case object Heliport extends AirportType {
    val value = "heliport"
  }

  case object SeaPlane extends AirportType {
    val value = "seaplane_base"
  }

  case object BalloonPort extends AirportType {
    val value = "balloonport"
  }

  case object Closed extends AirportType {
    val value = "closed"
  }

}