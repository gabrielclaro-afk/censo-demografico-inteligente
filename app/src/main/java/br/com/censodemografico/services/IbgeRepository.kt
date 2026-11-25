package br.com.censodemografico.services

import br.com.censodemografico.models.Estado
import br.com.censodemografico.models.Municipio
import br.com.censodemografico.models.NomesLocalidadeResponse

class IbgeRepository(private val api: IbgeApi = IbgeService.api) {

    suspend fun estados(): List<Estado> = api.listarEstados()

    suspend fun municipios(estado: Estado): List<Municipio> =
        api.listarMunicipios(estado.id.toString())

    /** Aceita o id de um estado (2 dígitos) ou de um município (7 dígitos). */
    suspend fun nomes(idLocalidade: Int): NomesLocalidadeResponse =
        api.nomesPorLocalidade(idLocalidade)
}
