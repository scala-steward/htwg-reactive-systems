package de.htwg.rs.streams
import java.util.Properties
import org.apache.kafka.clients.consumer.KafkaConsumer
import scala.collection.JavaConverters._

object Consumer extends App{
    def main(args: Array[String]): Unit = {
        val props = new Properties()
        props.put("bootstrap.servers", "localhost:9092")
        props.put("group.id", "test")
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        
        val consumer = KafkaConsumer[String, String](props)
        val topic = "test"
        consumer.subscribe(java.util.Collections.singletonList(topic))
        
        while (true) {
            println("Polling")
            val records = consumer.poll(100)
            for (record <- records.asScala) {
            println(s"Received message: ${record}")
            }
        }
    }
  
}
