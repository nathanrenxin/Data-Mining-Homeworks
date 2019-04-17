package homework1

import scala.collection.mutable.TreeSet

//A class MinHashing that builds a minHash signature (in the form of a vector or a set) of a given
// length n from a given set of integers (a set of hashed shingles).

import scala.collection.mutable.SortedSet


/*
 * A class MinHashing that builds a minHash signature
 * (in the form of a vector or a set) of a given
 * length n from a given set of integers (a set of hashed shingles).
 */





class MinHashing(signatureLen: Int) extends Serializable {
  val random = new scala.util.Random(99744299)
  val prime = 2147483659L
  val coefficientA = generateCoefficient(signatureLen, Int.MaxValue)
  val coefficientB = generateCoefficient(signatureLen, Int.MaxValue)

  def generateCoefficient(number: Int, max: Int): IndexedSeq[Int] = {
    return for (i <- 1 to number) yield r.nextInt(max)
  }

  def signatures(shingles: TreeSet[Int]): Array[BigInt] = {
    /*val signaturesInit = BigInt(0) to BigInt(coefficientA.length-1)

    val signatures = signaturesInit.map(i => shingles.map(shingle=>BigInt(coefficientA(i.toInt) * shingle + coefficientB(i.toInt)) % prime)).map(_.min)

    println(signatures)

    signatures.toArray*/

    val signatureInit = Array.fill[BigInt](coefficientA.length)(prime + 1)
    val signatures = shingles.toList.aggregate(signatureInit)((signatureTmp, shingle) => {
      for (i <- 0 to coefficientA.length - 1) {
        val hash = BigInt(coefficientA(i) * shingle + coefficientB(i)) % prime
        if (signatureTmp(i) > hash) {
          signatureTmp(i) = hash
        }
      }
      signatureTmp
    }, (bucket1, bucket2) => {
      for (i <- 0 to bucket1.length - 1) {
        if (bucket1(i) > bucket2(i)) {
          bucket1(i) = bucket2(i)
        }
      }
      bucket1
    })
    //println(signatures mkString ",")
    signatures
  }
}


