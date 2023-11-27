package de.htwg.rs.streams

import de.htwg.rs.dsl.external.{CriticRatingGenerator, CriticRatingParser}
import de.htwg.rs.dsl.internal.CriticRating

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, Source}

object ReviewStream:
  private type ParseResult = CriticRatingParser.ParseResult[List[CriticRating]]
  private val NumberOfReviews = 100_000

  private def generateReviewSource(using random: Random): Source[String, ?] =
    Source
      .repeat(1)
      .map(_ => CriticRatingGenerator.generate)

  private val parsingFlow: Flow[String, ParseResult, ?] =
    Flow[String].map(CriticRatingParser.parse)

  private val successFlatMap: Flow[ParseResult, CriticRating, ?] =
    Flow[ParseResult]
      .filter(_.successful)
      .flatMapConcat(elem => Source(elem.get))

  private val printSink: Sink[CriticRating, Future[akka.Done]] =
    Sink.foreach(println)

  def main(args: Array[String]): Unit =
    implicit val reviewSystem: ActorSystem = ActorSystem("ReviewSystem")
    implicit val executionContext: ExecutionContext = reviewSystem.dispatcher
    implicit val random: Random = Random()

    generateReviewSource
      .take(NumberOfReviews)
      .via(parsingFlow)
      .via(successFlatMap)
      .runWith(printSink)
      .onComplete(_ => reviewSystem.terminate())
