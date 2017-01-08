package com.lunatech.web.support

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonSupport  extends SprayJsonSupport with DefaultJsonProtocol {

}