package lekt09_services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;
import lekt03_diverse.BenytNotifikation;

/**
 * Simpel service der, når startet, forsøger at holde app'en i hukommelsen
 * BEMÆRK: Kræver android.permission.FOREGROUND_SERVICE tilladelsen
 * @author j
 */
public class ForgrundsService extends Service {
  final String TAG = getClass().getName();

  /**
   * Service-mekanik. Ligegyldig da vi kører i samme proces og ikke ønsker at binde til denne service
   */
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    Toast.makeText(this, TAG + " onCreate", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onDestroy() {
    Toast.makeText(this, TAG + " onDestroy", Toast.LENGTH_LONG).show();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Toast.makeText(this, TAG + " onStartCommand("+flags + " " + startId, Toast.LENGTH_LONG).show();
    Intent i = new Intent(this, BenytService.class);
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, BenytNotifikation.opretNotifKanal(this))
            .setContentIntent(PendingIntent.getActivity(this, 0, i, 0))
            .setSmallIcon(R.drawable.bil)
//            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
            .setTicker("AndroidElementer holdes i hukommelsen")
            .setContentTitle("Bliver i hukommelsen")
            .setContentText("Klik her for at stoppe servicen")
            ;
    startForeground(42, builder.build());
    return START_STICKY; // evt START_REDELIVER_INTENT hvis intentdata skal huskes i fald at processen bliver genstartet
  }
}
