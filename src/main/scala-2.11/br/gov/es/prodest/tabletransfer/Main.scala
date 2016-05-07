package br.gov.es.prodest.tabletransfer

import java.util.Properties

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SQLContext, SaveMode}

object Main extends App{
  //copies a table from A to B
  // original table
  val HOST = args(0)
  val DATABASE = args(1)
  val USER = args(2)
  val PASSWORD = args(3)
  val CONNECTION_URL = s"jdbc:jtds:sqlserver://$HOST/$DATABASE;instance=MSSQLSERVER;user=$USER;password=$PASSWORD"
  val TABLE = args(4)
  val OUT = args(5)


  // spark!
  val conf = new SparkConf().setAppName("Spark Table Write").setMaster("local")
  val sc = new SparkContext(conf)
  val sqlContext = new SQLContext(sc)



  // criar dataFrame da tabela e registrar ele
  val original_table = sqlContext.read.jdbc(
    CONNECTION_URL,
    TABLE,
    new Properties()
  )
  original_table.registerTempTable(TABLE)

  // pegar todos os registros
  val results = sqlContext.sql(s"SELECT * FROM $TABLE")

  //inserir no novo bd todos os registros
  //obs ele cria a tabela se não for especificada

  results.save(OUT,SaveMode.Overwrite)
}