package wakeb.tech.drb.Profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

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
import wakeb.tech.drb.Adapters.TripsAdapter;
import wakeb.tech.drb.Adapters.UsersSuggestionAdapter;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Models.PostedTrip;
import wakeb.tech.drb.Models.SpotModel;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.Uitils.DefaultExceptionHandler;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;
import wakeb.tech.drb.ui.profile.ProfileViewModel;
import wakeb.tech.drb.ui.spots.SpotsAdapter;

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


    @BindView(R.id.MyProfile_displayName)
    TextView MyProfile_displayName;

    @BindView(R.id.email_address)
    TextView email_address;

    @BindView(R.id.bio)
    TextView bio;


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


    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @BindView(R.id.suggestions_layout)
    LinearLayout suggestions_layout;

    @BindView(R.id.bio_layout)
    LinearLayout bio_layout;

    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;

    ArrayList<PostedTrip> my_data;

    String  User_Image, User_Name;

    ProfileViewModel profileViewModel;

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.my_profile));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_btn);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        card_view.setLayoutParams(new LinearLayout.LayoutParams(width, height + 40));

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);



        init();
        get_profile();
        get_suggestions();


        MyProfile_swipeRefreshLayout.setRefreshing(true);


        MyProfile_suggestions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        MyProfile_suggestions.setNestedScrollingEnabled(false);


        MyProfile_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_profile();
                get_suggestions();
                profileViewModel.refresh();
            }
        });

    }


    @Override
    protected void init() {
        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
        my_data = new ArrayList<>();


        profileViewModel.get_profile_spots(dataManager.getID());


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        // Create a custom SpanSizeLookup where the first item spans both columns
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if(position % 5 == 0)
                {
                    return 2;
                }
                else {
                    return 1;
                }

            }
        });
        MyProfile_trips.setLayoutManager(layoutManager);
        MyProfile_trips.setNestedScrollingEnabled(false);
        profileViewModel.getListLiveData().observe(this, new androidx.lifecycle.Observer<PagedList<SpotModel>>() {
            @Override
            public void onChanged(PagedList<SpotModel> doctor_models) {

                SpotsAdapter spotsAdapter = new SpotsAdapter();
                spotsAdapter.submitList(doctor_models);
                MyProfile_trips.setAdapter(spotsAdapter);
            }
        });

    }



    void get_profile() {

        Map<String, String> parms = new HashMap<>();
        parms.put("user_id", dataManager.getID());
        parms.put("publisher_id", dataManager.getID());
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


                            MyProfile_displayName.setText(apiResponse.getData().getUserProfileModel().getDisplayName());
                            Glide.with(getApplicationContext())
                                    .load("http://3.17.76.229/uploads/publishers/" + apiResponse.getData().getUserProfileModel().getImage())
                                    .apply(new RequestOptions()
                                            .placeholder(MyProfile_Image.getDrawable())
                                    )
                                    .into(MyProfile_Image);

                            User_Image = "http://3.17.76.229/uploads/publishers/" + apiResponse.getData().getUserProfileModel().getImage();

                            MyProfile_tripsText.setText(apiResponse.getData().getUserProfileModel().getSpots() + "");
                            MyProfile_followersText.setText(apiResponse.getData().getUserProfileModel().getFollowing() + "");
                            MyProfile_followingText.setText(apiResponse.getData().getUserProfileModel().getFollowers() + "");

                            if (apiResponse.getData().getUserProfileModel().getBio() != null) {
                                bio_layout.setVisibility(View.VISIBLE);
                                bio.setText(apiResponse.getData().getUserProfileModel().getBio());
                            }
                            email_address.setText(apiResponse.getData().getUserProfileModel().getEmail());


                            if (apiResponse.getData().getUserProfileModel().getVerified() == 1) {
                                ProfileVerified.setVisibility(View.VISIBLE);
                            }



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;


            case  R.id.edit_profile:

                startActivity(new Intent(this, EditProfile.class));

                break;


            case  R.id.log_activities:
                Intent intent = new Intent(MyProfile.this, FollowersList.class);
                intent.putExtra("FLAG", "LOGS");
                intent.putExtra("ItemID", dataManager.getID());
                startActivity(intent);
                break;


            case  R.id.notification:
                Intent intent2 =  new Intent(this , FollowersList.class);
                intent2.putExtra("FLAG" , "NOTIFICATIONS");
                intent2.putExtra("ItemID" , dataManager.getID());
                startActivity(intent2);

                break;
        }
        return super.onOptionsItemSelected(item);
    }


}