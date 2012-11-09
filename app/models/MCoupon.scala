package models

import play.api.Play.current
import anorm._
import play.api.db._
import anorm.SqlParser._
import java.util.HashMap
import play.api.Logger
import java.util.Date

object MCoupon {
  val parser = {
    get[Pk[Long]]("id") ~
      get[String]("coupon_id") ~
      get[String]("coupon_code") ~
      get[Int]("status") map {
        case id ~ couponid ~ coupon_code ~ status => MCoupon(id, couponid, coupon_code, status)
      }
  }

  def findAndUpdateCouponNotUsed() = {
    DB.withConnection { implicit c =>
      val coupon = SQL("Select * from mcoupons where status=0 limit 1 for update")
        .using(parser).singleOpt().getOrElse(null)
      if(coupon != null && coupon.id !=null){
        markAsUsed(coupon.id.get)
        coupon
      }else{
        null
      }
    }
  }

  def markAsUsed(couponId: Long) {
    val createdDate = new Date()
    DB.withConnection { implicit c =>
      SQL("""update mcoupons set status=1 where id={id}""").
        on("id" -> couponId).executeUpdate()
    }
  }
}

case class MCoupon(id: Pk[Long], coupon_id: String, coupon_code: String, status: Int)