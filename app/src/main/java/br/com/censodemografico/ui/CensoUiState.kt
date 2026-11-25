package br.com.censodemografico.ui

import br.com.censodemografico.models.Estado
import br.com.censodemografico.models.Municipio
import br.com.censodemografico.models.NomesLocalidadeResponse

data class CensoUiState(
    val estados: List<Estado> = emptyList(),
    val municipios: List<Municipio> = emptyList(),
    val estadoSelecionado: Estado? = null,
    val municipioSelecionado: Municipio? = null,
    val nomes: NomesLocalidadeResponse? = null,
    val carregando: Boolean = false,
    val erro: String? = null
)
