package br.com.powerance.denterprofessional



data class Payload (
     var address1: String?,
     var address2: String?,
     var address3: String?,
     var cep: String?,
     var email: String?,
     var fcmToken: String?,
     var miniResume: String?,
     var name: String?,
     var phone: String?,
     var status: Boolean?,
     var uid: String?
)