package controllers

import javax.inject._
import akka.actor.ActorSystem
import akka.kafka.scaladsl._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.stream.scaladsl.StreamConverters
import akka.util.ByteString
import java.io.FileInputStream
import scala.util.{Try, Success, Failure}

import akka.kafka.{ProducerMessage, ProducerSettings, Subscriptions}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}
 //import scala.concurrent.duration._
 //import services.KafkaStreamer

 import akka.kafka.ConsumerSettings

import java.io._
import play.api.mvc._
import play.api.http.Writeable
import play.api.libs.json._

/**
 * This controller creates an `Action` to stream kafka messages
 */
@Singleton
class KafkaStreamController @Inject()(cc: ControllerComponents) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {
  implicit val actorSystem = ActorSystem("Votes")
  implicit val materializer = ActorMaterializer()
  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def candidateStream = Action {
    val candidateVotes = scala.collection.mutable.Map[String, Int]()
    val source = Consumer.committableSource(kafkaConsumerSettings, Subscriptions.topics("votes"))

    Ok.chunked(source.map { msg =>
        val value = Try(Json.parse(msg.record.value)) match {
            case Success(json) => {
                val name = json \ "candidateName" 
                val voteCount = (json \ "votes").as[Int]
                val candidateName = name match {
                    case JsDefined(v) => v.toString.replaceAll("\"", "")
                    case undefined: JsUndefined => "Not a valid vote\n".replaceAll("\"", "")
                }
                candidateVotes.put(candidateName, voteCount)
                candidateName + " \t\t\t " + voteCount + " \t votes \n"
            }
            case Failure(e) => {
                "Not a valid vote\n".replaceAll("\"", "")
            }
        }
        value
    }.limit(100))
    
  }
  
  def kafkaConsumerSettings: ConsumerSettings[Array[Byte], String] = 
   ConsumerSettings(actorSystem, new ByteArrayDeserializer, new StringDeserializer)
     .withBootstrapServers("localhost:9092")
     .withGroupId("mygroup")
     .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

}
