package de.htwg.rs.spark

import de.htwg.rs.spark.* 
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import io.circe.parser._
import io.circe.generic.auto._
import scala.collection.mutable
import org.scalatest._
import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SparkSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach with BeforeAndAfterAll with EmbeddedKafka {

  var spark: SparkSession = _
  var streamingContext: StreamingContext = _

  override def beforeAll(): Unit = {
    EmbeddedKafka.start()
  }

  override def afterAll(): Unit = {
    EmbeddedKafka.stop()
  }

  override def beforeEach(): Unit = {
    // Initialisieren Sie SparkSession und StreamingContext hier
    spark = SparkSession.builder
      .appName("Test Application")
      .config("spark.master", "local")
      .getOrCreate()

    streamingContext = new StreamingContext(spark.sparkContext, Seconds(1))
  }

"processCriticRating" should "update movieStats and print the current ranking" in {
    val movieStats: mutable.Map[String, (Int, Int)] = mutable.Map.empty

    val criticRating = CriticRating("Movie A", 4, "stars")
    Spark.processCriticRating(criticRating, movieStats)

    val topMovies = movieStats.toSeq
      .sortBy { case (_, (total, sum)) => if (total > 0) sum.toDouble / total else 0.0 }
      .reverse
    println(s"Top Movies: $topMovies")
    assert(topMovies.nonEmpty)
    assert(topMovies.head._1 == "Movie A")
    assert(topMovies.head._2._1 == 1)
    assert(topMovies.head._2._2 == 80)
  }
}