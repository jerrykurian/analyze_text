package models
import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._
import java.util.Date

object Feedback{
  val parser = {
      get[Pk[Long]]("id") ~
      get[String]("message") ~
      get[Option[Pk[Long]]]("sentiment_id") ~
      get[Pk[Long]]("user_id") ~
      get[Pk[Long]]("branch_id") ~
      get[Date]("created_at") ~
      get[Int]("sentiment_score") map{
      case pk ~ message ~ sentiment ~ user ~ branch ~ createdDate ~ score => Feedback(pk , message, 
          User.findById(user),
          if (sentiment.getOrElse(null) != null) Some(Sentiment.findById(sentiment.get)) else None, 
          Branch.findById(branch), createdDate, 
          score)
    }
  }
  
  def save(feedback:Feedback)={
    val id:Long = DB.withConnection {implicit c =>
	   SQL("""insert into feedbacks(message,sentiment_id,user_id,branch_id,created_at,updated_at,sentiment_score)
			   		values ({message},{sentiment},{user},{branch},{createdDate},{updatedDate},{score})""").
	   on("message" -> feedback.message, "sentiment" -> feedback.sentiment.map{_.id}.getOrElse(null),
	       "user"->feedback.user.id, "branch" -> feedback.branch.id,
	       "createdDate" -> feedback.createdDate,"updatedDate" -> feedback.createdDate,
	       "score" -> feedback.sentimentScore).executeInsert()
	} match {
	  case Some(long) => long
	  case _ => -1
	}
	
	new Feedback(if(id != -1) new Id(id) else null,
	    feedback.message, feedback.user, feedback.sentiment, feedback.branch, feedback.createdDate, feedback.sentimentScore)
  }
  
  def findById(id: Pk[Long]): Feedback = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from feedbacks where id = {id}").on("id" -> id.get).using(parser).single()
    }
  }
  
  def findAll() = {
    DB.withConnection { implicit c =>
      SQL("Select * from feedbacks")
        .as(parser *)
    }
  }
}

case class Feedback(id:Pk[Long] = NotAssigned, message:String, user:User, sentiment:Option[Sentiment],
    branch:Branch, createdDate:Date, sentimentScore:Int)
