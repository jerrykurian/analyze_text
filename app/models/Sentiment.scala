package models

import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._
import java.util.Date

object Sentiment {
  val parser = {
    get[Pk[Long]]("id") ~
      get[String]("name") ~
      get[Int]("value") map {
        case pk ~ name ~ value => Sentiment(pk, name, value)
      }
  }

  def findById(id: Pk[Long]): Sentiment = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from sentiments where id = {id}").on("id" -> id.get).using(parser).single()
    }
  }

  def findByName(name: String): Sentiment = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from sentiments where name like {name}").on("name" -> name).using(parser).single()
    }
  }

  def findByValue(value: Int): Sentiment = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from sentiments where value = {value}").on("value" -> value).using(parser).single()
    }
  }

  def nameToValue(name: String) = {
    name match {
      case "sad" => -2
      case "negative" => -1
      case "neutral" => 0
      case "positive" => 1
      case "happy" => 2
      case "Positive Weak" => 1
      case "Positive Strong" => 2
      case "Neutral" => 0
      case "Negative Weak" => -1
      case "Negative Strong" => -2
      case _ => 0
    }
  }

  def nameToScore(name: String) = {
    name match {
      case "sad" => 0
      case "negative" => 25
      case "neutral" => 50
      case "positive" => 75
      case "happy" => 100
      case "Positive Weak" => 75
      case "Positive Strong" => 100
      case "Neutral" => 50
      case "Negative Weak" => 25
      case "Negative Strong" => 0
      case _ => 0
    }
  }
}

case class Sentiment(id: Pk[Long], name: String, value: Int) {
  def score = value match {
    case -2 => 0
    case -1 => 25
    case 0 => 50
    case 1 => 75
    case 2 => 100
    case _ => 0
  }
}