package br.com.censodemografico.ui

import java.text.Normalizer
import java.text.NumberFormat
import java.util.Locale

private val localeBrasil: Locale = Locale.forLanguageTag("pt-BR")

/** A API devolve nomes em minúsculo ("maria"); aqui vira "Maria". Não inventa acento. */
fun formatarNome(nome: String): String =
    nome.split(" ").joinToString(" ") { parte ->
        parte.replaceFirstChar { it.titlecase(localeBrasil) }
    }

/** 1297501 vira "1.297.501". */
fun formatarNumero(numero: Int): String =
    NumberFormat.getIntegerInstance(localeBrasil).format(numero)

/** 1297501 vira "1.297.501 pessoas". */
fun formatarFrequencia(frequencia: Int): String = "${formatarNumero(frequencia)} pessoas"

/** Quantas vezes o 1º lugar é maior que o 2º, com uma casa e vírgula: "3,0". */
fun formatarRazao(maior: Int, menor: Int): String =
    if (menor <= 0) "" else String.format(localeBrasil, "%.1f", maior.toDouble() / menor)

/**
 * Tira acento e põe em minúsculas, letra por letra, sem mudar o tamanho do texto.
 * Assim a posição do trecho encontrado na busca vale também para o nome original.
 */
fun normalizarParaBusca(texto: String): String = buildString(texto.length) {
    texto.forEach { letra ->
        val base = Normalizer.normalize(letra.toString(), Normalizer.Form.NFD).first()
        append(base.lowercaseChar())
    }
}
