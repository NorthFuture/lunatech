package com.lunatech.model

case class Country(id:Long,code:String,name: String, continent: Continent, wikipediaLink: Option[String],
                   keywords: Option[String])

