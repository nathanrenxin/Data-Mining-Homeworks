package Homework3

import com.clearspring.analytics.stream.cardinality.HyperLogLog
import org.apache.spark.graphx.{EdgeDirection, GraphLoader}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.log4j.{Level, Logger}
import scala.collection.mutable.HashMap

object Main {

  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    val stream = 1 to 60000
    val hyperloglog = new Hyperloglog()
    stream.foreach(hyperloglog.add(_))
    println("Stream Hyperloglog counter is "+hyperloglog.size()+", while real size is 60000.")

    val conf = new SparkConf().setAppName("ID2222 Assignment 3").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val graph = GraphLoader.edgeListFile(sc, "facebook-wosn-links/out.facebook-wosn-links")
    val neighbourhoods = graph.reverse.collectNeighborIds(EdgeDirection.Out).collect()
    val hb = new HyperBall(neighbourhoods)
    val closeness: HashMap[Int, Double] = hb.execute()
    println("Top 10 nodes with highest harmonic centrality:")
    closeness.toSeq.sortWith(_._2>_._2).take(10).foreach(println)
    //println(neighbourhoods.filter(_._1.toInt == 36648)(0)._2.size)
    //println(graph.edges.count())
  }
  
}