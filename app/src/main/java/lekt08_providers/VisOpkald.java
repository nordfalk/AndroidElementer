/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lekt08_providers;

import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog.Calls;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.widget.ScrollView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Jacob Nordfalk
 */
public class VisOpkald extends AppCompatActivity {

  private TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    textView = new TextView(this);
    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(textView);
    setContentView(scrollView);

    textView.append("Bemærk: Eksemplet virker ikke i versionen installeret fra Google Play. \n" +
            "Se https://android-developers.googleblog.com/2019/01/reminder-smscall-log-policy-changes.html\n");

    visOpkald();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    visOpkald();
  }

  private void visOpkald() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
      textView.append("Bruger skal godkende tilladelser først\n\n");
      if (textView.getText().length()>1000) return; // undgå uendelig løkke i fald tilladelserne ikke er i manifest
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 1234);
      return;
    }
    textView.append("Herunder kommmer opkald\n");

    String[] kolonner = {Calls.DATE, Calls.TYPE, Calls.NUMBER, Calls.CACHED_NAME, Calls.DURATION};
    String where = Calls.DATE + " >= " + (System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 7); // sidste 7 dage

    ContentResolver cr = getContentResolver();
    //Cursor c = cr.query(Calls.CONTENT_URI, kolonner, where, null, Calls.DATE);
    Cursor c = cr.query(Uri.parse("content://call_log/calls"), kolonner, where, null, Calls.DATE);
    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    textView.append("\nkolonner = " + Arrays.asList(kolonner)); // date, number, name, duration
    textView.append("\nwhere = " + where); // date >= 1304737637646
    textView.append("\nURI = " + Calls.CONTENT_URI); // content://call_log/calls
    textView.append("\n\nDer er "+c.getCount()+" rækker:\n\n");
    while (c.moveToNext()) {
      textView.append(df.format(new Date(c.getLong(0))) + "  " + c.getInt(1) + " " + c.getString(2) + "  " + c.getString(3) + "  " + c.getInt(4) + "  " + "\n");
    }
    c.close();
  }
}
