package wakeb.tech.drb.Profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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

public class UserProfile extends BaseActivity implements TripsAdapter.AdapterCallback {

   /* @BindView(R.id.profile_trust)
    ImageView profile_trust;*/

    @BindView(R.id.bio_layout)
    LinearLayout bio_layout;

    @BindView(R.id.email_address)
    TextView email_address;
    @BindView(R.id.bio)
    TextView bio;


    @BindView(R.id.userProfile_Image)
    CircleImageView userProfile_Image;

    @BindView(R.id.userProfile_displayName)
    TextView userProfile_displayName;


    @BindView(R.id.MyProfile_tripsText)
    TextView MyProfile_tripsText;

    boolean follow_status = false;

    @OnClick(R.id.userProfile_Follow)
    void userProfile_Follow() {
        userProfile_swipeRefreshLayout.setRefreshing(true);

        add_follow(ItemID);
    }
    @OnClick(R.id.userProfile_Image)
    void view_recourse_image()
    {
        Intent intent =  new Intent(this , ShowImage.class);
        intent.putExtra("URL" , User_Image);
        startActivity(intent);
    }


    @OnClick(R.id.userProfile_followers)
    void userProfile_followers() {

        Intent intent = new Intent(UserProfile.this, FollowersList.class);
        intent.putExtra("FLAG", "FOLLOWERS");
        intent.putExtra("ItemID", ItemID);
        startActivity(intent);

    }


    @OnClick(R.id.userProfile_following)
    void userProfile_following() {
        Intent intent = new Intent(UserProfile.this, FollowersList.class);
        intent.putExtra("FLAG", "FOLLOWING");
        intent.putExtra("ItemID", ItemID);
        startActivity(intent);
    }



    @BindView(R.id.userProfile_Follow)
    Button userProfile_Follow;


    @BindView(R.id.ProfileVerified)
    ImageView ProfileVerified;


    @BindView(R.id.userProfile_followersText)
    TextView userProfile_followersText;


    @BindView(R.id.userProfile_followingText)
    TextView userProfile_followingText;


    @BindView(R.id.card_view)
    CardView card_view;

    @BindView(R.id.userProfile_trips)
    RecyclerView userProfile_trips;

    @BindView(R.id.userProfile_swipeRefreshLayout)
    SwipeRefreshLayout userProfile_swipeRefreshLayout;

    @BindView(R.id.userProfile_nestedScrollView)
    NestedScrollView userProfile_nestedScrollView;

    @BindView(R.id.empty_list)
    RelativeLayout empty_list;
    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;

    ArrayList<PostedTrip> my_data;
    TripsAdapter tripsAdapter;
    int page_number = 1;
    boolean next = true;
    String ItemID, BlockStatus ,User_Image;

    ProfileViewModel profileViewModel;


    @BindView(R.id.toolbar)
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));

        super.onCreate(savedInstanceState);
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


            ItemID = getIntent().getStringExtra("ItemID");

        setContentView(R.layout.activity_user_profile);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.my_profile));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_btn);


        userProfile_trips.setLayoutManager(new LinearLayoutManager(this));
        userProfile_swipeRefreshLayout.setRefreshing(true);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        card_view.setLayoutParams(new LinearLayout.LayoutParams(width, height + 40));

        init();
        get_profile(ItemID);

        userProfile_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                get_profile(ItemID);
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
        tripsAdapter = new TripsAdapter(UserProfile.this, my_data, dataManager, retrofit, myAPI, UserProfile.this);
        userProfile_trips.setAdapter(tripsAdapter);

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        profileViewModel.get_profile_spots(ItemID);


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
        userProfile_trips.setLayoutManager(layoutManager);
        userProfile_trips.setNestedScrollingEnabled(true);
        profileViewModel.getListLiveData().observe(this, new androidx.lifecycle.Observer<PagedList<SpotModel>>() {
            @Override
            public void onChanged(PagedList<SpotModel> doctor_models) {

                SpotsAdapter spotsAdapter = new SpotsAdapter();
                spotsAdapter.submitList(doctor_models);
                userProfile_trips.setAdapter(spotsAdapter);
            }
        });



    }



    void get_profile(String user_id) {

        Map<String, String> parms = new HashMap<>();
        parms.put("user_id",user_id );
        parms.put("publisher_id",dataManager.getID() );

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

                        if (apiResponse.getData().getUserProfileModel().getIsFollowed()) {
                            userProfile_Follow.setText(getString(R.string.unfollow));
                            follow_status = true;
                        } else {
                            userProfile_Follow.setText(getString(R.string.follow));
                            follow_status = false;

                        }

                         if (apiResponse.getData().getUserProfileModel().getIsBlocked()) {
                             BlockStatus = getString(R.string.un_block);
                        } else {
                             BlockStatus = getString(R.string.block);
                        }

                        if (apiResponse.getStatus()) {

 /*
                            userProfile_userName.setText("@" + apiResponse.getData().getUserProfileModel().getUsername());
*/
                            MyProfile_tripsText.setText(apiResponse.getData().getUserProfileModel().getSpots()+"");
                            userProfile_followersText.setText(apiResponse.getData().getUserProfileModel().getFollowing() + "");
                            userProfile_followingText.setText(apiResponse.getData().getUserProfileModel().getFollowers() + "");
                            if (apiResponse.getData().getUserProfileModel().getBio()!=null) {
                                bio_layout.setVisibility(View.VISIBLE);
                                bio.setText(apiResponse.getData().getUserProfileModel().getBio());
                            }
                            //userProfile_title.setText(apiResponse.getData().getUserProfileModel().getDisplayName());

                            userProfile_displayName.setText(apiResponse.getData().getUserProfileModel().getDisplayName());

                            Glide.with(getApplicationContext())
                                    .load("http://3.17.76.229/uploads/publishers/" +apiResponse.getData().getUserProfileModel().getImage())
                                    .apply(new RequestOptions()
                                            .placeholder(userProfile_Image.getDrawable())
                                    )
                                    .into(userProfile_Image);

                            User_Image = "http://3.17.76.229/uploads/publishers/" +apiResponse.getData().getUserProfileModel().getImage();
/*
                            mobile_number.setText(apiResponse.getData().getUserProfileModel().getMobile());
*/
                            email_address.setText(apiResponse.getData().getUserProfileModel().getEmail());


                            if (apiResponse.getData().getUserProfileModel().getVerified() == 1) {
                                ProfileVerified.setVisibility(View.VISIBLE);
                            }




                        } else {

                            Toast.makeText(UserProfile.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(UserProfile.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Log.i("ERROR_RETROFIT", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                         try {
                            userProfile_swipeRefreshLayout.setRefreshing(false);
                        } catch (Exception e) {
                        }
                    }
                });
    }


    void add_follow(String follower_id) {


        Map<String, String> parms = new HashMap<>();
        parms.put("publisher_id", follower_id);
        parms.put("user_id", dataManager.getID());
        myAPI.follow_action(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {
                            page_number = 1;
                            next = true;
                            get_profile(ItemID);
                            if (follow_status) {
                                CommonUtilities.save_log(myAPI, dataManager.getID(), "UNFOLLOW", "publisher", ItemID);

                            } else {
                                CommonUtilities.save_log(myAPI, dataManager.getID(), "FOLLOW", "publisher", ItemID);

                            }


                        } else {

                            Toast.makeText(UserProfile.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(UserProfile.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Log.i("ERROR_RETROFIT", e.getMessage());

                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }

    void action_block(String follower_id) {


        Map<String, String> parms = new HashMap<>();
        parms.put("publisher_id", follower_id);
        parms.put("user_id", dataManager.getID());
        myAPI.action_block(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {

                            Toast.makeText(UserProfile.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            finish();

                        } else {

                            Toast.makeText(UserProfile.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(UserProfile.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Log.i("ERROR_RETROFIT", e.getMessage());

                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }


    @Override
    public void onItemClicked(String ID) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_profile, menu);

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                final View menuItemView = findViewById(R.id.profile_more);

        menuItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(UserProfile.this, menuItemView);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.user_profile, popup.getMenu());
                Menu menu = popup.getMenu();
                menu.findItem(R.id.profile_more).setTitle(BlockStatus);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //do your things in each of the following cases
                        switch (item.getItemId()) {
                            case R.id.block_user:

                                userProfile_swipeRefreshLayout.setRefreshing(true);

                                action_block(ItemID);

                                return true;
                            default:
                                return false;
                        }

                    }
                });
                popup.show();
            }
        });


            }
        });



        return super.onCreateOptionsMenu(menu);
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
