package com.yuriisurzhykov.pointdetector.data.remote

fun interface DataSourceCondition<T> {

    fun getCondition(): T

}