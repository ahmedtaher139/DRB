package wakeb.tech.drb.ui.home;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import butterknife.BindView;
import retrofit2.Retrofit;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Home.HomeFragment;
import wakeb.tech.drb.Home.MoreFragment;
import wakeb.tech.drb.Home.SuggestsPlaces;
import wakeb.tech.drb.Home.TripsFragment;
import wakeb.tech.drb.Profile.FollowersList;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.DefaultExceptionHandler;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;
import wakeb.tech.drb.ui.addNewSpot.AddNewSpot;

public class HomeActivity extends BaseActivity {

    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;
    boolean doubleBackToExitPressedOnce = false;

    @BindView(R.id.spaceNavigationView)
    SpaceNavigationView spaceNavigationView;


    final Fragment fragment1 = new TripsFragment();
    final Fragment fragment2 = new HomeFragment();
    final Fragment addNewSpotFragment = new AddNewSpot();
    final Fragment fragment4 = new MoreFragment();
    final Fragment fragment5 = new SuggestsPlaces();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    int colorGray, colorColored;

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
        setContentView(R.layout.activity_home);
        init();

        colorGray = Color.parseColor("#858585");
        colorColored = Color.parseColor("#008577");

        fm.beginTransaction().add(R.id.Container, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.Container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.Container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.Container, fragment1, "1").commit();


        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);

        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.timeline), R.drawable.ic_timeline));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.world), R.drawable.ic_world));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.places), R.drawable.ic_events));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.more), R.drawable.ic_more));
        //spaceNavigationView.showIconOnly();
        spaceNavigationView.setCentreButtonSelectable(true);


        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {


                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, 0, 0)
                        .replace(R.id.Container_new, addNewSpotFragment, AddNewSpot.TAG).commit();


            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {


                switch (itemIndex) {
                    case 0:

                        if (addNewSpotFragment != null) {
                            getSupportFragmentManager()
                                    .beginTransaction().remove(addNewSpotFragment).commit();
                        }

                        fm.beginTransaction().hide(active).show(fragment1).commit();
                        active = fragment1;

                        getSupportActionBar().setTitle(R.string.timeline);
                        break;


                    case 1:

                        if (addNewSpotFragment != null) {
                            getSupportFragmentManager()
                                    .beginTransaction().remove(addNewSpotFragment).commit();
                        }

                        fm.beginTransaction().hide(active).show(fragment2).commit();
                        active = fragment2;

                        getSupportActionBar().setTitle(R.string.map);
                        break;


                    case 2:

                        if (addNewSpotFragment != null) {
                            getSupportFragmentManager()
                                    .beginTransaction().remove(addNewSpotFragment).commit();
                        }

                        getSupportActionBar().setTitle(R.string.events);
                        fm.beginTransaction().hide(active).show(fragment5).commit();
                        active = fragment5;


                        break;


                    case 3:

                        if (addNewSpotFragment != null) {
                            getSupportFragmentManager()
                                    .beginTransaction().remove(addNewSpotFragment).commit();
                        }

                        getSupportActionBar().setTitle(R.string.more);
                        fm.beginTransaction().hide(active).show(fragment4).commit();
                        active = fragment4;


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
    protected void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.timeline);

        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
    }





    @Override
    public void onBackPressed() {


        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStack();
        } else {
            AddNewSpot test = (AddNewSpot) getSupportFragmentManager().findFragmentByTag(AddNewSpot.TAG);
            if (test != null && test.isVisible()) {
                getSupportFragmentManager()
                        .beginTransaction().remove(addNewSpotFragment).commit();

                close_add_fragment();

            } else if (active != fragment1) {
                fm.beginTransaction().hide(active).show(fragment1).commit();
                active = fragment1;
                spaceNavigationView.changeCurrentItem(0);

            } else {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:

                Intent intent = new Intent(this, FollowersList.class);
                intent.putExtra("FLAG", "NOTIFICATIONS");
                intent.putExtra("ItemID", dataManager.getID());
                startActivity(intent);
                break;

            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                {
                    getSupportFragmentManager().popBackStack();
                } else {
                    AddNewSpot test = (AddNewSpot) getSupportFragmentManager().findFragmentByTag(AddNewSpot.TAG);
                    if (test != null && test.isVisible()) {
                        getSupportFragmentManager()
                                .beginTransaction().remove(addNewSpotFragment).commit();

                        close_add_fragment();

                    } else if (active != fragment1) {
                        fm.beginTransaction().hide(active).show(fragment1).commit();
                        active = fragment1;
                        spaceNavigationView.changeCurrentItem(0);

                    } else {
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
                break;
        }

        return true;
    }


    void close_add_fragment() {
        if (addNewSpotFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction().remove(addNewSpotFragment).commit();

        }

        if (active == fragment1) {
            getSupportActionBar().setTitle(R.string.timeline);
            spaceNavigationView.changeCurrentItem(0);
        } else if (active == fragment2) {
            getSupportActionBar().setTitle(R.string.map);
            spaceNavigationView.changeCurrentItem(1);

        } else if (active == fragment4) {
            getSupportActionBar().setTitle(R.string.events);
            spaceNavigationView.changeCurrentItem(3);

        } else if (active == fragment5) {
            getSupportActionBar().setTitle(R.string.more);
            spaceNavigationView.changeCurrentItem(2);

        }
    }

}
