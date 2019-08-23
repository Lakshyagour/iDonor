package com.example.idonor2;

import android.content.Context;
import android.widget.Toast;

public class MessageClass {
    public static void message(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
