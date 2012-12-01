package models
import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._
import java.util.Date

object Business{
	val parser = {
      get[Pk[Long]]("id") ~
      get[String]("name") map {
      case pk ~ name => Business(pk, name)
    }
  }
  
  def findById(id: Pk[Long]): Business = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from businesses where id = {id}").on("id" -> id.get).using(parser).single()
    }
  }
  
  def save(business: Business)={
    val createdDate = new Date()
    val id:Long = DB.withConnection {implicit c =>
	   SQL("""insert into businesses(name,created_at,updated_at)
			   		values ({name},{createdDate},{updatedDate})""").
	   on("name" -> business.name, "createdDate" -> createdDate,"updatedDate" -> createdDate).executeInsert()
	} match {
	  case Some(long) => long
	  case _ => -1
	}
	
	new Business(if(id != -1) new Id(id) else null,
	   business.name)
  }
}

case class Business(id:Pk[Long], name:String) 