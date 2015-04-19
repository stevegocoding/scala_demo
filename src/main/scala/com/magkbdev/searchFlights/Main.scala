package com.magkbdev.searchFlights

object Main {
  def main(args: Array[String]): Unit = {
    val daoFactory = AbastractFlightsDAOFactory(FactoryType.FilesDAO)
    daoFactory.files = List("data/Provider1.txt", "data/Provider2.txt", "data/Provider3.txt")

    daoFactory.createDAO.findWhere(
      (entry: FlightEntry) => entry.origin == "YYC" && entry.dest == "YVR"
    ).map(FlightEntry.prettyPrint(_))
  }
}
