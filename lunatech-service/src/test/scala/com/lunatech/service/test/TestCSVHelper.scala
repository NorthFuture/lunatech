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

    assert("aaa,bbb" == result(3))
    assert("b" == result(2))
    assert("a" == result(1))
    assert("01" == result(0))
  }

  test("test empty field") {

    val result = CSVHelper.splitLine("""01,,02""")

    assert("02" == result(2))
    assert("" == result(1))
    assert("01" == result(0))
  }

}
