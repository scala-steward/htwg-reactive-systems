package de.htwg.rs.util.dsl.external

import de.htwg.rs.util.dsl.internal.{CriticRating, RatingCategory}

import java.time.LocalDate
import scala.io.Source

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CriticRatingParserSpec extends AnyWordSpec with Matchers:

  "ReviewParser" when {
    "parsing a valid review" should {
      "successfully parse and return a Review object" in {
        val input =
          """ "Movie Title" rated 5 Stars by "John Doe" on "2023-01-15" """
        val result = CriticRatingParser.parse(input)
        result.successful shouldBe true
        result.get.head shouldBe CriticRating(
          "Movie Title",
          5,
          RatingCategory.Stars,
          Some("John Doe"),
          Some(LocalDate.of(2023, 1, 15))
        )
      }
    }

    "parsing multiple valid reviews" should {
      "successfully parse and return a list of Review objects" in {
        val input =
          """ "Movie 1" rated 4 Stars by "User 1" on "2023-01-10" 
             "Movie 2" rated 3 Stars by "User 2" on "2023-01-11" """
        val result = CriticRatingParser.parse(input)
        result.successful shouldBe true
        result.get shouldBe List(
          CriticRating(
            "Movie 1",
            4,
            RatingCategory.Stars,
            Some("User 1"),
            Some(LocalDate.of(2023, 1, 10))
          ),
          CriticRating(
            "Movie 2",
            3,
            RatingCategory.Stars,
            Some("User 2"),
            Some(LocalDate.of(2023, 1, 11))
          )
        )
      }
    }

    "parsing an invalid review" should {
      "fail to parse and return a failure result" in {
        val input =
          """ "Invalid Review rated 5 Stars by "Author" on "2023-01-15" """
        val result = CriticRatingParser.parse(input)
        result.successful shouldBe false
      }
    }

    "parsing an empty input" should {
      "fail to parse and return a failure result" in {
        val input = ""
        val result = CriticRatingParser.parse(input)
        result.successful shouldBe true
        result.get shouldBe List()
      }
    }

    "parsing an incomplete review" should {
      "fail to parse and return a failure result" in {
        val input = """ "Incomplete Review" rated 5 Stars by "Author" """
        val result = CriticRatingParser.parse(input)
        result.successful shouldBe false
      }
    }

    "parsing a big list of reviews" should {
      "successfully parse" in {
        val input = Source.fromResource("reviews.txt").mkString
        val result = CriticRatingParser.parse(input)
        result.successful shouldBe true
        result.get.size shouldBe 1000
        result.get.head.movieName shouldBe "Pulp Fiction"
      }
    }
  }
