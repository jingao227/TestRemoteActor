package Remote

import java.io.File

import akka.actor.{ActorRef, Props, ActorSystem, Actor}
import Message.Message
import com.typesafe.config.ConfigFactory

import scala.collection.mutable.ListBuffer

/**
  * Created by Jing Ao on 2016/3/12.
  */
class RemoteActor extends Actor {
  override def receive: Receive = {
    case msg: ListBuffer[String] => {
//      println("remote received " + msg + " from " + sender)
      println(msg)
      sender ! "CONFIRM"
    }
    case _ => println("Received unknown msg ")
  }
}

object RemoteActor{
  def main(args: Array[String]) {
    //get the configuration file from classpath
    val configFile = getClass.getClassLoader.getResource("remote_application.conf").getFile
    //parse the config
    //val config = ConfigFactory.parseFile(new File(configFile))
    val config = ConfigFactory.parseString("""
      akka {
        loglevel = "INFO"
        actor {
          provider = "akka.remote.RemoteActorRefProvider"
          deployment {
            /remote {
              mailbox = prio-mailbox
            }
          }
        }
        remote {
          enabled-transports = ["akka.remote.netty.tcp"]
          netty.tcp {
            hostname = "127.0.0.1"
            port = 5150
          }
          log-sent-messages = on
          log-received-messages = on
        }
      }
      prio-mailbox {
        mailbox-type = "Message.PriorityMailbox"
      }""")
    //create an actor system with that config
    val system = ActorSystem("RemoteSystem" , ConfigFactory.load(config))
    //create a remote actor from actorSystem
    val remote = system.actorOf(Props[RemoteActor], name="remote")
    println("remote is ready")


  }
}
//class RemoteActor extends Actor {
//  def receive = {
//    case msg: String =>
//      println(s"RemoteActor received message '$msg'")
//      sender ! "Hello from the RemoteActor"
//    case _ =>
//  }
//}