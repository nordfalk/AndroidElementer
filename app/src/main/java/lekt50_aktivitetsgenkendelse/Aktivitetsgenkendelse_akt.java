package lekt50_aktivitetsgenkendelse;

import androidx.appcompat.app.AppCompatActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import lekt04_arkitektur.MinApp;
import lekt50_googlested.TekstTilTale;

/**
 * @author Jacob Nordfalk
 */
public class Aktivitetsgenkendelse_akt extends AppCompatActivity implements OnClickListener {
  PendingIntent pendingIntent;

  Button knap1, knap2, knap3;
  TextView tv;
  ActivityRecognitionClient activityRecognitionClient;

  static Aktivitetsgenkendelse_akt instans;
  static StringBuilder log = new StringBuilder();

  static void log(String s) {
    if (instans != null) instans.tv.append(s + "\n");
    log.append(s + "\n");
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);
    tv = new TextView(this);
    tv.setText("Detektering af brugerens aktiviteter\n");
    tv.append(log); // vis evt gammel log
    tl.addView(tv);

    knap1 = new Button(this);
    knap1.setText("Start detektering");
    tl.addView(knap1);

    knap2 = new Button(this);
    knap2.setText("Stop detektering");
    tl.addView(knap2);

    knap3 = new Button(this);
    knap3.setText("System.exit()");
    tl.addView(knap3);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);


    Intent intent = new Intent(this, Aktivitetsgenkendelse_reciever.class);
    pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

    activityRecognitionClient = ActivityRecognition.getClient(this);
    TekstTilTale.instans(this);
    instans = this;

  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    instans = null;
  }


  @Override
  public void onClick(View klikPåHvad) {
    try {
      if (klikPåHvad == knap1) {
        Task task = activityRecognitionClient.requestActivityUpdates(10000L, pendingIntent);
        task.addOnCompleteListener(new OnCompleteListener() {
          @Override
          public void onComplete(@NonNull Task task) {
            log("Aktivitetsgenkendelse startet: "+task.isSuccessful());
          }
        });
      } else if (klikPåHvad == knap2) {
        Task task = activityRecognitionClient.removeActivityUpdates(pendingIntent);
      } else if (klikPåHvad == knap3) {
        finish();
        System.exit(0);
      }
    } catch (Throwable t) {
      t.printStackTrace();
      MinApp.langToast("Fejl: "+t);
    }
  }

}
