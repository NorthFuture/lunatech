package com.lunatech.service

import com.typesafe.config.ConfigFactory

object AppConfig {

  private val config = ConfigFactory.load()

  private lazy val root = config.getConfig("application.conf")

  lazy val NumberOfEntriesInScoring = config.getInt("app.NumberOfEntriesInScoring")
}