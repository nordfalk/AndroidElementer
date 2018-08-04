package lekt50_konti;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
  private TextView tv;
  private Button knap;

  private static final String TAG = "RestApiActivity";

  // Scope for reading user's contacts
  private static final String CONTACTS_SCOPE = "https://www.googleapis.com/auth/contacts.readonly";

  /**
   * View your complete date of birth.
   */
  public static final String USER_BIRTHDAY_READ = "https://www.googleapis.com/auth/user.birthday.read";

  /**
   * View your phone numbers.
   */
  public static final String USER_PHONENUMBERS_READ = "https://www.googleapis.com/auth/user.phonenumbers.read";

  /**
   * View your email address.
   */
  public static final String USERINFO_EMAIL = "https://www.googleapis.com/auth/userinfo.email";

  /**
   * View your basic profile info.
   */
  public static final String USERINFO_PROFILE = "https://www.googleapis.com/auth/userinfo.profile";


  // Request codes
  private static final int RC_SIGN_IN = 9001;
  private static final int RC_RECOVERABLE = 9002;

  // Global instance of the HTTP transport
  private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();

  // Global instance of the JSON factory
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  private GoogleSignInClient mGoogleSignInClient;

  private Account mAccount;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Configure sign-in to request the user's ID, email address, basic profile,
    // and readonly access to contacts.
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(new Scope(CONTACTS_SCOPE))
            .requestScopes(new Scope(USERINFO_PROFILE))
            .requestEmail()
            .requestProfile()
            .build();

    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    // Check for existing Google Sign In account, if the user is already signed in
    // the GoogleSignInAccount will be non-null.
    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

    if (account == null) {
      Intent signInIntent = mGoogleSignInClient.getSignInIntent();
      startActivityForResult(signInIntent, RC_SIGN_IN);
    } else {
      System.out.println(account.toJson());
    }


    TableLayout tl = new TableLayout(this);
    tv = new TextView(this);
    tl.addView(tv);

    /*
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

    }
    */
    knap = new Button(this);
    knap.setText("Hent info for den valgte adgang og konto");
    knap.setOnClickListener(this);
    tl.addView(knap);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    if (requestCode == RC_SIGN_IN) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      handleSignInResult(task);
    }

    // Handling a user-recoverable auth exception
    if (requestCode == RC_RECOVERABLE) {
      if (resultCode == RESULT_OK) {
        getContacts();
      } else {
        Toast.makeText(this, "msg_contacts_failed", Toast.LENGTH_SHORT).show();
      }
    }
  }

  private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
    android.util.Log.d(TAG, "handleSignInResult:" + completedTask.isSuccessful());

    try {
      GoogleSignInAccount account = completedTask.getResult(ApiException.class);
      updateUI(account);

      // Store the account from the result
      mAccount = account.getAccount();

      // Asynchronously access the People API for the account
      getContacts();
    } catch (ApiException e) {
      android.util.Log.w(TAG, "handleSignInResult:error", e);

      // Clear the local account
      mAccount = null;

      // Signed out, show unauthenticated UI.
      updateUI(null);
    }
  }

  private void getContacts() {
    if (mAccount == null) {
      android.util.Log.w(TAG, "getContacts: null account");
      return;
    }

    final ProgressDialog mProgressDialog = new ProgressDialog(this);
    mProgressDialog.setMessage("R.string.loading");
    mProgressDialog.setIndeterminate(true);
    mProgressDialog.show();


    new AsyncTask() {
      @Override
      protected Object doInBackground(Object[] objects) {
        try {
          GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                  getApplicationContext(),
                  Arrays.asList(CONTACTS_SCOPE, USERINFO_PROFILE, USER_PHONENUMBERS_READ, USERINFO_EMAIL, USER_BIRTHDAY_READ));
          credential.setSelectedAccount(mAccount);

          PeopleService service = new PeopleService.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                  .setApplicationName("Google Sign In Quickstart")
                  .build();

          Person profile = service.people().get("people/me").setPersonFields("birthdays,phoneNumbers,genders").execute();
          android.util.Log.d(TAG, " PROFFF " + profile);
          android.util.Log.d(TAG, " PROFFF " + profile.toPrettyString());
//                profile.getBirthdays().get(0).toString();


          return profile.getGenders() + "\nx " + profile.getBirthdays();

        } catch (UserRecoverableAuthIOException recoverableException) {
          android.util.Log.w(TAG, "onRecoverableAuthException", recoverableException);
          startActivityForResult(recoverableException.getIntent(), RC_RECOVERABLE);
        } catch (IOException e) {
          android.util.Log.w(TAG, "getContacts:exception", e);
        }

        return null;
      }

      @Override
      protected void onPostExecute(Object resultat) {
        mProgressDialog.hide();
        tv.setText("" + resultat);
      }
    }.execute();
  }


  @Override
  public void onClick(View view) {
    // Signing out clears the current authentication state and resets the default user,
    // this should be used to "switch users" without fully un-linking the user's google
    // account from your application.
    mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        updateUI(null);
      }
    });
  }

  private void updateUI(@Nullable GoogleSignInAccount account) {
    if (account != null) {
      tv.setText("Logget ind med " + account);

    } else {
      tv.setText("Logget ud");

    }
  }
}
