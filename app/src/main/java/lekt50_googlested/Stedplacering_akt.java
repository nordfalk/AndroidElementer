package lekt50_googlested;

import android.Manifest;
import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;


public class Stedplacering_akt extends AppCompatActivity implements View.OnClickListener {

  static Stedplacering_akt instans;

  Button knap1, knap2, knap3, knap4, knap5;
  TextView tv;

  FusedLocationProviderClient fusedLocationProviderClient;

  PendingIntent geofencePi;
  GeofencingClient geofencingClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);
    tv = new TextView(this);
    tv.setText("Stedplacering. \n");
    tl.addView(tv);

    knap1 = new Button(this);
    knap1.setText("Start logning af sted");
    tl.addView(knap1);

    knap2 = new Button(this);
    knap2.setText("Præcis stedbestemmelse");
    tl.addView(knap2);

    knap3 = new Button(this);
    knap3.setText("Stop stedplacering");
    tl.addView(knap3);


    Intent intent = new Intent(this, GeofenceIntentService.class);
    // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
    // calling addGeofences() and removeGeofences().
    geofencePi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    knap4 = new Button(this);
    knap4.setText("Start geofence her");
    tl.addView(knap4);

    knap5 = new Button(this);
    knap5.setText("Stop geofencing");
    tl.addView(knap5);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);
    knap4.setOnClickListener(this);
    knap5.setOnClickListener(this);

    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    geofencingClient = LocationServices.getGeofencingClient(this);

    TekstTilTale.instans(this);
    instans = this;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    instans = null;
  }

  void log(String s) {
    Log.d(s);
    tv.append("\n"+s + "\n");
  }

  @Override
  public void onClick(View v) {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1234);
      return;
    }
    if (v == knap1) {
      LocationRequest locationRequest = LocationRequest.create();
      locationRequest.setSmallestDisplacement(50); // 50 meter
      locationRequest.setInterval(1000 * 60 * 10); // 10 min
      locationRequest.setFastestInterval(1000 * 60 * 3); // 3 min
      locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
      log("Anmoder om " + locationRequest);
      fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }
    if (v==knap2) {
      LocationRequest locationRequest = LocationRequest.create();
      locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
      locationRequest.setExpirationDuration(10000);
      locationRequest.setNumUpdates(1);
      log("Anmoder om "+locationRequest);
      fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }
    if (v==knap3) {
      fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
    if (v==knap4) {
      fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location her) {
          sætGeofence(her);
        }
      });
    }
    if (v==knap5) {
      geofencingClient.removeGeofences(geofencePi);
    }
  }

  @SuppressLint("MissingPermission")
  private void sætGeofence(Location her) {
    ArrayList<Geofence> geofenceliste = new ArrayList<Geofence>();
    geofenceliste.add(new Geofence.Builder()
            .setCircularRegion(her.getLatitude(), her.getLongitude(), 100) // 100 meter
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_ENTER)
            .setExpirationDuration(1000 * 60 * 60 * 2) // 2 timer
            .setRequestId("100meter")
            .build());
    log("Anmoder om " + geofenceliste);

    geofencingClient.addGeofences(new GeofencingRequest.Builder().addGeofences(geofenceliste).build(), geofencePi)
            .addOnSuccessListener(this, new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                log("addGeofences onSuccess");
                TekstTilTale.instans(null).tal("Gå nu 100 meter væk");
              }
            });

  }

  LocationCallback locationCallback = new LocationCallback() {
    @Override
    public void onLocationResult(LocationResult locationResult) {
      for (Location l : locationResult.getLocations()) {
        log("onLocationChanged( " + l);
        TekstTilTale.instans(null).tal("ny placering registreret inden for " + (int) l.getAccuracy() + " meter");
      }
    };
  };

}
