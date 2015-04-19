package com.magkbdev.searchFlights

import java.security.InvalidParameterException

trait AbastractFlightsDAOFactory {
  def createDAO: FlightsDAO
}

object AbastractFlightsDAOFactory {
  object FactoryType extends Enumeration {
  }

  def apply(factoryType: String) = {
    factoryType match {
      case "file" => FileFlightsDAOFactory()
      case _ => throw new InvalidParameterException("No factory type found!")
    }
  }
}

class FileFlightsDAOFactory(var files: List[String] = List()) extends AbastractFlightsDAOFactory {
  def createDAO: FlightsDAO = FilesFlightsDAO(files)
}

object FileFlightsDAOFactory {
  def apply(): FileFlightsDAOFactory = new FileFlightsDAOFactory()
}