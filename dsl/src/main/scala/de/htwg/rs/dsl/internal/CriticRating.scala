package de.htwg.rs.dsl.internal

import java.time.LocalDate

import io.circe.*
import io.circe.generic.semiauto.*

/** Movie represents a movie. */
case class Movie(title: String):
  /** Returns a CriticRating with the given rating. */
  def rated(rating: Int, category: RatingCategory): CriticRating =
    CriticRating(title, rating, category)

/** CriticRating represents a rating of a movie by a critic. */
case class CriticRating(
    movieName: String,
    rating: Int,
    category: RatingCategory,
    critic: Option[String] = None,
    date: Option[LocalDate] = None
):
  /** Returns a copy of this CriticRating with the given critic. */
  def by(critic: String): CriticRating = copy(critic = Some(critic))

  /** Returns a copy of this CriticRating with the given date. */
  def on(date: String): CriticRating =
    val localDate = LocalDate.parse(date)
    copy(date = Some(localDate))

  def asJson: String =
    s"""{
       |  "movieName": "$movieName",
       |  "rating": $rating,
       |  "category": "${category.toString.toLowerCase}", 
       |  "critic": "${critic.getOrElse("")}",
       |  "date": "${date.getOrElse("")}"
       |}""".stripMargin

enum RatingCategory:
  case Stars, %

extension (movieTitle: String)
  /** Returns a CriticRating with the given rating. */
  def rated(
      rating: Int,
      category: RatingCategory = RatingCategory.Stars
  ): CriticRating =
    Movie(movieTitle).rated(rating, category)
