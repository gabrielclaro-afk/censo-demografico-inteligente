package br.com.censodemografico.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/*
 * Tema do Censo Inteligente. A cor dinâmica do Android 12+ fica DESLIGADA:
 * com ela, o sistema trocaria o Anil pela cor do papel de parede.
 */

private val EsquemaClaro = lightColorScheme(
    primary = Anil700,
    onPrimary = Branco,
    primaryContainer = Anil200,
    onPrimaryContainer = Anil800,
    secondary = Turmalina700,
    onSecondary = Branco,
    secondaryContainer = Turmalina100,
    onSecondaryContainer = Turmalina800,
    tertiary = Urucum700,
    onTertiary = Branco,
    tertiaryContainer = Urucum100,
    onTertiaryContainer = Urucum800,
    error = Erro,
    onError = Branco,
    errorContainer = ErroFundo,
    onErrorContainer = Erro,
    background = Grafite100,              // surface
    onBackground = Grafite900,            // ink
    surface = Branco,                     // surface-raised
    onSurface = Grafite900,
    surfaceVariant = Grafite200,          // surface-sunken
    onSurfaceVariant = Grafite700,        // ink-muted
    surfaceTint = Branco,
    surfaceContainerLowest = Branco,
    surfaceContainerLow = Branco,
    surfaceContainer = Branco,
    surfaceContainerHigh = Grafite100,
    surfaceContainerHighest = Grafite200,
    outline = Grafite600,                 // line-strong
    outlineVariant = Grafite200,          // line
    scrim = Color(0x991A1F33),
)

private val EsquemaEscuro = darkColorScheme(
    primary = Anil400,
    onPrimary = SobreAnilEscuro,
    primaryContainer = AnilSuaveEscuro,
    onPrimaryContainer = AnilTextoEscuro,
    secondary = Turmalina500,
    onSecondary = FundoEscuro,
    secondaryContainer = HomensFundoEscuro,
    onSecondaryContainer = Turmalina300,
    tertiary = Urucum400,
    onTertiary = FundoEscuro,
    tertiaryContainer = MulheresFundoEscuro,
    onTertiaryContainer = MulheresTextoEscuro,
    error = ErroEscuro,
    onError = FundoEscuro,
    errorContainer = ErroFundoEscuro,
    onErrorContainer = ErroEscuro,
    background = FundoEscuro,
    onBackground = TextoEscuro,
    surface = SuperficieEscura,
    onSurface = TextoEscuro,
    surfaceVariant = RebaixadoEscuro,
    onSurfaceVariant = TextoSecundarioEscuro,
    surfaceTint = SuperficieEscura,
    surfaceContainerLowest = RebaixadoEscuro,
    surfaceContainerLow = SuperficieEscura,
    surfaceContainer = SuperficieEscura,
    surfaceContainerHigh = SuperficieEscura,
    surfaceContainerHighest = RebaixadoEscuro,
    outline = BordaForteEscura,
    outlineVariant = BordaEscura,
    scrim = Color(0xB3000000),
)

/** Cores de uma série (um ranking): a barra, o fundo suave e o texto sobre o fundo. */
@Immutable
data class CoresSerie(val barra: Color, val fundo: Color, val texto: Color)

/** Cores da marca que não cabem no ColorScheme do Material 3. */
@Immutable
data class CoresCenso(
    val acento: Color,
    val mulheres: CoresSerie,
    val homens: CoresSerie,
    val sobrenomes: CoresSerie,
)

private val CoresClaras = CoresCenso(
    acento = Urucum500,
    mulheres = CoresSerie(barra = Urucum500, fundo = Urucum100, texto = Urucum700),
    homens = CoresSerie(barra = Turmalina600, fundo = Turmalina100, texto = Turmalina700),
    sobrenomes = CoresSerie(barra = Anil700, fundo = Anil100, texto = Anil800),
)

private val CoresEscuras = CoresCenso(
    acento = Urucum500,
    mulheres = CoresSerie(barra = Urucum500, fundo = MulheresFundoEscuro, texto = MulheresTextoEscuro),
    homens = CoresSerie(barra = Turmalina500, fundo = HomensFundoEscuro, texto = Turmalina300),
    sobrenomes = CoresSerie(barra = Anil500, fundo = SobrenomesFundoEscuro, texto = Anil300),
)

private val LocalCoresCenso = staticCompositionLocalOf { CoresClaras }

/** Acesso às cores extras: `CensoTema.cores.mulheres.barra`. */
object CensoTema {
    val cores: CoresCenso
        @Composable @ReadOnlyComposable
        get() = LocalCoresCenso.current
}

val Formas = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(24.dp),
)

@Composable
fun CensoDemograficoInteligenteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalCoresCenso provides if (darkTheme) CoresEscuras else CoresClaras) {
        MaterialTheme(
            colorScheme = if (darkTheme) EsquemaEscuro else EsquemaClaro,
            typography = Tipografia,
            shapes = Formas,
            content = content,
        )
    }
}
