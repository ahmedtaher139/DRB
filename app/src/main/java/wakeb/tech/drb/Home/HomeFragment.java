package wakeb.tech.drb.Home;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;

import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import mumayank.com.airlocationlibrary.AirLocation;
import retrofit2.Retrofit;
import wakeb.tech.drb.Activities.NewResource;
import wakeb.tech.drb.Activities.ViewPlaces;
import wakeb.tech.drb.Activities.ViewRecourse;
import wakeb.tech.drb.Base.BaseFragment;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Dialogs.EndTripDialog;
import wakeb.tech.drb.Models.InfoWindowData;
import wakeb.tech.drb.Models.NearPlaces;
import wakeb.tech.drb.Models.Risk;
import wakeb.tech.drb.Models.Store;
import wakeb.tech.drb.Models.Suggest;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Registration.LoginScreen;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.Uitils.GetBitmapTask;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

import static io.fabric.sdk.android.Fabric.TAG;
import static wakeb.tech.drb.Uitils.CommonUtilities.encodeImage;
import static wakeb.tech.drb.Uitils.CommonUtilities.scaleDown;


public class HomeFragment extends BaseFragment implements OnMapReadyCallback {




    @BindView(R.id.startTrip)
    Button startTrip;

    @BindView(R.id.tv_startTrip)
    TextView tv_startTrip;

    @BindView(R.id.tv_endTrip)
    TextView tv_endTrip;

    @BindView(R.id.tv_startTrip_preview)
    TextView tv_startTrip_preview;

    @BindView(R.id.tv_endTrip_preview)
    TextView tv_endTrip_preview;

    @BindView(R.id.duration)
    TextView duration;

    @BindView(R.id.distance)
    TextView distance;

    @BindView(R.id.shopping_medicines)
    CardView shopping_medicines;

    @BindView(R.id.duration2)
    TextView duration2;

    @BindView(R.id.distance2)
    TextView distance2;

    @BindView(R.id.getMyLocation)
    ImageButton getMyLocation;


    @BindView(R.id.bottom_sheet)
    CardView bottom_sheet;


    Route route;

    @BindView(R.id.start_location)
    LinearLayout start_location;

    @BindView(R.id.end_location)
    LinearLayout end_location;


    @OnClick(R.id.cancelTrip)
    void cancelTrip() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getString(R.string.cancel_triip));
        alertDialog.setMessage(getString(R.string.cancel_confirm));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        end_trip("private", "2", "");
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


    @OnClick(R.id.start_location)
    void start_location() {
        if (!Started || TextUtils.isEmpty(tv_startTrip.getText().toString())) {
            getCurrentLocation();
        }
    }

   /* @OnClick(R.id.show_places)
    void show_places() {
        if (risk_visible) {
            for (Marker marker : risk_markers) {
                marker.setVisible(false);
                risk_visible = false;
            }
        } else {
            for (Marker marker : risk_markers) {
                marker.setVisible(true);
                risk_visible = true;
            }
        }
    }*/


    @OnClick(R.id.end_location)
    void end_location() {
        if (!Started) {
            startActivityForResult(new Intent(getActivity(), SelectLocation.class), 1002);
        }
    }

    @OnClick(R.id.get_maps_Direction)
    void get_maps_Direction() {
        CommonUtilities.get_maps_directions(getActivity(), String.valueOf(start_latlng.latitude), String.valueOf(start_latlng.longitude), String.valueOf(end_latlang.latitude), String.valueOf(end_latlang.longitude));

    }

    @OnClick(R.id.add_new_resource)
    void add_new_resource() {
        Intent intent = new Intent(getActivity(), NewResource.class);
        intent.putExtra("TRIP_ID", dataManager.getTripID());
        startActivity(intent);

    }

    @OnClick(R.id.getMyLocation)
    void getMyLocation() {
        if (myLocation != null) {
            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        } else {
            getCurrentLocation();
        }
    }

    @OnClick(R.id.resetAll)
    void resetAll() {


        if (!Started) {
            reset();
        }
    }


    @OnClick(R.id.startTrip)
    void startTrip() {
        start_trip();
    }

    @OnClick(R.id.endTrip)
    void endTrip() {
        if (Started) {

            EndTripDialog cdd = new EndTripDialog(getActivity());
            cdd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            Window window = cdd.getWindow();
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = 0.7f;
            lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            cdd.getWindow().setAttributes(lp);

            cdd.show();


        }
    }


    private MapView mapView;
    private MapView snapMapView;
    private GoogleMap mMap;
    private GoogleMap mMap2;


    private int mMapWidth = 1200;
    private int mMapHeight = 600;
    PolylineOptions polylineOptions;

    private ArrayList<Marker> risk_markers = new ArrayList<>();
    private ArrayList<Marker> suggest_markers = new ArrayList<>();
    private ArrayList<Marker> stores_markers = new ArrayList<>();

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private BottomSheetBehavior mBottomSheetBehavior, mBottomSheetBehavior_view;
    View bottomSheet_view, bottomSheet;
    private AirLocation airLocation;
    private Marker start_marker, end_marker;

    private String trip_address;
    private LatLng start_latlng, end_latlang;
    private Polyline polyline;
    private Location myLocation;
    private Boolean ready = false;
    private Boolean routed = false;
    private double distance_text;
    private boolean Started = false;
    private Bitmap map_snapshot;
    private String base64;
    private ApiServices myAPI;
    private Retrofit retrofit;
    private DataManager dataManager;
    private Bundle mapViewBundle;

    private boolean get_opened_trip = false;

    boolean risk_visible = true;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getActivity().getApplicationContext());
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        dataManager = ((MainApplication) getActivity().getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);


        GoogleMapOptions options = new GoogleMapOptions()
                .compassEnabled(false)
                .mapToolbarEnabled(false)
                .liteMode(true);

        snapMapView = new MapView(getActivity(), options);
        snapMapView.onCreate(mapViewBundle);
        snapMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap2 = googleMap;
                mMap2.setPadding(20, 20, 20, 20);

                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    getActivity(), R.raw.mapstyle));

                    if (!success) {
                        Log.e(TAG, "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }
                snapMapView.measure(View.MeasureSpec.makeMeasureSpec(mMapWidth, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(mMapHeight, View.MeasureSpec.EXACTLY));
                snapMapView.layout(0, 0, mMapWidth, mMapHeight);

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        bottomSheet = view.findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(false);

        bottomSheet_view = view.findViewById(R.id.bottom_sheet_started);
        mBottomSheetBehavior_view = BottomSheetBehavior.from(bottomSheet_view);
        mBottomSheetBehavior_view.setHideable(false);
        mBottomSheetBehavior_view.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mMap.setPadding(0, 170, 0, mBottomSheetBehavior_view.getPeekHeight());

                        setCameraWithCoordinationBounds(route);
                        shopping_medicines.setVisibility(View.VISIBLE);

                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:

                        mMap.setPadding(0, 0, 0, bottomSheet_view.getHeight());
                        setCameraWithCoordinationBounds(route);
                        shopping_medicines.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        get_location();
        get_currentTrip();

        myAPI.get_all()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {

                        if (apiResponse.getStatus()) {


                            for (Risk risk : apiResponse.getData().getAllPlaces().getRisks()) {
                                Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(risk.getLat(), risk.getLng())).icon(resizeMarkerIcon3(R.drawable.risk_marker)).title(risk.getDesc()));

                                InfoWindowData infoWindowData = new InfoWindowData();
                                infoWindowData.setImage_url(risk.getImage());
                                infoWindowData.setDesc(risk.getDesc());
                                infoWindowData.setDate(DateUtils.getRelativeTimeSpanString(risk.getCreatedAt(),
                                        System.currentTimeMillis(),
                                        DateUtils.SECOND_IN_MILLIS).toString());
                                infoWindowData.setType(getString(R.string.risk));
                                infoWindowData.setId(String.valueOf(risk.getId()));
                                marker.setTag(infoWindowData);
                                risk_markers.add(marker);
                            }


                            for (Suggest suggest : apiResponse.getData().getAllPlaces().getSuggests()) {
                                Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(suggest.getLat(), suggest.getLng())).icon(resizeMarkerIcon3(R.drawable.suggest_mark)).title(suggest.getDesc()));
                                InfoWindowData infoWindowData = new InfoWindowData();
                                infoWindowData.setImage_url(suggest.getImage());
                                infoWindowData.setDesc(suggest.getDesc());
                                infoWindowData.setDate(String.valueOf(suggest.getCreatedAt()));
                                infoWindowData.setType(getString(R.string.suggest));
                                infoWindowData.setId(String.valueOf(suggest.getId()));
                                marker.setTag(infoWindowData);
                                suggest_markers.add(marker);


                            }


                            for (NearPlaces nearPlaces : apiResponse.getData().getAllPlaces().getStores()) {

                                Glide.with(getActivity())
                                        .asBitmap()
                                        .load(nearPlaces.getStore().getStoreType().getIcon())
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                                Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(nearPlaces.getPlaces().getLat(), nearPlaces.getPlaces().getLng())).icon(resizeMarkerIcon2(resource)).title(nearPlaces.getPlaces().getDesc()));
                                                InfoWindowData infoWindowData = new InfoWindowData();
                                                infoWindowData.setImage_url(nearPlaces.getPlaces().getImage());
                                                infoWindowData.setDesc(nearPlaces.getPlaces().getDesc());
                                                infoWindowData.setDate(DateUtils.getRelativeTimeSpanString(nearPlaces.getPlaces().getCreatedAt(),
                                                        System.currentTimeMillis(),
                                                        DateUtils.SECOND_IN_MILLIS).toString());
                                                infoWindowData.setType(getString(R.string.places));
                                                infoWindowData.setId("1");
                                                marker.setTag(infoWindowData);
                                                stores_markers.add(marker);

                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                            }
                                        });


                            }


                        } else {

                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {


                    }
                });
        return view;

    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.invalidate();
        mapView.onStart();


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.mapstyle));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setPadding(0, 170, 0, 75);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

            }
        });
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
                String s = null;
                try {
                    s = infoWindowData.getType();
                } catch (Exception e) {

                }

                if (s == null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    return true;

                } else {
                    return false;
                }


            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
                String s = null;
                try {
                    s = infoWindowData.getType();
                } catch (Exception e) {

                }

                if (s == null) {


                } else {

                    if (infoWindowData.getType().equals(getString(R.string.risk))) {


                        Intent intent = new Intent(getActivity(), ViewPlaces.class);
                        intent.putExtra("TYPE", infoWindowData.getType());
                        intent.putExtra("ADDRESS", new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
                        intent.putExtra("URL", infoWindowData.getImage_url());
                        intent.putExtra("DESC", infoWindowData.getDesc());
                        startActivity(intent);


                    } else if (infoWindowData.getType().equals(getString(R.string.suggest))) {

                        Intent intent = new Intent(getActivity(), ViewPlaces.class);
                        intent.putExtra("TYPE", infoWindowData.getType());
                        intent.putExtra("ADDRESS", new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
                        intent.putExtra("URL", infoWindowData.getImage_url());
                        intent.putExtra("DESC", infoWindowData.getDesc());
                        startActivity(intent);


                    } else {
                    }

                }


            }
        });


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


        if (dataManager.getTurnOnRisksMarkers()) {
            for (Marker marker : risk_markers) {
                marker.setVisible(true);
            }
        } else {
            for (Marker marker : risk_markers) {
                marker.setVisible(false);
            }
        }

        if (dataManager.getTurnOnPlacesMarkers()) {
            for (Marker marker : suggest_markers) {
                marker.setVisible(true);
            }
        } else {
            for (Marker marker : suggest_markers) {
                marker.setVisible(false);
            }
        }

        if (dataManager.getTurnOnStoresMarkers()) {
            for (Marker marker : stores_markers) {
                marker.setVisible(true);
            }
        } else {
            for (Marker marker : stores_markers) {
                marker.setVisible(true);
            }
        }


    }

    public void onPause() {
        mapView.onPause();
        super.onPause();

        try {
            CommonUtilities.hideDialog();
        } catch (Exception e) {
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case 1003:
                try {
                    trip_address = data.getStringExtra("address");
                    start_latlng = new LatLng(Double.parseDouble(data.getStringExtra("latitude")), Double.parseDouble(data.getStringExtra("longitude")));
                    tv_startTrip.setText(trip_address);
                    tv_startTrip_preview.setText(trip_address);


                    if (start_marker == null) {

                        start_marker = mMap.addMarker(new MarkerOptions().position(start_latlng).icon(resizeMarkerIcon(R.drawable.start_marker)));


                    } else {
                        start_marker.setPosition(start_latlng);
                    }


                    if (end_marker == null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start_latlng, 15));

                    } else {
                        requestDirection();
                        ready = true;
                    }


                } catch (Exception e) {

                }

                break;

            case 1002:

                try {
                    trip_address = data.getStringExtra("address");
                    end_latlang = new LatLng(Double.parseDouble(data.getStringExtra("latitude")), Double.parseDouble(data.getStringExtra("longitude")));
                    tv_endTrip.setText(trip_address);
                    tv_endTrip_preview.setText(trip_address);


                    if (end_marker == null) {
                        end_marker = mMap.addMarker(new MarkerOptions().position(end_latlang).icon(resizeMarkerIcon(R.drawable.end_marker)));
                    } else {
                        end_marker.setPosition(end_latlang);
                    }

                    if (start_marker == null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(end_latlang, 15));

                    } else {
                        requestDirection();
                        ready = true;

                    }
                } catch (Exception e) {

                }


                break;
        }
        airLocation.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    public void requestDirection() {

        if (start_latlng == null) {
            Toast.makeText(getActivity(), "please choose start location first", Toast.LENGTH_SHORT).show();
        } else if (end_latlang == null) {
            Toast.makeText(getActivity(), "please choose end location first", Toast.LENGTH_SHORT).show();

        } else {

            //-------------Using AK Exorcist Google Direction Library---------------\\
            GoogleDirection.withServerKey("AIzaSyBtCSwehQfoAdYemxDzJrKInCYafq83e6g")
                    .from(start_latlng)
                    .to(end_latlang)
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
                                Info distanceInfo = leg.getDistance();
                                Info durationInfo = leg.getDuration();
                                String duration_text = durationInfo.getText();
                                distance_text = Double.parseDouble(distanceInfo.getValue()) / 1000;
                                DecimalFormat numberFormat = new DecimalFormat("#.0");


                                distance.setText(numberFormat.format(distance_text) + getString(R.string.k_m));
                                duration.setText(duration_text);

                                distance2.setText(numberFormat.format(distance_text) + getString(R.string.k_m));
                                duration2.setText(duration_text);
                                if (Started) {
                                    bottomSheet_view.setVisibility(View.VISIBLE);
                                    bottomSheet.setVisibility(View.GONE);
                                } else {
                                    bottomSheet_view.setVisibility(View.GONE);
                                    bottomSheet.setVisibility(View.VISIBLE);
                                    mMap.setPadding(0, 170, 0, mBottomSheetBehavior.getPeekHeight());
                                }


                                //------------Displaying Distance and Time-----------------\\
                                //   showingDistanceTime(distance, duration); // Showing distance and time to the user in the UI \\
//                            String message = "Total Distance is " + distance + " and Estimated Time is " + duration;
//                            StaticMethods.customSnackBar(consumerHomeActivity.parentLayout, message,
//                                    getResources().getColor(R.color.colorPrimary),
//                                    getResources().getColor(R.color.colorWhite), 3000);

                                /*   Toast.makeText(getActivity(), distance + "\n" + duration, Toast.LENGTH_SHORT).show();*/

                                //--------------Drawing Path-----------------\\
                                mMap2.clear();
                                polylineOptions = DirectionConverter.createPolyline(getActivity(),
                                        leg.getDirectionPoint(), 3, getResources().getColor(R.color.dark_mid_color));
                                polyline = mMap.addPolyline(polylineOptions);
                                routed = true;



                                setCameraWithCoordinationBounds(route);
                                //------------------------------------------------------------------\\

                            } else {
                                Toast.makeText(getActivity(), "No routes exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            // Do something here
                        }
                    });


        }

    }

    void moveCameraToRoute(Marker start, Marker end) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(start.getPosition());
        builder.include(end.getPosition());
        LatLngBounds bounds = builder.build();
        int padding = 50; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
        mMap2.animateCamera(cu, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {

                snapShot();
            }

            @Override
            public void onCancel() {
            }
        });

    }

    private void setCameraWithCoordinationBounds(Route route) {

        if (route != null) {
            LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
            LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
            LatLngBounds bounds = new LatLngBounds(southwest, northeast);

            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                }
            });


            mMap2.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));


            try {
                CommonUtilities.hideDialog();
            } catch (Exception e) {
            }

        }

    }

    BitmapDescriptor resizeMarkerIcon(int icon) {
        int height = 25;
        int width = 25;
        Bitmap b = BitmapFactory.decodeResource(getResources(), icon);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
        return smallMarkerIcon;
    }

    BitmapDescriptor resizeMarkerIcon3(int icon) {
        int height = 80;
        int width = 70;
        Bitmap b = BitmapFactory.decodeResource(getResources(), icon);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
        return smallMarkerIcon;
    }


    private void getCurrentLocation() {

        airLocation = new AirLocation(getActivity(), true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(Location location) {


                try {
                    CommonUtilities.hideDialog();
                } catch (Exception e) {
                }

                myLocation = location;

                if (TextUtils.isEmpty(tv_startTrip.getText().toString())) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));


                    start_latlng = new LatLng(location.getLatitude(), location.getLongitude());

                    Geocoder geocoder;
                    List<Address> addresses = new ArrayList<>();
                    geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addresses.size() != 0) {
                        tv_startTrip.setText(addresses.get(0).getAddressLine(0));
                        tv_startTrip_preview.setText(addresses.get(0).getAddressLine(0));
                    }


                    if (start_marker == null) {
                        start_marker = mMap.addMarker(new MarkerOptions().position(start_latlng).icon(resizeMarkerIcon(R.drawable.start_marker)));
                    } else {
                        start_marker.setPosition(start_latlng);
                    }


                    if (end_marker == null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start_latlng, 15));

                    } else {
                        moveCameraToRoute(start_marker, end_marker);
                    }

                }


            }

            @Override
            public void onFailed(AirLocation.LocationFailedEnum locationFailedEnum) {
                Toast.makeText(getActivity(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                try {
                    CommonUtilities.hideDialog();
                } catch (Exception e) {
                }
            }
        });


    }

    private void get_location() {
        airLocation = new AirLocation(getActivity(), true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(Location location) {

                myLocation = location;


            }

            @Override
            public void onFailed(AirLocation.LocationFailedEnum locationFailedEnum) {


            }
        });


    }

    private BroadcastReceiver mGpsSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                getCurrentLocation();
            }
        }
    };


    private void reset() {


        if (start_marker != null) {
            start_marker.remove();
            start_marker = null;
        }
        if (end_marker != null) {
            end_marker.remove();
            end_marker = null;
        }

        if (polyline != null) {
            polyline.remove();
            polyline = null;
        }

        tv_startTrip.setText("");
        tv_endTrip.setText("");

        start_latlng = null;
        end_latlang = null;

        ready = false;
        routed = false;
        Started = false;

        bottomSheet.setVisibility(View.GONE);
        bottomSheet_view.setVisibility(View.GONE);
        mMap.setPadding(0, 170, 0, 75);


        getCurrentLocation();


    }


    void snapShot() {


        mMap2.addMarker(new MarkerOptions().position(start_latlng).icon(resizeMarkerIcon(R.drawable.start_marker)));
        mMap2.addMarker(new MarkerOptions().position(end_latlang).icon(resizeMarkerIcon(R.drawable.end_marker)));
        mMap2.addPolyline(polylineOptions);

        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {


            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                map_snapshot = snapshot;
                base64 = encodeImage(scaleDown(map_snapshot, 700, true));


                CommonUtilities.showStaticDialog(getActivity(), "startTrip");

                Map<String, String> parms = new HashMap<>();
                parms.put("start_lat", String.valueOf(start_latlng.latitude));
                parms.put("start_lng", String.valueOf(start_latlng.longitude));
                parms.put("start_address", tv_startTrip.getText().toString());
                parms.put("end_lat", String.valueOf(end_latlang.latitude));
                parms.put("end_lng", String.valueOf(end_latlang.longitude));
                parms.put("end_address", tv_endTrip.getText().toString());
                parms.put("distance", String.valueOf(distance_text));
                parms.put("estimated_duration", duration.getText().toString());
                parms.put("publisher_id", dataManager.getID());
                parms.put("map_screen_shot", base64);

                myAPI.start_trip(parms)
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


                                    Started = true;
                                    moveCameraToRoute(start_marker, end_marker);
                                    dataManager.tripStarted(true);
                                    dataManager.tripID(apiResponse.getData().getTrip().getId().toString());


                                    bottomSheet.setVisibility(View.GONE);
                                    bottomSheet_view.setVisibility(View.VISIBLE);
                                    mMap.setPadding(0, 170, 0, mBottomSheetBehavior_view.getPeekHeight());


                                } else {
                                    Toast.makeText(getActivity(), apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(getActivity(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                CommonUtilities.hideDialog();
                            }

                            @Override
                            public void onComplete() {


                            }
                        });


            }
        };
        mMap2.snapshot(callback);

    }


    void start_trip() {


        snapShot();

    }


    void get_currentTrip() {


        CommonUtilities.showStaticDialog(getActivity(), "get_current");

        Map<String, String> parms = new HashMap<>();
        parms.put("publisher_id", dataManager.getID());
        myAPI.get_currentTrip(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {
                            mMap.setPadding(0, 170, 0, mBottomSheetBehavior_view.getPeekHeight());

                            Started = true;
                            get_opened_trip = true;

                            start_latlng = new LatLng(apiResponse.getData().getCurrentTrip().getTrip().getStartLat(), apiResponse.getData().getCurrentTrip().getTrip().getStartLng());
                            tv_startTrip.setText(apiResponse.getData().getCurrentTrip().getTrip().getStartAddress());
                            tv_startTrip_preview.setText(apiResponse.getData().getCurrentTrip().getTrip().getStartAddress());
                            if (start_marker == null) {
                                start_marker = mMap.addMarker(new MarkerOptions().position(start_latlng).icon(resizeMarkerIcon(R.drawable.start_marker)));
                            } else {
                                start_marker.setPosition(start_latlng);
                            }

                            end_latlang = new LatLng(apiResponse.getData().getCurrentTrip().getTrip().getEndLat(), apiResponse.getData().getCurrentTrip().getTrip().getEndLng());
                            tv_endTrip.setText(apiResponse.getData().getCurrentTrip().getTrip().getEndAddress());
                            tv_endTrip_preview.setText(apiResponse.getData().getCurrentTrip().getTrip().getEndAddress());
                            if (end_marker == null) {
                                end_marker = mMap.addMarker(new MarkerOptions().position(end_latlang).icon(resizeMarkerIcon(R.drawable.end_marker)));
                            } else {
                                start_marker.setPosition(end_latlang);
                            }


                            dataManager.tripStarted(true);
                            dataManager.tripID(apiResponse.getData().getCurrentTrip().getTrip().getId().toString());

                            requestDirection();


                        } else {

                            getCurrentLocation();

                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        CommonUtilities.hideDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    void end_trip(String privacy, String status, String desc) {

        CommonUtilities.showStaticDialog(getActivity(), "endTrip");

        Map<String, String> parms = new HashMap<>();
        parms.put("trip_id", dataManager.getTripID());
        parms.put("publisher_id", dataManager.getID());
        parms.put("privacy", privacy);
        parms.put("status", status);
        parms.put("desc", desc);


        myAPI.end_trip(parms)
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


                            Started = false;
                            get_opened_trip = false;
                            reset();
                            dataManager.tripStarted(false);


                            getCurrentLocation();


                            shopping_medicines.setVisibility(View.VISIBLE);


                        } else {
                            Toast.makeText(getActivity(), apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();

                        CommonUtilities.hideDialog();
                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }

    @Subscribe
    public void onEvent(EventBusMssg_home model) {
        end_trip(model.getPrivacy(), model.getStatus(), model.getDesc());
    }


    BitmapDescriptor resizeMarkerIcon2(Bitmap b) {
        int height = 80;
        int width = 80;
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
        return smallMarkerIcon;
    }


    void get_user() {


        Map<String, String> parms = new HashMap<>();
        parms.put("user_id", dataManager.getID());

        myAPI.get_user(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {

                            if (String.valueOf(apiResponse.getData().getUser().getStatus()).equals("0")) {
                                dataManager.setLoggedIn(false);
                                startActivity(new Intent(getActivity(), LoginScreen.class));
                                getActivity().finish();

                            }
                        } else {

                            getCurrentLocation();

                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }

                });
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

            TextView date = ((TextView) myContentsView.findViewById(R.id.info_date));
            TextView desc = ((TextView) myContentsView.findViewById(R.id.info_desc));
            TextView type = ((TextView) myContentsView.findViewById(R.id.info_type));

            ImageView image = ((ImageView) myContentsView.findViewById(R.id.info_image));

            marker.hideInfoWindow();

            try {
                date.setText(infoWindowData.getDate());
            } catch (Exception e) {

                date.setText("");
            }

            try {
                desc.setText(infoWindowData.getDesc());
            } catch (Exception e) {
                desc.setText("");
            }


            try {
                type.setText(infoWindowData.getType());
            } catch (Exception e) {
                type.setText("");
            }


            try {
                if (infoWindowData.getType().equals("RISK")) {

                    image.setImageResource(R.drawable.risk_marker);


                } else {
                    image.setImageResource(R.drawable.suggest_mark);

                }
            } catch (Exception e) {
                image.setImageDrawable(null);
            }


            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }


    private class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                return null;
            }
        }

        protected void onPostExecute(Bitmap result) {

        }

    }

}



