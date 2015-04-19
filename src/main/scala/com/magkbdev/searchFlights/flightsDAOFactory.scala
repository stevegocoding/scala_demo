package com.magkbdev.searchFlights

import java.security.InvalidParameterException

object FactoryType extends Enumeration {
  val FilesDAO = Value
}

trait AbastractFlightsDAOFactory {
  def createDAO: FlightsDAO
}

object AbastractFlightsDAOFactory {

  def apply(factoryType: FactoryType.Value) = {
    factoryType match {
      case FactoryType.FilesDAO => new FileFlightsDAOFactory()
      case _ => throw new InvalidParameterException("No factory type found!")
    }
  }
}

class FileFlightsDAOFactory(var files: List[String] = List()) extends AbastractFlightsDAOFactory {
  def createDAO: FlightsDAO = new FilesFlightsDAO(files)
}
