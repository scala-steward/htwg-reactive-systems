package de.htwg.rs.spark

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.TaskContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka010.*
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import io.circe.parser._
import io.circe.generic.auto._

import scala.collection.mutable

// Mutable Map zur Aufbewahrung der Bewertungsstatistiken für jeden Film
val movieStats: mutable.Map[String, (Int, Int)] = mutable.Map.empty
case class CriticRating(
    movieName: String,
    rating: Int,
    category: String,
    critic: Option[String] = None,
    date: Option[String] = None
)
  

object Spark:
  def main(args: Array[String]): Unit =
    println("Spark!!")
    val spark = SparkSession.builder
      .appName("Simple Application")
      .config("spark.master", "local")
      .getOrCreate()
    // set log level to error
    spark.sparkContext.setLogLevel("ERROR")
    val testFile =
      spark.read.textFile("/home/tim/Downloads/logiJoy.config.yml").cache()
    val numA = testFile.filter(line => line.contains("a")).count()
    println(s"lines with a: $numA")
    println("ended")

    val streamingContext = new StreamingContext(spark.sparkContext, Seconds(1))
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "11",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array("critic-ratings")
    val stream = KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )
    stream.foreachRDD { rdd =>
      rdd.foreach { record =>
        val value = record.value()
        val result = decode[CriticRating](value)

        result match {
          case Right(criticRating) =>
            processCriticRating(criticRating)
          case Left(error) =>
            println(s"Failed to parse JSON: $error")
        }
      }
    }

    streamingContext.start()
    streamingContext.awaitTermination()

    spark.stop()
    println("Spark stopped")


  def processCriticRating(criticRating: CriticRating,movieStats: mutable.Map[String, (Int, Int)]=movieStats): Unit = {
    // Extrahiere Informationen aus der Bewertung
    val movieName = criticRating.movieName
    
    val rating = 
    if (criticRating.category == "stars") criticRating.rating * 20
    else criticRating.rating


    // Aktualisiere die Statistiken für den Film
    val (totalRatings, totalRatingSum) = movieStats.getOrElse(movieName, (0, 0))
    val newTotalRatings = totalRatings + 1
    val newTotalRatingSum = totalRatingSum + rating
    movieStats.update(movieName, (newTotalRatings, newTotalRatingSum))
    // Erstelle ein Ranking der besten Filme basierend auf durchschnittlichen Bewertungen
    val topMovies = movieStats.toSeq
      .sortBy { case (_, (total, sum)) => if (total > 0) sum.toDouble / total else 0.0 }
      .reverse
    
    // Drucke das aktuelle Ranking der besten Filme
    println("Current Ranking of Top Movies:")
    topMovies.foreach { case (movie, (total, sum)) =>
      val averageRating = if (total > 0) sum.toDouble / total else 0.0
      println(s"Movie: $movie, Total Ratings: $total, Average Rating: $averageRating %")
    } 
  }    
