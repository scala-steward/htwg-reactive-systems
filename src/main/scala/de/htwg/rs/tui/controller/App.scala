package de.htwg.rs.tui.controller

import de.htwg.rs.tui.model.TabsState
import de.htwg.rs.tui.model.apiclient.Country
import de.htwg.rs.tui.model.utils.{
  calculatePaymentModelSupportPercentage,
  getSpreadStreamingProvider,
  getStreamingProvider
}

import scala.collection.immutable.Map
import scala.util.Try

import tui.*
import tui.widgets.ListWidget

case class App(
    title: String,
    var shouldQuit: Boolean,
    tabs: TabsState,
    countries: Try[List[Country]],
    streamingProviderSpread: Map[String, Int],
    streamingProviderPaymentModelSpread: Map[String, Int],
    countNewChangesProvider: Map[String, Int],
    countRemovedChangesProvider: Map[String, Int]
):

  /** Switch to the next tab. */
  def onRight(): Unit =
    this.tabs.next()

  /** Switch to the previous tab. */
  def onLeft(): Unit =
    this.tabs.previous()

  /** React to a key press. */
  def onKey(c: Char): Unit =
    c match
      case 'q' => this.shouldQuit = true
      case _   => ()

object App:
  def apply(
      title: String,
      countries: Try[List[Country]],
      countChangesProvider: Try[Map[String, Int]],
      countRemovedChangesProvider: Try[Map[String, Int]]
  ): App =
    new App(
      title = title,
      countries = countries,
      shouldQuit = false,
      tabs = TabsState(
        List("List of Movies", "Stats Streaming Provider", "Stats Changes")
      ),
      streamingProviderSpread = countries
        .map(getSpreadStreamingProvider)
        .getOrElse(Map[String, Int]("Error" -> 100)),
      streamingProviderPaymentModelSpread = countries
        .map(getStreamingProvider)
        .map(calculatePaymentModelSupportPercentage)
        .getOrElse(Map[String, Int]("Error" -> 100)),
      countNewChangesProvider = countChangesProvider
        .getOrElse(Map[String, Int]("Error" -> 100)),
      countRemovedChangesProvider = countRemovedChangesProvider
        .getOrElse(Map[String, Int]("Error" -> 100))
    )
