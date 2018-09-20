package avsoftware.com.realmapp.model

import android.util.Log
import avsoftware.com.realmapp.data.Parcel
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.kotlin.where

fun firstParcel() : Observable<Parcel> = Observable.create { emitter: ObservableEmitter<Parcel> ->
    val observableRealm = Realm.getDefaultInstance()

    // first parcel
    val results = observableRealm.where<Parcel>().findFirstAsync()

    // handle data changed event
    val parcelChangeListener = RealmChangeListener<Parcel> {
        Log.d("XX", "First parcel changing")

        if (!emitter.isDisposed){
            if (results.isValid && results.isLoaded){
                emitter.onNext(observableRealm.copyFromRealm(it))
            }
        }
    }

    // disposable - clean up
    emitter.setDisposable(Disposables.fromRunnable {
        if (results.isValid){
            results.removeChangeListener(parcelChangeListener)
        }
        observableRealm.close()
    })

    results.addChangeListener( parcelChangeListener )
}
        //.subscribeOn(Schedulers.io())
