package com.lrose07.concat;
/**
 * @authors Elizabeth Jolly and Lauren Rose
 * @version 2020-02-23
 * Controls the setting activity Intent that is called by the Main Activity
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.content.Intent;

public class Settings extends AppCompatActivity {

    private Button mLeave;
    private Button mShare;
    private  String str;

    /**
     * Creates the setting display which allows the user to eddit the settings of leaving a room
     * and sharing a room
     * and
     * @param savedInstanceState Bundle created by the previous intent
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mLeave = findViewById(R.id.leave);
        mShare = findViewById(R.id.share);

        Bundle extras = getIntent().getExtras();
            str = extras.getString("currentEvent");
        //leaving room
        mLeave.setOnClickListener(e ->
                {
                    Intent myIntent = new Intent(this, MainActivity.class);
                    startActivity(myIntent);
                }
        );
//sharing
        mShare.setOnClickListener(e ->
                {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Join my concat meet up! The passcode is " + str;
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
        );
    }

}
