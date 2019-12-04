package wakeb.tech.drb.Registration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import wakeb.tech.drb.Activities.TextsView;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.Uitils.DefaultExceptionHandler;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

public class SignUp extends BaseActivity implements View.OnClickListener, StoresType_Interface {

    @BindView(R.id.SignUp_user_radio)
    LinearLayout SignUp_user_radio;

    @BindView(R.id.SignUp_store_radio)
    LinearLayout SignUp_store_radio;

    @OnClick(R.id.back_button)
    void back_button() {
        finish();
    }

    @BindView(R.id.LogIn_user_layout_image)
    ImageView LogIn_user_layout_image;

    @BindView(R.id.LogIn_user_layout_text)
    TextView LogIn_user_layout_text;

    @BindView(R.id.LogIn_store_layout_image)
    ImageView LogIn_store_layout_image;

    @BindView(R.id.LogIn_store_layout_text)
    TextView LogIn_store_layout_text;

    @OnClick(R.id.SignUp_user_radio)
    void SignUp_user_radio()
    {

         choose_user();
    }

    @OnClick(R.id.SignUp_store_radio)
    void SignUp_store_radio()
    {
        choose_store();
    }

    @BindView(R.id.SignUp_Store_displayname)
    TextInputEditText SignUp_Store_displayname;

    @BindView(R.id.SignUp_Store_mobile)
    TextInputEditText SignUp_Store_mobile;

    @BindView(R.id.SignUp_Store_emailaddress)
    TextInputEditText SignUp_Store_emailaddress;

    @BindView(R.id.SignUp_Store_password)
    TextInputEditText SignUp_Store_password;

    @BindView(R.id.SignUp_Store_type)
    TextView SignUp_Store_type;

    @BindView(R.id.SignUp_Terms)
    TextView SignUp_Terms;


    @BindView(R.id.SignIn)
    Button SignIn;

    @BindView(R.id.CheckBox_Terms)
    CheckBox CheckBox_Terms;

    @BindView(R.id.User_Layout)
    LinearLayout User_Layout;

    @BindView(R.id.Store_Layout)
    LinearLayout Store_Layout;

    @BindView(R.id.SignUp_username)
    TextInputEditText SignUp_username;

    @BindView(R.id.SignUp_displayname)
    TextInputEditText SignUp_displayname;

    @BindView(R.id.SignUp_mobile)
    TextInputEditText SignUp_mobile;

    @BindView(R.id.SignUp_emailaddress)
    TextInputEditText SignUp_emailaddress;

    @BindView(R.id.SignUp_password)
    TextInputEditText SignUp_password;

    @BindView(R.id.SignUp_Bio)
    TextInputEditText SignUp_Bio;


    @BindView(R.id.SignUp_user_contryCode)
    CountryCodePicker SignUp_user_contryCode;


    @BindView(R.id.SignUp_store_contryCode)
    CountryCodePicker SignUp_store_contryCode;


    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;

    String FLAG = "USER";

    String countryCode, TYPE_ID;

    boolean Terms = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));

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
        }
        else
        {
            Window window = getWindow();
            window.setBackgroundDrawableResource(R.drawable.background_png);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        choose_user();
        init();
        SignUp_Store_type.setOnClickListener(this);
        SignUp_Terms.setOnClickListener(this);
        SignIn.setOnClickListener(this);
        CheckBox_Terms.setOnClickListener(this);

    }


    @Override
    protected void init() {
        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);

        countryCode = SignUp_user_contryCode.getDefaultCountryCode();

        SignUp_user_contryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = SignUp_user_contryCode.getSelectedCountryCode();
            }
        });
        SignUp_store_contryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = SignUp_store_contryCode.getSelectedCountryCode();
            }
        });
        SignUp_Terms.setPaintFlags(SignUp_Terms.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);


    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.SignUp_Store_type:
                get_storeTypes();
                break;

            case R.id.SignUp_Terms:

                Intent intent = new Intent(SignUp.this , TextsView.class);
                intent.putExtra("FLAG" , "TERMS");
                startActivity(intent);

                break;


            case R.id.SignIn:

                if (Terms) {

                    if (FLAG.equals("USER")) {
                        signUp_user();
                    } else if (FLAG.equals("STORE")) {
                        signUp_store();
                    }
                } else {
                    Toast.makeText(this, "you have to agree on Terms &amp; Conditions ", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.CheckBox_Terms:

                if (CheckBox_Terms.isChecked()) {
                    Terms = true;
                } else {
                    Terms = false;
                }

                break;
        }
    }

    void get_storeTypes() {

        CommonUtilities.showStaticDialog(this , "getStoreType");
        Map<String, String> parms = new HashMap<>();
        parms.put("username", "test");
        myAPI.get_storeTypes(parms)
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
                            StoresTypes_Dialog cdd = new StoresTypes_Dialog((Activity) SignUp.this, apiResponse.getData().getStoresTypes(), dataManager);
                            cdd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            cdd.setCanceledOnTouchOutside(false);
                            cdd.setCancelable(false);
                            cdd.show();


                            Window window = cdd.getWindow();
                            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

                            window.setLayout(width, height);

                        } else {

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SignUp.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
                        Log.i("ERROR_RETROFIT", e.getMessage());
                        CommonUtilities.hideDialog();
                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }

    void signUp_user() {
        CommonUtilities.showStaticDialog(this , "signUp");



        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();

                Map<String, String> parms = new HashMap<>();
                parms.put("username", SignUp_username.getText().toString());
                parms.put("display_name", SignUp_displayname.getText().toString());
                parms.put("mobile", SignUp_mobile.getText().toString());
                parms.put("email", SignUp_emailaddress.getText().toString());
                parms.put("city", countryCode);
                parms.put("password", SignUp_password.getText().toString());
                parms.put("device_id", mToken);
                parms.put("device_type", "android");
                parms.put("bio", SignUp_Bio.getText().toString());
                myAPI.user_register(parms)
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
                                    Toast.makeText(SignUp.this, "Successful", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent();
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();


                                } else {
                                    Toast.makeText(SignUp.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();

                                }


                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(SignUp.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                CommonUtilities.hideDialog();
                                Log.i("ERROR_RETROFIT", e.getMessage());

                            }

                            @Override
                            public void onComplete() {


                            }
                        });



            }});





    }

    void signUp_store() {
        CommonUtilities.showStaticDialog(this, "");


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();



                Map<String, String> parms = new HashMap<>();
                parms.put("store_name", SignUp_Store_displayname.getText().toString());
                parms.put("mobile", SignUp_Store_mobile.getText().toString());
                parms.put("email", SignUp_Store_emailaddress.getText().toString());
                parms.put("city", countryCode);
                parms.put("password", SignUp_Store_password.getText().toString());
                parms.put("device_id", mToken);
                parms.put("device_type", "android");
                parms.put("store_type_id", TYPE_ID);
                myAPI.store_register(parms)
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
                                    dataManager.saveType("STORE");
                                    dataManager.saveID(apiResponse.getData().getStore().getId().toString());
                                    dataManager.saveName(apiResponse.getData().getStore().getStoreName());
                                    dataManager.saveImage(apiResponse.getData().getStore().getImage());
                                    Toast.makeText(SignUp.this, "Successful", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent();
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();


                                } else {
                                    Toast.makeText(SignUp.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();

                                }


                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(SignUp.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                CommonUtilities.hideDialog();

                                Log.i("ERROR_RETROFIT", e.getMessage());
                            }

                            @Override
                            public void onComplete() {


                            }
                        });

            }});


    }

    void choose_user() {


        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            SignUp_user_radio.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.curved_primary_dark_layout) );
            SignUp_store_radio.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.curved_light_layout) );
        } else {
            SignUp_user_radio.setBackground(ContextCompat.getDrawable(this, R.drawable.curved_primary_dark_layout));
            SignUp_store_radio.setBackground(ContextCompat.getDrawable(this, R.drawable.curved_light_layout));
        }

        LogIn_user_layout_image.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        LogIn_user_layout_text.setTextColor((getResources().getColor(R.color.white)));

        LogIn_store_layout_image.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        LogIn_store_layout_text.setTextColor((getResources().getColor(R.color.grey)));


        Store_Layout.setVisibility(View.GONE);
        User_Layout.setVisibility(View.VISIBLE);
        FLAG = "USER";
    }

    void choose_store() {

        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            SignUp_user_radio.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.curved_light_layout) );
            SignUp_store_radio.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.curved_primary_dark_layout) );
        } else {
            SignUp_user_radio.setBackground(ContextCompat.getDrawable(this, R.drawable.curved_light_layout));
            SignUp_store_radio.setBackground(ContextCompat.getDrawable(this, R.drawable.curved_primary_dark_layout));
        }

        LogIn_user_layout_image.setColorFilter(ContextCompat.getColor(this, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        LogIn_user_layout_text.setTextColor((getResources().getColor(R.color.grey)));

        LogIn_store_layout_image.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        LogIn_store_layout_text.setTextColor((getResources().getColor(R.color.white)));

        Store_Layout.setVisibility(View.VISIBLE);
        User_Layout.setVisibility(View.GONE);
        FLAG = "STORE";

    }

    @Override
    public void setType(String typeID, String Name) {
        SignUp_Store_type.setText(Name);
        TYPE_ID = typeID;
    }
}
