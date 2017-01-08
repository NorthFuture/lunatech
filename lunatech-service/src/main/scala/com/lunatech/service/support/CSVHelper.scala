package com.lunatech.service.support

import net.tixxit.delimited._

class CSVHelper {

}

object CSVHelper {

  val myFormat: DelimitedFormat = DelimitedFormat(
    separator = ",",
    quote = "\"",
    rowDelim = RowDelim("\n")
  )

  def splitLine(line: String): Vector[Option[String]] = {

    val rows: Vector[Either[DelimitedError, Row]] = DelimitedParser(myFormat).parseString(line)

    if (rows.length != 1) {
      Vector()
    } else {
      rows(0) match {
        case Left(e) => Vector()
        case Right(r) => r.toVector.map(x => if (x == null || x == "") None else Some(x))
      }
    }
  }
}
