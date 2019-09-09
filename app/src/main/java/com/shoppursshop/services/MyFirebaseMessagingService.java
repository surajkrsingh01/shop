package com.shoppursshop.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shoppursshop.R;
import com.shoppursshop.activities.MainActivity;
import com.shoppursshop.utilities.AppController;
import com.shoppursshop.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ARIEON-7 on 30-06-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {



    private static final String TAG = "MyFirebaseMsgService";
    private String CHANNEL_ID = "shoppursChannel";
    private String message;
    private int notificationId = 1;

    /**

     * Called when message is received.

     *

     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.

     */

    // [START receive_message]

    @Override

    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());



        // Check if message contains a data payload.

        if (remoteMessage.getData().size() > 0) {

            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            message = remoteMessage.getData().toString();
            try {
                if(message.contains("=")){
                    JSONObject jsonObject = new JSONObject(message.split("=")[1]);
                    NotificationService.displayNotification(this,jsonObject.getString("message"));
                }else{
                    NotificationService.displayNotification(this,message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //sendNotification();

        }



        // Check if message contains a notification payload.

        if (remoteMessage.getNotification() != null) {
            message = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Notification data payload: " + message);
            Log.d(TAG, "Notification data data: " + remoteMessage.getData().toString());
            //sendNotification();

        }



    }

    // [END receive_message]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.MYPREFERENCEKEY,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.FCM_TOKEN,token);
        editor.putBoolean(Constants.IS_TOKEN_SAVED,false);
        editor.commit();
        sendRegistrationToServer(token);
    }


    /**

     * Create and show a simple notification containing the received FCM message.

     *

     *  messageBody FCM message body received.

     */

    private void sendOldNotification() {
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);

        Notification.Builder builder = null;
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            setupChannels(this,mNotificationManager);
            builder = new Notification.Builder(this, CHANNEL_ID);
        }
        else{
            builder = new Notification.Builder(this);
            //  Notification notification = builder.getNotification();
        }

        Uri sound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notification = builder.setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher).setSound(sound)
                .build();

        //  notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(1, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void setupChannels(Context context, NotificationManager notificationManager) {
        CharSequence channelName = context.getString(R.string.notifications_channel_name);
        String channelDescription = context.getString(R.string.notifications_channel_description);

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(channelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    private void sendNotification(){

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        createNotificationChannel();


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = getResources().getString(R.string.notifications_channel_name);
            String channelDescription = getResources().getString(R.string.notifications_channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendRegistrationToServer(String token){
        final SharedPreferences sharedPreferences = getSharedPreferences(Constants.MYPREFERENCEKEY,MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences.getBoolean(Constants.IS_LOGGED_IN,false)){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("userType","Seller");
                jsonObject.put("mobile",sharedPreferences.getString(Constants.MOBILE_NO,""));
                jsonObject.put("token",token);
                jsonObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
                jsonObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
                jsonObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("FCM","params "+jsonObject.toString());

            String url = getResources().getString(R.string.url)+"/api/user/save_fcm_token";

            Log.i("FCM","url "+url);

            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,url,jsonObject,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    AppController.getInstance().getRequestQueue().getCache().clear();
                    Log.i(TAG,response.toString());
                    try {
                        if(response.getBoolean("status")){
                           editor.putBoolean(Constants.IS_TOKEN_SAVED,true);
                           editor.commit();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    AppController.getInstance().getRequestQueue().getCache().clear();
                    Log.i(TAG,"Json Error "+error.toString());
                    // DialogAndToast.showDialog(getResources().getString(R.string.connection_error),BaseActivity.this);
                }
            }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer "+sharedPreferences.getString(Constants.TOKEN,""));
                    //params.put("VndUserDetail", appVersion+"#"+deviceName+"#"+osVersionName);
                    return params;
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        }
    }

}
