package models
import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._

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
}

case class Business(id:Pk[Long], name:String) 