package de.htwg.rs.util.actors

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

final case class MovieReview(name: String, rating: Int)

object StatsAnalyzerActor:
  def apply(): Behavior[MovieReview] =
    Behaviors.setup { context =>
      var totalRating = 0
      var totalCount = 0

      def currentAverage: Double =
        BigDecimal(totalRating.toDouble / totalCount)
          .setScale(2, BigDecimal.RoundingMode.HALF_UP)
          .toDouble

      Behaviors.receiveMessage { case MovieReview(name, rating) =>
        totalRating += rating
        totalCount += 1

        println(
          s"Received MovieReview for $name with rating $rating. Average rating is $currentAverage"
        )

        Behaviors.same
      }
    }

@main def main(): Unit =
  println("Starting Actor System")
  val system = ActorSystem(StatsAnalyzerActor(), "StatsAnalyzerActor")

  println("Sending Movie Reviews")
  (1 to 100).foreach { _ =>
    val rating = scala.util.Random.nextInt(5) + 1
    system ! MovieReview("The Shawshank Redemption", rating)
  }
