package de.htwg.rs.controller

import de.htwg.rs.model.TabsState
import de.htwg.rs.model.models.Country
import de.htwg.rs.model.utils.{
  getPaymentModelsSpreadFromStreamingProvider,
  getSpreadStreamingProvider,
  getStreamingProvider
}

import scala.collection.immutable.Map
import scala.collection.mutable
import scala.util.Try

import tui.*
import tui.widgets.ListWidget

case class App(
    title: String,
    var shouldQuit: Boolean,
    tabs: TabsState,
    countries: Try[List[Country]],
    streamingProviderSpread: Map[String, Int],
    streamingProviderPaymentModelSpread: Map[String, Int]
):

  /*   def onUp(): Unit =
    this.countries.previous()

  def onDown(): Unit =
    this.countries.next() */

  def onRight(): Unit =
    this.tabs.next()

  def onLeft(): Unit =
    this.tabs.previous()

  def onKey(c: Char): Unit =
    c match
      case 'q' => this.shouldQuit = true
      case _   => ()

object App:
  def apply(
      title: String,
      countries: Try[List[Country]]
  ): App =
    new App(
      title = title,
      countries = countries,
      shouldQuit = false,
      tabs = TabsState(List("List of Movies", "Stats Streaming Provider")),
      streamingProviderSpread =
        if countries.isSuccess then getSpreadStreamingProvider(countries.get)
        else Map[String, Int]("Error" -> 100),
      streamingProviderPaymentModelSpread = if countries.isSuccess then
        val countriesRight = countries.get
        val streamingProvider = getStreamingProvider(countriesRight)
        getPaymentModelsSpreadFromStreamingProvider(streamingProvider)
      else Map[String, Int]("Error" -> 100)
    )
