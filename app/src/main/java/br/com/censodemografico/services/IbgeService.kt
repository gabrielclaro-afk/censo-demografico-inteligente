package br.com.censodemografico.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object IbgeService {

    const val BASE_URL = "https://servicodados.ibge.gov.br/"

    val api: IbgeApi by lazy { criarApi(BASE_URL) }

    fun criarApi(baseUrl: String): IbgeApi =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IbgeApi::class.java)
}
