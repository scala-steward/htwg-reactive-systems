package de.htwg.rs.model

import scala.collection.mutable

import tui.widgets.ListWidget

/** A list of items with a state to keep track of the selected item. */
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
