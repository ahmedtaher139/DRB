package wakeb.tech.drb.Activities;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
//import hb.xvideoplayer.MxVideoPlayer;
//import hb.xvideoplayer.MxVideoPlayerWidget;
import hb.xvideoplayer.MxVideoPlayer;
import hb.xvideoplayer.MxVideoPlayerWidget;
import mumayank.com.airlocationlibrary.AirLocation;
import retrofit2.Retrofit;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Home.SelectLocation;
import wakeb.tech.drb.R;
import wakeb.tech.drb.SuggestionPlaces.AddNewPlace;
import wakeb.tech.drb.Uitils.ImagesBottomSheet;
import wakeb.tech.drb.Uitils.ScalingUtilities;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

import static wakeb.tech.drb.Uitils.CommonUtilities.getCustomImagePath;

public class NewResource extends BaseActivity {
    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;
    File file;
    String mCapturedImageUrl, media_type, address;
    double lat, lng;
    AirLocation airLocation;
    public KProgressHUD blg;

    @OnClick(R.id.start_location)
    void start_location() {

        startActivityForResult(new Intent(this, SelectLocation.class), 1002);

    }


    @BindView(R.id.image_layout)
    LinearLayout image_layout;

    @BindView(R.id.image_button_layout)
    LinearLayout image_button_layout;



    @OnClick(R.id.Upload)
    void upload() {


        blg = KProgressHUD.create(NewResource.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)

                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        AndroidNetworking.upload("http://3.17.76.229/api/trip/upload-resource")
                .addMultipartFile("resource", file)
                .addMultipartParameter("trip_id", dataManager.getTripID())
                .addMultipartParameter("address", address)
                .addMultipartParameter("type", media_type)
                .addMultipartParameter("desc", upload_et.getText().toString())
                .addMultipartParameter("lat", String.valueOf(lat))
                .addMultipartParameter("lng", String.valueOf(lng))
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {

                        float f = ((float) bytesUploaded / (float) totalBytes) * 100;
                        int test = Math.round(f);
                        blg.setLabel(String.valueOf(test) + "%");

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        blg.dismiss();

                        Gson gson = new Gson();
                        ApiResponse apiResponse = gson.fromJson(response.toString(), ApiResponse.class);

                        if (apiResponse.getStatus()) {
                            Toast.makeText(NewResource.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();// First key in your json object
                            finish();
                        } else {
                            Toast.makeText(NewResource.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();// First key in your json object

                        }


                    }

                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(NewResource.this, error.getErrorBody(), Toast.LENGTH_SHORT).show();// First key in your json object
                        finish();
                    }
                });

    }


    @OnClick(R.id.choose_video)
    void choose_video() {

        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {




            final String[] items = new String[]{"Choose from camera", "Choose from gallery"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setItems(items, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (items[which].equals("Choose from camera")) {


                        Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
                        intent.putExtra(android.provider.MediaStore.EXTRA_VIDEO_QUALITY, 0);
                        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 5);
                        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
                        startActivityForResult(intent, 111);

                    } else if (items[which].equals("Choose from gallery")) {

                        Intent intent = new Intent();
                        intent.setType("video/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Video"),222);

                    }
                }
            });
            AlertDialog alertDialog = builder.show();
            alertDialog.setCanceledOnTouchOutside(true);






        } else {
            requestCameraPermission();
        }
    }

    @OnClick(R.id.back_button)
    void back_button() {
        finish();
    }

    @OnClick(R.id.choose_image)
    void choose_image() {
        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


            final String[] items = new String[]{"Choose from camera", "Choose from gallery"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setItems(items, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (items[which].equals("Choose from camera")) {


                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = getCustomImagePath(NewResource.this, System.currentTimeMillis() + "");
                        mCapturedImageUrl = file.getAbsolutePath();
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        startActivityForResult(takePicture, 30);


                    } else if (items[which].equals("Choose from gallery")) {

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 20);

                    }
                }
            });
            AlertDialog alertDialog = builder.show();
            alertDialog.setCanceledOnTouchOutside(true);


        } else {
            requestCameraPermission();
        }
    }


    @BindView(R.id.tv_startTrip)
    TextView tv_startTrip;


    @BindView(R.id.upload_et)
    TextInputEditText upload_et;


    @BindView(R.id.upload_videoView_layout)
    RelativeLayout upload_videoView_layout;


    @BindView(R.id.mxVideoPlayerWidget)
    MxVideoPlayerWidget mxVideoPlayerWidget;


    @BindView(R.id.upload_imageView)
    ImageView upload_imageView;

    @OnClick(R.id.cancel_image)
    void cancel_image() {

        file = null;
        image_button_layout.setVisibility(View.VISIBLE);
        image_layout.setVisibility(View.GONE);

    }


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
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        init();

        airLocation = new AirLocation(this, true, true, new AirLocation.Callbacks() {
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
        });


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        airLocation.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 111) {

                image_button_layout.setVisibility(View.GONE);
                image_layout.setVisibility(View.VISIBLE);

                upload_imageView.setVisibility(View.GONE);
                upload_videoView_layout.setVisibility(View.VISIBLE);
                media_type = "vedio";
                file = new File(getPathFromUri(this, data.getData()));

                mxVideoPlayerWidget.startPlay(file.getPath(), MxVideoPlayer.SCREEN_LAYOUT_NORMAL, "");





            }
            else if (requestCode == 222) {

                image_button_layout.setVisibility(View.GONE);
                image_layout.setVisibility(View.VISIBLE);

                upload_imageView.setVisibility(View.GONE);
                upload_videoView_layout.setVisibility(View.VISIBLE);
                media_type = "vedio";
                file = new File(getPathFromUri(this, data.getData()));

                mxVideoPlayerWidget.startPlay(file.getPath(), MxVideoPlayer.SCREEN_LAYOUT_NORMAL, "");





            } else if (requestCode == 30) {

                image_button_layout.setVisibility(View.GONE);
                image_layout.setVisibility(View.VISIBLE);

                upload_imageView.setVisibility(View.VISIBLE);
                upload_videoView_layout.setVisibility(View.GONE);
                media_type = "image";


                file = new File(decodeFile(mCapturedImageUrl, 600, 600));
                upload_imageView.setImageURI(Uri.fromFile(file));


            } else if (requestCode == 20) {

                image_button_layout.setVisibility(View.GONE);
                image_layout.setVisibility(View.VISIBLE);


                upload_imageView.setVisibility(View.VISIBLE);
                upload_videoView_layout.setVisibility(View.GONE);
                media_type = "image";


                upload_imageView.setImageURI(data.getData());
                file = new File(decodeFile(getPathFromUri(this, data.getData()), 600, 600));


            } else if (requestCode == 1002) {
                try {


                    address = data.getStringExtra("address");
                    lat = Double.parseDouble(data.getStringExtra("latitude"));
                    lng = Double.parseDouble(data.getStringExtra("longitude"));
                    tv_startTrip.setText(address);


                } catch (Exception e) {

                }


            }
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
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

            String s = "tmp.png";

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
    protected void onPause() {
        super.onPause();
        try {
            MxVideoPlayer.releaseAllVideos();

        } catch (Exception e) {

        }
    }

}
