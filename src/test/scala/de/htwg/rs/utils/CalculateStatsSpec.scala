package de.htwg.rs.utils

import de.htwg.rs.models.{Country, StreamingProvider}
import de.htwg.rs.utils.{
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
    val countries = Array(
      Country(
        name = "Country A",
        code = "de",
        servicesRaw = ujson.Obj(
          "Netflix" -> ujson.Obj()
        ),
        servicesAsList = List("Netflix", "Hulu", "Disney+")
      )
    )

    val result = getSpreadStreamingProvider(countries)
    println(result)

    result shouldEqual mutable.Map(
      "Netflix" -> 100,
      "Disney+" -> 100,
      "Hulu" -> 100
    )
  }

  it should "return an empty map when given an empty array of countries" in {
    val countries = Array.empty[Country]

    val result = getSpreadStreamingProvider(countries)

    result shouldBe empty
  }

  it should "return a map with a single streaming provider when given an array of countries with only one streaming provider" in {
    val countries = Array(
      Country("Country A", "de", ujson.Obj(), servicesAsList = List("Netflix")),
      Country("Country B", "pl", ujson.Obj(), servicesAsList = List("Netflix")),
      Country("Country C", "af", ujson.Obj(), servicesAsList = List("Netflix"))
    )

    val result = getSpreadStreamingProvider(countries)

    result shouldEqual mutable.Map("Netflix" -> 100)
  }

  it should "return a map with a single streaming provider when given an array of countries with only one streaming provider and one country" in {
    val countries = Array(
      Country("Country A", "de", ujson.Obj(), servicesAsList = List("Netflix"))
    )

    val result = getSpreadStreamingProvider(countries)

    result shouldEqual mutable.Map("Netflix" -> 100)
  }

  "getAllStreamingProviderAsList" should "return a list of all streaming providers in the given countries" in {
    val countries = Array(
      Country(
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
      Country(
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
      Country(
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

    val expected = Array("Netflix", "Hulu", "Disney+")

    val result = getAllStreamingProviderAsList(countries)

    result should contain theSameElementsAs expected
  }

  "getPaymentModelsSpreadFromStreamingProvider" should "return a map of streaming provider streaming type and percentage of countries that support it" in {
    val streamingProviders = Array(
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
