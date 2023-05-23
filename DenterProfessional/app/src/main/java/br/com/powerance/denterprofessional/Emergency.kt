package br.com.powerance.denterprofessional

import android.media.Image

data class Emergency(
    val nome: String,
    val telefone: String,
    val fotos: String,
    val status: String,
    val uidSocorrista: String,
    val fcmTokenSocorrista: String
    ){
    constructor(): this("","","","","","")
}
