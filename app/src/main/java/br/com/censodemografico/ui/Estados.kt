package br.com.censodemografico.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.censodemografico.R
import br.com.censodemografico.ui.theme.CensoTema
import br.com.censodemografico.ui.theme.Numero
import br.com.censodemografico.ui.theme.Sobretitulo
import br.com.censodemografico.ui.theme.TituloBoasVindas

/** Estado inicial: a pergunta do app num cartão Anil. Some quando há um estado escolhido. */
@Composable
fun BoasVindas(modifier: Modifier = Modifier) {
    val cores = MaterialTheme.colorScheme
    Surface(
        shape = MaterialTheme.shapes.large,
        color = cores.primary,
        contentColor = cores.onPrimary,
        modifier = modifier.fillMaxWidth(),
    ) {
        Box {
            Icon(
                painter = painterResource(R.drawable.ic_pino),
                contentDescription = null,
                tint = cores.onPrimary.copy(alpha = 0.12f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 22.dp, y = 34.dp)
                    .size(width = 150.dp, height = 181.dp),
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(width = 24.dp, height = 6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(CensoTema.cores.acento)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "CENSO DEMOGRÁFICO 2022",
                        style = Sobretitulo,
                        color = cores.onPrimary.copy(alpha = 0.9f),
                    )
                }
                Text(
                    text = "Quais nomes mais se repetem por aqui?",
                    style = TituloBoasVindas,
                    modifier = Modifier
                        .widthIn(max = 280.dp)
                        .semantics { heading() },
                )
                Text(
                    text = "Veja os nomes e sobrenomes mais comuns de cada estado e município do Brasil.",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp, lineHeight = 22.sp),
                    color = cores.onPrimary.copy(alpha = 0.92f),
                    modifier = Modifier.widthIn(max = 290.dp),
                )
            }
        }
    }
}

/** Os três passos curtos que aparecem abaixo dos seletores no início. */
@Composable
fun PassosIniciais(modifier: Modifier = Modifier) {
    val passos = listOf(
        "Escolha um estado." to " A lista carrega direto do IBGE.",
        "Se quiser, escolha um município" to " para ver só a cidade.",
        "Troque entre Mulheres, Homens e Sobrenomes" to " para ver cada ranking.",
    )
    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = modifier) {
        passos.forEachIndexed { indice, (forte, resto) ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.size(24.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "${indice + 1}", style = Numero.copy(fontSize = 12.sp, lineHeight = 12.sp))
                    }
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)) {
                            append(forte)
                        }
                        append(resto)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

/** Tipo e código do IBGE em sobretítulo, nome da localidade e, num município, o link de volta. */
@Composable
fun CabecalhoLocal(
    sobretitulo: String,
    nome: String,
    nomeDoEstado: String?,
    onVerEstado: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cores = MaterialTheme.colorScheme
    Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = modifier) {
        Text(text = sobretitulo.uppercase(), style = Sobretitulo, color = cores.onSurfaceVariant)
        Text(
            text = nome,
            style = MaterialTheme.typography.headlineMedium,
            color = cores.onSurface,
            modifier = Modifier.semantics { heading() },
        )
        if (nomeDoEstado != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .heightIn(min = 40.dp)
                    .clip(MaterialTheme.shapes.small)
                    .clickable(role = Role.Button, onClick = onVerEstado)
                    .padding(end = 8.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_voltar),
                    contentDescription = null,
                    tint = cores.primary,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(4.dp))
                Text(text = "Ver $nomeDoEstado inteiro", style = MaterialTheme.typography.labelLarge, color = cores.primary)
            }
        }
    }
}

/** Esqueleto com o formato do resultado, pulsando devagar, e a nota "Consultando o IBGE…". */
@Composable
fun EsqueletoResultado(modifier: Modifier = Modifier) {
    val transicao = rememberInfiniteTransition(label = "esqueleto")
    val alfa by transicao.animateFloat(
        initialValue = 1f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "alfa",
    )
    val cores = MaterialTheme.colorScheme
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Consultando o IBGE" },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.graphicsLayer { alpha = alfa },
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Bloco(largura = 120.dp, altura = 12.dp)
                Bloco(largura = 200.dp, altura = 30.dp)
            }
            Bloco(largura = null, altura = 48.dp, forma = CircleShape)
            Bloco(largura = null, altura = 168.dp, forma = MaterialTheme.shapes.large)
            Surface(
                shape = MaterialTheme.shapes.large,
                color = cores.surface,
                border = BorderStroke(1.dp, cores.outlineVariant),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    listOf(0.46f, 0.38f, 0.52f, 0.30f).forEach { fracao ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Bloco(largura = 32.dp, altura = 32.dp)
                            Spacer(Modifier.width(12.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                                Box(
                                    Modifier
                                        .fillMaxWidth(fracao)
                                        .height(14.dp)
                                        .clip(MaterialTheme.shapes.small)
                                        .background(cores.surfaceVariant)
                                )
                                Bloco(largura = null, altura = 8.dp)
                            }
                        }
                    }
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clearAndSetSemantics { }) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = cores.primary,
                trackColor = cores.surfaceVariant,
                strokeWidth = 2.dp,
            )
            Spacer(Modifier.width(8.dp))
            Text(text = "Consultando o IBGE…", style = MaterialTheme.typography.bodySmall, color = cores.onSurfaceVariant)
        }
    }
}

@Composable
private fun Bloco(largura: Dp?, altura: Dp, forma: Shape = MaterialTheme.shapes.small) {
    Box(
        Modifier
            .then(if (largura != null) Modifier.width(largura) else Modifier.fillMaxWidth())
            .height(altura)
            .clip(forma)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )
}

/** Cartão de erro no lugar do resultado. Os seletores continuam usáveis acima. */
@Composable
fun MensagemErro(mensagem: String, onTentarNovamente: () -> Unit, modifier: Modifier = Modifier) {
    val cores = MaterialTheme.colorScheme
    Surface(
        shape = MaterialTheme.shapes.large,
        color = cores.errorContainer,
        modifier = modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite },
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_alerta),
                    contentDescription = null,
                    tint = cores.error,
                    modifier = Modifier.size(22.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Não foi possível consultar o IBGE",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = cores.error,
                )
            }
            Text(text = mensagem, style = MaterialTheme.typography.bodyMedium, color = cores.onSurface)
            Button(onClick = onTentarNovamente, shape = MaterialTheme.shapes.medium) {
                Icon(
                    painter = painterResource(R.drawable.ic_atualizar),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text("Tentar novamente")
            }
        }
    }
}
