package de.htwg.rs.tui.model

case class TabsState(titles: List[String]):
  var index: Int = 0

  def next(): Unit =
    index = (index + 1) % titles.length

  def previous(): Unit =
    index = (index - 1 + titles.length) % titles.length
