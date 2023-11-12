package de.htwg.rs.util.dsl

import de.htwg.rs.util.dsl.*

import java.time.LocalDate

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CriticRatingSpec extends AnyWordSpec with Matchers:

  "CriticRating" should {

    "allow creating a rating with a critic and date" in {
      val rating = "The Seventh Seal" rated 5 by "Roger Ebert" on "2000-04-16"
      rating.movieName should be("The Seventh Seal")
      rating.rating should be(5)
      rating.critic should contain("Roger Ebert")
      rating.date should contain(LocalDate.of(2000, 4, 16))
    }

    "allow creating a rating with a critic" in {
      val rating = "The Seventh Seal" rated 5 by "Roger Ebert"
      rating.movieName should be("The Seventh Seal")
      rating.rating should be(5)
      rating.critic should contain("Roger Ebert")
      rating.date should be(empty)
    }

    "allow creating a rating with a date" in {
      val rating = "The Seventh Seal" rated 5 on "2000-04-16"
      rating.movieName should be("The Seventh Seal")
      rating.rating should be(5)
      rating.critic should be(empty)
      rating.date should contain(LocalDate.of(2000, 4, 16))
    }

    "allow creating a rating without a critic or date" in {
      val rating = "The Seventh Seal" rated 5
      rating.movieName should be("The Seventh Seal")
      rating.rating should be(5)
      rating.critic should be(empty)
      rating.date should be(empty)
    }
  }
