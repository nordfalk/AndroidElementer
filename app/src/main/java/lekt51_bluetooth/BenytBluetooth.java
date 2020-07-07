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

import com.google.android.material.snackbar.Snackbar;

/**
 * @author Jacob Nordfalk
 */
public class BenytBluetooth extends AppCompatActivity implements OnClickListener {

  BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
  ArrayList<BluetoothDevice> fundneEnheder = new ArrayList<>();
  Button knap1, knap2, knap3, knap4;
  TextView logTv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);
    logTv = new TextView(this);
    tl.addView(logTv);
    if (bluetoothAdapter==null) {
      logTv.setText("\nDenne enhed har ikke Bluetooth\n");
    } else {
      if (!bluetoothAdapter.isEnabled()) {
          Snackbar.make(logTv, "Bluetooth er slukket", Snackbar.LENGTH_INDEFINITE)
                  .setAction("Tænd", view -> startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1234))
                  .show();
      }


      logTv.setText("Allerede parrede enheder:\n");

      Set<BluetoothDevice> parredeEnheder = bluetoothAdapter.getBondedDevices();

      logTv.append(parredeEnheder.toString());

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
    }

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
    unregisterReceiver(btEnhedFundetReceiver);
  }


  BroadcastReceiver btEnhedFundetReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
      fundneEnheder.add(bluetoothDevice);
      int  rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
      logTv.append("\nNy enhed fundet:"+bluetoothDevice.getName()+
              "\n"+bluetoothDevice.getAddress()+"  "+rssi+" dB\n\n");
    }
  };


  @Override
  public void onClick(View klikPåHvad) {
    if (klikPåHvad == knap1) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 12345);

    } else if (klikPåHvad == knap2) {

      registerReceiver(btEnhedFundetReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
      bluetoothAdapter.startDiscovery();

    } else if (klikPåHvad == knap3) {
      ArrayList<String> navne = new ArrayList<>();
      for (BluetoothDevice enhed : fundneEnheder) navne.add(enhed.getName()+"/"+enhed.getAddress());

      AlertDialog dialog = new AlertDialog.Builder(this)
              .setTitle("Vælg en enhed")
              .setItems(navne.toArray(new String[0]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  BluetoothDevice enhed = fundneEnheder.get(which);
                  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) try {
                    UUID uuid = enhed.getUuids()[0].getUuid();
                    BluetoothSocket bts = enhed.createRfcommSocketToServiceRecord(uuid);
                    bts.connect();
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                  logTv.append("\nForbundet til "+enhed+"!!!!");


                }
              }).show();


    } else if (klikPåHvad == knap4) {
      unregisterReceiver(btEnhedFundetReceiver);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    logTv.append("\nonRequestPermissionsResult: "+ Arrays.toString(permissions)+ Arrays.toString(grantResults)+"\n");
  }
}
