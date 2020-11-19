package com.example.dailyshoping;

import android.content.Context;
import android.net.ConnectivityManager;

import java.text.DateFormat;
import java.util.Date;

public class UtilitiesClass {
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public  String getFormatedDate()
    {
        String date= DateFormat.getDateInstance().format(new Date());
        return  date;
    }

}
