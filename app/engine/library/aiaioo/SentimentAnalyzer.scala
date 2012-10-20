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
    val nonOverriddenSentence = scala.collection.mutable.ListBuffer.empty[String]
    var analyzedSentiments = scala.collection.mutable.ListBuffer.empty[(String, String, List[String])]

    // Call the aiaioo api to extract sentiments
    val sentiments = sentimentClient.getSentiments(sentences.toList)

    // For each sentence create a tuple of sentence and its sentiment
    sentiments.foreach(sentiment => {
      Logger.info("Adding sentiment for non overriden text")
      val sOverride = isOverridden(sentiment.getText().getCharacters().toString())
      val ent:List[String] = entities(sentiment)
      if(!sOverride._1){
        analyzedSentiments += ((
          sentiment.getText().getCharacters().toString(),
          sentiment.getSentimentType().getLabel().getValue(),
          ent
        ))
      }else{
    	analyzedSentiments += ((
          sentiment.getText().getCharacters().toString(),
          sOverride._2,
          ent
        ))
      }
    }
    )
    analyzedSentiments.toList
  }

  def isOverridden(text: String): (Boolean,String) = {
    Logger.info("Analyzing sentiments for " + text)
    var overridden = false;
    sentimentOverrides.foreach({
      soverride =>
        if (text.contains(soverride.text)) {
          Logger.info("Applying override for " + soverride.text)
         
          return (true,soverride.sentiment.name)
        }
    })
    return (false,"")
  }
  
  def entities(sentiment:Sentiment):List[String] = {
    val docSentiment = sentiment.asInstanceOf[EntitySentiment]
    val entSentiments = docSentiment.getEntitySentiment()
    Logger.info("Got entity sentiments " + entSentiments)
    val entSets = entSentiments.keySet()
    val ents = scala.collection.mutable.ListBuffer.empty[String]
    entSets.foreach({
      ent => {
        ents += ent.asInstanceOf[Entity].toString()
        Logger.info("Got entity " + ent.asInstanceOf[Entity].toString())
      }
      
    })
    ents.toList
  }
}
