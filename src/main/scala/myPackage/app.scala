package myPackage
import tui._
import tui.widgets.ListWidget

import scala.collection.mutable
import scala.reflect.ClassTag
import scala.util.Random
import myPackage.models.Country



case class TabsState(titles: Array[String]) {
  var index: Int = 0
  def next(): Unit =
    index = (this.index + 1) % this.titles.length

  def previous(): Unit =
    if (this.index > 0) {
      this.index -= 1
    } else {
      this.index = this.titles.length - 1
    }
}

case class StatefulList[T](
    state: ListWidget.State,
    items: mutable.ArrayDeque[T]
) {

  def next(): Unit = {
    val i = this.state.selected match {
      case Some(i) => if (i >= this.items.length - 1) 0 else i + 1
      case None    => 0
    }
    this.state.select(Some(i))
  }

  def previous(): Unit = {
    val i = this.state.selected match {
      case Some(i) => if (i == 0) this.items.length - 1 else i - 1
      case None    => 0
    }
    this.state.select(Some(i))
  }
}

object StatefulList {
  def with_items[T](items: Array[T]): StatefulList[T] =
    StatefulList(state = ListWidget.State(), items = mutable.ArrayDeque.from(items))
}



case class App(
    title: String,
    var should_quit: Boolean,
    tabs: TabsState,
    var show_chart: Boolean,
    countries: Either[Array[Country],String],
    enhanced_graphics: Boolean
) {

/*   def on_up(): Unit =
    this.tasks.previous()

  def on_down(): Unit =
    this.tasks.next() */

  def on_right(): Unit =
    this.tabs.next()

  def on_left(): Unit =
    this.tabs.previous()

  def on_key(c: Char): Unit =
    c match {
      case 'q' => this.should_quit = true
      case 't' => this.show_chart = !this.show_chart
      case _   => ()
    }

}

object App {
  def apply(title: String, enhanced_graphics: Boolean,countries: Either[Array[Country],String]): App = {
    new App(
      title = title,
      countries = countries,
      should_quit = false,
      tabs = TabsState(Array("List of Movies", "Stats")),
      show_chart = true,
      enhanced_graphics = enhanced_graphics
    )
  }


}
