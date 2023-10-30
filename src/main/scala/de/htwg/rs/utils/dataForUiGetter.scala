package de.htwg.rs.utils

import de.htwg.rs.getCountries_json
import de.htwg.rs.models.Country

def getCountries(): Either[Array[Country], String] =
  val countries = getCountries_json()
  if countries.isLeft then
    val countriesSucess = countries.left.get
    val countriesJson = ujson.read(countriesSucess)("result")
    val countriesObje = countriesJson.obj
    // create empty Array of countries
    var countriesList = Array[Country]()
    countriesObje.foreach((key, value) =>
      // extract values from country
      val services = value("services")
      // get keys from services and convert to List
      val servicesKeys = services.obj.keys.toList
      val countryName = value("name")
      val servicesRaw = value("services")
      // add country to countriesList
      val country = Country(countryName.str, key, servicesRaw, servicesKeys)
      countriesList = countriesList :+ country
    )
    return Left(countriesList)
  else return Right("Error getting countries")
