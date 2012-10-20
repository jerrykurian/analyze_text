package models
import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._

object Keyword {

  val parser = {
      get[Pk[Long]]("id") ~
      get[String]("keyword") ~
      get[Option[Pk[Long]]]("business_id") ~
      get[Pk[Long]]("branch_id") ~
      get[String]("mobile_number") map{
      case pk ~ keyword ~ business ~ branch ~ mobileNumber => Keyword(pk, keyword, 
          if (business.getOrElse(null) != null) Some(Business.findById(business.get)) else None, 
          Branch.findById(branch), mobileNumber)
    }
  }
  
  def findById(id: Pk[Long]): Keyword = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from keywords where id = {id}").on("id" -> id.get).using(parser).single()
    }
  }
  
  def findByKeyword(keyword: String): Keyword = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from keywords where keyword like {keyword}").on("keyword" -> keyword).using(parser).single()
    }
  }
}

case class Keyword(id:Pk[Long], keyword:String, business:Option[Business], branch:Branch, mobileNumber:String)
