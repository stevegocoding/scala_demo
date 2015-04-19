package com.magkbdev.searchFlights

import java.util.Calendar
import java.text.SimpleDateFormat

/**
 * Created by magkbdev on 4/16/15.
 */

class FlightEntry(val origin: String, val departureTime: Calendar,
                  val dest: String, val arrivalTime: Calendar, val price: Int) {

  override def equals(that: Any): Boolean =
    that match {
      case other: FlightEntry => {
        this.origin == other.origin && this.departureTime == other.departureTime &&
        this.dest == other.dest && this.arrivalTime == other.arrivalTime &&
        this.price == other.price
      }
      case _ => false
    }
}

object FlightEntry {
  val formatter = new SimpleDateFormat("M/d/yyyy HH:mm:ss")
  val template = "%s --> %s (%s --> %s) - $%d"
  def prettyPrint(entry: FlightEntry) = {
    println (template.format(entry.origin, entry.dest,
      formatter.format(entry.departureTime.getTime),
      formatter.format(entry.arrivalTime.getTime),
      entry.price))
  }
}
