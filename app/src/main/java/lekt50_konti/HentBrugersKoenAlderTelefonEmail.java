package lekt50_konti;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import static lekt50_konti.Autorisering_akt.læsStrengOgLuk;

/**
 * Denne aktivitet henter personlige oplysninger for brugeren - hans fødselsdato og køn
Inden den virker for dig skal du gå til
 https://developers.google.com/identity/sign-in/android/start-integrating

Og køre
keytool -exportcert -keystore .android/debug.keystore -list -v

og taste SHA1 og pakkenavn ind i API konsollen
 */
public class HentBrugersKoenAlderTelefonEmail extends AppCompatActivity implements OnClickListener {
  private TextView logTv;
  private Button knap;

  private GoogleSignInClient googleSignInClient;

  private Account account;

  public static final String USER_BIRTHDAY_READ = "https://www.googleapis.com/auth/user.birthday.read";
  public static final String USER_PHONENUMBERS_READ = "https://www.googleapis.com/auth/user.phonenumbers.read";
  public static final String USERINFO_EMAIL = "https://www.googleapis.com/auth/userinfo.email";
  public static final String USERINFO_PROFILE = "https://www.googleapis.com/auth/userinfo.profile";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tableLayout = new TableLayout(this);
    knap = new Button(this);
    knap.setOnClickListener(this);
    tableLayout.addView(knap);

    logTv = new TextView(this);
    logTv.setGravity(Gravity.BOTTOM);
    logTv.setMovementMethod(new ScrollingMovementMethod());
    tableLayout.addView(logTv);
    setContentView(tableLayout);

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(new Scope(USERINFO_PROFILE))
            .requestScopes(new Scope(USERINFO_EMAIL))
            .requestScopes(new Scope(USER_BIRTHDAY_READ))
            .requestScopes(new Scope(USER_PHONENUMBERS_READ))
            .requestEmail()
            .build();

    googleSignInClient = GoogleSignIn.getClient(this, gso);


    // Check for existing Google Sign In account, if the user is already signed in
    // the GoogleSignInAccount will be non-null.
    //GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
    //account = googleSignInAccount.getAccount();
    //if (googleSignInAccount != null) {
    //  log("googleSignInAccount = "+googleSignInAccount.toJson());
    //}
    opdaterKnap();
  }

  private void opdaterKnap() {
    if (account != null) {
      log("Logget ind med " + account);
      knap.setText("Log ud og prøv igen");
    } else {
      log("Logget ud");
      knap.setText("Log ind");
    }
  }

  private void log(final String str) {
    Log.d(getClass().getSimpleName(), str);
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        logTv.append("\n");
        logTv.append(str);
        logTv.append("\n");
      }
    });
  }


  @Override
  public void onClick(View view) {
    if (account!=null) {
      googleSignInClient.signOut();
      account = null;
      opdaterKnap();
    } else {
      startActivityForResult(googleSignInClient.getSignInIntent(), 1234);
    }
  }


  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    log("onActivityResult req="+requestCode+" res="+resultCode+" data="+data);

    GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
    log("googleSignInAccount = "+googleSignInAccount);
    account = googleSignInAccount.getAccount();
    opdaterKnap();

    if (account!=null) {
      new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] objects) {
          // hentProfildataMedPeopleAPIBg();
          hentProfildataBg();
          return null;
        }
      }.execute();
    }
  }


  private void hentProfildataBg() {

    try {
      String token = GoogleAuthUtil.getToken(getApplicationContext(), account, "oauth2: " +USERINFO_PROFILE+" "+USER_PHONENUMBERS_READ+" "+USERINFO_EMAIL+" "+USER_BIRTHDAY_READ);
      log("token=\n"+token);

      URL url = new URL("https://people.googleapis.com/v1/people/me?"
              +"personFields=genders,birthdays,phoneNumbers,emailAddresses"
              +"&access_token=" + token);
      log("Henter "+url);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      int sc = con.getResponseCode();
      if (sc == 200) {
        InputStream is = con.getInputStream();
        JSONObject profile = new JSONObject(læsStrengOgLuk(is));
        log("Fik:" + profile.toString(2));
        log("Køn: "+profile.opt("genders"));
        log("Fødelsedag: "+profile.opt("birthdays"));
        log("Telefon: "+profile.opt("phoneNumbers"));
        log("Email: "+profile.opt("emailAddresses"));
      } else if (sc == 401) {
        GoogleAuthUtil.clearToken(this, token);
        log("Server auth fejl, prøv igen\n" + læsStrengOgLuk(con.getErrorStream()));
      } else {
        log("Serverfejl: " + sc);
      }


    } catch (UserRecoverableAuthException recoverableException) {
      log("onRecoverableAuthException" + recoverableException);
      startActivityForResult(recoverableException.getIntent(), 1234);
    } catch (Exception e) {
      e.printStackTrace();
      log(e.getMessage());
    }
  }

/*
Hvis du vil bruge klientbibliotekerne til Google People API (https://developers.google.com/people/)
skal følgende med i build.gradle

    implementation 'com.google.api-client:google-api-client-android:1.22.0'
    implementation 'com.google.apis:google-api-services-people:v1-rev139-1.22.0'

Bemærk - din app fylder 1.3 MB mere med disse biblioteker

Du kan så bruge koden herunder i stedet.

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;

  private void hentProfildataMedPeopleAPIBg() {
    try {
      HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
      JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
      GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
              getApplicationContext(),
              Arrays.asList(USERINFO_PROFILE, USER_PHONENUMBERS_READ, USERINFO_EMAIL, USER_BIRTHDAY_READ))
              .setSelectedAccount(account);
      String tok = credential.getToken();
      log("tok=\n"+tok);

      PeopleService service = new PeopleService.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).build();

      Person profile = service.people().get("people/me").setPersonFields("genders,birthdays,phoneNumbers,emailAddresses").execute();
      log(profile.toPrettyString());

      log("Køn: "+profile.getGenders());
      log("Fødelsedag: "+profile.getBirthdays());
      log("Telefon: "+profile.getPhoneNumbers());
      log("Email: "+profile.getEmailAddresses());

    } catch (UserRecoverableAuthIOException recoverableException) {
      log("onRecoverableAuthException" + recoverableException);
      startActivityForResult(recoverableException.getIntent(), 1234);
    } catch (Exception e) {
      e.printStackTrace();
      log(e.getMessage());
    }
  }
  */
}
