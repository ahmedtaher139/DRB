package wakeb.tech.drb.Home;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Retrofit;
import wakeb.tech.drb.Activities.FilterMap;
import wakeb.tech.drb.Profile.FollowersList;
import wakeb.tech.drb.Risks.AddNewRisk;
import wakeb.tech.drb.Activities.FavoritesList;
import wakeb.tech.drb.Activities.NewResource;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Profile.MyProfile;
import wakeb.tech.drb.R;
import wakeb.tech.drb.SuggestionPlaces.AddNewPlace;
import wakeb.tech.drb.SuggestionPlaces.SuggestedPlaces;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

public class HomeActivity extends BaseActivity  {

    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;
    boolean doubleBackToExitPressedOnce = false;


    @OnClick(R.id.add_new_risk)
    void add_new_risk() {
        Intent intent = new Intent(HomeActivity.this, AddNewRisk.class);
        startActivity(intent);
    }

    @OnClick(R.id.add_new_place)
    void add_new_place() {
        Intent intent = new Intent(HomeActivity.this, AddNewPlace.class);
        startActivity(intent);
    }

    @OnClick(R.id.filter)
    void filter() {
        Intent intent = new Intent(HomeActivity.this, FilterMap.class);
        startActivity(intent);
    }

    @OnClick(R.id.notifications)
    void notifications() {
        Intent intent =  new Intent(this , FollowersList.class);
        intent.putExtra("FLAG" , "NOTIFICATIONS");
        intent.putExtra("ItemID" , dataManager.getID());
        startActivity(intent);
    }

    @OnClick(R.id.notifications2)
    void notifications2() {
        Intent intent =  new Intent(this , FollowersList.class);
        intent.putExtra("FLAG" , "NOTIFICATIONS");
        intent.putExtra("ItemID" , dataManager.getID());
        startActivity(intent);
    }

    @OnClick(R.id.notifications3)
    void notifications3() {
        Intent intent =  new Intent(this , FollowersList.class);
        intent.putExtra("FLAG" , "NOTIFICATIONS");
        intent.putExtra("ItemID" , dataManager.getID());
        startActivity(intent);
    }

    @OnClick(R.id.notifications4)
    void notifications4() {
        Intent intent =  new Intent(this , FollowersList.class);
        intent.putExtra("FLAG" , "NOTIFICATIONS");
        intent.putExtra("ItemID" , dataManager.getID());
        startActivity(intent);
    }

    @OnClick(R.id.notifications5)
    void notifications5() {
        Intent intent =  new Intent(this , FollowersList.class);
        intent.putExtra("FLAG" , "NOTIFICATIONS");
        intent.putExtra("ItemID" , dataManager.getID());
        startActivity(intent);
    }





    @BindView(R.id.toolbar_home_layout)
    RelativeLayout toolbar_home_layout;

    @BindView(R.id.toolbar_trips_layout)
    RelativeLayout toolbar_trips_layout;

    @BindView(R.id.toolbar_risks_layout)
    RelativeLayout toolbar_risks_layout;

    @BindView(R.id.toolbar_more_layout)
    RelativeLayout toolbar_more_layout;

    @BindView(R.id.toolbar_places_layout)
    RelativeLayout toolbar_places_layout;



    @BindView(R.id.spaceNavigationView)
    SpaceNavigationView spaceNavigationView;


    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new TripsFragment();
    final Fragment fragment3 = new RisksFragment();
    final Fragment fragment4 = new MoreFragment();
    final Fragment fragment5 = new SuggestsPlaces();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    int colorGray, colorColored;

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
        setContentView(R.layout.activity_home);
        init();
        setViewListeners();
        colorGray = Color.parseColor("#858585");
        colorColored = Color.parseColor("#008577");

        fm.beginTransaction().add(R.id.Container, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.Container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.Container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.Container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.Container, fragment1, "1").commit();



        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("Home", R.drawable.ic_home));
        spaceNavigationView.addSpaceItem(new SpaceItem("Trips", R.drawable.ic_trips));
        spaceNavigationView.addSpaceItem(new SpaceItem("Places", R.drawable.ic_places));
        spaceNavigationView.addSpaceItem(new SpaceItem("More", R.drawable.ic_more));
        spaceNavigationView.showIconOnly();
        spaceNavigationView.setCentreButtonSelectable(true);


        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {

                fm.beginTransaction().hide(active).show(fragment3).commit();
                active = fragment3;


                toolbar_home_layout.setVisibility(View.GONE);
                toolbar_risks_layout.setVisibility(View.VISIBLE);
                toolbar_trips_layout.setVisibility(View.GONE);
                toolbar_more_layout.setVisibility(View.GONE);
                toolbar_places_layout.setVisibility(View.GONE);


            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {


                switch (itemIndex)
                {
                    case 0:
                        fm.beginTransaction().hide(active).show(fragment1).commit();
                        active = fragment1;

                        toolbar_home_layout.setVisibility(View.VISIBLE);
                        toolbar_risks_layout.setVisibility(View.GONE);
                        toolbar_trips_layout.setVisibility(View.GONE);
                        toolbar_more_layout.setVisibility(View.GONE);
                        toolbar_places_layout.setVisibility(View.GONE);


                        break;


                    case 1:

                        fm.beginTransaction().hide(active).show(fragment2).commit();
                        active = fragment2;

                        toolbar_home_layout.setVisibility(View.GONE);
                        toolbar_risks_layout.setVisibility(View.GONE);
                        toolbar_trips_layout.setVisibility(View.VISIBLE);
                        toolbar_more_layout.setVisibility(View.GONE);
                        toolbar_places_layout.setVisibility(View.GONE);


                        break;


                    case 2:

                        fm.beginTransaction().hide(active).show(fragment5).commit();
                        active = fragment5;


                        toolbar_home_layout.setVisibility(View.GONE);
                        toolbar_risks_layout.setVisibility(View.GONE);
                        toolbar_trips_layout.setVisibility(View.GONE);
                        toolbar_more_layout.setVisibility(View.GONE);
                        toolbar_places_layout.setVisibility(View.VISIBLE);

                        break;


                    case 3:

                        fm.beginTransaction().hide(active).show(fragment4).commit();
                        active = fragment4;


                        toolbar_home_layout.setVisibility(View.GONE);
                        toolbar_risks_layout.setVisibility(View.GONE);
                        toolbar_trips_layout.setVisibility(View.GONE);
                        toolbar_more_layout.setVisibility(View.VISIBLE);
                        toolbar_places_layout.setVisibility(View.GONE);


                        break;

                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

            }
        });



    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {


    }

    @Override
    protected void setViewListeners() {

    }

    @Override
    protected void init() {

        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
    }


    @Override
    protected boolean isValidData() {
        return false;
    }







    @Override
    public void onBackPressed()  {


        if (active != fragment1)
        {
            fm.beginTransaction().hide(active).show(fragment1).commit();
            active = fragment1;
            spaceNavigationView.changeCurrentItem(0);

            toolbar_home_layout.setVisibility(View.VISIBLE);
            toolbar_risks_layout.setVisibility(View.GONE);
            toolbar_trips_layout.setVisibility(View.GONE);
            toolbar_more_layout.setVisibility(View.GONE);

        }
        else
        {
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


}
