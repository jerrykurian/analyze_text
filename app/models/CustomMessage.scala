package models
import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._
import java.util.Date

object CustomMessage{
  val parser = {
      get[Pk[Long]]("id") ~
      get[String]("message") ~
      get[Pk[Long]]("sentiment_id") ~ 
      get[Option[Pk[Long]]]("branch_id") ~
      get[Option[Pk[Long]]]("business_id") map {
      case pk ~ message ~ sentiment ~ branch ~ business 
      	=> CustomMessage(pk, message, Sentiment.findById(sentiment),
      	    if (branch.getOrElse(null) != null) Some(Branch.findById(branch.get)) else None,
      	    if (business.getOrElse(null) != null) Some(Business.findById(business.get)) else None)
    }
  }
  
  def findMessageForSentiment(sentiment:Sentiment, branch:Branch, business:Business):CustomMessage={
    DB.withConnection {
      implicit connection =>
        var message = SQL("""select * from custom_messages 
        			where sentiment_id = {sentiment} and branch_id = {branch} and business_id = {business}""")
        			.on("sentiment" -> sentiment.id, "branch" -> branch.id, "business" -> business.id).using(parser).singleOpt().getOrElse(null)
        if(message == null){
          message = SQL("""select * from custom_messages 
        			where sentiment_id = {sentiment} and business_id = {business} and branch_id is null""")
        			.on("sentiment" -> sentiment.id, "business" -> business.id).using(parser).singleOpt().getOrElse(null)
        }else{
          return message
        }
        if(message == null){
          message = SQL("""select * from custom_messages 
        			where sentiment_id = {sentiment} and branch_id is null and business_id is null""")
        			.on("sentiment" -> sentiment.id).using(parser).singleOpt().getOrElse(null)
        }
        message
    }
  }
  
  def save(customMessage: CustomMessage){
    val createdDate = new Date()
    val id:Long = DB.withConnection {implicit c =>
	   SQL("""insert into custom_messages(message,sentiment_id,branch_id,business_id,created_at,updated_at)
			   		values ({message},{sentiment},{branch},{business},{createdDate},{updatedDate})""").
	   on("message" -> customMessage.message,"sentiment" -> customMessage.sentiment.id, 
	       "branch" -> customMessage.branch.map{_.id}.getOrElse(null),"business" -> customMessage.business.map{_.id}.getOrElse(null),
	       "createdDate" -> createdDate,"updatedDate" -> createdDate).executeInsert()
	} match {
	  case Some(long) => long
	  case _ => -1
	}
  }
}

case class CustomMessage(id:Pk[Long], message:String, sentiment:Sentiment, branch:Option[Branch],
    business:Option[Business])
