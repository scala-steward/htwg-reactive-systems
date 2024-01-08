package de.htwg.rs.streams

import de.htwg.rs.dsl.external.{CriticRatingGenerator, CriticRatingParser}
import de.htwg.rs.dsl.internal.CriticRating

import java.util.Properties
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random
import concurrent.duration.DurationInt
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, Source}
import org.apache.kafka.clients.producer.{
  KafkaProducer,
  ProducerConfig,
  ProducerRecord
}
import scala.util.Success
import scala.util.Failure
import scala.concurrent.Await

val KafkaTopic = "critic-ratings"

def initKafkaProducer(): KafkaProducer[String, String] =
  val props = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put(
    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringSerializer"
  )
  props.put(
    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringSerializer"
  )
  KafkaProducer[String, String](props)

object ReviewStream:
  private type ParseResult = CriticRatingParser.ParseResult[List[CriticRating]]
  private val NumberOfReviews = 1_000

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

  private def publishSink(
      producer: KafkaProducer[String, String]
  ): Sink[CriticRating, Future[akka.Done]] =
    Sink.foreach(event =>
      val result = producer.send(new ProducerRecord(KafkaTopic, "key", event.toString))
    )

  def main(args: Array[String]): Unit =
    implicit val reviewSystem: ActorSystem = ActorSystem("ReviewSystem")
    implicit val executionContext: ExecutionContext = reviewSystem.dispatcher
    implicit val random: Random = Random()

    val producer = initKafkaProducer()
    val streamResult =generateReviewSource
      .take(NumberOfReviews)
      .via(parsingFlow)
      .via(successFlatMap)
      .runWith(publishSink(producer))
    // wait for stream to complete or 10 seconds
    val result = Await.result(streamResult, 50.seconds)


    println(s"Stream completed with result: $result")
    
