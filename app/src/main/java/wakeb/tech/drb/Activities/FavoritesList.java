package wakeb.tech.drb.Activities;


import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import wakeb.tech.drb.Adapters.TripsAdapter;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Models.PostedTrip;
import wakeb.tech.drb.R;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

public class FavoritesList extends BaseActivity {
    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;

    @OnClick(R.id.back_button)
    void back_button() {
        finish();
    }

    @BindView(R.id.card_view)
    CardView card_view;


    @BindView(R.id.myTrips)
    RecyclerView myTrips;

    @BindView(R.id.myTrips_NestedScrollView)
    NestedScrollView myTrips_NestedScrollView;

    @BindView(R.id.myTrips_SwipeRefreshLayout)
    SwipeRefreshLayout myTrips_SwipeRefreshLayout;


    TripsAdapter.AdapterCallback adapterCallback;

    ArrayList<PostedTrip> postedTrips_list;
    int page_number = 1;
    boolean next = true;
    TripsAdapter tripsAdapter;
    @BindView(R.id.empty_list)
    RelativeLayout empty_list;

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
        setContentView(R.layout.activity_favorites_list);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        card_view.setLayoutParams(new LinearLayout.LayoutParams(width, height+ 40));
        init();

        myTrips_SwipeRefreshLayout.setRefreshing(true);
        myTrips_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                postedTrips_list.clear();
                page_number = 1;
                next = true;
                get_public();
            }
        });


        myTrips.setLayoutManager(new LinearLayoutManager(this));
        tripsAdapter = new TripsAdapter(this, postedTrips_list, dataManager, retrofit, myAPI, adapterCallback);
        myTrips.setAdapter(tripsAdapter);
        myTrips_NestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == 0) {

                }
                if (scrollY > oldScrollY) {

                }
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    get_public();
                }
            }
        });

        get_public();


    }



    @Override
    protected void init() {
        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
        postedTrips_list = new ArrayList<>();
    }



    void get_public() {

        if (next) {
            Map<String, String> parms = new HashMap<>();
            parms.put("user_id", dataManager.getID());
            parms.put("page", String.valueOf(page_number));
            myAPI.list_favourit(parms)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ApiResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ApiResponse apiResponse) {


                            if (apiResponse.getStatus()) {

                                if (apiResponse.getData().getPublishing().getPulishings().size() == 0) {
                                    empty_list.setVisibility(View.VISIBLE);
                                } else {
                                    empty_list.setVisibility(View.GONE);

                                }

                                page_number++;
                                if (apiResponse.getData().getPublishing().getMeta().getNextPageUrl().equals("")) {
                                    next = false;
                                }
                                if (apiResponse.getData().getPublishing().getPulishings().size() == 0) {

                                } else {
                                    postedTrips_list.addAll(apiResponse.getData().getPublishing().getPulishings());

                                }
                                try {
                                    myTrips_SwipeRefreshLayout.setRefreshing(false);
                                } catch (Exception e) {
                                }
                            } else {
                                Toast.makeText(FavoritesList.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(FavoritesList.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();

                            Log.i("ERROR_RETROFIT", e.getMessage());

                        }

                        @Override
                        public void onComplete() {

                            tripsAdapter.notifyDataSetChanged();
                        }
                    });
        }


    }
}
