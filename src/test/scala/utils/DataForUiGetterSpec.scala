package utils

import myPackage.utils.getCountries
import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*

import scala.runtime.stdLibPatches.Predef.assert

class DataForUiGetterSpec extends AnyFlatSpec with should.Matchers:

  "getCountries" should "return an array of countries" in {
    val countries = getCountries()
    countries.isLeft should be(true)
    val countriesList = countries.left.get
    countriesList.length should be > 0
  }
