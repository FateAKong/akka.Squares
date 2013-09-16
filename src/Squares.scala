/**
 * Created with IntelliJ IDEA.
 * User: FateAKong
 * Date: 9/9/13
 * Time: 8:23 PM
 */

import akka.actor.{Actor, ActorSystem, Props, ActorRef}
import scala.math.sqrt

object Squares extends App {

  if (args.length == 2) {
    findSeqs(nWorkers = 4, nElements = args(1).toInt, nMsgs = args(0).toInt)
  }

  def findSeqs(nWorkers: Int, nElements: Int, nMsgs: Int) {
    // Create an Akka system
    val system = ActorSystem("SquaresSystem")

    // create the master
    val master = system.actorOf(Props(new Master(nWorkers, nElements, nMsgs)), name = "master")

    // start the calculation
    master ! Assign

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
        // Use sum of squares equation to calculate sum of a sequence's squares
        // Here use Long to avoid potential Int overflow problems
        // e.g. 1000000 24 should output 30 sequences instead of 36 using Int
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

class Master(nWorkers: Int, nElements: Int, nMsgs: Int)
  extends Actor {

  var workLoad = nMsgs / nWorkers
  var nDone: Int = _

  val workers = new Array[ActorRef](nWorkers)
  for (i ← 0 to nWorkers - 1)
    workers(i) = context.actorOf(Props[Worker])

  def receive = {
    case Assign ⇒
      for (i ← 0 to nWorkers - 1) workers(i) ! Work(i * workLoad + 1, workLoad, nElements)
      workers(0) ! Work(nWorkers * workLoad + 1, nMsgs - nWorkers * workLoad, nElements)
    case Done ⇒
      nDone += 1
      if (nDone == nMsgs) {
        context.system.shutdown()
      }
  }
}