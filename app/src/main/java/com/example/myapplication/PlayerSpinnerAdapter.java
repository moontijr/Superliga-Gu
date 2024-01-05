package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.model.Player;

import java.util.List;

public class PlayerSpinnerAdapter extends ArrayAdapter<Player> {

    public PlayerSpinnerAdapter(Context context, List<Player> playerList) {
        super(context, android.R.layout.simple_spinner_item, playerList);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setTextColor(Color.BLACK); // Set the default text color
        return textView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        textView.setTextColor(Color.BLACK); // Set the text color for each item in the dropdown
        return textView;
    }
}
