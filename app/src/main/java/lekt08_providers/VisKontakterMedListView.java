package lekt08_providers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class VisKontakterMedListView extends Activity implements OnItemClickListener {

  private TextView textView;
  private ListView listView;
  private SimpleCursorAdapter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    textView = new TextView(this);

    listView = new ListView(this);
    listView.addHeaderView(textView);
    listView.setOnItemClickListener(this);

    setContentView(listView);
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
    String[] kolonnner = {Contacts._ID, Contacts.DISPLAY_NAME, Email.DATA};

    Cursor cursor = getContentResolver().query(Email.CONTENT_URI, kolonnner,
            Contacts.IN_VISIBLE_GROUP + " = '1'", null, Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
    startManagingCursor(cursor); // Lad cursoren følge aktivitetens livscyklus

    adapter = new SimpleCursorAdapter(this, R.layout.lekt04_listeelement, cursor,
            // Disse kolonner i cursoren...
            new String[]{Contacts.DISPLAY_NAME, Email.DATA},
            // ... skal afbildes over i disse views i res/layout/lekt04_listeelementelement.xml
            new int[]{R.id.listeelem_overskrift, R.id.listeelem_beskrivelse}
    );

    textView.append("Der er " + cursor.getCount() + " kontakter på telefonen.");
    listView.setAdapter(adapter);
  }

  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    position--; // pga addHeaderView(textView)
    if (position > 0) {
      Cursor cursor = adapter.getCursor();
      cursor.moveToPosition(position);
      String navn = cursor.getString(1); // Contacts.DISPLAY_NAME
      Toast.makeText(this, navn, Toast.LENGTH_LONG).show();
    }
  }
}
