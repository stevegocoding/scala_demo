import org.scalatest.FunSuite

import java.util.Calendar
import com.magkbdev.searchFlights._

/**
 * Created by magkbdev on 4/16/15.
 */
class SearchFlightsSuite extends FunSuite {

  test("Parse flights location") {
    val location = "YYZ"
    assert( FlightDataParser.parseLocation(location) == Some("YYZ") )
  }

  test("Parse flights departure/arrival date") {
    val date1 = "6-21-2014 17:55:00"
    val cal1 = FlightDataParser.parseDate(date1).getOrElse(Calendar.getInstance())

    assert(cal1.get(Calendar.YEAR) == 2014)
    assert(cal1.get(Calendar.MONTH) == Calendar.JUNE)
    assert(cal1.get(Calendar.DATE) == 21)
    assert(cal1.get(Calendar.HOUR_OF_DAY) == 17)
    assert(cal1.get(Calendar.MINUTE) == 55)

    val date2 = "6/30/2014 9:30:00"
    val cal2 = FlightDataParser.parseDate(date2).getOrElse(Calendar.getInstance())

    assert(cal2.get(Calendar.YEAR) == 2014)
    assert(cal2.get(Calendar.MONTH) == Calendar.JUNE)
    assert(cal2.get(Calendar.DATE) == 30)
    assert(cal2.get(Calendar.HOUR_OF_DAY) == 9)
    assert(cal2.get(Calendar.MINUTE) == 30)
  }

  test("Parse flights price") {
    val price = "$1122.00"
    assert(FlightDataParser.parsePrice(price) == Some(1122))
  }

  test("Parse flight entry") {
    val f = "YYC,6-23-2014 12:40:00,YYZ,6-23-2014 14:54:00,$541.00"
    val flight = FlightDataParser.parseLine(f)

    assert(flight.origin == "YYC")

    assert(flight.departureTime.get(Calendar.YEAR) == 2014)
    assert(flight.departureTime.get(Calendar.MONTH) == Calendar.JUNE)
    assert(flight.departureTime.get(Calendar.DATE) == 23)
    assert(flight.departureTime.get(Calendar.HOUR_OF_DAY) == 12)
    assert(flight.departureTime.get(Calendar.MINUTE) == 40)

    assert(flight.dest == "YYZ")

    assert(flight.arrivalTime.get(Calendar.YEAR) == 2014)
    assert(flight.arrivalTime.get(Calendar.MONTH) == Calendar.JUNE)
    assert(flight.arrivalTime.get(Calendar.DATE) == 23)
    assert(flight.arrivalTime.get(Calendar.HOUR_OF_DAY) == 14)
    assert(flight.arrivalTime.get(Calendar.MINUTE) == 54)

    assert(flight.price == 541)

    val f2 = "JFK|6/15/2014 9:30:00|YEG|6/15/2014 17:50:00|$730.00"
    val flight2 = FlightDataParser.parseLine(f2)

    assert(flight2.origin == "JFK")

    assert(flight2.departureTime.get(Calendar.YEAR) == 2014)
    assert(flight2.departureTime.get(Calendar.MONTH) == Calendar.JUNE)
    assert(flight2.departureTime.get(Calendar.DATE) == 15)
    assert(flight2.departureTime.get(Calendar.HOUR_OF_DAY) == 9)
    assert(flight2.departureTime.get(Calendar.MINUTE) == 30)

    assert(flight2.dest == "YEG")

    assert(flight2.arrivalTime.get(Calendar.YEAR) == 2014)
    assert(flight2.arrivalTime.get(Calendar.MONTH) == Calendar.JUNE)
    assert(flight2.arrivalTime.get(Calendar.DATE) == 15)
    assert(flight2.arrivalTime.get(Calendar.HOUR_OF_DAY) == 17)
    assert(flight2.arrivalTime.get(Calendar.MINUTE) == 50)

    assert(flight2.price == 730)
  }

  test("Search duplicate flights ") {
    val daoFactory = AbstractFlightsDAOFactory(FactoryType.FilesDAO)
    daoFactory.files = List("data/Provider1.txt", "data/Provider2.txt", "data/Provider3.txt")
    val flights = daoFactory.createDAO.findWhere(
      (f: FlightEntry) => f.origin == "YYC" && f.dest == "YYZ"
    )
    assert(flights.size == 3)
  }

  test("Sort flights search result by price primarily and departure time in secondary") {
    val daoFactory = AbstractFlightsDAOFactory(FactoryType.FilesDAO)
    daoFactory.files = List("src/test/data/Provider1.txt",
      "src/test/data/Provider2.txt", "src/test/data/Provider3.txt")
    val flights = daoFactory.createDAO.findWhere(
      (f: FlightEntry) => f.origin == "YYZ" && f.dest == "YYC"
    )
    assert(flights.size == 3)
    assert(flights(0).price < flights(1).price && flights(1).price < flights(2).price)

    val flights2 = daoFactory.createDAO.findWhere(
      (f: FlightEntry) => f.origin == "MIA" && f.dest == "ORD"
    )
    assert(flights2.size == 2)
    assert(flights2(0).price == flights2(1).price &&
      flights2(0).departureTime.compareTo(flights2(1).departureTime) < 0)
  }

  test("Parse command arguments") {
    val args = Array("-o", "YYC", "-d", "YYZ")
    val od: (String, String) = Main.parseArgs(args).getOrElse(("", ""))
    assert(od._1 == "YYC")
    assert(od._2 == "YYZ")
  }

}
