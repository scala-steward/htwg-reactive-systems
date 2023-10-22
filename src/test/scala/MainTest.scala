import munit.FunSuite
import myPackage.models.Country
import myPackage.utils.getStreamingProvider
import myPackage.utils.getSpreadStreamingProvider
import myPackage.utils.getCountries

class MainTest extends FunSuite {
  
}
class calculateStatsTest extends FunSuite {
  test("getStreamingProvider should return an array of unique streaming providers") {
    val countries = Array(
      Country("Country1","DE", List("Netflix", "Hulu")),
      Country("Country2","CH" ,List("Netflix", "Amazon Prime")),
      Country("Country3","PL", List("Hulu", "Disney+"))
    )
    val streamingProviders = getStreamingProvider(countries)
    assert(streamingProviders.length == 4)
    assert(streamingProviders.contains("Netflix"))
    assert(streamingProviders.contains("Hulu"))
    assert(streamingProviders.contains("Amazon Prime"))
    assert(streamingProviders.contains("Disney+"))
  }

  test("getSpreadStreamingProvider should return a map of streaming providers and their percentage of countries") {
    val countries = Array(
       Country("Country1","DE", List("Netflix", "Hulu")),
      Country("Country2","CH" ,List("Netflix", "Amazon Prime")),
      Country("Country3","PL", List("Hulu", "Disney+"))
    )
    val streamingProviderPercentage = getSpreadStreamingProvider(countries)
    assert(streamingProviderPercentage.size == 4)
    assert(streamingProviderPercentage("Netflix") == 66)
    assert(streamingProviderPercentage("Hulu") == 66)
    assert(streamingProviderPercentage("Amazon Prime") == 33)
    assert(streamingProviderPercentage("Disney+") == 33)
  }
}
class dataForUiGetterTest extends FunSuite {
  test("getCountries should return an array of countries") {
    val countries = getCountries()
    assert(countries.isLeft)
    val countriesList = countries.left.get
    assert(countriesList.length > 0)
  
  }
}
