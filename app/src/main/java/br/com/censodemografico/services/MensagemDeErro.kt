package br.com.censodemografico.services

import com.google.gson.JsonParseException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import retrofit2.HttpException

/** Converte falhas de rede/HTTP/JSON em uma mensagem amigável em português. */
fun Throwable.mensagemAmigavel(): String = when (this) {
    is UnknownHostException, is ConnectException ->
        "Sem conexão com a internet. Verifique sua rede e tente novamente."
    is SocketTimeoutException ->
        "A conexão demorou demais para responder. Tente novamente."
    is HttpException ->
        "O servidor do IBGE retornou um erro (código ${code()}). Tente novamente mais tarde."
    is JsonParseException ->
        "Não foi possível interpretar a resposta do IBGE."
    else ->
        "Ocorreu um erro inesperado. Tente novamente."
}
