package avsoftware.com.realmapp.repository

import avsoftware.com.realmapp.data.Parcel
import avsoftware.com.realmapp.realm.RxRealm
import io.reactivex.Observable

class ParcelRepository ( ){

    private val parcelsObservable = RxRealm.allItems<Parcel>().share()

    val parcelCount : Observable<Int> = RxRealm.itemCount<Parcel>()

    val firstParcel : Observable<Parcel> = parcelsObservable.map { it.first() }

    val lastParcel : Observable<Parcel> = parcelsObservable.map { it.last() }
}