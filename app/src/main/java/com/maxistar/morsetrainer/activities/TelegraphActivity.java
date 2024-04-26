package com.maxistar.morsetrainer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.maxistar.morsetrainer.R;
import android.os.StrictMode;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.util.Log;
public class TelegraphActivity extends AppCompatActivity {

    static String TAG = "udp";
    static int PORT = 2000;

    private DatagramSocket socket;

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
    }

    public void onResume() {
        super.onResume();
        startUDPListener();
    }

    public void onPause() {
        super.onPause();
        stopListening();
    }

    private void stopListening() {
        socket.close();
    }


    private void vibrate() {

    }


    private void stopClick() {
        sendBroadcast("broadcast");
    }

    private void startClick() {
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
    }
}