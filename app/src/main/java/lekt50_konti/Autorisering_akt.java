package lekt50_konti;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import lekt50_googlested.Log;

/**
 * XXXX forældet
 * Se https://developers.google.com/identity/sign-in/android/migration-guide


 * Aktivitet til at hente kontoinformation
 * Se https://developers.google.com/android/guides/http-auth
 */
public class Autorisering_akt extends AppCompatActivity implements OnClickListener {
  private TextView tv;
  private Spinner adgangspinner;
  private Button knap;

  private Account[] konto;

  private String[] adgange = {
    "oauth2:https://www.googleapis.com/auth/userinfo.profile",
    "oauth2:https://www.googleapis.com/auth/userinfo.email",
    "oauth2:https://www.googleapis.com/auth/plus.login",
    "oauth2:https://www.googleapis.com/auth/plus.me",
    "blogger",
    "youtube",
    "cl (Google kalender)",
    "jotspot (Google Sites)",
    "local (Google Maps)",
    "cp (Google Kontakter)",
    "mail (Gmail)",
    };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Find kontonavne
    AccountManager accountManager = AccountManager.get(this);
    konto = accountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE); // "com.google"

    TableLayout tl = new TableLayout(this);
    tv = new TextView(this);
    tl.addView(tv);

    if (konto.length==0) {
      tv.setText("Du skal først logge ind på en konto.");
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Intent intent = AccountManager.newChooseAccountIntent(null,null, null, null, null, null, null);
        startActivityForResult(intent, 1002);
        finish();
      }
    } else {
      tv.setText("Eksempel på at hente data på en bruger.\n\n" +
          "Spinner 1: Hvilken adgang app'en skal have (f.eks. profil):\n" +
                      Arrays.toString(adgange) + "\n\n" +
          "Spinner 2: Hvilken konto:\n" +
              Arrays.toString(konto) +
              "\n\nHerunder kommer log af kaldene der foretages:\n"

      );
      adgangspinner = new Spinner(this);
      adgangspinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, adgange));
      tl.addView(adgangspinner);

      TextView kontospinner = new TextView(this);
      kontospinner.setText( Arrays.toString(konto));
      tl.addView(kontospinner);

      knap = new Button(this);
      knap.setText("Hent info for den valgte adgang og konto");
      knap.setOnClickListener(this);
      tl.addView(knap);
    }

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);
  }

  @Override
  public void onClick(View v) {
    tv.setText("");
    startHentAuthToken();
  }

  public void log(final Object obj) {
    Log.d(obj.toString());
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        tv.append("\n\n" + obj);
      }
    });
  }

  protected void log(String msg, Exception e) {
    if (e != null) {
      e.printStackTrace();
      log(e.toString());
    }
    log(msg);
  }


  /**
   * Læser en inputstream som en stren og lukker strømmen bagefter
   */
  public static String læsStrengOgLuk(InputStream is) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    byte[] data = new byte[2048];
    int len = 0;
    while ((len = is.read(data, 0, data.length)) >= 0) {
      bos.write(data, 0, len);
    }
    is.close();
    return new String(bos.toByteArray(), "UTF-8");
  }


  private void hentAuthTokenBg() {
    try {
      String adgang = adgange[adgangspinner.getSelectedItemPosition()].split(" ")[0];
      String token = GoogleAuthUtil.getToken(this, konto[0], adgang);
      if (!adgang.contains("oauth2:")) {
        log("Her er token til "+adgang+": "+token);
        return;
      }

      URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + token);
      log("Henter "+url);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      int sc = con.getResponseCode();
      if (sc == 200) {
        InputStream is = con.getInputStream();
        JSONObject profile = new JSONObject(læsStrengOgLuk(is));
        log("Fik:" + profile.toString(2));
      } else if (sc == 401) {
        GoogleAuthUtil.clearToken(this, token);
        log("Server auth error, please try again\n" + læsStrengOgLuk(con.getErrorStream()));
      } else {
        log("Server returned the following error code: " + sc);
      }
    } catch (final GooglePlayServicesAvailabilityException gpe) {
      // GooglePlayServices mangler eller er ikke opdateret, vis dialog til at løse det
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          Dialog d = GooglePlayServicesUtil.getErrorDialog(gpe.getConnectionStatusCode(), Autorisering_akt.this, 1001);
          d.show();
        }
      });
    } catch (UserRecoverableAuthException userRecoverableException) {
      log("Brugeren skal logge ind", userRecoverableException);
      log("Starter aktivitet " + userRecoverableException.getIntent());
      startActivityForResult(userRecoverableException.getIntent(), 1001);
    } catch (Exception ex) {
      ex.printStackTrace();
      log(ex);
    }
  }

  private void startHentAuthToken() {
    knap.setEnabled(false);
    new AsyncTask() {
      @Override
      protected Object doInBackground(Object... params) {
        hentAuthTokenBg();
        return null;
      }

      @Override
      protected void onPostExecute(Object o) {
        knap.setEnabled(true);
      }
    }.execute();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 1001) {
      if (resultCode == RESULT_OK) {
        log("Prøver igen");
        startHentAuthToken(); // Prøv igen
        return;
      }
      if (resultCode == RESULT_CANCELED) {
        log("User rejected authorization.");
        return;
      }
      log("Unknown error, click the button again");
      return;
    }
    super.onActivityResult(requestCode, resultCode, data);
  }
}