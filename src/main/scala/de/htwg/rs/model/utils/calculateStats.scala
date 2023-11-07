package de.htwg.rs.model.utils

import de.htwg.rs.model.models.*

import scala.collection.immutable.Map
import scala.collection.mutable
import scala.util.{Failure, Success, Try}

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
      if value then
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

def getCountChanges(
    client: ApiClient,
    change_type: ChangeType,
    service: ServiceChange,
    target_type: TargetType,
    country_code: String,
    cursorNextPage: Option[String]
): Try[Int] =
  val changes = client.getChanges(
    change_type,
    service,
    target_type,
    country_code,
    cursorNextPage
  )
  if changes.isSuccess then
    val changesSucess = changes.get
    val changesJson = changes.get.result
    val changesObje = changesJson.arr
    val changesCount = changesObje.size
    val hasMore = changes.get.hasMore
    if hasMore then
      val nextCursor = changes.get.nextCursor
      val changesCountNextPage =
        getCountChanges(
          client,
          change_type,
          service,
          target_type,
          country_code,
          nextCursor
        )
      if changesCountNextPage.isSuccess then
        val changesCountNextPageLeft = changesCountNextPage.get
        Success(changesCount + changesCountNextPageLeft)
      else Failure(new Error("Error getting changes from api"))
    else Success(changesCount)
  else Failure(new Error("Error getting changes from api"))

// this function calls the api for every streaming service and returns the changes as Map
def getCountChangesForEveryService(
    client: ApiClient,
    change_type: ChangeType,
    target_type: TargetType,
    country_code: String
): Try[Map[String, Int]] =
  // iterate over ServiceChange enum
  val serviceChanges = ServiceChange.values
  val serviceChangesCountMap = mutable.Map[String, Int]()
  serviceChanges.foreach((service) =>
    val changesCount =
      getCountChanges(
        client,
        change_type,
        service,
        target_type,
        country_code,
        None
      )
    if changesCount.isSuccess then
      serviceChangesCountMap(service.toString) = changesCount.get
    else serviceChangesCountMap(service.toString + "error!") = 0
  )
  Success(Map.from(serviceChangesCountMap))
