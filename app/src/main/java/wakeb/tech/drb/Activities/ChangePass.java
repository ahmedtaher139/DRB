package wakeb.tech.drb.Activities;


import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Registration.ResetPasswordScreen;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

public class ChangePass extends BaseActivity {


    DataManager dataManager;
    ApiServices myAPI;
    Retrofit retrofit;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ChangePass_old)
    TextInputEditText ChangePass_old;

    @BindView(R.id.ChangePass_new)
    TextInputEditText ChangePass_new;

    @BindView(R.id.ChangePass_confirm)
    TextInputEditText ChangePass_confirm;


    @OnClick(R.id.change_pass)
    void change_pass() {

        if (ChangePass_new.getText().toString().equals(ChangePass_confirm.getText().toString())) {
            CommonUtilities.showStaticDialog(this, "");
            Map<String, String> parms = new HashMap<>();
            parms.put("user_id", dataManager.getID());
            parms.put("old_password", ChangePass_old.getText().toString());
            parms.put("password", ChangePass_new.getText().toString());
            myAPI.change_password(parms)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ApiResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ApiResponse apiResponse) {

                            CommonUtilities.hideDialog();
                            if (apiResponse.getStatus()) {


                                Toast.makeText(ChangePass.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                Toast.makeText(ChangePass.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(ChangePass.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                            Log.i("ERROR_RETROFIT", e.getMessage());
                            CommonUtilities.hideDialog();

                        }

                        @Override
                        public void onComplete() {


                        }
                    });
        } else {
            Toast.makeText(this, getString(R.string.password_not_match), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable;
            if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                drawable = getResources().getDrawable(R.drawable.background_svg, getTheme());
            } else {
                drawable = VectorDrawableCompat.create(getResources(), R.drawable.background_svg, getTheme());
            }
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(drawable);
        } else {
            Window window = getWindow();
            window.setBackgroundDrawableResource(R.drawable.background_png);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.change_password));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_btn);

        init();
    }



    @Override
    protected void init() {
        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
