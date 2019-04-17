package Homework2

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

import scala.collection.immutable.Set
import org.apache.spark.rdd.RDD
import org.apache.log4j.{Level, Logger}

object Main {

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    val conf = new SparkConf().setAppName("ID2222 Homework2").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val data = sc.textFile("data/T10I4D100K.dat")
    val baskets = data.map(_.split("\\s+").map(_.toInt).toSet).cache()
    var size = baskets.count()
    println(s"The size of data is $size.")
    val support = (0.01 * size).toInt
    println(s"Support threshold is $support.")
    val apriori = new Apriori()
    val t0 = System.nanoTime
    val frequentItemSets = apriori.findFrequentItemSets(baskets, support)
    val t1 = System.nanoTime
    size = frequentItemSets.size
    println(s"The size of frequent itemsets is $size.")
    for((itemsets,count) <- frequentItemSets) println("("++itemsets.mkString(",")++"): "++ count.toString)
    val confidence = 0.5
    val t2 = System.nanoTime
    val associationRules = apriori.generateAssociationRules(frequentItemSets,confidence)
    val t3 = System.nanoTime
    size = associationRules.size
    println(s"The size of association rules is $size.")
    for((itemsets1, itemsets2, count) <- associationRules) println("("++itemsets1.mkString(",")++")->("++itemsets2.mkString(",")++"): "++ count.toString)
    println(s"Duration for generating frequent itemsets: ${(t1 - t0) / 1e9d} sec")
    println(s"Duration for generating association rules: ${(t3 - t2) / 1e9d} sec")
    sc.stop()

  }
}

