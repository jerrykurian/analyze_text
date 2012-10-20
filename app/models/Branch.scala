package models
import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._

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
}

case class Branch(id:Pk[Long], business:Business, sourceNumber:String)

