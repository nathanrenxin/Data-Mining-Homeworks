package homework1

class CompareSignatures {

  def getJaccardSimilarity(signatureOne : Array[BigInt], signatureTwo : Array[BigInt]) = {
    var similarityCount : Double = 0

    for(i <- 0 until signatureOne.length) {
      if(signatureOne(i) == signatureTwo(i))
        similarityCount += 1
    }
    similarityCount/signatureOne.length.toDouble
  }
}