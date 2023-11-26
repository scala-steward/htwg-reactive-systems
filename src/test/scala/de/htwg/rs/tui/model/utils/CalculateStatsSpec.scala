package de.htwg.rs.tui.model.utils

import de.htwg.rs.apiclient
import de.htwg.rs.apiclient.*
import de.htwg.rs.tui.model.utils.{
  getAllStreamingProviderAsList,
  getPaymentModelsSpreadFromStreamingProvider,
  getSpreadStreamingProvider
}

import scala.collection.mutable
import scala.runtime.stdLibPatches.Predef.assert

import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*

class CalculateStatsSpec extends AnyFlatSpec with should.Matchers:

  "getSpreadStreamingProvider" should "return a map of streaming providers and their percentage of countries that support them" in {
    val countries = List(
      apiclient.Country(
        name = "Country A",
        code = "de",
        servicesRaw = ujson.Obj(
          "Netflix" -> ujson.Obj()
        ),
        servicesAsList = List("Netflix", "Hulu", "Disney+")
      )
    )

    val result = getSpreadStreamingProvider(countries)

    result shouldEqual mutable.Map(
      "Netflix" -> 100,
      "Disney+" -> 100,
      "Hulu" -> 100
    )
  }

  it should "return an empty map when given an empty list of countries" in {
    val countries = List.empty[Country]

    val result = getSpreadStreamingProvider(countries)

    result shouldBe empty
  }

  it should "return a map with a single streaming provider when given an list of countries with only one streaming provider" in {
    val countries = List(
      apiclient.Country(
        "Country A",
        "de",
        ujson.Obj(),
        servicesAsList = List("Netflix")
      ),
      apiclient.Country(
        "Country B",
        "pl",
        ujson.Obj(),
        servicesAsList = List("Netflix")
      ),
      apiclient.Country(
        "Country C",
        "af",
        ujson.Obj(),
        servicesAsList = List("Netflix")
      )
    )

    val result = getSpreadStreamingProvider(countries)

    result shouldEqual mutable.Map("Netflix" -> 100)
  }

  it should "return a map with a single streaming provider when given an list of countries with only one streaming provider and one country" in {
    val countries = List(
      apiclient.Country(
        "Country A",
        "de",
        ujson.Obj(),
        servicesAsList = List("Netflix")
      )
    )

    val result = getSpreadStreamingProvider(countries)

    result shouldEqual mutable.Map("Netflix" -> 100)
  }

  "getAllStreamingProviderAsList" should "return a list of all streaming providers in the given countries" in {
    val countries = List(
      apiclient.Country(
        name = "Country A",
        code = "de",
        servicesRaw = ujson.Obj(
          "Netflix" -> ujson.Obj(
            "id" -> "netflix",
            "name" -> "Netflix",
            "homePage" -> "https://www.netflix.com",
            "supportedStreamingTypes" -> ujson.Obj(
              "buy" -> true,
              "subscribe" -> true,
              "rent" -> false,
              "addon" -> false,
              "free" -> false
            )
          )
        ),
        servicesAsList = List("Netflix", "Hulu", "Disney+")
      ),
      apiclient.Country(
        name = "Country B",
        code = "pl",
        servicesRaw = ujson.Obj(
          "Netflix" -> ujson.Obj(
            "id" -> "netflix",
            "name" -> "Netflix",
            "homePage" -> "https://www.netflix.com",
            "supportedStreamingTypes" -> ujson.Obj(
              "buy" -> true,
              "subscribe" -> true,
              "rent" -> false,
              "addon" -> false,
              "free" -> false
            )
          )
        ),
        servicesAsList = List("Netflix", "Hulu", "Disney+")
      ),
      apiclient.Country(
        name = "Country C",
        code = "af",
        servicesRaw = ujson.Obj(
          "Netflix" -> ujson.Obj(
            "id" -> "netflix",
            "name" -> "Netflix",
            "homePage" -> "https://www.netflix.com",
            "supportedStreamingTypes" -> ujson.Obj(
              "buy" -> true,
              "subscribe" -> true,
              "rent" -> false,
              "addon" -> false,
              "free" -> false
            )
          )
        ),
        servicesAsList = List("Netflix", "Hulu", "Disney+")
      )
    )

    val expected = List("Netflix", "Hulu", "Disney+")

    val result = getAllStreamingProviderAsList(countries)

    result should contain theSameElementsAs expected
  }

  "getPaymentModelsSpreadFromStreamingProvider" should "return a map of streaming provider streaming type and percentage of countries that support it" in {
    val streamingProviders = List(
      StreamingProvider(
        "Netflix",
        "Netflix",
        "",
        Map("buy" -> true, "subscribe" -> true)
      ),
      StreamingProvider(
        "Hulu",
        "Hulu",
        "",
        Map("buy" -> false, "subscribe" -> true)
      ),
      StreamingProvider(
        "Disney+",
        "Disney+",
        "",
        Map("buy" -> true, "subscribe" -> false)
      )
    )

    val expected = Map(
      "subscribe" -> 66,
      "buy" -> 66
    )

    val result = getPaymentModelsSpreadFromStreamingProvider(streamingProviders)

    result shouldEqual expected
  }
//
//  "getCountChanges" should "return the number of changes for a given set of parameters" in {
//    val result = getCountChanges(
//      ChangeType.New,
//      ServiceChange.Netflix,
//      TargetType.Movie,
//      "de",
//      None
//    )
//    result.isLeft should be(true)
//    result.left.get should be >= 0
//  }
//
//  it should "return an error message if there was an error getting changes from the API" in {
//    val result = getCountChanges(
//      ChangeType.New,
//      ServiceChange.Netflix,
//      TargetType.Movie,
//      "de",
//      Some("cursor")
//    )
//    result.isRight should be(true)
//    result.right.get should be("Error getting changes from api")
//  }
//
//  it should "return an error message if any of the getCountChanges calls return an error" in {
//    val result = getCountChangesForEveryService(
//      ChangeType.Removed,
//      TargetType.Episode,
//      "invalid_country_code"
//    )
//    result.isLeft shouldBe true
//    info("result.left.get: " + result.left.get.toString())
//    // check if key of HashMap contains "error!"
//    result.left.get.forall(_.toString.contains("error!")) shouldBe true
//  }
