package de.htwg.rs.utils

import de.htwg.rs.models.{Country, StreamingProvider}

import scala.collection.mutable
import scala.collection.mutable.Map

def getSpreadStreamingProvider(
    countries: Array[Country]
): mutable.Map[String, Int] =
  // get streaming providers
  val streamingProvider = getAllStreamingProviderAsList(countries)
  // create empty Key VAlue storage of streaming providers and percentage
  val streamingProviderPercentage = mutable.Map[String, Int]()
  streamingProvider.foreach((streamingProvider) =>
    val amountStreamingProvider =
      countries.count((country) =>
        country.servicesAsList.contains(streamingProvider)
      )
    val percentageStreamingProvider =
      (amountStreamingProvider * 100) / countries.length
    streamingProviderPercentage(streamingProvider) = percentageStreamingProvider
  )
  return streamingProviderPercentage

def getAllStreamingProviderAsList(countries: Array[Country]): Array[String] =
  // create empty Array of streaming providers
  var streamingProvider = Array[String]()
  countries.foreach((country) =>
    country.servicesAsList.foreach((service) =>
      if !streamingProvider.contains(service) then
        streamingProvider = streamingProvider :+ service
    )
  )
  return streamingProvider

def getStreamingProvider(countries: Array[Country]): Array[StreamingProvider] =
  // create empty Array of streaming providers
  var streamingProvider = Array[StreamingProvider]()
  countries.foreach((country) =>
    country.servicesAsList.foreach((service) =>
      if !streamingProvider.exists((streamingProvider) =>
          streamingProvider.id == service
        )
      then
        val streamingProviderName = country.servicesRaw(service)("name")
        val streamingProviderUrl = country.servicesRaw(service)("homePage")
        val streamingProviderSupportedStreamingTypes =
          country.servicesRaw(service)("supportedStreamingTypes")
        val streamingProviderSupportedStreamingTypesMap =
          streamingProviderSupportedStreamingTypes.obj
        var streamingProviderSupportedStreamingTypesMapAsScala =
          mutable.Map[String, Boolean]()
        streamingProviderSupportedStreamingTypesMap.foreach((key, value) =>
          streamingProviderSupportedStreamingTypesMapAsScala(key) = value.bool
        )
        val streamingProviderObj = StreamingProvider(
          streamingProviderName.str,
          service,
          streamingProviderUrl.str,
          streamingProviderSupportedStreamingTypesMapAsScala.toMap
        )
        streamingProvider = streamingProvider :+ streamingProviderObj
    )
  )
  return streamingProvider

  // returns map of streaming provider streaming type and percentage of countries that support it
  // for example: Map("buy" -> 50, "subscripe" -> 90)
  // one streaming provider can support multiple streaming types
  // you can get the Array of streaming providers from getStreamingProvider()
def getPaymentModelsSpreadFromStreamingProvider(
    streamingProvider: Array[StreamingProvider]
): mutable.Map[String, Int] =
  // create empty Key VAlue storage of streaming providertype and percentage
  val streamingProviderSupportedStreamingTypesCount = mutable.Map[String, Int]()
  streamingProvider.foreach((streamingProvider) =>
    streamingProvider.supportedStreamingTypes.foreach((key, value) =>
      if value == true then
        if !streamingProviderSupportedStreamingTypesCount.contains(key) then
          streamingProviderSupportedStreamingTypesCount(key) = 1
        else streamingProviderSupportedStreamingTypesCount(key) += 1
    )
  )
  val streamingProviderSupportedStreamingTypesPercentage =
    mutable.Map[String, Int]()
  streamingProviderSupportedStreamingTypesCount.foreach((key, value) =>
    val percentage = (value * 100) / streamingProvider.length
    streamingProviderSupportedStreamingTypesPercentage(key) = percentage
  )
  return streamingProviderSupportedStreamingTypesPercentage
