package com.yuriisurzhykov.pointdetector.data.remote.entities

import androidx.annotation.NonNull

data class WorkingHoursGroupRemote(
    @NonNull
    val workingHours: List<WorkingHoursRemote>
)