package com.yuriisurzhykov.pointdetector.data.remote.entities

import com.yuriisurzhykov.pointdetector.data.common.entities.LatLng

data class PointRemote(
    val id: String,
    val address: String,
    val coordinates: LatLng,
    val placeName: String,
    val workingHoursCache: WorkingHoursGroupRemote
)