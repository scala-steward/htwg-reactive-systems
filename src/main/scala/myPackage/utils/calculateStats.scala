package myPackage
package utils

import myPackage.models.Country
import scala.collection.mutable.Map


def getSpreadStreamingProvider(countries:Array[Country]):Map[String,Int] = {
    // get streaming providers
    val streamingProvider = getStreamingProvider(countries)
  // create empty Key VAlue storage of streaming providers and percentage
    var streamingProviderPercentage = Map[String,Int]()
    streamingProvider.foreach((streamingProvider) => {
        val amountStreamingProvider = countries.filter((country) => {
            country.services.contains(streamingProvider)
        }).length
        val percentageStreamingProvider = (amountStreamingProvider * 100) / countries.length
        streamingProviderPercentage(streamingProvider) = percentageStreamingProvider
    })
    return streamingProviderPercentage

}

def getStreamingProvider(countries:Array[Country]):Array[String] = {
    // create empty Array of streaming providers
    var streamingProvider = Array[String]()
    countries.foreach((country) => {
        country.services.foreach((service) => {
            if (!streamingProvider.contains(service)){
                streamingProvider = streamingProvider :+ service
            }
        })
    })
    return streamingProvider
}