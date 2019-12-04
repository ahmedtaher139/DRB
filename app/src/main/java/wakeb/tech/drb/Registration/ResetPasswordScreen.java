package wakeb.tech.drb.Registration;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.Uitils.DefaultExceptionHandler;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

public class ResetPasswordScreen extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.RestPass_email)
    TextInputEditText RestPass_email;


    @BindView(R.id.temporary_password)
    TextInputEditText temporary_password;

    @OnClick(R.id.back_button)
    void back_button() {
        finish();
    }

    @BindView(R.id.change_password)
    TextInputEditText change_password;


    @BindView(R.id.email_layout)
    LinearLayout email_layout;


    @BindView(R.id.temp_layout)
    LinearLayout temp_layout;


    @BindView(R.id.pass_layout)
    LinearLayout pass_layout;


    @BindView(R.id.RestPass_rest)
    Button RestPass_rest;

    @BindView(R.id.confirm_rest)
    Button confirm_rest;

    @BindView(R.id.change_rest)
    Button change_rest;


    DataManager dataManager;
    ApiServices myAPI;
    Retrofit retrofit;


    String USER_ID = "";
    String TEMP = "";
    String EMAIL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_screen);
        init();
        RestPass_rest.setOnClickListener(this);
        change_rest.setOnClickListener(this);
        confirm_rest.setOnClickListener(this);
    }


    @Override
    protected void init() {
        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.RestPass_rest:

                if (!TextUtils.isEmpty(RestPass_email.getText().toString())) {
                    EMAIL = RestPass_email.getText().toString();

                    send_emailReset(EMAIL);
                }
                break;


            case R.id.confirm_rest:


                if (!TextUtils.isEmpty(temporary_password.getText().toString())) {
                    check_tempPassword(EMAIL, temporary_password.getText().toString());
                } else {
                    //Toast.makeText(this, "scscsc", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.change_rest:

                if (!TextUtils.isEmpty(change_password.getText().toString())) {
                    reset_password(USER_ID, change_password.getText().toString());
                }

                break;


        }
    }


    void send_emailReset(String email) {


        CommonUtilities.showStaticDialog(this , "sendRest");
        Map<String, String> parms = new HashMap<>();
        parms.put("email", email);
        myAPI.send_emailReset(parms)
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

                            email_layout.setVisibility(View.GONE);
                            temp_layout.setVisibility(View.VISIBLE);
                            pass_layout.setVisibility(View.GONE);

                            Toast.makeText(ResetPasswordScreen.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(ResetPasswordScreen.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ResetPasswordScreen.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Log.i("ERROR_RETROFIT", e.getMessage());
                        CommonUtilities.hideDialog();

                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }


    void check_tempPassword(String email, String temp) {


        CommonUtilities.showStaticDialog(this, "");
        Map<String, String> parms = new HashMap<>();
        parms.put("email", email);
        parms.put("temp_password", temp);
        myAPI.check_tempPassword(parms)
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


                            email_layout.setVisibility(View.GONE);
                            temp_layout.setVisibility(View.GONE);
                            pass_layout.setVisibility(View.VISIBLE);

                            Toast.makeText(ResetPasswordScreen.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            USER_ID = String.valueOf(apiResponse.getData().getUser().getId());
                        } else {
                            Toast.makeText(ResetPasswordScreen.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ResetPasswordScreen.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Log.i("ERROR_RETROFIT", e.getMessage());
                        CommonUtilities.hideDialog();

                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }


    void reset_password(String user_id, String password) {


        CommonUtilities.showStaticDialog(this , "");
        Map<String, String> parms = new HashMap<>();
        parms.put("user_id", user_id);
        parms.put("password", password);
        myAPI.reset_password(parms)
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


                            Toast.makeText(ResetPasswordScreen.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            Toast.makeText(ResetPasswordScreen.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(ResetPasswordScreen.this, dataManager.getID(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ResetPasswordScreen.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Log.i("ERROR_RETROFIT", e.getMessage());
                        CommonUtilities.hideDialog();

                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }
}
