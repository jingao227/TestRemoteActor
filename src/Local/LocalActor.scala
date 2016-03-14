package Local

import java.io.File

import akka.actor._
import com.typesafe.config.ConfigFactory

class LocalActor extends Actor{
  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    /*
      Connect to remote actor. The following are the different parts of actor path

      akka.tcp : enabled-transports  of remote_application.conf

      RemoteSystem : name of the actor system used to create remote actor

      127.0.0.1:5150 : host and port

      user : The actor is user defined

      remote : name of the actor, passed as parameter to system.actorOf call

     */
    val remoteActor = context.actorSelection("akka.tcp://RemoteSystem@127.0.0.1:5150/user/remote")
    println("That 's remote:" + remoteActor)
    remoteActor ! Message.Message.content
  }
  override def receive: Receive = {

    case msg:String => {
      println("got message from remote " + msg)
    }
  }
}



object LocalActor {

  def main(args: Array[String]) {

    val configFile = getClass.getClassLoader.getResource("local_application.conf").getFile
//    val config = ConfigFactory.parseFile(new File(configFile))
    val config = ConfigFactory.parseString("""
      akka {
        loglevel = "INFO"
        actor {
          provider = "akka.remote.RemoteActorRefProvider"
        }
        remote {
          enabled-transports = ["akka.remote.netty.tcp"]
          netty.tcp {
            hostname = "127.0.0.1"
            port = 0
          }
          log-sent-messages = on
          log-received-messages = on
        }
      }""")
//    val system = ActorSystem("ClientSystem",config)
    val system = ActorSystem("LocalSystem" , ConfigFactory.load(config))
    val localActor = system.actorOf(Props[LocalActor], name="local")
  }


}
//class LocalActor extends Actor {
//
//  // create the remote actor (Akka 2.1 syntax)
//  val remote = context.actorSelection("akka.tcp://RemoteSystem@127.0.0.1:5150/user/remote")
//  var counter = 0
//  println("Connection to: " + remote)
////  remote ! "hi"
//
//  def receive = {
//    case "START" =>
//      println("local starts")
//      remote ! "Hello from the LocalActor"
//    case msg: String =>
//      println(s"LocalActor received message: '$msg'")
//      if (counter < 5) {
//        sender ! "Hello back to you"
//        counter += 1
//      }
//    case _ =>
//  }
//}
