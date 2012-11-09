package models

import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._
import java.util.HashMap
import play.api.Logger
import java.util.Date

object AiaiooFailure {
  val parser = {
    get[Pk[Long]]("id") ~
      get[String]("text") ~
      get[String]("source_number") ~
      get[Int]("retried") ~
      get[Date]("created_at")map {
        case id ~ text ~ source ~ retried ~ createdDate=> AiaiooFailure(id, text, source, retried, createdDate)
      }
  }

  def findAllNotRetried() = {
    DB.withConnection { implicit c =>
      SQL("Select * from aiaioo_failures where retried=0")
        .as(parser *)
    }
  }

  def save(aiaiooFailure: AiaiooFailure) = {
    val createdDate = aiaiooFailure.createdDate
    val id: Long = DB.withConnection { implicit c =>
      SQL("""insert into aiaioo_failures(text,source_number,created_at,updated_at,retried) 
	       values ({text},{source},{createdDate},{updatedDate},{retried})""").
        on("text" -> aiaiooFailure.text,
          "source" -> aiaiooFailure.sourceNumber,
          "createdDate" -> createdDate,
          "updatedDate" -> createdDate,
          "retried" -> 0).executeInsert()
    } match {
      case Some(long) => long
      case _ => -1
    }
    var failure = AiaiooFailure(if (id != -1) new Id(id) else null, aiaiooFailure.text,
      aiaiooFailure.sourceNumber, 0, createdDate)
    failure
  }

  def retried(aiaiooFailureId: Long) {
	val updatedDate = new Date()
    DB.withConnection { implicit c =>
      SQL("""update aiaioo_failures set retried=1, updated_at={updatedDate} where id={id}""").
        on("updatedDate" -> updatedDate, "id" -> aiaiooFailureId).executeUpdate()
    }
  }
}

case class AiaiooFailure(id: Pk[Long], text: String, sourceNumber: String, retried: Int, createdDate:Date)