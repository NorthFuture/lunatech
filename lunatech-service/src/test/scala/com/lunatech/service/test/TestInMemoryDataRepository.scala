package com.lunatech.service.test

import com.lunatech.model.Continent
import com.lunatech.service.repository.InMemoryDataRepository
import org.junit.runner.RunWith
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}


@RunWith(classOf[JUnitRunner])
class TestInMemoryDataRepository extends FunSuiteLike with BeforeAndAfterAll with ScalaFutures {

  test("test parse countries length") {
    assert(247 == InMemoryDataRepository.getCountries().length)
  }

  test("test parsed values") {

    assert(302672 == InMemoryDataRepository.getCountries()(0).id)
    assert("AD" == InMemoryDataRepository.getCountries()(0).code)
    assert("Andorra" == InMemoryDataRepository.getCountries()(0).name)
    assert(Continent("EU") == InMemoryDataRepository.getCountries()(0).continent)
    assert(Some("http://en.wikipedia.org/wiki/Andorra") == InMemoryDataRepository.getCountries()(0).wikipediaLink)
    assert(None == InMemoryDataRepository.getCountries()(0).keywords)

    assert(302618 == InMemoryDataRepository.getCountries()(1).id)
    assert("AE" == InMemoryDataRepository.getCountries()(1).code)
    assert("United Arab Emirates" == InMemoryDataRepository.getCountries()(1).name)
    assert(Continent("AS") == InMemoryDataRepository.getCountries()(1).continent)
    assert(Some("http://en.wikipedia.org/wiki/United_Arab_Emirates") == InMemoryDataRepository.getCountries()(1).wikipediaLink)
    assert(Some("UAE") == InMemoryDataRepository.getCountries()(1).keywords)
  }

  test("test continents") {
    assert(7 == InMemoryDataRepository.getContinents.length)
  }

  test("test airports") {
    assert(46505 == InMemoryDataRepository.getAirports.length)
  }

  test("test runways") {
    assert(39536 == InMemoryDataRepository.getRunways.length)
  }

  // TODO: missing tests  for airport population e runways population
}
