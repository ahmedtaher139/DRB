package wakeb.tech.drb.ui.addNewSpot;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import wakeb.tech.drb.Activities.NewResource;
import wakeb.tech.drb.R;
import wakeb.tech.drb.data.Retrofit.ApiResponse;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UploadNewSpot extends IntentService {

    public UploadNewSpot() {
        super("UploadNewSpot");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            int notificationId = 1;
            String channelId = "channel-01";
            String channelName = "Channel Name";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Uploading New Spot")
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addNextIntent(intent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setOnlyAlertOnce(true);
             mBuilder.setOngoing(true);
            notificationManager.notify(notificationId, mBuilder.build());


            Log.i("Uploading ... =", "Start");

            ArrayList<File> files = (ArrayList<File>) intent.getSerializableExtra("files");

            AndroidNetworking.upload("http://3.17.76.229/api/trip/create-spot")
                    .addMultipartFileList("files[]", files)
                    .addMultipartParameter("publisher_id",intent.getStringExtra("publisher_id"))
                    .addMultipartParameter("location", intent.getStringExtra("location"))
                    .addMultipartParameter("desc", intent.getStringExtra("desc"))
                    .addMultipartParameter("lat", intent.getStringExtra("lat"))
                    .addMultipartParameter("lng", intent.getStringExtra("lng"))
                    .addMultipartParameter("journal_id", intent.getStringExtra("journal_id"))
                    .setPriority(Priority.HIGH)
                    .build()
                    .setUploadProgressListener(new UploadProgressListener() {
                        @Override
                        public void onProgress(long bytesUploaded, long totalBytes) {

                            float f = ((float) bytesUploaded / (float) totalBytes) * 100;
                            int test = Math.round(f);
                            //    blg.setLabel(String.valueOf(test) + "%");

                            Log.i("Uploading ... =", String.valueOf(test) + "%");
                            mBuilder.setProgress(100, test, false);
                            mBuilder.setContentText(String.valueOf(test) + "%");

                            notificationManager.notify(notificationId, mBuilder.build());


                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Gson gson = new Gson();
                            ApiResponse apiResponse = gson.fromJson(response.toString(), ApiResponse.class);

                            if (apiResponse.getStatus()) {

                            } else {
                            }
                            Log.i("Uploading ... =", "Successful");
                            mBuilder.setContentText("Download complete")
                                    .setProgress(0, 0, false);
                            mBuilder.setOngoing(false);
                            notificationManager.notify(notificationId, mBuilder.build());


                        }

                        @Override
                        public void onError(ANError error) {

                            Log.i("Uploading ... =", "ANError" + error.getErrorBody());


                        }
                    });


        }
    }




}
