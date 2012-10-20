package models

import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._
import java.util.Date

object TrendingTopic{
  val parser = {
      get[Pk[Long]]("id") ~
      get[String]("text") ~
      get[Pk[Long]]("sentiment_id") ~ 
      get[Int]("sentiment_score") ~ 
      get[Pk[Long]]("branch_id") ~
      get[Date]("created_at") map {
      case pk ~ text ~ sentiment ~ score ~ branch ~ createdDate => TrendingTopic(pk, text, 
          Sentiment.findById(sentiment),score, Branch.findById(branch), createdDate)
    }
  }
  
  val joinParser = {
      get[Pk[Long]]("id") ~
      get[String]("text") ~
      get[Pk[Long]]("sentiment") ~ 
      get[Int]("sentiment_score") ~ 
      get[Pk[Long]]("feedbackid") ~ 
      get[Pk[Long]]("feedbacksentiment") ~
      get[String]("message") ~
      get[Date]("created_at") map {
      case pk ~ text ~ sentiment ~ score ~ feedbackid ~ feedbacksentiment ~ message ~ createdDate => (pk, text, 
          Sentiment.findById(sentiment),score, Feedback.findById(feedbackid),Sentiment.findById(feedbacksentiment), 
          message, createdDate)
    }
  }
  
  def findAllForBranch(branch:Branch)={
    DB.withConnection {implicit c =>
    	SQL("Select * from trending_topics where branch_id = {branch}").on("branch"->branch.id)
    	.as(parser *)
    }
  }
  
  def findTrendingTopicWithFeedbackInBranch(branch:String)={
    DB.withConnection {implicit c =>
    	SQL("""Select topic.id as id, topic.text as text, topic.sentiment_id as sentiment, 
    			feedback.id as feedbackid, feedback.message as message, feedback.sentiment_id as feedbacksentiment,
    			topic.created_at as createdDate
    			from trending_topics topic join feedback_topics ft on topic.id=ft.trendingtopic_id
    			join feedbacks feedback on feedback.id=ft.feedback_id
    	        where topic.branch_id = {branch}""").on("branch"->branch)
    	.as(joinParser *)
    }
  }
  
  def save(topic:TrendingTopic, feedback:Feedback)={
    DB.withConnection {implicit c =>
	    val topicId:Long = SQL("""insert into trending_topics(text,sentiment_id,sentiment_score,branch_id,
	    			created_at,updated_at)
			   		values ({text},{sentiment},{score},{branch},{createdDate},{updatedDate})""").
			   		on("text" -> topic.text, "sentiment" -> topic.sentiment.id, 
			   		   "score" -> topic.sentiment.score,
			   		   "branch"->topic.branch.id, 
			   		   "createdDate" -> topic.createdDate,
			   		   "updatedDate" -> topic.createdDate).executeInsert()
		 match {
		  case Some(long) => long
		  case _ => -1
		}
	    SQL("""insert into feedback_topics(feedback_id,trendingtopic_id,branch_id)
			   		values ({feedback},{topic},{branch})""").
			   		on("feedback" -> feedback.id, "topic" -> topicId,
	       "branch"->topic.branch.id).executeInsert()
	       
    	new TrendingTopic(if(topicId != -1) new Id(topicId) else null,
	    topic.text, topic.sentiment, topic.sentiment.value,topic.branch, topic.createdDate)
    }
  
  }
}

case class TrendingTopic (id:Pk[Long]=NotAssigned, text:String, sentiment:Sentiment, sentimentScore:Int, branch:Branch, createdDate:Date)
