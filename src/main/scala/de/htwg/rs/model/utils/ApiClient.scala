package de.htwg.rs.model.utils

import de.htwg.rs.model.models.Country

import scala.util.Try

import scalaj.http.{Http, HttpResponse}

class ApiClient(
    token: String,
    host: String = "streaming-availability.p.rapidapi.com"
):
  def getCountries: Try[List[Country]] =
    for
      response <- get(s"https://$host/countries")
      countries <- parseCountries(response.body)
    yield countries

  private def get(url: String): Try[HttpResponse[String]] = Try {
    Http(url)
      .header("X-RapidAPI-Key", token)
      .header("X-RapidAPI-Host", host)
      .method("GET")
      .asString
  }

private def parseCountries(jsonString: String): Try[List[Country]] = Try {
  ujson
    .read(jsonString)("result")
    .obj
    .map { case (key, value) =>
      val services = value("services")
      val servicesKeys = services.obj.keys.toList
      val countryName = value("name")
      val servicesRaw = value("services")
      Country(countryName.str, key, servicesRaw, servicesKeys)
    }
    .toList
}
