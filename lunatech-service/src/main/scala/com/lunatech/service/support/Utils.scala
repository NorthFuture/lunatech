package com.lunatech.service.support

import scala.util.{Failure, Success, Try}

class Utils {

}

object Utils {

  def tryClose[A <: {def close() : Unit}, B](resource: A)(doWork: A => B): Try[B] = {
    try {
      Success(doWork(resource))
    } catch {
      case e: Exception => Failure(e)
    }
    finally {
      try {
        if (resource != null) {
          resource.close()
        }
      } catch {
        case e: Exception => println(e) // should be logged
      }
    }
  }

}
