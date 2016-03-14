package Message

/**
  * Created by Jing Ao on 2016/3/13.
  */

import akka.actor.{PoisonPill, ActorSystem}
import akka.dispatch.{PriorityGenerator, UnboundedStablePriorityMailbox}
import com.typesafe.config.Config

import scala.collection.mutable.ListBuffer

/**
  * Created by Jing Ao on 2016/2/27.
  */
class PriorityMailbox(settings: ActorSystem.Settings, config: Config)
  extends UnboundedStablePriorityMailbox(
    PriorityGenerator {
      case msg: ListBuffer[String] => 0
      case str: String => 1
      case PoisonPill => 2
      case _ => 3
    }) {
}
