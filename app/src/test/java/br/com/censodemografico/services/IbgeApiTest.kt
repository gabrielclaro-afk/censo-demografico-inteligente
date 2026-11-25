package br.com.censodemografico.services

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class IbgeApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: IbgeApi

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        api = IbgeService.criarApi(server.url("/").toString())
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun listarEstados_chamaUrlCertaEIgnoraCamposExtras() = runBlocking {
        server.enqueue(
            MockResponse().setBody(
                """[{"id":12,"sigla":"AC","nome":"Acre","regiao":{"id":1,"sigla":"N","nome":"Norte"}}]"""
            )
        )

        val estados = api.listarEstados()

        assertEquals("/api/v1/localidades/estados?orderBy=nome", server.takeRequest().path)
        assertEquals(1, estados.size)
        assertEquals("AC", estados[0].sigla)
        assertEquals(12, estados[0].id)
    }

    @Test
    fun listarMunicipios_chamaUrlCertaEIgnoraCamposExtras() = runBlocking {
        server.enqueue(
            MockResponse().setBody(
                """[{"id":3100104,"nome":"Abadia dos Dourados","microrregiao":{"id":31019,"nome":"Patrocínio"}}]"""
            )
        )

        val municipios = api.listarMunicipios("MG")

        assertEquals(
            "/api/v1/localidades/estados/MG/municipios?orderBy=nome",
            server.takeRequest().path
        )
        assertEquals(3100104, municipios[0].id)
        assertEquals("Abadia dos Dourados", municipios[0].nome)
    }

    @Test
    fun nomesPorLocalidade_chamaUrlCertaEMapeiaAsTresListas() = runBlocking {
        server.enqueue(
            MockResponse().setBody(
                """
                {"localidade":31,
                 "mulheres":[{"nome":"maria","frequencia":1297501}],
                 "homens":[{"nome":"jose","frequencia":528206}],
                 "sobrenomes":[{"nome":"silva","frequencia":3162886}]}
                """.trimIndent()
            )
        )

        val resposta = api.nomesPorLocalidade(31)

        assertEquals("/api/v3/nomes/2022/localidade/31", server.takeRequest().path)
        assertEquals(31, resposta.localidade)
        assertEquals("maria", resposta.mulheres[0].nome)
        assertEquals(1297501, resposta.mulheres[0].frequencia)
        assertEquals("jose", resposta.homens[0].nome)
        assertEquals("silva", resposta.sobrenomes[0].nome)
    }
}
