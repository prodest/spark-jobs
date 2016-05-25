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
  CASE MONTH(data)
  WHEN 1
    THEN 'Janeiro'
  WHEN 2
    THEN 'Fevereiro'
  WHEN 3
    THEN 'Março'
  WHEN 4
    THEN 'Abril'
  WHEN 5
    THEN 'Maio'
  WHEN 6
    THEN 'Junho'
  WHEN 7
    THEN 'Julho'
  WHEN 8
    THEN 'Agosto'
  WHEN 9
    THEN 'Setembro'
  WHEN 10
    THEN 'Outubro'
  WHEN 11
    THEN 'Novembro'
  WHEN 12
    THEN 'Dezembro'
  END     MesDescritivo,
  data AS dataReceita
FROM Receita
WHERE ano >= 2009