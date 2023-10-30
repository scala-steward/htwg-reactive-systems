package de.htwg.rs.controller

import de.htwg.rs.model.models.Country
import de.htwg.rs.model.utils.{
  getPaymentModelsSpreadFromStreamingProvider,
  getSpreadStreamingProvider,
  getStreamingProvider
}

import scala.collection.immutable.Map
import scala.collection.mutable
import scala.reflect.ClassTag
import scala.util.Try

import tui.*
import tui.widgets.ListWidget

case class TabsState(titles: Array[String]):
  var index: Int = 0

  def next(): Unit =
    index = (this.index + 1) % this.titles.length

  def previous(): Unit =
    if this.index > 0 then this.index -= 1
    else this.index = this.titles.length - 1

case class StatefulList[T](
    state: ListWidget.State,
    items: mutable.ArrayDeque[T]
):

  def next(): Unit =
    val i = this.state.selected match
      case Some(i) => if i >= this.items.length - 1 then 0 else i + 1
      case None    => 0
    this.state.select(Some(i))

  def previous(): Unit =
    val i = this.state.selected match
      case Some(i) => if i == 0 then this.items.length - 1 else i - 1
      case None    => 0
    this.state.select(Some(i))

object StatefulList:
  def withItems[T](items: Array[T]): StatefulList[T] =
    StatefulList(
      state = ListWidget.State(),
      items = mutable.ArrayDeque.from(items)
    )

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
      tabs = TabsState(Array("List of Movies", "Stats Streaming Provider")),
      streamingProviderSpread =
        if countries.isSuccess then getSpreadStreamingProvider(countries.get)
        else Map[String, Int]("Error" -> 100),
      streamingProviderPaymentModelSpread = if countries.isSuccess then
        val countriesRight = countries.get
        val streamingProvider = getStreamingProvider(countriesRight)
        getPaymentModelsSpreadFromStreamingProvider(streamingProvider)
      else Map[String, Int]("Error" -> 100)
    )
