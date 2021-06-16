package com.example.slidertest;
import java.util.ArrayList;

import io.palaima.smoothbluetooth.SmoothBluetooth;
import io.palaima.smoothbluetooth.Device;

public class DeviceFound {

    // TextView 1
    private String deviceName;

    // TextView 2
    private String addressName;

    private Device device;

    // create constructor to set the values for all the parameters of the each single view
    public DeviceFound(String deviceName, String addressName) {
        this.deviceName = deviceName;
        this.addressName = addressName;
    }
/*
    public DeviceFound(String deviceName, String addressName, DeviceFound device) {
        this.deviceName = deviceName;
        this.addressName = addressName;
        this.device = device;
    }
*/
    // getter method for returning the ID of the imageview
    public String getDeviceName() {
        return deviceName;
    }

    // getter method for returning the ID of the TextView 1
    public String getAddressName() {
        return addressName;
    }
/*
    public Device getDevice() {
        return device;
    }
*/
    public void setDeviceName(String newName) { this.deviceName = newName; }

    public void setAddressName(String newName) { this.addressName = newName;}


    /*
    public void setDevice(Device device) {this.device = device;}
  */
}
