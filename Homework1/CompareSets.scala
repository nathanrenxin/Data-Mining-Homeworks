package homework1

import scala.collection.mutable.Set

class CompareSets[A] {

  def getJaccardSimilarity(set1: Set[A], set2: Set[A]): Double = {
    set1.intersect(set2).size.toDouble/set1.union(set2).size.toDouble
  }

}