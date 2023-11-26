package de.htwg.rs.dsl.internal

import de.htwg.rs.dsl.*
import de.htwg.rs.dsl.internal.RatingCategory.{%, Stars}

import java.time.LocalDate

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CriticRatingSpec extends AnyWordSpec with Matchers:

  "Movie" should {

    "allow creating a movie with a title" in {
      val movie = Movie("The Seventh Seal")
      movie.title should be("The Seventh Seal")
    }
  }

  "CriticRating" should {

    "allow creating a star-rating with a critic and date" in {
      val rating =
        "The Seventh Seal" rated (5, Stars) by "Roger Ebert" on "2000-04-16"

      rating.movieName should be("The Seventh Seal")
      rating.rating should be(5)
      rating.category should be(Stars)
      rating.critic should contain("Roger Ebert")
      rating.date should contain(LocalDate.of(2000, 4, 16))
    }

    "allow creating a percentage-rating with a critic" in {
      val rating = "The Seventh Seal" rated (80, %) by "Roger Ebert"

      rating.movieName should be("The Seventh Seal")
      rating.rating should be(80)
      rating.category should be(%)
      rating.critic should contain("Roger Ebert")
      rating.date should be(empty)
    }

    "allow default-creating a rating with a date" in {
      val rating = "The Seventh Seal" rated 5 on "2000-04-16"

      rating.movieName should be("The Seventh Seal")
      rating.rating should be(5)
      rating.category should be(Stars)
      rating.critic should be(empty)
      rating.date should contain(LocalDate.of(2000, 4, 16))
    }

    "allow creating a rating without a critic or date" in {
      val rating = "The Seventh Seal" rated 5
      rating.movieName should be("The Seventh Seal")
      rating.rating should be(5)
      rating.category should be(Stars)
      rating.critic should be(empty)
      rating.date should be(empty)
    }
  }
