package services

import engine.library.Implicits._
import models.IncomingMessageLog
import java.util.Date
import models.User
import play.api.mvc._
import models.Sentiment
import models.Feedback
import models.Keyword
import models.CustomMessage
import play.api.Logger
import models.SentimentOverride
import models.AiaiooFailure
import play.api.i18n.Messages
import models.TrendingTopic
import models.MCoupon
import play.api.libs.json._

object TextHandlerService {

  def handle(sourceNumber: String, text: String, timeOfText:Date): (Boolean, String) = {
    handle(sourceNumber,text,timeOfText,false)
  }
  
  def handle(sourceNumber: String, text: String, timeOfText:Date, retry:Boolean): (Boolean, String) = {
    // Log the incoming message
    try {
      if (text.trim().isEmpty()) return (true, Messages("empty_message"))

      val logIncoming = IncomingMessageLog(null, text, sourceNumber, new Date())
      IncomingMessageLog.save(logIncoming)

      // Find or create a new entry for the mobile number
      val mobileUser = User.findOrCreate(sourceNumber)

      Logger.info("Seperating keyword")
      // Extract the keyword and the actual message from the text sent in
      val splitText = separateKeywordAndText(text)

      if (splitText._2.trim().isEmpty()) return (true, Messages("empty_message"))

      // Find details for the keyword sent by user
      val keyword = Keyword.findByKeyword(splitText._1.toLowerCase())
      Logger.info("Got keyword for " + splitText._1.toLowerCase())

      Logger.info("Converting SMS Lingo")
      // Fix any sms lingo in the text and convert it to language text
      val smsLingoFixedText = splitText._2.convertSmsLingo()

      Logger.info("Correcting spellings")
      // Fix any spelling mistakes
      val spellFixedText = smsLingoFixedText.correctSpellings()

      // Extract text sentiments and sent appropriate reply
      handleSentimentExtraction(spellFixedText, splitText, mobileUser, keyword, timeOfText, retry)
    } catch {
      case e: Throwable => {
        Logger.error(e.getMessage())
        (false, Messages("general_failure"))
      }
    }
  }

  private def handleSentimentExtraction(spellFixedText: String, splitText: (String, String),
    mobileUser: User, keyword: Keyword, timeOfText:Date, retry:Boolean) = {
    Logger.info("Extracting sentiments from " + spellFixedText)

    var overallSentiment: Sentiment = null
    var feedbackForMessage: Feedback = null
    try {
      // extract the sentiment of the text
      val textWithSentiments = spellFixedText.extractSentiments()

      textWithSentiments.allSentiments.foreach({ sentence =>
        // Store the feedback text along with its corrected format
        val userFeedback = new Feedback(null, splitText._2, mobileUser,
          Some(Sentiment.findByValue(Sentiment.nameToValue(sentence._2))),
          keyword.branch, timeOfText,
          sentence._1, Sentiment.nameToScore(sentence._2))

        val feedback = Feedback.save(userFeedback)

        if (feedbackForMessage == null) feedbackForMessage = feedback;

        // extractTrendingTopics(feedback, sentence._3)
        // Pass on the feedback to the text analyzer, which will extract the 
        // trending topics asynchrounously
        TextAnalyzer ! feedback
      })
      // Find out the message that has to be sent in reply
      findReplyText(feedbackForMessage, keyword, Sentiment.findByValue(textWithSentiments.sentenceSentiment), retry)
    } catch {
      case e: Throwable => {
        Logger.error(e.getMessage())
        // Store the failed text for retry
        AiaiooFailure.save(AiaiooFailure(null, splitText._1.toLowerCase() + " " + splitText._2, mobileUser.number, 0, timeOfText))
        (false, Messages("aiaioo_failure"))
      }
    }
  }

  private def extractTrendingTopics(feedback: Feedback, topics: List[String]) {
    Logger.info("Storing topic")
    topics.foreach {
      entity =>
        // Save the trending topic
        TrendingTopic.save(new TrendingTopic(null, entity, feedback.sentiment.getOrElse(null),
          feedback.sentiment.get.value,
          feedback.branch, feedback.createdDate), feedback)
    }
  }

  private def separateKeywordAndText(text: String): (String, String) = {
    val indexOfSpace = text.trim.indexOf(" ")
    if (indexOfSpace < 0) return ("", "")
    Logger.info("Index of space is " + indexOfSpace)
    // Text till the first space is the keyword
    val keyword = text.substring(0, indexOfSpace)
    // Rest of the text is the actual message
    val actualText = if (text.length() > indexOfSpace + 1) text.substring(indexOfSpace + 1, text.length()) else ""
    (keyword, actualText)
  }

  private def findReplyText(feedback: Feedback, keyword: Keyword, sentiment: Sentiment, retry:Boolean) = {
    // Find the reply message to be sent for the sentiment expressed by the user
    val message = CustomMessage.findMessageForSentiment(sentiment, keyword.branch,
      keyword.business.getOrElse(null)).message

     var transformedMessage = if(!retry && message.contains("""{fburl}""")){
       // Transform the message by adding FB URL to it
       getFbTransformedMessage(feedback,message)
     }else{
       // Return the original message as is
       message
     }
   
    if (transformedMessage.indexOf("""@""") > 0) {
      // Find the first unused coupon
      val coupon = MCoupon.findAndUpdateCouponNotUsed()

      // Add coupon code to the code place holder
      if (coupon != null) transformedMessage = transformedMessage.replaceAll("""@""", coupon.coupon_code)
    }

    (true, transformedMessage)
  }
  
  def getFbTransformedMessage(feedback:Feedback, originalMessage:String)={
    // Replace the feedback ID place holder
    val fbUrl = Messages("fb_redirect_url", feedback.id.toString())
  
    // Get the shortened URL
    var shortenedUrl = GoogleShortener.shorten(fbUrl)
    if(shortenedUrl==null)shortenedUrl=fbUrl
    
    Logger.info("Got the shortened URL " + shortenedUrl)
    
    """\{fburl\}""".r replaceAllIn (originalMessage, shortenedUrl)
  }

 
}

