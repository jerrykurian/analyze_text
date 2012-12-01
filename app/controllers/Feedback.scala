package controllers

import play.api._
import play.api.mvc._
import services.TextHandlerService
import java.util.Date

object Feedback extends Controller {
  
  def index = Action {
    Ok(views.html.feedback.userInput("Please enter your comments"))
  }
  
  def record = Action {implicit request =>
    Logger.info("Got feedback")
    var text = request.body.asFormUrlEncoded.get("text")(0)
    var service = TextHandlerService.handle("007", "fb " + text, new Date())
    Ok(service._2)
  }
}