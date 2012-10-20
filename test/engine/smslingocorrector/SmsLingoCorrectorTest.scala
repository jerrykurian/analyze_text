package engine.smslingocorrector

import engine.library.Implicits._
import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SmsLingoCorrectorTest extends Specification {

  "This is a specification for converting sms lingo in a text to their english equivalent" ^
    p ^
    "Text may contain sms lingo. The analyzer should" ^
    "Search the text for sms specific words being used" ^
    "Convert one sms specific words in to its equivalent english words" ! convertOne ^
    "Convert multiple sms specific words in a sentence into its equivalent english sentence " ! convertMultiple ^
    "Convert multiple sms specific words in a sentence with multiple spaces into its equivalent english sentence " ! convertMultipleSpaces ^
    "Test a positive sentence" ! positiveSentiment ^
    "Test a negative sentence" ! negativeSentiment ^
    "Test multiple positive statements sentence" ! multiStatementPositive ^
    "Test multiple negative statements sentence" ! multiStatementNegative ^
    "Test multiple negative and positive statements sentence" ! multiPosNegStatementNegative ^
    "Test text with carriage return" ! textWithCarriageReturn ^
    "Test text with elipses" ! textWithElipses ^
    "Test text with double quotes" ! textWithDoubleQuotes ^
    "Test text with comma" ! textWithComma ^
    "Text with special characters" ! textWithSpecialChars ^
    "Text with elipses and ending with period" ! textWithElipsesEndingWithPeriod ^
    "Text ending with exclamation" ! textEndingWithExclamation ^
    "Text with Xlnt" ! convertXlnt ^
    "Text containing numbers" ! textWithNumber ^
    "Text with %" ! textWithPercent ^
    "Text with apostrphe" ! textWithApostrophe ^
    "Text having word with exclamation" ! textWithWordWithExclamation ^
    "Text having a minus sign" ! textWithMinus ^
    end

  def convertOne = "grt" convertSmsLingo () must equalTo("great")
  def convertMultiple = "how r u, coffee was gr8" convertSmsLingo () must equalTo("how are you, coffee was great")
  def convertMultipleSpaces = "how r    u, coffee  was gr8" convertSmsLingo () must equalTo("how are    you, coffee  was great")
  def convertXlnt = """Xlnt food and service""" convertSmsLingo () must equalTo("""excellent food and service""")
  
  
  def textWithMinus = {
    val text = """How dare they charge RM3.20 for a cup of Iced 
      White Coffee and RM0.80 per piece of bread when their 
      Wi-Fi connection sucks like anything?"""
    text convertSmsLingo () must equalTo(text.toLowerCase())
  }
  
  def textWithWordWithExclamation = {
    val text = """High speed Internet access!pw:oldtown050"""
    text convertSmsLingo () must equalTo(text.toLowerCase())
  }

  def textWithPercent = {
    val text = """testing %"""
    text convertSmsLingo () must equalTo(text.toLowerCase())
   }

  def textWithApostrophe = {
    val text = """didn't like coffees."""
    text convertSmsLingo () must equalTo(text.toLowerCase())
  }

  def textWithNumber = {
    val text = """this place at avenue 1 sucks man !"""
    text convertSmsLingo () must equalTo(text.toLowerCase())
  }

  def textEndingWithExclamation = {
    val text = """this place sucks man !"""
    text convertSmsLingo () must equalTo(text.toLowerCase())
   }

  def textWithElipsesEndingWithPeriod = {
    val text = """was good..... sizzler was more like veg kadai ."""
    text convertSmsLingo () must equalTo(text.toLowerCase())
  }

  def textWithSpecialChars = {
    val text = """fucking slow ya @####"""
    text convertSmsLingo () must equalTo(text.toLowerCase())
  }

  def textWithComma = {
    val text = """very good, enjoyed rich taste"""
    text convertSmsLingo () must equalTo(text.toLowerCase())
  }

  def textWithDoubleQuotes = {
    val text = """this "super place""""
    text convertSmsLingo () must equalTo(text.toLowerCase())
  }

  def textWithElipses = {
    val text = """the new menu is superb i am loving this ..."""
    text convertSmsLingo () must equalTo(text.toLowerCase())
  }

  def textWithCarriageReturn = {
    val text = """good service
     """
    text convertSmsLingo () must equalTo(text.toLowerCase())
  }

  def positiveSentiment = {
    val text = "the coffee is good here"
    text convertSmsLingo () must equalTo(text.toLowerCase())
  }

  def negativeSentiment = {
    val text = "the coffee is bad here"
    text convertSmsLingo () must equalTo(text.toLowerCase())
  }

  def multiStatementPositive = {
    val text = "i love the coffee at this place. the service is great too"
    text convertSmsLingo () must equalTo(text.toLowerCase())
  }

  def multiStatementNegative = {
    val text = "the manager here mr nagesh is a very rude guy. he needs to learn how to talk to his customers"
    text convertSmsLingo () must equalTo(text.toLowerCase())
  }

  def multiPosNegStatementNegative = {
    val text = "i love the coffee at this place. service is not that great though"
    text convertSmsLingo () must equalTo(text.toLowerCase())
  }

}