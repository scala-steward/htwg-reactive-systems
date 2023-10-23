package utils

import myPackage.models.Country
import myPackage.utils.{getSpreadStreamingProvider, getStreamingProvider}
import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*

import scala.runtime.stdLibPatches.Predef.assert

class CalculateStatsSpec extends AnyFlatSpec with should.Matchers:

  "getStreamingProvider" should "return an array of unique streaming providers" in {
    val countries = Array(
      Country("Country1", "DE", List("Netflix", "Hulu")),
      Country("Country2", "CH", List("Netflix", "Amazon Prime")),
      Country("Country3", "PL", List("Hulu", "Disney+"))
    )

    val streamingProviders = getStreamingProvider(countries)

    streamingProviders.length should be(4)
    streamingProviders should contain("Netflix")
    streamingProviders should contain("Hulu")
    streamingProviders should contain("Amazon Prime")
    streamingProviders should contain("Disney+")
  }

  "getSpreadStreamingProvider" should "return a map of streaming providers and their percentage of countries" in {
    val countries = Array(
      Country("Country1", "DE", List("Netflix", "Hulu")),
      Country("Country2", "CH", List("Netflix", "Amazon Prime")),
      Country("Country3", "PL", List("Hulu", "Disney+"))
    )

    val streamingProviderPercentage = getSpreadStreamingProvider(countries)

    streamingProviderPercentage.size should be(4)
    streamingProviderPercentage("Netflix") should be(66)
    streamingProviderPercentage("Hulu") should be(66)
    streamingProviderPercentage("Amazon Prime") should be(33)
    streamingProviderPercentage("Disney+") should be(33)
  }
