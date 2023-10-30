package de.htwg.rs.model.utils

import de.htwg.rs.model.models.{Country, StreamingProvider}

import scala.collection.immutable.Map
import scala.collection.mutable

/** Computes a map of streaming providers and the percentage of countries that
  * support it
  */
def getSpreadStreamingProvider(countries: List[Country]): Map[String, Int] =
  countries
    .flatMap(_.servicesAsList)
    .groupBy(identity)
    .view
    .mapValues(_.length * 100 / countries.length)
    .to(Map)

/** Returns a list of all streaming providers */
def getAllStreamingProviderAsList(countries: List[Country]): List[String] =
  countries
    .flatMap(_.servicesAsList)
    .distinct

def getStreamingProvider(countries: List[Country]): List[StreamingProvider] =
  // create empty Array of streaming providers
  var streamingProvider = List[StreamingProvider]()
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
    streamingProvider: List[StreamingProvider]
): Map[String, Int] =
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
  return Map.from(streamingProviderSupportedStreamingTypesPercentage)
