package de.htwg.rs.models

case class Country(
    name: String,
    code: String,
    servicesRaw: ujson.Value,
    servicesAsList: List[String]
)
case class StreamingProvider(
    name: String,
    id: String,
    url: String,
    supportedStreamingTypes: Map[String, Boolean]
)
