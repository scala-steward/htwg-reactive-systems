package de.htwg.rs.model.utils

import de.htwg.rs.model.models.Country

import scalaj.http.Http

class ApiClient(
    token: String,
    host: String = "streaming-availability.p.rapidapi.com"
):
  private def get(url: String): Either[String, String] =
    try
      val result = Http(url)
        .header("X-RapidAPI-Key", token)
        .header("X-RapidAPI-Host", host)
        .method("GET")
        .asString

      if result.isError then Left(result.body)
      else Right(result.body)
    catch case e: Exception => return Left(e.toString)

  def getCountries(): Either[String, Array[Country]] =
    val countries = get(s"https://$host/countries")

    if countries.isRight then
      val countriesSucess = countries.right.get
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
      Right(countriesList)
    else Left("Error getting countries")
