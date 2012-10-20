package engine.treeparser

import engine.library.Implicits._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable._

@RunWith(classOf[JUnitRunner])
class TreeParseTest extends Specification {

  "This specification tests creation of a PennParse Tree for a given text " ^
    p ^
    "Standard test text " ! parseStandard ^
    "Text with multiple words " ! parseMultipleWord ^
    "Text with names and places " ! namePlaceTest ^
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
  end

  def parseStandard = "Bell, a company which is based in LA, makes and distributes computer products." nouns () must not be null and not be empty and contain("company", "computer", "products")
  def parseMultipleWord = "I love the coffee at this place and the service is great too" nouns () must not be null and not be empty and contain("coffee", "place", "service")
  def namePlaceTest = "The manager here Mr Nagesh is a very rude. He needs to learn how to talk to his customers" nouns () must not be null and not be empty and contain("manager", "rude")
  def textEndingWithExclamation = {
    val text = """this place sucks man !"""
    text nouns () must not be null 
  }

  def textWithElipsesEndingWithPeriod = {
    val text = """was good..... sizzler was more like veg kadai ."""
    text nouns () must not be null 
  }

  def textWithSpecialChars = {
    val text = """fucking slow ya @####"""
    text nouns () must not be null 
  }

  def textWithComma = {
    val text = """very good, enjoyed rich taste"""
    text nouns () must not be null 
  }

  def textWithDoubleQuotes = {
    val text = """this "super place""""
    text nouns () must not be null 
  }

  def textWithElipses = {
    val text = """the new menu is superb i am loving this ..."""
    text nouns () must not be null 
  }

  def textWithCarriageReturn = {
    val text = """good service
     """
    text nouns () must not be null 
  }

  def positiveSentiment = {
    val text = "the coffee is good here"
    text nouns () must not be null 
  }

  def negativeSentiment = {
    val text = "the coffee is bad here"
    text nouns () must not be null 
  }

  def multiStatementPositive = {
    val text = "i love the coffee at this place. the service is great too"
    text nouns () must not be null 
  }

  def multiStatementNegative = {
    val text = "the manager here mr nagesh is a very rude guy. he needs to learn how to talk to his customers"
    text nouns () must not be null 
  }

  def multiPosNegStatementNegative = {
    val text = "i love the coffee at this place. service is not that great though"
    text nouns () must not be null 
  }

}