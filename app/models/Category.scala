package models
import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._
import java.util.Date

object Category{
  val parser = {
      get[Pk[Long]]("id") ~
      get[String]("name") map{
      case pk ~ name  => Category(pk, name)
    }
  }
  
  def findById(id: Pk[Long]): Category = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from categories where id = {id}").on("id" -> id.get).using(parser).single()
    }
  }
  
  def save(category:Category){
    val createdDate = new Date()
     DB.withConnection {implicit c =>
        SQL("""insert into categories(id,name,created_at,updated_at)
			   		values ({id},{name},{createdDate},{updatedDate})""").
			   		on("id" -> category.id.get, "name" -> category.name,
			   		    "createdDate"->createdDate,"updatedDate"->createdDate).executeInsert()
     }
  }
  
  def save(feedback:Feedback, category:Category){
     DB.withConnection {implicit c =>
        SQL("""insert into feedback_categories(feedback_id,category_id,branch_id)
			   		values ({feedback},{category},{branch})""").
			   		on("feedback" -> feedback.id, "category" -> category.id, "branch" -> feedback.branch.id).executeInsert()
     }
  }
}

case class Category(id:Pk[Long], name:String)