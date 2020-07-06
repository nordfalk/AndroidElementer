package lekt03_diverse;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;

import dk.nordfalk.android.elementer.R;
import lekt05_grafik.Tegneprogram;

/**
 * @author Jacob Nordfalk
 */
public class VisNotifikation extends AppCompatActivity implements OnClickListener {

  Button visNoitifikation, lukNotifikation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    visNoitifikation = new Button(this);
    visNoitifikation.setText("Vis noitifikation");
    tl.addView(visNoitifikation);

    lukNotifikation = new Button(this);
    lukNotifikation.setText("Fjern noitifikation igen");
    tl.addView(lukNotifikation);

    visNoitifikation.setOnClickListener(this);
    lukNotifikation.setOnClickListener(this);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);
  }

  public void onClick(View klikPåHvad) {
    if (klikPåHvad == visNoitifikation) {
      Intent intent = new Intent(this, Tegneprogram.class);
      NotificationCompat.Builder builder = new NotificationCompat.Builder(this, opretNotifKanal(this))
              .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
              .setSmallIcon(R.drawable.bil)
              .setSubText("Tekst øverst til højre for ikonet")
              .setContentTitle("Tekst der vises hele tiden")
              .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
              .setContentText("Tekst når notif ikke er ekspanderet. Kun 1 linje, resten skæres væk")
              .setStyle(new NotificationCompat.BigTextStyle()
                      .bigText("Tekst når notifikationen er ekspanderet. Teksten kan strække sig over flere linjer. \nBla bla bla en lang tekst over flere linjer. Bla bla bla en lang tekst over flere linjer. Bla bla bla en lang tekst over flere linjer. "))
              .setVibrate(new long[]{0, 100, 300, 400, 500, 510, 550, 560, 600, 610, 650, 610, -1});

      NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
      notificationManager.notify(42, builder.build());
    } else if (klikPåHvad == lukNotifikation) {
      NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
      notificationManager.cancel(42);
    }
  }


  public static String opretNotifKanal(Context ctx) {
    String KANALID = "kanal_id";
    // Fra API 26 skal man bruge en NotificationChannel
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel kanal = new NotificationChannel(KANALID, "kanalnavn", NotificationManager.IMPORTANCE_DEFAULT);
      kanal.setDescription("En notifikationskanal for AndroidElementer (setDescription)");
      ctx.getSystemService(NotificationManager.class).createNotificationChannel(kanal);
    }
    return KANALID;
  }
}
