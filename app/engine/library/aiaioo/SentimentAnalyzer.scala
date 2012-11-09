package engine.library.aiaioo
import com.aiaioo.api.VakSentClient
import scala.collection.JavaConversions._
import play.api.Logger
import models.SentimentOverride
import com.aiaioo.sentiment.DocumentSentiment
import com.aiaioo.sentiment.Sentiment
import com.aiaioo.util.Entity
import com.aiaioo.sentiment.EntitySentiment

object SentimentAnalyzer {

  val sentimentClient = new VakSentClient()
  val sentimentOverrides: List[SentimentOverride] = SentimentOverride.getAll()

  def analyzeSentiment(text: String): List[(String, String, List[String])] = {
    // For a sentence that is made made of multiple sentences
    var sentences: Array[String] = text.split(""" but """)
    if (sentences == null || sentences.length == 1) sentences = text.split(""" though """)
    if (sentences == null || sentences.length == 1) sentences = text.split(""" although """)
    if (sentences == null || sentences.length == 1) sentences = text.split(""" however """)
    if (sentences == null || sentences.length == 0) sentences = Array(text)
    var analyzedSentiments = scala.collection.mutable.ListBuffer.empty[(String, String, List[String])]

    // Call the aiaioo api to extract sentiments
    val sentiments = sentimentClient.getSentiments(sentences.toList)
Logger.info("Got sentiments from aiaioo " + sentiments)
    // For each sentence create a tuple of sentence and its sentiment
    sentiments.foreach(sentiment => {
      Logger.debug("Adding sentiment for non overriden text")
      val sOverride = isOverridden(sentiment.getText().getCharacters().toString())
      val ents: List[String] = entities(sentiment)
      if (!sOverride._1) {
        analyzedSentiments += ((
          sentiment.getText().getCharacters().toString(),
          sentiment.getSentimentType().getLabel().getValue(),
          ents))
      } else {
        analyzedSentiments += ((
          sentiment.getText().getCharacters().toString(),
          sOverride._2,
          ents))
      }
    })
    analyzedSentiments.toList
  }

  def isOverridden(text: String): (Boolean, String) = {
    Logger.debug("Analyzing sentiments for " + text)
    var overridden = false;
    sentimentOverrides.foreach({
      soverride =>
        if (text.contains(soverride.text)) {
          Logger.info("Applying override for " + soverride.text)

          return (true, soverride.sentiment.name)
        }
    })
    return (false, "")
  }

  def entities(sentiment: Sentiment): List[String] = {
    val docSentiment = sentiment.asInstanceOf[DocumentSentiment]
    val sentenceSentiments = docSentiment.getSentenceSentiment()
    val ents = scala.collection.mutable.ListBuffer.empty[String]
    sentenceSentiments.keySet().foreach({ textSegment =>
      val entitySentiment = sentenceSentiments(textSegment).asInstanceOf[EntitySentiment].getEntitySentiment()
      val entSets = entitySentiment.keySet()
      entSets.foreach({
        ent =>
          {
            val entityName = ent.asInstanceOf[Entity].toString()
            ents += entityName
            Logger.debug("Got entity " + entityName)
          }
     })
    })
    ents.toList
  }
}
