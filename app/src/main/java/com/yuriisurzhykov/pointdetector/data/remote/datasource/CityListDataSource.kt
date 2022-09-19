package com.yuriisurzhykov.pointdetector.data.remote.datasource

import com.google.firebase.database.FirebaseDatabase
import com.yuriisurzhykov.pointdetector.data.remote.entities.CityRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface CityListDataSource {

    suspend fun getCityList(): Flow<List<CityRemote>>

    class Base @Inject constructor(
        private val databaseReference: FirebaseDatabase
    ) : CityListDataSource {
        override suspend fun getCityList() = flow<List<CityRemote>> {
            databaseReference.getReference("")
        }
    }

}