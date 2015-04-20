package com.magkbdev.searchFlights

import java.security.InvalidParameterException

object FactoryType extends Enumeration {
  val FilesDAO = Value
}

trait AbstractFlightsDAOFactory {
  def createDAO: FlightsDAO
}

object AbstractFlightsDAOFactory {

  def apply(factoryType: FactoryType.Value) = {
    factoryType match {
      case FactoryType.FilesDAO => new FileFlightsDAOFactory()
      case _ => throw new InvalidParameterException("No factory type found!")
    }
  }
}

class FileFlightsDAOFactory(var files: List[String] = List()) extends AbstractFlightsDAOFactory {
  def createDAO: FlightsDAO = new FilesFlightsDAO(files)
}
