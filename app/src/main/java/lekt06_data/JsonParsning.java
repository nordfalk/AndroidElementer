package lekt06_data;

import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;
import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class JsonParsning extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView tv = new TextView(this);

    try {
      JSONObject obj = new JSONObject("{ navn: 'Jacob', efternavn: 'Nordfalk' }");
      tv.append("Jacobs efternavn: " + obj.getString("efternavn") );
      tv.append("\n\nHele JSON-objektet: " + obj.toString(2) );

      tv.append("\n\nEksempel på parsning fra en fil: ");
      InputStream is = getResources().openRawResource(R.raw.data_jsoneksempel);
      // Vi kunne hente over netværket (men det kræver at vi er i en baggrundstråd)
      //InputStream is = new URL("https://javabog.dk/eksempel.json").openStream();

      byte b[] = new byte[is.available()]; // kun små filer
      is.read(b);
      String str = new String(b, "UTF-8");
      tv.append(str);

      JSONObject json = new JSONObject(str);
      String bank = json.getString("bank");
      tv.append("\n\n=== Oversigt over " + bank + "s kunder ===\n");
      double totalKredit = 0;

      JSONArray kunder = json.getJSONArray("kunder");
      int antal = kunder.length();
      for (int i = 0; i < antal; i++) {
        JSONObject kunde = kunder.getJSONObject(i);
        System.err.println("obj = " + kunde);
        String navn = kunde.getString("navn");
        double kredit = kunde.getDouble("kredit");
        tv.append(navn + " med " + kredit + " kr.\n");
        totalKredit = totalKredit + kredit;
      }
      tv.append("\n\nTotal kredit er " + totalKredit + " kr.");

    } catch (Exception ex) {
      ex.printStackTrace();
      tv.append("FEJL:" + ex.toString());
    }

    setContentView(tv);
  }
}
