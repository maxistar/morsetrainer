package com.maxistar.morsetrainer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
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

import android.util.Log;
import android.widget.Toast;

public class TelegraphActivity extends AppCompatActivity {

    static String TAG = "udp";
    static int PORT = 2000;

    private DatagramSocket socket;

    private Vibrator vibrator;

    private long keyDownTime = 0;
    private boolean pressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telegraph);

        Button b1 = this.findViewById(R.id.button2);

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


    private void vibrate() {
        final VibrationEffect vibrationEffect1;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // this effect creates the vibration of default amplitude for 1000ms(1 sec)
            vibrationEffect1 = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE);

            // it is safe to cancel other vibrations currently taking place
            vibrator.cancel();
            vibrator.vibrate(vibrationEffect1);
        } else {
            vibrator.vibrate(500);
        }
    }


    private void stopClick() {
        pressed = false;
        long keyUpTime = System.currentTimeMillis();
        long interval = keyUpTime - keyDownTime;
        sendBroadcast("broadcast:" + interval);
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
        vibrate();
    }
}