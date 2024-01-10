package de.htwg.rs.spark

import de.htwg.rs.spark.*

import scala.collection.mutable

import io.circe.generic.auto.*
import io.circe.parser.*
import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
class SparkSpec extends AnyWordSpec with Matchers:

  "processCriticRating" should {
    "update movieStats and print the current ranking" in {
      val movieStats: mutable.Map[String, (Int, Int)] = mutable.Map.empty

      val criticRating = CriticRating("Movie A", 4, "stars")
      Spark.processCriticRating(criticRating, movieStats)

      val topMovies = movieStats.toSeq.sortBy { case (_, (total, sum)) =>
        if total > 0 then sum.toDouble / total else 0.0
      }.reverse

      assert(topMovies.nonEmpty)
      assert(topMovies.head._1 == "Movie A")
      assert(topMovies.head._2._1 == 1)
      assert(topMovies.head._2._2 == 80)
    }
  }
