/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lekt06_data;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import java.io.File;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @author Jacob Nordfalk
 */
public class BenytSQLite extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView textView = new TextView(this);
    textView.append("Herunder resultatet af en forespørgsel på en SQLite-database\n\n");
    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(textView);
    setContentView(scrollView);


    // Oprettelse af database
    File dbFil = new File(getFilesDir(), "/database.db");
    boolean databaseFandtes = dbFil.exists();

    SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFil, null);

    if (!databaseFandtes) {
      // Hvis filen ikke findes: Opret tabellerne via SQL
      //db.execSQL("DROP TABLE IF EXISTS kunder;");
      db.execSQL("CREATE TABLE kunder (_id INTEGER PRIMARY KEY, navn TEXT NOT NULL, kredit INTEGER);");
    }



    // Oprette en række
    ContentValues række = new ContentValues();
    række.put("navn", "Jacob Nordfalk");
    række.put("kredit", 500);
    db.insert("kunder", null, række);

    db.execSQL("INSERT INTO kunder (navn, kredit) VALUES ('Troels Nordfalk', 400);");

    // Søgning
    //Cursor cursor = db.rawQuery("SELECT * from kunder WHERE kredit > 100 ORDER BY kredit ASC;", null);
    String[] kolonner = {"_id", "navn", "kredit"};
    String valg = "kredit > 100"; // WHERE
    String sortering = "kredit ASC"; // ORDER BY
    Cursor cursor = db.query("kunder", kolonner, valg, null, null, null, sortering);

    while (cursor.moveToNext()) {
      long id = cursor.getLong(0);
      String navn = cursor.getString(1);
      int kredit = cursor.getInt(2);
      textView.append(id + "  " + navn + " " + kredit + "\n");
    }
    cursor.close();

    db.close();

  }
}
