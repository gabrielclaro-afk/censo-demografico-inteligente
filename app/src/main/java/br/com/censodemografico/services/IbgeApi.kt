package br.com.censodemografico.services

import br.com.censodemografico.models.Estado
import br.com.censodemografico.models.Municipio
import br.com.censodemografico.models.NomesLocalidadeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IbgeApi {

    @GET("api/v1/localidades/estados")
    suspend fun listarEstados(
        @Query("orderBy") orderBy: String = "nome"
    ): List<Estado>

    /** [uf] pode ser a sigla (ex.: "MG") ou o id do estado (ex.: "31"). */
    @GET("api/v1/localidades/estados/{uf}/municipios")
    suspend fun listarMunicipios(
        @Path("uf") uf: String,
        @Query("orderBy") orderBy: String = "nome"
    ): List<Municipio>

    /** [id] é o id da localidade: estado (2 dígitos) ou município (7 dígitos). */
    @GET("api/v3/nomes/2022/localidade/{id}")
    suspend fun nomesPorLocalidade(
        @Path("id") id: Int
    ): NomesLocalidadeResponse
}
