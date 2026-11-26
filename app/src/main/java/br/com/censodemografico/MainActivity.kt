package br.com.censodemografico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.censodemografico.ui.CensoScreenDemo
import br.com.censodemografico.ui.theme.CensoDemograficoInteligenteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CensoDemograficoInteligenteTheme {
                // TEMPORÁRIO: tela com dados falsos.
                // Depois do merge da branch `dados`, trocar pela CensoScreen
                // ligada ao CensoViewModel.
                CensoScreenDemo()
            }
        }
    }
}
