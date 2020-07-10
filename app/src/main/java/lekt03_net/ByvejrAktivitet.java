/*
 * /dmi/femdgn_dk.png
 * /dmi/index/danmark/solvarsel.htm
 * /dmi/varsel.xml  - DMI - varsel om farligt vejr
 * /dmi/rss-nyheder
 */
// https://servlet.dmi.dk/byvejr/servlet/byvejr?valgtBy=2500&tabel=dag10_14
// For kun at se denne proces' output:
// adb logcat -v tag ActivityManager:I Vejret:V System.err:V *:S
// Fremtidig geokodning bliver med
// http://www.geonames.org/postal-codes/postal-codes-denmark.html
package lekt03_net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Simpel aktivitet der viser byvejret i Ballerup
 *
 * @author Jacob Nordfalk
 */
public class ByvejrAktivitet extends AppCompatActivity implements OnClickListener {

  String valgtPostNr = "2750";
  ImageView imageView_dag1, imageView_dag3_9, imageView_dag10_14;
  EditText editText_postnr;

  Executor bgThread = Executors.newSingleThreadExecutor(); // håndtag til en baggrundstråd
  Handler uiThread = new Handler();                        // håndtag til forgrundstråden

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    LinearLayout linearLayout = new LinearLayout(this);
    linearLayout.setOrientation(LinearLayout.VERTICAL);

    // række hvor postnr kan indtastes
    LinearLayout række = new LinearLayout(this);
    TextView textView = new TextView(this);
    textView.setText("Vælg postnummer");
    række.addView(textView);

    // indlæs foretrukken postnummer fra PreferenceManager
    valgtPostNr = PreferenceManager.getDefaultSharedPreferences(this)
            .getString("foretrukkenPostNr", "2750");
    editText_postnr = new EditText(this);
    editText_postnr.setText(valgtPostNr);
    række.addView(editText_postnr);

    Button button_postnr = new Button(this);
    button_postnr.setText("OK");
    række.addView(button_postnr);

    // når bruger trykker OK skal det indlæses
    button_postnr.setOnClickListener(this);

    // tilføj rækken
    linearLayout.addView(række);

    // første billede
    imageView_dag1 = new ImageView(this);
    //imageView_dag1.setScaleType(ImageView.ScaleType.FIT_XY);
    imageView_dag1.setAdjustViewBounds(true);
    linearLayout.addView(imageView_dag1);

    // en tekst
    textView = new TextView(this);
    textView.setText("... med 3 til 9-døgnsudsigt...");
    linearLayout.addView(textView);

    // andet billede
    imageView_dag3_9 = new ImageView(this);
    imageView_dag3_9.setAdjustViewBounds(true);
    linearLayout.addView(imageView_dag3_9);

    // mere tekst
    textView = new TextView(this);
    textView.setText("... og en 10-14-døgnsudsigt.");
    linearLayout.addView(textView);

    // tredie billede
    imageView_dag10_14 = new ImageView(this);
    imageView_dag10_14.setAdjustViewBounds(true);
    linearLayout.addView(imageView_dag10_14);


    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(linearLayout);
    setContentView(scrollView);

    startHentBilleder();
  }

  @Override
  public void onClick(View v) {
    valgtPostNr = editText_postnr.getText().toString();
    // gem foretrukkent postnummer i PreferenceManager
    PreferenceManager.getDefaultSharedPreferences(this)
            .edit().putString("foretrukkenPostNr", valgtPostNr).apply();

    skjulTastatur(v);

    startHentBilleder();
  }


  public void skjulTastatur(View v) {
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
  }

  private void startHentBilleder() {
    bgThread.execute(() -> {
      try {
        Bitmap byvejr_dag1 = opretBitmapFraUrl("https://servlet.dmi.dk/byvejr/servlet/byvejr_dag1?by=" + valgtPostNr + "&mode=long");
        Bitmap byvejr_dag3_9 = opretBitmapFraUrl("https://servlet.dmi.dk/byvejr/servlet/byvejr?by=" + valgtPostNr + "&tabel=dag3_9");
        Bitmap byvejr_dag10_14 = opretBitmapFraUrl("https://servlet.dmi.dk/byvejr/servlet/byvejr?by=" + valgtPostNr + "&tabel=dag10_14");

        // Dette skal gøres i GUI-tråden
        uiThread.post(() -> {
          imageView_dag1.setImageBitmap(byvejr_dag1);
          // Sørg for at den er synlig på skærmen
          imageView_dag1.requestRectangleOnScreen(new Rect());
          imageView_dag3_9.setImageBitmap(byvejr_dag3_9);
          imageView_dag10_14.setImageBitmap(byvejr_dag10_14);
        });

      } catch (Exception ex) {
        ex.printStackTrace();
        uiThread.post(() -> {
          advarBruger("Kunne ikke få data fra DMI");
          advarBruger("" + ex);
        });
      }
    });
  }

  public static Bitmap opretBitmapFraUrl(String url) throws IOException {
    InputStream is = new URL(url).openStream();
    Bitmap bitmap = BitmapFactory.decodeStream(is);
    is.close();
    return bitmap;
  }

  private void advarBruger(String advarsel) {
    Log.w("Vejret", advarsel);
    Toast.makeText(this, advarsel, Toast.LENGTH_LONG).show();
  }
}
