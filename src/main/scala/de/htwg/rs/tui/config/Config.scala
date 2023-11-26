package de.htwg.rs.tui.config

case class Config(apiUrl: String, apiToken: String)

object Config:
  /** Reads configuration from the given environment.
    *
    * @param env
    *   The environment to read the configuration from, usually `sys.env`.
    * @return
    *   The configuration or an error message.
    */
  def fromEnv(env: Map[String, String]): Either[String, Config] =
    for
      apiUrl <- env.get("API_URL").toRight("API_URL not set")
      apiToken <- env.get("API_TOKEN").toRight("API_TOKEN not set")
    yield Config(apiUrl, apiToken)
