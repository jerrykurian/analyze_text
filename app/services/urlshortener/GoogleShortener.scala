package services
import play.api.libs.ws.WS
import play.api.Logger
import play.api.libs.json._
import play.api.i18n.Messages
import play.api.libs.concurrent.Promise
import java.util.concurrent.TimeUnit
import services.remotecaller.HttpSender

object GoogleShortener {
  def shorten(longUrl: String) = {
    val params = """{"longUrl":"""" + longUrl + """"}"""

    HttpSender.post(Messages("google_url_shortener_url") + "?key=" + Messages("google_api_key"),
        ("Content-Type", "application/json"), params, handleResult)
  }

  def handleResult(result: Promise[play.api.libs.ws.Response]) = {
    val url =
      result.orTimeout("timeout", 10000).map(responseOrTimeout => {
        responseOrTimeout.fold(
          response => {
            if (response.status == 200) {
              Logger.info("Url Fetch success " + response.body)
              val result = Json.parse(response.body)
              val shortUrl = (result \ "id").asOpt[String]
              println(shortUrl)
              shortUrl.get
            } else {
              Logger.info("Url Fetch failed " + response.status + " " + response.body)
              null
            }
          },
          timeout => {
            println("Throwing Exception")
            new RuntimeException()
          })
      })

    url.await(15, TimeUnit.SECONDS).get.asInstanceOf[String]
  }
}