package lekt02_intents;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Jacob Nordfalk
 */
public class BenytIntentsMedTilladelser extends AppCompatActivity implements OnClickListener {
  EditText nummerfelt;
  Button ringOpDirekte, info;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    TextView tv = new TextView(this);
    tv.setText("Brugeren skal spørges om lov først før visse handlinger kan udføres.\n\n");
    tl.addView(tv);

    nummerfelt = new EditText(this);
    nummerfelt.setHint("Skriv telefonnummer her");
    nummerfelt.setInputType(InputType.TYPE_CLASS_PHONE);
    tl.addView(nummerfelt);

    ringOpDirekte = new Button(this);
    ringOpDirekte.setText("Ring op - direkte");
    tl.addView(ringOpDirekte);

    info = new Button(this);
    info.setText("Info om runtime tilladelser");
    tl.addView(info);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);


    ringOpDirekte.setOnClickListener(this);
  }


  public void onClick(View klikPåHvad) {
    if (klikPåHvad == info) {
      startActivity(new Intent(Intent.ACTION_VIEW,
              Uri.parse("https://developer.android.com/training/permissions/requesting")));
      return;
    }


    String nummer = nummerfelt.getText().toString();
    nummerfelt.setError(null);
    if (nummer.length() == 0) {
      nummerfelt.setError("Skriv et telefonnummer");
      Toast.makeText(this, "Skriv et telefonnummer", Toast.LENGTH_LONG).show();
      return;
    }

    // Kræver <uses-permission android:name="android.permission.CALL_PHONE" /> i manifestet.
    try {
      if (klikPåHvad == ringOpDirekte) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
          if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            Toast.makeText(this, "Her skal vises et rationale/forklaring: ...", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Giv tilladelse for at eksemplet virker :-)", Toast.LENGTH_LONG).show();
          }
          ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 12345);
        } else {
          startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + nummer)));
        }
      } else if (klikPåHvad == info) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com/training/permissions/index.html"));
        startActivity(intent);
      }
    } catch (Exception e) {
      Toast.makeText(this, "Du mangler vist <uses-permission android:name=\"android.permission.CALL_PHONE\" /> i manifestet\n" + e, Toast.LENGTH_LONG).show();
      e.printStackTrace();
    }
  }


  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode != 12345) return; // ikke vores requestCode
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
      Snackbar.make(ringOpDirekte, "Du har afvist at give tilladelser", Snackbar.LENGTH_SHORT).show();
      return;
    }
    String nummer = nummerfelt.getText().toString();
    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + nummer)));
  }
}
