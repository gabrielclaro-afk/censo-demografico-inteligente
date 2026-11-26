package br.com.censodemografico.ui

import br.com.censodemografico.models.Estado
import br.com.censodemografico.models.Municipio
import br.com.censodemografico.models.NomeFrequencia
import br.com.censodemografico.models.NomesLocalidadeResponse

/**
 * TEMPORÁRIO: dados para desenvolver e testar a tela antes da camada de dados
 * (branch `dados`) estar pronta. Também alimenta os @Preview da CensoScreen.
 *
 * Estados e municípios são os reais do IBGE. Os nomes de Minas Gerais (31) e de
 * Varginha (3170701) são os da API do Censo 2022; nas outras localidades os
 * números são uma escala dos de MG, só para a tela mudar ao trocar a seleção.
 * Os municípios são 20 dos 853 de Minas Gerais.
 */
object DadosFalsos {

    val estados: List<Estado> = listOf(
        Estado(id = 12, sigla = "AC", nome = "Acre"),
        Estado(id = 27, sigla = "AL", nome = "Alagoas"),
        Estado(id = 16, sigla = "AP", nome = "Amapá"),
        Estado(id = 13, sigla = "AM", nome = "Amazonas"),
        Estado(id = 29, sigla = "BA", nome = "Bahia"),
        Estado(id = 23, sigla = "CE", nome = "Ceará"),
        Estado(id = 53, sigla = "DF", nome = "Distrito Federal"),
        Estado(id = 32, sigla = "ES", nome = "Espírito Santo"),
        Estado(id = 52, sigla = "GO", nome = "Goiás"),
        Estado(id = 21, sigla = "MA", nome = "Maranhão"),
        Estado(id = 51, sigla = "MT", nome = "Mato Grosso"),
        Estado(id = 50, sigla = "MS", nome = "Mato Grosso do Sul"),
        Estado(id = 31, sigla = "MG", nome = "Minas Gerais"),
        Estado(id = 15, sigla = "PA", nome = "Pará"),
        Estado(id = 25, sigla = "PB", nome = "Paraíba"),
        Estado(id = 41, sigla = "PR", nome = "Paraná"),
        Estado(id = 26, sigla = "PE", nome = "Pernambuco"),
        Estado(id = 22, sigla = "PI", nome = "Piauí"),
        Estado(id = 33, sigla = "RJ", nome = "Rio de Janeiro"),
        Estado(id = 24, sigla = "RN", nome = "Rio Grande do Norte"),
        Estado(id = 43, sigla = "RS", nome = "Rio Grande do Sul"),
        Estado(id = 11, sigla = "RO", nome = "Rondônia"),
        Estado(id = 14, sigla = "RR", nome = "Roraima"),
        Estado(id = 42, sigla = "SC", nome = "Santa Catarina"),
        Estado(id = 35, sigla = "SP", nome = "São Paulo"),
        Estado(id = 28, sigla = "SE", nome = "Sergipe"),
        Estado(id = 17, sigla = "TO", nome = "Tocantins"),
    )

    private val municipiosDeMinas: List<Municipio> = listOf(
            Municipio(3106200, "Belo Horizonte"),
            Municipio(3106705, "Betim"),
            Municipio(3118601, "Contagem"),
            Municipio(3122306, "Divinópolis"),
            Municipio(3127701, "Governador Valadares"),
            Municipio(3131307, "Ipatinga"),
            Municipio(3132404, "Itajubá"),
            Municipio(3136702, "Juiz de Fora"),
            Municipio(3138203, "Lavras"),
            Municipio(3143302, "Montes Claros"),
            Municipio(3147907, "Passos"),
            Municipio(3151800, "Poços de Caldas"),
            Municipio(3152501, "Pouso Alegre"),
            Municipio(3154606, "Ribeirão das Neves"),
            Municipio(3167202, "Sete Lagoas"),
            Municipio(3168606, "Teófilo Otoni"),
            Municipio(3169307, "Três Corações"),
            Municipio(3170107, "Uberaba"),
            Municipio(3170206, "Uberlândia"),
            Municipio(3170701, "Varginha"),
    )

    fun municipiosDe(siglaUf: String): List<Municipio> =
        if (siglaUf == "MG") municipiosDeMinas else emptyList()

    private val minasGerais = NomesLocalidadeResponse(
        localidade = 31,
        mulheres = listOf(
            NomeFrequencia("maria", 1297501),
            NomeFrequencia("ana", 431676),
            NomeFrequencia("julia", 74771),
            NomeFrequencia("fernanda", 60250),
            NomeFrequencia("alice", 58670),
            NomeFrequencia("juliana", 56092),
            NomeFrequencia("adriana", 55511),
            NomeFrequencia("patricia", 54587),
            NomeFrequencia("laura", 54469),
            NomeFrequencia("leticia", 52995),
        ),
        homens = listOf(
            NomeFrequencia("jose", 528206),
            NomeFrequencia("joao", 372897),
            NomeFrequencia("antonio", 194316),
            NomeFrequencia("pedro", 171651),
            NomeFrequencia("carlos", 149050),
            NomeFrequencia("luiz", 148049),
            NomeFrequencia("lucas", 148033),
            NomeFrequencia("paulo", 140311),
            NomeFrequencia("gabriel", 135660),
            NomeFrequencia("marcos", 116929),
        ),
        sobrenomes = listOf(
            NomeFrequencia("silva", 3162886),
            NomeFrequencia("santos", 1726601),
            NomeFrequencia("oliveira", 1435182),
            NomeFrequencia("souza", 1163790),
            NomeFrequencia("pereira", 932125),
            NomeFrequencia("ferreira", 887346),
            NomeFrequencia("alves", 764476),
            NomeFrequencia("rodrigues", 724735),
            NomeFrequencia("costa", 524390),
            NomeFrequencia("gomes", 480872),
        ),
    )

    private val varginha = NomesLocalidadeResponse(
        localidade = 3170701,
        mulheres = listOf(
            NomeFrequencia("maria", 7407),
            NomeFrequencia("ana", 2764),
            NomeFrequencia("julia", 623),
            NomeFrequencia("leticia", 495),
            NomeFrequencia("patricia", 472),
            NomeFrequencia("helena", 470),
            NomeFrequencia("alice", 459),
            NomeFrequencia("juliana", 443),
            NomeFrequencia("adriana", 437),
            NomeFrequencia("fernanda", 435),
        ),
        homens = listOf(
            NomeFrequencia("jose", 3012),
            NomeFrequencia("joao", 2439),
            NomeFrequencia("luiz", 1387),
            NomeFrequencia("antonio", 1247),
            NomeFrequencia("pedro", 1218),
            NomeFrequencia("paulo", 1101),
            NomeFrequencia("gabriel", 1064),
            NomeFrequencia("lucas", 1044),
            NomeFrequencia("carlos", 990),
            NomeFrequencia("rafael", 900),
        ),
        sobrenomes = listOf(
            NomeFrequencia("silva", 18453),
            NomeFrequencia("oliveira", 7850),
            NomeFrequencia("santos", 6562),
            NomeFrequencia("souza", 6195),
            NomeFrequencia("pereira", 5180),
            NomeFrequencia("ferreira", 4335),
            NomeFrequencia("carvalho", 3598),
            NomeFrequencia("aparecida", 3120),
            NomeFrequencia("ribeiro", 3039),
            NomeFrequencia("rodrigues", 2974),
        ),
    )

    /** Mesmo formato da resposta de /api/v3/nomes/2022/localidade/{id}. */
    fun nomesDa(idLocalidade: Int): NomesLocalidadeResponse {
        if (idLocalidade == 31) return minasGerais
        if (idLocalidade == 3170701) return varginha
        val fator = ((idLocalidade % 89) + 3) / 100.0
        fun escalar(lista: List<NomeFrequencia>) =
            lista.map { it.copy(frequencia = (it.frequencia * fator).toInt().coerceAtLeast(1)) }
        return NomesLocalidadeResponse(
            localidade = idLocalidade,
            mulheres = escalar(minasGerais.mulheres),
            homens = escalar(minasGerais.homens),
            sobrenomes = escalar(minasGerais.sobrenomes),
        )
    }
}
