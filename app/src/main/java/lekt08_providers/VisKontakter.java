/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lekt08_providers;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

/**
 * @author Jacob Nordfalk
 */
public class VisKontakter extends Activity {

  private TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    textView = new TextView(this);
    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(textView);
    setContentView(scrollView);

    visKontakter();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    visKontakter();
  }

  private void visKontakter() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 4242);
      Toast.makeText(this, "Bruger skal godkende tilladelser først", Toast.LENGTH_LONG).show();
      return;
    }
    textView.append("Herunder kommmer kontakters navn og info fra telefonbogen\n\n");
    ContentResolver cr = getContentResolver();

    Uri uri = Contacts.CONTENT_URI;
    textView.append("uri=" + uri + "\n\n");
    String[] kolonnner = {Contacts._ID, Contacts.DISPLAY_NAME, Contacts.PHOTO_ID};
    textView.append("SELECT " + Arrays.asList(kolonnner) + " FROM " + uri);
    Cursor cursor = cr.query(uri, kolonnner, null, null, null);

    textView.append("\n\nDer er " + cursor.getCount() + " kontakter på telefonen:\n");
    while (cursor.moveToNext()) {
      String id = cursor.getString(0);
      String navn = cursor.getString(1);
      String foto = cursor.getString(2);
      textView.append("\n" + id + " " + navn + "  " + foto);
    }
    cursor.close();
  }
}
