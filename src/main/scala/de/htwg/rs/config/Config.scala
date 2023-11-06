package de.htwg.rs.config

case class Config(apiUrl: String, apiToken: String)

val DEFAULT_API_URL = "streaming-availability.p.rapidapi.com"
val DEFAULT_API_TOKEN = "***REMOVED***"

def readConfigFromEnv(env: Map[String, String]): Config =
  Config(
    apiUrl = env.getOrElse("API_URL", DEFAULT_API_URL),
    apiToken = env.getOrElse("API_TOKEN", DEFAULT_API_TOKEN)
  )
