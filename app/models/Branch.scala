package models
import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._
import java.util.Date

object Branch{
	val parser = {
      get[Pk[Long]]("id") ~
      get[Pk[Long]]("business_id") ~
      get[String]("contact_number")map {
      case pk ~ business ~ contact => Branch(pk, Business.findById(business),contact)
    }
  }
  
  def findById(id: Pk[Long]): Branch = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from branches where id = {id}").on("id" -> id.get).using(parser).single()
    }
  }
  
  def save(branch: Branch)={
    val createdDate = new Date()
    val id:Long = DB.withConnection {implicit c =>
	   SQL("""insert into branches(business_id,contact_number,created_at,updated_at)
			   		values ({business},{contact_number},{createdDate},{updatedDate})""").
	   on("business" -> branch.business.id,"contact_number" -> branch.sourceNumber,
	       "createdDate" -> createdDate,"updatedDate" -> createdDate).executeInsert()
	} match {
	  case Some(long) => long
	  case _ => -1
	}
	
	new Branch(if(id != -1) new Id(id) else null,
	   branch.business,branch.sourceNumber)
  }
}

case class Branch(id:Pk[Long], business:Business, sourceNumber:String)

