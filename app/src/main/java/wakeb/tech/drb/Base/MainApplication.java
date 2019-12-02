package wakeb.tech.drb.Base;

import android.app.Application;
import android.content.res.Configuration;


import androidx.multidex.MultiDexApplication;


import com.zeugmasolutions.localehelper.LocaleAwareApplication;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.branch.referral.Branch;
import wakeb.tech.drb.Uitils.LocaleUtils;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.SharedPrefsHelper;

/**
 * Created by A.taher on 10/17/2018.
 */

public class MainApplication extends MultiDexApplication {


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleUtils.updateConfig(this, newConfig);
    }

    DataManager dataManager;
    public DataManager getDataManager() {
        return dataManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
         SharedPrefsHelper sharedPrefsHelper = new SharedPrefsHelper(getApplicationContext());
        dataManager = new DataManager(sharedPrefsHelper);
        // Branch object initialization
        Branch.getAutoInstance(this);


    }





}
