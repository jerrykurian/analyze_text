package engine.library
import edu.stanford.nlp.ling.Sentence
import edu.stanford.nlp.trees.PennTreebankLanguagePack
import scala.collection.JavaConversions._
import engine.library.stanfordparser.Parser

trait EnglishGrammer {
  /* A word may have some special characters attached to it, which might prevent from it being found in the 
   dictionary. For example, user may send "How are u,?". In this case the letter "u" is an abbreviation
   but it will not be found since it has the special characters attached to it. This regular expression
   will make sure that the actual word is isolated from surrounding special characters before it is
   looked up in the dictionary
   The expression supports words like I, #@!#@! gre:at gr8!!
   In case of a word being made of only special characters, the actual word returned will be NULL
  */
  val specialCharEnvelopePattern = """([\W]*)([\w0-9]*[\w\W]*[\w0-9]+)*([\W]*)""".r

  def createGrammarTree(text :String)={
    // Using the stanford parser, extract the words from the text
    val rawWords = Sentence.toCoreLabelList(text.split(" "):_*)
    
    // Create the grammar tree
    val parse = Parser.lexParser.apply(rawWords);
    parse.toString()
  }
  
  // This is regular expression that will extract the terms that start with "(NN" and ends with ")"
  // Any text between this will be our noun word
  val nounRepresentation = """(?<=\(NN\s)(.*?)(?=\))""".r
  val commonWords = List("i","a","else","too","well","didn","is","here","something","the")
  
  def extractNouns(text :String) : List[String] = {
    var nouns = scala.collection.mutable.ListBuffer.empty[String]

    for (noun <- nounRepresentation findAllIn text) {
      val specialCharEnvelopePattern(prefix, actualWord, postfix) = noun
      if(actualWord != null && !commonWords.contains(noun)) nouns += actualWord
    }
    
    nouns.toList
  }
}
