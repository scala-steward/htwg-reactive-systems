import munit.FunSuite

class MainTest extends FunSuite {
  test("hello should print 'Hello world!' to the console") {
    val outputStream = new java.io.ByteArrayOutputStream()
    Console.withOut(outputStream) {
      hello()
    }
    val output = outputStream.toString.trim
    assert(output == "Hello world!")
  }
}
