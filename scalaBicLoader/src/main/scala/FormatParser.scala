package com.github.kright

import scala.collection.mutable.ArrayBuffer
import scala.util.chaining._
import scala.language.implicitConversions


object FormatParser:
  def main(args: Array[String]): Unit = {
    println(s"args = ${args.mkString("[", ", ", "]")}")

    val text = readLines("../bicformat.txt")
    val parsed = parse(text)

    parsed.foreach( section =>
      println(s"${section.prettyString}")
    )

    println(s"text = ${text}")
  }

def parse(lines: Array[String]) = {
  val filteredLines = lines.filter(s => !s.startsWith("//") && !s.isBlank)
  val sections = groupSections(filteredLines)
  sections.map(parseSection)
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

def parseSection(lines: Array[String]): Section = {
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

  Section(shortName, longName, result.toList)
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


case class Section(shortName: String, description: Option[String], content: List[Content]):
  def prettyString: String = {
    def intended(c: Content, level: Int): String = {
      val indent = "  ".repeat(level)
      c match {
        case Simple(size, tip, text) => s"${indent}${size}:${tip} ${text}"
        case Binary(size, tip, text, bits) => s"${indent}${size}:${tip} ${text} ${bits.mkString(", ")}"
        case ForEachDescr(name, contents) =>
          s"${indent}For each ${name}:\n${contents.map(c => intended(c, level + 2)).mkString("\n")}"
      }
    }

    s"SECTION ${shortName} : ${description.getOrElse("")}\n" +
    s"${content.map(c => intended(c, 2)).mkString("\n")}"
  }

sealed trait Content

case class Simple(size: Int, `type`: String, text: String) extends Content
case class Binary(size: Int, `type`: String, text: String, bits: List[Bit]) extends Content
case class ForEachDescr(name: String, contents: List[Content]) extends Content

object Simple:
  def apply(text: String): Simple = {
    val (first, others) = text.splitFirstWord
    val (second, rem) = others.splitFirstWord
    require(!rem.isBlank)
    Simple(first.toInt, second, rem)
  }

object Binary:
  def apply(text: String, bits: List[String]): Binary = {
    val (first, others) = text.splitFirstWord
    val (second, rem) = others.splitFirstWord
    require(!rem.isBlank)
    val bbits: List[Bit] = bits.map(s => Bit(s))
    val bbbits: List[Bit] = bbits.zipWithIndex.map((b, i) => b.copy(place = b.place + 8 * (i / 8)))
    require(bbbits.last.place < 32)
    Binary(first.toInt, second, rem, bbbits)
  }

case class Bit(name: String, place: Int)

object Bit:
  def apply(descr: String): Bit = {
    val t = descr.trim
    require(hasBitMask(t))
    val text = t.substring(0, t.length - 8).trim
    val mask = t.substring(t.length - 8)
    Bit(text, mask2Int(mask))
  }

  val masks = Seq(
    ".......1",
    "......1.",
    ".....1..",
    "....1...",
    "...1....",
    "..1.....",
    ".1......",
    "1.......",
  )

  val mask2Int: Map[String, Int] = masks.zipWithIndex.toMap

def hasBitMask(s: String): Boolean = {
  Bit.masks.exists(m => s.trim.endsWith(m))
}

