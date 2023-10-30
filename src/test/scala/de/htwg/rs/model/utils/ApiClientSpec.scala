package de.htwg.rs.model.utils

import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class ApiClientSpec extends AnyWordSpec:
  val validToken = "***REMOVED***"
  val invalidToken = "invalid_token"
  val validHost = "streaming-availability.p.rapidapi.com"

  "An ApiClient" when {
    "constructed with a valid token and a valid host" should {
      "return a list of countries on a successful GET request" in {
        val apiClient = new ApiClient(validToken, validHost)
        val result = apiClient.getCountries
        result.isSuccess should be(true)
        result.get.length should be > 0
      }

      "return a Failure on an unsuccessful GET request with an invalid host" in {
        val apiClient = new ApiClient(validToken, "invalid-host")
        val result = apiClient.getCountries
        result.isFailure should be(true)
      }
    }

    "constructed with an invalid token and a valid host" should {
      "return a Failure on a GET request" in {
        val apiClient = new ApiClient(invalidToken, validHost)
        val result = apiClient.getCountries
        result.isFailure should be(true)
      }
    }
  }
