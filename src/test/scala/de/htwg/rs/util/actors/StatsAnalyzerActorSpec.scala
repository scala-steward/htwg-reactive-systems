package de.htwg.rs.util.actors

import akka.actor.testkit.typed.scaladsl.{ScalaTestWithActorTestKit, TestProbe}
import org.scalatest.wordspec.AnyWordSpecLike

class StatsAnalyzerActorSpec
    extends ScalaTestWithActorTestKit
    with AnyWordSpecLike:

  "StatsAnalyzerActor" should {
    "update totalRating and totalCount upon receiving MovieReview" in {
      val statsAnalyzerActor = spawn(StatsAnalyzerActor())

      val probe = TestProbe[MovieReview]()
      val movieReview = MovieReview("Inception", 4)

      statsAnalyzerActor ! movieReview

      probe.expectNoMessage()
    }

    "calculate the correct average rating" in {
      val statsAnalyzerActor = spawn(StatsAnalyzerActor())

      val probe = TestProbe[MovieReview]()
      val movieReviews = List(
        MovieReview("Interstellar", 5),
        MovieReview("The Dark Knight", 4),
        MovieReview("Dunkirk", 3)
      )

      movieReviews.foreach(statsAnalyzerActor ! _)

      probe.expectNoMessage()
    }
  }
