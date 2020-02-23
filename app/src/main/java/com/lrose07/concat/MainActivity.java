package com.lrose07.concat;

/**
 * @author Lauren Rose, Elizabeth Jolly
 * @version 23 Feb 2020
 *
 * The main activity for ConCat Chat app.
 *
 * This class handles main functionality of the app, including creation of new event rooms,
 * joining existing event rooms, and sending and seeing messages.
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private MessageAdapter mMessageAdapter;
    private EditText mMessageEditText;
    private Button mSendButton;
    private boolean roomSuccessful = false;

    private TableLayout overlay;
    private Button newEventButton;
    private EditText eventName;
    private EditText eventCode;
    private Button createEvent;
    private Button settings;

    private boolean uniqueFlag = true;

    private DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference mEventsDatabaseReference;
    private ChildEventListener mChildEventListener;

    private ConCatEvent currentEvent;

    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Triggers when activity is created when app launches
     * @param savedInstanceState save state data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        mEventsDatabaseReference = mFirebaseDatabase.getReference().child("events");

        ListView mMessageListView = findViewById(R.id.messageListView);
        mMessageEditText = findViewById(R.id.messageEditText);
        mSendButton = findViewById(R.id.sendButton);
        overlay = findViewById(R.id.overlay);
        settings = findViewById(R.id.settings);

        newEventButton = findViewById(R.id.newEventButton);
        eventName = findViewById(R.id.eventName);
        eventCode = findViewById(R.id.eventCode);
        createEvent = findViewById(R.id.createEvent);

        newEventButton.setOnClickListener(e -> newEventButtonClicked());
        createEvent.setOnClickListener(e -> createEventClicked());

        List<ConCatMessage> ccMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.message, ccMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        // watches text field and only enables Send button if text has been entered
        // prevents saving empty strings to the db
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // restrict message length to 1000 chars
        mMessageEditText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(1000)});

        // handles if message is to join a room or send a message
        mSendButton.setOnClickListener(e -> {
            if (!roomSuccessful) {
                final String enteredCode = mMessageEditText.getText().toString();
                final DatabaseReference findEventRef = mEventsDatabaseReference.child(enteredCode);
                DatabaseReference eventRefParent = findEventRef.getParent();

                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean eventFound = false;
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ConCatEvent tempEvent = snapshot.getValue(ConCatEvent.class);
                            if (tempEvent != null) {
                                if (tempEvent.getCode().equals(enteredCode)) {
                                    eventFound = true;
                                    setCurrentEvent(tempEvent);
                                    makeToast("Successfully entered " + currentEvent.getName());
                                }
                            }
                        }
                        if (!eventFound) {
                            makeToast("Event chat not found!");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        logger.log(Level.INFO, "Database error");
                    }
                };

                if (eventRefParent != null) {
                    eventRefParent.addListenerForSingleValueEvent(eventListener);
                }

                checkCurrentEventNull();
                mMessageEditText.setText("");
            } else {
                ConCatMessage ccMessage = new ConCatMessage(mMessageEditText.getText().toString(), currentEvent);
                mMessagesDatabaseReference.push().setValue(ccMessage);
                mMessageEditText.setText("");
            }

        });

        settings.setOnClickListener(e -> {
            Intent myIntent = new Intent(MainActivity.this, Settings.class);
            //myIntent.putExtra("currentEvent", currentEvent);
            startActivity(myIntent);
        });

    }

    private void newEventButtonClicked() {
        logger.log(Level.SEVERE, "new event button clicked");
        eventName.setVisibility(View.VISIBLE);
        eventCode.setVisibility(View.VISIBLE);
        createEvent.setVisibility(View.VISIBLE);
        newEventButton.setVisibility(View.GONE);
    }

    private void createEventClicked() {
        String newEventName = eventName.getText().toString();
        String newEventCode = eventCode.getText().toString();

        checkCodeUnique(newEventName, newEventCode);
    }

    private void enterNewEventRoom(ConCatEvent event) {
        currentEvent = event;
        checkCurrentEventNull();
        makeToast("Successfully entered " + currentEvent.getName());
    }

    private void checkCodeUnique(String name, String code) {
        DatabaseReference findEventRef = mEventsDatabaseReference.child(code);
        DatabaseReference eventRefParent = findEventRef.getParent();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setUniqueFlag(true);
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ConCatEvent tempEvent = snapshot.getValue(ConCatEvent.class);
                    if (tempEvent != null) {
                        if (tempEvent.getCode().equals(code)) {
                            setUniqueFlag(false);
                        }
                    }
                }

                if (uniqueFlag) {
                    ConCatEvent newEvent = new ConCatEvent(name, code);
                    mEventsDatabaseReference.push().setValue(newEvent);

                    enterNewEventRoom(newEvent);
                } else {
                    logger.log(Level.SEVERE, "code not unique");
                    makeToast("Event code exists already");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                logger.log(Level.INFO, "Database error");
            }
        };

        if (eventRefParent != null) {
            eventRefParent.addListenerForSingleValueEvent(eventListener);
        }
    }

    private void makeToast(String message) {
        logger.log(Level.SEVERE, "toast made");
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setUniqueFlag(boolean bool) {
        uniqueFlag = bool;
    }

    private void checkCurrentEventNull() {
        if (currentEvent != null) {
            roomSuccessful = true;
            attachDatabaseReadListener();
            overlay.setVisibility(View.GONE);
            settings.setVisibility(View.VISIBLE);
        } else {
            logger.log(Level.SEVERE, "CE is null");
        }
    }

    private void setCurrentEvent(ConCatEvent event) {
        currentEvent = event;
        checkCurrentEventNull();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                    ConCatMessage ccMessage = dataSnapshot.getValue(ConCatMessage.class);
                    checkCurrentEventNull();

                    if (ccMessage != null && ccMessage.getEvent() != null) {
                        if (ccMessage.getEvent().getCode().equals(currentEvent.getCode())) {
                            mMessageAdapter.add(ccMessage);
                        }
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };

            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }
}
