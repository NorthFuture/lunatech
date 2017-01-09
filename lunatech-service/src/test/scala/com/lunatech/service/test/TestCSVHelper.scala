package com.lunatech.service.test

import com.lunatech.service.support.CSVHelper
import org.junit.runner.RunWith
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class TestCSVHelper extends FunSuiteLike with BeforeAndAfterAll with ScalaFutures {

  test("test parse line") {

    val result = CSVHelper.splitLine("""01,"a","b","aaa,bbb"""")

    assert(Some("aaa,bbb") == result(3))
    assert(Some("b") == result(2))
    assert(Some("a") == result(1))
    assert(Some("01") == result(0))
  }

  test("test empty field") {

    val result = CSVHelper.splitLine("""01,,02""")

    assert(Some("02") == result(2))
    assert(None == result(1))
    assert(Some("01") == result(0))
  }

}
