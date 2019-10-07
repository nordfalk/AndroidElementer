package lekt07_lister2;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import dk.nordfalk.android.elementer.R;

public class BenytRecyclerview extends AppCompatActivity {

  String[] landeArray = {"Danmark", "Norge", "Sverige", "Island", "Færøerne", "Finland",
          "Tyskland", "Østrig", "Belgien", "Holland", "Italien", "Grækenland",
          "Frankrig", "Spanien", "Portugal", "Nepal", "Indien", "Kina", "Japan", "Thailand"};
  // Vi laver en arrayliste så vi kan fjerne/indsætte elementer
  ArrayList<String> lande = new ArrayList<>(Arrays.asList(landeArray));

  ListeelemAdapter listeelemAdapter = new ListeelemAdapter();
  RecyclerView recyclerView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    recyclerView = new RecyclerView(this);

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    //recyclerView.setOnItemClickListener(this); FINDES IKKE - i stedet skal man lytte efter onClick på de enkelte vieww
    recyclerView.setAdapter(listeelemAdapter);

    setContentView(recyclerView);


    Snackbar.make(recyclerView, "Tryk en titel for at flytte et element til toppen " +
            "eller på billedet for at fjerne det", Snackbar.LENGTH_INDEFINITE)
            .setAction("Skift\nlayout", new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                skiftLayoutManager();
              }
            }).show();
  }


  class ListeelemAdapter extends RecyclerView.Adapter<ListeelemViewholder> {
    @Override
    public int getItemCount()  {
      return lande.size();
    }

    @Override
    public ListeelemViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
      View listeelementViews = getLayoutInflater().inflate(R.layout.lekt04_listeelement, parent, false);
      ListeelemViewholder vh = new ListeelemViewholder(listeelementViews);
      return vh;
    }

    @Override
    public void onBindViewHolder(ListeelemViewholder vh, int position) {
      vh.overskrift.setText(lande.get(position));
      if (position>0) vh.overskrift.append(" (flyt op)");

      vh.beskrivelse.setText("Land nummer " + position + " på vh@"+Integer.toHexString(vh.hashCode()));
      if (position % 3 == 2) {
        vh.billede.setImageResource(android.R.drawable.ic_menu_delete);
      } else {
        vh.billede.setImageResource(android.R.drawable.ic_delete);
      }
    }
  };


  /**
   * En Viewholder holder referencer til de forskellige views i et listeelement,
   * sådan at søgninger i viewhierakiet med findViewById() kun behøver at ske EN gang.
   * Se https://developer.android.com/guide/topics/ui/layout/recyclerview
   */
  class ListeelemViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView overskrift;
    TextView beskrivelse;
    ImageView billede;

    public ListeelemViewholder(View listeelementViews) {
      super(listeelementViews);
      overskrift = listeelementViews.findViewById(R.id.listeelem_overskrift);
      beskrivelse = listeelementViews.findViewById(R.id.listeelem_beskrivelse);
      billede = listeelementViews.findViewById(R.id.listeelem_billede);
      // Gør listeelementer klikbare og vis det ved at deres baggrunsfarve ændrer sig ved berøring
      overskrift.setBackgroundResource(android.R.drawable.list_selector_background);
      beskrivelse.setBackgroundResource(android.R.drawable.list_selector_background);
      billede.setBackgroundResource(android.R.drawable.list_selector_background);
      overskrift.setOnClickListener(this);
      beskrivelse.setOnClickListener(this);
      billede.setOnClickListener(this);
    }


    /**
     * Håndtering af klik på de forskellige views i listeelementet.
     * Metoden kan fjernes hvis du ikke understøtter klik
     * @param v Viewet der blev klikket på
     */

    @Override
    public void onClick(View v) {
      final int position = getAdapterPosition();
      final String landenavn = lande.get(position);
      Toast.makeText(v.getContext(), "Klik på " + position + "/" + landenavn, Toast.LENGTH_SHORT).show();

      if (v == billede) { // Klik på billede fjerner landet fra listen
        lande.remove(position);
        listeelemAdapter.notifyItemRemoved(position);
        Snackbar.make(recyclerView, landenavn + " fjernet", Snackbar.LENGTH_INDEFINITE)
                .setAction("Fortryd", new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                    lande.add(position, landenavn);
                    listeelemAdapter.notifyItemInserted(position);
                    recyclerView.smoothScrollToPosition(position);
                    Snackbar.make(recyclerView, "OK, du får "+landenavn + " tilbage", Snackbar.LENGTH_LONG).show();
                  }
                }).show();
      }

      if (v == overskrift) { // Klik på overskrift flytter landet op til toppen
        lande.remove(position);
        lande.add(0, landenavn);
        listeelemAdapter.notifyItemMoved(position, 0);
        recyclerView.scrollToPosition(0);
      }

      if (v == beskrivelse) {
        skiftLayoutManager();
      }
    }
  }


  /**
   * Denne metode skifter LayoutManager og kan sagtens fjernes
   */
  int aktivLayoutManager;
  private void skiftLayoutManager() {
    aktivLayoutManager++;
    String aktivLayoutManagerTekst;

    if (aktivLayoutManager==1) {
      recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
      aktivLayoutManagerTekst = "GridLayoutManager\n2 søjler synkront";
    } else if (aktivLayoutManager == 2) {
      recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
      aktivLayoutManagerTekst = "StaggeredGridLayoutManager\n2 søjler (ikke synkront)";
    } else if (aktivLayoutManager == 3) {
      recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
      aktivLayoutManagerTekst = "LinearLayoutManager vandret";
    } else if (aktivLayoutManager == 4) {
      recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
      aktivLayoutManagerTekst = "LinearLayoutManager bagfra";
    } else {
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
      aktivLayoutManagerTekst = "Normal LinearLayoutManager";
      aktivLayoutManager = 0;
    }
    Snackbar.make(recyclerView, aktivLayoutManagerTekst, Snackbar.LENGTH_INDEFINITE).setAction("Skift", new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        skiftLayoutManager();
      }
    }).show();
  }

}
