package de.htwg.rs.apiclient

import scala.util.{Failure, Success, Try}

import scalaj.http.{Http, HttpResponse}

class ApiClient(token: String, host: String):
  def getCountries: Try[List[Country]] =
    for
      response <- get("/countries", Map.empty)
      apiResponse <- ApiResponse.parse(response.body, parseCountries)
    yield apiResponse.result

  def getChanges(
      changeType: ChangeType,
      service: ServiceChange,
      targetType: TargetType,
      countryCode: String,
      cursor: Option[String]
  ): Try[ApiResponse[ujson.Value]] =
    var params: Map[String, String] = Map(
      "change_type" -> changeType.toString.toLowerCase(),
      "services" -> service.toString.toLowerCase(),
      "target_type" -> targetType.toString.toLowerCase(),
      "country" -> countryCode
    )
    if cursor.isDefined then params += ("cursor" -> cursor.get)
    for
      response <- get("/changes", params)
      apiResponse <- ApiResponse.parse(response.body, Success(_))
    yield apiResponse

  private def get(
      path: String,
      params: Map[String, String]
  ): Try[HttpResponse[String]] =
    Try {
      Http(s"${host.stripSuffix("/")}$path")
        .header("X-RapidAPI-Key", token)
        .header("X-RapidAPI-Host", host)
        .method("GET")
        .params(params)
        .asString
    }

case class ApiResponse[T](
    result: T,
    hasMore: Boolean,
    nextCursor: Option[String]
)

private object ApiResponse:
  def parse[T](
      json: String,
      parser: ujson.Value => Try[T]
  ): Try[ApiResponse[T]] = Try {
    val value = ujson.read(json)
    val hasMore = value.obj.get("hasMore").exists(_.bool)
    val nextCursor = value.obj
      .get("nextCursor")
      .map(_.str)
      .flatMap(cursor => if cursor.isEmpty then None else Some(cursor))
    val result = value("result")

    parser(result) match
      case Success(parsed) => ApiResponse(parsed, hasMore, nextCursor)
      case Failure(ex)     => throw ex
  }

private def parseCountries(value: ujson.Value): Try[List[Country]] = Try {
  value.obj.map { case (key, value) =>
    val services = value("services")
    val servicesKeys = services.obj.keys.toList
    val countryName = value("name")
    val servicesRaw = value("services")
    Country(countryName.str, key, servicesRaw, servicesKeys)
  }.toList
}
