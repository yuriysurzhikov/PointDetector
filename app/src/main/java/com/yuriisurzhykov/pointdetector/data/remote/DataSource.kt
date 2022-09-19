package com.yuriisurzhykov.pointdetector.data.remote

interface DataSource<T> {

    suspend fun getData(): T

    suspend fun getDataByCondition(condition: DataSourceCondition<*>): T

}