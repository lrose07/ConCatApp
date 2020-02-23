package com.lrose07.concat;

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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private MessageAdapter mMessageAdapter;
    private EditText mMessageEditText;
    private Button mSendButton;
    private boolean roomSuccessful = false;
    private TableLayout overlay;

    private DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference mEventsDatabaseReference;
    private ChildEventListener mChildEventListener;

    private ConCatEvent currentEvent;

    Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

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

        List<ConCatMessage> ccMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.message, ccMessages);
        mMessageListView.setAdapter(mMessageAdapter);

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

        mMessageEditText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(100)});

        mSendButton.setOnClickListener(e -> {
            if (!roomSuccessful) {
                final String enteredCode = mMessageEditText.getText().toString();
                final DatabaseReference findEventRef = mEventsDatabaseReference.child(enteredCode);
                DatabaseReference eventRefParent = findEventRef.getParent();

                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ConCatEvent tempEvent = snapshot.getValue(ConCatEvent.class);
                            if (tempEvent != null) {
                                if (tempEvent.getCode().equals(enteredCode)) {
                                    setCurrentEvent(tempEvent);
                                }
                            }
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
    }

    private void checkCurrentEventNull() {
        if (currentEvent != null) {
            roomSuccessful = true;
            attachDatabaseReadListener();
            overlay.setVisibility(View.GONE);
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
