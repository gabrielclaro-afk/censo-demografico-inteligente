package br.com.censodemografico.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import br.com.censodemografico.R
import br.com.censodemografico.ui.theme.CensoTema
import br.com.censodemografico.ui.theme.JetBrainsMono
import br.com.censodemografico.ui.theme.Schibsted
import br.com.censodemografico.ui.theme.Sobretitulo

/** Barra do topo: a marca à esquerda e o selo "Censo 2022" à direita. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(scrollBehavior: TopAppBarScrollBehavior? = null) {
    TopAppBar(
        title = { MarcaCenso() },
        actions = {
            SeloCenso()
            Spacer(Modifier.width(16.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        scrollBehavior = scrollBehavior,
    )
}

/** Símbolo (pino com ranking) + "censo" + "INTELIGENTE". */
@Composable
fun MarcaCenso(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clearAndSetSemantics { contentDescription = "Censo Inteligente" },
    ) {
        Image(
            painter = painterResource(R.drawable.ic_censo_simbolo),
            contentDescription = null,
            modifier = Modifier.size(width = 26.dp, height = 31.dp),
        )
        Spacer(Modifier.width(10.dp))
        Column {
            Text(
                text = "censo",
                style = TextStyle(
                    fontFamily = Schibsted, fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp, lineHeight = 22.sp, letterSpacing = (-0.02).em,
                ),
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "INTELIGENTE",
                style = TextStyle(
                    fontFamily = JetBrainsMono, fontWeight = FontWeight.Medium,
                    fontSize = 8.5.sp, lineHeight = 10.sp, letterSpacing = 0.16.em,
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** Selo informativo: de onde vêm os dados. Não é botão. */
@Composable
fun SeloCenso() {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .height(28.dp)
                .padding(horizontal = 10.dp),
        ) {
            Box(
                Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(CensoTema.cores.acento)
            )
            Text(
                text = "CENSO 2022",
                style = Sobretitulo.copy(letterSpacing = 0.06.em),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
