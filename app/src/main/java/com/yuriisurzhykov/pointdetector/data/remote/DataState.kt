package com.yuriisurzhykov.pointdetector.data.remote

import java.lang.Exception

sealed class DataState<out T> {

    class Data<T>(val value: T) : DataState<T>()
    class Error(val exception: Exception) : DataState<Nothing>()
    object Loading : DataState<Nothing>()

}