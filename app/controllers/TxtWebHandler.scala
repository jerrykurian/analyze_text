package controllers

import play.api._
import play.api.mvc._
import services.TextHandlerService
import play.api.libs.iteratee.Enumerator
import play.api.Logger
import models.OutgoingMessageLog
import models.User
import java.util.Date

object TxtWebHandler extends Controller {

  def text() = Action {
      request => {
      val text = request.queryString.get("txtweb-message").get(0)
      val sourceNumber = request.queryString.get("txtweb-mobile").get(0)
      val destinationNumber = null
      Logger.info("Got request " + text + " from " + sourceNumber)
      
      val result = TextHandlerService.handle(sourceNumber, text)
      Logger.info("Sending reply to " + sourceNumber + " from " + result._2)
      
      try{
    	OutgoingMessageLog.save(new OutgoingMessageLog(null,result._2,sourceNumber,"Done",
    		  User.findByMobileNumber(sourceNumber),
    		  new Date()))
      }catch{
        case e:Throwable => Logger.error(e.getMessage())
      }
    		  
      SimpleResult(
	    header = ResponseHeader(200, Map(CONTENT_TYPE -> "text/html")), 
	    body = Enumerator("""<html><head><meta name="txtweb-appkey" content="feea5ec6-ad27-4301-b50a-9a175bac6fe3" /></head><body>"""+result._2+"</body></html>")
	  )
    }
   }
}
