import play.api._
import models.SmsLingo
import models.SentimentOverride
import akka.util.duration._
import play.libs.Akka
import services.sentiments.FailedMessageRetryService
import play.api.i18n.Messages
import anorm.Id

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    initData()
    Logger.info("Loading all sms lingo in memory")
    SmsLingo.load()
    Logger.info("Loading sentiment overrides")
    SentimentOverride.load()
    Logger.info("Scheduling Actor for cleanup")
    
    val seconds:Int = Integer.valueOf(Messages("failure_cron_seconds"))
    val min:Int = Integer.valueOf(Messages("failure_cron_minutes"))
    
    Akka.system().scheduler.schedule(seconds seconds, min minutes, new FailedMessageRetryService())
    Logger.info("scheduled to run every " + min + " minutes and " + seconds + " seconds")
    Logger.info("Application has started")
  }  
  
  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }  
    
  def initData(){
    import models.Business
    val business = Business(null, "test")
    val savedBusiness = Business.save(business)
    
    import models.Branch
    val branch = Branch(null,savedBusiness,"31321")
    val savedBranch = Branch.save(branch)
    
    import models.Keyword
    val keyword = new Keyword(null,"fb",Some(savedBusiness),savedBranch,"3213")
    Keyword.save(keyword)
    
    import models.Category
    val defaultCategory = Category(Id(999),"default")
    Category.save(defaultCategory)
    
    import models.Sentiment
    val pWeak = Sentiment(null,"Positive Weak",1)
    val positiveSentiment = Sentiment.save(pWeak)

    val pStrong = Sentiment(null,"Positive Strong",2)
    val positiveStrong = Sentiment.save(pStrong)

    val neu = Sentiment(null,"Neutral",0)
    val neutral = Sentiment.save(neu)
    
    val nWeak = Sentiment(null,"Negative Weak",-1)
    val negativeSentiment = Sentiment.save(nWeak)

    val nStrong = Sentiment(null,"Negative Strong",-2)
    val negativeStrong = Sentiment.save(nStrong)

    import models.CustomMessage
    val pWeakMessage = CustomMessage(null,"""Thanks for your appreciation. Click here and share your feedback on Facebook {fburl}.
        Get a free Hot beverage for bill of $ 100 or more on your next visit. Just show code @""",
        positiveSentiment,Some(savedBranch),Some(savedBusiness))
    val pStrongMessage = CustomMessage(null,"""Thanks for your strong appreciation. Click here and share your feedback on Facebook {fburl}.
        Get a free Hot beverage for bill of $ 100 or more on your next visit. Just show code @""",positiveStrong,Some(savedBranch),Some(savedBusiness))
    val neutralMessage = CustomMessage(null,"Thanks for your feedback",neutral,Some(savedBranch),Some(savedBusiness))
    val negativeMessage = CustomMessage(null,"We apologize for your negative experience.",negativeSentiment,Some(savedBranch),Some(savedBusiness))
    val negativeStrongMessage = CustomMessage(null,"We apologize for your negative experience.",negativeStrong,Some(savedBranch),Some(savedBusiness))
    
    CustomMessage.save(pWeakMessage)
    CustomMessage.save(pStrongMessage)
    CustomMessage.save(neutralMessage)
    CustomMessage.save(negativeMessage)
    CustomMessage.save(negativeStrongMessage)
  }
}