package com.lrose07.concat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.view.View;
import android.widget.TableLayout;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;
    private boolean roomSuccessful = false;
    private TableLayout overlay;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference mEventsDatabaseReference;
    private ChildEventListener mChildEventListener;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private ConCatEvent currentEvent;

    Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        mEventsDatabaseReference = mFirebaseDatabase.getReference().child("events");

        mMessageListView = findViewById(R.id.messageListView);
        mPhotoPickerButton = findViewById(R.id.photoPickerButton);
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

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!roomSuccessful) {
                    // if code entered exists?
                    final String enteredCode = mMessageEditText.getText().toString();
                    final DatabaseReference findEventRef = mEventsDatabaseReference.child(enteredCode);
                    DatabaseReference eventRefParent = findEventRef.getParent();

                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ConCatEvent tempEvent = snapshot.getValue(ConCatEvent.class);
                                setCurrentEvent(tempEvent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            logger.log(Level.INFO, "Database error");
                        }
                    };

                    eventRefParent.addListenerForSingleValueEvent(eventListener);

                    checkCurrentEventNull();
                    mMessageEditText.setText("");
                } else {
                    ConCatMessage ccMessage = new ConCatMessage(mMessageEditText.getText().toString(), currentEvent);
                    mMessagesDatabaseReference.push().setValue(ccMessage);
                    mMessageEditText.setText("");
                }
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
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ConCatMessage ccMessage = dataSnapshot.getValue(ConCatMessage.class);
                    checkCurrentEventNull();
                    if (ccMessage.getEvent() != null) {
//                        if (ccMessage.getEvent() == currentEvent) {
//                            mMessageAdapter.add(ccMessage);
//                        }
                        mMessageAdapter.add(ccMessage);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };

            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }
}
