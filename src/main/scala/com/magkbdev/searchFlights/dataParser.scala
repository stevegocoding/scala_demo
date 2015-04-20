package com.magkbdev.searchFlights

import scala.io.Source
import java.util.Date
import java.util.Calendar

/**
 * A data parser using scala's Regex
 */
object FlightDataParser {
  def parseLocation(str: String): Option[String] = {
    val locPattern = """([A-Z]{3})""".r
    str match {
      case locPattern(codeName) => Some(codeName)
      case _ => None
    }
  }

  def parseDate(str: String): Option[Calendar] = {
    var datePattern = """([0-9]|1[012])[-\/]([1-9]|[12][0-9]|3[01])[-\/](20\d\d) ([0-9]|1[0-9]|2[0-3]):(0[0-9]|[0-5][0-9]):00""".r
    str match {
      case datePattern(month, day, year, hour, min) => {
        val cal= Calendar.getInstance()
        cal.clear()
        cal.set(year.toInt, month.toInt - 1, day.toInt, hour.toInt, min.toInt)
        Some(cal)
      }
      case _ => None
    }
  }

  def parsePrice(str: String): Option[Int] = {
    val pricePattern = """\$([0-9]+)\.[0-9]{2}""".r
    str match {
      case pricePattern(price) => Some(price.toInt)
      case _ => None
    }
  }

  // A list of parsers' functions for cleaner line parsing
  val flightParsers = List(parseLocation _, parseDate _, parseLocation _, parseDate _, parsePrice _)

  def parseLine(line: String): FlightEntry = {
    val params = for ( (s, parser) <- line.split(""",|\|""").toList.zip(flightParsers) ) yield {
      val r = parser(s)
      r match {
        case None => throw new Exception("Parsing data failed")
        case _ => r
      }
    }

    new FlightEntry(
      params(0) match { case Some(x:String) => x case _ => ""},
      params(1) match { case Some(x:Calendar) => x case _ => Calendar.getInstance()},
      params(2) match { case Some(x:String) => x case _ => ""},
      params(3) match { case Some(x:Calendar) => x case _ => Calendar.getInstance()},
      params(4) match { case Some(x:Int) => x case _ => 0}
    )
  }
}
