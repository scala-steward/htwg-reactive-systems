package de.htwg.rs.utils

import de.htwg.rs.utils.getCountries

import scala.runtime.stdLibPatches.Predef.assert

import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*

class DataForUiGetterSpec extends AnyFlatSpec with should.Matchers:

  "getCountries" should "return an array of countries" in {
    val countries = getCountries()
    countries.isLeft should be(true)
    val countriesList = countries.left.get
    countriesList.length should be > 0
  }
