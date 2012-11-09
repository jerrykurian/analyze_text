package services.remotecaller
import play.api.libs.concurrent.Promise
import play.api.libs.ws.WS
import java.net.URLEncoder
import play.api.Logger

object HttpSender {

  def post(url:String, headers:(String,String),params:String, resultWorker:Promise[play.api.libs.ws.Response]=>String)={
    resultWorker(WS.url(url).withHeaders(headers).post(params))
  }
  
  def get(url:String, headers:(String,String),params:String, resultWorker:Promise[play.api.libs.ws.Response]=>String)={
    Logger.info("Sending message with params " + params)

    resultWorker(WS.url(url + "?" + params).withHeaders(headers).get())
  }
}