package engine.library
import models.SmsLingo

trait SmsDictionary {
  def replace(actualWord :String):String = {
     SmsLingo.find(actualWord.toLowerCase()) match {
      case x:String => x
      case _ => actualWord
    }
  }
}