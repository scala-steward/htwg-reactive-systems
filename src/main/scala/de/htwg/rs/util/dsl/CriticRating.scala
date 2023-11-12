package de.htwg.rs.util.dsl

import java.time.LocalDate

/** CriticRating represents a rating of a movie by a critic. */
case class CriticRating(
    movieName: String,
    rating: Int,
    critic: Option[String] = None,
    date: Option[LocalDate] = None
):
  /** Returns a copy of this CriticRating with the given critic. */
  def by(critic: String): CriticRating = copy(critic = Some(critic))

  /** Returns a copy of this CriticRating with the given date. */
  def on(date: String): CriticRating =
    val localDate = LocalDate.parse(date)
    copy(date = Some(localDate))

extension (movieName: String)
  /** Returns a CriticRating with the given rating. */
  def rated(rating: Int): CriticRating = CriticRating(movieName, rating)
