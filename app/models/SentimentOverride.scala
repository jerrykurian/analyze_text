package models

import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._
import scala.collection.mutable.ListBuffer
import play.api.Logger

object SentimentOverride {
  val overrides:ListBuffer[SentimentOverride] = new ListBuffer[SentimentOverride]
  val parser = {
    get[String]("text") ~ 
    get[Pk[Long]]("sentiment_id") map {
      case text ~ sentiment=> SentimentOverride(text,Sentiment.findById(sentiment))
    }
  }

  def findAll() = {
    DB.withConnection { implicit c =>
      SQL("Select * from sentiment_overrides")
        .as(parser *)
    }
  }
  
  def getAll():List[SentimentOverride] = {
	  overrides.toList
  }
  
  def load(){
    val sentiments = findAll()
    sentiments.foreach({
      sentiment => {
        Logger.info("Adding override sentiment for " + sentiment.text)
        overrides += sentiment
      }
    })
  }
}

case class SentimentOverride(text:String, sentiment:Sentiment)