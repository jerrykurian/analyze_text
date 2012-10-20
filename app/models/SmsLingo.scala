package models

import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._
import java.util.HashMap
import play.api.Logger

object SmsLingo {
  val dictionary = new HashMap[String,String]
  
  val parser = {
    get[String]("sms_word") ~
    get[String]("english_text") map {
      case key ~ value => SmsLingo(key, value)
    }
  }

  def findAll() = {
    DB.withConnection { implicit c =>
      SQL("Select * from sms_lingos")
        .as(parser *)
    }
  }
  
  def find(key:String) = {
	  dictionary.get(key)
  }
  
  def load(){
    val smsLingos = findAll()
    smsLingos.foreach({
      smsLingo => {
        Logger.info("Adding sms lingo " + smsLingo.key + " with " + smsLingo.value)
        dictionary.put(smsLingo.key, smsLingo.value)
      }
    })
  }
}

case class SmsLingo(key: String, value: String)