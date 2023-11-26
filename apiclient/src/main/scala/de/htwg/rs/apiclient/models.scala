package de.htwg.rs.apiclient

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

enum ChangeType:
  case New, Updated, Removed

enum ServiceChange:
  case Netflix, `Prime.subscription`, `Prime.rent`, `prime.buy,apple.rent`,
    `apple.buy`, hbo, `prime.addon.hbomaxus`, `hulu.subscription`,
    `hulu.addon.hbo`, `apple.addon`, `peacock.free`

enum TargetType:
  case Movie, Show, Series, Season, Episode
