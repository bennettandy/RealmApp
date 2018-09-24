package avsoftware.com.realmapp

import android.app.Application
import android.util.Log
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.reactivex.Flowable
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.concurrent.TimeUnit
import timber.log.Timber.DebugTree
import timber.log.Timber



class RealmApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(DebugTree())

        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this)

        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()

        Realm.setDefaultConfiguration(config)

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).withDeleteIfMigrationNeeded(true).build())
                        .build());
    }
}