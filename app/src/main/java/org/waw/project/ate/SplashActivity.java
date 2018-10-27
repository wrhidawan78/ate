package org.waw.project.ate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import org.waw.project.ate.utils.SharedPrefManager;


public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=1700;
    private String deviceID;
    SharedPrefManager sharedPrefManager;
    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_waw);
        sharedPrefManager = new SharedPrefManager(this);
        deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sharedPrefManager.saveSPString(SharedPrefManager.SP_DEVICE_ID, deviceID);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, OnBoarding.class));
               // Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                //startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
