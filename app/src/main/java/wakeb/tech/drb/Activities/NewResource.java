package wakeb.tech.drb.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Home.SelectLocation;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.Uitils.ScalingUtilities;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;
import wakeb.tech.drb.imagepiker.adapter.ImagesAdapter;
import wakeb.tech.drb.imagepiker.features.ImagePicker;
import wakeb.tech.drb.imagepiker.model.Image;
import wakeb.tech.drb.ui.addNewSpot.JourneysList;
import wakeb.tech.drb.ui.addNewSpot.adpters.JourneysAdapter;

import static io.fabric.sdk.android.Fabric.TAG;

public class NewResource extends BaseActivity implements OnMapReadyCallback, JourneysAdapter.AttachJourney {
    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;
    double lat, lng;
    public KProgressHUD blg;

    boolean location = false;

    JourneysList journeysList;

    String CountryName, AdminArea, SubAdminArea, Locality;

    @OnClick(R.id.Upload)
    void upload() {


        ArrayList<File> files = new ArrayList<>();

        for (Image image : imageViewerModels) {
            files.add(new File(decodeFile(image.getPath(), 1000, 1000)));

            Log.i("FILENAME", new File(decodeFile(image.getPath(), 1000, 1000)).getAbsolutePath());
        }


        if (files.size() == 0) {
            Toast.makeText(this, "you have upload at least one image ", Toast.LENGTH_SHORT).show();

        } else if (!location) {
            Toast.makeText(this, "you have choose location on map ", Toast.LENGTH_SHORT).show();

        } else {
            blg = KProgressHUD.create(NewResource.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();


            if (TextUtils.isEmpty(ID)) {
                AndroidNetworking.upload("http://3.17.76.229/api/trip/create-spot")
                        .addMultipartFileList("files[]", files)
                        .addMultipartParameter("publisher_id", dataManager.getID())
                        .addMultipartParameter("location", tv_startTrip.getText().toString() + " ")
                        .addMultipartParameter("desc", spot_desc.getText().toString() + " ")
                        .addMultipartParameter("lat", String.valueOf(lat))
                        .addMultipartParameter("lng", String.valueOf(lng))
                        .addMultipartParameter("place_name", spot_title.getText().toString() + " ")
                        .addMultipartParameter("country", CountryName + " ")
                        .addMultipartParameter("city", AdminArea + " ")
                        .addMultipartParameter("subcity", SubAdminArea + " ")
                        .addMultipartParameter("locality", Locality + " ")
                        .setPriority(Priority.HIGH)
                        .build()
                        .setUploadProgressListener(new UploadProgressListener() {
                            @Override
                            public void onProgress(long bytesUploaded, long totalBytes) {

                                float f = ((float) bytesUploaded / (float) totalBytes) * 100;
                                int test = Math.round(f);
                                //    blg.setLabel(String.valueOf(test) + "%");

                                Log.i("Uploading ... =", String.valueOf(test) + "%");

                                blg.setLabel(String.valueOf(test) + "%");

                            }
                        })
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                CommonUtilities.hideDialog();
                                Gson gson = new Gson();
                                ApiResponse apiResponse = gson.fromJson(response.toString(), ApiResponse.class);

                                if (apiResponse.getStatus()) {

                                    blg.dismiss();
                                    Log.i("Uploading ... =", "Successful");

                                    Toast.makeText(NewResource.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                    finish();

                                } else {
                                    Toast.makeText(NewResource.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();

                                }


                            }

                            @Override
                            public void onError(ANError error) {

                                Log.i("Uploading ... =", "ANError" + error.getErrorBody());
                                Toast.makeText(NewResource.this, R.string.connection_error, Toast.LENGTH_SHORT).show();

                                CommonUtilities.hideDialog();

                            }
                        });
            } else {
                AndroidNetworking.upload("http://3.17.76.229/api/trip/create-spot")
                        .addMultipartFileList("files[]", files)
                        .addMultipartParameter("publisher_id", dataManager.getID())
                        .addMultipartParameter("location", tv_startTrip.getText().toString() + " ")
                        .addMultipartParameter("desc", spot_desc.getText().toString() + " ")
                        .addMultipartParameter("lat", String.valueOf(lat))
                        .addMultipartParameter("lng", String.valueOf(lng))
                        .addMultipartParameter("place_name", spot_title.getText().toString() + " ")
                        .addMultipartParameter("country", CountryName + " ")
                        .addMultipartParameter("city", AdminArea + " ")
                        .addMultipartParameter("subcity", SubAdminArea + " ")
                        .addMultipartParameter("locality", Locality + " ")
                        .addMultipartParameter("journey_id", ID)
                        .setPriority(Priority.HIGH)
                        .build()
                        .setUploadProgressListener(new UploadProgressListener() {
                            @Override
                            public void onProgress(long bytesUploaded, long totalBytes) {

                                float f = ((float) bytesUploaded / (float) totalBytes) * 100;
                                int test = Math.round(f);
                                //    blg.setLabel(String.valueOf(test) + "%");

                                Log.i("Uploading ... =", String.valueOf(test) + "%");

                                blg.setLabel(String.valueOf(test) + "%");

                            }
                        })
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                CommonUtilities.hideDialog();
                                Gson gson = new Gson();
                                ApiResponse apiResponse = gson.fromJson(response.toString(), ApiResponse.class);

                                if (apiResponse.getStatus()) {

                                    blg.dismiss();
                                    Log.i("Uploading ... =", "Successful");

                                    Toast.makeText(NewResource.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                    finish();

                                } else {
                                    Toast.makeText(NewResource.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();

                                }


                            }

                            @Override
                            public void onError(ANError error) {

                                Log.i("Uploading ... =", "ANError" + error.getErrorBody());
                                Toast.makeText(NewResource.this, R.string.connection_error, Toast.LENGTH_SHORT).show();

                                CommonUtilities.hideDialog();

                            }
                        });
            }


        }


    }

    RecyclerView recyclerView;
    ImagesAdapter imagesAdapter;
    ArrayList<Image> imageViewerModels;

    @BindView(R.id.journey_name)
    TextView journey_name;

    @BindView(R.id.attach_icon)
    ImageView attach_icon;

    @BindView(R.id.tv_startTrip)
    TextView tv_startTrip;

    @BindView(R.id.attach_icon_close)
    ImageView attach_icon_close;


    @OnClick(R.id.attach_icon_close)
    void attach_icon_close() {
        ID = "";
        NAME = "";
        DESC = "";

        journey_name.setText(getString(R.string.attach_to_journey_book));
        attach_optional.setVisibility(View.VISIBLE);
        attach_icon.setVisibility(View.VISIBLE);
        attach_icon_close.setVisibility(View.GONE);

    }


    @BindView(R.id.attach_optional)
    TextView attach_optional;


    @BindView(R.id.spot_title)
    TextView spot_title;


    @OnClick(R.id.map_view_location)
    void map_view_location() {
        startActivityForResult(new Intent(this, SelectLocation.class), 1002);

    }


    @OnClick(R.id.attach_to_journey)
    void attach_to_journey() {

        journeysList = new JourneysList();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, 0, 0)
                .replace(R.id.Container, journeysList, JourneysList.TAG).commit();

    }


    @BindView(R.id.spot_desc)
    TextInputEditText spot_desc;


    @BindView(R.id.toolbar)
    Toolbar toolbar;


    String ID, NAME, DESC;


    private MapView mapView;
    private GoogleMap mMap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private Bundle mapViewBundle;


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
        setContentView(R.layout.activity_new_resource);
        ButterKnife.bind(this);
        AndroidNetworking.initialize(getApplicationContext());

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.add_new_stop_point));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_btn);


        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);


        mapView = findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        ID = getIntent().getStringExtra("ID");
        NAME = getIntent().getStringExtra("NAME");
        DESC = getIntent().getStringExtra("DESC");

        if (ID != null) {
            journey_name.setText(NAME);
        }

        imageViewerModels = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        imagesAdapter = new ImagesAdapter(this, imageViewerModels);
        recyclerView.setAdapter(imagesAdapter);


       /* airLocation = new AirLocation(this, true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(Location location) {

                lat = location.getLatitude();
                lng = location.getLongitude();

                Geocoder geocoder;
                List<Address> addresses = new ArrayList<>();
                geocoder = new Geocoder(NewResource.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addresses.size() != 0) {
                    address = addresses.get(0).getAddressLine(0);
                    tv_startTrip.setText(address);
                }
            }

            @Override
            public void onFailed(AirLocation.LocationFailedEnum locationFailedEnum) {


            }
        });*/

    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);


            imageViewerModels.addAll(images);
            imagesAdapter.setMy_data(imageViewerModels);

        } else if (requestCode == 1002) {

            if (resultCode != RESULT_CANCELED) {
                lat = Double.parseDouble(data.getStringExtra("latitude"));
                lng = Double.parseDouble(data.getStringExtra("longitude"));

                tv_startTrip.setText(data.getStringExtra("address"));
                CountryName = data.getStringExtra("CountryName");
                AdminArea = data.getStringExtra("AdminArea");
                SubAdminArea = data.getStringExtra("SubAdminArea");
                Locality = data.getStringExtra("Locality");


                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
                location = true;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));


            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage("")

                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(NewResource.this,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 22);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(NewResource.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 22);

        }
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 22) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (journeysList != null) {
            getSupportFragmentManager()
                    .beginTransaction().remove(journeysList).commit();
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (journeysList != null) {
                    getSupportFragmentManager()
                            .beginTransaction().remove(journeysList).commit();
                    journeysList = null;
                    Toast.makeText(this, "no", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }

        return super.onOptionsItemSelected(item);
    }


    private String decodeFile(String path, int DESIREDWIDTH, int DESIREDHEIGHT) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/TMMFOLDER");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = String.valueOf(System.currentTimeMillis()) + "tmp.png";

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

    }


    @Override
    public void attach(String id, String name, String desc) {

        if (journeysList != null) {

            getSupportFragmentManager()
                    .beginTransaction().remove(journeysList).commit();

            ID = id;
            NAME = name;
            DESC = desc;

            if (ID != null) {
                journey_name.setText(NAME);
                attach_optional.setVisibility(View.GONE);
                attach_icon.setVisibility(View.GONE);
                attach_icon_close.setVisibility(View.VISIBLE);

            }

        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

        mMap.setPadding(0, 100, 0, 0);
        mMap.getUiSettings().setAllGesturesEnabled(false);

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

    }


    @Override
    protected void init() {

    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.invalidate();
        mapView.onStart();


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
    public void onResume() {
        super.onResume();
        mapView.onResume();

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
}
