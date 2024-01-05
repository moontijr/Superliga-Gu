package com.example.myapplication;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.model.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerListAdapter extends ArrayAdapter<Player> {

    private List<Player> playerList;
    private SparseBooleanArray selectedPlayers;

    public PlayerListAdapter(@NonNull Context context, int resource, @NonNull List<Player> players) {
        super(context, resource, players);
        this.playerList = players;
        this.selectedPlayers = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.player_list_item, parent, false);
        }

        // Get the current player
        Player currentPlayer = getItem(position);

        // Set player name
        TextView playerNameTextView = convertView.findViewById(R.id.playerNameTextView);
        playerNameTextView.setText(currentPlayer != null ? currentPlayer.toString() : "");

        // Set up checkbox
        CheckBox playerCheckBox = convertView.findViewById(R.id.playerCheckBox);
        playerCheckBox.setChecked(selectedPlayers.get(position, false));
        playerCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedPlayers.put(position, isChecked);
        });

        return convertView;
    }

    // Get the list of selected players
    public List<Player> getSelectedPlayers() {
        List<Player> selectedPlayersList = new ArrayList<>();
        for (int i = 0; i < playerList.size(); i++) {
            if (selectedPlayers.get(i, false)) {
                selectedPlayersList.add(playerList.get(i));
            }
        }
        return selectedPlayersList;
    }
}
