package wakeb.tech.drb.ui.addNewSpot;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

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

public class CreateNewJourney extends BaseActivity {

    DataManager dataManager;
    ApiServices myAPI;
    Retrofit retrofit;

    @OnClick(R.id.back_button)
    void back_button() {
        finish();
    }

    @BindView(R.id.journey_name)
    TextView journey_name;

    @BindView(R.id.journey_desc)
    TextView journey_desc;

    @OnClick(R.id.Upload)
    void Upload() {

        CommonUtilities.showStaticDialog(this, "");

        Map<String, String> parms = new HashMap<>();
        parms.put("publisher_id", dataManager.getID());
        parms.put("name", journey_name.getText().toString());
        parms.put("desc", journey_desc.getText().toString());
        myAPI.create_journey(parms)
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


                        } else {

                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("ERROR_RETROFIT", e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        CommonUtilities.hideDialog();

                        finish();
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
        } else {
            Window window = getWindow();
            window.setBackgroundDrawableResource(R.drawable.background_png);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_journey);

        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
    }

    @Override
    protected void init() {

    }
}
