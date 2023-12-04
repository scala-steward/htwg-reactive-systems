package de.htwg.rs.streams

import java.util.Properties

import org.apache.kafka.clients.producer.*

object Producer extends App:
  def main(args: Array[String]): Unit =
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put(
      "key.serializer",
      "org.apache.kafka.common.serialization.StringSerializer"
    )
    props.put(
      "value.serializer",
      "org.apache.kafka.common.serialization.StringSerializer"
    )

    val producer = KafkaProducer[String, String](props)
    val topic = "test"
    val msg = "Hello World!"

    for i <- 1 to 100 do
      val record = new ProducerRecord(topic, "key", s"hello $i")
      Thread.sleep(1000)
      println(s"Sending $i")
      producer.send(record)
    val record =
      new ProducerRecord(topic, "key", "the end" + new java.util.Date)
    println("Sending the end message")
    producer.send(record)
    producer.close()
