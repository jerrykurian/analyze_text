package models

import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._
import java.util.Date

object IncomingMessageLog{
   val parser = {
      get[Pk[Long]]("id") ~
      get[String]("message") ~
      get[String]("mobile_number") ~
      get[Date]("created_at") map {
      case pk ~ message ~ mobileNumber ~ createdDate => IncomingMessageLog(pk, message, mobileNumber, createdDate)
    }
  }
   
   def save(incomingMessage:IncomingMessageLog){
    DB.withConnection {implicit c =>
	   SQL("""insert into incoming_message_logs(message, mobile_number, created_at, updated_at) 
	       values ({message},{mobileNumber},{createdDate},{updatedDate})""").
	   on("message" -> incomingMessage.message, "mobileNumber"->incomingMessage.mobileNumber, 
	       "createdDate" -> incomingMessage.createdDate,
	       "updatedDate" -> incomingMessage.createdDate).executeInsert()
	}
   }
}

case class IncomingMessageLog(id:Pk[Long], message:String, mobileNumber:String, createdDate:Date)