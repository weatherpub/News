package edu.sfsu.news.code.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;

import androidx.core.content.ContextCompat;

public class ArticleService extends Service {
    public ArticleService() {
    }

    private void startForeground() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if(cameraPermission == PackageManager.PERMISSION_DENIED) {
            stopSelf();
            return;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}