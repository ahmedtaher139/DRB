package wakeb.tech.drb.Uitils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;

import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wakeb.tech.drb.Activities.ChangePass;
import wakeb.tech.drb.R;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;

public class CommonUtilities {
    public static KProgressHUD blg;

    public static void hideDialog() {
        blg.dismiss();
    }

    public static boolean hasNavBar (Resources resources)
    {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }

    public static void showStaticDialog(Activity currentActivity , String lable) {


        blg = KProgressHUD.create(currentActivity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

    }


    public static String get_Date(String myDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
        Date date = null;
        try {
            date = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return DateUtils.getRelativeTimeSpanString(date.getTime(),
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS).toString();


    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public static String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;

    }

    public static File getCustomImagePath(Context context, String fileName) {

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {

            String filePath = Environment.getExternalStorageDirectory().getPath();

            File myDir = new File(filePath, "Healthy");
            myDir.mkdirs();

            String fname = null;

            if (fileName != null)
                fname = fileName + ".png";
            else
                fname = Calendar.getInstance().getTimeInMillis() + ".png";

            File file = new File(myDir, fname);

            if (file.exists()) file.delete();

            //return (file.getAbsolutePath());
            return file;
        } else {
            Toast.makeText(context, "Sd Card is not mounted", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    public static long get_Millis(String myDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
        Date date = null;
        try {
            date = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return date.getTime();


    }

    public static void save_log(ApiServices myAPI, String user_id, String type, String status, String action_id) {

        Map<String, String> parms = new HashMap<>();
        parms.put("user_id", user_id);
        parms.put("type", type);
        parms.put("status", status);
        parms.put("action_id", action_id);
        myAPI.log_action(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }

    public static void get_maps_directions(Context context, String sourceLatitude, String sourceLongitude, String destinationLatitude, String destinationLongitude) {
      /*  String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)", sourceLatitude, sourceLongitude, "Start Trip", destinationLatitude, destinationLongitude, "End Trip");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

*/
        try {
            String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + sourceLatitude + "," + sourceLongitude + "&daddr=" + destinationLatitude + "," + destinationLongitude;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            context.startActivity(intent);
        } catch (Exception e) {
        }

    }

    public static void shareImageOnSocialMedia(Context context, String imagePath, String userEmail) {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        Uri imageUri = FileProvider.getUriForFile(
                context,
                "wakeb.tech.drb.provider", //(use your app signature + ".provider" )
                new File(imagePath));

        share.putExtra(Intent.EXTRA_STREAM, imageUri);
        share.putExtra(Intent.EXTRA_TEXT, "This image shared with you from DRB, Download the mobile app now");
        if (share.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(share, "Share Trip"));
        } else {

        }
    }
}
