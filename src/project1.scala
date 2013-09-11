/**
 * Created with IntelliJ IDEA.
 * User: Haotian
 * Date: 9/10/13
 * Time: 9:01 AM
 * To change this template use File | Settings | File Templates.
 */
object project1 {
  def main(args: Array[String]) {
    if (args.length!=2)
      return
    val squares = new Squares
    squares.findSeqs(nWorkers = 2, nElements = args(1).toInt, nMsgs = args(0).toInt)
  }
}
