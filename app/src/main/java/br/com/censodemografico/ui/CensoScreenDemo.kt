package br.com.censodemografico.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.censodemografico.models.Estado
import br.com.censodemografico.models.Municipio
import br.com.censodemografico.models.NomesLocalidadeResponse
import kotlinx.coroutines.delay

/** Tempo de "rede" simulado, para ver o indicador de carregamento. */
private const val ATRASO_SIMULADO_MS = 700L

/** Troque para true para testar a mensagem de erro e o botão "Tentar novamente". */
private const val SIMULAR_ERRO = false

/**
 * TEMPORÁRIO: faz o papel do ViewModel usando [DadosFalsos], para a tela
 * funcionar no emulador antes da branch `dados` ser mesclada.
 *
 * Quando o CensoViewModel existir, a MainActivity passa a chamar a
 * CensoScreen ligada ao ViewModel e este arquivo pode ser apagado.
 */
@Composable
fun CensoScreenDemo() {
    var estados by remember { mutableStateOf<List<Estado>>(emptyList()) }
    var municipios by remember { mutableStateOf<List<Municipio>>(emptyList()) }
    var estadoSelecionado by remember { mutableStateOf<Estado?>(null) }
    var municipioSelecionado by remember { mutableStateOf<Municipio?>(null) }
    var nomes by remember { mutableStateOf<NomesLocalidadeResponse?>(null) }
    var carregando by remember { mutableStateOf(true) }
    var erro by remember { mutableStateOf<String?>(null) }
    var tentativa by remember { mutableIntStateOf(0) }

    // Roda de novo sempre que a seleção muda. Se o usuário trocar rápido,
    // o Compose cancela a execução anterior, então uma resposta antiga
    // nunca sobrescreve a nova.
    LaunchedEffect(estadoSelecionado, municipioSelecionado, tentativa) {
        carregando = true
        erro = null
        delay(ATRASO_SIMULADO_MS)

        if (SIMULAR_ERRO) {
            erro = "Sem conexão com a internet. Verifique sua rede e tente novamente."
            carregando = false
            return@LaunchedEffect
        }

        if (estados.isEmpty()) {
            estados = DadosFalsos.estados
        }

        val estado = estadoSelecionado
        if (estado != null) {
            if (municipioSelecionado == null) {
                municipios = DadosFalsos.municipiosDe(estado.sigla)
            }
            val idLocalidade = municipioSelecionado?.id ?: estado.id
            nomes = DadosFalsos.nomesDa(idLocalidade)
        }

        carregando = false
    }

    CensoScreen(
        estados = estados,
        municipios = municipios,
        estadoSelecionado = estadoSelecionado,
        municipioSelecionado = municipioSelecionado,
        nomes = nomes,
        carregando = carregando,
        erro = erro,
        onEstadoSelecionado = { estado ->
            carregando = true
            estadoSelecionado = estado
            municipioSelecionado = null
            municipios = emptyList()
        },
        onMunicipioSelecionado = { municipio ->
            carregando = true
            municipioSelecionado = municipio
        },
        onLimparMunicipio = {
            carregando = true
            municipioSelecionado = null
        },
        onTentarNovamente = { tentativa++ }
    )
}
