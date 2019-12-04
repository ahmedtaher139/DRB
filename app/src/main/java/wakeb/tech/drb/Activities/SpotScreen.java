package wakeb.tech.drb.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Models.File;
import wakeb.tech.drb.Models.JourneySpots;
import wakeb.tech.drb.Models.SpotModel;
import wakeb.tech.drb.Profile.MyProfile;
import wakeb.tech.drb.Profile.UserProfile;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.Uitils.DefaultExceptionHandler;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;
import wakeb.tech.drb.databinding.ActivitySpotScreenBinding;
import wakeb.tech.drb.imagesviewer.StoriesProgressView;
import wakeb.tech.drb.ui.spots.JourneySpotsAdapter;

import static io.fabric.sdk.android.Fabric.TAG;


public class SpotScreen extends BaseActivity implements OnMapReadyCallback, StoriesProgressView.StoriesListener {
    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;

    SpotModel model;

    ArrayList<LatLng> points;
    private int PROGRESS_COUNT;
    private int counter = 0;
    private ArrayList<String> resources;


    private GoogleMap mMap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private Bundle mapViewBundle;

    ActivitySpotScreenBinding binding;


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    String profile_ID, spotID;
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


        spotID = getIntent().getStringExtra("SPOT_ID");

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_spot_screen);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(getString(R.string.details));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_btn);


        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
        resources = new ArrayList<>();


        binding.mapView.onCreate(mapViewBundle);
        binding.mapView.getMapAsync(this);
        mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        binding.mapView.setClickable(false);
         binding.spotScreenSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSpot(spotID);
            }
        });


        binding.likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like_action();
            }
        });

        binding.faveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fave_action();
            }
        });



        binding.commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpotScreen.this, TripComments.class);
                intent.putExtra("ItemID", model.getId().toString());
              startActivity(intent);
            }
        });

        binding.profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(profile_ID))
                {
                    if ( profile_ID.equals(dataManager.getID())) {
                        Intent intent = new Intent(SpotScreen.this, MyProfile.class);
                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(SpotScreen.this, UserProfile.class);
                        intent.putExtra("ItemID",profile_ID);
                        startActivity(intent);
                    }
                }



            }
        });
    }

    @Override
    public void onNext() {

        Glide.with(SpotScreen.this)
                .load(resources.get(++counter))
                .apply(new RequestOptions()
                        .placeholder(binding.image.getDrawable())
                )
                .into(binding.image);
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;

        Glide.with(SpotScreen.this)
                .load(resources.get(--counter))
                .apply(new RequestOptions()
                        .placeholder(binding.image.getDrawable())
                )
                .into(binding.image);
    }


    @Override
    public void onComplete() {
        counter = 0;

        Glide.with(SpotScreen.this)
                .load(resources.get(counter))
                .apply(new RequestOptions()
                        .placeholder(binding.image.getDrawable())
                )
                .into(binding.image);
    }

    @Override
    protected void onDestroy() {
        // Very important !
        binding.stories.destroy();
        binding.mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(0, 100, 0, 20);
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }


        if (!TextUtils.isEmpty(spotID)) {
            getSpot(spotID);
        }



    }


    @Override
    protected void init() {

    }

    @Override
    public void onStart() {
        super.onStart();
        binding.mapView.invalidate();
        binding.mapView.onStart();


        Branch.getInstance().initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    Log.i("BRANCH SDK", referringParams.toString());
                    // Retrieve deeplink keys from 'referringParams' and evaluate the values to determine where to route the user
                    // Check '+clicked_branch_link' before deciding whether to use your Branch routing logic

                    Log.e("BRANCH SDK", referringParams.toString());

                    if (referringParams.has("property1")) {
                        String property1 = referringParams.optString("property1", "");
                        if (property1 != null) {
                            Gson gson = new Gson();
                            model = gson.fromJson(property1, SpotModel.class);
                            if (model != null) {
                                spotID = String.valueOf(model.getId());
                                getSpot(String.valueOf(model.getId()));


                            }
                        }
                    }


                } else {
                    Log.i("BRANCH SDK", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);



    }

    @Override
    public void onStop() {

        binding.mapView.onStop();
        super.onStop();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        binding.mapView.onSaveInstanceState(mapViewBundle);
    }


    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();

    }

    public void onPause() {
        binding.mapView.onPause();
        super.onPause();

        try {
            CommonUtilities.hideDialog();
        } catch (Exception e) {
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }


    void getSpot(String spotID) {

        binding.spotScreenSwipe.setRefreshing(true);
        Map<String, String> parms = new HashMap<>();
        parms.put("spot_id", spotID);
        parms.put("publisher_id", dataManager.getID());
        myAPI.get_spot(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {

                        Log.w("printed gson => ", new GsonBuilder().setPrettyPrinting().create().toJson(apiResponse));

                        if (apiResponse.getStatus()) {

                            if (apiResponse.getData().getSpotModels().size() == 0) {

                            } else {

                                model = apiResponse.getData().getSpotModels().get(0);

                                profile_ID = String.valueOf(model.getPublisher().getId());
                                for (File file : model.getFiles()) {
                                    resources.add("http://3.17.76.229/" + file.getFile());
                                }

                                binding.showImages.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Fragment fragment = new ImagesSlider();

                                        Bundle args = new Bundle();
                                        args.putStringArrayList("IMAGES", resources);
                                        fragment.setArguments(args);


                                        getSupportFragmentManager()
                                                .beginTransaction()
                                                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, 0, 0)
                                                .replace(R.id.Container, fragment, ImagesSlider.TAG)
                                                .addToBackStack(null)
                                                .commit();


                                    }
                                });

                                PROGRESS_COUNT = model.getFiles().size();
                                counter = 0;
                                binding.stories.setStoriesCount(PROGRESS_COUNT);
                                binding.stories.setStoryDuration(3000L);
                                binding.stories.setStoriesListener(SpotScreen.this);

                                binding.stories.startStories(counter);


                                Glide.with(SpotScreen.this)
                                        .load("http://3.17.76.229/" + model.getFiles().get(counter).getFile())
                                        .apply(new RequestOptions()
                                                .placeholder(binding.image.getDrawable())
                                        )
                                        .into(binding.image);

                                Glide.with(SpotScreen.this)
                                        .load("http://3.17.76.229/uploads/publishers/" + model.getPublisher().getImage())
                                        .apply(new RequestOptions()
                                                .placeholder(binding.spotAdminImage.getDrawable())
                                        )
                                        .into(binding.spotAdminImage);

                                binding.spotAdminName.setText(model.getPublisher().getDisplayName());

                                binding.spotDesc.setText(model.getDescription());

                                binding.spotDate.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(model.getCreatedAt())));

                                binding.spotLikeCount.setText(String.valueOf(model.getLikesCount()));
                                binding.spotCommentCount.setText(String.valueOf(model.getCommCount()));
                                binding.spotFavesCount.setText(String.valueOf(model.getFavouritesCount()));

                                if (model.getIsFavorite()) {
                                    binding.spotFavesBtn.setColorFilter(ContextCompat.getColor(SpotScreen.this, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);

                                } else {
                                    binding.spotFavesBtn.setColorFilter(ContextCompat.getColor(SpotScreen.this, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);

                                }

                                if (model.getIsLiked()) {
                                    binding.spotLikeBtn.setColorFilter(ContextCompat.getColor(SpotScreen.this, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);

                                } else {
                                    binding.spotLikeBtn.setColorFilter(ContextCompat.getColor(SpotScreen.this, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);

                                }

                                if (model.getJournalId() == null) {


                                    if (!TextUtils.isEmpty(model.getPlaceName()))
                                    {
                                        binding.spotTitle.setText("#" + model.getPlaceName());
                                    }


                                } else {
                                    binding.spotTitle.setText("#" + model.getSpotJourney().getName());
                                    binding.spotOtherSpotsTitle.setVisibility(View.VISIBLE);

                                }


                                Log.i("LINK", "http://drbtravel.com/" + model.getFiles().get(counter).getFile());


                                points = new ArrayList<LatLng>();

                                if (model.getJournalId() != null) {

                                    if (model.getSpotJourney().getJourneySpots().size() > 1) {
                                        for (JourneySpots journeySpots : model.getSpotJourney().getJourneySpots()) {


                                            LatLng point = new LatLng(journeySpots.getLat(), journeySpots.getLng());

                                            MarkerOptions markerOptions = new MarkerOptions();
                                            if (model.getLat() == point.latitude) {
                                                markerOptions.icon(bitmapDescriptorFromVector(SpotScreen.this, R.drawable.ic_current_marker));
                                            } else {
                                                markerOptions.icon(bitmapDescriptorFromVector(SpotScreen.this, R.drawable.ic_marker));

                                            }
                                            // Setting latitude and longitude of the marker position
                                            markerOptions.position(point);

                                            // Instantiating the class PolylineOptions to plot polyline in the map
                                            PolylineOptions polylineOptions = new PolylineOptions();

                                            // Setting the color of the polyline
                                            polylineOptions.color(R.color.dark_mid_color);

                                            // Setting the width of the polyline
                                            polylineOptions.width(3);

                                            // Adding the taped point to the ArrayList
                                            points.add(point);

                                            // Setting points of polyline
                                            polylineOptions.addAll(points);

                                            // Adding the polyline to the map
                                            mMap.addPolyline(polylineOptions);

                                            // Adding the marker to the map
                                            mMap.addMarker(markerOptions);
                                        }


                                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                        for (LatLng point : points) {
                                            builder.include(point);
                                        }
                                        LatLngBounds bounds = builder.build();

                                        int padding = 0; // offset from edges of the map in pixels
                                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                                        mMap.moveCamera(cu);

                                        GridLayoutManager layoutManager = new GridLayoutManager(SpotScreen.this, 2);

                                        // Create a custom SpanSizeLookup where the first item spans both columns
                                        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                            @Override
                                            public int getSpanSize(int position) {

                                                if (position % 5 == 0) {
                                                    return 2;
                                                } else {
                                                    return 1;
                                                }

                                            }
                                        });

                                        binding.spotOtherSpotsRecycler.setLayoutManager(layoutManager);
                                        binding.spotOtherSpotsRecycler.setNestedScrollingEnabled(false);
                                        binding.spotOtherSpotsRecycler.setAdapter(new JourneySpotsAdapter(SpotScreen.this, model.getSpotJourney().getJourneySpots()));

                                    } else {

                                        LatLng point = new LatLng(model.getLat(), model.getLng());
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.icon(bitmapDescriptorFromVector(SpotScreen.this, R.drawable.ic_current_marker));
                                        markerOptions.position(point);
                                        mMap.addMarker(markerOptions);

                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 10));


                                    }


                                } else {

                                    LatLng point = new LatLng(model.getLat(), model.getLng());
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.icon(bitmapDescriptorFromVector(SpotScreen.this, R.drawable.ic_current_marker));
                                    markerOptions.position(point);
                                    mMap.addMarker(markerOptions);

                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 10));


                                }


                                binding.shareLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        createDeepLink(model);
                                    }
                                });


                            }





                        } else {
                            Toast.makeText(SpotScreen.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SpotScreen.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();

                        Log.i("ERROR_RETROFIT", e.getMessage());
                        binding.spotScreenSwipe.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        binding.spotScreenSwipe.setRefreshing(false);
                    }
                });
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    void fave_action() {


        Map<String, String> parms = new HashMap<>();
        parms.put("publisher_id", dataManager.getID());
        parms.put("spot_id", getIntent().getStringExtra("SPOT_ID"));
        myAPI.fav_action(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {


                        } else {

                            Toast.makeText(SpotScreen.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SpotScreen.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Log.i("ERROR_RETROFIT", e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                        getSpot(spotID);
                    }
                });
    }


    void like_action() {


        Map<String, String> parms = new HashMap<>();
        parms.put("publisher_id", dataManager.getID());
        parms.put("spot_id", getIntent().getStringExtra("SPOT_ID"));
        myAPI.like_action(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {


                        } else {

                            Toast.makeText(SpotScreen.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SpotScreen.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Log.i("ERROR_RETROFIT", e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                        getSpot(spotID);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void createDeepLink(final SpotModel product) {
        Gson gson = new Gson();

        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier("item/" + product.getId())
                .setTitle("DRB")
                .setContentDescription(product.getPlaceName())
                .setContentImageUrl("http://3.17.76.229/" + product.getFiles().get(0).getFile())
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata("property1", gson.toJson(product));


        LinkProperties linkProperties = new LinkProperties()
                .setChannel("facebook")
                .setFeature("sharing");

        ShareSheetStyle shareSheetStyle = new ShareSheetStyle(this, "Check this out!", "")
                .setCopyUrlStyle(getResources().getDrawable(android.R.drawable.ic_menu_send), "Copy", "Added to clipboard")
                .setMoreOptionStyle(getResources().getDrawable(android.R.drawable.ic_menu_search), "Show more")
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.TWITTER)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.WHATS_APP)
                .setAsFullWidthStyle(true)
                .setSharingTitle("Share");

        branchUniversalObject.showShareSheet(this,
                linkProperties,
                shareSheetStyle,
                new Branch.BranchLinkShareListener() {
                    @Override
                    public void onShareLinkDialogLaunched() {
                    }

                    @Override
                    public void onShareLinkDialogDismissed() {
                    }

                    @Override
                    public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {

                    }

                    @Override
                    public void onChannelSelected(String channelName) {
                    }
                });

        branchUniversalObject.generateShortUrl(this, linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {

                }
                Toast.makeText(SpotScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("ERROORR", error.getMessage());

            }
        });

    }


}
