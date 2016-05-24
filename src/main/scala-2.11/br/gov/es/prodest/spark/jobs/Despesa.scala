package br.gov.es.prodest.spark.jobs

import java.util.Properties

import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SQLContext, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}
;

object Despesa extends App{
  val CONNECTION_URL = args(0)
  val OUT = args(1)
  val TABLE = "Receita"



  // spark!
  val conf = new SparkConf().setAppName("spark-jobs-despesa").setMaster("local")
  val sc = new SparkContext(conf)
  val sqlContext = new SQLContext(sc)



  // criar dataFrame da tabela e registrar ele
  val fromTable = sqlContext.read.jdbc(
    CONNECTION_URL,
    TABLE,
    new Properties()
  ).registerTempTable(TABLE)


  // pegar todos os registros
  val results = sqlContext.sql(s"""
        SELECT
        a.codUnidadeGestora,
        a.strNomeUnidadeGestora,
        a.codCategoriaEconomica,
        a.dsCategoriaEconomica,
        a.codOrigem,
        a.dsOrigem,
        a.codRubrica,
        a.dsRubrica,
        a.codAlinea,
        a.dsAlinea,
        a.codSubalinea,
        a.dsSubalinea,
        a.vlPrevisto,
        a.vlRealizado,
        case MONTH(a.data)
        when 1 then 'Janeiro'
        when 2 then 'Fevereiro'
        when 3 then 'Março'
        when 4 then 'Abril'
        when 5 then 'Maio'
        when 6 then 'Junho'
        when 7 then 'Julho'
        when 8 then 'Agosto'
        when 9 then 'Setembro'
        when 10 then 'Outubro'
        when 11 then 'Novembro'
        when 12 then 'Dezembro'
        end MesDescritivo,
        a.data as dataReceita
        FROM $TABLE a
    """
  ).map(t => {

    val codUnidadeGestora =  t(0).toString
    val strUnidadeGestora =  t(1).toString.trim.toUpperCase
    //--
    val codCategoriaEconomica = t(2).toString
    val strCategoriaEconomica =  t(3).toString.trim.toUpperCase
    //--
    val codOrigem = t(4).toString
    val dsOrigem =  t(5).toString.trim.toUpperCase
    //--
    val codRubrica = t(6).toString
    val dsRubrica = t(7).toString.trim.toUpperCase
    //--
    val codAlinea = t(8).toString
    val dsAlinea = t(9).toString.trim.toUpperCase
    //--
    val codSubalinea = t(10).toString
    val dsSubalinea = t(11).toString.trim.toUpperCase

    //--
    val vlPrevisto = t(12)
    val vlRealizado = t(13)
    val mesDescritivo = t(14)
    val dataReceita = t(15)


    Row(
      s"($codUnidadeGestora) $strUnidadeGestora",
      s"($codCategoriaEconomica) $strCategoriaEconomica",
      s"($codOrigem) $dsOrigem",
      s"($codRubrica) $dsRubrica",
      s"($codAlinea) $dsAlinea",
      s"($codSubalinea) $dsSubalinea",
      vlPrevisto,
      vlRealizado,
      mesDescritivo,
      dataReceita
    )
  })


  val fields =
    Seq(
      StructField("UnidadeGestora", StringType),
      StructField("CategoriaEconomica", StringType),
      StructField("Origem", StringType),
      StructField("Rubrica", StringType),
      StructField("Alinea", StringType),
      StructField("Subalinea", StringType),
      StructField("vlPrevisto", DecimalType(32,2)),
      StructField("vlRealizado", DecimalType(32,2)),
      StructField("MesDescritivo", StringType),
      StructField("dataReceita", TimestampType)
    )

  val schema = StructType(fields)


  val df = sqlContext.createDataFrame(results, schema)

  //inserir no novo bd todos os registros
  //obs ele cria a tabela se não for especificada
  df.write.mode(SaveMode.Overwrite).json(OUT)
}