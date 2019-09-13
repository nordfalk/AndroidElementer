package lekt50_aktivitetsgenkendelse;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.Date;

import dk.nordfalk.android.elementer.R;
import lekt03_diverse.VisNotifikation;
import lekt50_googlested.TekstTilTale;


/**
 * Created by j on 10-11-13.
 */
public class Aktivitetsgenkendelse_reciever extends BroadcastReceiver {
  private String forrigeTekst;

  @Override
  public void onReceive(Context context, Intent intent) {

    if (ActivityRecognitionResult.hasResult(intent)) {

      ActivityRecognitionResult res = ActivityRecognitionResult.extractResult(intent);

      DetectedActivity mostProbableActivity = res.getMostProbableActivity();
      String aktivitetsnavn = getBeskrivelse(mostProbableActivity.getType());

      String tekst = aktivitetsnavn+" med "+ mostProbableActivity.getConfidence() +" procents sandsynlighed";
      if (!tekst.equals(forrigeTekst)) {
        TekstTilTale.instans(context).tal(tekst); // læs kun ændringer op
        forrigeTekst = tekst;
      }

      Aktivitetsgenkendelse_akt.log("\n" + new Date());
      for (DetectedActivity a : res.getProbableActivities()) {
        Aktivitetsgenkendelse_akt.log(a.getType() + ":" + getBeskrivelse(a.getType())+" "+ +a.getConfidence()+ "%");
      }
      if (Aktivitetsgenkendelse_akt.instans == null) {
        // Vis en notifikation så man kan komme hen og slå aktivitetsgenkendelse fra
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, Aktivitetsgenkendelse_akt.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, VisNotifikation.opretNotifKanal(context))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.bil)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo))
                .setContentTitle("Aktivitetsgenkendelse")
                .setContentText(tekst);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(42, builder.build());
      }
    }
  }

  private String getBeskrivelse(int activityType) {
    switch (activityType) {
      case DetectedActivity.IN_VEHICLE:
        return "Du kører bil";
      case DetectedActivity.ON_BICYCLE:
        return "Du cykler";
      case DetectedActivity.ON_FOOT:
        return "Du er til fods";
      case DetectedActivity.WALKING:
        return "Du går";
      case DetectedActivity.STILL:
        return "Telefonen ligger stille";
      case DetectedActivity.TILTING:
        return "Du holder telefonen";
      case DetectedActivity.UNKNOWN:
    }
    return "Ukendt aktivitet";
  }
}
