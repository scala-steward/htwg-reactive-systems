package de.htwg.rs.dsl.external

import scala.util.Random

import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CriticRatingGeneratorSpec
    extends AnyWordSpec
    with Matchers
    with BeforeAndAfterEach:

  implicit var random: Random = _

  override def beforeEach(): Unit =
    random = new Random(42)

  "CriticRatingGenerator" should {
    "generate a valid rating with movie title, critic name, and date" in {
      val generatedRating = CriticRatingGenerator.generate
      generatedRating should be(
        """"The Shawshank Redemption" rated 4 Stars by "John Miller" on "2004-06-11""""
      )
    }

    "generate different ratings for different invocations" in {
      val generatedRating1 = CriticRatingGenerator.generate
      val generatedRating2 = CriticRatingGenerator.generate
      generatedRating1 should not be generatedRating2
    }

    "generate many different ratings for many invocations" in {
      val generatedRatings = (1 to 100).map(_ => CriticRatingGenerator.generate)
      generatedRatings.distinct.size should be(generatedRatings.size)
    }
  }
