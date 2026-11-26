package br.com.censodemografico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.censodemografico.ui.CensoScreen
import br.com.censodemografico.ui.CensoViewModel
import br.com.censodemografico.ui.theme.CensoDemograficoInteligenteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CensoDemograficoInteligenteTheme {
                // O ViewModel sobrevive à rotação da tela; a tela só recebe o estado e os eventos.
                val viewModel: CensoViewModel = viewModel()
                val estado by viewModel.uiState.collectAsStateWithLifecycle()

                CensoScreen(
                    estados = estado.estados,
                    municipios = estado.municipios,
                    estadoSelecionado = estado.estadoSelecionado,
                    municipioSelecionado = estado.municipioSelecionado,
                    nomes = estado.nomes,
                    carregando = estado.carregando,
                    erro = estado.erro,
                    onEstadoSelecionado = viewModel::selecionarEstado,
                    onMunicipioSelecionado = viewModel::selecionarMunicipio,
                    // "Ver estado inteiro": selecionar o estado de novo limpa o município
                    // e recarrega o ranking do estado.
                    onLimparMunicipio = { estado.estadoSelecionado?.let(viewModel::selecionarEstado) },
                    onTentarNovamente = viewModel::tentarNovamente
                )
            }
        }
    }
}
