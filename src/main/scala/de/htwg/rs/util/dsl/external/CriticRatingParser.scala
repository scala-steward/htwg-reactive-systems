package de.htwg.rs.util.dsl.external

import de.htwg.rs.util.dsl.internal.{CriticRating, RatingCategory}

import java.time.LocalDate
import scala.util.parsing.combinator.*

object CriticRatingParser extends JavaTokenParsers:
  private def movieName: Parser[String] = stringLiteral ^^ { s =>
    s.substring(1, s.length - 1)
  }

  private def rating: Parser[Int] = "rated" ~> wholeNumber <~ "Stars" ^^ {
    _.toInt
  }

  private def reviewAuthor: Parser[String] = "by" ~> stringLiteral ^^ { s =>
    s.substring(1, s.length - 1)
  }

  private def reviewDate: Parser[LocalDate] =
    "on" ~> """"\d{4}-\d{2}-\d{2}"""".r ^^ { s =>
      LocalDate.parse(s.substring(1, s.length - 1))
    }

  private def review: Parser[CriticRating] =
    movieName ~ rating ~ reviewAuthor ~ reviewDate.? ^^ {
      case name ~ rate ~ author ~ date =>
        CriticRating(name, rate, RatingCategory.Stars, Some(author), date)
    }

  private def reviews: Parser[List[CriticRating]] = rep(review)

  def parse(input: String): ParseResult[List[CriticRating]] =
    parseAll(reviews, input)
