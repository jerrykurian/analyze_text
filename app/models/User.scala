package models

import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._
import java.util.Date

object User{
  val parser = {
      get[Pk[Long]]("id") ~
      get[String]("mobile_number") map {
      case pk ~ mobileNumber => User(pk, mobileNumber)
    }
  }
  
  def save(user:User)={
    val createdDate = new Date()
    val id:Long = DB.withConnection {implicit c =>
	   SQL("""insert into users(mobile_number,created_at,updated_at) 
	       values ({mobileNumber},{createdDate},{updatedDate})""").
	   on("mobileNumber" -> user.number,
	       "createdDate"->createdDate,
	       "updatedDate"->createdDate).executeInsert()
	} match {
	  case Some(long) => long
	  case _ => -1
	}
	var newUser = User(if(id != -1) new Id(id) else null, user.number)
	newUser
  }
  
  def findByMobileNumber(number:String):User = {
    DB.withConnection {implicit c =>
    	SQL("Select id,mobile_number from users where mobile_number like {number}").on("number"->number)
    	.using(parser).singleOpt().getOrElse(null)
    }
  }
  
  def findOrCreate(number:String):User={
    var user = findByMobileNumber(number)
    if(user==null){
      user = new User(null, number)
      save(user)
    }else{
      user
    }
  }
  
  def findById(id: Pk[Long]): User = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from users where id = {id}").on("id" -> id.get).using(parser).singleOpt().getOrElse(null)
    }
  }
}

case class User(id:Pk[Long] = NotAssigned, number:String)

