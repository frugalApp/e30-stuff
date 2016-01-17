package com.example.peter.carentry;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Peter on 1/9/2016.
 */
public class TalkToCar {
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothSocket mBluetoothSocket;
    private String deviceName = "HC-05";
    private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public TalkToCar() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        connect();
    }

    private boolean connect() {
        try {
            if (mBluetoothAdapter == null) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothDevice = null;
                mBluetoothSocket = null;
            }
            if (mBluetoothAdapter.isEnabled()) {
                if (mBluetoothSocket == null) {
                    for (BluetoothDevice device : mBluetoothAdapter.getBondedDevices()) {
                        if (device.getName().equals(deviceName)) {
                            mBluetoothDevice = device;
                            mBluetoothSocket = mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
                            mBluetoothSocket.connect();
                            return true;
                        }
                    }
                    return false;
                } else {
                    if (mBluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                        mBluetoothSocket = mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
                        mBluetoothSocket.connect();
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public void lockCar() {
        sendMsg("1234");
    }

    public void unlockCar() {
        sendMsg("2345");
    }

    public void panic() {
        sendMsg("3456");
    }

    private void sendMsg(String msg) {
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled() && connect()) {
            try {
                mBluetoothSocket.getOutputStream().write(msg.getBytes());
            } catch (Exception e) {
                resetAndResend(msg);
                return;
            }
        }
    }
    private void resetAndResend(String msg) {
        mBluetoothSocket = null;
        mBluetoothDevice = null;
        if (connect()) {
            try {
                mBluetoothSocket.getOutputStream().write(msg.getBytes());
            } catch (Exception v) {
                return;
            }
        }
    }
}
