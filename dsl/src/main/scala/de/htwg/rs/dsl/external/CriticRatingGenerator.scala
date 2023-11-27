package de.htwg.rs.dsl.external

import scala.util.Random

object CriticRatingGenerator:
  private def getRandomElement[T](list: List[T])(using random: Random): T =
    list(random.nextInt(list.size))

  private def movieTitle(using random: Random): String =
    val movies =
      List("The Dark Knight", "Forrest Gump", "The Shawshank Redemption")
    getRandomElement(movies)

  private def rating(using random: Random): String =
    random.nextInt(2) match
      case 0 => s"${random.between(1, 6)} Stars"
      case 1 => s"${random.between(1, 101)}%"

  private def reviewerName(using random: Random): String =
    val givenNames =
      List("John", "Jane", "Jack", "Jill", "James", "Jenny", "Jules", "Judy")
    val surnames =
      List("Smith", "Doe", "Johnson", "Brown", "Jones", "Miller", "Davis")
    s"${getRandomElement(givenNames)} ${getRandomElement(surnames)}"

  private def date(using random: Random): String =
    val year = random.between(2000, 2023)
    val month = f"${random.between(1, 13)}%02d"
    val day = f"${random.between(1, 29)}%02d"
    s"$year-$month-$day"

  def generate(using random: Random): String =
    s""""${movieTitle}" rated ${rating} by "${reviewerName}" on "${date}""""
