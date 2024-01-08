package com.example.myapplication;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.ValueEventListener;

public class ToastUtils {
    public static void showToast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
