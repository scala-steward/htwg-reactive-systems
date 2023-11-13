package de.htwg.rs

import de.htwg.rs.config.Config
import de.htwg.rs.controller.App
import de.htwg.rs.model.models.{ChangeType, TargetType}
import de.htwg.rs.model.utils.{getCountChangesForEveryService, ApiClient}
import de.htwg.rs.view.ui

import java.time.{Duration, Instant}
import scala.sys.exit

import tui.*
import tui.crossterm.CrosstermJni

object Scala:
  def main(args: Array[String]): Unit = withTerminal { (jni, terminal) =>
    val apiClient = Config.fromEnv(sys.env) match
      case Right(cfg) => ApiClient(token = cfg.apiToken, host = cfg.apiUrl)
      case Left(error) =>
        println(s"Error reading config: $error")
        return

    // create app and run it
    val tickRate = Duration.ofMillis(250)
    // getting countries
    val countries = apiClient.getCountries;
    val amountChangesNew =
      getCountChangesForEveryService(
        apiClient,
        ChangeType.New,
        TargetType.Movie,
        "de"
      )
    val amountChangesRemoved =
      getCountChangesForEveryService(
        apiClient,
        ChangeType.Removed,
        TargetType.Movie,
        "de"
      )
    val app = App(
      title = "Movies Movies Movies!!",
      countries = countries,
      countChangesProvider = amountChangesNew,
      countRemovedChangesProvider = amountChangesRemoved
    )

    runApp(terminal, app, tickRate, jni)
  }
/*@main def hello: Unit =
  println("Hello world!")
  val config = readConfigFromEnv(sys.env)
  val apiClient = ApiClient(token = config.apiToken, host = config.apiUrl)
  // get countries from api
  println("Getting countries")
  val countries = apiClient.getCountries;
  if countries.isSuccess then
    println("Success getting countries")
    // get streaming providers spread
    val countriesLeft = countries.get
    val streamingProvider = getStreamingProvider(countriesLeft)
    // loop over streamingproviders and print them
    streamingProvider.foreach((streamingProvider) =>
      println("name: " + streamingProvider.name)
      println("id: " + streamingProvider.id)
      println("url: " + streamingProvider.url)
      streamingProvider.supportedStreamingTypes.foreach((key, value) =>
        println("supported Type: " + key)
        println("value: " + value)
      )
      println("--------------------")
    )
    val streamingProviderSupportedStreamingTypesPercentage =
      getPaymentModelsSpreadFromStreamingProvider(streamingProvider)
    streamingProviderSupportedStreamingTypesPercentage.foreach((key, value) =>
      println("supported Type: " + key)
      println("value %: " + value)
    )
  else println("Error getting countries: " + countries.failed.get)
  println("End of program") */

def runApp(
    terminal: Terminal,
    app: App,
    tickRate: java.time.Duration,
    jni: CrosstermJni
): Unit =
  val lastTick = Instant.now()

  def elapsed = java.time.Duration.between(lastTick, java.time.Instant.now())

  def timeout =
    val timeout = tickRate.minus(elapsed)
    new tui.crossterm.Duration(timeout.toSeconds, timeout.getNano)

  while true do
    terminal.draw(f => ui.draw(f, app))

    if jni.poll(timeout) then
      jni.read() match
        case key: tui.crossterm.Event.Key =>
          key.keyEvent.code match
            case char: tui.crossterm.KeyCode.Char => app.onKey(char.c())
            case _: tui.crossterm.KeyCode.Left    => app.onLeft()
            // case _: tui.crossterm.KeyCode.Up      => app.onUp()
            case _: tui.crossterm.KeyCode.Right => app.onRight()
            // case _: tui.crossterm.KeyCode.Down    => app.onDown()
            case _ => ()
        case _ => ()
    if app.shouldQuit then return
