package myPackage
package utils

import myPackage.models.Country

import scala.collection.mutable
import scala.collection.mutable.Map

def getSpreadStreamingProvider(
    countries: Array[Country]
): mutable.Map[String, Int] =
  // get streaming providers
  val streamingProvider = getStreamingProvider(countries)
  // create empty Key VAlue storage of streaming providers and percentage
  val streamingProviderPercentage = mutable.Map[String, Int]()
  streamingProvider.foreach((streamingProvider) =>
    val amountStreamingProvider =
      countries.count((country) => country.services.contains(streamingProvider))
    val percentageStreamingProvider =
      (amountStreamingProvider * 100) / countries.length
    streamingProviderPercentage(streamingProvider) = percentageStreamingProvider
  )
  return streamingProviderPercentage

def getStreamingProvider(countries: Array[Country]): Array[String] =
  // create empty Array of streaming providers
  var streamingProvider = Array[String]()
  countries.foreach((country) =>
    country.services.foreach((service) =>
      if !streamingProvider.contains(service) then
        streamingProvider = streamingProvider :+ service
    )
  )
  return streamingProvider
