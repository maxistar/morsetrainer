package com.maxistar.morsetrainer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.maxistar.morsetrainer.R;
import com.maxistar.morsetrainer.ServiceLocator;

import android.os.StrictMode;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import android.util.Log;
import android.widget.Toast;

public class TelegraphActivity extends AppCompatActivity {

    static String TAG = "udp";
    static int PORT = 2000;

    private DatagramSocket socket;

    private Vibrator vibrator;

    private long keyDownTime = 0;

    private int counter = 0;
    private int dialogCounter = 0;

    private boolean pressed = false;

    String uniqueId = "";

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telegraph);

        Button b1 = this.findViewById(R.id.button2);

        uniqueId = UUID.randomUUID().toString().substring(0, 8);

        b1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.w("TAG", "Action dows");
                        startClick();
                        v.setPressed(true);
                        return true;

                    case MotionEvent.ACTION_UP:
                        stopClick();
                        // v.performClick();
                        Log.w("UP", "Action up");
                        v.setPressed(false);
                        return true;
                }
                return false;
            }
        });

        checkPermissions();
        // get the VIBRATOR_SERVICE system service
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);


    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED
        ) {
            return;
        }

        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.DISABLE_KEYGUARD,
                Manifest.permission.VIBRATE
        };

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.VIBRATE)) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
    }

    public void onResume() {
        super.onResume();
        startUDPListener();
        ServiceLocator.getInstance().getWakeLockService().acquireLock(this.getApplicationContext());
    }

    public void onPause() {
        super.onPause();
        stopListening();
        ServiceLocator.getInstance().getWakeLockService().releaseLock();
    }

    private void stopListening() {
        socket.close();
    }


    private void vibrate(int duration) {
        final VibrationEffect vibrationEffect1;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // this effect creates the vibration of default amplitude for 1000ms(1 sec)
            vibrationEffect1 = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE);

            // it is safe to cancel other vibrations currently taking place
            vibrator.cancel();
            vibrator.vibrate(vibrationEffect1);
        } else {
            vibrator.vibrate(duration);
        }
    }


    private void stopClick() {
        if (!pressed) {
            return;
        }
        pressed = false;
        long keyUpTime = System.currentTimeMillis();
        long interval = keyUpTime - keyDownTime;

        String payload = "" + uniqueId + ":" + (counter++) + ":" + interval;
        sendBroadcast(payload);
        handler.postDelayed(() -> sendBroadcast(payload), 100);
        handler.postDelayed(() -> sendBroadcast(payload), 200);
        handler.postDelayed(() -> sendBroadcast(payload), 300);
        handler.postDelayed(() -> sendBroadcast(payload), 400);
    }

    private void startClick() {
        if (pressed) {
            return;
        }
        pressed = true;
        keyDownTime = System.currentTimeMillis();
    }

    void sendBroadcast(String messageStr) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            byte[] sendData = messageStr.getBytes();
            InetAddress address = getBroadcastAddress();
            Log.e(TAG, "address: " + address.toString());
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, PORT);
            socket.send(sendPacket);
            System.out.println(this.getClass().getName() + "Broadcast packet sent to: " + getBroadcastAddress().getHostAddress());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        }
    }

    public InetAddress getBroadcastAddress() throws UnknownHostException {
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        android.net.DhcpInfo dhcp = wifi.getDhcpInfo();
        int broadcast = (dhcp.ipAddress & dhcp.netmask) | dhcp.netmask ^ 0xFFFFFFFF;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++) {
            quads[k] = (byte) ((broadcast >> (k * 8)) & 0xFF);
        }
        return InetAddress.getByAddress(quads);
    }

    private void startUDPListener() {

        Thread thread = new Thread(() -> {
            try {
                startListening(getBroadcastAddress());
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();

    }

    public void startListening(InetAddress address) {
        try {
            socket = new DatagramSocket(2000);
            byte[] buffer = new byte[1024];
            System.out.println("address: " + address.toString());
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                // Handle received message
                handleMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(String message) {
        // Implement your message handling logic here
        System.out.println("Received message: " + message);

        String [] parts = message.split(":");
        if (parts.length != 3) {
            //wrong format
            return;
        }
        String clientId = parts[0];
        String duration = parts[2];
        String dialogCounterString = parts[1];
        int vibrationDuration;
        if (clientId.equals(uniqueId)) {
            //message from us
            return;
        }
        try{
            vibrationDuration = Integer.parseInt(duration);
            System.out.println(vibrationDuration); // output = 25
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
            return;
        }
        try{
            int counter = Integer.parseInt(dialogCounterString);
            if (counter <= dialogCounter) {
                return;
            }
            dialogCounter = counter;
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
            return;
        }
        vibrate(vibrationDuration);


    }
}