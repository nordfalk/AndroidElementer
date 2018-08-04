package lekt50_konti;

import android.accounts.Account;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Scope;

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
 *
keytool -exportcert -keystore .android/debug.keystore -list -v
 SHA1: 0A:89:29:BA:67:9C:98:6B:B6:4C:EF:6D:0F:E3:AC:6B:49:44:14:D5

Gå til https://developers.google.com/identity/sign-in/android/start-integrating

 Client ID
 147436537597-f6ve2c728qe0rom2j2k8anqho99h87ca.apps.googleusercontent.com

 Client Secret
 MGO5q0KY_SPjiFTReHmaUV2E

 API konsol:
 https://console.developers.google.com/?authuser=0&project=androidelementer-1533318220622



 */
public class Autorisering2_akt extends AppCompatActivity implements OnClickListener {
  private TextView tv;
  private Spinner kontospinner;
  private Spinner adgangspinner;
  private Button knap;

  private Account[] konti;

  private String[] adgange = {
    "oauth2:https://www.googleapis.com/auth/userinfo.profile",
    "oauth2:https://www.googleapis.com/auth/userinfo.email",
    "oauth2:https://www.googleapis.com/auth/plus.login",
    "oauth2:https://www.googleapis.com/auth/plus.me",
    "people/me",
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

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(new Scope("https://www.googleapis.com/auth/contacts.readonly"))
            .requestProfile()
            .build();

    // Build a GoogleSignInClient with the options specified by gso.
    GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    // Check for existing Google Sign In account, if the user is already signed in
    // the GoogleSignInAccount will be non-null.
    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

    if (account==null) {
      Intent signInIntent = mGoogleSignInClient.getSignInIntent();
      startActivityForResult(signInIntent, 12345);
    } else {
      System.out.println(account.toJson());
    }


    TableLayout tl = new TableLayout(this);
    tv = new TextView(this);
    tl.addView(tv);

    if (konti.length==0) {
      tv.setText("Du skal først logge ind på en Google-konto på telefonen\n"+account);
//      GoogleAuthUtil.removeAccount(this, account);
    } else {
      tv.setText("Eksempel på at hente data på en bruger.\n\n" +
          "Spinner 1: Hvilken adgang app'en skal have (f.eks. profil):\n" +
                      Arrays.toString(adgange) + "\n\n" +
          "Spinner 2: Hvilken konto:\n" +
              Arrays.toString(konti) +
              "\n\nHerunder kommer log af kaldene der foretages:\n"

      );
      adgangspinner = new Spinner(this);
      adgangspinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, adgange));
      tl.addView(adgangspinner);

      kontospinner = new Spinner(this);
      kontospinner.setAdapter(new ArrayAdapter<Account>(this, android.R.layout.simple_spinner_item, konti));
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
      String kontonavn = konti[kontospinner.getSelectedItemPosition()].name;
      String adgang = adgange[adgangspinner.getSelectedItemPosition()].split(" ")[0];
      String token = GoogleAuthUtil.getToken(this, kontonavn, adgang);
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
        GoogleAuthUtil.invalidateToken(this, token);
        log("Server auth error, please try again\n" + læsStrengOgLuk(con.getErrorStream()));
      } else {
        log("Server returned the following error code: " + sc);
      }
    } catch (final GooglePlayServicesAvailabilityException gpe) {
      // GooglePlayServices mangler eller er ikke opdateret, vis dialog til at løse det
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          Dialog d = GooglePlayServicesUtil.getErrorDialog(gpe.getConnectionStatusCode(), Autorisering2_akt.this, 1001);
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