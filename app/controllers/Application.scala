package controllers

import play.api._
import play.api.mvc._
import services.TextHandlerService
import java.util.Date

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.userInput("Please enter your comments"))
  }
  
  def feedback = Action {implicit request =>
    Logger.info("Got feedback")
    var text = request.body.asFormUrlEncoded.get("text")(0)
    var service = TextHandlerService.handle("007", text, new Date())
    Ok(service._2)
  }
}