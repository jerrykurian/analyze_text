package services.aiaioo
import scala.actors.Actor
import models.AiaiooFailure
import services.TextHandlerService
import play.api.Logger

class FailedMessageRetryService extends Runnable {
  def run() {
    retryFailedMessages()
  }
  
  private def retryFailedMessages(){
    Logger.info("Running the FailedMessageRetryService")
    val failures = AiaiooFailure.findAllNotRetried()
    failures.foreach({
      failure => {
        try{
	        Logger.info("Fixing failure " + failure.id.get + " for " + failure.text + " from " + failure.sourceNumber)
	        val result = TextHandlerService.handle(failure.sourceNumber,failure.text)
	        Logger.info("Setting Retried")
	        AiaiooFailure.retried(failure.id.get)
	        Logger.info("Fixed")
        }catch{
          case e:Exception => Logger.error(e.getMessage())
        }
      }
    })
  }
}