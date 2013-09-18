/**
 * Created with IntelliJ IDEA.
 * User: FateAKong
 * Date: 9/16/13
 * Time: 10:10 PM
 */

import akka.kernel.Bootable
import akka.actor.{Props, Actor, ActorSystem}
import com.typesafe.config.ConfigFactory

class Daemon extends Bootable {
  //#setup
  val system = ActorSystem("Daemon",
    ConfigFactory.load.getConfig("daemon"))

  def startup() {
  }

  def shutdown() {
    system.shutdown()
  }
}

object Daemon extends App {
  new Daemon
  println("Started Daemon - waiting for tasks")
}
