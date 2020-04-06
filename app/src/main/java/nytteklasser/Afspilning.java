package nytteklasser;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class Afspilning {
  private static MediaPlayer mp;

  /**
   * Sætter volumen lidt op under afspilning af lyd, hvis der er helt skruet ned
   * Minimumsværdi er angivet i procenter af fuld styrke
   * @param ctx Kontekst, f.eks. aktivitet (huskes ikke)
   * @param procent værdi mellem 0 og 100
   */
  public static void tjekVolumenErMindst(Context ctx, int procent) {
    AudioManager audioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);

    int aktuelVærdi = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    int maxVærdi = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    if (aktuelVærdi < procent * maxVærdi / 100) {
      audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, procent * maxVærdi / 100, AudioManager.FLAG_SHOW_UI);
    }
  }


  public static void start(MediaPlayer mp1) {
    start(mp1, null);
  }

  public static void start(MediaPlayer mp1, final MediaPlayer.OnCompletionListener ocl) {
    stop();
    try {
      if (mp1 == null) return;
      mp = mp1;
      mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mpx) {
          try {
            if (ocl != null) ocl.onCompletion(null);
            mp.reset();
            mp.release();
            mp = null;
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
      mp.start();
    } catch (Exception e) {
      e.printStackTrace();  // fang alle fejl og log dem, men lad være at crashe app'en
    }
  }

  public static void stop() {
    if (mp != null) try {
      mp.stop(); // Stop tidligere afspilning
      mp.reset();
      mp.release();
      mp = null;
    } catch (Exception e) {
      e.printStackTrace();  // fang alle fejl og log dem, men lad være at crashe app'en
    }
  }
}
