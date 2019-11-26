package wakeb.tech.drb.Risks;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import wakeb.tech.drb.Activities.NewResource;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Home.SelectLocation;
import wakeb.tech.drb.R;
import wakeb.tech.drb.SuggestionPlaces.AddNewPlace;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.Uitils.ImagesBottomSheet;
import wakeb.tech.drb.Uitils.ScalingUtilities;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

import static wakeb.tech.drb.Uitils.CommonUtilities.getCustomImagePath;

public class AddNewRisk extends BaseActivity implements RiskTypes_Interface {

    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;

    String mCapturedImageUrl;
    String risk_address, risk_id;
    double lat, lng;
    File file;
    RiskTypesDialog cdd;
    @OnClick(R.id.back_button)
    void back_button() {
        finish();
    }

    @BindView(R.id.tv_startTrip)
    TextView tv_startTrip;


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

    @BindView(R.id.Risks_imageView)
    ImageView Risks_imageView;

    @BindView(R.id.Risk_type)
    TextView Risk_type;

    @BindView(R.id.risk_desc)
    EditText risk_desc;



    @OnClick(R.id.setRiskType)
    void setRiskType() {
        CommonUtilities.showStaticDialog(this, "serRisk");
        myAPI.get_riskTypes()
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


                            cdd = new RiskTypesDialog((Activity) AddNewRisk.this, apiResponse.getData().getRiskTypes(), dataManager);
                            cdd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


                            Window window = cdd.getWindow();
                            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                            window.setGravity(Gravity.CENTER);


                            WindowManager.LayoutParams lp = window.getAttributes();
                            lp.dimAmount = 0.7f;
                            lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                            cdd.getWindow().setAttributes(lp);

                            cdd.show();

                        } else {

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AddNewRisk.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
                        CommonUtilities.hideDialog();
                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }

    @OnClick(R.id.Upload)
    void Upload() {

        if (TextUtils.isEmpty(risk_address)) {
            Toast.makeText(this, getString(R.string.choose_risk_location), Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(risk_id)) {
            Toast.makeText(this, getString(R.string.choose_risk_type), Toast.LENGTH_SHORT).show();
        } else if (file == null) {

            CommonUtilities.showStaticDialog(this, "");
            Map<String, String> parms = new HashMap<>();
            parms.put("lat", String.valueOf(lat));
            parms.put("lng", String.valueOf(lng));
            parms.put("address", risk_address);
            parms.put("desc", risk_desc.getText().toString());
            parms.put("risk_type_id", risk_id);
            parms.put("publisher_id", dataManager.getID());

            myAPI.add_risks_without_image(parms)
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

                                Toast.makeText(AddNewRisk.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                finish();


                            } else {
                                Toast.makeText(AddNewRisk.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(AddNewRisk.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                            Log.i("ERROR_RETROFIR", e.getMessage());
                            CommonUtilities.hideDialog();
                        }

                        @Override
                        public void onComplete() {


                        }
                    });

        } else {

            CommonUtilities.showStaticDialog(this, "");

            RequestBody risk_lat = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(lat));
            RequestBody risk_lng = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(lng));
            RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), risk_address);
            RequestBody desc = RequestBody.create(MediaType.parse("multipart/form-data"), risk_desc.getText().toString());
            RequestBody risk_type_id = RequestBody.create(MediaType.parse("multipart/form-data"), risk_id);
            RequestBody publisher_id = RequestBody.create(MediaType.parse("multipart/form-data"), dataManager.getID());

            MultipartBody.Part body_2 = MultipartBody.Part.createFormData("image", file.getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), file));


            myAPI.add_risks(risk_lat, risk_lng, address, desc, risk_type_id, publisher_id, body_2)
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

                                Toast.makeText(AddNewRisk.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                finish();


                            } else {
                                Toast.makeText(AddNewRisk.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(AddNewRisk.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                            Log.i("ERROR_RETROFIR", e.getMessage());
                            CommonUtilities.hideDialog();
                        }

                        @Override
                        public void onComplete() {


                        }
                    });
        }

    }

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
            File file = getCustomImagePath(AddNewRisk.this, System.currentTimeMillis() + "");
            mCapturedImageUrl = file.getAbsolutePath();
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(takePicture, 30);

        } else {
            requestCameraPermission();
        }
    }


    @OnClick(R.id.risk_location)
    void risk_location() {
        startActivityForResult(new Intent(AddNewRisk.this, SelectLocation.class), 1003);
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
        setContentView(R.layout.activity_add_new_risk);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        init();
        startActivityForResult(new Intent(AddNewRisk.this, SelectLocation.class), 1003);
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
                    risk_address = data.getStringExtra("address");
                    lat = Double.parseDouble(data.getStringExtra("latitude"));
                    lng = Double.parseDouble(data.getStringExtra("longitude"));
                    tv_startTrip.setText(risk_address);



                } catch (Exception e) {

                }

                break;

            case 30:
                try {


                    file = new File(decodeFile(mCapturedImageUrl, 600, 600));
                    Risks_imageView.setImageURI(Uri.fromFile(file));

                    image_button_layout.setVisibility(View.GONE);
                    image_layout.setVisibility(View.VISIBLE);

                } catch (Exception e) {

                }

                break;

            case 20:
                try {


                    file = new File(decodeFile(getPathFromUri(this, data.getData()), 600, 600));
                    Risks_imageView.setImageURI(Uri.fromFile(file));


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
                            ActivityCompat.requestPermissions(AddNewRisk.this,
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
            ActivityCompat.requestPermissions(AddNewRisk.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 22);

        }
    }

    @Override
    public void setRiskType(String riskName, String riskID) {
        risk_id = riskID;
        Risk_type.setText(riskName);

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

}
