package br.com.powerance.denterprofessional

import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Phone

data class Payload (
     val address1: String?,
     val address2: String?,
     val address3: String?,
     val cep: Int?,
     val email: Email?,
     val name: String?,
     val phone: Phone?,
)