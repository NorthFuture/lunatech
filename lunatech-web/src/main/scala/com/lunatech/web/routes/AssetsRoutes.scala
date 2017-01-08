package com.lunatech.web.routes

import akka.http.scaladsl.server.Directives._

trait AssetsRoutes {

  def assetsRoutes = {
    pathPrefix("web") {
      getFromResourceDirectory("web")
    }
  }

}
