package engine.sentiment

import engine.library.Implicits._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable._

@RunWith(classOf[JUnitRunner])
class SentimentAnalysisTest extends Specification {
  "This specification tests sentiment analysis of a given text " ^
    p ^
    "Test a positive sentence" ! positiveSentiment ^
    "Test a negative sentence" ! negativeSentiment ^
    "Test multiple positive statements sentence" ! multiStatementPositive ^
    "Test multiple negative statements sentence" ! multiStatementNegative ^
    "Test multiple negative and positive statements sentence" ! multiPosNegStatementNegative ^
    "Test multiple negative and positive statements sentence" ! multiNegPosStatementNegative ^
    "Test text with carriage return" ! textWithCarriageReturn ^
    "Test text with elipses" ! textWithElipses ^
    "Test text with double quotes" ! textWithDoubleQuotes ^
    "Test text with comma" ! textWithComma ^
    "Text with special characters" ! textWithSpecialChars ^
    "Text with elipses and ending with period" ! textWithElipsesEndingWithPeriod ^
    "Text ending with exclamation" ! textEndingWithExclamation ^
    "Text with %" ! textWithPercent ^
    "Text with apostrphe" ! textWithApostrophe ^
    "Text with but" ! multiPosNegStatementNegativeWithBut ^
    "Text having word with exclamation" ! textWithWordWithExclamation ^
    end

  def textWithWordWithExclamation = {
    val text = """High speed Internet access!pw:oldtown050"""
    val sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(0)
  }
  
  def textWithPercent = {
    val text = """testing %"""
    val sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(0)
  }
  
  def textWithApostrophe = {
    val text = """didn't like coffees."""
    val sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(-1)
  }

  def textEndingWithExclamation = {
    val text = """this place sucks man !"""
    val sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(-1)
  }

  def textWithElipsesEndingWithPeriod = {
    val text = """was good..... Sizzler was more like veg kadai ."""
    val sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(0)
  }

  def textWithSpecialChars = {
    val text = """fucking slow ya @####"""
    val sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(-1)
  }

  def textWithComma = {
    val text = """Very good, enjoyed rich taste """"
    val sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(1)
  }

  def textWithDoubleQuotes = {
    val text = """this "super place""""
    val sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(1)
  }

  def textWithElipses = {
    var text = """the new menu is superb I am loving this ..."""
    var sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(1)
    
    text = """Don't come here.. air cond is not cool.ppl smoke in non smoking area.WTH"""
    sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(-1) 
  }

  def textWithCarriageReturn = {
    val text = """good service
     """
    val sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(1)
  }

  def positiveSentiment = {
    val text = "The coffee is good here"
    val sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(1)
  }

  def negativeSentiment = {
    val text = "The coffee is bad here"
    val sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(-1)
  }

  def multiStatementPositive = {
    val text = "I love the coffee at this place. The service is great too"
    val sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(1)
  }

  def multiStatementNegative = {
    val text = "The manager here Mr Nagesh is a very rude guy. He needs to learn how to talk to his customers"
    val sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(0)
  }

  def multiPosNegStatementNegative = {
    val text = "I love the coffee at this place. Service is not that great though"
    val sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(-1)
  }
  
  def multiNegPosStatementNegative = {
    val text = "Service is not that great though. I love the coffee at this place"
    val sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(-1)
  }
  
   def multiPosNegStatementNegativeWithBut = {
    val text = "I love the coffee at this place but Service is not that great"
    val sentiment = text extractSentiments ()
    sentiment.sentenceSentiment must be_==(-1)
  }
}