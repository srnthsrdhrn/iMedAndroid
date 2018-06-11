package io.iqube.srinath.imeddispenser.vendor;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import io.iqube.srinath.imeddispenser.R;
import io.iqube.srinath.imeddispenser.models.User;
import io.iqube.srinath.imeddispenser.network.API_interface;
import io.iqube.srinath.imeddispenser.network.ServiceGenerator;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendorHomeActivity extends AppCompatActivity {
    DataTransfer dataTransfer;
    boolean connected = false;
    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter bluetoothAdapter;
    AlertDialog dialog;
    Button connect, send;
    SimpleDraweeView vendor_qr;
    Realm realm;
    EditText send_data;
    AlertDialog chamberControl;
    ProgressBar progressBar;
    Button chamberControlButton;
    Button chamber_1_load, chamber_2_load, chamber_3_load;
    Button chamber_1_load_done, chamber_2_load_done, chamber_3_load_done;
    EditText wheel_qty, spring_1_qty, spring_2_qty, chamber_1_qty, chamber_2_qty, chamber_3_qty;
    Spinner wheel_med, spring_1_med, spring_2_med, chamber_1_med, chamber_2_med, chamber_3_med;

    public void disableButtons() {
        chamber_1_load.setEnabled(false);
        chamber_2_load.setEnabled(false);
        chamber_3_load.setEnabled(false);
        chamber_1_load_done.setEnabled(false);
        chamber_2_load_done.setEnabled(false);
        chamber_3_load_done.setEnabled(false);
    }

    public void enableButtons() {
        chamber_1_load.setEnabled(true);
        chamber_2_load.setEnabled(true);
        chamber_3_load.setEnabled(true);
        chamber_1_load_done.setEnabled(true);
        chamber_2_load_done.setEnabled(true);
        chamber_3_load_done.setEnabled(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_home);
        connect = findViewById(R.id.connect);
        vendor_qr = findViewById(R.id.vendor_qr);
        send_data = findViewById(R.id.text_data);
        progressBar = new ProgressBar(this);
        send = findViewById(R.id.send);
        chamberControlButton = findViewById(R.id.chamber_control_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataTransfer.outputStream != null) {
                    dataTransfer.write(send_data.getText().toString());
                }
            }
        });
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        String data = "vendor-" + user.getVendor_id().split("-")[1];
        vendor_qr.setImageURI("https://chart.googleapis.com/chart?cht=qr&chl=" + data + "&chs=200x200&chld=L|0");
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.chamber_control, null);
        wheel_qty = v.findViewById(R.id.wheel_med_qty);
        spring_1_qty = v.findViewById(R.id.spring_1_med_qty);
        spring_2_qty = v.findViewById(R.id.spring_2_med_qty);
        chamber_1_qty = v.findViewById(R.id.chamber_1_med_qty);
        chamber_2_qty = v.findViewById(R.id.chamber_2_med_qty);
        chamber_3_qty = v.findViewById(R.id.chamber_3_med_qty);
        chamber_1_load = v.findViewById(R.id.chamber_1_load);
        chamber_2_load = v.findViewById(R.id.chamber_2_load);
        chamber_3_load = v.findViewById(R.id.chamber_3_load);
        chamber_1_load_done = v.findViewById(R.id.chamber_1_done_load);
        chamber_2_load_done = v.findViewById(R.id.chamber_2_done_load);
        chamber_3_load_done = v.findViewById(R.id.chamber_3_done_load);
        wheel_med = v.findViewById(R.id.wheel_med);
        spring_1_med = v.findViewById(R.id.spring_1_med);
        spring_2_med = v.findViewById(R.id.spring_2_med);
        chamber_1_med = v.findViewById(R.id.chamber_1_med);
        chamber_2_med = v.findViewById(R.id.chamber_2_med);
        chamber_3_med = v.findViewById(R.id.chamber_3_med);
        final API_interface client = ServiceGenerator.getClient("", "", this).create(API_interface.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).
                setTitle("Chamber Control")
                .setView(v)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String data = "{ \"device_id\": 2," +
                                "\"data\": [" +
                                " {\"medicine\": \"" + wheel_med.getSelectedItem() + "\", \"qty\": " + wheel_qty.getText().toString() + "}, " +
                                " {\"medicine\": \"" + spring_1_med.getSelectedItem() + "\", \"qty\": " + spring_1_qty.getText().toString() + "}," +
                                " {\"medicine\": \"" + spring_2_med.getSelectedItem() + "\", \"qty\": " + spring_2_qty.getText().toString() + "}," +
                                " {\"medicine\": \"" + chamber_1_med.getSelectedItem() + "\", \"qty\": " + chamber_1_qty.getText().toString() + "}," +
                                " {\"medicine\": \"" + chamber_2_med.getSelectedItem() + "\", \"qty\": " + chamber_2_qty.getText().toString() + "}," +
                                " {\"medicine\": \"" + chamber_3_med.getSelectedItem() + "\", \"qty\": " + chamber_3_qty.getText().toString() + "}," +
                                "]" +
                                "}";
                        dataTransfer.write("end");
                        client.postLoad(data).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(VendorHomeActivity.this, "Data Posted", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(VendorHomeActivity.this, "Error Posting", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
        chamber_1_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataTransfer.write("4");
                disableButtons();
            }
        });
        chamber_1_load_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataTransfer.write("7");
                disableButtons();
            }
        });
        chamber_2_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataTransfer.write("5");
                disableButtons();
            }
        });
        chamber_2_load_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataTransfer.write("7");
                disableButtons();
            }
        });
        chamber_3_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataTransfer.write("6");
                disableButtons();
            }
        });
        chamber_3_load_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataTransfer.write("7");
                disableButtons();
            }
        });
        chamberControl = builder.create();
        chamberControlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(dataTransfer == null){
//                    Toast.makeText(VendorHomeActivity.this, "Please Connect to Device", Toast.LENGTH_SHORT).show();
//                }else {
                    chamberControl.show();
//                }
            }
        });


    }

    public void connect() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {

                if (!bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.startDiscovery();
                }
                Set<BluetoothDevice> paired_devices = bluetoothAdapter.getBondedDevices();
                if (!paired_devices.isEmpty()) {
                    for (BluetoothDevice device : paired_devices) {
                        listAdapter.add(device.getName() + "\n" + device.getAddress());
                    }
                    listAdapter.notifyDataSetChanged();
                    builder.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String[] strings = listAdapter.getItem(i).split("\n");
                            BluetoothDevice device;
                            try {
                                device = bluetoothAdapter.getRemoteDevice(strings[1]);
                            } catch (Exception e) {
                                device = bluetoothAdapter.getRemoteDevice(strings[2]);
                            }
                            new Client().execute(device);
                        }
                    });

                }
                builder.setTitle("Paired Devices");

                dialog = builder.create();
                dialog.show();


            } else {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 1);
            }
        } else {
            Toast.makeText(this, "This Device Does not Support Bluetooth", Toast.LENGTH_SHORT).show();
        }


    }

    private class DataTransfer extends AsyncTask<Void, String, Void> {
        OutputStream outputStream;
        BluetoothSocket socket;
        InputStream inputStream;

        DataTransfer(BluetoothSocket socket) {

            try {
                this.socket = socket;
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            enableButtons();
            Toast.makeText(VendorHomeActivity.this, values[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                int available = inputStream.available();
                byte[] buffer = new byte[1024];  // buffer store for the stream
                int bytes; // bytes returned from read()
                // Keep listening to the InputStream until an exception occurs
                while (true) {
                    try {
                        // Read from the InputStream
                        bytes = inputStream.read(buffer);
                        // Send the obtained bytes to the UI activity
                        String string = new String(buffer, 0, bytes);
                        publishProgress(string);
                    } catch (IOException e) {
                        break;
                    }
                }
            } catch (IOException e) {
                Toast.makeText(VendorHomeActivity.this, "Connection Error, PLease check if the device is online", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            return null;
        }

        public void write(String data) {
            try {
                outputStream.write(data.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(VendorHomeActivity.this, "Send Error", Toast.LENGTH_SHORT).show();
            }
        }

        public void cancel() {
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
            }
        }
    }


    private void manageConnectedSocket(BluetoothSocket mmSocket) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VendorHomeActivity.this, "Connected", Toast.LENGTH_LONG).show();
            }
        });
        connected = true;
        dataTransfer = new DataTransfer(mmSocket);
        dataTransfer.execute();
    }

    private class Client extends AsyncTask<BluetoothDevice, Void, Void> {
        @Override
        protected Void doInBackground(BluetoothDevice... bluetoothDevices) {
            try {
                BluetoothSocket Socket = bluetoothDevices[0].createRfcommSocketToServiceRecord(MY_UUID);
                // Cancel discovery because it will slow down the connection
                bluetoothAdapter.cancelDiscovery();

                try {
                    // Connect the device through the socket. This will block
                    // until it succeeds or throws an exception
                    Socket.connect();
                } catch (IOException connectException) {
                    // Unable to connect; close the socket and get out
                    try {
                        Socket.close();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(VendorHomeActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (IOException closeException) {
                    }
                    return null;
                }

                // Do work to manage the connection (in a separate thread)
                manageConnectedSocket(Socket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            connect();
        } else {
            Toast.makeText(this, "Bluetooth Should be enabled to Connect", Toast.LENGTH_SHORT).show();
        }
    }


}

