package homework1

import scala.collection.mutable.TreeSet

case class Shingling (k: Int) {
  def createHashedShingles(content: String): TreeSet[Int] = {
    val newContent = content.replaceAll("\\s+", " ")
    val set = TreeSet.empty[Int]
    for (i <- 0 until newContent.length - k) {
      set.add(newContent.substring(i, i + k).hashCode())
    }
    set
  }
}