package avsoftware.com.realmapp.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Parcel : RealmObject() {

    @PrimaryKey
    var id: Long = 0

    var name: String? = null
}