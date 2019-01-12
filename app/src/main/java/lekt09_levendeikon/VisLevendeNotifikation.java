package lekt09_levendeikon;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.Date;

import dk.nordfalk.android.elementer.R;
import lekt03_diverse.BenytNotifikation;
import lekt05_grafik.Tegneprogram;

/**
 * @author Jacob Nordfalk
 */
public class VisLevendeNotifikation extends AppCompatActivity implements OnClickListener {

  Button visStandardToast, visToastMedBillede, visProgressDialog, visProgressDialogMedBillede, visNoitifikation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    visStandardToast = new Button(this);
    visStandardToast.setText("vis Standard Toast");
    tl.addView(visStandardToast);


    visNoitifikation = new Button(this);
    visNoitifikation.setText("vis Noitifikation");
    tl.addView(visNoitifikation);

    visStandardToast.setOnClickListener(this);
    visNoitifikation.setOnClickListener(this);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    if (hvadBlevDerKlikketPå == visStandardToast) {
      Toast.makeText(this, "Standard-toast", Toast.LENGTH_LONG).show();
    } else if (hvadBlevDerKlikketPå == visToastMedBillede) {
      Toast t = new Toast(this);
      ImageView im = new ImageView(this);
      im.setImageResource(R.drawable.logo);
      im.setAlpha(180);
      t.setView(im);
      t.setGravity(Gravity.CENTER, 0, 0);
      t.show();
    } else if (hvadBlevDerKlikketPå == visProgressDialog) {
      ProgressDialog.show(this, "", "En ProgressDialog", true).setCancelable(true);
    } else if (hvadBlevDerKlikketPå == visProgressDialogMedBillede) {
      ProgressDialog dialog = new ProgressDialog(this);
      dialog.setIndeterminate(true); // drejende hjul
      dialog.setTitle("En ProgressDialog");
      dialog.setIcon(R.drawable.logo);
      dialog.setMessage("hej herfra");
      dialog.setOnCancelListener(new OnCancelListener() {

        public void onCancel(DialogInterface dialog) {
          Toast.makeText(VisLevendeNotifikation.this, "Annulleret", Toast.LENGTH_LONG).show();
        }
      });
      dialog.show();
    } else if (hvadBlevDerKlikketPå == visNoitifikation) {
      Intent intent = new Intent(this, Tegneprogram.class);
      PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

      NotificationCompat.Builder builder = new NotificationCompat.Builder(this, BenytNotifikation.opretNotifKanal(this))
              .setContentIntent(pi)
              .setSmallIcon(R.drawable.bil)
              .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
              .setTicker("Der skal tegnes!")
              .setContentTitle("Tegn!")
              .setContentText("Du er nødt til at tegne lidt")
              .setSubText("Bla bla bla og en længere forklaring");


      RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.lekt09_levendeikon_visklokken);

      remoteViews.setTextViewText(R.id.etTextView, "Klokken er:\n" + new Date());
      // Vis en tilfældig farve på TextViewet
      int farve = (int) System.currentTimeMillis() | 0xff0000ff;
      remoteViews.setTextColor(R.id.etTextView, farve);

      // generisk måde at gøre det samme på
      remoteViews.setInt(R.id.etTextView, "setTextColor", farve);

      // Lav et intent der skal affyres hvis knapppen trykkes
      Intent tegneIntent = new Intent(this, Tegneprogram.class);
      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, tegneIntent, 0);
      remoteViews.setOnClickPendingIntent(R.id.enKnap, pendingIntent);

      builder.setCustomContentView( remoteViews );

      NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
      notificationManager.notify(42, builder.build());


      // Lad uret opdatere hvert sekund i det næste minut. Burde gøres fra en service...
      new Thread() {
        @Override
        public void run() {
          for (int i = 0; i < 60; i++) {
            SystemClock.sleep(1000);
            VisKlokkenIkon.opdaterIkoner(getApplicationContext());
          }
        }
      }.start();



    }
  }
}
