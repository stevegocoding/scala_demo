package com.magkbdev.searchFlights

import scala.io.Source

/**
 * Created by magkbdev on 4/17/15.
 */

trait FlightsDAO {}
class FilesFlightsDAO(fileNames: List[String]) extends FlightsDAO {
  def findWhere(f: FlightEntry => Boolean): List[FlightEntry] = {
    fileNames.flatMap(
      file => {
        val source = Source.fromFile(file)
        val ret = for (i <- source.getLines().drop(1) ) yield {
          FlightDataParser.parseLine(i)
        }
        source.close()
        ret.filter(f)
      }
    ).distinct
  }
}

object FilesFlightsDAO {
  def apply(files: List[String]): FilesFlightsDAO = new FilesFlightsDAO(files)
}