package avsoftware.com.realmapp.realm

import android.os.HandlerThread
import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmResults

object RxRealm {

//    inline fun <reified T : RealmModel> numberOfItems(): Observable<Int> = allItems<T>().map { it.size }
//
//    inline fun <reified T : RealmModel> lastItem(): Observable<T> = allItems<T>().map { it.last() }
//
//    inline fun <reified T : RealmModel> firstItem(): Observable<T> = allItems<T>().map { it.first() }

    inline fun <reified T : RealmModel> allItems(): Observable<List<T>> = Observable.create { emitter: ObservableEmitter<List<T>> ->

        val observableRealm = Realm.getDefaultInstance()

        // first parcel
        val results = observableRealm.where(T::class.java).findAllAsync()

        // handle data changed event
        val parcelChangeListener = RealmChangeListener<RealmResults<T>> {
            if (!emitter.isDisposed) {
                if (results.isValid && results.isLoaded) {
                    emitter.onNext(observableRealm.copyFromRealm(results.toList()))
                }
            }
        }

        // disposable - clean up
        emitter.setDisposable(Disposables.fromRunnable {
            if (results.isValid) {
                results.removeChangeListener(parcelChangeListener)
            }
            observableRealm.close()
        })

        results.addChangeListener(parcelChangeListener)

    }.subscribeOn(RealmScheduler.scheduler)
            .doOnSubscribe { Log.d("XXX", "SUBSCRIBE") }
            .doOnDispose { Log.d("XXX", "DISPOSE") }
            .doFinally { Log.d("XXX", "FINALLY") }


    inline fun <reified T : RealmModel> itemCount(): Observable<Int> = Observable.create { emitter: ObservableEmitter<Int> ->

        val observableRealm = Realm.getDefaultInstance()

        // first parcel
        val results = observableRealm.where(T::class.java).findAllAsync()

        // handle data changed event
        val parcelChangeListener = RealmChangeListener<RealmResults<T>> {
            if (!emitter.isDisposed) {
                if (results.isValid && results.isLoaded) {
                    val count = results.count()
                    Log.d("XXX", "Emit $count")
                    emitter.onNext(count)
                }
            }
        }

        // disposable - clean up
        emitter.setDisposable(Disposables.fromRunnable {
            if (results.isValid) {
                results.removeChangeListener(parcelChangeListener)
            }
            observableRealm.close()
        })

        results.addChangeListener(parcelChangeListener)

    }.subscribeOn(RealmScheduler.scheduler)
            .doOnSubscribe { Log.d("XXX", "SUBSCRIBE") }
            .doOnDispose { Log.d("XXX", "DISPOSE") }
            .doFinally { Log.d("XXX", "FINALLY") }


    /**
     * Simple Scheduler for use in Realm queries
     * creates a Scheduler from a single looper thread
     */
    object RealmScheduler {

        val scheduler: Scheduler by lazy {
            createScheduler()
        }

        private fun createScheduler(): Scheduler {
            val handlerThread = HandlerThread("REALM_LOOPER_SCHEDULER");
            handlerThread.start();

            var looperScheduler: Scheduler? = null

            synchronized(handlerThread) {
                looperScheduler = AndroidSchedulers.from(handlerThread.getLooper());
            }

            return looperScheduler!!
        }
    }
}