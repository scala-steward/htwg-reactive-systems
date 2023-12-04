package de.htwg.rs.streams

import java.time.Duration
import java.util.Properties
import scala.jdk.CollectionConverters.*

import org.apache.kafka.clients.consumer.KafkaConsumer

object Consumer:
  def main(args: Array[String]): Unit =
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("group.id", "test")
    props.put(
      "key.deserializer",
      "org.apache.kafka.common.serialization.StringDeserializer"
    )
    props.put(
      "value.deserializer",
      "org.apache.kafka.common.serialization.StringDeserializer"
    )

    val consumer = KafkaConsumer[String, String](props)
    consumer.subscribe(java.util.Collections.singletonList(KafkaTopic))

    while true do
      val records = consumer.poll(Duration.ofMillis(100))
      for record <- records.asScala do println(s"Received message: ${record}")
