package lekt50_konti;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;

import java.io.IOException;
import java.util.Arrays;

/**
 * Denne aktivitet henter personlige oplysninger for brugeren - hans fødselsdato og køn
Inden den virker for dig skal du gå til
 https://developers.google.com/identity/sign-in/android/start-integrating

Og køre
keytool -exportcert -keystore .android/debug.keystore -list -v
 SHA1: 0A:89:29:BA:67:9C:98:6B:B6:4C:EF:6D:0F:E3:AC:6B:49:44:14:D5


 API konsol (du skal have dit eget projekt):
 https://console.developers.google.com/?authuser=0&project=androidelementer-1533318220622

 */
public class Autorisering2_akt extends AppCompatActivity implements OnClickListener {
  private TextView logTv;
  private Button knap;
  private ScrollView scrollView;

  private static final String TAG = "RestApiActivity";

  public static final String USER_BIRTHDAY_READ = "https://www.googleapis.com/auth/user.birthday.read";
  public static final String USER_PHONENUMBERS_READ = "https://www.googleapis.com/auth/user.phonenumbers.read";
  public static final String USERINFO_EMAIL = "https://www.googleapis.com/auth/userinfo.email";
  public static final String USERINFO_PROFILE = "https://www.googleapis.com/auth/userinfo.profile";


  // Request codes
  private static final int RC_SIGN_IN = 9001;
  private static final int RC_RECOVERABLE = 9002;

  // Global instance of the HTTP transport
  private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();

  // Global instance of the JSON factory
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  private GoogleSignInClient googleSignInClient;

  private Account account;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);
    knap = new Button(this);
    knap.setOnClickListener(this);
    tl.addView(knap);

    logTv = new TextView(this);
    tl.addView(logTv);

    scrollView = new ScrollView(this);
    scrollView.addView(tl);
    setContentView(scrollView);

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(new Scope(USERINFO_PROFILE))
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

  private void log(String str) {
    Log.d(TAG, str);
    logTv.append("\n");
    logTv.append(str);
    logTv.append("\n");
    scrollView.scrollTo(0, Integer.MAX_VALUE);
  }


  @Override
  public void onClick(View view) {
    if (account==null) {
      startActivityForResult(googleSignInClient.getSignInIntent(), RC_SIGN_IN);
    } else {
      googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
          log("Logget ud");
        }
      });
    }
  }


  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    log("onActivityResult req="+requestCode+" res="+resultCode+" data="+data);

    if (requestCode == RC_SIGN_IN) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

      try {
        GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
        log("googleSignInAccount = "+googleSignInAccount);
        account = googleSignInAccount.getAccount();

        hentProfildata();
      } catch (ApiException e) {
        android.util.Log.w(TAG, "handleSignInResult:error", e);
        account = null;
      }
      opdaterKnap();
    }

    // Handling a user-recoverable auth exception
    if (requestCode == RC_RECOVERABLE) {
      if (resultCode == RESULT_OK) {
        hentProfildata();
      }
    }
  }

  private void hentProfildata() {

    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Henter info, vent....");
    progressDialog.setIndeterminate(true);
    progressDialog.show();
/* https://people.googleapis.com/v1/people/me?personFields=birthdays,phoneNumbers,genders
Bearer ya29.GlsNBtG9MQAcLpimByEvIXePFD4-8SNm6KR3cjOKgHZfwNcB9-N-fQeixldqFgQJ35ponBdgpw1u9Wc5ggj2cI7X5NCRDoGtMUnbfoRhUw7pwJbwi7y_awsFk8zD

https://people.googleapis.com/v1/people/me?personFields=birthdays,phoneNumbers,genders&access_token=ya29.GlsNBn0FKg4gnsfHsDB4lsibN8m6jncqLifQhefbE7BROOTrtuRjw2zvUvFG4aaqaFxeV4xaeYN3nB0pz8-3lcZOwDqGpN9wttfefUBlRFqYl7jphCzZtiOITjQu

 */

    new AsyncTask() {
      @Override
      protected Object doInBackground(Object[] objects) {
        try {
          GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                  getApplicationContext(),
                  Arrays.asList(USERINFO_PROFILE, USER_PHONENUMBERS_READ, USERINFO_EMAIL, USER_BIRTHDAY_READ))
                  .setSelectedAccount(account);

          String tok = credential.getToken();
          log("tok=\n"+tok);

          PeopleService service = new PeopleService.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).build();

          Person profile = service.people().get("people/me").setPersonFields("birthdays,phoneNumbers,genders").execute();
          android.util.Log.d(TAG, " PROFFF " + profile);
          android.util.Log.d(TAG, " PROFFF " + profile.toPrettyString());
//                profile.getBirthdays().get(0).toString();


          return profile.getGenders() + "\nx " + profile.getBirthdays().get(0).getDate();

        } catch (UserRecoverableAuthIOException recoverableException) {
          log("onRecoverableAuthException" + recoverableException);
          startActivityForResult(recoverableException.getIntent(), RC_RECOVERABLE);
        } catch (Exception e) {
          e.printStackTrace();
          log(e.getMessage());
        }

        return null;
      }

      @Override
      protected void onPostExecute(Object resultat) {
        progressDialog.hide();
        log("Resultat: " + resultat);
      }
    }.execute();
  }
}
