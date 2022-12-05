package com.yuriisurzhykov.pointdetector.presentation.update

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateInitActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val updateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = updateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { updateInfo ->
            if (updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                
            }
        }
    }

}