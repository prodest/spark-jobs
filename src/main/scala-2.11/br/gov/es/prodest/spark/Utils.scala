package br.gov.es.prodest.spark

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.TimeZone

/**
  * Created by clayton on 24/05/16.
  */
object Utils {
  val fOptString = (value : Any) =>  value match {
    case null => ""
    case s:String => value.toString.trim.toUpperCase
  }

  val fDateString = (value: Any, timezone: String) => value match {
    case null => null
    case s:Timestamp => {
      val dataf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
      //      dataf.setTimeZone(TimeZone.getTimeZone(timezone))
      dataf.format(value)
    }
  }

}


