package Homework3

import org.apache.spark.graphx._
import org.apache.spark.{SparkConf, SparkContext}
import scala.collection.mutable.HashMap

class HyperBall(neighbourhoods: Array[(VertexId, Array[VertexId])]) {
  val c: HashMap[Int, Hyperloglog] = HashMap.empty[Int, Hyperloglog]
  var t = 0
  var distances: HashMap[Int, Double] = HashMap.empty[Int, Double]

  for (neighbourhood <- neighbourhoods){
    val id = neighbourhood._1.toInt
    val hyperloglog = new Hyperloglog()
    hyperloglog.add(id)
    c += ((id, hyperloglog))
    distances += ((id, 0))
  }

  def execute(): HashMap[Int, Double] = {
    var changed = true
    while (changed) {
      val pairs: HashMap[Int, Hyperloglog] = HashMap.empty[Int, Hyperloglog]
      changed = false

      for (neighbourhood <- neighbourhoods){
        val id = neighbourhood._1.toInt
        val neighbours = neighbourhood._2
        var a: Hyperloglog = c(id).copy()
        //if(id==36648)
          //println("t: " + t + " before: " + a.size())
        for (neighbour <- neighbours){
          a.union(c(neighbour.toInt))
        }
        pairs += ((id, a))
        distances(id) += ((a.size() - c(id).size())/(t + 1))
      }

      for (neighbourhood <- neighbourhoods){
        val id = neighbourhood._1.toInt
        if (!changed && !pairs(id).isSame((c(id)))) {
          changed = true
        }
        c(id) = pairs(id)
      }
      //println("t: " + t + " after: " + c(36648).size())
      t = t + 1
      //println("t: " + t + " D: " + distances(36648))
    }
    distances
  }
}
