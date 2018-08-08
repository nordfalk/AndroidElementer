package lekt02_intents;

import android.Manifest;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Demonstrerer hvordan man benytter intents til at vælg en
 * kontaktperson, et billede eller tage et billede med kameraet
 *
 * @author Jacob Nordfalk
 */
public class BenytIntentsMedResultat extends AppCompatActivity implements OnClickListener {

  Button vælgKontakt, vælgGoogleKonto, vælgKonto, tagBillede, dokumentation;
  TextView resultatTextView;
  LinearLayout resultatHolder;
  private int VÆLG_KONTAKT = 1111;
  private int TAG_BILLEDE = 2222;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    vælgKontakt = new Button(this);
    vælgKontakt.setText("Vælg kontakt");
    vælgKontakt.setOnClickListener(this);
    tl.addView(vælgKontakt);

    vælgGoogleKonto = new Button(this);
    vælgGoogleKonto.setText("Vælg Google-konto");
    vælgGoogleKonto.setOnClickListener(this);
    tl.addView(vælgGoogleKonto);

    vælgKonto = new Button(this);
    vælgKonto.setText("Alle typer konti");
    vælgKonto.setOnClickListener(this);
    tl.addView(vælgKonto);

    tagBillede = new Button(this);
    tagBillede.setText("Tag billede med kameraet");
    tagBillede.setOnClickListener(this);
    tl.addView(tagBillede);

    dokumentation = new Button(this);
    dokumentation.setText("Dokumentation om intents");
    dokumentation.setOnClickListener(this);
    tl.addView(dokumentation);

    resultatHolder = new LinearLayout(this);
    tl.addView(resultatHolder);

    resultatTextView = new TextView(this);
    tl.addView(resultatTextView);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);
  }

  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  public void onClick(View v) {
    try {
      if (v == vælgKontakt) {
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, VÆLG_KONTAKT);

      } else if (v == vælgGoogleKonto) {
        Intent i = AccountManager.newChooseAccountIntent(null, null, new String[]{"com.google"}, false, null, null, null, null);
        startActivityForResult(i, 0);

      } else if (v == vælgKonto) {
        Intent i = AccountManager.newChooseAccountIntent(null, null, null, false, null, null, null, null);
        startActivityForResult(i, 0);

      } else if (v == tagBillede) {
        // Bemærk at jeg måtte have android:configChanges="orientation" for at aktiviteten
        // ikke blev vendt og jeg mistede billedet. I et rigtigt ville jeg forsyne mine views med
        // ID'er så deres indhold overlevede at skærmen skiftede orientering
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, TAG_BILLEDE);

      } else {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://developer.android.com/guide/components/intents-common.html")));
      }
    } catch (Throwable e) {
      Toast.makeText(this, "Denne telefon mangler en funktion:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
      e.printStackTrace();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    resultatTextView.setText("onActivityResult requestCode="+requestCode + " resultCode=" + resultCode + "\ndata=" + data);
    if (data!=null) {
      data.removeExtra("et eller andet"); // Gennemtving udpakning af at extra bundle
      resultatTextView.append("\nextras = "+data.getExtras());
      resultatTextView.append("\ndataURI = "+data.getData());
    }
    System.out.println(resultatTextView.getText());

    resultatHolder.removeAllViews();
    ContentResolver cr = getContentResolver();

    if (resultCode == Activity.RESULT_OK) {
      try {
        if (requestCode == VÆLG_KONTAKT) {
          Uri kontaktData = data.getData();
          Cursor c = cr.query(kontaktData, null, null, null, null);
          while (c.moveToNext()) {
            for (int i = 0; i < c.getColumnCount(); i++) {
              resultatTextView.append("\n" + c.getColumnName(i) + ": " + c.getString(i));
//								+ c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            }

            // Har vi adgang til at vise billedet?
            // Fra Android 6 (targetSdkVersion 23) og frem skal brugeren spørges om lov først
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
              Snackbar.make(resultatTextView, "Giv tilladelse og start forfra (vælg en kontakt igen for at se billedet)", Snackbar.LENGTH_INDEFINITE).show();
              ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},1234);
            } else {
              // Ja - vis det
              Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                      c.getInt(c.getColumnIndex(ContactsContract.Contacts._ID)));
              ImageView iv = new ImageView(this);
              InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
              if (input != null) {
                iv.setImageBitmap(BitmapFactory.decodeStream(input));
                input.close();
              } else {
                iv.setImageResource(android.R.drawable.ic_menu_gallery);
              }
              resultatHolder.addView(iv);
            }
          }
          c.close();
        } else if (requestCode == TAG_BILLEDE) {
          ImageView iv = new ImageView(this);
          Bitmap bmp = (Bitmap) data.getExtras().get("data");
          System.out.println("TAG_BILLEDE bmp = "+ bmp.getWidth());
          iv.setImageBitmap(bmp);
          resultatHolder.addView(iv);
        }

      } catch (Exception e) {
        e.printStackTrace();
        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
      }
    }
  }
}
