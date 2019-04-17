package Homework3

import scala.util.hashing.MurmurHash3
import java.nio.ByteBuffer

class Hyperloglog {

  val b: Int = 8
  val a: Double = getA(b)

  val counter: Array[Byte] = Array.fill[Byte](scala.math.pow(2, b).toInt)(0)


  def add(item: Int) = {
    val h = MurmurHash3.bytesHash(ByteBuffer.allocate(4).putInt(item).array())
    val i = h >>> (32 - b)
    val c = h << b
    counter(i) = scala.math.max(counter(i).toInt, Integer.numberOfLeadingZeros(c) + 1).toByte
  }

  def size(): Double = {
    val length = counter.length
    val z = 1 / (counter.map(x => (1 / scala.math.pow(2, x.toInt))).reduce(_ + _))
    val size = a * length * length * z
    size
  }

  def union(N: Hyperloglog) = {
    val length = counter.length
    for (i <- 0 until length){
      counter(i) = scala.math.max(counter(i).toInt, N.counter(i).toInt).toByte
    }
  }

  def copy(): Hyperloglog = {
    var i = 0
    val copy = new Hyperloglog()
    for (i <- 0 until counter.length){
      copy.counter(i) = counter(i)
    }
    copy
  }

  def isSame(N: Hyperloglog): Boolean ={
    var result = true
    var i = 0
    while (result && i < counter.length){
      if (counter(i) != N.counter(i))
        result = false
      i = i + 1
    }
    result
  }


  def getA(b: Int): Double = { // See the paper.
    b match {
      case 4 =>
        0.673
      case 5 =>
        0.697
      case 6 =>
        0.709
      case _ =>
        (0.7213 / (1 + 1.079 / scala.math.pow(2, b).toInt))
    }
  }
}