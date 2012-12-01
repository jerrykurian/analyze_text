import play.api._
import models.SmsLingo
import models.SentimentOverride
import akka.util.duration._
import play.libs.Akka
import services.sentiments.FailedMessageRetryService
import play.api.i18n.Messages

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    initData()
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
    
  def initData(){
    import models.Business
    val business = new Business(null, "test")
    val savedBusiness = Business.save(business)
    
    import models.Branch
    val branch = new Branch(null,savedBusiness,"31321")
    val savedBranch = Branch.save(branch)
    
    import models.Keyword
    val keyword = new Keyword(null,"fb",Some(savedBusiness),savedBranch,"3213")
    Keyword.save(keyword)
  }
}