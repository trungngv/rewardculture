package com.rewardculture.misc;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Utils {

    public static void showToastAndLog(Context context, String msg, String tag) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        Log.d(tag, msg);
    }
}
