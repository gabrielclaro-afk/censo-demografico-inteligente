package br.com.censodemografico.models

data class NomesLocalidadeResponse(
    val localidade: Int,
    val mulheres: List<NomeFrequencia>,
    val homens: List<NomeFrequencia>,
    val sobrenomes: List<NomeFrequencia>
)
