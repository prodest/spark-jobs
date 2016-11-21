SELECT
  a.CodigoUnidadeGestora,
  a.UnidadeGestora,
  a.CodigoCategoriaEconomica,
  a.CategoriaEconomica,
  a.CodigoAcao,
  a.Acao,
  a.CodigoElementoDespesa,
  a.ElementoDespesa,
  a.CodigoSubelementoDespesa,
  a.SubelementoDespesa,
  a.CpfCnpjNis as CodigoFavorecido,
  a.Favorecido,
  a.CodigoFonte,
  a.Fonte,
  a.CodigoFuncao,
  a.Funcao,
  a.CodigoSubFuncao,
  a.SubFuncao,
  a.CodigoModalidade,
  a.Modalidade,
  a.CodigoPrograma,
  a.Programa,
  a.ValorEmpenho,
  a.ValorLiquidado,
  a.ValorPago,
  a.ValorRap,
  a.Ano,
  case MONTH(a.data)
  when 1 then '01 - Janeiro'
  when 2 then '02 - Fevereiro'
  when 3 then '03 - Março'
  when 4 then '04 - Abril'
  when 5 then '05 - Maio'
  when 6 then '06 - Junho'
  when 7 then '07 - Julho'
  when 8 then '08 - Agosto'
  when 9 then '09 - Setembro'
  when 10 then '10 - Outubro'
  when 11 then '11 - Novembro'
  when 12 then '12 - Dezembro'
  end MesDescritivo,
  data as dataDespesa,
  a.CodigoGrupoDespesa,
  a.GrupoDespesa
FROM Despesa a
WHERE Ano >= 2016 and  MONTH(a.data) > 7