package com.magkbdev.searchFlights

import scala.io.Source

/**
 * An abstract flight DAO trait
 */
trait FlightsDAO {
  def findWhere(f: FlightEntry => Boolean): List[FlightEntry]
}

/**
 * An simple files flights DAO
 */
class FilesFlightsDAO(val fileNames: List[String]) extends FlightsDAO {
  def removeDup(lst: List[FlightEntry]): List[FlightEntry] = {
    if (lst.isEmpty) lst
    else lst.head :: removeDup(lst.filter(_ != lst.head))
  }

  def findWhere(f: FlightEntry => Boolean): List[FlightEntry] = {
    val fs = fileNames.flatMap(
      file => {
        val source = Source.fromFile(file)
        val ret = for (i <- source.getLines().drop(1)) yield FlightDataParser.parseLine(i)
        ret.filter(f)
      }
    )
    removeDup(fs)
  }
}
