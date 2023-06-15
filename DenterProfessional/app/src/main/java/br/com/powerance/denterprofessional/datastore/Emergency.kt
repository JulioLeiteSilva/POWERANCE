package br.com.powerance.denterprofessional.datastore

import android.os.Parcel
import android.os.Parcelable

data class Emergency(
    val name: String,
    val phone: String,
    val photos: List<String>,
    val status: String,
    var uid: String,
    val fcmToken: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(phone)
        parcel.writeStringList(photos)
        parcel.writeString(status)
        parcel.writeString(uid)
        parcel.writeString(fcmToken)
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
    constructor() : this("", "", emptyList(), "", "", "")
    }


