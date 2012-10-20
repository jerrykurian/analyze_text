package engine.textcategoryzer

import engine.library.Implicits._
import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SpellCheckTest extends Specification {

  "This is a specification for correcting misspelled words in an english sentence" ^
    p ^
    "Text may contain sms lingo. The analyzer should" ^
    "Search the text for sms specific words being used" ^
    "Convert misspelled word in a sentence to its closes match" ! fixOne ^
    "Convert multiple misspelled words in a sentence into its corrected form " ! fixMultiple ^
    "Convert multiple sms specific words in a sentence with multiple spaces into its equivalent english sentence " ! convertMultipleSpaces ^
    "Convert sentence with multiple correct words and some misspelled words" ! fixCorrectAndMisspelled ^
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
    "Text containing numbers" ! textWithNumber ^
    "Text with %" ! textWithPercent ^
    "Text with apostrophe" ! textWithApostrophe ^
  end
  
  def fixOne = "osters" correctSpellings () must equalTo("posters")
  def fixMultiple = "osters musters mixters" correctSpellings () must equalTo("posters masters matters")
  def convertMultipleSpaces = "osters   musters  mixters" correctSpellings () must equalTo("posters   masters  matters")
  def fixCorrectAndMisspelled = "I lve this coffee" correctSpellings () must equalTo("i love this coffee")
  
  def textWithPercent = {
    val text = """testing %"""
    text correctSpellings () must equalTo(text)
  }

  def textWithApostrophe = {
    val text = """didn't like coffees."""
    text correctSpellings () must equalTo(text)
  }

  def textWithNumber = {
    val text = """this place at avenue 1 sucks man !"""
    text correctSpellings () must equalTo(text)
  }
  
  def textEndingWithExclamation = {
    val text = """this place sucks man !"""
    text correctSpellings () must equalTo(text)
  }

  def textWithElipsesEndingWithPeriod = {
    val text = """was good..... sizzler was more like veg kadai ."""
    text correctSpellings () must equalTo(text)
  }

  def textWithSpecialChars = {
    val text = """fucking slow ya @####"""
    text correctSpellings () must equalTo(text)
  }

  def textWithComma = {
    val text = """very good, enjoyed rich taste"""
    text correctSpellings () must equalTo(text)
  }

  def textWithDoubleQuotes = {
    val text = """this "super place""""
    text correctSpellings () must equalTo(text)
  }

  def textWithElipses = {
    val text = """the new menu is superb i am loving this ..."""
    text correctSpellings () must equalTo(text)
  }

  def textWithCarriageReturn = {
    val text = """good service
     """
    text correctSpellings () must equalTo(text)
  }

  def positiveSentiment = {
    val text = "the coffee is good here"
    text correctSpellings () must equalTo(text)
  }

  def negativeSentiment = {
    val text = "the coffee is bad here"
    text correctSpellings () must equalTo(text)
  }

  def multiStatementPositive = {
    val text = "i love the coffee at this place. the service is great too"
    text correctSpellings () must equalTo(text)
  }

  def multiStatementNegative = {
    val text = "the manager here mr nagesh is a very rude guy. he needs to learn how to talk to his customers"
    text correctSpellings () must equalTo(text)
  }

  def multiPosNegStatementNegative = {
    val text = "i love the coffee at this place. service is not that great though"
    text correctSpellings () must equalTo(text)
  }
}