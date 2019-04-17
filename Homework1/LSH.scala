package homework1

import scala.collection.mutable.TreeSet

class LSH {
  def generateCandidates(minhash: Map[String, Array[BigInt]], similarityThreshold: Double, sigLength: Int) = {
    val combos = minhash.keys.toList.combinations(2).toList
    val (bandNumber, rowNumber) = calPartitions(similarityThreshold, sigLength)
    val minhashPartitioned = minhash.mapValues(sig=>(0 to bandNumber-1).map(bandId=> sig.slice(bandId*rowNumber,bandId*rowNumber+rowNumber).toSeq.hashCode()))
    combos.filter(combo=> isCandidates(minhashPartitioned(combo(0)),minhashPartitioned(combo(1))))
  }

  def isCandidates(minhashP1: IndexedSeq[Int], minhashP2: IndexedSeq[Int]): Boolean = {
    /*minhashP1.foreach(x=>println(x.mkString(",")))
    println(s"=================================================")
    minhashP2.foreach(x=>println(x.mkString(",")))*/

    //println(minhashP1.mkString(","))
    //println(minhashP2.mkString(","))
    var result = false
    var i = 0
    while (result == false && i < minhashP1.size) {
      if (minhashP1(i)==minhashP2(i))
        result = true
      else
        i = i + 1
    }
    result
  }

  def calPartitions(similarityThreshold: Double, sigLength: Int): (Int, Int) = {
    var b = 0d
    var b1 = 0d
    var r = 0d
    var r1 = 0d
    var t = 1d
    var t1 = 1d
    do {
      b = b + 1
      if(sigLength%b == 0) {
        r = sigLength/b
        t = scala.math.pow(1d/b,1d/r)
        if (t>=similarityThreshold){
          b1 = b
          r1 = r
          t1 = t
        }
      }
    }
    while (t>=similarityThreshold && b<=sigLength)
    println((s"Band number is $b1, row number is $r1, and threshold is $t1."))
    (b1.toInt, r1.toInt)
  }
}