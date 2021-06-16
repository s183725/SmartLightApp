package com.example.slidertest;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.bluetooth.BluetoothAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.database.DefaultDatabaseErrorHandler;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


//MULTITOGGLEBUTTON LIBS
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;


//BLUETOOTH LIBS
import com.sirvar.bluetoothkit.BluetoothKit;
import com.palaima.bluetoothmanager.BuildConfig;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import io.palaima.smoothbluetooth.SmoothBluetooth;
import io.palaima.smoothbluetooth.Device;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {
    
    final int warm = 0;
    final int cold = 1;

    public static final int ENABLE_BT__REQUEST = 1;

    private int[] sliderValue = new int[2];
    private Slider[] sliders = new Slider[2];


    private MaterialButtonToggleGroup mtb1;
    private TextView slideText;
    private ListView deviceListView;
    private ProgressBar progressBar;

    public void setDeviceList(ListView deviceList) {
        this.deviceListView = deviceList;
    }

    private Calendar nowTime = Calendar.getInstance();
    private SimpleDateFormat dateFormat;
    private String date;


    private SmoothBluetooth smoothSignal;
    // INSTANTIATION OF APP AND CREATION CONDITIONS
    //
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        progressBar.animate().cancel();
        progressBar.setAlpha(0.0f);

        //Bluetooth initiation
        //
        smoothSignal = new SmoothBluetooth(getApplicationContext(),
                SmoothBluetooth.ConnectionTo.OTHER_DEVICE, SmoothBluetooth.Connection.SECURE,smoothListener);

        //Buttons and interaction initiation
        //
        mtb1 = (MaterialButtonToggleGroup) findViewById(R.id.mtb); //MultiToggleButton
        Button connectButton = (Button) findViewById(R.id.btnConnect); //DiscoveryButton

        //Slider initiation
        //
        sliders[warm] = (Slider) findViewById(R.id.warmSlider);
        sliders[cold] = (Slider) findViewById(R.id.coldSlider);
        slideText = (TextView) findViewById(R.id.blueText) ;

        //ListView initiation and itemized device handling
        //
        ArrayList<DeviceFound> devicesArrayList = new ArrayList<>();
        devicesArrayList.add(new DeviceFound("TestNavn0","0x2123"));
        devicesArrayList.add(new DeviceFound("TestNavn1","0x2003"));
        DevicesAdapter adapter = new DevicesAdapter(this,devicesArrayList);
        deviceListView = (ListView) findViewById(R.id.deviceList);
        deviceListView.setAdapter(adapter);

        /*      DeviceFound[] items = {new DeviceFound("name1","mac1"),
                               new DeviceFound("name2","mac2")};
        */

        //deviceListView.setAdapter( new ArrayAdapter(this,R.layout.device_list,R.id.deviceName,items));
        //deviceListView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();

        //MultiToggleButton Conditional
        //
                mtb1.addOnButtonCheckedListener(
                        new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                            @Override
                            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                                if (checkedId == R.id.btnCycle) {
                                    SlidersOn(sliders, false);
                                    ConnectButtonOn(connectButton,false);
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    slideText.setText(midnightMinutes());
                                } else if (checkedId == R.id.btnManual) {
                                    initTextview();
                                    SlidersOn(sliders, true);
                                    sliders[warm].addOnChangeListener(warmListen);
                                    sliders[cold].addOnChangeListener(coldListen);
                                    ConnectButtonOn(connectButton,false);
                                } else if (checkedId == R.id.btnOff) {
                                    SlidersOn(sliders, false);
                                    slideText.setText("Disconnected");
                                    ConnectButtonOn(connectButton,true);
                                    //adapter.notifyDataSetChanged();
                                }
                            }
                        });
        //Connect button listener
        //
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceFound example = new DeviceFound("AddedName","Added address");
                devicesArrayList.add(example);
                adapter.notifyDataSetChanged();
            }
        });

        //Display standard value for sliders as defined in activity_main
        //
        sliderValue[warm] = (int) sliders[warm].getValue();
        sliderValue[cold] = (int) sliders[cold].getValue();
    }

    // WARM SLIDER CONFIGURATION
    //
    private final Slider.OnChangeListener warmListen = new Slider.OnChangeListener() {
        @Override
        public void onValueChange(@NonNull Slider warmSlider, float value, boolean fromUser) {
            sliderValue[warm] = (int) warmSlider.getValue();
            String textString = "Warm: " + sliderValue[warm] + ", Cold: " + sliderValue[cold];
            slideText.setText(textString);
        }
    };

    // COLD SLIDER CONFIGURATION
    //
    private final Slider.OnChangeListener coldListen = new Slider.OnChangeListener() {
        @Override
        public void onValueChange(@NonNull Slider coldSlider, float value, boolean fromUser) {
            sliderValue[cold] = (int) coldSlider.getValue();
            String textString = "Warm: " + sliderValue[warm] + ", Cold: " + sliderValue[cold];
            slideText.setText(textString);
        }
    };
/*
    // TOGGLEBUTTON LISTENER AND CONDITIONALS
    private final MaterialButtonToggleGroup.OnButtonCheckedListener mtbListen = new MaterialButtonToggleGroup.OnButtonCheckedListener() {

        public void onSelected(MaterialButtonToggleGroup mtb1, View item, int position, String label, boolean Selected) {
            if ( mtb1.getCheckedButtonId() ==  idCycle) {
                SlidersOn(sliders, false);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                slideText.setText(midnightMinutes());
            } else if ( mtb1.getCheckedButtonId() == idManual ) {
                initTextview();
                SlidersOn(sliders, true);
                sliders[warm].addOnChangeListener(warmListen);
                sliders[cold].addOnChangeListener(coldListen);
            } else {
                SlidersOn(sliders, false);
            }
        }
    };
  */

    // SLIDER ON/OFF SWITCH FOR state
    //
    private void SlidersOn(Slider[] sliders, boolean state) {
        if (state == true) {
            sliders[warm].setEnabled(true);
            sliders[warm].setAlpha(1.0f);
            sliders[cold].setEnabled(true);
            sliders[cold].setAlpha(1.0f);
            findViewById(R.id.imageView2).setAlpha(1.0f);
        } else {
            sliders[warm].setEnabled(false);
            sliders[warm].setAlpha(0.3f);
            sliders[cold].setEnabled(false);
            sliders[cold].setAlpha(0.3f);
            findViewById(R.id.imageView2).setAlpha(0.3f);
        }
    }

    private void progressBarOn(boolean enable){
        ProgressBar buffer = (ProgressBar) findViewById(R.id.progressBar);
        if(enable) {
            buffer.setClickable(false);
            buffer.setAlpha(1.0f);
            buffer.setRotation(0.5f);
            buffer.animate().start();
        } else {
            buffer.setClickable(false);
            buffer.setAlpha(0.0f);
            buffer.animate().cancel();
        }
    }

    private void ConnectButtonOn(Button connectButton, boolean enable){
        if (enable == false) {
            connectButton.setClickable(false);
            connectButton.setFocusable(false);
            connectButton.setAlpha(0.3f);
        } else if(enable == true) {
            connectButton.setClickable(true);
            connectButton.setFocusable(true);
            connectButton.setAlpha(1.0f);
        }
    }
    
    private void MultiToggleButtonOn(Boolean enable) {
        if(enable) {
            mtb1.setAlpha(1.0f);
            mtb1.setClickable(true);
        } else {
            mtb1.setAlpha(0.3f);
            mtb1.setClickable(false);
        }
    }

    private void initTextview() {
        String textString = "Warm: " + sliderValue[warm] + ", Cold: " + sliderValue[cold];
        slideText.setText(textString);
    }

    private String midnightMinutes() {
        nowTime = Calendar.getInstance();
        int h = nowTime.get(Calendar.HOUR);
        int m = nowTime.get(Calendar.MINUTE)+h*60;
        //return "Time: " +  h + ": " + m; // Displays time
        return "Minutes: " + m;
    }

    //BLUETOOTH OPERATION FUNCTIONS AND LISTENER CONDITIONS
    //
    private SmoothBluetooth.Listener smoothListener = new SmoothBluetooth.Listener() {
        @Override
        public void onBluetoothNotSupported() {
            Toast.makeText(getApplicationContext(), "Bluetooth not supported", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onBluetoothNotEnabled() {
            Toast.makeText(getApplicationContext(), "Bluetooth not enabled", Toast.LENGTH_LONG).show();
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, ENABLE_BT__REQUEST);
        }

        @Override
        public void onConnecting(Device device) {
            mtb1.setAlpha(0.3f);
            progressBarOn(true);
            //deviceListView.setAlpha(0.0f);
        }

        @Override
        public void onConnected(Device device) {
            mtb1.setAlpha(1.0f);
            progressBarOn(false);
        }

        @Override
        public void onDisconnected() {
            mtb1.setAlpha(0.0f);
            progressBarOn(false);
        }

        @Override
        public void onConnectionFailed(Device device) {

        }

        @Override
        public void onDiscoveryStarted() {

        }

        @Override
        public void onDiscoveryFinished() {

        }

        @Override
        public void onNoDevicesFound() {

        }

        @Override
        public void onDevicesFound(List<Device> deviceList, SmoothBluetooth.ConnectionCallback connectionCallback) {

        }

        @Override
        public void onDataReceived(int data) {

        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        smoothSignal.stop();
    };

}


