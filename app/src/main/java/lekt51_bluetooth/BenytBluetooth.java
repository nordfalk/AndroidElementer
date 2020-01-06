package lekt51_bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * @author Jacob Nordfalk
 */
public class BenytBluetooth extends AppCompatActivity implements OnClickListener {

  BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
  ArrayList<BluetoothDevice> fundneEnheder = new ArrayList<>();
  Button knap1, knap2, knap3, knap4;
  TextView logTv;

  BroadcastReceiver reciever = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
      fundneEnheder.add(bluetoothDevice);
      logTv.append("\nNy enhed fundet: "+bluetoothDevice.getName()+"\n"+bluetoothDevice.getAddress()+"\n\n");
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);
    logTv = new TextView(this);
    logTv.setText("Allerede parrede enheder:\n");

    Set<BluetoothDevice> parredeEnheder = bluetoothAdapter.getBondedDevices();

    logTv.append(parredeEnheder.toString());
    tl.addView(logTv);

    knap1 = new Button(this);
    knap1.setText("Bed om tilladelser");
    tl.addView(knap1);

    knap2 = new Button(this);
    knap2.setText("Søg efter ikke-parrede enheder");
    tl.addView(knap2);

    knap3 = new Button(this);
    knap3.setText("Par en fundet enhed");
    tl.addView(knap3);

    knap4 = new Button(this);
    knap4.setText("Stop detektering");
    tl.addView(knap4);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);
    knap4.setOnClickListener(this);
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
  }


  @Override
  public void onClick(View hvadBlevDerKlikketPå) {
    if (hvadBlevDerKlikketPå == knap1) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 12345);
    } else if (hvadBlevDerKlikketPå == knap2) {
      bluetoothAdapter.startDiscovery();
      registerReceiver(reciever, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    } else if (hvadBlevDerKlikketPå == knap3) {
      ArrayList<String> navne = new ArrayList<>();
      for (BluetoothDevice enhed : fundneEnheder) navne.add(enhed.getName()+"/"+enhed.getAddress());

      AlertDialog dialog = new AlertDialog.Builder(this)
              .setTitle("Vælg en enhed")
              .setItems(navne.toArray(new String[0]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  BluetoothDevice enhed = fundneEnheder.get(which);
                  try {
                    UUID uuid = enhed.getUuids()[0].getUuid();
                    BluetoothSocket bts = enhed.createRfcommSocketToServiceRecord(uuid);
                    bts.connect();
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                  logTv.append("\nForbundet til "+enhed+"!!!!");


                }
              }).show();


    } else if (hvadBlevDerKlikketPå == knap4) {
      unregisterReceiver(reciever);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    logTv.append("\nonRequestPermissionsResult: "+ Arrays.toString(permissions)+ Arrays.toString(grantResults)+"\n");
  }
}
