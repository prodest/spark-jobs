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
  data as dataDespesa
FROM Despesa a
WHERE Ano >= 2009