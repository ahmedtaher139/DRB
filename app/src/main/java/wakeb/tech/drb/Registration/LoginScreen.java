package wakeb.tech.drb.Registration;

import android.content.Intent;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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
import wakeb.tech.drb.Home.HomeActivity;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Stores.StoreHomeActivity;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

public class LoginScreen extends BaseActivity {

    @BindView(R.id.LogIn_user_layout)
    LinearLayout LogIn_user_radio;


    @BindView(R.id.LogIn_user_layout_image)
    ImageView LogIn_user_layout_image;

    @BindView(R.id.LogIn_user_layout_text)
    TextView LogIn_user_layout_text;


    @BindView(R.id.LogIn_store_layout)
    LinearLayout LogIn_store_radio;

    @BindView(R.id.LogIn_store_layout_image)
    ImageView LogIn_store_layout_image;

    @BindView(R.id.LogIn_store_layout_text)
    TextView LogIn_store_layout_text;


    @BindView(R.id.LogIn_emailAddress)
    TextInputEditText LogIn_emailAddress;

    @BindView(R.id.LogIn_password)
    TextInputEditText LogIn_password;

    @BindView(R.id.LogIn_forgetPass)
    TextView LogIn_forgetPass;

    @BindView(R.id.LogIn_signUp)
    TextView LogIn_signUp;

    @BindView(R.id.LogIn_signin)
    Button LogIn_signin;


    @OnClick(R.id.LogIn_user_layout)
    void LogIn_user_radio() {


        choose_user();
    }

    @OnClick(R.id.LogIn_store_layout)
    void LogIn_store_radio() {

        choose_store();
    }

    @OnClick(R.id.LogIn_forgetPass)
    void LogIn_forgetPass() {
        startActivityForResult(new Intent(this, ResetPasswordScreen.class), 11);

    }

    @OnClick(R.id.LogIn_signUp)
    void LogIn_signUp() {
        startActivityForResult(new Intent(this, SignUp.class), 11);

    }

    @OnClick(R.id.LogIn_signin)
    void LogIn_signin() {
        if (FLAG.equals("USER")) {
            login_user();
        } else if (FLAG.equals("STORE")) {
            login_store();
        }

    }

    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;

    String FLAG = "USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);


        init();
        setViewListeners();
        launchHomeActivity();


    }

    private void launchHomeActivity() {
        if (dataManager.getLoggedInMode()) {

            if (dataManager.getType().equals("USER")) {

                startActivity(new Intent(this, HomeActivity.class));
                finish();

            } else if (dataManager.getType().equals("STORE")) {

                startActivity(new Intent(this, StoreHomeActivity.class));
                finish();
            }
        }
    }


    @Override
    protected void setViewListeners() {

    }

    @Override
    protected void init() {
        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
        choose_user();
    }

    @Override
    protected boolean isValidData() {
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 11:
                launchHomeActivity();
                break;
        }

    }

    void login_user() {
        CommonUtilities.showStaticDialog(this, "login");


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                Map<String, String> parms = new HashMap<>();
                parms.put("email", LogIn_emailAddress.getText().toString());
                parms.put("password", LogIn_password.getText().toString());
                parms.put("device_id", mToken);
                parms.put("device_type", "android");

                myAPI.login_user(parms)
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
                                    dataManager.setLoggedIn(true);
                                    dataManager.saveType("USER");
                                    dataManager.saveID(apiResponse.getData().getUser().getId().toString());
                                    dataManager.saveName(apiResponse.getData().getUser().getDisplayName());
                                    dataManager.saveImage(apiResponse.getData().getUser().getImage());
                                    startActivity(new Intent(LoginScreen.this, HomeActivity.class));
                                    finish();

                                } else {
                                    Toast.makeText(LoginScreen.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();

                                }


                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(LoginScreen.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                CommonUtilities.hideDialog();
                            }

                            @Override
                            public void onComplete() {


                            }
                        });

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                CommonUtilities.hideDialog();
                Toast.makeText(LoginScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    void login_store() {
        CommonUtilities.showStaticDialog(this, "loginStore");


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                Map<String, String> parms = new HashMap<>();
                parms.put("email", LogIn_emailAddress.getText().toString());
                parms.put("password", LogIn_password.getText().toString());
                parms.put("device_id", mToken);
                parms.put("device_type", "android");

                myAPI.login_store(parms)
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
                                    dataManager.setLoggedIn(true);
                                    dataManager.saveType("USER");
                                    dataManager.saveID(apiResponse.getData().getStore().getId().toString());
                                    dataManager.saveImage(apiResponse.getData().getStore().getImage());

                                    startActivity(new Intent(LoginScreen.this, StoreHomeActivity.class));
                                    finish();

                                } else {
                                    Toast.makeText(LoginScreen.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();

                                }


                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(LoginScreen.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                CommonUtilities.hideDialog();
                            }

                            @Override
                            public void onComplete() {


                            }
                        });

            }
        });

    }

    void choose_user() {
        FLAG = "USER";

        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            LogIn_user_radio.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.curved_primary_dark_layout));
            LogIn_store_radio.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.curved_light_layout));
        } else {
            LogIn_user_radio.setBackground(ContextCompat.getDrawable(this, R.drawable.curved_primary_dark_layout));
            LogIn_store_radio.setBackground(ContextCompat.getDrawable(this, R.drawable.curved_light_layout));
        }

        LogIn_user_layout_image.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        LogIn_user_layout_text.setTextColor((getResources().getColor(R.color.white)));

        LogIn_store_layout_image.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        LogIn_store_layout_text.setTextColor((getResources().getColor(R.color.grey)));
    }

    void choose_store() {
        FLAG = "STORE";
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            LogIn_user_radio.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.curved_light_layout));
            LogIn_store_radio.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.curved_primary_dark_layout));
        } else {
            LogIn_user_radio.setBackground(ContextCompat.getDrawable(this, R.drawable.curved_light_layout));
            LogIn_store_radio.setBackground(ContextCompat.getDrawable(this, R.drawable.curved_primary_dark_layout));
        }

        LogIn_user_layout_image.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        LogIn_user_layout_text.setTextColor((getResources().getColor(R.color.grey)));

        LogIn_store_layout_image.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        LogIn_store_layout_text.setTextColor((getResources().getColor(R.color.white)));

    }
}
