package engine.library
import engine.library.aiaioo.SentimentAnalyzer
import play.api.Logger
import models.Sentiment

class EnglishText(text: String) extends EnglishGrammer with SmsDictionary with SpellCheck with Text{
  var treeRepresentationOfText: String = ""
  
  private def transform(textToTransform: String, transformer: String => String): String = {
    text.split(" ").par.map(word => ({
      if (word.equals("")) {
        // Return if the word sent in is empty
        ""
      } else {
        // strip the word of any special characters pre or post fixing it	
        val specialCharEnvelopePattern(prefix, actualWord, postfix) = word

        if(actualWord != null){
        	// apply the transformation
	        val replacedWord = transformer(actualWord)
	
	        var before = prefix
	        var after = postfix
	        if (before == null) before = ""
	        if (after == null) after = ""
	
	        // Re-attach the special characters to the transformed word
	        before + replacedWord + after         
        }else{
          word
        }
      }
    })).reduceLeft(_ + " " + _) // Appends the individual words to each other in the original form
  }

  def convertSmsLingo(): String = {
    transform(text, replace)
  }

  def correctSpellings(): String = {
    transform(text, correct)
  }

  def nouns() : List[String] = {
    if(treeRepresentationOfText == null || treeRepresentationOfText.length()==0) {
     treeRepresentationOfText = createGrammarTree(text) 
    }
    extractNouns(treeRepresentationOfText)
  }
  
  def extractSentiments(): EnglishText = {
    allSentiments = SentimentAnalyzer.analyzeSentiment(text)

    allSentiments.foreach(sentimenttup => {
      val sentiment = Sentiment.nameToValue(sentimenttup._2) 
      Logger.info("Got sentiment " + sentiment)
      // The sentence sentiment will be the most negative among all sentiments returned
      if(sentenceSentiment==0 || sentiment<sentenceSentiment) sentenceSentiment = sentiment
      Logger.info("Sentence sentiment " + sentenceSentiment)
    })
    this
  }
 }