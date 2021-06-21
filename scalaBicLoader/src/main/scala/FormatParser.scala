package com.github.kright

import scala.collection.mutable.ArrayBuffer
import scala.util.chaining._
import scala.language.implicitConversions


object FormatParser:
  def main(args: Array[String]): Unit = {
    println(s"args = ${args.mkString("[", ", ", "]")}")

    val text = readLines("../bicformat.txt")
    val parsed = parse(text)

    println(s"text = ${text}")
  }

def parse(lines: Array[String]) = {
  val filteredLines = lines.filter(s => !s.startsWith("//") && !s.isBlank)
  val sections = groupSections(filteredLines)
  sections.foreach(parseSection)
}

def groupSections(lines: Iterable[String]): Array[Array[String]] = {
  val result = new ArrayBuffer[Array[String]]()
  val accum = new ArrayBuffer[String]()
  for(line <- lines) {
    if (line.startsWith("TOP")) {
      if (accum.nonEmpty) {
        result += accum.toArray
      }
      accum.clear()
    }
    accum += line
  }
  result += accum.toArray
  result.toArray
}

def parseSection(lines: Array[String]): Unit = {
  val (shortName, longName) = parseSectionHeader(lines.head)

  val content = lines.tail.toArray
  var pos = 0

  val result = new ArrayBuffer[Content]()

  def parseForEach(): ForEachDescr = {
    require(content(pos).trim.startsWith("For each "))
    val result = new ArrayBuffer[Content]()
    val name = content(pos).trim.drop(9).dropRight(1)
    pos += 1
    val indent = content(pos).getIndent
    result += parse()
    while(pos < content.size && content(pos).getIndent == indent) {
      result += parse()
    }

    ForEachDescr(name, result.toList)
  }

  def parseSimple(): Simple =
    Simple(content(pos)).tap { _ =>
      pos += 1
    }

  def parseBinary(): Binary = {
    require(content(pos).endsWith("(binary):"))
    val text = content(pos)
    pos += 1
    if (content(pos).contains("00000000")) {
      pos += 1
    }
    val bits = new ArrayBuffer[String]()
    val indent = content(pos).getIndent
    while(pos < content.size && (hasBitMask(content(pos)) || content(pos).contains("00000000"))) {
      if (hasBitMask(content(pos))) {
        bits += content(pos)
      }
      pos += 1
    }
    Binary(text, bits.toList)
  }

  def parse(): Content = {
    if (content(pos).endsWith("(binary):")) {
      parseBinary()
    } else if (content(pos).trim.startsWith("For each")) {
      parseForEach()
    } else {
      parseSimple()
    }
  }

  while(pos < content.size) {
    result += parse()
  }

  println(s"parsed section = ${result.mkString("[", "\n", "]")}\n")
  result
}

def parseSectionHeader(line: String): (String, Option[String]) = {
  require(line.startsWith("TOP"))

  val headerArr = line.split(" ")
  require(headerArr(0) == "TOP")
  val shortName = headerArr(1)
  require(headerArr(2) == "SECTION")
  val longName = line.split("\\(").lift(1).map(_.replace(')', ' ').trim)
  (shortName, longName)
}



case class Section(shortName: String, description: String, content: Array[Content])

sealed trait Content

case class Simple(text: String) extends Content
case class Binary(text: String, bits: List[String]) extends Content
case class ForEachDescr(name: String, contents: List[Content]) extends Content

def hasBitMask(s: String): Boolean = {
  val masks = Seq(
    "1.......",
    ".1......",
    "..1.....",
    "...1....",
    "....1...",
    ".....1..",
    "......1.",
    ".......1"
  )
  masks.exists(m => s.trim.endsWith(m))
}
