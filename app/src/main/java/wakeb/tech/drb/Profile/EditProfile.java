package wakeb.tech.drb.Profile;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import com.google.android.material.textfield.TextInputEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hbb20.CountryCodePicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
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
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.Uitils.ImagesBottomSheet;
import wakeb.tech.drb.Uitils.ScalingUtilities;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

import static wakeb.tech.drb.Uitils.CommonUtilities.getCustomImagePath;

public class EditProfile extends BaseActivity {
    DataManager dataManager;
    ApiServices myAPI;
    Retrofit retrofit;

    @OnClick(R.id.back_button)
    void back_button() {
        finish();
    }

    String mCapturedImageUrl;

    @BindView(R.id.Edit_image)
    CircleImageView Edit_image;


    File file;


    @OnClick(R.id.Edit_button)
    void Edit_button() {
        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


            ImagesBottomSheet addPhotoBottomDialogFragment =
                    new ImagesBottomSheet(new ImagesBottomSheet.DialogListener() {
                        @Override
                        public void camera() {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File file = getCustomImagePath(EditProfile.this, System.currentTimeMillis() + "");
                            mCapturedImageUrl = file.getAbsolutePath();
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                            startActivityForResult(takePicture, 30);
                        }

                        @Override
                        public void gallery() {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 20);
                        }
                    });
            addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                    "add_photo_dialog_fragment");


        } else {
            requestCameraPermission();
        }
    }


    @BindView(R.id.Edit_userName)
    TextView Edit_userName;


    @BindView(R.id.Edit_displayName)
    TextInputEditText Edit_displayName;


    @BindView(R.id.Edit_mobileNumber)
    TextInputEditText Edit_mobileNumber;


    @BindView(R.id.Edit_emailAddress)
    TextInputEditText Edit_emailAddress;

    @BindView(R.id.Edit_Bio)
    TextInputEditText Edit_Bio;


    @OnClick(R.id.Edit)
    void Edit() {
        update_data();
    }

    @BindView(R.id.Edit_countryCode)
    CountryCodePicker Edit_countryCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        }
        else
        {
            Window window = getWindow();
            window.setBackgroundDrawableResource(R.drawable.background_png);
        }

        setContentView(R.layout.activity_edit_profile);

        init();

        get_user();
    }



    @Override
    protected void init() {
        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
    }




    void get_user() {

        CommonUtilities.showStaticDialog(this , "get_user");
        Map<String, String> parms = new HashMap<>();
        parms.put("user_id", dataManager.getID());
        myAPI.getUser(parms)
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


                            Glide.with(getApplicationContext())
                                    .load(apiResponse.getData().getUser().getImage())
                                    .apply(new RequestOptions()
                                            .placeholder(Edit_image.getDrawable())
                                    )
                                    .into(Edit_image);

                            Edit_userName.setText(apiResponse.getData().getUser().getUsername());
                            Edit_displayName.setText(apiResponse.getData().getUser().getDisplayName());
                            Edit_emailAddress.setText(apiResponse.getData().getUser().getEmail());
                            Edit_mobileNumber.setText(apiResponse.getData().getUser().getMobile());
                            Edit_Bio.setText(apiResponse.getData().getUser().getBio());
                            Edit_countryCode.setCountryForPhoneCode(apiResponse.getData().getUser().getCity());


                        } else {
                            Toast.makeText(EditProfile.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(EditProfile.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Log.i("ERROR", e.getMessage());
                        CommonUtilities.hideDialog();

                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }


    void update_data() {


        CommonUtilities.showStaticDialog(this ,"editProfile");
        Map<String, String> parms = new HashMap<>();
        parms.put("user_id", dataManager.getID());
        parms.put("username", Edit_userName.getText().toString());
        parms.put("display_name", Edit_displayName.getText().toString());
        parms.put("mobile", Edit_mobileNumber.getText().toString());
        parms.put("email", Edit_emailAddress.getText().toString());
        parms.put("bio", Edit_Bio.getText().toString());
        parms.put("city", Edit_countryCode.getSelectedCountryCode());
        myAPI.update_profile(parms)
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

get_user();

                        } else {
                            Toast.makeText(EditProfile.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(EditProfile.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Log.i("ERROR", e.getMessage());
                        CommonUtilities.hideDialog();

                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }


    void update_image() {


        CommonUtilities.showStaticDialog(this, "update_image");
        Map<String, String> parms = new HashMap<>();
        parms.put("user_id", dataManager.getID());
        myAPI.update_profile(parms)
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


                            Glide.with(getApplicationContext())
                                    .load(apiResponse.getData().getUser().getImage())
                                    .apply(new RequestOptions()
                                            .placeholder(Edit_image.getDrawable())
                                    )
                                    .into(Edit_image);

                            Edit_userName.setText(apiResponse.getData().getUser().getUsername());
                            Edit_displayName.setText(apiResponse.getData().getUser().getDisplayName());
                            Edit_emailAddress.setText(apiResponse.getData().getUser().getEmail());
                            Edit_mobileNumber.setText(apiResponse.getData().getUser().getMobile());
                            Edit_Bio.setText(apiResponse.getData().getUser().getBio());
                            Edit_countryCode.setCountryForPhoneCode(+20);


                        } else {
                            Toast.makeText(EditProfile.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(EditProfile.this, dataManager.getID(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(EditProfile.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Log.i("ERROR", e.getMessage());
                        CommonUtilities.hideDialog();

                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }

    void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage("")

                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(EditProfile.this,
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
            ActivityCompat.requestPermissions(EditProfile.this,
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


                ImagesBottomSheet addPhotoBottomDialogFragment =
                        new ImagesBottomSheet(new ImagesBottomSheet.DialogListener() {
                            @Override
                            public void camera() {
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                File file = getCustomImagePath(EditProfile.this, System.currentTimeMillis() + "");
                                mCapturedImageUrl = file.getAbsolutePath();
                                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                startActivityForResult(takePicture, 30);
                            }

                            @Override
                            public void gallery() {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 20);
                            }
                        });
                addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                        "add_photo_dialog_fragment");

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == 30) {


                file = new File(decodeFile(mCapturedImageUrl, 600, 600));
                Edit_image.setImageURI(Uri.fromFile(file));

                CommonUtilities.showStaticDialog(this , "upload");


                MultipartBody.Part body_2 = MultipartBody.Part.createFormData("image", file.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), file));
                RequestBody trip_id = RequestBody.create(MediaType.parse("multipart/form-data"), dataManager.getID());
                RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), Edit_userName.getText().toString());
                RequestBody display_name = RequestBody.create(MediaType.parse("multipart/form-data"), Edit_displayName.getText().toString());
                RequestBody mobile = RequestBody.create(MediaType.parse("multipart/form-data"), Edit_mobileNumber.getText().toString());
                RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), Edit_emailAddress.getText().toString());
                RequestBody bio = RequestBody.create(MediaType.parse("multipart/form-data"), Edit_Bio.getText().toString());
                RequestBody city = RequestBody.create(MediaType.parse("multipart/form-data"), Edit_countryCode.getSelectedCountryCode());


                myAPI.update_image(trip_id, username, display_name, mobile, email, bio, city, body_2)
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

                                    Toast.makeText(EditProfile.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                    finish();


                                } else {
                                    Toast.makeText(EditProfile.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(EditProfile.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                CommonUtilities.hideDialog();
                            }

                            @Override
                            public void onComplete() {


                            }
                        });


            } else if (requestCode == 20) {


                file = new File(decodeFile(getPathFromUri(this, data.getData()), 600, 600));
                Edit_image.setImageURI(data.getData());

                CommonUtilities.showStaticDialog(this, "upload");


                MultipartBody.Part body_2 = MultipartBody.Part.createFormData("image", file.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), file));
                RequestBody trip_id = RequestBody.create(MediaType.parse("multipart/form-data"), dataManager.getID());
                RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), Edit_userName.getText().toString());
                RequestBody display_name = RequestBody.create(MediaType.parse("multipart/form-data"), Edit_displayName.getText().toString());
                RequestBody mobile = RequestBody.create(MediaType.parse("multipart/form-data"), Edit_mobileNumber.getText().toString());
                RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), Edit_emailAddress.getText().toString());
                RequestBody bio = RequestBody.create(MediaType.parse("multipart/form-data"), Edit_Bio.getText().toString());
                RequestBody city = RequestBody.create(MediaType.parse("multipart/form-data"), Edit_countryCode.getSelectedCountryCode());


                myAPI.update_image(trip_id, username, display_name, mobile, email, bio, city, body_2)
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

                                    Toast.makeText(EditProfile.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                    finish();


                                } else {
                                    Toast.makeText(EditProfile.this, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(EditProfile.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                CommonUtilities.hideDialog();
                            }

                            @Override
                            public void onComplete() {


                            }
                        });

            }
        }
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
