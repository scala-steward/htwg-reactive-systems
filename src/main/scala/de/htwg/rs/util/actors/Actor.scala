package de.htwg.rs.util.actors

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

final case class MovieReview(name: String, rating: Int)

object StatsAnalyzerActor:
  final private case class MovieState(totalRating: Int, totalCount: Int)

  private object MovieState:
    val empty: MovieState = MovieState(0, 0)

  def apply(): Behavior[MovieReview] =
    Behaviors.setup { context =>
      var state: Map[String, MovieState] = Map.empty

      def currentAverage(movieName: String): Double =
        state.get(movieName) match
          case Some(MovieState(totalRating, totalCount)) if totalCount > 0 =>
            totalRating.toDouble / totalCount
          case _ => 0.0

      Behaviors.receiveMessage { case MovieReview(name, rating) =>
        val movieState = state.getOrElse(name, MovieState.empty)
        state = state.updated(
          name,
          movieState.copy(
            totalRating = movieState.totalRating + rating,
            totalCount = movieState.totalCount + 1
          )
        )

        println(
          s"Received MovieReview for $name with rating $rating. Average rating is ${currentAverage(name)}"
        )

        Behaviors.same
      }
    }

@main def main(): Unit =
  val system = ActorSystem(StatsAnalyzerActor(), "StatsAnalyzerActor")
  val movieNames = List(
    "The Shawshank Redemption",
    "The Godfather",
    "The Dark Knight",
    "Star Wars"
  )

  (1 to 100_000).foreach { _ =>
    val rating = scala.util.Random.nextInt(5) + 1
    val movieName = movieNames(scala.util.Random.nextInt(movieNames.size))
    system ! MovieReview(movieName, rating)
  }

  system.terminate()
