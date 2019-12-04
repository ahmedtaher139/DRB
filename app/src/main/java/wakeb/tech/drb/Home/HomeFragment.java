package wakeb.tech.drb.Home;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import mumayank.com.airlocationlibrary.AirLocation;
import retrofit2.Retrofit;
import wakeb.tech.drb.Base.BaseFragment;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Models.InfoWindowData;
import wakeb.tech.drb.Models.MapSpots;
import wakeb.tech.drb.R;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;
import wakeb.tech.drb.databinding.FragmentHomeBinding;
import wakeb.tech.drb.ui.searched.SearchedList;


public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements OnMapReadyCallback {

    public static final String TAG = HomeFragment.class.getSimpleName();
    private FragmentHomeBinding fragmentHomeBinding;

    private GoogleMap mMap;


    private static final String MAP_VIEW_BUNDLE_KEY = "mapViewBundleKey";
    AirLocation airLocation;
    private ApiServices myAPI;
    private Retrofit retrofit;
    private DataManager dataManager;
    private Bundle mapViewBundle;

    private String search;

    int current_zoom = 100;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentHomeBinding = getViewDataBinding();


        fragmentHomeBinding.mapView.onCreate(mapViewBundle);
        fragmentHomeBinding.mapView.getMapAsync(this);
        mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }



    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        dataManager = ((MainApplication) getBaseActivity().getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);


    }


    @Override
    public void onStart() {
        super.onStart();
        fragmentHomeBinding.mapView.invalidate();
        fragmentHomeBinding.mapView.onStart();


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        getSpots();


        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getBaseActivity(), R.raw.mapstyle));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        mMap.setPadding(0, 170, 0, 75);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);


        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {


                getSpots();

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

                Fragment fragment = new SearchedList();
                Bundle args = new Bundle();
                args.putString("SEARCH_TYPE", infoWindowData.getType());
                args.putString("SEARCH_ID", infoWindowData.getId());
                args.putString("SEARCH_NAME", infoWindowData.getName());
                fragment.setArguments(args);
                getBaseActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, 0, 0)
                        .replace(R.id.Container_new, fragment, SearchedList.TAG)
                        .addToBackStack(SearchedList.TAG).commit();


                return false;


            }
        });

        if (ActivityCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                    if (infoWindowData.getType().equals(getBaseActivity().getString(R.string.risk))) {


                        Intent intent = new Intent(getBaseActivity(), ViewPlaces.class);
                        intent.putExtra("TYPE", infoWindowData.getType());
                        intent.putExtra("ADDRESS", new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
                        intent.putExtra("URL", infoWindowData.getImage_url());
                        intent.putExtra("DESC", infoWindowData.getDesc());
                        startActivity(intent);


                    } else if (infoWindowData.getType().equals(getBaseActivity().getString(R.string.suggest))) {

                        Intent intent = new Intent(getBaseActivity(), ViewPlaces.class);
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

        fragmentHomeBinding.mapView.onStop();
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

        fragmentHomeBinding.mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentHomeBinding.mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentHomeBinding.mapView.onResume();
    }

    public void onPause() {
        fragmentHomeBinding.mapView.onPause();
        super.onPause();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        fragmentHomeBinding.mapView.onLowMemory();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case 1003:

                break;

            case 1002:


                break;
        }
        airLocation.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }


    void getSpots() {
        if (current_zoom > 7 && (int) mMap.getCameraPosition().zoom > 7) {

        } else if (current_zoom < 7 && (int) mMap.getCameraPosition().zoom < 7) {

        } else {
            Geocoder geocoder;
            List<Address> addresses = new ArrayList<>();
            geocoder = new Geocoder(getBaseActivity(), new Locale("en"));
            try {
                addresses = geocoder.getFromLocation(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses.size() != 0) {

                Log.i("ADDRESS", "getCountryName = " + addresses.get(0).getCountryName());
                Log.i("ADDRESS", "getAdminArea = " + addresses.get(0).getAdminArea());
                Log.i("ADDRESS", "getSubAdminArea = " + addresses.get(0).getSubAdminArea());
                Log.i("ADDRESS", "getLocality = " + addresses.get(0).getLocality());

                Log.i("ZOOM", "ZOOM = " + String.valueOf((int) mMap.getCameraPosition().zoom));


                current_zoom = (int) mMap.getCameraPosition().zoom;
                if ((int) mMap.getCameraPosition().zoom < 7) {
                    search = "";
                    Map<String, String> parms = new HashMap<>();
                    parms.put("zoom", String.valueOf((int) mMap.getCameraPosition().zoom));
                    parms.put("search", "");
                    myAPI.get_spots_count(parms)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<ApiResponse>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(ApiResponse apiResponse) {

                                    if (apiResponse.getStatus()) {
                                        mMap.clear();
                                        for (MapSpots mapSpots : apiResponse.getData().getMapSpots()) {


                                              /*      IconGenerator iconGen = new IconGenerator(getBaseActivity());
                                                    MarkerOptions markerOptions = new MarkerOptions().
                                                            icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon(String.valueOf(mapSpots.getCount())))).
                                                            position(new LatLng(mapSpots.getMapSpot().getLat(), mapSpots.getMapSpot().getLng())).
                                                            anchor(iconGen.getAnchorU(), iconGen.getAnchorV());

                                                    mMap.addMarker(markerOptions);
*/


                                            View marker_view = ((LayoutInflater) getBaseActivity().getSystemService(getBaseActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);

                                            TextView counter = (TextView) marker_view.findViewById(R.id.addressTxt);
                                            counter.setText(String.valueOf(mapSpots.getCount()));
                                            Marker marker = mMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(mapSpots.getMapSpot().getLat(), mapSpots.getMapSpot().getLng()))
                                                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getBaseActivity(), marker_view))));

                                            InfoWindowData infoWindowData = new InfoWindowData(String.valueOf(mapSpots.getSearchId()), mapSpots.getSearchType(), mapSpots.getSearch());

                                            marker.setTag(infoWindowData);


                                        }

                                    } else {

                                        Toast.makeText(getBaseActivity(), apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                    }


                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getBaseActivity(), getBaseActivity().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                    Log.i("ERROR_RETROFIT", e.getMessage());

                                }

                                @Override
                                public void onComplete() {


                                }
                            });

                } else {

                    search = "";
                    Map<String, String> parms = new HashMap<>();
                    parms.put("zoom", String.valueOf((int) mMap.getCameraPosition().zoom));
                    parms.put("search", addresses.get(0).getCountryName());
                    myAPI.get_spots_count(parms)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<ApiResponse>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(ApiResponse apiResponse) {

                                    if (apiResponse.getStatus()) {
                                        mMap.clear();
                                        for (MapSpots mapSpots : apiResponse.getData().getMapSpots()) {

                                       /*             IconGenerator iconGen = new IconGenerator(getBaseActivity());
                                                    MarkerOptions markerOptions = new MarkerOptions().
                                                            icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon(String.valueOf(mapSpots.getCount())))).
                                                            position(new LatLng(mapSpots.getMapSpot().getLat(), mapSpots.getMapSpot().getLng())).
                                                            anchor(iconGen.getAnchorU(), iconGen.getAnchorV());

                                                    mMap.addMarker(markerOptions);*/


                                            View marker_view = ((LayoutInflater) getBaseActivity().getSystemService(getBaseActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);

                                            TextView counter = (TextView) marker_view.findViewById(R.id.addressTxt);
                                            counter.setText(String.valueOf(mapSpots.getCount()));
                                            Marker marker = mMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(mapSpots.getMapSpot().getLat(), mapSpots.getMapSpot().getLng()))
                                                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getBaseActivity(), marker_view))));


                                            InfoWindowData infoWindowData = new InfoWindowData(String.valueOf(mapSpots.getSearchId()), mapSpots.getSearchType(), mapSpots.getSearch());

                                            marker.setTag(infoWindowData);


                                        }

                                    } else {

                                        Toast.makeText(getBaseActivity(), apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                    }


                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getBaseActivity(), getBaseActivity().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                    Log.i("ERROR_RETROFIT", e.getMessage());

                                }

                                @Override
                                public void onComplete() {


                                }
                            });


                }


            } else {
            }
        }

    }


    private void getCurrentLocation() {

        airLocation = new AirLocation(getBaseActivity(), true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(Location location) {

                getSpots();

            }

            @Override
            public void onFailed(AirLocation.LocationFailedEnum locationFailedEnum) {
                Toast.makeText(getBaseActivity(), R.string.connection_error, Toast.LENGTH_SHORT).show();

            }
        });

    }


}



