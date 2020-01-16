package lekt03_diverse;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class BenytDialogerOgToasts extends AppCompatActivity implements OnClickListener {

  Button visStandardToast, visToastMedBillede, visSnackBar, visAlertDialog, visAlertDialog1, visAlertDialog2, visAlertDialogListe, visProgressDialog, visProgressDialogMedBillede;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    visStandardToast = new Button(this);
    visStandardToast.setText("vis Standard Toast");
    tl.addView(visStandardToast);

    visToastMedBillede = new Button(this);
    visToastMedBillede.setText("vis Toast Med Billede");
    tl.addView(visToastMedBillede);

    visSnackBar = new Button(this);
    visSnackBar.setText("vis SnackBar");
    tl.addView(visSnackBar);

    visAlertDialog = new Button(this);
    visAlertDialog.setText("vis AlertDialog");
    tl.addView(visAlertDialog);

    visAlertDialog1 = new Button(this);
    visAlertDialog1.setText("vis AlertDialog med 1 knap");
    tl.addView(visAlertDialog1);

    visAlertDialog2 = new Button(this);
    visAlertDialog2.setText("vis AlertDialog med 2 knapper");
    tl.addView(visAlertDialog2);

    visAlertDialogListe = new Button(this);
    visAlertDialogListe.setText("vis AlertDialog med 2 knapper");
    tl.addView(visAlertDialogListe);

    visProgressDialog = new Button(this);
    visProgressDialog.setText("vis Progress Dialog");
    tl.addView(visProgressDialog);

    visProgressDialogMedBillede = new Button(this);
    visProgressDialogMedBillede.setText("vis ProgressDialog Med Billede");
    tl.addView(visProgressDialogMedBillede);

    visStandardToast.setOnClickListener(this);
    visToastMedBillede.setOnClickListener(this);
    visProgressDialog.setOnClickListener(this);
    visProgressDialogMedBillede.setOnClickListener(this);
    visSnackBar.setOnClickListener(this);
    visAlertDialog.setOnClickListener(this);
    visAlertDialog1.setOnClickListener(this);
    visAlertDialog2.setOnClickListener(this);

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
    } else if (hvadBlevDerKlikketPå == visSnackBar) {
      // Bemærk - kræver at designbiblioteket er med i build.gradle - f.eks. med
      // compile 'com.android.support:design:25.3.1'
      Snackbar.make(hvadBlevDerKlikketPå, "En kort Snackbar", Snackbar.LENGTH_LONG).setAction("Vis en mere", new OnClickListener() {
        @Override
        public void onClick(View v) {
          Snackbar.make(visSnackBar, "OK, her er en lang snackbar", Snackbar.LENGTH_SHORT).show();
        }
      }).show();
    } else if (hvadBlevDerKlikketPå == visAlertDialog) {
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("En AlertDialog");
      dialog.setMessage("Denne her har ingen knapper");
      dialog.show();
    } else if (hvadBlevDerKlikketPå == visAlertDialog1) {
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("En AlertDialog");
      dialog.setIcon(R.drawable.logo);
      dialog.setMessage("Denne her har én knap");
      dialog.setPositiveButton("Vis endnu en toast", new AlertDialog.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {
          Toast.makeText(BenytDialogerOgToasts.this, "Standard-toast", Toast.LENGTH_LONG).show();
        }
      });
      dialog.show();
    } else if (hvadBlevDerKlikketPå == visAlertDialog2) {
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("En AlertDialog");
      EditText et = new EditText(this);
      et.setText("Denne her viser et generelt view og har to knapper");
      dialog.setView(et);
      dialog.setPositiveButton("Vis endnu en toast", new AlertDialog.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {
          Toast.makeText(BenytDialogerOgToasts.this, "Endnu en standard-toast", Toast.LENGTH_LONG).show();
        }
      });
      dialog.setNegativeButton("Nej tak", null);
      dialog.show();

    } else if (hvadBlevDerKlikketPå == visAlertDialogListe) {
      final String[] lande = {"Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Tyskland",
              "Frankrig", "Spanien", "Portugal", "Nepal", "Indien", "Kina", "Japan", "Thailand"};

      AlertDialog dialog = new AlertDialog.Builder(this)
              .setTitle("Vælg en enhed")
              .setItems(lande, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  Snackbar.make(visAlertDialog2, "Du valgte "+lande[which], Snackbar.LENGTH_LONG).show();
                }
              }).show();

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
          Toast.makeText(BenytDialogerOgToasts.this, "Annulleret", Toast.LENGTH_LONG).show();
        }
      });
      dialog.show();
    }
  }
}
