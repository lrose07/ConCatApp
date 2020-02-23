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
    private Switch mTextFet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mLeave = findViewById(R.id.leave);
        mShare = findViewById(R.id.share);
        mTextFet = findViewById(R.id.textFet);

        mLeave.setOnClickListener(e ->
                {
                    Intent myIntent = new Intent(this, MainActivity.class);
                    startActivity(myIntent);
                }
        );

        mShare.setOnClickListener(e ->
                {
                    // null; //implement later need to have it share to social media probably pop up
                }
        );





    }

}
