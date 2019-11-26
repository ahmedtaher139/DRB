package wakeb.tech.drb.Uitils;

/**
 * Created by Develop on 5/15/2018.
 */

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.ui.home.HomeActivity;
import wakeb.tech.drb.Profile.MyProfile;
import wakeb.tech.drb.R;
import wakeb.tech.drb.data.DataManager;


public class MyFCMService extends FirebaseMessagingService {

    DataManager dataManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        dataManager = ((MainApplication) getApplication()).getDataManager();


        if (dataManager.getTurnOnNotifications()) {
            if (dataManager.getID() != null) {

                Notification(remoteMessage.getData().get("title"), remoteMessage.getData().get("msg"));

            }
        }


    }


    private void Notification_Profile(String title, String messageBody, String id) {
        Intent intent = new Intent(this, MyProfile.class);
        showNotification(getApplicationContext(), title, messageBody, intent);

    }


    private void Notification(String title, String messageBody) {
        Intent intent = new Intent(this, HomeActivity.class);
        showNotification(getApplicationContext(), title, messageBody, intent);
    }


    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body))
                .setContentText(body)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }


}
