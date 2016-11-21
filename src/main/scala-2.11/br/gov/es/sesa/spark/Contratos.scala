package br.gov.es.sesa.spark

import java.util.Properties

import br.gov.es.prodest.spark.Utils._
import com.mongodb.spark.config.ReadConfig
import org.apache.commons.io.IOUtils
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SQLContext, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}
import com.mongodb.spark.sql._

case class Teste(name: String, setorSolicitante: String)


object Contratos extends App{
  val CONNECTION_URL = args(0)
  val OUT = args(1)
  val TABLE = "contracts"
  val TIMEZONE = "UTC"

  // spark!
  val conf = new SparkConf().setAppName("spark-jobs-sesa-contracts").setMaster("local")
  val sc = new SparkContext(conf)
  val sqlContext = new SQLContext(sc)




  // pegar todos os registros
  val df = sqlContext
    .loadFromMongoDB(ReadConfig(Map("uri" -> "mongodb://localhost:27017/contracts_production.contracts")))
    .map(d=> Teste(d.getString(d.fieldIndex("name")),d.getString(d.fieldIndex("requesting"))))

  val df1 = sqlContext.createDataFrame(df)


  //inserir no novo bd todos os registros
  //obs ele cria a tabela se não for especificada

  df1.write.mode(SaveMode.Overwrite).json(OUT)
}