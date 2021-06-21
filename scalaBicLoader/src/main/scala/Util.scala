package com.github.kright

def readLines(path: String): Array[String] =
  val source = scala.io.Source.fromFile(path, "UTF-8")
  val result = source.getLines().toArray
  source.close()
  result

extension (s: String)
  def getIndent: Int = {
    for((c, i) <- s.zipWithIndex) {
      if (c.isLetterOrDigit) {
        return i
      }
    }
    return s.length
  }