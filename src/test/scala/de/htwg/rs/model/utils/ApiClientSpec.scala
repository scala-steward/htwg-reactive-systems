package de.htwg.rs.model.utils

import de.htwg.rs.model.models.{ChangeType, ServiceChange, TargetType}

import scala.io.Source

import okhttp3.mockwebserver.{MockResponse, MockWebServer, RecordedRequest}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ApiClientSpec extends AnyWordSpec with Matchers:

  "An ApiClient" when {
    "calling getCountries" should {
      "return a list of countries" in {
        val mockResponse = new MockResponse()
        mockResponse.setResponseCode(200)
        mockResponse.setBody(
          Source.fromResource("responses/countries/countries.json").mkString
        )

        val result = withMockApi(
          pre = server => server.enqueue(mockResponse),
          block = apiClient => apiClient.getCountries
        )

        result.isSuccess shouldBe true
        result.get.length shouldBe 58
        result.get.head.name shouldBe "United Emirates"
        result.get.head.code shouldBe "ae"
        result.get.head.servicesAsList shouldBe List(
          "apple",
          "curiosity",
          "mubi",
          "netflix",
          "prime",
          "zee5"
        )
      }

      "return a failure on non-2xx response codes" in {
        val mockResponse = new MockResponse()
        mockResponse.setResponseCode(400)

        val result = withMockApi(
          pre = server => server.enqueue(mockResponse),
          block = apiClient => apiClient.getCountries,
          post = request =>
            request.getHeader("X-RapidAPI-Key") shouldBe "token"
            request.getPath shouldBe "/countries"
        )

        result.isFailure shouldBe true
      }
    }

    "calling getChanges" should {
      "return a list of changes" in {
        val mockResponse = new MockResponse()
        mockResponse.setResponseCode(200)
        mockResponse.setBody(
          Source.fromResource("responses/changes/1.json").mkString
        )

        val result = withMockApi(
          pre = server => server.enqueue(mockResponse),
          block = apiClient =>
            apiClient.getChanges(
              ChangeType.New,
              ServiceChange.Netflix,
              TargetType.Show,
              "de",
              None
            ),
          post = request =>
            request.getHeader("X-RapidAPI-Key") shouldBe "token"
            request.getPath shouldBe "/changes?change_type=new&services=netflix&target_type=show&country=de"
        )

        result.isSuccess shouldBe true
      }
    }
  }

private def withMockApi[T](
    pre: MockWebServer => Unit,
    block: ApiClient => T,
    post: RecordedRequest => Unit = _ => ()
): T =
  val server = new MockWebServer()
  server.start()

  val result =
    try
      pre(server)
      block(ApiClient("token", server.url("/").toString))
    finally server.shutdown()

  post(server.takeRequest())
  result
