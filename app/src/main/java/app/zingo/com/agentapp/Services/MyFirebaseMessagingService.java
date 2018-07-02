package app.zingo.com.agentapp.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import app.zingo.com.agentapp.Activities.BiddingBookingActivity;
import app.zingo.com.agentapp.Activities.BiddingRequestReply;
import app.zingo.com.agentapp.Activities.NotificationActivity;
import app.zingo.com.agentapp.MainActivity;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.PreferenceHandler;


/**
 * Created by ZingoHotels Tech on 19-03-2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> map = remoteMessage.getData();

        sendPopNotification(notification.getTitle(), notification.getBody(), map);
       // sendNotification(notification.getTitle(), notification.getBody(), map);
       /* if(notification.getTitle().equalsIgnoreCase("New Booking from Zingo Hotels")){

            //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            sendPopNotification(notification.getTitle(), notification.getBody(), map);

        }else{
            //sendNotification(notification.getTitle(), notification.getBody(), map);
        }*/

      /*  if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }*/
    }


 /*   private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("body");
            String imageUrl = data.getString("image");

            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            //creating an intent for the notification
            Intent intent;

            if(title.equalsIgnoreCase("Room Bookings")){
                intent = new Intent(getApplicationContext(), PendingCheckOutActivities.class);
            }else{
                intent = new Intent(getApplicationContext(), FireBaseCheck.class);
            }
            //Intent intent = new Intent(getApplicationContext(), FireBaseCheck.class);

            //if there is no image
            if(imageUrl.equals("null")){
                //displaying small notification
                mNotificationManager.showSmallNotification(title, message, intent);
            }else{
                //if there is an image
                //displaying a big notification
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }*/


    private void sendPopNotification(String title, String body, Map<String, String> map) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Intent intent = null;
        String message;

        if(title.equalsIgnoreCase("Bidding Request Rejected")||title.equalsIgnoreCase("Bidding Request Accepted")){
            intent = new Intent(this, BiddingBookingActivity.class);
            message = "Your bidding Request details";
        }else if(title.equalsIgnoreCase("Bidding Request Reply")){
            intent = new Intent(this, BiddingRequestReply.class);
            message = "Your bidding Request Reply";
        }else{
            intent = new Intent(this, NotificationActivity.class);
            message = body;
        }


        intent.putExtra("Title",title);
        intent.putExtra("Message",body);
        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/good_morning");
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, m, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        int notifyID = 1;
        String CHANNEL_ID = ""+ PreferenceHandler.getInstance(MyFirebaseMessagingService.this).getUserId();// The id of the channel.
        CharSequence name = ""+ PreferenceHandler.getInstance(MyFirebaseMessagingService.this).getUserName();// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        }

        Notification.Builder notificationBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(this)
                    .setTicker(title).setWhen(0)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setFullScreenIntent(pendingIntent, true)
                    .setSound(sound)
                    .setContentIntent(pendingIntent)
                    .setContentInfo(title)
                    .setLargeIcon(icon)
                    .setChannelId("1")
                    .setPriority(Notification.PRIORITY_MAX)
                    // .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setSmallIcon(R.drawable.logo);
        }else{
            notificationBuilder = new Notification.Builder(this)
                    .setTicker(title).setWhen(0)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setFullScreenIntent(pendingIntent, true)
                    .setSound(sound)
                    .setContentIntent(pendingIntent)
                    .setContentInfo(title)
                    .setLargeIcon(icon)
                    .setPriority(Notification.PRIORITY_MAX)
                    // .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setSmallIcon(R.drawable.logo);
        }

        try {
            String picture_url = map.get("picture_url");
            //String picture_url = "https://travel.jumia.com/blog/wp-content/uploads/2016/09/Hotel-booking-iStock_000089313057_Medium-940x529-660x400.jpg";
            if (picture_url != null && !"".equals(picture_url)) {
                URL url = new URL(picture_url);
                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
               //notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(body));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.YELLOW, 1000, 300);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(m, notificationBuilder.build());
    }


  /*  private void sendNotification(String title, String body, Map<String, String> map) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Intent intent = null;
        String message="";

        if(title.equalsIgnoreCase("Room Rejected")||title.equalsIgnoreCase("Room Upgrade Suggession Rejected")){

           message = "Sorry! Your suggested room rejected by user";
            intent = new Intent(this, RoomRequestAcceptanceActivity.class);

        }else if(title.equalsIgnoreCase("Room Request")||title.equalsIgnoreCase("Select Room Upgrade Request")){
            message = "You got one new room request";
            intent = new Intent(this, RoomRequestAcceptanceActivity.class);

        }else if(title.equalsIgnoreCase("Change Room Request")||title.equalsIgnoreCase("Room Upgrade Suggestion Accepted")){
            message = "You got one new Change Room request";
            intent = new Intent(this, ChangeRoomRequestActivity.class);
        }else if(title.equalsIgnoreCase("Checkout Request")){
            message = "You got one new Check-out request";
            intent = new Intent(this, CheckoutRequestActivity.class);
        }else if(title.equalsIgnoreCase("Room Service Request")){
            message = "You got one new Room Service request";
            intent = new Intent(this, PendingServicesActivity.class);
        }



        intent.putExtra("Title",title);
        intent.putExtra("Message",body);
        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/good_morning");
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, m, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setTicker(title).setWhen(0)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pendingIntent)
                .setContentInfo(title)
                .setLargeIcon(icon)
                .setPriority(Notification.PRIORITY_MAX)
                // .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher);

        try {
            String picture_url = map.get("picture_url");
            //String picture_url = "https://travel.jumia.com/blog/wp-content/uploads/2016/09/Hotel-booking-iStock_000089313057_Medium-940x529-660x400.jpg";
            if (picture_url != null && !"".equals(picture_url)) {
                URL url = new URL(picture_url);
                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(body));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.YELLOW, 1000, 300);

        Random random = new Random();
        //int m = random.nextInt(9999 - 1000) + 1000;



        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(m, notificationBuilder.build());
    }*/

    @Override
    public void handleIntent(Intent intent) {
        try
        {
            if (intent.getExtras() != null)
            {
                RemoteMessage.Builder builder = new RemoteMessage.Builder("MyFirebaseMessagingService");

                for (String key : intent.getExtras().keySet())
                {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }

                onMessageReceived(builder.build());
            }
            else
            {
                super.handleIntent(intent);
            }
        }
        catch (Exception e)
        {
            super.handleIntent(intent);
        }
    }
}