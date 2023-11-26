package de.htwg.rs.tui.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TabsStateSpec extends AnyWordSpec with Matchers:
  "TabsState" should {
    "initialize with the correct index" in {
      val tabsState = TabsState(List("Tab 1", "Tab 2", "Tab 3"))
      tabsState.index should be(0)
    }

    "increment index correctly with next()" in {
      val tabsState = TabsState(List("Tab 1", "Tab 2", "Tab 3"))
      tabsState.next()
      tabsState.index should be(1)
    }

    "wrap around when reaching the end with next()" in {
      val tabsState = TabsState(List("Tab 1", "Tab 2", "Tab 3"))
      tabsState.next() // index 1
      tabsState.next() // index 2
      tabsState.next() // index 0 (wrap around)
      tabsState.index should be(0)
    }

    "decrement index correctly with previous()" in {
      val tabsState = TabsState(List("Tab 1", "Tab 2", "Tab 3"))
      tabsState.index = 2
      tabsState.previous()
      tabsState.index should be(1)
    }

    "wrap around when reaching the beginning with previous()" in {
      val tabsState = TabsState(List("Tab 1", "Tab 2", "Tab 3"))
      tabsState.index = 0
      tabsState.previous() // index 2 (wrap around)
      tabsState.index should be(2)
    }
  }
