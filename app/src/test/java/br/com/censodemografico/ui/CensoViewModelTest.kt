package br.com.censodemografico.ui

import br.com.censodemografico.models.Estado
import br.com.censodemografico.models.Municipio
import br.com.censodemografico.models.NomeFrequencia
import br.com.censodemografico.models.NomesLocalidadeResponse
import br.com.censodemografico.services.IbgeApi
import br.com.censodemografico.services.IbgeRepository
import java.net.UnknownHostException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CensoViewModelTest {

    private val mg = Estado(31, "MG", "Minas Gerais")
    private val sp = Estado(35, "SP", "São Paulo")
    private val bh = Municipio(3106200, "Belo Horizonte")

    private fun nomes(id: Int) = NomesLocalidadeResponse(
        localidade = id,
        mulheres = listOf(NomeFrequencia("maria", 1)),
        homens = listOf(NomeFrequencia("jose", 1)),
        sobrenomes = listOf(NomeFrequencia("silva", 1))
    )

    /** API falsa: cada função pode ser trocada pelo teste para simular atraso ou erro. */
    private class FakeApi : IbgeApi {
        var estados: suspend () -> List<Estado> = { emptyList() }
        var municipios: suspend (String) -> List<Municipio> = { emptyList() }
        var nomes: suspend (Int) -> NomesLocalidadeResponse = { error("nomes não configurado") }

        override suspend fun listarEstados(orderBy: String) = estados()
        override suspend fun listarMunicipios(uf: String, orderBy: String) = municipios(uf)
        override suspend fun nomesPorLocalidade(id: Int) = nomes(id)
    }

    private lateinit var api: FakeApi

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        api = FakeApi()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun criarViewModel() = CensoViewModel(IbgeRepository(api))

    @Test
    fun init_carregaEstados() = runTest {
        api.estados = { listOf(mg, sp) }

        val vm = criarViewModel()
        assertTrue(vm.uiState.value.carregando)
        advanceUntilIdle()

        assertEquals(listOf(mg, sp), vm.uiState.value.estados)
        assertFalse(vm.uiState.value.carregando)
        assertNull(vm.uiState.value.erro)
    }

    @Test
    fun selecionarEstado_buscaMunicipiosENomesELimpaMunicipio() = runTest {
        api.estados = { listOf(mg) }
        api.municipios = { listOf(bh) }
        api.nomes = { nomes(it) }
        val vm = criarViewModel()
        advanceUntilIdle()
        vm.selecionarMunicipio(bh)
        advanceUntilIdle()
        assertEquals(bh, vm.uiState.value.municipioSelecionado)

        vm.selecionarEstado(mg)
        advanceUntilIdle()

        val estado = vm.uiState.value
        assertEquals(mg, estado.estadoSelecionado)
        assertNull(estado.municipioSelecionado)
        assertEquals(listOf(bh), estado.municipios)
        assertEquals(31, estado.nomes?.localidade)
        assertFalse(estado.carregando)
    }

    @Test
    fun selecionarMunicipio_buscaNomesDoMunicipio() = runTest {
        api.nomes = { nomes(it) }
        val vm = criarViewModel()
        advanceUntilIdle()

        vm.selecionarMunicipio(bh)
        advanceUntilIdle()

        assertEquals(3106200, vm.uiState.value.nomes?.localidade)
        assertFalse(vm.uiState.value.carregando)
    }

    @Test
    fun erro_preencheMensagemEmPortuguesETentarNovamenteRepete() = runTest {
        api.nomes = { throw UnknownHostException() }
        val vm = criarViewModel()
        advanceUntilIdle()

        vm.selecionarMunicipio(bh)
        advanceUntilIdle()

        assertNotNull(vm.uiState.value.erro)
        assertTrue(vm.uiState.value.erro!!.contains("internet"))
        assertFalse(vm.uiState.value.carregando)

        api.nomes = { nomes(it) }
        vm.tentarNovamente()
        advanceUntilIdle()

        assertNull(vm.uiState.value.erro)
        assertEquals(3106200, vm.uiState.value.nomes?.localidade)
    }

    @Test
    fun respostaAntigaNaoSobrescreveSelecaoMaisNova() = runTest {
        // MG demora 1000 ms, SP responde em 100 ms.
        api.nomes = { id ->
            delay(if (id == mg.id) 1000 else 100)
            nomes(id)
        }
        val vm = criarViewModel()
        advanceUntilIdle()

        vm.selecionarEstado(mg)
        advanceTimeBy(10)
        vm.selecionarEstado(sp)
        advanceUntilIdle()

        assertEquals(sp, vm.uiState.value.estadoSelecionado)
        assertEquals(35, vm.uiState.value.nomes?.localidade)
        assertFalse(vm.uiState.value.carregando)
    }
}
