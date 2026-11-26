package br.com.censodemografico.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.censodemografico.R
import br.com.censodemografico.models.Estado
import br.com.censodemografico.models.Municipio
import br.com.censodemografico.ui.theme.Numero
import br.com.censodemografico.ui.theme.Sobretitulo
import kotlinx.coroutines.launch

/**
 * Os dois "dropdowns" do app, empilhados num cartão: estado em cima, município embaixo.
 * Tocar num deles abre a [FolhaSelecao]. O município só libera depois do estado.
 */
@Composable
fun CartaoSeletores(
    estado: Estado?,
    municipio: Municipio?,
    estadosDisponiveis: Boolean,
    municipiosDisponiveis: Boolean,
    onAbrirEstados: () -> Unit,
    onAbrirMunicipios: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(8.dp)) {
            LinhaSeletor(
                icone = R.drawable.ic_local,
                rotulo = "Estado",
                valor = estado?.let { "${it.nome} (${it.sigla})" },
                textoVazio = if (estadosDisponiveis) "Selecione um estado" else "Carregando estados…",
                habilitado = estadosDisponiveis,
                onClick = onAbrirEstados,
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
            LinhaSeletor(
                icone = R.drawable.ic_cidade,
                rotulo = "Município · opcional",
                valor = municipio?.nome,
                textoVazio = if (estado == null) "Escolha um estado primeiro" else "Todos os municípios",
                habilitado = estado != null && municipiosDisponiveis,
                onClick = onAbrirMunicipios,
            )
        }
    }
}

@Composable
private fun LinhaSeletor(
    icone: Int,
    rotulo: String,
    valor: String?,
    textoVazio: String,
    habilitado: Boolean,
    onClick: () -> Unit,
) {
    val cores = MaterialTheme.colorScheme
    val transparencia = if (habilitado) 1f else 0.6f
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(enabled = habilitado, role = Role.Button, onClickLabel = "trocar", onClick = onClick)
            .semantics(mergeDescendants = true) { stateDescription = valor ?: textoVazio }
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(MaterialTheme.shapes.small)
                .background(if (habilitado) cores.primaryContainer else cores.surfaceVariant),
        ) {
            Icon(
                painter = painterResource(icone),
                contentDescription = null,
                tint = if (habilitado) cores.onPrimaryContainer else cores.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(
            Modifier
                .weight(1f)
                .alpha(transparencia)
        ) {
            Text(text = rotulo.uppercase(), style = Sobretitulo, color = cores.onSurfaceVariant)
            Text(
                text = valor ?: textoVazio,
                style = if (valor != null) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
                color = if (valor != null) cores.onSurface else cores.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Icon(
            painter = painterResource(R.drawable.ic_expandir),
            contentDescription = null,
            tint = cores.onSurfaceVariant,
            modifier = Modifier
                .size(20.dp)
                .alpha(transparencia),
        )
    }
}

/**
 * Folha inferior com busca, para escolher estado ou município.
 * A busca ignora acento e maiúscula ("tres coracoes" acha "Três Corações")
 * e sublinha o trecho encontrado. A lista é preguiçosa: aguenta os 853 municípios de MG.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> FolhaSelecao(
    titulo: String,
    subtitulo: String,
    textoBusca: String,
    itens: List<T>,
    selecionado: T?,
    textoDoItem: (T) -> String,
    onSelecionar: (T) -> Unit,
    onFechar: () -> Unit,
    siglaDoItem: ((T) -> String)? = null,
) {
    val estadoFolha = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val escopo = rememberCoroutineScope()
    var busca by rememberSaveable { mutableStateOf("") }
    val termo = normalizarParaBusca(busca.trim())
    val filtrados = remember(itens, termo) {
        if (termo.isEmpty()) itens else itens.filter { normalizarParaBusca(textoDoItem(it)).contains(termo) }
    }
    val cores = MaterialTheme.colorScheme

    ModalBottomSheet(
        onDismissRequest = onFechar,
        sheetState = estadoFolha,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = cores.surface,
        scrimColor = cores.scrim,
    ) {
        Column(Modifier.padding(horizontal = 16.dp)) {
            Text(text = titulo, style = MaterialTheme.typography.titleLarge, color = cores.onSurface)
            Text(text = subtitulo, style = MaterialTheme.typography.bodyMedium, color = cores.onSurfaceVariant)
            Spacer(Modifier.padding(top = 12.dp))
            OutlinedTextField(
                value = busca,
                onValueChange = { busca = it },
                placeholder = { Text(textoBusca) },
                leadingIcon = {
                    Icon(painterResource(R.drawable.ic_buscar), contentDescription = null, modifier = Modifier.size(20.dp))
                },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = cores.background,
                    unfocusedContainerColor = cores.background,
                    unfocusedBorderColor = cores.outline,
                ),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.padding(top = 8.dp))
        }
        if (filtrados.isEmpty()) {
            Text(
                text = "Nenhum resultado para \"$busca\".",
                style = MaterialTheme.typography.bodyMedium,
                color = cores.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
            )
        }
        LazyColumn(contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 24.dp)) {
            items(filtrados) { item ->
                OpcaoSelecao(
                    nome = destacarBusca(textoDoItem(item), termo),
                    sigla = siglaDoItem?.invoke(item),
                    selecionada = item == selecionado,
                    onClick = {
                        escopo.launch { estadoFolha.hide() }.invokeOnCompletion { onSelecionar(item) }
                    },
                )
            }
        }
    }
}

@Composable
private fun OpcaoSelecao(nome: AnnotatedString, sigla: String?, selecionada: Boolean, onClick: () -> Unit) {
    val cores = MaterialTheme.colorScheme
    val corTexto = if (selecionada) cores.onPrimaryContainer else cores.onSurface
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(if (selecionada) cores.primaryContainer else Color.Transparent)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 12.dp),
    ) {
        if (sigla != null) {
            Text(
                text = sigla,
                style = Numero.copy(fontSize = 12.sp),
                color = if (selecionada) cores.onPrimaryContainer else cores.onSurfaceVariant,
                modifier = Modifier.width(34.dp),
            )
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = nome,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (selecionada) FontWeight.SemiBold else FontWeight.Normal,
            color = corTexto,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        if (selecionada) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = "Selecionado",
                tint = corTexto,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

/** Sublinha e põe em negrito o trecho de [nome] que bate com o termo já normalizado. */
private fun destacarBusca(nome: String, termo: String): AnnotatedString {
    val inicio = if (termo.isEmpty()) -1 else normalizarParaBusca(nome).indexOf(termo)
    if (inicio < 0) return AnnotatedString(nome)
    return buildAnnotatedString {
        append(nome.substring(0, inicio))
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline)) {
            append(nome.substring(inicio, inicio + termo.length))
        }
        append(nome.substring(inicio + termo.length))
    }
}
