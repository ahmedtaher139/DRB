package wakeb.tech.drb.Home;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.graphics.drawable.ColorDrawable;
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
import androidx.fragment.app.Fragment;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
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
import com.google.android.material.textfield.TextInputEditText;
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
import wakeb.tech.drb.Activities.Settings;
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
import wakeb.tech.drb.Registration.SplashScreen;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.Uitils.GetBitmapTask;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

import static io.fabric.sdk.android.Fabric.TAG;
import static wakeb.tech.drb.Uitils.CommonUtilities.encodeImage;
import static wakeb.tech.drb.Uitils.CommonUtilities.scaleDown;


public class HomeFragment extends Fragment implements OnMapReadyCallback {


    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }



    @BindView(R.id.getMyLocation)
    ImageButton getMyLocation;


    private MapView mapView;
    private GoogleMap mMap;


    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    private ApiServices myAPI;
    private Retrofit retrofit;
    private DataManager dataManager;
    private Bundle mapViewBundle;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        dataManager = ((MainApplication) getActivity().getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

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
                            context, R.raw.mapstyle));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

  /*      mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
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

                    if (infoWindowData.getType().equals(context.getString(R.string.risk))) {


                        Intent intent = new Intent(context, ViewPlaces.class);
                        intent.putExtra("TYPE", infoWindowData.getType());
                        intent.putExtra("ADDRESS", new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
                        intent.putExtra("URL", infoWindowData.getImage_url());
                        intent.putExtra("DESC", infoWindowData.getDesc());
                        startActivity(intent);


                    } else if (infoWindowData.getType().equals(context.getString(R.string.suggest))) {

                        Intent intent = new Intent(context, ViewPlaces.class);
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
*/

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

/*

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
*/


    }

    public void onPause() {
        mapView.onPause();
        super.onPause();
/*
        try {
            CommonUtilities.hideDialog();
        } catch (Exception e) {
        }*/

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

                break;

            case 1002:




                break;
        }
      //  airLocation.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       // airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);

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

}



