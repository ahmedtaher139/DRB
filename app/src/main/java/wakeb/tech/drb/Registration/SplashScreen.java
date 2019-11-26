package wakeb.tech.drb.Registration;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.R;
import wakeb.tech.drb.data.DataManager;

public class SplashScreen extends BaseActivity {

    DataManager dataManager;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_splash);
        dataManager = ((MainApplication) getApplication()).getDataManager();

        sleepFor3000();

    }




    @Override
    protected void init() {

    }





    void sleepFor3000() {
        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    Log.d("Exception", "Exception" + e);
                } finally {


                    if (dataManager.getLangStatus()) {

                        if (dataManager.getWelcomeStatus()) {
                            startActivity(new Intent(SplashScreen.this, LoginScreen.class));
                            finish();
                        } else {
                            startActivity(new Intent(SplashScreen.this, WelcomeScreen.class));
                            finish();
                        }

                    } else {

                        startActivity(new Intent(SplashScreen.this, SelectLanguage.class));
                        finish();
                    }


                }

            }
        };


        logoTimer.start();
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    sleepFor3000();

                } else {
                    sleepFor3000();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }


    }


}
