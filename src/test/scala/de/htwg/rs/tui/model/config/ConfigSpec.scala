package de.htwg.rs.tui.model.config

import de.htwg.rs.tui.config.Config

import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ConfigSpec extends AnyWordSpec with Matchers:

  "Config" when {
    "calling fromEnv" should {
      "return valid configuration if required environment variables are set" in {
        val env = Map("API_URL" -> "example.com", "API_TOKEN" -> "secret_token")
        val configResult = Config.fromEnv(env)

        configResult shouldBe a[Right[_, _]]
        val Right(config) = configResult: @unchecked
        config.apiUrl should be("example.com")
        config.apiToken should be("secret_token")
      }

      "report an error when 'API_URL' is missing" in {
        val env = Map("API_TOKEN" -> "secret_token")
        val configResult = Config.fromEnv(env)

        configResult shouldBe a[Left[_, _]]
        val Left(errorMessage) = configResult: @unchecked
        errorMessage should include("API_URL not set")
      }

      "report an error when 'API_TOKEN' is missing" in {
        val env = Map("API_URL" -> "example.com")
        val configResult = Config.fromEnv(env)

        configResult shouldBe a[Left[_, _]]
        val Left(errorMessage) = configResult: @unchecked
        errorMessage should include("API_TOKEN not set")
      }

      "report an error when both 'API_URL' and 'API_TOKEN' are missing" in {
        val env = Map.empty[String, String]
        val configResult = Config.fromEnv(env)

        configResult shouldBe a[Left[_, _]]
        val Left(errorMessage) = configResult: @unchecked
        errorMessage should include("API_URL not set")
      }
    }
  }
