package com.example.slidertest;

import io.palaima.smoothbluetooth.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;

import io.palaima.smoothbluetooth.Device;

public class DevicesAdapter extends ArrayAdapter<DeviceFound> {
    public DevicesAdapter(Context context, ArrayList<DeviceFound> deviceItems) {
        super(context, 0, deviceItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_list, parent, false);
        }
        Button itemButton = (Button) convertView.findViewById(R.id.itemButton);
        //Get the button located at the listed item's position which is an integer
        itemButton.setTag(position);
        itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                //Define an item/class based on the position
                DeviceFound connectedItem = getItem(position);
                //Operate the item/class at the gotten position
                connectedItem.setDeviceName("Changed");
                //notify data has changed to update listview
                notifyDataSetChanged();
            }
        });

        // Get the data item for this position
        DeviceFound deviceItems = getItem(position);

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.deviceName);
        TextView tvHome = (TextView) convertView.findViewById(R.id.deviceAddress);
        //Button itemConnect = (Button) convertView.findViewById(R.id.itemButton);
        // Populate the data into the template view using the data object
        tvName.setText(deviceItems.getDeviceName());
        tvHome.setText(deviceItems.getAddressName());

        // Return the completed view to render on screen
        return convertView;
    }
}

