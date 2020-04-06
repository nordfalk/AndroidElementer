package lekt09_recievers;

import androidx.appcompat.app.AppCompatActivity;
import dk.nordfalk.android.elementer.R;
import nytteklasser.Afspilning;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Jacob Nordfalk
 */
public class BenytAlarmer extends AppCompatActivity implements OnClickListener {

  Button knap1, knapStop;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);
    TextView tv = new TextView(this);
    tv.setText("Eksempel på brug af AlarmManager til regelmæssigt at blive vækket");
    tl.addView(tv);

    knap1 = new Button(this);
    knap1.setText("Start regelmæssig alarm");
    tl.addView(knap1);

    knapStop = new Button(this);
    knapStop.setText("Stop regelmæssig alarm");
    tl.addView(knapStop);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);

    knap1.setOnClickListener(this);
    knapStop.setOnClickListener(this);
  }

  public void onClick(View klikketPå) {
    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    Intent intent = new Intent(this, AlarmReciever.class);
    PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

    if (getPackageManager().queryBroadcastReceivers(intent, 0).size()==0) {
      String fejl = "Du mangler at definere "+intent.getComponent().toShortString()+" i manifestet";
      System.out.println(fejl);
      new AlertDialog.Builder(this).setTitle("Mangel i manifestet").setMessage(fejl).show();
      return;
    }


    if (klikketPå == knap1) {
//      am.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + 1000, 60000, alarmIntent);

      alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
              SystemClock.elapsedRealtime() +
                      5 * 1000, alarmIntent);
      finish();
      /*
      alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
              SystemClock.elapsedRealtime() + 1000,
              15*1000, alarmIntent);

       */
    } else if (klikketPå == knapStop) {
      alarmManager.cancel(alarmIntent);
    }
  }


  public static class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent i) {
      System.out.println("AndroidElementer AlarmReciever onReceive" + ctx + ":\n" + i);
      Toast.makeText(ctx, "AndroidElementer AlarmReciever onReceive", Toast.LENGTH_LONG).show();
      ctx.startActivity(new Intent(ctx, BenytAlarmer.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

      // Tjek også hvad lydstyrken er lige nu og hvis lyden er lavere end 1/5 af fuld lydstyrke
      // så skru volumen og til 1/5 af fuld lydstyrke
      Afspilning.tjekVolumenErMindst(ctx, 20);
      Afspilning.start(MediaPlayer.create(ctx, R.raw.dyt));
    }
  }

}
