package wakeb.tech.drb.SuggestionPlaces;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Home.SelectLocation;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Risks.AddNewRisk;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.Uitils.ImagesBottomSheet;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

import static wakeb.tech.drb.Uitils.CommonUtilities.getCustomImagePath;


public class AddNewPlace extends BaseActivity {


    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;

    String mCapturedImageUrl;
    String suggest_address;
    double lat, lng;
    File file;

    @BindView(R.id.image_layout)
    LinearLayout image_layout;

    @BindView(R.id.image_button_layout)
    LinearLayout image_button_layout;


    @OnClick(R.id.cancel_image)
    void cancel_image() {

        file = null;
        image_button_layout.setVisibility(View.VISIBLE);
        image_layout.setVisibility(View.GONE);

    }

    @OnClick(R.id.back_button)
    void back_button() {
        finish();
    }

    @BindView(R.id.suggest_imageView)
    ImageView suggest_imageView;

    @BindView(R.id.tv_suggest)
    TextView tv_suggest;


    @BindView(R.id.suggest_desc)
    EditText suggest_desc;


    @OnClick(R.id.choose_gallery)
    void choose_gallery() {
        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 20);

        } else {
            requestCameraPermission();
        }
    }


    @OnClick(R.id.choose_camera)
    void choose_camera() {
        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = getCustomImagePath(AddNewPlace.this, System.currentTimeMillis() + "");
            mCapturedImageUrl = file.getAbsolutePath();
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(takePicture, 30);

        } else {
            requestCameraPermission();
        }
    }



    @OnClick(R.id.Upload)
    void Upload() {

        if (TextUtils.isEmpty(suggest_address)) {
            Toast.makeText(this, "choose risk location", Toast.LENGTH_SHORT).show();
        } else if (file == null) {

            CommonUtilities.showStaticDialog(this, "");

            Map<String, String> parms = new HashMap<>();
            parms.put("lat", String.valueOf(lat));
            parms.put("lng", String.valueOf(lng));
            parms.put("address", suggest_address);
            parms.put("desc", suggest_desc.getText().toString());
            parms.put("user_id", dataManager.getID());
            myAPI.suggest_without_image(parms)
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

                                Toast.makeText(AddNewPlace.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                finish();


                            } else {
                                Toast.makeText(AddNewPlace.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(AddNewPlace.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                            Log.i("ERROR_RETROFIT", e.getMessage());
                            CommonUtilities.hideDialog();
                        }

                        @Override
                        public void onComplete() {


                        }
                    });

        } else {

            CommonUtilities.showStaticDialog(this, "");

            RequestBody suggest_lat = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(lat));
            RequestBody suggest_lng = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(lng));
            RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), suggest_address);
            RequestBody desc = RequestBody.create(MediaType.parse("multipart/form-data"), suggest_desc.getText().toString());
            RequestBody publisher_id = RequestBody.create(MediaType.parse("multipart/form-data"), dataManager.getID());

            MultipartBody.Part body_2 = MultipartBody.Part.createFormData("image", file.getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), file));


            myAPI.add_suggest(suggest_lat, suggest_lng, address, desc, publisher_id, body_2)
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

                                Toast.makeText(AddNewPlace.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                finish();


                            } else {
                                Toast.makeText(AddNewPlace.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(AddNewPlace.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                            Log.i("ERROR_RETROFIT", e.getMessage());
                            CommonUtilities.hideDialog();
                        }

                        @Override
                        public void onComplete() {


                        }
                    });
        }

    }

    @OnClick(R.id.suggest_location)
    void risk_location() {
        startActivityForResult(new Intent(AddNewPlace.this, SelectLocation.class), 1003);
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
        setContentView(R.layout.activity_add_new_place);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        init();
    }



    @Override
    protected void init() {
        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 1003:
                try {
                    suggest_address = data.getStringExtra("address");
                    lat = Double.parseDouble(data.getStringExtra("latitude"));
                    lng = Double.parseDouble(data.getStringExtra("longitude"));
                    tv_suggest.setText(suggest_address);

                } catch (Exception e) {

                }


                break;

            case 30:
                try {

                    if (resultCode == RESULT_OK) {
                        file = new File(mCapturedImageUrl);
                        suggest_imageView.setImageURI(Uri.fromFile(file));

                        image_button_layout.setVisibility(View.GONE);
                        image_layout.setVisibility(View.VISIBLE);

                    } else if (resultCode == RESULT_CANCELED) {

                    }


                } catch (Exception e) {

                }

                break;

            case 20:
                try {


                    suggest_imageView.setImageURI(data.getData());
                    file = new File(getPathFromUri(this, data.getData()));

                    image_button_layout.setVisibility(View.GONE);
                    image_layout.setVisibility(View.VISIBLE);

                } catch (Exception e) {

                }

                break;


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


    void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage("")

                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddNewPlace.this,
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
            ActivityCompat.requestPermissions(AddNewPlace.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 22);

        }
    }


}
