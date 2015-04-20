package com.magkbdev.searchFlights

object Main {
  def printUsage(): Unit = {
    println ("Please use the following options: ")
    println ("-o -- The origin of the flights")
    println ("-d -- The destination of the flights")
  }

  def parseArgs(args: Array[String]): Option[(String, String)] = {
    val pattern = """\-o ([a-zA-Z]+) \-d ([a-zA-Z]+)""".r
    args.mkString(" ") match {
      case pattern(o, d) => Some((o.toString, d.toString))
      case _ => None
    }
  }

  def main(args: Array[String]): Unit = {
    parseArgs(args) match {
      case Some(od:(String,String)) => {
        val daoFactory = AbastractFlightsDAOFactory(FactoryType.FilesDAO)
        daoFactory.files = List("data/Provider1.txt", "data/Provider2.txt", "data/Provider3.txt")

        val flights = daoFactory.createDAO.findWhere(
          (entry: FlightEntry) => entry.origin == od._1 && entry.dest == od._2
        )
        if (flights.isEmpty) println("No flights found!") else flights.map(FlightEntry.prettyPrint(_))
      }
      case _ => printUsage()
    }
  }
}
