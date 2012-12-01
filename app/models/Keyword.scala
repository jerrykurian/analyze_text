package models
import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._
import java.util.Date

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
  
   def save(keyword: Keyword)={
    val createdDate = new Date()
    val id:Long = DB.withConnection {implicit c =>
	   SQL("""insert into keywords(keyword,business_id,branch_id,mobile_number,created_at,updated_at)
			   		values ({keyword},{business},{branch},{mobilenumber},{createdDate},{updatedDate})""").
	   on("keyword" -> keyword.keyword, "business" -> keyword.business.map{_.id}.getOrElse(null),
	       "branch" -> keyword.branch.id, "mobilenumber" -> keyword.mobileNumber,
	       "createdDate" -> createdDate,"updatedDate" -> createdDate).executeInsert()
	} match {
	  case Some(long) => long
	  case _ => -1
	}
	
	new Keyword(if(id != -1) new Id(id) else null, keyword.keyword, keyword.business, keyword.branch, keyword.mobileNumber)

  }
}

case class Keyword(id:Pk[Long], keyword:String, business:Option[Business], branch:Branch, mobileNumber:String)
