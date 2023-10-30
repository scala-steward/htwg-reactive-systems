package de.htwg.rs.model.utils

import de.htwg.rs.model.models.Country

import scala.util.Try

import scalaj.http.{Http, HttpResponse}

class ApiClient(
    token: String,
    host: String = "streaming-availability.p.rapidapi.com"
):
  private def get(url: String): Try[HttpResponse[String]] =
    Try {
      Http(url)
        .header("X-RapidAPI-Key", token)
        .header("X-RapidAPI-Host", host)
        .method("GET")
        .asString
    }

  def getCountries: Try[Array[Country]] =
    for
      response <- get(s"https://$host/countries")
      countries <- Try {
        val jsonString = response.body
        val result = ujson.read(jsonString)("result").obj
        result.map { case (key, value) =>
          val services = value("services")
          val servicesKeys = services.obj.keys.toList
          val countryName = value("name")
          val servicesRaw = value("services")
          Country(countryName.str, key, servicesRaw, servicesKeys)
        }.toArray
      }
    yield countries
