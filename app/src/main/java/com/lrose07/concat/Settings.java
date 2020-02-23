package com.lrose07.concat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    private Button mLeave;
    private Button mShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mLeave = findViewById(R.id.leave);
        mShare = findViewById(R.id.share);


        mLeave.setOnClickListener(e ->
                {
                    Intent myIntent = new Intent(this, MainActivity.class);
                    startActivity(myIntent);
                }
        );

        mShare.setOnClickListener(e ->
                {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Join my concat meet up! The passcode is ";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
        );





    }

}
