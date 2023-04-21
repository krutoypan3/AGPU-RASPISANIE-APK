package ru.agpu.artikproject.background_work.datebase.realm

import io.realm.RealmConfiguration
import com.google.android.datatransport.runtime.dagger.Provides
import javax.inject.Singleton

class RealmConfig {
    // 1.
    private val realmVersion = 1L

    @Singleton
    @Provides
    fun providesRealmConfig(): RealmConfiguration =
        // 2.
        RealmConfiguration.Builder()
            .schemaVersion(realmVersion)
            .allowWritesOnUiThread(true)
            .build()
}