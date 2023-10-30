package de.htwg.rs

import de.htwg.rs.controller.App
import de.htwg.rs.model.utils.ApiClient
import de.htwg.rs.view.ui

import java.time.{Duration, Instant}

import tui.*
import tui.crossterm.CrosstermJni

object Scala:
  def main(args: Array[String]): Unit = withTerminal { (jni, terminal) =>
    val apiClient =
      ApiClient("***REMOVED***")
    // create app and run it
    val tick_rate = Duration.ofMillis(250)
    // getting countries
    val countries = apiClient.getCountries();
    val app = App(
      title = "Movies Movies Movies!!",
      enhanced_graphics = true,
      countries = countries
    )

    run_app(terminal, app, tick_rate, jni)
  }
/* @main def hello: Unit = {
        println("Hello world!")
        println("wtf")
     // get countries from api
        println("Getting countries")
        val countries = getCountries();
        if (countries.isLeft){
          println("Success getting countries")
          // get streaming providers spread
          val countriesLeft = countries.left.get
          val streamingProvider = getStreamingProvider(countriesLeft)
          // loop over streamingproviders and print them
          streamingProvider.foreach((streamingProvider) =>
            println("name: "+ streamingProvider.name)
            println("id: "+streamingProvider.id)
            println("url: "+streamingProvider.url)
            streamingProvider.supportedStreamingTypes.foreach((key, value) =>
              println("supported Type: "+key)
              println("value: "+value)
            )
            println("--------------------")
            )
          val streamingProviderSupportedStreamingTypesPercentage = getPaymentModelsSpreadFromStreamingProvider(streamingProvider)
          streamingProviderSupportedStreamingTypesPercentage.foreach((key, value) =>
            println("supported Type: "+key)
            println("value %: "+value)
          )
        }
        else {
          println("Error getting countries")
        }
        println("End of program")
    }  */

def run_app(
    terminal: Terminal,
    app: App,
    tick_rate: java.time.Duration,
    jni: CrosstermJni
): Unit =
  val last_tick = Instant.now()

  def elapsed = java.time.Duration.between(last_tick, java.time.Instant.now())

  def timeout =
    val timeout = tick_rate.minus(elapsed)
    new tui.crossterm.Duration(timeout.toSeconds, timeout.getNano)

  while true do
    terminal.draw(f => ui.draw(f, app))

    if jni.poll(timeout) then
      jni.read() match
        case key: tui.crossterm.Event.Key =>
          key.keyEvent.code match
            case char: tui.crossterm.KeyCode.Char => app.on_key(char.c())
            case _: tui.crossterm.KeyCode.Left    => app.on_left()
            // case _: tui.crossterm.KeyCode.Up      => app.on_up()
            case _: tui.crossterm.KeyCode.Right => app.on_right()
            // case _: tui.crossterm.KeyCode.Down    => app.on_down()
            case _ => ()
        case _ => ()
    if app.should_quit then return
