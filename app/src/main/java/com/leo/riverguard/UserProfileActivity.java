package com.leo.riverguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {



    public static ConnectedThread connectedThread;
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update

    MaterialCardView goBump, goUltrasonic, goIDR;

    static TextView ultrasonicLabel, idrLabel;

    Boolean bumpStatus, goBumpStatus;

    TextView name_field;

    FirebaseAuth mAuth;
    DatabaseReference database;
    Users user;
    ProgressBar progressBar;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);

        bumpStatus = false;
        goBumpStatus = false;

        ultrasonicLabel = findViewById(R.id.ultrasonic_label);
        idrLabel = findViewById(R.id.idr_label);

        goBump = findViewById(R.id.goBump);
        goUltrasonic = findViewById(R.id.goUltrasonic);
        goIDR = findViewById(R.id.goIDR);

        goUltrasonic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, UltrasonicInfoActivity.class);
                startActivity(intent);
            }
        });

        goIDR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, IdrInfoActivity.class);
                startActivity(intent);
            }
        });



        connectedThread = new ConnectedThread(BluetoothConnectionActivity.mmSocket);
        connectedThread.start();

        goBump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (goBumpStatus){
                    goBumpStatus = false;

                    if (bumpStatus){
                        bumpStatus = false;
                        connectedThread.write("Nope;");
                    } else if (!bumpStatus){
                        bumpStatus = true;
                        connectedThread.write("Yup;");
                    }

                } else if (!goBumpStatus){
                    goBumpStatus = true;

                    if (bumpStatus){
                        bumpStatus = false;
                        connectedThread.write("goNope;");
                    } else if (!bumpStatus){
                        bumpStatus = true;
                        connectedThread.write("goYup;");
                    }

                }

            }
        });



        name_field = findViewById(R.id.fullname_field);

        progressBar = findViewById(R.id.progressBar);
        container = findViewById(R.id.profile_container);

        progressBar.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://river-guard-default-rtdb.firebaseio.com/").getReference("Users").child(mAuth.getCurrentUser().getUid());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(Users.class);
                name_field.setText(user.getFirstName() + " " + user.getLastName());
                progressBar.setVisibility(View.GONE);
                container.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                container.setVisibility(View.VISIBLE);
            }
        });


    }


    /* ============================ Thread to Create Bluetooth Connection =================================== */
    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;


        ArrayList<String> ultrasonicList = new ArrayList<>();
        ArrayList<String> idrList = new ArrayList<>();


        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes = 0; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    /*
                    Read from the InputStream from Arduino until termination character is reached.
                    Then send the whole String message to GUI Handler.
                     */
                    buffer[bytes] = (byte) mmInStream.read();
                    String message_ultrasonic;
                    String message_idr;
                    String read_bump;


                    if (buffer[bytes] == ';'){
                        message_ultrasonic = new String(buffer,0,bytes);
                        ultrasonicList.add(message_ultrasonic);

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                ultrasonicLabel.setText(message_ultrasonic);

                            }
                        });

                        Log.e("Arduino Message Ultrasonic",message_ultrasonic);
                        BluetoothConnectionActivity.handler.obtainMessage(MESSAGE_READ,message_ultrasonic).sendToTarget();
                        bytes = 0;
                    }
                    else if (buffer[bytes] == '&') {
                        message_idr = new String(buffer,0,bytes);
                        idrList.add(message_idr);

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                idrLabel.setText(message_idr);

                            }
                        });

                        Log.e("Arduino Message Idr",message_idr);
                        BluetoothConnectionActivity.handler.obtainMessage(MESSAGE_READ,message_idr).sendToTarget();
                        bytes = 0;
                    }
                    else if (buffer[bytes] == '@') {
                        read_bump = new String(buffer,0,bytes);
                        Log.e("Arduino Message Bump",read_bump);

                        if (read_bump.matches("Yup")){
                            bumpStatus = true;
                            goBump.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.yellow));
                        } else if (read_bump.matches("Nope")){
                            bumpStatus = false;
                            goBump.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.gray_background));
                        }
                        BluetoothConnectionActivity.handler.obtainMessage(MESSAGE_READ,read_bump).sendToTarget();
                        bytes = 0;
                    }
                    else {
                        bytes++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes(); //converts entered String into bytes
            try {

                mmOutStream.write(bytes);

            } catch (IOException e) {
                Log.e("Send Error","Unable to send message",e);
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}