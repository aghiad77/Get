package com.example.aghiad_pc.get.Behaviour;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import com.example.aghiad_pc.get.Model.Database;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.UI.HomeActivity;
import com.example.aghiad_pc.get.Utilities.GetStrings;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Date;
import androidx.core.app.NotificationCompat;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    Database db;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        try {
            //Toast.makeText(getBaseContext(),"I am called firebase",Toast.LENGTH_LONG).show();
            if (remoteMessage.getNotification() != null) {
                // Since the notification is received directly from
                // FCM, the title and the body can be fetched
                // directly as below.
                //db=new Database(getBaseContext());
                //db.addNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),new Date().toString());
                //showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
                //startServices(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
                sendNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle(),new Date().toString());
                //db.close();
            }
        }catch (Exception e){ }

    }


    private void sendNotification(String messageBody, String messageTitle,String date) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("title", messageTitle);
        intent.putExtra("body", messageBody);
        intent.putExtra("date", date);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        GetStrings.notificationMsg=true;
        String channelId = "1";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.raw.g)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}