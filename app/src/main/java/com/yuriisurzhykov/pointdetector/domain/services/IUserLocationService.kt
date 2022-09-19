package com.yuriisurzhykov.pointdetector.domain.services

import com.yuriisurzhykov.pointdetector.data.common.entities.LatLng

interface IUserLocationService {

    fun currentUserLocation(): LatLng

}