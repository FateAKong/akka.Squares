/**
 * Created with IntelliJ IDEA.
 * User: FateAKong
 * Date: 9/9/13
 * Time: 8:23 PM
 */

import akka.actor._
import akka.routing.RoundRobinRouter
import scala.collection.mutable.TreeSet
import scala.math._

object Squares extends App {

  if (args.length == 2) {
    findSeqs(nWorkers = 2, nElements = args(1).toInt, nMsgs = args(0).toInt)
  }

  def findSeqs(nWorkers: Int, nElements: Int, nMsgs: Int) {
    // Create an Akka system
    val system = ActorSystem("SquaresSystem")

    // create the result listener, which will print the result and shutdown the system
    val listener = system.actorOf(Props[Listener], name = "listener")

    // create the master
    val master = system.actorOf(Props(new Master(nWorkers, nElements, nMsgs, listener)), name = "master")

    // startOfSeq the calculation
    master ! Assign

  }
}

sealed trait SquaresMsg

case object Assign extends SquaresMsg

// only one Calculate message is permitted during entire execution
case class Work(start: Int, nrOfElements: Int) extends SquaresMsg

case class Result(startOfSeq: Int) extends SquaresMsg

case class ResultsSet(seqsSet: TreeSet[Int]) extends SquaresMsg

class Worker extends Actor {

  def verifySeq(startOfSeq: Int, nElements: Int): Int = {
    var sumOfSquares = 0.0
    for (i ← startOfSeq until (startOfSeq + nElements)) {
      // calculate square using Long to avoid potential Int overflow problems
      // e.g. 1000000 24 should output 30 sequences instead of 36 using Int
      sumOfSquares += i.toLong * i.toLong
    }
    val sqrtOfSum = sqrt(sumOfSquares)
    if (sqrtOfSum == ceil(sqrtOfSum))
      startOfSeq
    else
      0 - startOfSeq
  }

  def receive = {
    case Work(start, nElements) ⇒
      sender ! Result(verifySeq(start, nElements)) // perform the work
  }
}

class Master(nWorkers: Int, nElements: Int, nMsgs: Int, listener: ActorRef)
  extends Actor {

  var seqsSet = new TreeSet[Int]
  var nResults: Int = _

  val workerRouter = context.actorOf(
    Props[Worker].withRouter(RoundRobinRouter(nWorkers)), name = "workerRouter")

  def receive = {
    case Assign ⇒
      for (i ← 1 to nMsgs) workerRouter ! Work(i, nElements)
    case Result(startOfSeq) ⇒
      if (startOfSeq > 0)
        seqsSet += startOfSeq
      nResults += 1
      if (nResults == nMsgs) {
        // Send the result to the listener
        listener ! ResultsSet(seqsSet)
        // Stops this actor and all its supervised children
        context.stop(self)
      }
  }

}

class Listener extends Actor {
  def receive = {
    case ResultsSet(seqsSet) ⇒
      seqsSet.foreach(println)
      context.system.shutdown()
  }
}
