package com.lunatech.model


case class Runway(id: Long,
                  airport: Airport,
                  length: Option[BigDecimal],
                  width: Option[BigDecimal],
                  surface: Option[Surface],
                  lighted: Boolean,
                  closed: Boolean,
                  leIdent: Option[String],
                  leLocation: Option[GeoLocation],
                  leHeading: Option[BigDecimal],
                  leThreshold: Option[BigDecimal],
                  heIdent: Option[String],
                  heLocation: Option[GeoLocation],
                  heHeading: Option[BigDecimal],
                  heThreshold: Option[BigDecimal]
                 )