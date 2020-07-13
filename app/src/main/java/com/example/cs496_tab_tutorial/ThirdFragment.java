package com.example.cs496_tab_tutorial;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;

import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.nio.charset.Charset;

public class ThirdFragment extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment3, null);

        Button OnOffButton = (Button) view.findViewById(R.id.OnOffButton);
        Button NotifButton = (Button) view.findViewById(R.id.NotifButton);

        createNotificationChannel();

        OnOffButton.setOnClickListener(new Button.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view){
                boolean onoff;
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                onoff = sharedPref.getBoolean("onoff", true);
//                System.out.println("Before: " + Boolean.toString(onoff));

                if(!onoff){
                    Toast.makeText(getActivity(), "turned on", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(getActivity().getApplicationContext(), NotifService.class);
//                     intent.putExtra("","");
//                    getActivity().startService(intent);

                }
                else{
                    Toast.makeText(getActivity(), "turned off", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getActivity().getApplicationContext(), NotifService.class);
//                    getActivity().stopService(intent);
                }
                SharedPreferences.Editor editor = sharedPref.edit();
                onoff = !onoff;
                editor.putBoolean("onoff", onoff);
                editor.commit();

                setAlarm();
//                System.out.println("After: " + Boolean.toString(onoff));
            }


        });

        NotifButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                TimePickerDialog timedialog = new TimePickerDialog(
                        getActivity(),
                        timelistener,
                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                        Calendar.getInstance().get(Calendar.MINUTE),
                        false
                );
                timedialog.show();
                DatePickerDialog datedialog = new DatePickerDialog(
                        getActivity(),
                        datelistener,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
                datedialog.show();

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                boolean onoff = sharedPref.getBoolean("onoff", true);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("onoff", true);
                editor.commit();
            }
        });
        
        return view;
    }
}

    private TimePickerDialog.OnTimeSetListener timelistener = new TimePickerDialog.OnTimeSetListener() {
        int hour, min;
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int min) {
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("hour", hour);
            editor.putInt("min", min);
            editor.commit();

            setAlarm();
        }
    };

    private DatePickerDialog.OnDateSetListener datelistener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day){
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("year", year);
            editor.putInt("month", month);
            editor.putInt("day", day);
            editor.commit();
        }
    };

// 이와 같이 모든 경우에 서비스로부터 받은 인텐트가 처리 될 수 있도록한다.

    private void processCommand(Intent intent) {
        if (intent != null) {
            String command = intent.getStringExtra("command");
            String name = intent.getStringExtra("name");

            //Toast.makeText(this, "서비스로부터 전달받은 데이터: " + command + ", " + name, Toast.LENGTH_LONG).show();
        }
    }

    public class AlarmHATT {
        private Context context;

        public AlarmHATT(Context context) {
            this.context=context;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void Alarm() {
            AlarmManager am = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getActivity(), BroadcastD.class);

            PendingIntent sender = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            //알람시간 calendar에 set해주기

            int year, month, day, hour, min;
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            year = sharedPref.getInt("year", calendar.get(Calendar.YEAR));
            month = sharedPref.getInt("month", calendar.get(Calendar.MONTH));
            day = sharedPref.getInt("day", calendar.get(Calendar.DAY_OF_MONTH));
            hour = sharedPref.getInt("hour", calendar.get(Calendar.HOUR_OF_DAY));
            min = sharedPref.getInt("min", calendar.get(Calendar.MINUTE));

            System.out.println("\n\n"+Integer.toString(year) + Integer.toString(month) + Integer.toString(day) + Integer.toString(hour) + Integer.toString(min));
            calendar.set(year, month, day, hour, min, 0);

            //알람 예약
            long diff = calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
            System.out.println("time diff: " + diff);
            if(diff > 0)
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String CHANNEL_ID = getString(R.string.channel_id);
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAlarm() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean onoff = sharedPref.getBoolean("onoff", true);
        if (onoff) {
            new AlarmHATT(getActivity()).Alarm();
//                }
        }
    }
}
