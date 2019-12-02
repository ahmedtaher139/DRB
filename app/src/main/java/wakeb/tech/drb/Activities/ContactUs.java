package wakeb.tech.drb.Activities;

import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

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
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

public class ContactUs extends BaseActivity {


    @BindView(R.id.contact_number)
    TextInputEditText contact_number;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.contact_email)
    TextInputEditText contact_email;

    @BindView(R.id.contact_subject)
    TextInputEditText contact_subject;

    @BindView(R.id.contact_desc)
    TextInputEditText contact_desc;


    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;

    @OnClick(R.id.Upload)
    void Upload()
    {
        CommonUtilities.showStaticDialog(this , "CONTACT_US");
        Map<String, String> parms = new HashMap<>();
        parms.put("publisher_id", dataManager.getID());
        parms.put("contact_number", contact_number.getText().toString());
        parms.put("email", contact_email.getText().toString());
        parms.put("subject", contact_subject.getText().toString());
        parms.put("desc", contact_desc.getText().toString());

        myAPI.contactUs(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {

                            Toast.makeText(ContactUs.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            Toast.makeText(ContactUs.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ContactUs.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Log.i("ERROR_RETROFIT", e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        CommonUtilities.hideDialog();

                     }
                });
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
        }
        else
        {
            Window window = getWindow();
            window.setBackgroundDrawableResource(R.drawable.background_png);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.contact_us));
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
