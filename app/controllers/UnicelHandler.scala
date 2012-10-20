package controllers

import play.api._
import play.api.mvc._
import services.TextHandlerService

object UnicelHandler extends Controller {

  def text() = Action {
      request => {
      val text = request.queryString.get("text").get(0)
      val sourceNumber = request.queryString.get("sourceAddress").get(0)
      val destinationNumber = request.queryString.get("destinationAddress").get(0)
      
      val result = TextHandlerService.handle(sourceNumber, text)
      Ok(result._2)
    }
   }
}