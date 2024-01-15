package de.htwg.rs.tui.model.utils

import de.htwg.rs.tui.model.apiclient.*

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

/** Computes a map of payment models and the percentage of streaming providers
  * that support it
  */
def calculatePaymentModelSupportPercentage(
    streamingProviders: List[StreamingProvider]
): Map[String, Int] =
  val totalProviders = streamingProviders.length
  streamingProviders
    .flatMap(_.supportedStreamingTypes)
    .collect { case (paymentModel, isSupported) if isSupported => paymentModel }
    .groupMapReduce(identity)(_ => 1)(_ + _)
    .map { case (paymentModel, availableProviders) =>
      paymentModel -> (availableProviders * 100) / totalProviders
    }

/** Computes a map of streaming types and the percentage of streaming providers
  * that support it
  */
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

/** Computes a map of streaming providers and the number of changes for a
  * specific change type
  */
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
