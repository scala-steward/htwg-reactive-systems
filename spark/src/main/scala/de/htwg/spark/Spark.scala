package de.htwg.rs.spark

import org.apache.spark.sql.SparkSession

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.TaskContext

object Spark:
  def main(args: Array[String]): Unit =
    println("Spark!!")
    val spark = SparkSession.builder.appName("Simple Application").config("spark.master", "local").getOrCreate()
    val testFile= spark.read.textFile("/home/tim/Downloads/logiJoy.config.yml").cache()
    val numA= testFile.filter(line => line.contains("a")).count()
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
    println("oooooooooooooooooooo")
    stream.foreachRDD { rdd =>
      val messages = rdd.map(_.value)
      messages.foreach(println)
    }
    println("oooooooooooooooooooo")
   
    streamingContext.start()
    streamingContext.awaitTermination()





      
    spark.stop()



