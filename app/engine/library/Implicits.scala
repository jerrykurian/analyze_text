package engine.library

object Implicits {
  
  // Adds EnglishText nature to every String object implicitly
  implicit def string2Text(text :String) = new EnglishText(text)
}