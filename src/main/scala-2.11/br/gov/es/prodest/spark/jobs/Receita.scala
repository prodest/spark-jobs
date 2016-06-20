package br.gov.es.prodest.spark.jobs

import java.sql.Timestamp
import java.text.{DateFormat, SimpleDateFormat}
import java.util.{Properties, TimeZone}
import br.gov.es.prodest.spark.Utils._
import org.apache.commons.io.IOUtils
import org.apache.spark.sql.types._
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Row, SQLContext, SaveMode}

import scala.io.Source
;

object Receita extends App{

  val CONNECTION_URL = args(0)
  val OUT = args(1)
  val TABLE = "Receita"
  val TIMEZONE = "UTC"

  // spark!
  val conf = new SparkConf().setAppName("spark-jobs-receita").setMaster("local")
  val sc = new SparkContext(conf)
  val sqlContext = new SQLContext(sc)



  // criar dataFrame da tabela e registrar ele
  val fromTable = sqlContext.read.jdbc(
    CONNECTION_URL,
    TABLE,
    new Properties()
  ).registerTempTable(TABLE)





  // pegar todos os registros
  val results = sqlContext.sql(IOUtils.toString(getClass.getResource("/receita.sql").openStream(),"UTF-8")).map(t => {

    val codUnidadeGestora =  t(0).toString
    val strUnidadeGestora =   fOptString( t(1) )
    //--
    val codCategoriaEconomica = t(2).toString
    val strCategoriaEconomica =  fOptString( t(3) )
    //--
    val codOrigem = t(4).toString
    val dsOrigem =  fOptString( t(3) )
    //--
    val codRubrica = t(6).toString
    val dsRubrica = fOptString( t(7) )
    //--
    val codAlinea = t(8).toString
    val dsAlinea = fOptString( t(9) )
    //--
    val codSubalinea = t(10).toString
    val dsSubalinea = fOptString( t(11) )
    //--
    val codEspecie = t(12).toString
    val dsEspecie = fOptString( t(13) )

    //--
    val vlPrevisto = t(14)
    val vlRealizado = t(15)
    val ano = t(16)
    val mesDescritivo = t(17)
    val dataReceita =  fDateString(t(18),TIMEZONE)


    Row(
      s"($codUnidadeGestora) $strUnidadeGestora",
      s"($codCategoriaEconomica) $strCategoriaEconomica",
      s"($codOrigem) $dsOrigem",
      s"($codRubrica) $dsRubrica",
      s"($codAlinea) $dsAlinea",
      s"($codSubalinea) $dsSubalinea",
      s"($codEspecie) $dsEspecie",
      vlPrevisto,
      vlRealizado,
      ano,
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
      StructField("Especie", StringType),
      StructField("vlPrevisto", DecimalType(32,2)),
      StructField("vlRealizado", DecimalType(32,2)),
      StructField("Ano", IntegerType),
      StructField("MesDescritivo", StringType),
      StructField("dataReceita", StringType)
    )

  val schema = StructType(fields)


  val df = sqlContext.createDataFrame(results, schema)

  //inserir no novo bd todos os registros
  //obs ele cria a tabela se não for especificada
  df.write.mode(SaveMode.Overwrite).json(OUT)
}