package wakeb.tech.drb.Profile;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.transition.Slide;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import wakeb.tech.drb.Activities.ShowImage;
import wakeb.tech.drb.Activities.ViewRecourse;
import wakeb.tech.drb.Adapters.TripsAdapter;
import wakeb.tech.drb.Adapters.UsersSuggestionAdapter;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Models.PostedTrip;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

public class MyProfile extends BaseActivity implements UsersSuggestionAdapter.Refresh_suggestions, TripsAdapter.AdapterCallback {


    @BindView(R.id.ProfileVerified)
    ImageView ProfileVerified;


    @BindView(R.id.MyProfile_Image)
    CircleImageView MyProfile_Image;


    @OnClick(R.id.MyProfile_Image)
    void view_recourse_image()
    {
        Intent intent =  new Intent(this , ShowImage.class);
        intent.putExtra("URL" , User_Image);
        startActivity(intent);
    }


    @OnClick(R.id.back_button)
    void back_button() {
        finish();
    }

    @BindView(R.id.MyProfile_displayName)
    TextView MyProfile_displayName;

    @BindView(R.id.email_address)
    TextView email_address;

    @BindView(R.id.bio)
    TextView bio;

    @OnClick(R.id.MyProfile_editProfile)
    void editProfile() {
        startActivity(new Intent(this, EditProfile.class));
    }

    @OnClick(R.id.MyProfile_activityLog)
    void MyProfile_activityLog() {


        Intent intent = new Intent(MyProfile.this, FollowersList.class);
        intent.putExtra("FLAG", "LOGS");
        intent.putExtra("ItemID", dataManager.getID());
        startActivity(intent);


    }
    @OnClick(R.id.notifications)
    void notifications() {

        Intent intent =  new Intent(this , FollowersList.class);
        intent.putExtra("FLAG" , "NOTIFICATIONS");
        intent.putExtra("ItemID" , dataManager.getID());
        startActivity(intent);


    }

    @OnClick(R.id.MyProfile_followers)
    void MyProfile_followers() {
        Intent intent = new Intent(MyProfile.this, FollowersList.class);
        intent.putExtra("FLAG", "FOLLOWERS");
        intent.putExtra("ItemID", dataManager.getID());
        startActivity(intent);
    }

    @OnClick(R.id.MyProfile_following)
    void MyProfile_following() {
        Intent intent = new Intent(MyProfile.this, FollowersList.class);
        intent.putExtra("FLAG", "FOLLOWING");
        intent.putExtra("ItemID", dataManager.getID());
        startActivity(intent);
    }

    @BindView(R.id.MyProfile_tripsText)
    TextView MyProfile_tripsText;

    @BindView(R.id.MyProfile_followersText)
    TextView MyProfile_followersText;

    @BindView(R.id.MyProfile_followingText)
    TextView MyProfile_followingText;

    @BindView(R.id.MyProfile_trips)
    RecyclerView MyProfile_trips;

    @BindView(R.id.card_view)
    CardView card_view;

    @BindView(R.id.MyProfile_suggestions)
    RecyclerView MyProfile_suggestions;

    @BindView(R.id.empty_list)
    RelativeLayout empty_list;

    @BindView(R.id.MyProfile_swipeRefreshLayout)
    SwipeRefreshLayout MyProfile_swipeRefreshLayout;

    @BindView(R.id.MyProfile_nestedScrollView)
    NestedScrollView MyProfile_nestedScrollView;

    @BindView(R.id.suggestions_layout)
    LinearLayout suggestions_layout;

    @BindView(R.id.bio_layout)
    LinearLayout bio_layout;

    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;

    ArrayList<PostedTrip> my_data;
    TripsAdapter tripsAdapter;
    int page_number = 1;
    boolean next = true;
    String ItemID, User_Image, User_Name;

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
        setContentView(R.layout.activity_my_profile);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                User_Name = null;
                User_Image = null;
            } else {
                User_Name = extras.getString("User_Name");
                User_Image = extras.getString("User_Image");
            }
        } else {
            User_Name = (String) savedInstanceState.getSerializable("User_Name");
            User_Image = (String) savedInstanceState.getSerializable("User_Image");
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        card_view.setLayoutParams(new LinearLayout.LayoutParams(width, height + 40));

        init();
        get_profile();
        get_suggestions();


        MyProfile_swipeRefreshLayout.setRefreshing(true);


        MyProfile_suggestions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        MyProfile_suggestions.setNestedScrollingEnabled(false);


        MyProfile_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page_number = 1;
                next = true;
                get_profile();
                get_suggestions();
            }
        });


        MyProfile_nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == 0) {

                }
                if (scrollY > oldScrollY) {

                }
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    if (next) {
                        get_more();
                    }

                }
            }
        });

    }

    @Override
    protected void setViewListeners() {

    }

    @Override
    protected void init() {
        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
        my_data = new ArrayList<>();

        MyProfile_trips.setLayoutManager(new LinearLayoutManager(this));
        tripsAdapter = new TripsAdapter(MyProfile.this, my_data, dataManager, retrofit, myAPI, MyProfile.this);
        MyProfile_trips.setAdapter(tripsAdapter);

    }

    @Override
    protected boolean isValidData() {
        return false;
    }


    void get_profile() {
        my_data.clear();

        Map<String, String> parms = new HashMap<>();
        parms.put("user_id", dataManager.getID());
        parms.put("publisher_id", dataManager.getID());
        parms.put("page", String.valueOf(page_number));
        myAPI.get_profile(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {
                            page_number++;

                            if (apiResponse.getData().getProfile().getMeta().getNextPageUrl().equals("")) {
                                next = false;
                            }

                            MyProfile_displayName.setText(apiResponse.getData().getProfile().getPublisher().getDisplayName());
                            Glide.with(getApplicationContext())
                                    .load(apiResponse.getData().getProfile().getPublisher().getImage())
                                    .apply(new RequestOptions()
                                            .placeholder(MyProfile_Image.getDrawable())
                                    )
                                    .into(MyProfile_Image);

                            User_Image = apiResponse.getData().getProfile().getPublisher().getImage();

                            MyProfile_tripsText.setText(apiResponse.getData().getProfile().getPublisher().getTrips_count());
                            MyProfile_followersText.setText(apiResponse.getData().getProfile().getPublisher().getFollower() + "");
                            MyProfile_followingText.setText(apiResponse.getData().getProfile().getPublisher().getFollow() + "");

                            if (!apiResponse.getData().getProfile().getPublisher().getBio().equals("")) {
                                bio_layout.setVisibility(View.VISIBLE);
                                bio.setText(apiResponse.getData().getProfile().getPublisher().getBio());
                            }
                            email_address.setText(apiResponse.getData().getProfile().getPublisher().getEmail());

                            my_data.addAll(apiResponse.getData().getProfile().getPostedTrips());


                            if (apiResponse.getData().getProfile().getPublisher().getVerified() == 1) {
                                ProfileVerified.setVisibility(View.VISIBLE);
                            }


                            if (apiResponse.getData().getProfile().getPostedTrips().size() == 0) {
                                empty_list.setVisibility(View.VISIBLE);
                            } else {
                                empty_list.setVisibility(View.GONE);

                            }


                        } else {

                            Toast.makeText(MyProfile.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MyProfile.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                        tripsAdapter.notifyDataSetChanged();
                        try {
                            MyProfile_swipeRefreshLayout.setRefreshing(false);
                        } catch (Exception e) {
                        }
                    }
                });
    }

    void get_more() {

        Map<String, String> parms = new HashMap<>();
        parms.put("user_id", dataManager.getID());
        parms.put("publisher_id", dataManager.getID());
        parms.put("page", String.valueOf(page_number));

        myAPI.get_profile(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {
                            page_number++;

                            if (apiResponse.getData().getProfile().getMeta().getNextPageUrl().equals("")) {
                                next = false;
                            }
                            my_data.addAll(apiResponse.getData().getProfile().getPostedTrips());

                        } else {

                            Toast.makeText(MyProfile.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MyProfile.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                        tripsAdapter.notifyDataSetChanged();

                        try {
                            MyProfile_swipeRefreshLayout.setRefreshing(false);
                        } catch (Exception e) {
                        }
                    }
                });
    }

    void get_suggestions() {


        Map<String, String> parms = new HashMap<>();
        parms.put("user_id", dataManager.getID());
        myAPI.random_publisher(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {

                            if (apiResponse.getData().getSuggested_users().size() == 0) {
                                suggestions_layout.setVisibility(View.GONE);
                            } else {
                                suggestions_layout.setVisibility(View.VISIBLE);

                            }
                            MyProfile_suggestions.setAdapter(new UsersSuggestionAdapter(MyProfile.this, apiResponse.getData().getSuggested_users(), dataManager, myAPI, MyProfile.this));
                        } else {

                            Toast.makeText(MyProfile.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MyProfile.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Log.i("ERROR_RETROFIT", e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                        try {
                            CommonUtilities.hideDialog();
                        } catch (Exception e) {
                        }

                    }
                });
    }


    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();

        super.onBackPressed();

    }

    @Override
    public void refresh_suggestion() {
        CommonUtilities.showStaticDialog(MyProfile.this, "");
        get_suggestions();
    }

    @Override
    public void onItemClicked(String ID) {

    }
}
