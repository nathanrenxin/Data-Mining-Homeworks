package Homework2
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import scala.collection.mutable.ArrayBuffer


class Apriori {

  def findFrequentItemSets(rddData:RDD[Set[Int]], threshold:Int):Array[(Seq[Int], Int)] = {
    val singletons = rddData.flatMap(data => data.map(x => (x, 1))).
      reduceByKey(_ + _).
      filter(_._2 >= threshold).collect()

    if(singletons.isEmpty) {
      Array()
    } else {
      var nPairs: Array[(Seq[Int], Int)] = singletons.map(x => (ArrayBuffer(x._1), x._2))
      var frequentItems = singletons.map(x => x._1)
      var rdd_nPairs = rddData.filter(basket => singletons.map(x => Set(x._1)).exists(_.subsetOf(basket))).map(basket => basket.filter(frequentItems.contains(_)))
      var result: Array[(Seq[Int], Int)] = nPairs
      var length = 2
      while (!nPairs.isEmpty){
        nPairs = rdd_nPairs.flatMap(_.toSeq.combinations(length)).
          map(p => (p, 1)).reduceByKey(_ + _).
          filter(_._2 >= threshold).
          collect()
        frequentItems = nPairs.flatMap(x=>x._1).distinct
        rdd_nPairs = rdd_nPairs.filter(basket => nPairs.map(x => x._1.toSet).exists(_.subsetOf(basket))).map(basket => basket.filter(frequentItems.contains(_)))
        length = length + 1
        result = result ++ nPairs
      }
      result
    }
  }

  def generateAssociationRules(frequentItemSets:Array[(Seq[Int], Int)], confidence: Double): Array[(Seq[Int], Seq[Int], Double)] ={
    var result :  Array[(Seq[Int], Seq[Int], Double)] = Array((Seq(),Seq(), -1.doubleValue()))

    val sortedSets = frequentItemSets.map(x=>(x._1.sorted, x._2))

    for ((itemsets,count) <- frequentItemSets) {
      if(itemsets.size >= 2){
        for(i:Int <- 1 to itemsets.size -1){
          result = result ++ itemsets.combinations(i).toArray.map(s => (s,itemsets.diff(s),
            count.toDouble/(sortedSets.filter(_._1.equals(s.sorted))(0)._2)))
        }
      }
    }
    result.filter(_._3>=confidence)
  }
}