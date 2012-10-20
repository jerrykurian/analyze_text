package models
import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._
import java.util.Date

object OutgoingMessageLog {
  val parser = {
    get[Pk[Long]]("id") ~
      get[String]("message") ~
      get[String]("destination_mobile_number") ~
      get[String]("message_status") ~
      get[Pk[Long]]("user_id") ~
      get[Date]("created_at") map {
        case pk ~ message ~ mobileNumber ~ status ~ user ~ createdDate => OutgoingMessageLog(pk, message, mobileNumber, status,
            if (user.getOrElse(null) != null) User.findById(user) else null, createdDate)
      }
  }

  def save(outgoingMessage: OutgoingMessageLog) {
    DB.withConnection { implicit c =>
      SQL("""insert into outgoing_message_logs(message, destination_mobile_number, message_status, user_id, created_at, updated_at) 
	       values ({message},{mobileNumber},{status},{user},{createdDate},{updatedDate})""").
        on("message" -> outgoingMessage.message, "mobileNumber" -> outgoingMessage.mobileNumber,
          "status" -> outgoingMessage.status, 
          "user" -> outgoingMessage.user.id,
          "createdDate" -> outgoingMessage.createdDate,
          "updatedDate" -> outgoingMessage.createdDate).executeInsert()
    }
  }
}

case class OutgoingMessageLog(id: Pk[Long], message: String, mobileNumber: String, status: String, user:User, createdDate: Date) 