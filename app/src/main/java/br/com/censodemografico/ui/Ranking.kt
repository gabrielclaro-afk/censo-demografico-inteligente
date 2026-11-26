package br.com.censodemografico.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.censodemografico.R
import br.com.censodemografico.models.NomeFrequencia
import br.com.censodemografico.models.NomesLocalidadeResponse
import br.com.censodemografico.ui.theme.CensoTema
import br.com.censodemografico.ui.theme.CoresSerie
import br.com.censodemografico.ui.theme.Numero
import br.com.censodemografico.ui.theme.NumeroDestaque
import br.com.censodemografico.ui.theme.Sobretitulo

/** Os três rankings da resposta da API, sempre nesta ordem. */
enum class Serie(val rotulo: String, val rotuloDestaque: String, val rotuloLeitor: String) {
    MULHERES("Mulheres", "Nome feminino", "os nomes femininos"),
    HOMENS("Homens", "Nome masculino", "os nomes masculinos"),
    SOBRENOMES("Sobrenomes", "Sobrenome", "os sobrenomes"),
}

/** A cor segue o ranking, nunca a posição. */
@Composable
fun Serie.cores(): CoresSerie = when (this) {
    Serie.MULHERES -> CensoTema.cores.mulheres
    Serie.HOMENS -> CensoTema.cores.homens
    Serie.SOBRENOMES -> CensoTema.cores.sobrenomes
}

fun NomesLocalidadeResponse.lista(serie: Serie): List<NomeFrequencia> = when (serie) {
    Serie.MULHERES -> mulheres
    Serie.HOMENS -> homens
    Serie.SOBRENOMES -> sobrenomes
}

/** Abas em pílula: Mulheres · Homens · Sobrenomes. Trocar de aba não faz nova consulta. */
@Composable
fun AbasSerie(selecionada: Serie, onSelecionar: (Serie) -> Unit, modifier: Modifier = Modifier) {
    val cores = MaterialTheme.colorScheme
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(cores.surfaceVariant)
            .padding(4.dp)
            .selectableGroup(),
    ) {
        Serie.entries.forEach { serie ->
            val ativa = serie == selecionada
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp)
                    .then(if (ativa) Modifier.shadow(1.dp, CircleShape) else Modifier)
                    .clip(CircleShape)
                    .background(if (ativa) cores.surface else Color.Transparent)
                    .selectable(selected = ativa, role = Role.Tab, onClick = { onSelecionar(serie) }),
            ) {
                Box(
                    Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(serie.cores().barra)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = serie.rotulo,
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp),
                    color = if (ativa) cores.onSurface else cores.onSurfaceVariant,
                    maxLines = 1,
                )
            }
        }
    }
}

/** O 1º lugar em destaque: é a resposta direta da pergunta do app. */
@Composable
fun CartaoDestaque(
    serie: Serie,
    primeiro: NomeFrequencia,
    segundo: NomeFrequencia?,
    modifier: Modifier = Modifier,
) {
    val cs = serie.cores()
    val cores = MaterialTheme.colorScheme
    val nome = formatarNome(primeiro.nome)
    val numero = formatarNumero(primeiro.frequencia)
    Surface(
        shape = MaterialTheme.shapes.large,
        color = cs.fundo,
        modifier = modifier
            .fillMaxWidth()
            .clearAndSetSemantics {
                contentDescription = "1º lugar entre ${serie.rotuloLeitor}: $nome, $numero pessoas"
            },
    ) {
        Box {
            Icon(
                painter = painterResource(R.drawable.ic_pino),
                contentDescription = null,
                tint = cs.barra.copy(alpha = 0.16f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 16.dp, y = (-14).dp)
                    .size(width = 116.dp, height = 140.dp),
            )
            Column(Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(cs.barra)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "1º LUGAR · ${serie.rotuloDestaque.uppercase()}",
                        style = Sobretitulo,
                        color = cs.texto,
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(text = nome, style = MaterialTheme.typography.displaySmall, color = cores.onSurface)
                Spacer(Modifier.height(8.dp))
                Row {
                    Text(
                        text = numero,
                        style = NumeroDestaque,
                        color = cores.onSurface,
                        modifier = Modifier.alignByBaseline(),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "pessoas",
                        style = MaterialTheme.typography.bodySmall,
                        color = cores.onSurfaceVariant,
                        modifier = Modifier.alignByBaseline(),
                    )
                }
                if (segundo != null && segundo.frequencia > 0) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = cores.onSurface)) {
                                append("${formatarRazao(primeiro.frequencia, segundo.frequencia)}×")
                            }
                            append(" mais que ${formatarNome(segundo.nome)}, o 2º lugar.")
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, lineHeight = 18.sp),
                        color = cores.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

/** Do 2º ao 10º lugar, com barra proporcional ao 1º. [nomes] é a lista inteira (o 1º incluído). */
@Composable
fun ListaRanking(serie: Serie, nomes: List<NomeFrequencia>, modifier: Modifier = Modifier) {
    val cores = MaterialTheme.colorScheme
    val maior = nomes.firstOrNull()?.frequencia?.coerceAtLeast(1) ?: 1
    val corBarra = serie.cores().barra
    Surface(
        shape = MaterialTheme.shapes.large,
        color = cores.surface,
        border = BorderStroke(1.dp, cores.outlineVariant),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(vertical = 4.dp)) {
            nomes.drop(1).forEachIndexed { indice, item ->
                if (indice > 0) HorizontalDivider(color = cores.outlineVariant)
                LinhaRanking(
                    posicao = indice + 2,
                    item = item,
                    fracao = item.frequencia.toFloat() / maior,
                    corBarra = corBarra,
                    atraso = indice * 30,
                )
            }
            HorizontalDivider(color = cores.outlineVariant)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp),
            ) {
                Text(text = "FONTE: IBGE · CENSO 2022", style = Sobretitulo, color = cores.onSurfaceVariant)
                Text(
                    text = "BARRA = % DO 1º LUGAR",
                    style = Sobretitulo,
                    color = cores.onSurfaceVariant,
                    modifier = Modifier.clearAndSetSemantics { },
                )
            }
        }
    }
}

@Composable
private fun LinhaRanking(posicao: Int, item: NomeFrequencia, fracao: Float, corBarra: Color, atraso: Int) {
    val cores = MaterialTheme.colorScheme
    val nome = formatarNome(item.nome)
    val numero = formatarNumero(item.frequencia)

    // A barra cresce da base até o tamanho final quando o dado chega ou muda.
    var alvo by remember(item) { mutableFloatStateOf(0f) }
    LaunchedEffect(item, fracao) { alvo = fracao }
    val largura by animateFloatAsState(
        targetValue = alvo,
        animationSpec = tween(durationMillis = 300, delayMillis = atraso, easing = FastOutSlowInEasing),
        label = "barra",
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clearAndSetSemantics { contentDescription = "${posicao}º lugar, $nome, $numero pessoas" }
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(32.dp)
                .clip(MaterialTheme.shapes.small)
                .background(cores.surfaceVariant),
        ) {
            Text(text = "$posicao", style = Numero.copy(fontSize = 13.sp), color = cores.onSurfaceVariant)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = nome,
                    style = MaterialTheme.typography.titleMedium,
                    color = cores.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(8.dp))
                Text(text = numero, style = Numero, color = cores.onSurfaceVariant)
            }
            Spacer(Modifier.height(6.dp))
            // Trilho em `background`, para a barra manter 3:1 contra ele.
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(cores.background)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(largura.coerceIn(0.01f, 1f))
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                        .background(corBarra)
                )
            }
        }
    }
}
