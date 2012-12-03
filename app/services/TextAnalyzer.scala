package services
import scala.actors.Actor
import models.Feedback
import engine.library.Implicits._
import models.TrendingTopic
import java.util.Date
import models.CategoryKeyword
import models.Category
import play.api.Logger
import anorm.Id

object TextAnalyzer extends Actor {
  start()
  def act() {
    loop {
      react {
        case feedback: Feedback => {
          extractTrendingTopics(feedback)
          extractCategories(feedback)
          sendNotification(feedback)
        }
      }
    }
  }

  private def sendNotification(feedback: Feedback) {
    if (feedback.sentiment.get.value < 0) {
      val contactNumber = feedback.branch.sourceNumber
      Logger.info("ALERT_NOTIFICATION: You have received a negative message from " + feedback.user.number
        + " The message is " + feedback.message)
    }
  }

  private def extractTrendingTopics(feedback: Feedback) {
    val nouns = feedback.message.nouns()
    Logger.info("Storing topic")
    nouns.foreach {
      noun =>
        TrendingTopic.save(new TrendingTopic(null, noun, feedback.sentiment.getOrElse(null), 
            feedback.sentiment.get.value,
          feedback.branch, feedback.createdDate), feedback)
    }
  }

  val defaultCategory = Category.findById(new Id(999))
  private def extractCategories(feedback: Feedback) {
    val categoryKeywords = CategoryKeyword.findAll()
    val lowerCasedText = feedback.message.toLowerCase()
    Logger.info("Extracting categories")

    var addedToCat = false;
    // Assign feedback to categories as per the keyword
    categoryKeywords.foreach(category => {
      if (lowerCasedText.contains(category.keyword.toLowerCase())) {
        Logger.info("Adding to category " + category.category.name)
        Category.save(feedback, category.category)
        addedToCat = true
      }
    })
    if (!addedToCat) {
      // If the feedback does not fit a category, bind it to a default one
       Logger.info("Adding to default category")
      Category.save(feedback, defaultCategory)
    }
  }
}


