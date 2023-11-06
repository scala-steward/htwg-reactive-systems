package de.htwg.rs.model.config

import de.htwg.rs.config.{readConfigFromEnv, DEFAULT_API_TOKEN, DEFAULT_API_URL}

import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ConfigSpec extends AnyWordSpec with Matchers:

  "Config" should {

    "read configuration from environment variables with valid values" in {
      val env = Map("API_URL" -> "example.com", "API_TOKEN" -> "secret_token")
      val config = readConfigFromEnv(env)

      config.apiUrl should be("example.com")
      config.apiToken should be("secret_token")
    }

    "read configuration from environment variables with missing values" in {
      val env = Map.empty[String, String]
      val config = readConfigFromEnv(env)

      config.apiUrl should be(DEFAULT_API_URL)
      config.apiToken should be(DEFAULT_API_TOKEN)
    }

    "read configuration from environment variables with missing API_TOKEN" in {
      val env = Map("API_URL" -> "example.com")
      val config = readConfigFromEnv(env)

      config.apiUrl should be("example.com")
      config.apiToken should be(DEFAULT_API_TOKEN)
    }

    "read configuration from environment variables with missing API_URL" in {
      val env = Map("API_TOKEN" -> "secret_token")
      val config = readConfigFromEnv(env)

      config.apiUrl should be(DEFAULT_API_URL)
      config.apiToken should be("secret_token")
    }
  }
