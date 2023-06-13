package br.com.powerance.denterprofessional.datastore

import android.os.Parcel
import android.os.Parcelable

data class Emergency(
    val nome: String,
    val telefone: String,
    val foto: String,
    val status: String,
    val uid: String,
    val fcm: String,
    var docID: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nome)
        parcel.writeString(telefone)
        parcel.writeString(foto)
        parcel.writeString(status)
        parcel.writeString(uid)
        parcel.writeString(fcm)
        parcel.writeString(docID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Emergency> {
        override fun createFromParcel(parcel: Parcel): Emergency {
            return Emergency(parcel)
        }

        override fun newArray(size: Int): Array<Emergency?> {
            return arrayOfNulls(size)
        }
    }
        constructor(): this("","","","","","","")
    }


