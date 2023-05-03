package br.com.powerance.denterprofessional

data class User(
    var email:String,
    var name:String,
    var phone:String,
    var password:String,
    var cep:String,
    var adress1:String,
    var adress2:String,
    var adress3:String,
    var miniResume:String,
    var status: Boolean,
    var fcmToken: String,
    var uid: String
                )
