/**
 * Created with IntelliJ IDEA.
 * User: FateAKong
 * Date: 9/9/13
 * Time: 8:23 PM
 */

import akka.actor.{Actor, ActorSystem, Props, ActorRef}
import akka.routing.RoundRobinRouter
import scala.math.sqrt

import akka.kernel.Bootable
import com.typesafe.config.ConfigFactory

object Squares extends App {

  if (args.length == 2) {
    val creator = new CreationApplication(nWorkers = 4, nElements = args(1).toInt, nMsgs = args(0).toInt)
    creator.findSeqs
    println("Master Started Assigning Tasks to Remote Computers")
  }


}

class CreationApplication(nWorkers: Int, nElements: Int, nMsgs: Int) extends Bootable {

  val system = ActorSystem("RemoteCreation", ConfigFactory.load.getConfig("remotecreation"))
  // remoteActor
  val worker = system.actorOf(Props[Worker], name = "worker")
  // localActor
  val master = system.actorOf(Props(new Master(nWorkers, nElements, nMsgs, worker)), name = "master")

  def findSeqs() {
    master ! Assign
  }

  def startup() {
  }

  def shutdown() {
    system.shutdown()
  }
}

sealed trait SquaresMsg

case object Assign extends SquaresMsg

// only one Calculate message is permitted during entire execution
case class Work(startOfLoad: Int, workLoad: Int, nElements: Int) extends SquaresMsg

case object Done extends SquaresMsg

class Worker extends Actor {

  def receive = {
    case Work(startOfLoad, workLoad, nElements) ⇒
      for (startOfSeq <- startOfLoad to startOfLoad + workLoad - 1) {
        val start: Long = startOfSeq - 1
        val end: Long = start + nElements
        val sumOfSquares = (end * (end + 1) * (2 * end + 1) - start * (start + 1) * (2 * start + 1)) / 6
        val sqrtOfSum = sqrt(sumOfSquares)
        if (sqrtOfSum == sqrtOfSum.toLong)
          println(startOfSeq)
        sender ! Done
      }
  }
}

class Master(nWorkers: Int, nElements: Int, nMsgs: Int, worker: ActorRef) extends Actor {

//  var workLoad = nMsgs / nWorkers
  var nDone: Int = _

//  val workers = new Array[ActorRef](nWorkers)
//  val workerRouter = new RoundRobinRouter()
//  for (i ← 0 to nWorkers - 1)
//    workers(i) = context.actorOf(Props[Worker])

  def receive = {
    case Assign ⇒
//      for (i ← 0 to nWorkers - 1) workers(i) ! Work(i * workLoad + 1, workLoad, nElements)
//      workers(0) ! Work(nWorkers * workLoad + 1, nMsgs - nWorkers * workLoad, nElements)
      worker ! Work(1, nMsgs, nElements)
    case Done ⇒
      nDone += 1
      if (nDone == nMsgs) {
        context.system.shutdown()
      }
  }
}