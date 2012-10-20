import play.api._
import models.SmsLingo
import models.SentimentOverride
import akka.util.duration._
import play.libs.Akka
import services.aiaioo.FailedMessageRetryService
import play.api.i18n.Messages

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Loading all sms lingo in memory")
    SmsLingo.load()
    Logger.info("Loading sentiment overrides")
    SentimentOverride.load()
    Logger.info("Scheduling Actor for cleanup")
    
    val seconds:Int = Integer.valueOf(Messages("aiaioo_failure_cron_seconds"))
    val min:Int = Integer.valueOf(Messages("aiaioo_failure_cron_minutes"))
    
    Akka.system().scheduler.schedule(seconds seconds, min minutes, new FailedMessageRetryService())
    Logger.info("scheduled to run every " + min + " minutes and " + seconds + " seconds")
    Logger.info("Application has started")
  }  
  
  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }  
    
}