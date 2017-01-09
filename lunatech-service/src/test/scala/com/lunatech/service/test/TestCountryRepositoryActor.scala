package com.lunatech.service.test


import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.lunatech.bean.CountrySearchResult
import com.lunatech.service.repository.{CountryRepositoryActor, InMemoryDataRepository}
import com.lunatech.service.repository.CountryRepositoryActor.SearchCountryRequest
import com.typesafe.config.ConfigFactory
import org.junit.runner.RunWith
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FunSuiteLike}

@RunWith(classOf[JUnitRunner])
class TestCountryRepositoryActor(_system: ActorSystem) extends TestKit(_system) with FunSuiteLike with BeforeAndAfter with BeforeAndAfterAll with ScalaFutures with ImplicitSender {

  def this() = this(ActorSystem("TestActorSystem", ConfigFactory.load("akka.conf")))

  override def afterAll {
    TestKit.shutdownActorSystem(_system)
  }

  def target() = _system.actorOf(Props(classOf[CountryRepositoryActor],InMemoryDataRepository))

  test("test existing exact country match by code") {

    target ! SearchCountryRequest("VA")

    val result = expectMsgClass(classOf[CountrySearchResult])

    assert(result.exact)
    assert(result.result.get.code == "VA")
  }


  test("test existing exact country match  by name") {

    target ! SearchCountryRequest("Andorra")

    val result = expectMsgClass(classOf[CountrySearchResult])

    assert(result.exact)
    assert(result.result.get.code == "AD")
  }

  test("test existing partial match country by name") {

    target ! SearchCountryRequest("And")

    val result = expectMsgClass(classOf[CountrySearchResult])

    assert(!result.exact)
    assert(result.result.get.code == "AD")
  }


  test("test non existing country") {

    target ! SearchCountryRequest("xx")

    val result = expectMsgClass(classOf[CountrySearchResult])

    assert(result.result == None)
  }
}