package de.htwg.rs.tui.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class StatefulListSpec extends AnyWordSpec with Matchers:
  "A StatefulList" when {
    "created with items" should {
      "initialize with no item selected" in {
        val items = Array("Item1", "Item2", "Item3")
        val statefulList = StatefulList.withItems(items)

        statefulList.state.selected shouldBe None
      }

      "have the correct number of items" in {
        val items = Array("Item1", "Item2", "Item3")
        val statefulList = StatefulList.withItems(items)

        statefulList.items should have size 3
      }
    }

    "navigated with next" should {
      "select the first item" in {
        val items = Array("Item1", "Item2", "Item3")
        val statefulList = StatefulList.withItems(items)

        statefulList.next() // Move to the first item

        statefulList.state.selected shouldBe Some(0)
      }

      "select the next item" in {
        val items = Array("Item1", "Item2", "Item3")
        val statefulList = StatefulList.withItems(items)

        statefulList.next() // Move to the first item
        statefulList.next() // Move to the second item

        statefulList.state.selected shouldBe Some(1)
      }

      "wrap around to the first item when at the end" in {
        val items = Array("Item1", "Item2", "Item3")
        val statefulList = StatefulList.withItems(items)

        statefulList.next() // Move to the first item
        statefulList.next() // Move to the second item
        statefulList.next() // Move to the third item
        statefulList.next() // Wrap around to the first item

        statefulList.state.selected shouldBe Some(0)
      }
    }

    "navigated with previous" should {
      "select the previous item" in {
        val items = Array("Item1", "Item2", "Item3")
        val statefulList = StatefulList.withItems(items)

        statefulList.next() // Move to the first item
        statefulList.next() // Move to the second item
        statefulList.previous() // Move back to the first item

        statefulList.state.selected shouldBe Some(0)
      }

      "wrap around to the last item when at the beginning" in {
        val items = Array("Item1", "Item2", "Item3")
        val statefulList = StatefulList.withItems(items)

        statefulList.previous() // Move to the first item
        statefulList.previous() // Wrap around to the last item

        statefulList.state.selected shouldBe Some(2)
      }
    }
  }
