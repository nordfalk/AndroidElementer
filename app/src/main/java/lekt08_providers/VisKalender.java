/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lekt08_providers;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Date;

/**
 * @author Jacob Nordfalk
 */
public class VisKalender extends AppCompatActivity {

  private TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    textView = new TextView(this);
    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(textView);
    setContentView(scrollView);

    visKalender();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    visKalender();
  }

  private void visKalender() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 1234);
      Toast.makeText(this, "Bruger skal godkende tilladelser først", Toast.LENGTH_LONG).show();
      new Exception().printStackTrace();
      return;
    }
    textView.append("Herunder kommmer kalender\n\n");
    ContentResolver cr = getContentResolver();

    Uri uri = Events.CONTENT_URI;
    textView.append("uri=" + uri + "\n\n");
    String[] kolonnner = { Events._ID, Events.ORGANIZER, Events.DTSTART, Events.TITLE};
    textView.append("SELECT " + Arrays.asList(kolonnner) + " FROM " + uri);
    Cursor cursor = cr.query(uri, kolonnner, null, null, null);

    textView.append("\n\nDer er " + cursor.getCount() + " kalenderaftaler på telefonen:\n");
    while (cursor.moveToNext()) {
      String id = cursor.getString(0);
      String arrangør = cursor.getString(1);
      Date tid = new Date(cursor.getLong(2));
      String titel = cursor.getString(3);
      textView.append("\n\n" + id + " " + arrangør + "  " + tid + "  " + titel);
    }
    cursor.close();
  }
}
