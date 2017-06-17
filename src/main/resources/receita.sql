SELECT
  codUnidadeGestora,
  strNomeUnidadeGestora,
  codCategoriaEconomica,
  dsCategoriaEconomica,
  codOrigem,
  dsOrigem,
  codRubrica,
  dsRubrica,
  codAlinea,
  dsAlinea,
  codSubalinea,
  dsSubalinea,
  codEspecie,
  dsEspecie,
  vlPrevisto,
  vlRealizado,
  ano,
  case MONTH(data)
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
  END     MesDescritivo,
  data AS dataReceita
FROM Receita
WHERE a.Ano >= 2009