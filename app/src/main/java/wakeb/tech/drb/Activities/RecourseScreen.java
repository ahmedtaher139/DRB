package wakeb.tech.drb.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import wakeb.tech.drb.Adapters.ResourcesAdapter;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Home.Fragments.ResourcesList;
import wakeb.tech.drb.Models.PostedTrip;
import wakeb.tech.drb.Models.Resource;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.Uitils.DefaultExceptionHandler;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

import static io.fabric.sdk.android.Fabric.TAG;

public class RecourseScreen extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    @BindView(R.id.tv_startTrip)
    TextView tv_startTrip;

    Route route;


    @BindView(R.id.tv_startTrip_date)
    TextView tv_startTrip_date;

    @BindView(R.id.tv_endTrip)
    TextView tv_endTrip;

    private MenuItem list;
    private MenuItem map;

    @OnClick(R.id.trip_details_directions)
    void trip_details_directions() {
        CommonUtilities.get_maps_directions(this, String.valueOf(StartLat), String.valueOf(StartLng), String.valueOf(EndLat), String.valueOf(EndLng));
    }

    double StartLat, StartLng, EndLat, EndLng;

    @BindView(R.id.tv_endTrip_date)
    TextView tv_endTrip_date;

    @BindView(R.id.card_view)
    CardView card_view;

    @BindView(R.id.view_recourse_image)
    CircleImageView view_recourse_image;

    @BindView(R.id.view_recourse_name)
    TextView view_recourse_name;

    @BindView(R.id.view_recourse_date)
    TextView view_recourse_date;

    @BindView(R.id.tv_startTrip_bottom)
    TextView tv_startTrip_bottom;

    @BindView(R.id.tv_endTrip_bottom)
    TextView tv_endTrip_bottom;

    @BindView(R.id.view_recourse_recycler)
    RecyclerView view_recourse_recycler;


    @BindView(R.id.Container)
    RelativeLayout Container;

    @BindView(R.id.trip_details_duration)
    TextView trip_details_duration;

    @OnClick(R.id.back_button)
    void back_button() {
        finish();
    }

    @BindView(R.id.trip_details_distance)
    TextView trip_details_distance;

    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    ArrayList<Resource> resources;

    @BindView(R.id.details_container)
    RelativeLayout details_container;

    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;
    private MapView mapView;
    private GoogleMap mMap;
    PolylineOptions polylineOptions;
    Bundle mapViewBundle;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    String Publishing_id, Trip_id;
    Marker start_marker, end_marker;
    Polyline polyline;
    List<Polyline> polylines;

    PostedTrip postedTrip;

    private BottomSheetBehavior mBottomSheetBehavior;
    View bottomSheet;

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
        setContentView(R.layout.activity_recourse_screen);
        ButterKnife.bind(this);

        Publishing_id = getIntent().getStringExtra("Publishing_id");
        Trip_id = getIntent().getStringExtra("Trip_id");


        bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        card_view.setLayoutParams(new CoordinatorLayout.LayoutParams(width, height + 40));


        if (CommonUtilities.hasNavBar(getResources())) {
            details_container.setLayoutParams(new FrameLayout.LayoutParams(width, height - 40));


        } else {
            details_container.setLayoutParams(new FrameLayout.LayoutParams(width, height - 80));


        }
        init();
        mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        polylines = new ArrayList<>();


    }


    void get_data(String publishing_id) {

        CommonUtilities.showStaticDialog(this, "recourse");

        Map<String, String> parms = new HashMap<>();
        parms.put("publishing_id", publishing_id);
        parms.put("user_id", dataManager.getID());
        myAPI.get_publishing(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {


                            StartLat = apiResponse.getData().getPostedTrip().getTrip().getStartLat();
                            StartLng = apiResponse.getData().getPostedTrip().getTrip().getStartLng();
                            EndLat = apiResponse.getData().getPostedTrip().getTrip().getEndLat();
                            EndLng = apiResponse.getData().getPostedTrip().getTrip().getEndLng();


                            long diff = apiResponse.getData().getPostedTrip().getTrip().getEndedAt() - apiResponse.getData().getPostedTrip().getTrip().getCreatedAt();
                            long diffMinutes = diff / (60 * 1000) % 60;
                            trip_details_duration.setText(String.valueOf(diffMinutes) + " " + getString(R.string.minte));

                            tv_startTrip.setText(apiResponse.getData().getPostedTrip().getTrip().getStartAddress());
                            tv_startTrip_bottom.setText(apiResponse.getData().getPostedTrip().getTrip().getStartAddress());
                            tv_endTrip.setText(apiResponse.getData().getPostedTrip().getTrip().getEndAddress());
                            tv_endTrip_bottom.setText(apiResponse.getData().getPostedTrip().getTrip().getEndAddress());


                            view_recourse_name.setText(apiResponse.getData().getPostedTrip().getTrip().getPublisher().getDisplayName());

                            Glide.with(getApplicationContext())
                                    .load(apiResponse.getData().getPostedTrip().getTrip().getPublisher().getImage())
                                    .apply(new RequestOptions()
                                            .placeholder(view_recourse_image.getDrawable())
                                    )
                                    .into(view_recourse_image);

                            view_recourse_date.setText(DateUtils.getRelativeTimeSpanString(apiResponse.getData().getPostedTrip().getCreatedAt(),
                                    System.currentTimeMillis(),
                                    DateUtils.SECOND_IN_MILLIS).toString());

                            tv_startTrip_date.setText(DateUtils.getRelativeTimeSpanString(apiResponse.getData().getPostedTrip().getTrip().getCreatedAt(),
                                    System.currentTimeMillis(),
                                    DateUtils.SECOND_IN_MILLIS).toString());
                            tv_endTrip_date.setText(DateUtils.getRelativeTimeSpanString(apiResponse.getData().getPostedTrip().getTrip().getEndedAt(),
                                    System.currentTimeMillis(),
                                    DateUtils.SECOND_IN_MILLIS).toString());


                            start_marker = mMap.addMarker(new MarkerOptions().position(new LatLng(StartLat, StartLng)).icon(resizeMarkerIcon(R.drawable.start_marker)));
                            end_marker = mMap.addMarker(new MarkerOptions().position(new LatLng(EndLat, EndLng)).icon(resizeMarkerIcon(R.drawable.end_marker)));


                            GoogleDirection.withServerKey("AIzaSyBtCSwehQfoAdYemxDzJrKInCYafq83e6g")
                                    .from(new LatLng(StartLat, StartLng))
                                    .to(new LatLng(EndLat, EndLng))
                                    .transportMode(TransportMode.DRIVING)
                                    .execute(new DirectionCallback() {
                                        @SuppressLint("SetTextI18n")
                                        @Override
                                        public void onDirectionSuccess(Direction direction, String rawBody) {

                                            if (polyline != null) {
                                                polyline.remove();
                                            }
                                            String status = direction.getStatus();
                                            if (status.equals(RequestResult.OK)) {

                                                route = direction.getRouteList().get(0);
                                                Leg leg = route.getLegList().get(0);

                                                polylineOptions = DirectionConverter.createPolyline(RecourseScreen.this,
                                                        leg.getDirectionPoint(), 3, getResources().getColor(R.color.dark_mid_color));
                                                polyline = mMap.addPolyline(polylineOptions);
                                                Info distanceInfo = leg.getDistance();

                                                DecimalFormat numberFormat = new DecimalFormat("#.0");
                                                trip_details_distance.setText(numberFormat.format(Double.parseDouble(distanceInfo.getValue()) / 1000) + getString(R.string.k_m));

                                                setCameraWithCoordinationBounds(route);

                                                //------------------------------------------------------------------\\

                                            } else {
                                                Toast.makeText(RecourseScreen.this, "No routes exist", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onDirectionFailure(Throwable t) {
                                            // Do something here
                                        }
                                    });


                        } else {

                            Toast.makeText(RecourseScreen.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();


                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RecourseScreen.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Log.i("RETROFIT_ERROR", e.getMessage());

                        CommonUtilities.hideDialog();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtilities.hideDialog();

                    }
                });
    }




    protected void init() {

        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
        resources = new ArrayList<>();
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setPadding(20, 250, 20, 450);

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            RecourseScreen.this, R.raw.mapstyle));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        if (Publishing_id != null) {
            get_data(Publishing_id);
        }

        if (Trip_id != null) {
            get_resource(Trip_id);

        }


    }


    @Override
    public void onStart() {
        super.onStart();

        mapView.invalidate();
        mapView.onStart();

        // Branch init
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
                            postedTrip = gson.fromJson(property1, PostedTrip.class);
                            if (postedTrip != null) {

                                get_data(String.valueOf(postedTrip.getId()));

                                get_resource(String.valueOf(postedTrip.getTrip().getId()));


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

        mapView.onStop();
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

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        if (route != null) {
            setCameraWithCoordinationBounds(route);
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    BitmapDescriptor resizeMarkerIcon(int icon) {
        int height = 25;
        int width = 25;
        Bitmap b = BitmapFactory.decodeResource(getResources(), icon);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
        return smallMarkerIcon;
    }

    BitmapDescriptor resizeMarkerIcon2(int icon) {
        int height = 100;
        int width = 100;
        Bitmap b = BitmapFactory.decodeResource(getResources(), icon);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
        return smallMarkerIcon;
    }

    void get_resource(String trip_id) {

        Map<String, String> parms = new HashMap<>();
        parms.put("trip_id", trip_id);
        parms.put("user_id", dataManager.getID());
        myAPI.get_resource(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {

                            resources.addAll(apiResponse.getData().getResources());
                            for (int i = 0; i < apiResponse.getData().getResources().size(); i++) {
                                if (apiResponse.getData().getResources().get(i).getType().equals("vedio")) {

                                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(apiResponse.getData().getResources().get(i).getLat(), apiResponse.getData().getResources().get(i).getLng())).icon(resizeMarkerIcon2(R.drawable.recourse_video)).title(apiResponse.getData().getResources().get(i).getAddress()));
                                    mHashMap.put(marker, i);

                                } else if (apiResponse.getData().getResources().get(i).getType().equals("image")) {

                                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(apiResponse.getData().getResources().get(i).getLat(), apiResponse.getData().getResources().get(i).getLng())).icon(resizeMarkerIcon2(R.drawable.recourse_image)).title(apiResponse.getData().getResources().get(i).getAddress()));
                                    mHashMap.put(marker, i);

                                }

                            }


                            view_recourse_recycler.setLayoutManager(new LinearLayoutManager(RecourseScreen.this));
                            view_recourse_recycler.setAdapter(new ResourcesAdapter(RecourseScreen.this, apiResponse.getData().getResources()));

                        } else {
                            Toast.makeText(RecourseScreen.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RecourseScreen.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);

        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));

        } catch (Exception e) {

        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        try {

            int pos = mHashMap.get(marker);
            Intent intent = new Intent(RecourseScreen.this, ViewRecourse.class);
            intent.putExtra("TYPE", resources.get(pos).getType());
            intent.putExtra("ADDRESS", resources.get(pos).getAddress());
            intent.putExtra("URL", resources.get(pos).getResource());
            intent.putExtra("DESC", resources.get(pos).getDesc());
            startActivity(intent);

        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            getSupportFragmentManager().popBackStack();
            list.setVisible(true);
            map.setVisible(false);
            Container.setVisibility(View.GONE);
        } else {
            finish();
        }
    }


}
