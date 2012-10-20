package engine.library.stanfordparser
import edu.stanford.nlp.parser.lexparser.LexicalizedParser

object Parser {
  val lexParser = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz")
}