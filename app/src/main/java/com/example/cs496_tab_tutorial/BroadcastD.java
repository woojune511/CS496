package com.example.cs496_tab_tutorial;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.LOCATION_SERVICE;

public class BroadcastD extends BroadcastReceiver {
    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;


    static final int REQUEST = 0;


    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds

    public double lontitube, latitude;

    protected LocationManager locationManager;
    protected Location currentLocation;

    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {

            String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            //Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }

        public void onStatusChanged(String s, int i, Bundle b) {
//            Toast.makeText(getActivity(), "Provider status changed",
//                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
//            Toast.makeText(getActivity(),
//                    "Provider disabled by the user. GPS turned off",
//                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
//            Toast.makeText(getActivity(),
//                    "Provider enabled by the user. GPS turned on",
//                    Toast.LENGTH_LONG).show();
        }

    }


    public void NotificationUmbrella(Context context, Intent intent) {
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.channel_id));
        builder.setSmallIcon(R.drawable.notificon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.umbrella))
                .setTicker("HETT")
                .setWhen(System.currentTimeMillis())
                .setNumber(1)
                .setContentTitle("우산 챙기세요!")
                .setContentText("오늘 비가 올지도 몰라요..")
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        notificationmanager.notify(1, builder.build());

    }
    @Override
    public void onReceive(final Context context, final Intent intent) {


        //알람 시간이 되었을때 onReceive를 호출함
        //NotificationManager 안드로이드 상태바에 메세지를 던지기위한 서비스 불러오고
        System.out.println("recieve!!!");
        locationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (currentLocation != null) {
            String message = String.format(
                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
                    currentLocation.getLongitude(), currentLocation.getLatitude()

            );
            lontitube=currentLocation.getLongitude();
            latitude=currentLocation.getLatitude();

            System.out.println("----lati----longi----"+latitude+"\n"+lontitube);

        }

        new Thread(){
            // send http request
            @Override
            public void run() {
                try {

                    String inputUrl = "http://api.openweathermap.org/data/2.5/weather?lat=" + Double.toString(latitude) + "&lon=" + Double.toString(lontitube)+ "&lang=kr&APPID=6114a7251f00c2d006a592fa6bb4bb54";
                    Log.d("gps", inputUrl);
                    URL url = new URL(inputUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET"); //method
                    connection.setDoOutput(true);      // setting to write data
                    connection.setDoInput(true);        // setting to read data

                    InputStream is = connection.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                    String result;
                    while((result = br.readLine())!=null){
                        sb.append(result+"\n");
                    }

                    result = sb.toString();
                    // load weather information
                    JSONObject resultJson = new JSONObject(result);
                    String weather = resultJson.getString("weather");
                    JSONArray weatherArray = new JSONArray(weather);
                    int weatherId = weatherArray.getJSONObject(0).getInt("id");
                    String weatherMain = weatherArray.getJSONObject(0).getString("main");
                    // get weather main description
                    System.out.println("main weather: " + weatherMain);
                    // get weather id
                    System.out.println("weather id : " + weatherId);
                    if((weatherId == 804) || (weatherId == 805) || (weatherMain.equals("Rain")) || (weatherMain.equals("Snow"))) {
                        NotificationUmbrella(context, intent);
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();



    }
}
