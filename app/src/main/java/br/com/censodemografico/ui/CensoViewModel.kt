package br.com.censodemografico.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.censodemografico.models.Estado
import br.com.censodemografico.models.Municipio
import br.com.censodemografico.services.IbgeRepository
import br.com.censodemografico.services.mensagemAmigavel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CensoViewModel(
    private val repository: IbgeRepository = IbgeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CensoUiState())
    val uiState: StateFlow<CensoUiState> = _uiState.asStateFlow()

    private var estadosJob: Job? = null

    // Seleção de estado e de município compartilham o mesmo Job: escolher outra
    // localidade cancela a busca anterior, então uma resposta antiga nunca
    // sobrescreve a mais nova.
    private var selecaoJob: Job? = null

    private var ultimaOperacao: (() -> Unit)? = null

    init {
        carregarEstados()
    }

    fun selecionarEstado(estado: Estado) {
        ultimaOperacao = { selecionarEstado(estado) }
        selecaoJob?.cancel()
        _uiState.update {
            it.copy(
                estadoSelecionado = estado,
                municipioSelecionado = null,
                municipios = emptyList(),
                nomes = null,
                carregando = true,
                erro = null
            )
        }
        selecaoJob = viewModelScope.launch {
            executar {
                coroutineScope {
                    launch {
                        val municipios = repository.municipios(estado)
                        _uiState.update { it.copy(municipios = municipios) }
                    }
                    launch {
                        val nomes = repository.nomes(estado.id)
                        _uiState.update { it.copy(nomes = nomes) }
                    }
                }
            }
        }
    }

    fun selecionarMunicipio(municipio: Municipio) {
        ultimaOperacao = { selecionarMunicipio(municipio) }
        selecaoJob?.cancel()
        _uiState.update {
            it.copy(
                municipioSelecionado = municipio,
                nomes = null,
                carregando = true,
                erro = null
            )
        }
        selecaoJob = viewModelScope.launch {
            executar {
                val nomes = repository.nomes(municipio.id)
                _uiState.update { it.copy(nomes = nomes) }
            }
        }
    }

    /** Repete a última operação (carregar estados, selecionar estado ou município) se ela falhou. */
    fun tentarNovamente() {
        if (_uiState.value.erro != null) {
            ultimaOperacao?.invoke()
        }
    }

    private fun carregarEstados() {
        ultimaOperacao = ::carregarEstados
        estadosJob?.cancel()
        _uiState.update { it.copy(carregando = true, erro = null) }
        estadosJob = viewModelScope.launch {
            executar {
                val estados = repository.estados()
                _uiState.update { it.copy(estados = estados) }
            }
        }
    }

    private suspend fun executar(bloco: suspend () -> Unit) {
        try {
            bloco()
            _uiState.update { it.copy(carregando = false) }
        } catch (e: CancellationException) {
            // Cancelado por uma nova operação: ela é quem controla `carregando`.
            throw e
        } catch (e: Exception) {
            _uiState.update { it.copy(carregando = false, erro = e.mensagemAmigavel()) }
        }
    }
}
