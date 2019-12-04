package wakeb.tech.drb.Base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;

import java.util.Locale;

import butterknife.ButterKnife;
import wakeb.tech.drb.Uitils.LocaleUtils;
import wakeb.tech.drb.data.DataManager;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity implements BaseFragment.Callback {

    DataManager dataManager;
    public BaseActivity() {
        LocaleUtils.updateConfig(this);
    }
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        dataManager = ((MainApplication) getApplication()).getDataManager();


        if (dataManager.getLang() != null) {
            if (dataManager.getLang().equals("ar")) {
                LocaleUtils.setLocale(new Locale("ar"));
                LocaleUtils.updateConfig(getApplication(), getBaseContext().getResources().getConfiguration());

            } else {
                LocaleUtils.setLocale(new Locale("en"));
                LocaleUtils.updateConfig(getApplication(), getBaseContext().getResources().getConfiguration());
            }
        }
    }



    protected abstract void init();

    @Override
    public void onFragmentAttached() {

    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

    @Override
    public void onFragmentDetached(String tag) {

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
