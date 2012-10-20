package models
import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._
import java.util.Date

object CategoryKeyword{
  val parser = {
      get[Pk[Long]]("id") ~
      get[String]("keyword") ~ 
      get[Pk[Long]]("category_id") map{
      case pk ~ keyword ~ category => CategoryKeyword(pk, keyword, Category.findById(category))
    }
  }
  
  def findAll()={
    DB.withConnection {implicit c =>
    	SQL("Select * from category_keywords")
    	.as(parser *)
    }
  }
}

case class CategoryKeyword(id:Pk[Long], keyword:String, category:Category)