package de.htwg.rs.model.config

import de.htwg.rs.config.readConfigFromEnv

import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ConfigSpec extends AnyWordSpec with Matchers:

  "Config" should {

    "read valid configuration from environment variables" in {
      val env = Map("API_URL" -> "example.com", "API_TOKEN" -> "secret_token")
      val configResult = readConfigFromEnv(env)

      configResult shouldBe a[Right[_, _]]
      val Right(config) = configResult
      config.apiUrl should be("example.com")
      config.apiToken should be("secret_token")
    }

    "report an error when 'API_URL' is missing" in {
      val env = Map("API_TOKEN" -> "secret_token")
      val configResult = readConfigFromEnv(env)

      configResult shouldBe a[Left[_, _]]
      val Left(errorMessage) = configResult
      errorMessage should include("API_URL not set")
    }

    "report an error when 'API_TOKEN' is missing" in {
      val env = Map("API_URL" -> "example.com")
      val configResult = readConfigFromEnv(env)

      configResult shouldBe a[Left[_, _]]
      val Left(errorMessage) = configResult
      errorMessage should include("API_TOKEN not set")
    }

    "report an error when both 'API_URL' and 'API_TOKEN' are missing" in {
      val env = Map.empty[String, String]
      val configResult = readConfigFromEnv(env)

      configResult shouldBe a[Left[_, _]]
      val Left(errorMessage) = configResult
      errorMessage should include("API_URL not set")
    }
  }
