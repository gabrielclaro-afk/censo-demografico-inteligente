package br.com.censodemografico.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.censodemografico.models.Estado
import br.com.censodemografico.models.Municipio
import br.com.censodemografico.models.NomesLocalidadeResponse
import br.com.censodemografico.ui.theme.CensoDemograficoInteligenteTheme

private enum class Folha { ESTADOS, MUNICIPIOS }

/**
 * Tela principal do app (camada de apresentação).
 *
 * A tela é "sem estado" para os dados: ela só desenha o que recebe e avisa,
 * pelos callbacks, quando o usuário faz alguma coisa. Quem decide o que
 * buscar e quando é o ViewModel (ou, por enquanto, o CensoScreenDemo).
 * Assim a tela não conhece Retrofit nem HTTP, como pede a arquitetura em camadas.
 *
 * Só o que é da tela fica aqui dentro: a aba escolhida e qual folha está aberta.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CensoScreen(
    estados: List<Estado>,
    municipios: List<Municipio>,
    estadoSelecionado: Estado?,
    municipioSelecionado: Municipio?,
    nomes: NomesLocalidadeResponse?,
    carregando: Boolean,
    erro: String?,
    onEstadoSelecionado: (Estado) -> Unit,
    onMunicipioSelecionado: (Municipio) -> Unit,
    onLimparMunicipio: () -> Unit,
    onTentarNovamente: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var serie by rememberSaveable { mutableStateOf(Serie.MULHERES) }
    var folhaAberta by rememberSaveable { mutableStateOf<Folha?>(null) }
    val rolagem = TopAppBarDefaults.pinnedScrollBehavior()

    Box(modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(rolagem.nestedScrollConnection),
            topBar = { BarraSuperior(scrollBehavior = rolagem) },
            containerColor = MaterialTheme.colorScheme.background,
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = innerPadding.calculateTopPadding() + 4.dp,
                    bottom = innerPadding.calculateBottomPadding() + 32.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // 1) Início: a pergunta do app
                if (estadoSelecionado == null) {
                    item(key = "boas-vindas") { BoasVindas() }
                }

                // 2) Os seletores (estado e município)
                item(key = "seletores") {
                    CartaoSeletores(
                        estado = estadoSelecionado,
                        municipio = municipioSelecionado,
                        estadosDisponiveis = estados.isNotEmpty(),
                        municipiosDisponiveis = municipios.isNotEmpty(),
                        onAbrirEstados = { folhaAberta = Folha.ESTADOS },
                        onAbrirMunicipios = { folhaAberta = Folha.MUNICIPIOS },
                    )
                }

                // 3) Conteúdo
                when {
                    estadoSelecionado == null && erro != null -> item(key = "erro-inicio") {
                        MensagemErro(mensagem = erro, onTentarNovamente = onTentarNovamente)
                    }

                    estadoSelecionado == null -> item(key = "passos") { PassosIniciais() }

                    carregando -> item(key = "carregando") { EsqueletoResultado() }

                    else -> {
                        item(key = "cabecalho") {
                            CabecalhoLocal(
                                sobretitulo = if (municipioSelecionado != null) {
                                    "Município · IBGE ${municipioSelecionado.id}"
                                } else {
                                    "Estado · IBGE ${estadoSelecionado.id}"
                                },
                                nome = municipioSelecionado?.nome ?: estadoSelecionado.nome,
                                nomeDoEstado = if (municipioSelecionado != null) estadoSelecionado.nome else null,
                                onVerEstado = onLimparMunicipio,
                            )
                        }
                        if (erro != null) {
                            item(key = "erro") { MensagemErro(mensagem = erro, onTentarNovamente = onTentarNovamente) }
                        } else if (nomes != null) {
                            val lista = nomes.lista(serie)
                            item(key = "abas") { AbasSerie(selecionada = serie, onSelecionar = { serie = it }) }
                            if (lista.isEmpty()) {
                                item(key = "vazio") {
                                    Text(
                                        text = "O IBGE não tem dados de ${serie.rotulo.lowercase()} para esta localidade.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            } else {
                                item(key = "destaque") {
                                    CartaoDestaque(serie = serie, primeiro = lista[0], segundo = lista.getOrNull(1))
                                }
                                if (lista.size > 1) {
                                    item(key = "ranking") { ListaRanking(serie = serie, nomes = lista) }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Folhas de seleção (ficam por cima de tudo)
        when (folhaAberta) {
            Folha.ESTADOS -> FolhaSelecao(
                titulo = "Escolha o estado",
                subtitulo = "${estados.size} unidades da federação",
                textoBusca = "Buscar estado",
                itens = estados,
                selecionado = estadoSelecionado,
                textoDoItem = { it.nome },
                siglaDoItem = { it.sigla },
                onSelecionar = {
                    folhaAberta = null
                    onEstadoSelecionado(it)
                },
                onFechar = { folhaAberta = null },
            )

            Folha.MUNICIPIOS -> FolhaSelecao(
                titulo = "Escolha o município",
                subtitulo = "${estadoSelecionado?.nome.orEmpty()} · ${municipios.size} municípios",
                textoBusca = "Buscar município",
                itens = municipios,
                selecionado = municipioSelecionado,
                textoDoItem = { it.nome },
                onSelecionar = {
                    folhaAberta = null
                    onMunicipioSelecionado(it)
                },
                onFechar = { folhaAberta = null },
            )

            null -> Unit
        }
    }
}

// ---------------------------------------------------------------------------
// Previews (painel "Split/Design" do Android Studio)
// ---------------------------------------------------------------------------

@Preview(showBackground = true, name = "Início", heightDp = 844)
@Composable
private fun PreviewInicio() {
    CensoDemograficoInteligenteTheme(darkTheme = false) {
        CensoScreen(
            estados = DadosFalsos.estados, municipios = emptyList(),
            estadoSelecionado = null, municipioSelecionado = null, nomes = null,
            carregando = false, erro = null,
            onEstadoSelecionado = {}, onMunicipioSelecionado = {}, onLimparMunicipio = {}, onTentarNovamente = {},
        )
    }
}

@Preview(showBackground = true, name = "Resultado", heightDp = 1200)
@Composable
private fun PreviewResultado() {
    val mg = DadosFalsos.estados.first { it.sigla == "MG" }
    CensoDemograficoInteligenteTheme(darkTheme = false) {
        CensoScreen(
            estados = DadosFalsos.estados, municipios = DadosFalsos.municipiosDe(mg.sigla),
            estadoSelecionado = mg, municipioSelecionado = null, nomes = DadosFalsos.nomesDa(mg.id),
            carregando = false, erro = null,
            onEstadoSelecionado = {}, onMunicipioSelecionado = {}, onLimparMunicipio = {}, onTentarNovamente = {},
        )
    }
}

@Preview(showBackground = true, name = "Resultado · escuro", heightDp = 1200)
@Composable
private fun PreviewResultadoEscuro() {
    val mg = DadosFalsos.estados.first { it.sigla == "MG" }
    CensoDemograficoInteligenteTheme(darkTheme = true) {
        CensoScreen(
            estados = DadosFalsos.estados, municipios = DadosFalsos.municipiosDe(mg.sigla),
            estadoSelecionado = mg, municipioSelecionado = null, nomes = DadosFalsos.nomesDa(mg.id),
            carregando = false, erro = null,
            onEstadoSelecionado = {}, onMunicipioSelecionado = {}, onLimparMunicipio = {}, onTentarNovamente = {},
        )
    }
}

@Preview(showBackground = true, name = "Erro", heightDp = 844)
@Composable
private fun PreviewErro() {
    val mg = DadosFalsos.estados.first { it.sigla == "MG" }
    CensoDemograficoInteligenteTheme(darkTheme = false) {
        CensoScreen(
            estados = DadosFalsos.estados, municipios = emptyList(),
            estadoSelecionado = mg, municipioSelecionado = null, nomes = null,
            carregando = false, erro = "Sem conexão com a internet. Verifique sua rede e tente de novo.",
            onEstadoSelecionado = {}, onMunicipioSelecionado = {}, onLimparMunicipio = {}, onTentarNovamente = {},
        )
    }
}
