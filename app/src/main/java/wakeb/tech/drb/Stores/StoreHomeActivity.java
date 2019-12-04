package wakeb.tech.drb.Stores;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import wakeb.tech.drb.Adapters.PlacesAdapter;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Dialogs.NewPlaceDialog;
import wakeb.tech.drb.Home.EventBusMssg_home;
import wakeb.tech.drb.Home.HomeInterface;
import wakeb.tech.drb.Home.SelectLocation;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Registration.LoginScreen;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.Uitils.DefaultExceptionHandler;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

public class StoreHomeActivity extends BaseActivity implements View.OnClickListener, StoresInterface {


    String trip_address;
    double lat, lng;
    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;


    @OnClick(R.id.add_new_place)
    void add_new_resource() {
        startActivityForResult(new Intent(StoreHomeActivity.this, SelectLocation.class), 1003);
    }


    @OnClick(R.id.Logout)
    void Logout() {
        dataManager.setLoggedIn(false);
        startActivity(new Intent(this, LoginScreen.class));
        finish();
    }



    @BindView(R.id.stores)
    RecyclerView stores;

    @BindView(R.id.empty_list)
    RelativeLayout empty_list;

    boolean doubleBackToExitPressedOnce = false;


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
        setContentView(R.layout.activity_store_home_screen);


        init();
        get_places();
    }



    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
        stores.setLayoutManager(new LinearLayoutManager(this));
    }



    @Override
    public void onClick(View v) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case 1003:
                try {
                    trip_address = data.getStringExtra("address");
                    lat = Double.parseDouble(data.getStringExtra("latitude"));
                    lng = Double.parseDouble(data.getStringExtra("longitude"));


                    NewPlaceDialog cdd = new NewPlaceDialog(StoreHomeActivity.this, trip_address);
                    cdd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


                    Window window = cdd.getWindow();
                    window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.CENTER);


                    WindowManager.LayoutParams lp = window.getAttributes();
                    lp.dimAmount = 0.7f;
                    lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    cdd.getWindow().setAttributes(lp);

                    cdd.show();

                } catch (Exception e) {

                }

                break;


        }

    }

    @Subscribe
    public void onEvent(String desc) {
        add_new_place(desc);
    }

    void add_new_place(String desc) {


        Map<String, String> parms = new HashMap<>();
        parms.put("store_id", dataManager.getID());
        parms.put("lat", String.valueOf(lat));
        parms.put("lng", String.valueOf(lng));
        parms.put("address", trip_address);
        parms.put("desc", desc);

        myAPI.add_store_place(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {

                            get_places();

                            Toast.makeText(StoreHomeActivity.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(StoreHomeActivity.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(StoreHomeActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }

    void get_places() {

        CommonUtilities.showStaticDialog(this, "");

        Map<String, String> parms = new HashMap<>();
        parms.put("store_id", dataManager.getID());
        myAPI.get_places(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {

                            if(apiResponse.getData().getStorePlaces().size()==0)
                            {
                                empty_list.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                empty_list.setVisibility(View.GONE);

                            }

                            stores.setAdapter(new PlacesAdapter(StoreHomeActivity.this, apiResponse.getData().getStorePlaces()));

                        } else {

                            Toast.makeText(StoreHomeActivity.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(StoreHomeActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        CommonUtilities.hideDialog();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtilities.hideDialog();

                    }
                });
    }

    @Override
    public void remove_place(final String id) {


        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.delete_place)
                .setMessage(R.string.delete_your_place)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {



                        Map<String, String> parms = new HashMap<>();
                        parms.put("storePlace_id", id);


                        myAPI.delete_store_place(parms)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<ApiResponse>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(ApiResponse apiResponse) {



                                        if (apiResponse.getStatus()) {
                                            get_places();
                                            Toast.makeText(StoreHomeActivity.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(StoreHomeActivity.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                        }


                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(StoreHomeActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();


                                    }

                                    @Override
                                    public void onComplete() {


                                    }
                                });
                    }
                }).show();



    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.click_agin_to_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
