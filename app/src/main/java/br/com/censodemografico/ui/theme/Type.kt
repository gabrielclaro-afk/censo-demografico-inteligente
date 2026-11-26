package br.com.censodemografico.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import br.com.censodemografico.R

/** Títulos: o nome da localidade, o 1º lugar e o título da folha. */
val Schibsted = FontFamily(
    Font(R.font.schibsted_bold, FontWeight.Bold),
    Font(R.font.schibsted_extrabold, FontWeight.ExtraBold),
)

/** Texto corrido, rótulos e botões. */
val PublicSans = FontFamily(
    Font(R.font.publicsans_regular, FontWeight.Normal),
    Font(R.font.publicsans_medium, FontWeight.Medium),
    Font(R.font.publicsans_semibold, FontWeight.SemiBold),
)

/** Dados: frequências, posições, códigos do IBGE e sobretítulos. */
val JetBrainsMono = FontFamily(
    Font(R.font.jetbrainsmono_medium, FontWeight.Medium),
    Font(R.font.jetbrainsmono_bold, FontWeight.Bold),
)

/** Os estilos do design system mapeados para os slots do Material 3. */
val Tipografia = Typography(
    displaySmall = TextStyle(             // destaque
        fontFamily = Schibsted, fontWeight = FontWeight.ExtraBold,
        fontSize = 40.sp, lineHeight = 44.sp, letterSpacing = (-0.02).em,
    ),
    headlineMedium = TextStyle(           // titulo
        fontFamily = Schibsted, fontWeight = FontWeight.Bold,
        fontSize = 28.sp, lineHeight = 32.sp, letterSpacing = (-0.02).em,
    ),
    titleLarge = TextStyle(               // subtitulo
        fontFamily = Schibsted, fontWeight = FontWeight.Bold,
        fontSize = 20.sp, lineHeight = 26.sp, letterSpacing = (-0.01).em,
    ),
    titleMedium = TextStyle(              // nome-item
        fontFamily = PublicSans, fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp, lineHeight = 22.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = PublicSans, fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp, lineHeight = 20.sp,
    ),
    bodyLarge = TextStyle(                // corpo
        fontFamily = PublicSans, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(               // corpo-pequeno
        fontFamily = PublicSans, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(                // legenda
        fontFamily = PublicSans, fontWeight = FontWeight.Medium,
        fontSize = 12.sp, lineHeight = 16.sp,
    ),
    labelLarge = TextStyle(               // rotulo
        fontFamily = PublicSans, fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp, lineHeight = 20.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = PublicSans, fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp, lineHeight = 16.sp,
    ),
    labelSmall = TextStyle(               // sobretitulo (use em MAIÚSCULAS)
        fontFamily = JetBrainsMono, fontWeight = FontWeight.Medium,
        fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.08.em,
    ),
)

/** numero-destaque: a frequência do 1º lugar. */
val NumeroDestaque = TextStyle(
    fontFamily = JetBrainsMono, fontWeight = FontWeight.Bold,
    fontSize = 28.sp, lineHeight = 32.sp, letterSpacing = (-0.01).em,
)

/** numero: frequência e posição em cada linha do ranking. */
val Numero = TextStyle(
    fontFamily = JetBrainsMono, fontWeight = FontWeight.Medium,
    fontSize = 14.sp, lineHeight = 20.sp,
)

/** sobretitulo: "ESTADO · IBGE 31". Passe o texto já em maiúsculas. */
val Sobretitulo = TextStyle(
    fontFamily = JetBrainsMono, fontWeight = FontWeight.Medium,
    fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.08.em,
)

/** Título do cartão de boas-vindas. */
val TituloBoasVindas = TextStyle(
    fontFamily = Schibsted, fontWeight = FontWeight.ExtraBold,
    fontSize = 30.sp, lineHeight = 34.sp, letterSpacing = (-0.02).em,
)
