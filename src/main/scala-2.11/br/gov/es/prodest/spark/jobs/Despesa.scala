package br.gov.es.prodest.spark.jobs

import java.util.Properties

import org.apache.commons.io.IOUtils
import org.apache.spark.sql.types._
import br.gov.es.prodest.spark.Utils._
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Row, SQLContext, SaveMode}

import scala.io.Source
;

object Despesa extends App{
  val CONNECTION_URL = args(0)
  val OUT = args(1)
  val TABLE = "Despesa"
  val TIMEZONE = "GMT-3"

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
  val results = sqlContext.sql(IOUtils.toString(getClass.getResource("/despesa.sql").openStream(),"UTF-8")).map(t => {

    val codUnidadeGestora =  t(0).toString
    val strUnidadeGestora =   fOptString( t(1) )
    //--
    val codCategoriaEconomica = t(2).toString
    val strCategoriaEconomica =  fOptString( t(3) )
    //--
    val codigoAcao = t(4).toString
    val strAcao =  fOptString( t(5) )
    //--
    val codElementoDespesa = t(6).toString
    val strElementoDespesa =  fOptString( t(7) )
    //--
    val codSubElementoDespesa = t(8).toString
    val strSubElementoDespesa =  fOptString( t(9) )
    //--
    val codFavorecido = t(10).toString
    val strFavorecido =  fOptString( t(11) )
    //--
    val codFonte = t(12).toString
    val strFonte =  fOptString( t(13) )
    //--
    val codFuncao = t(14).toString
    val strFuncao =  fOptString( t(15) )
    //--
    val codSubFuncao = t(16).toString
    val strSubFuncao =  fOptString( t(17) )
    //--
    val codModalidade = t(18).toString
    val strModalidade =  fOptString( t(19) )
    //--
    val codPrograma = t(20).toString
    val strPrograma =  fOptString( t(21) )
    //--
    val valorEmpenho = t(22)
    val valorLiquidado = t(23)
    val valorPago = t(24)
    val valorRap = t(25)
    // --
    val ano = t(26)
    val mesDescritivo = t(27)
    val dataDespesa = fDateString(t(28),TIMEZONE)


    Row(
      s"($codUnidadeGestora) $strUnidadeGestora",
      s"($codCategoriaEconomica) $strCategoriaEconomica",
      s"($codigoAcao) $strAcao",
      s"($codElementoDespesa) $strElementoDespesa",
      s"($codSubElementoDespesa) $strSubElementoDespesa",
      s"($codFavorecido) $strFavorecido",
      s"($codFonte) $strFonte",
      s"($codFuncao) $strFuncao",
      s"($codSubFuncao) $strSubFuncao",
      s"($codModalidade) $strModalidade",
      s"($codPrograma) $strPrograma",
      valorEmpenho,
      valorLiquidado,
      valorPago,
      valorRap,
      ano,
      mesDescritivo,
      dataDespesa
    )
  })


  val fields =
    Seq(
      StructField("UnidadeGestora", StringType),
      StructField("CategoriaEconomica", StringType),
      StructField("Acao", StringType),
      StructField("ElementoDespesa", StringType),
      StructField("SubElementoDespesa", StringType),
      StructField("Favorecido", StringType),
      StructField("Fonte", StringType),
      StructField("Funcao", StringType),
      StructField("SubFuncao", StringType),
      StructField("Modalidade", StringType),
      StructField("Programa", StringType),
      StructField("valorEmpenho", DecimalType(32,2)),
      StructField("valorLiquidado", DecimalType(32,2)),
      StructField("valorPago", DecimalType(32,2)),
      StructField("valorRap", DecimalType(32,2)),
      StructField("Ano", IntegerType),
      StructField("MesDescritivo", StringType),
      StructField("dataDespesa", TimestampType)
    )

  val schema = StructType(fields)


  val df = sqlContext.createDataFrame(results, schema)

  //inserir no novo bd todos os registros
  //obs ele cria a tabela se não for especificada
  df.write.mode(SaveMode.Overwrite).json(OUT)
}