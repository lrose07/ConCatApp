package com.lrose07.concat;

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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mLeave = findViewById(R.id.leave);
        mShare = findViewById(R.id.share);

        Bundle extras = getIntent().getExtras();
            str = extras.getString("currentEvent");


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
                    String shareBody = "Join my concat meet up! The passcode is " + str;
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
        );





    }

}
