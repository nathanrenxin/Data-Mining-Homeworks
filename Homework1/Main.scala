package homework1

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

import scala.collection.immutable.Set
import org.apache.spark.rdd.RDD
import org.apache.log4j.{Level, Logger}
import scala.collection.mutable.TreeSet

object Main {

  val dataSizes = 2 to 10
  val shinglingK = 10
  val similarityThreshold = 0.8
  val hashFunctionNumber = 200

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    val shingling = new Shingling(shinglingK)
    val compareSets = new CompareSets[Int]
    val minhashing = new MinHashing(hashFunctionNumber)
    val compareSignatures = new CompareSignatures
    val lsh = new LSH

    val conf = new SparkConf().setAppName("ID2222 Homework1").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val blogs = sc.wholeTextFiles("resource")

    for (dataSize <- dataSizes) {
      println(s"Data Size is $dataSize")
      var hashedShinglesMap:Map[String, TreeSet[Int]]=Map()

      val t0 = System.nanoTime
      val hashedShingles = blogs.take(dataSize).map{case (blog,content) => hashedShinglesMap+=(blog->shingling.createHashedShingles(content))}
      val combos = hashedShinglesMap.keys.toList.combinations(2).toList
      val t1 = System.nanoTime
      val jaccardCombos = combos.filter(combo=> compareSets.getJaccardSimilarity(hashedShinglesMap(combo(0)), hashedShinglesMap(combo(1)))>=similarityThreshold)
      val t2 = System.nanoTime
      val minHashSignaturesMap = hashedShinglesMap.mapValues(shinglings=> minhashing.createSignature(shinglings))
      val t3 = System.nanoTime
      val minHashCombos = combos.filter(combo=> compareSignatures.getJaccardSimilarity(minHashSignaturesMap(combo(0)), minHashSignaturesMap(combo(1)))>=similarityThreshold)
      val t4 = System.nanoTime
      val lshCombos = lsh.generateCandidates(minHashSignaturesMap, similarityThreshold, hashFunctionNumber)
      val t5 = System.nanoTime

      println(s"Duration for Jaccard: ${(t2 - t0) / 1e9d} sec")
      println(s"Duration for MinHash: ${(t1 - t0 + t4 - t2) / 1e9d} sec")
      println(s"Duration for LSH: ${(t1 - t0 + t3 - t2 + t5 - t4) / 1e9d} sec")

      jaccardCombos.foreach {
        case List(blog1, blog2)
        =>
          val similarity = compareSets.getJaccardSimilarity(hashedShinglesMap(blog1), hashedShinglesMap(blog2))
          println(s"Blogs ($blog1, $blog2) have Jaccard similarity of $similarity")
      }
      minHashCombos.foreach {
        case List(blog1, blog2)
        =>
          val similarity = compareSignatures.getJaccardSimilarity(minHashSignaturesMap(blog1), minHashSignaturesMap(blog2))
          println(s"Blogs ($blog1, $blog2) have minhash similarity of $similarity")
      }

      lshCombos.foreach {
        case List(blog1, blog2)
        =>
          println(s"Blogs ($blog1, $blog2) are candidate pairs with at least similarity of $similarityThreshold")
      }

      println(s"********************************************************")
      println(s"********************************************************")
      println(s"********************************************************")
      println(s"********************************************************")

    }

  }

}