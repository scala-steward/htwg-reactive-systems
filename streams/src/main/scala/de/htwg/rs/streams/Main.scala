package de.htwg.rs.streams

import de.htwg.rs.dsl.external.CriticRatingParser
import de.htwg.rs.dsl.internal.CriticRating

import scala.util.Random

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, Source}

object ReviewSource:
  private val movies = List(
    "The Dark Knight",
    "Forrest Gump",
    "The Shawshank Redemption",
    "The Godfather"
  )
  private val names = List(
    "Jane Young",
    "Jane Hayes",
    "Travis King",
    "Patricia Arnold",
    "Travis Davis",
    "Sam Hernandez"
  )

  private def randomRating(): String =
    if Random.nextBoolean() then s"${Random.nextInt(5) + 1} Stars"
    else s"${Random.nextInt(101)}%"

  private def randomMovieReview(): String =
    val movie = movies(Random.nextInt(movies.size))
    val name = names(Random.nextInt(names.size))
    val rating = randomRating()
    val date =
      if Random.nextBoolean() then s"on \"${randomDate()}\"" else ""
    s""""$movie" rated $rating by "$name" $date"""

  private def randomDate(): String =
    val year = 2022 + Random.nextInt(2)
    val month = f"${1 + Random.nextInt(12)}%02d"
    val day = f"${1 + Random.nextInt(28)}%02d"
    s"$year-$month-$day"

  def apply(): Source[String, ?] =
    Source
      .repeat(1)
      .map(_ => randomMovieReview())

object ParsingFlow:
  def apply()
      : Flow[String, CriticRatingParser.ParseResult[List[CriticRating]], ?] =
    Flow[String]
      .map(x => CriticRatingParser.parse(x))

object PrintSink:
  def apply(): Sink[CriticRatingParser.ParseResult[List[CriticRating]], ?] =
    Sink.foreach(elem =>
      if elem.successful then println(s"Successfully parsed: ${elem.get.head}")
      else println(s"Failed to parse: ${elem}")
    )

object Main extends App:
  implicit val system: ActorSystem = ActorSystem("ReviewSystem")

  ReviewSource()
    .via(ParsingFlow())
    .runWith(PrintSink())
