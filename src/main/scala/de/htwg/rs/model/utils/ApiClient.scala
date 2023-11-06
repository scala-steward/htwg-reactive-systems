package de.htwg.rs.model.utils

import de.htwg.rs.model.models.{ChangeType, Country, ServiceChange, TargetType}

import scala.util.Try

import scalaj.http.{Http, HttpResponse}

class ApiClient(
    token: String,
    host: String = "streaming-availability.p.rapidapi.com"
):
  def getCountries: Try[List[Country]] =
    for
      response <- get(s"$host/countries", Map.empty)
      countries <- parseCountries(response.body)
    yield countries

  def getChanges(
      changeType: ChangeType,
      service: ServiceChange,
      targetType: TargetType,
      countryCode: String,
      cursor: Option[String]
  ): Try[String] =
    var params: Map[String, String] = Map[String, String](
      "change_type" -> changeType.toString.toLowerCase(),
      "services" -> service.toString.toLowerCase(),
      "target_type" -> targetType.toString.toLowerCase(),
      "country" -> countryCode
    )
    if cursor.isDefined then params += ("cursor" -> cursor.get)
    // TODO: parse response
    for response <- get(s"$host/changes", params)
    yield response.body

  private def get(
      url: String,
      params: Map[String, String]
  ): Try[HttpResponse[String]] = Try {
    Http(url)
      .header("X-RapidAPI-Key", token)
      .header("X-RapidAPI-Host", host)
      .method("GET")
      .params(params)
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
